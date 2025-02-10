/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.quanlysinhviennew.service;

import com.mycompany.quanlysinhviennew.model.Khoa;
import java.util.List;

/**
 *
 * @author Administrator
 */
public interface khoaService {
    List<Khoa> getAllKhoa();
    boolean addKhoa(Khoa k);
    boolean updateKhoa(Khoa k);
    boolean deleteKhoa(String maKhoa);
    List<Khoa> searchKhoaByName(String tenKhoa);
}
