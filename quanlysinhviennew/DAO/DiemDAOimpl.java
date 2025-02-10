/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.quanlysinhviennew.DAO;

import com.mycompany.quanlysinhviennew.model.Diem;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Administrator
 */
public class DiemDAOimpl implements DiemDAO {

    @Override
    public List<Diem> getAllDiem() {
        List<Diem> dsDiem = new ArrayList<>();
        String sql = "SELECT MaSV, MaMH, DiemQT, DiemThi, (DiemQT + DiemThi) / 2 AS DiemTB FROM Diem";
    try (Connection conn = DBconnect.getConnection();
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery(sql)) {
        while (rs.next()) {
            dsDiem.add(new Diem(
                    rs.getString("MaSV"),
                    rs.getString("MaMH"),
                    rs.getFloat("DiemQT"),
                    rs.getFloat("DiemThi"),
                    rs.getFloat("DiemTB") // Lấy điểm trung bình từ SQL
            ));
        }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dsDiem;
    }

    @Override
    public boolean insertDiem(Diem diem) {
        String sql = "INSERT INTO Diem (MaSV, MaMH, DiemQT, DiemThi) VALUES (?, ?, ?, ?)";
     try (Connection conn = DBconnect.getConnection();
          PreparedStatement stmt = conn.prepareStatement(sql)) {

         stmt.setString(1, diem.getMaSinhVien());
         stmt.setString(2, diem.getMaMonHoc());
         stmt.setFloat(3, diem.getDiemQT());
         stmt.setFloat(4, diem.getDiemThi());

        int rowsAffected = stmt.executeUpdate(); // Kiểm tra số dòng bị ảnh hưởng
                return rowsAffected > 0;
    } catch (SQLException e) {
        e.printStackTrace(); // In ra lỗi trong Console để kiểm tra
        return false;
    }
    }

    @Override
    public boolean updateDiem(Diem diem) {
                String sql = "UPDATE Diem SET DiemQT = ?, DiemThi = ? WHERE MaSV = ? AND MaMH = ?";
         try (Connection conn = DBconnect.getConnection();
              PreparedStatement stmt = conn.prepareStatement(sql)) {

             stmt.setFloat(1, diem.getDiemQT());
             stmt.setFloat(2, diem.getDiemThi());
             stmt.setString(3, diem.getMaSinhVien());
             stmt.setString(4, diem.getMaMonHoc());

             int rowsAffected = stmt.executeUpdate(); // Kiểm tra số dòng bị ảnh hưởng
             return rowsAffected > 0;
    } catch (SQLException e) {
        e.printStackTrace(); // In ra lỗi trong Console để kiểm tra
        return false;
    }
    }

    @Override
    public List<Diem> findByMaSV(String maSV) {
        List<Diem> list = new ArrayList<>();
        String sql = "SELECT * FROM Diem WHERE MaSV LIKE ?";
        try (Connection conn = DBconnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + maSV + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(new Diem(
                        rs.getString("MaSV"),
                        rs.getString("MaMH"),
                        rs.getFloat("DiemQT"),
                        rs.getFloat("DiemThi"),
                        rs.getFloat("DiemThi")

                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
