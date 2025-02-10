/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.quanlysinhviennew.model;

/**
 *
 * @author Administrator
 */
public class Diem {
     private String maSinhVien;
    private String maMonHoc;
    private float diemQT;
    private float diemThi;
    private float diemTB;

    public Diem() {
    }

    public Diem(String maSinhVien, String maMonHoc, float diemQT, float diemThi, float diemTB) {
        this.maSinhVien = maSinhVien;
        this.maMonHoc = maMonHoc;
        this.diemQT = diemQT;
        this.diemThi = diemThi;
        this.diemTB = diemTB;

    }

    public String getMaSinhVien() {
        return maSinhVien;
    }

    public void setMaSinhVien(String maSinhVien) {
        this.maSinhVien = maSinhVien;
    }

    public String getMaMonHoc() {
        return maMonHoc;
    }

    public void setMaMonHoc(String maMonHoc) {
        this.maMonHoc = maMonHoc;
    }

    public float getDiemQT() {
        return diemQT;
    }

    public void setDiemQT(float diemQT) {
        this.diemQT = diemQT;
    }

    public float getDiemThi() {
        return diemThi;
    }

    public void setDiemThi(float diemThi) {
        this.diemThi = diemThi;
    }
    public float getDiemTB() {
        return diemTB;
    }
}
