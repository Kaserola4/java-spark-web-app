package com.pikolinc.services.impl;

import com.pikolinc.domain.dao.UserDao;
import com.pikolinc.domain.model.User;
import com.pikolinc.services.UserService;
import com.pikolinc.services.base.BaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

public class UserServiceImpl extends BaseService implements UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Override
    public long insert(User user) {
        return withDao(UserDao.class, dao -> dao.insert(user));
    }

    @Override
    public List<User> findAll() {
        return withDao(UserDao.class, UserDao::findAll);
    }

    @Override
    public Optional<User> findById(long id) {
        return withDao(UserDao.class, dao -> dao.findById(id));
    }

    @Override
    public long update(User user) {
        return withDao(UserDao.class, dao -> dao.update(user));
    }

    @Override
    public long delete(long id) {
        return withDao(UserDao.class, dao -> dao.deleteById(id));
    }
}
