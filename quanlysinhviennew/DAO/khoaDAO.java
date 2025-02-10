/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.quanlysinhviennew.DAO;

import com.mycompany.quanlysinhviennew.model.Khoa;
import java.util.List;

/**
 *
 * @author Administrator
 */
public interface khoaDAO {
    List<Khoa> getList();        // Lấy danh sách khoa
    boolean insert(Khoa k);      // Thêm khoa
    boolean update(Khoa k);      // Cập nhật khoa
    boolean delete(String maKhoa);  // Xóa khoa
    List<Khoa> findByName(String tenKhoa); // Tìm kiếm theo tên
}
