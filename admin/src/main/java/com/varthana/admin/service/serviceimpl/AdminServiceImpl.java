package com.varthana.admin.service.serviceimpl;

import com.varthana.admin.entity.Admin;
import com.varthana.admin.repository.AdminRepository;
import com.varthana.admin.service.AdminService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.slf4j.ILoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminServiceImpl implements AdminService {
    @Autowired
    private AdminRepository adminRepository;
    private Logger logger = LogManager.getLogger(AdminServiceImpl.class);

    @Override
    public Admin getAdminByEmail(String email) {
        try {
            Admin admin = adminRepository.findByEmail(email);
            return admin;
        } catch (Exception e) {
            logger.error("Error while getting admin by email : {}",e.getMessage());
            return null;
        }
    }

    @Override
    public Admin update(Admin admin) {
        try {
            Admin updatedAdmin = adminRepository.save(admin);
            return updatedAdmin;
        } catch (Exception e) {
            logger.error("Error while updating admin : {}",e.getMessage());
            return null;
        }
    }

    @Override
    public Admin saveAdmin(Admin admin) {
        try {
            return adminRepository.save(admin);
        } catch (Exception e) {
            logger.error("Error while saving admin : {}",e.getMessage());
            return null;
        }
    }
}
