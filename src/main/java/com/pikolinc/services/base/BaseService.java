package com.pikolinc.services.base;

import com.pikolinc.config.Database;
import org.jdbi.v3.core.Jdbi;

public class BaseService {
    protected final Jdbi jdbi;

    protected BaseService() {
        this.jdbi = Database.getJdbi();
    }

    protected <R, D> R withDao(Class<D> daoClass, DaoCallback<R, D> callback) {
        return jdbi.withExtension(daoClass, callback::execute);
    }

    @FunctionalInterface
    protected interface DaoCallback<R, D> {
        R execute(D dao);
    }
}
