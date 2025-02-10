/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.quanlysinhviennew.service;
import com.mycompany.quanlysinhviennew.DAO.sinhVienDAO;
import com.mycompany.quanlysinhviennew.DAO.sinhVienDAOimpl;
import com.mycompany.quanlysinhviennew.model.sinhVien;
import java.util.List;



/**
 *
 * @author Administrator
 */
public class sinhVienServiceimpl implements sinhVienService{
    private sinhVienDAO SinhVienDAO = null;
    public sinhVienServiceimpl(){
   this.SinhVienDAO = new sinhVienDAOimpl();
}

    @Override
    public List<sinhVien> getList() {
       return SinhVienDAO.getList();
    }

    @Override
    public List<sinhVien> findName(String maSV) {
        return SinhVienDAO.findName(maSV);
    }

    @Override
    public boolean insert(sinhVien sv) {
        return SinhVienDAO.insert(sv);
    }

    @Override
    public boolean update(sinhVien sv) {
        return SinhVienDAO.update(sv);
    }

    @Override
    public boolean delete(sinhVien sv) {
        return SinhVienDAO.delete(sv);
    }
}
