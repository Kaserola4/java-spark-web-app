package com.pikolinc.dao;

import com.pikolinc.domain.Item;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;
import java.util.Optional;

@RegisterBeanMapper(Item.class)
public interface ItemDao {

    @SqlUpdate("""
                CREATE TABLE IF NOT EXISTS items (
                    id BIGINT AUTO_INCREMENT PRIMARY KEY,
                    name VARCHAR(255) NOT NULL,
                    description TEXT,
                    price DECIMAL(10, 2) NOT NULL
                )
            """)
    void createTable();


    @SqlUpdate("""
        INSERT INTO items (name, description, price) VALUES
        ('Laptop', 'High-performance laptop with 16GB RAM and 512GB SSD', 1299.99),
        ('Smartphone', 'Latest model with 5G support and 128GB storage', 899.99),
        ('Wireless Headphones', 'Noise-cancelling over-ear headphones with 30-hour battery', 249.99),
        ('Gaming Console', 'Next-gen gaming console with 4K graphics', 499.99),
        ('Smartwatch', 'Fitness tracker with heart rate monitor and GPS', 349.99)
    """)
    void insertSampleData();

    @SqlUpdate("INSERT INTO items (name, description, price) VALUES (:name, :description, :price)")
    @GetGeneratedKeys
    long insert(@BindBean Item item);

    @SqlQuery("SELECT * FROM items")
    List<Item> findAll();

    @SqlQuery("SELECT * FROM items WHERE id = :id")
    Optional<Item> findById(@Bind("id") Long id);

    @SqlUpdate("UPDATE items SET name = :name, description = :description, price = :price WHERE id = :id")
    long update(@BindBean Item item);

    @SqlUpdate("DELETE FROM items WHERE id = :id")
    long deleteById(@Bind("id") Long id);
}