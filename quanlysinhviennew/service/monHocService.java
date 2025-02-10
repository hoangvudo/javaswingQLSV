/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.quanlysinhviennew.service;
import com.mycompany.quanlysinhviennew.model.monHoc;
import java.util.List;

/**
 *
 * @author Administrator
 */
public interface monHocService {
    List<monHoc> getAllMonHoc();
    boolean insertMonHoc(monHoc mh);
    boolean updateMonHoc(monHoc mh);
    boolean deleteMonHoc(String maMon);
    List<monHoc> findByName(String tenMon);

}
