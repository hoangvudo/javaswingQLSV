/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.quanlysinhviennew.service;

import com.mycompany.quanlysinhviennew.model.sinhVien;
import java.util.List;

/**
 *
 * @author Administrator
 */
public interface sinhVienService {
    public List<sinhVien> getList();
    public List<sinhVien> findName (String maSV);
    public boolean insert (sinhVien sv);
    public boolean update (sinhVien sv);
    public boolean delete (sinhVien sv);
}
