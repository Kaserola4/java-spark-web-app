package com.pikolinc.config;


import com.pikolinc.domain.dao.UserDao;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;

public class Database {
    private static Jdbi jdbi;

    public static void init(){
        jdbi = Jdbi.create(
                "jdbc:h2:mem:spark;DB_CLOSE_DELAY=1",
                "sa",
                ""
        );

        jdbi.installPlugin(new SqlObjectPlugin());

        jdbi.useExtension(UserDao.class, UserDao::createTable);
    }

    public static Jdbi getJdbi(){
        return jdbi;
    }
}
