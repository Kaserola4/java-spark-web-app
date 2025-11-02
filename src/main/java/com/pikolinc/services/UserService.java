package com.pikolinc.services;

import com.pikolinc.dto.request.UserCreateDto;
import com.pikolinc.domain.User;

import java.util.List;

/**
 * Service interface for User operations.
 *
 * @see com.pikolinc.services.impl.UserServiceImpl
 */
public interface UserService {
    long insert(UserCreateDto dto);
    List<User> findAll();
    User findById(long id);
    long update(long id, UserCreateDto dto);
    long delete(long id);
    Object options(long id);
}
