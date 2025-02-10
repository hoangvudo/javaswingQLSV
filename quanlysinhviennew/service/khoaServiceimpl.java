/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.quanlysinhviennew.service;
import com.mycompany.quanlysinhviennew.DAO.khoaDAO;
import com.mycompany.quanlysinhviennew.DAO.khoaDAOimpl;
import com.mycompany.quanlysinhviennew.model.Khoa;
import java.util.List;


/**
 *
 * @author Administrator
 */
public class khoaServiceimpl implements khoaService{
    private khoaDAO KhoaDAO = null;
    public khoaServiceimpl(){
   this.KhoaDAO = new khoaDAOimpl();
}

    @Override
    public List<Khoa> getAllKhoa() {
        return KhoaDAO.getList();
    }

    @Override
    public boolean addKhoa(Khoa k) {
        return KhoaDAO.insert(k);
    }

    @Override
    public boolean updateKhoa(Khoa k) {
        return KhoaDAO.update(k);
    }

    @Override
    public boolean deleteKhoa(String maKhoa) {
        return KhoaDAO.delete(maKhoa);
    }

    @Override
    public List<Khoa> searchKhoaByName(String tenKhoa) {
        return KhoaDAO.findByName(tenKhoa);
    }
}
