package com.varthana.admin.service;

import com.varthana.admin.entity.Admin;

public interface AdminService {
    public Admin getAdminByEmail(String email);

    public Admin update(Admin admin);

    public Admin saveAdmin(Admin admin);
}
