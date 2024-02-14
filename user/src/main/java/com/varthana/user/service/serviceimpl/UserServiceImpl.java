package com.varthana.user.service.serviceimpl;

import com.varthana.user.entity.User;
import com.varthana.user.exception.CustomException;
import com.varthana.user.repository.UserRepository;
import com.varthana.user.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    private Logger logger = LogManager.getLogger(UserServiceImpl.class);

    @Override
    public User getUserByEmail(String email) throws CustomException {
        try {
            User user = userRepository.findByEmail(email);
            logger.warn("getUserByEmail service : {}", email);
            return user;
        } catch (Exception e) {
            logger.error("error while getting user by email : {}", e.getMessage());
            throw new CustomException("Error while getting user by Email " + e.getMessage());
        }
    }

    @Override
    public User saveUser(User user) throws CustomException {
        try {
            logger.warn("saveUser service : {}", user.toString());
            return userRepository.save(user);
        } catch (Exception e) {
            logger.error("error while saving user : {}", e.getMessage());
            throw new CustomException("Error while saving user " + e.getMessage());
        }
    }
}
