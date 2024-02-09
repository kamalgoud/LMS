package com.varthana.user.service.serviceimpl;

import com.varthana.user.entity.User;
import com.varthana.user.repository.UserRepository;
import com.varthana.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public User getUserByEmail(String email) {
        try {
            User user = userRepository.findByEmail(email);
            return user;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public User saveUser(User user) {
        try {
            return userRepository.save(user);
        } catch (Exception e) {
            return null;
        }
    }
}
