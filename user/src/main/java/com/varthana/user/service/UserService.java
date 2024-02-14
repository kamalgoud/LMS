package com.varthana.user.service;

import com.varthana.user.entity.User;
import com.varthana.user.exception.CustomException;

public interface UserService {
    public User getUserByEmail(String email) throws CustomException;

    public User saveUser(User user) throws CustomException;
}
