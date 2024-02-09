package com.varthana.admin.service.serviceimpl;

import com.varthana.admin.entity.Admin;
import com.varthana.admin.repository.AdminRepository;
import com.varthana.admin.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminServiceImpl implements AdminService {
    @Autowired
    private AdminRepository adminRepository;
    @Override
    public Admin getAdminByEmail(String email) {
        Admin admin = adminRepository.findByEmail(email);
        return admin;
    }

    @Override
    public Admin update(Admin admin) {
        Admin updatedAdmin = adminRepository.save(admin);
        return updatedAdmin;
    }

    @Override
    public Admin saveAdmin(Admin admin) {
        return adminRepository.save(admin);
    }
}
