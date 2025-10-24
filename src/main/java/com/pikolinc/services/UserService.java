package com.pikolinc.services;

import com.pikolinc.domain.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    long insert(User user);
    List<User> findAll();
    Optional<User> findById(long id);
    long  update(User user);
    long delete(long id);
}
