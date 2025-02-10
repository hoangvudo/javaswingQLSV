/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.quanlysinhviennew.service;

import java.util.List;
import com.mycompany.quanlysinhviennew.model.monHoc;
import com.mycompany.quanlysinhviennew.DAO.monHocDAO;
import com.mycompany.quanlysinhviennew.DAO.monHocDAOimpl;
import com.mycompany.quanlysinhviennew.DAO.sinhVienDAOimpl;


/**
 *
 * @author Administrator
 */
public class monHocServiceimpl implements monHocService{
    private monHocDAO MonHocDAO = null;
public monHocServiceimpl(){
this.MonHocDAO = new monHocDAOimpl();
}
    @Override
    public List<monHoc> getAllMonHoc() {
        return MonHocDAO.getAllMonHoc();
    }

    @Override
    public boolean insertMonHoc(monHoc mh) {
        return MonHocDAO.insertMonHoc(mh);
    }

    @Override
    public boolean updateMonHoc(monHoc mh) {
        return MonHocDAO.updateMonHoc(mh);
    }

    @Override
    public boolean deleteMonHoc(String maMon) {
        return MonHocDAO.deleteMonHoc(maMon);
    }

    @Override
    public List<monHoc> findByName(String tenMon) {
       return MonHocDAO.findByName(tenMon);
    }
}
