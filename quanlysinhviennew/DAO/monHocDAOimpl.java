/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.quanlysinhviennew.DAO;
import com.mycompany.quanlysinhviennew.model.monHoc;
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
public class monHocDAOimpl implements monHocDAO{
    @Override
    public List<monHoc> getAllMonHoc() {
        List<monHoc> dsMonHoc = new ArrayList<>();
        String sql = "SELECT * FROM monhoc";
        try (Connection conn = DBconnect.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                dsMonHoc.add(new monHoc(
                        rs.getString("MaMH"),
                        rs.getString("TenMH"),
                        rs.getInt("SoTinChi")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dsMonHoc;
    }

    @Override
    public boolean insertMonHoc(monHoc mh) {
        String sql = "INSERT INTO monhoc (MaMH, TenMH, SoTinChi) VALUES (?, ?, ?)";
        try (Connection conn = DBconnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, mh.getMaMon());
            stmt.setString(2, mh.getTenMon());
            stmt.setInt(3, mh.getSoTinChi());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean updateMonHoc(monHoc mh) {
        String sql = "UPDATE MonHoc SET TenMH = ?, SoTinChi = ? WHERE MaMH = ?";
        try (Connection conn = DBconnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, mh.getTenMon());
            stmt.setInt(2, mh.getSoTinChi());
            stmt.setString(3, mh.getMaMon());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean deleteMonHoc(String maMon) {
        String sql = "DELETE FROM MonHoc WHERE MaMH = ?";
        try (Connection conn = DBconnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, maMon);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<monHoc> findByName(String tenMon) {
        List<monHoc> list = new ArrayList<>();
    String sql = "SELECT * FROM MonHoc WHERE TenMH LIKE ?";
    try (Connection con = DBconnect.getConnection();
         PreparedStatement ps = con.prepareStatement(sql)) {
        ps.setString(1, "%" + tenMon + "%");
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            monHoc mh = new monHoc(
                rs.getString("MaMH"),
                rs.getString("TenMH"),
                rs.getInt("SoTinChi")
            );
            list.add(mh);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return list;
    }
}
