package com.pikolinc.services;

import com.pikolinc.dto.request.UserCreateDto;
import com.pikolinc.domain.User;

import java.util.List;

public interface UserService {
    long insert(UserCreateDto user);
    List<User> findAll();
    User findById(long id);
    long update(User user);
    long delete(long id);
}
