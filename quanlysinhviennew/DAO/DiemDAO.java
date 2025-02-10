/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.quanlysinhviennew.DAO;

import com.mycompany.quanlysinhviennew.model.Diem;
import java.util.List;

/**
 *
 * @author Administrator
 */
public interface DiemDAO {
    List<Diem> getAllDiem();
    boolean insertDiem(Diem diem);
    boolean updateDiem(Diem diem);
    List<Diem> findByMaSV(String maSV);
}
