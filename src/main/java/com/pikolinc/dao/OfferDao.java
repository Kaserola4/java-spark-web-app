package com.pikolinc.dao;

import com.pikolinc.domain.Offer;
import com.pikolinc.domain.User;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;
import java.util.Optional;

@RegisterBeanMapper(Offer.class)
public interface OfferDao {

    @SqlUpdate("""
                CREATE TABLE IF NOT EXISTS offers (
                    id BIGINT AUTO_INCREMENT PRIMARY KEY,
                    user_id BIGINT NOT NULL,
                    item_id BIGINT NOT NULL,
                    amount DECIMAL(10, 2) NOT NULL CHECK (amount > 0),
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    status VARCHAR(255) DEFAULT "OPEN",
                    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
                    FOREIGN KEY (item_id) REFERENCES items(id) ON DELETE CASCADE
                )
            """)
    void createTable();

    @SqlUpdate("INSERT INTO offers (user_id, item_id, amount) VALUES (:userId, :itemId, :amount)")
    @GetGeneratedKeys
    long insert(@BindBean Offer offer);

    @SqlQuery("SELECT * FROM offers WHERE id = :id")
    Optional<Offer> findById(@Bind("id") Long id);

    @SqlUpdate("UPDATE offers SET user_id = :userId, item_id = :itemId, amount = :amount, status = :status WHERE id = :id")
    long update(@BindBean Offer offer);

    @SqlQuery("SELECT * FROM offers WHERE item_id = :itemId ORDER BY amount DESC")
    List<Offer> findByItemId(@Bind("itemId") Long itemId);

    @SqlQuery("SELECT * FROM offers WHERE user_id = :userId")
    List<Offer> findByUserId(@Bind("userId") Long userId);

    @SqlQuery("SELECT * FROM offers")
    List<Offer> findAll();

    @SqlUpdate("DELETE FROM offers WHERE id = :id")
    long deleteById(@Bind("id") Long id);

    @SqlUpdate("UPDATE offers SET amount = :amount WHERE id = :id")
    long updateAmount(@Bind("id") Long id, @Bind("amount") Double amount);
}
