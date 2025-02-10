/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.quanlysinhviennew.service;
import com.mycompany.quanlysinhviennew.DAO.DiemDAO;
import com.mycompany.quanlysinhviennew.DAO.DiemDAOimpl;
import com.mycompany.quanlysinhviennew.model.Diem;
import java.util.List;


/**
 *
 * @author Administrator
 */
public class DiemServiceimpl implements DiemService{
     private DiemDAO diemDAO = null;

    public DiemServiceimpl() {
        this.diemDAO = new DiemDAOimpl();
    }

    @Override
    public List<Diem> getAllDiem() {
        return diemDAO.getAllDiem();
    }

    @Override
    public boolean insertDiem(Diem diem) {
        return diemDAO.insertDiem(diem);
    }

    @Override
    public boolean updateDiem(Diem diem) {
        return diemDAO.updateDiem(diem);
    }

    @Override
    public List<Diem> findByMaSV(String maSV) {
        return diemDAO.findByMaSV(maSV);
    }
}
