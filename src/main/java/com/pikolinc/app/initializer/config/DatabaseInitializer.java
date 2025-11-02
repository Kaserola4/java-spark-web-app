package com.pikolinc.app.initializer.config;


import com.pikolinc.app.initializer.Initializer;
import com.pikolinc.dao.ItemDao;
import com.pikolinc.dao.OfferDao;
import com.pikolinc.dao.UserDao;
import lombok.Getter;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;

/**
 * Initializes the application's database connection and schema.
 *
 * <p>Creates a Jdbi instance (H2 in-memory by default) and installs SQL object plugin.
 * Then it ensures database tables exist and inserts sample data for users and items.
 *
 * @implNote The JDBI instance is exposed via a getter for service classes to use.
 */
public class DatabaseInitializer implements Initializer {
    @Getter
    private static Jdbi jdbi;

    @Override
    public void init(){
        jdbi = Jdbi.create(
                "jdbc:h2:mem:spark;DB_CLOSE_DELAY=-1",
                "sa",
                ""
        );

        jdbi.installPlugin(new SqlObjectPlugin());

        jdbi.useExtension(UserDao.class, UserDao::createTable);
        jdbi.useExtension(ItemDao.class, ItemDao::createTable);
        jdbi.useExtension(OfferDao.class,  OfferDao::createTable);

        jdbi.useExtension(UserDao.class,  UserDao::insertSampleData);
        jdbi.useExtension(ItemDao.class,  ItemDao::insertSampleData);


    }
}
