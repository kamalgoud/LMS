package com.varthana.user.service;

import com.varthana.user.entity.User;

public interface UserService {
    public User getUserByEmail(String email);

    public User saveUser(User user);
}
