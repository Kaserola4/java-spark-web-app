package com.pikolinc.app.initializer.config;


import com.pikolinc.app.initializer.Initializer;
import com.pikolinc.dao.UserDao;
import lombok.Getter;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;

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


    }
}
