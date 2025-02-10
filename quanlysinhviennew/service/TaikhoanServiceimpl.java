/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.quanlysinhviennew.service;


import com.mycompany.quanlysinhviennew.DAO.TaikhoanDAO;
import com.mycompany.quanlysinhviennew.DAO.TaikhoanDAOimpl;
import com.mycompany.quanlysinhviennew.model.taiKhoan;



/**
 *
 * @author Administrator
 */
public class TaikhoanServiceimpl implements TaikhoanService{
     private TaikhoanDAO taikhoanDAO;

    public TaikhoanServiceimpl() {
        this.taikhoanDAO = new TaikhoanDAOimpl();
    }

    @Override
    public boolean authenticate(String username, String password) {
        // Kiểm tra đầu vào có null hoặc rỗng không
        if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
            return false;
        }

        // Tạo đối tượng taiKhoan để truyền vào DAO
        taiKhoan taikhoan = new taiKhoan();
        taikhoan.setTen_dang_nhap(username);
        taikhoan.setMat_khau(password);

        // Gọi phương thức validateUser từ TaikhoanDAO
        return taikhoanDAO.validateUser(taikhoan);
    }
}