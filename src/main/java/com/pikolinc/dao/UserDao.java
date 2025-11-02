package com.pikolinc.dao;

import com.pikolinc.domain.User;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;
import java.util.Optional;

@RegisterBeanMapper(User.class)
public interface UserDao {
    @SqlUpdate("""
                CREATE TABLE IF NOT EXISTS users (
                    id BIGINT AUTO_INCREMENT PRIMARY KEY,
                    name VARCHAR(255) NOT NULL,
                    email VARCHAR(255) UNIQUE NOT NULL,
                    age INT
                )
            """)
    void createTable();

    @SqlUpdate("""
        INSERT INTO users (name, email, age) VALUES
        ('John Doe', 'john.doe@example.com', 28),
        ('Jane Smith', 'jane.smith@example.com', 34),
        ('Mike Johnson', 'mike.johnson@example.com', 42),
        ('Sarah Williams', 'sarah.williams@example.com', 25),
        ('David Brown', 'david.brown@example.com', 31)
    """)
    void insertSampleData();



    @SqlUpdate("INSERT INTO users (name, email, age) VALUES (:name, :email, :age)")
    @GetGeneratedKeys
    long insert(@BindBean User user);

    @SqlQuery("SELECT * FROM users")
    List<User> findAll();

    @SqlQuery("SELECT * FROM users WHERE id = :id")
    Optional<User> findById(@Bind("id") Long id);

    @SqlUpdate("UPDATE users SET name = :name, email = :email, age = :age WHERE id = :id")
    long update(@BindBean User user);

    @SqlUpdate("DELETE FROM users WHERE id = :id")
    long deleteById(@Bind("id") Long id);

    @SqlQuery("SELECT COUNT(*) > 0 FROM users WHERE email = :email")
    boolean existsByEmail(@Bind("email") String email);
}


