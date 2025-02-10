/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.quanlysinhviennew.DAO;

import com.mycompany.quanlysinhviennew.model.Khoa;
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
public class khoaDAOimpl implements khoaDAO{
    @Override
    public List<Khoa> getList() {
        List<Khoa> list = new ArrayList<>();
        Connection conn = DBconnect.getConnection();

        try {
            String sql = "SELECT * FROM Khoa";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                Khoa k = new Khoa();
                k.setMaKhoa(rs.getString("MaKhoa"));
                k.setTenKhoa(rs.getString("TenKhoa"));
                list.add(k);
            }
            rs.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public boolean insert(Khoa k) {
        String sql = "INSERT INTO Khoa (MaKhoa, TenKhoa) VALUES (?, ?)";
        try (Connection conn = DBconnect.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, k.getMaKhoa());
            pstmt.setString(2, k.getTenKhoa());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean update(Khoa k) {
        String sql = "UPDATE Khoa SET TenKhoa=? WHERE MaKhoa=?";
        try (Connection conn = DBconnect.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, k.getTenKhoa());
            pstmt.setString(2, k.getMaKhoa());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(String maKhoa) {
        String sql = "DELETE FROM Khoa WHERE MaKhoa=?";
        try (Connection conn = DBconnect.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, maKhoa);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Khoa> findByName(String tenKhoa) {
        List<Khoa> list = new ArrayList<>();
        String sql = "SELECT * FROM Khoa WHERE TenKhoa LIKE ?";
        try (Connection conn = DBconnect.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, "%" + tenKhoa + "%");
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Khoa k = new Khoa();
                k.setMaKhoa(rs.getString("MaKhoa"));
                k.setTenKhoa(rs.getString("TenKhoa"));
                list.add(k);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
