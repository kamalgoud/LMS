package com.varthana.admin.service;

import com.varthana.admin.entity.Admin;
import com.varthana.admin.exception.CustomException;

public interface AdminService {
    public Admin getAdminByEmail(String email) throws CustomException;

    public Admin update(Admin admin) throws CustomException;

    public Admin saveAdmin(Admin admin) throws CustomException;
}
