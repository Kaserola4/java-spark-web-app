// dao/ItemDao.java
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

    @SqlUpdate("CREATE TABLE IF NOT EXISTS items (" +
            "id BIGINT AUTO_INCREMENT PRIMARY KEY, " +
            "name VARCHAR(255) NOT NULL, " +
            "description TEXT, " +
            "price DECIMAL(10, 2) NOT NULL)")
    void createTable();

    @SqlUpdate("INSERT INTO items (name, description, price) VALUES (:name, :description, :price)")
    @GetGeneratedKeys
    long insert(@BindBean Item item);

    @SqlQuery("SELECT * FROM items")
    List<Item> findAll();

    @SqlQuery("SELECT * FROM items WHERE id = :id")
    Optional<Item> findById(@Bind("id") Long id);

    @SqlUpdate("UPDATE items SET name = :name, description = :description, price = :price WHERE id = :id")
    long update(@BindBean Item item);

    @SqlQuery("DELETE * FROM items where id = :id")
    int deleteById(@Bind("id") Long id);
}