/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.quanlysinhviennew.DAO;

import com.mycompany.quanlysinhviennew.model.taiKhoan;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
/**
 *
 * @author Administrator
 */
public class TaikhoanDAOimpl implements TaikhoanDAO{
   public boolean validateUser(taiKhoan taikhoan) {
    String query = "SELECT * FROM taikhoan WHERE ten_dang_nhap = ? AND mat_khau = ?";
    try (Connection connection = DBconnect.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(query)) {

        preparedStatement.setString(1, taikhoan.getTen_dang_nhap());
        preparedStatement.setString(2, taikhoan.getMat_khau());
        ResultSet resultSet = preparedStatement.executeQuery();

        return resultSet.next(); // Trả về true nếu tìm thấy tài khoản
    } catch (Exception e) {
        e.printStackTrace();
    }
    return false;
}

    public boolean validateUser(Object taikhoan) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}


