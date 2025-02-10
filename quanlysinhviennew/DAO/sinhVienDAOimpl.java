/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.quanlysinhviennew.DAO;
import com.mycompany.quanlysinhviennew.model.sinhVien;



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
public class sinhVienDAOimpl implements sinhVienDAO{
    
    public List<sinhVien> getList() { 
         ArrayList<sinhVien> list = new ArrayList<>(); 
         Connection conn = DBconnect.getConnection(); 
        try { 
            Statement stmt = conn.createStatement(); 
            // get data from table  
            String sql = "select * from sinhvien"; 
            ResultSet rs = stmt.executeQuery(sql); 
            //show data 
        while (rs.next()) { 
                sinhVien sv = new sinhVien(); 
                sv.setMaSV(rs.getString("MaSV")); 
                sv.setHoTen(rs.getString("HoTen"));
                sv.setNgaySinh(rs.getDate("NgaySinh"));
                sv.setGioiTinh(rs.getBoolean("GioiTinh"));
                sv.setDiaChi(rs.getString("DiaChi"));
                sv.setDienThoai(rs.getString("DienThoai"));
                sv.setMaKhoa(rs.getString("MaKhoa"));
                sv.setMonHoc(rs.getString("MaMH"));

                list.add(sv); 
            } 
            rs.close(); 
            conn.close(); 
        } catch (Exception e) { 
            e.printStackTrace(); 
        } 
        return list;
}

    @Override
    public List<sinhVien> findName(String maSV) {
 List<sinhVien> list = new ArrayList<>();
    Connection conn = DBconnect.getConnection();
    String sql = "SELECT * FROM SinhVien WHERE MaSV LIKE ?";
    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
        pstmt.setString(1, "%" + maSV + "%");  // Tìm kiếm gần đúng

        ResultSet rs = pstmt.executeQuery();
        while (rs.next()) {
            sinhVien sv = new sinhVien();
            sv.setMaSV(rs.getString("MaSV"));
            sv.setHoTen(rs.getString("HoTen"));
            sv.setNgaySinh(rs.getDate("NgaySinh"));
            sv.setGioiTinh(rs.getBoolean("GioiTinh"));
            sv.setDiaChi(rs.getString("DiaChi"));
            sv.setDienThoai(rs.getString("DienThoai"));
            sv.setMaKhoa(rs.getString("MaKhoa"));
            sv.setMonHoc(rs.getString("MaMH"));
            list.add(sv);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return list;    
    }

    @Override
    public boolean insert(sinhVien sv) {
         
        ArrayList<sinhVien> list = new ArrayList<>();
    Connection conn = DBconnect.getConnection();
    String sql = "INSERT INTO SinhVien (MaSV, HoTen, NgaySinh, GioiTinh, DiaChi, DienThoai, MaKhoa, MaMH) VALUES (?, ?, ?, ?, ?, ?, ?,?)";
     try (
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, sv.getMaSV());
            pstmt.setString(2, sv.getHoTen());
            pstmt.setDate(3, sv.getNgaySinh());
            pstmt.setBoolean(4, sv.isGioiTinh());
            pstmt.setString(5, sv.getDiaChi());
            pstmt.setString(6, sv.getDienThoai());
            pstmt.setString(7, sv.getMaKhoa());
            pstmt.setString(8, sv.getMonHoc());

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    
  }

    @Override
    public boolean update(sinhVien sv) {
    Connection conn = DBconnect.getConnection();
       String sql = "UPDATE SinhVien SET HoTen = ?, NgaySinh = ?, GioiTinh = ?, DiaChi = ?, DienThoai = ?, MaKhoa = ?, MaMH = ? WHERE MaSV = ?";
       try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
           pstmt.setString(1, sv.getHoTen());
           pstmt.setDate(2, sv.getNgaySinh());
           pstmt.setBoolean(3, sv.isGioiTinh());
           pstmt.setString(4, sv.getDiaChi());
           pstmt.setString(5, sv.getDienThoai());
           pstmt.setString(6, sv.getMaKhoa());
           pstmt.setString(7, sv.getMonHoc());
           pstmt.setString(8, sv.getMaSV());  // WHERE MaSV = ?

           int rowsAffected = pstmt.executeUpdate();
           return rowsAffected > 0;
       } catch (SQLException e) {
           e.printStackTrace();
           return false;
       }    
    }

    @Override
    public boolean delete(sinhVien sv) {
Connection conn = DBconnect.getConnection();
    String sql = "DELETE FROM SinhVien WHERE MaSV = ?";
    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
        pstmt.setString(1, sv.getMaSV());  // Xóa dựa trên MaSV

        int rowsAffected = pstmt.executeUpdate();
        return rowsAffected > 0;
    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }    
    }
}

