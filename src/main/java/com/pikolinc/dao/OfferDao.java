package com.pikolinc.dao;

import com.pikolinc.domain.Offer;
import com.pikolinc.dto.response.OfferResponseDto;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;
import java.util.Optional;

@RegisterBeanMapper(OfferResponseDto.class)
public interface OfferDao {
    @SqlUpdate("""
                CREATE TABLE IF NOT EXISTS offers (
                    id BIGINT AUTO_INCREMENT PRIMARY KEY,
                    user_id BIGINT NOT NULL,
                    item_id BIGINT NOT NULL,
                    amount DECIMAL(10, 2) NOT NULL CHECK (amount > 0),
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    status VARCHAR(255) DEFAULT 'OPEN',
                    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
                    FOREIGN KEY (item_id) REFERENCES items(id) ON DELETE CASCADE
                )
            """)
    void createTable();

    @SqlUpdate("INSERT INTO offers (user_id, item_id, amount) VALUES (:userId, :itemId, :amount)")
    @GetGeneratedKeys
    long insert(@BindBean Offer offer);

    @SqlQuery("""
        SELECT
            o.id,
            o.user_id AS userId,
            u.name AS userName,
            u.email AS userEmail,
            o.item_id AS itemId,
            i.name AS itemName,
            i.description AS itemDescription,
            i.price AS itemPrice,
            o.amount,
            o.created_at AS createdAt,
            o.status
        FROM offers o
        INNER JOIN users u ON o.user_id = u.id
        INNER JOIN items i ON o.item_id = i.id
        WHERE o.id = :id
    """)
    Optional<OfferResponseDto> findById(@Bind("id") Long id);

    @SqlUpdate("UPDATE offers SET user_id = :userId, item_id = :itemId, amount = :amount, status = :status WHERE id = :id")
    long update(@BindBean Offer offer);

    @SqlQuery("""
        SELECT 
            o.id,
            o.user_id AS userId,
            u.name AS userName,
            u.email AS userEmail,
            o.item_id AS itemId,
            i.name AS itemName,
            i.description AS itemDescription,
            i.price AS itemPrice,
            o.amount,
            o.created_at AS createdAt,
            o.status
        FROM offers o
        INNER JOIN users u ON o.user_id = u.id
        INNER JOIN items i ON o.item_id = i.id
        WHERE o.item_id = :itemId
        ORDER BY o.amount DESC
    """)
    List<OfferResponseDto> findByItemId(@Bind("itemId") Long itemId);

    @SqlQuery("""
        SELECT 
            o.id,
            o.user_id AS userId,
            u.name AS userName,
            u.email AS userEmail,
            o.item_id AS itemId,
            i.name AS itemName,
            i.description AS itemDescription,
            i.price AS itemPrice,
            o.amount,
            o.created_at AS createdAt,
            o.status
        FROM offers o
        INNER JOIN users u ON o.user_id = u.id
        INNER JOIN items i ON o.item_id = i.id
        WHERE o.user_id = :userId
    """)
    List<OfferResponseDto> findByUserId(@Bind("userId") Long userId);

    @SqlQuery("""
        SELECT 
            o.id,
            o.user_id AS userId,
            u.name AS userName,
            u.email AS userEmail,
            o.item_id AS itemId,
            i.name AS itemName,
            i.description AS itemDescription,
            i.price AS itemPrice,
            o.amount,
            o.created_at AS createdAt,
            o.status
        FROM offers o
        INNER JOIN users u ON o.user_id = u.id
        INNER JOIN items i ON o.item_id = i.id
    """)
    List<OfferResponseDto> findAll();

    @SqlUpdate("DELETE FROM offers WHERE id = :id")
    long deleteById(@Bind("id") Long id);

    @SqlUpdate("UPDATE offers SET amount = :amount WHERE id = :id")
    long updateAmount(@Bind("id") Long id, @Bind("amount") Double amount);

    @SqlQuery("""
        SELECT 
            o.id,
            o.user_id AS userId,
            u.name AS userName,
            u.email AS userEmail,
            o.item_id AS itemId,
            i.name AS itemName,
            i.description AS itemDescription,
            i.price AS itemPrice,
            o.amount,
            o.created_at AS createdAt,
            o.status
        FROM offers o
        INNER JOIN users u ON o.user_id = u.id
        INNER JOIN items i ON o.item_id = i.id
        WHERE o.item_id = :itemId AND o.status = :status
        ORDER BY o.amount DESC
    """)
    List<OfferResponseDto> findByItemIdAndStatus(@Bind("itemId") Long itemId, @Bind("status") String status);

    @SqlQuery("""
        SELECT 
            o.id,
            o.user_id AS userId,
            u.name AS userName,
            u.email AS userEmail,
            o.item_id AS itemId,
            i.name AS itemName,
            i.description AS itemDescription,
            i.price AS itemPrice,
            o.amount,
            o.created_at AS createdAt,
            o.status
        FROM offers o
        INNER JOIN users u ON o.user_id = u.id
        INNER JOIN items i ON o.item_id = i.id
        WHERE o.user_id = :userId AND o.status = :status
    """)
    List<OfferResponseDto> findByUserIdAndStatus(@Bind("userId") Long userId, @Bind("status") String status);

    @SqlQuery("""
        SELECT 
            o.id,
            o.user_id AS userId,
            u.name AS userName,
            u.email AS userEmail,
            o.item_id AS itemId,
            i.name AS itemName,
            i.description AS itemDescription,
            i.price AS itemPrice,
            o.amount,
            o.created_at AS createdAt,
            o.status
        FROM offers o
        INNER JOIN users u ON o.user_id = u.id
        INNER JOIN items i ON o.item_id = i.id
        WHERE o.status = :status
    """)
    List<OfferResponseDto> findByStatus(@Bind("status") String status);

    @SqlUpdate("UPDATE offers SET status = :status WHERE id = :id")
    long updateStatus(@Bind("id") Long id, @Bind("status") String status);
}