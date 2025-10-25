package com.pikolinc.services.impl;

import com.pikolinc.dao.UserDao;
import com.pikolinc.dto.request.UserCreateDto;
import com.pikolinc.domain.User;
import com.pikolinc.error.api.ApiResourceNotFound;
import com.pikolinc.services.UserService;
import com.pikolinc.services.base.BaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

public class UserServiceImpl extends BaseService implements UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Override
    public long insert(UserCreateDto user) {
        User userEntity = new User();

        userEntity.setAge(user.age());
        userEntity.setName(user.name());
        userEntity.setEmail(user.email());

        logger.info("User created with id {}", userEntity.getId());
        return withDao(UserDao.class, dao -> dao.insert(userEntity));
    }

    @Override
    public List<User> findAll() {
        logger.info("Finding all users");
        return withDao(UserDao.class, UserDao::findAll);
    }

    @Override
    public User findById(long id) {
        Optional<User> user = withDao(UserDao.class, dao -> dao.findById(id));

        if  (user.isPresent()) return user.get();

        throw new ApiResourceNotFound("User with id "  + id + " not found");
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
