package com.pikolinc.services.impl;

import com.pikolinc.dao.UserDao;
import com.pikolinc.dto.request.UserCreateDto;
import com.pikolinc.domain.User;
import com.pikolinc.exceptions.api.ApiResourceNotFoundException;
import com.pikolinc.exceptions.api.DuplicateResourceException;
import com.pikolinc.services.UserService;
import com.pikolinc.services.base.BaseService;
import com.pikolinc.util.ValidationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class UserServiceImpl extends BaseService implements UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Override
    public long insert(UserCreateDto dto) {
        // Validate DTO
        ValidationUtil.validate(dto);

        // Check for duplicate email and insert
        return withDao(UserDao.class, dao -> {
            // Check if email already exists
            if (dao.existsByEmail(dto.email())) {
                logger.warn("Attempted to create user with duplicate email: {}", dto.email());
                throw new DuplicateResourceException("User", "email", dto.email());
            }

            // Create user entity
            User userEntity = new User();
            userEntity.setName(dto.name());
            userEntity.setEmail(dto.email());
            userEntity.setAge(dto.age());

            // Insert and get generated ID
            long userId = dao.insert(userEntity);
            logger.info("User created with id: {}", userId);

            return userId;
        });
    }

    @Override
    public List<User> findAll() {
        logger.info("Finding all users");
        return withDao(UserDao.class, UserDao::findAll);
    }

    @Override
    public User findById(long id) {
        logger.info("Finding user by id: {}", id);

        return withDao(UserDao.class, dao ->
                dao.findById(id)
                        .orElseThrow(() -> {
                            logger.warn("User not found with id: {}", id);
                            return new ApiResourceNotFoundException("User", id);
                        })
        );
    }

    @Override
    public long update(long id, UserCreateDto dto) {

        ValidationUtil.validate(dto);

        return withDao(UserDao.class, dao -> {
            User existingUser = dao.findById(id)
                    .orElseThrow(() -> new ApiResourceNotFoundException("User", id));

            if (!dto.email().equals(existingUser.getEmail()) &&
                    dao.existsByEmail(dto.email())) {
                logger.warn("Attempted to update user {} with duplicate email: {}", id, dto.email());
                throw new DuplicateResourceException("User", "email", dto.email());
            }

            existingUser.setName(dto.name());
            existingUser.setEmail(dto.email());
            existingUser.setAge(dto.age());

            long updated = dao.update(existingUser);
            logger.info("User updated with id: {}", id);

            return updated;
        });
    }

    @Override
    public long delete(long id) {
        logger.info("Deleting user with id: {}", id);

        return withDao(UserDao.class, dao -> {
            // Check if user exists before deleting
            if (dao.findById(id).isEmpty()) {
                logger.warn("Attempted to delete non-existent user with id: {}", id);
                throw new ApiResourceNotFoundException("User", id);
            }

            long deleted = dao.deleteById(id);
            logger.info("User deleted with id: {}", id);

            return deleted;
        });
    }
}