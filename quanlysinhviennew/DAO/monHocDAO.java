/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.quanlysinhviennew.DAO;

import java.util.List;
import com.mycompany.quanlysinhviennew.model.monHoc;

/**
 *
 * @author Administrator
 */
public interface monHocDAO {
     List<monHoc> getAllMonHoc();
    boolean insertMonHoc(monHoc mh);
    boolean updateMonHoc(monHoc mh);
    boolean deleteMonHoc(String maMon);
    List<monHoc> findByName(String tenMon);

}
