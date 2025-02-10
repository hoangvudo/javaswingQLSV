/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.quanlysinhviennew.model;

/**
 *
 * @author Administrator
 */
public class taiKhoan {
    private String ma_tai_khoan;
    private String ten_dang_nhap;
    private String mat_khau;

    public taiKhoan() {
    }

    public taiKhoan(String ma_tai_khoan, String ten_dang_nhap, String mat_khau) {
        this.ma_tai_khoan = ma_tai_khoan;
        this.ten_dang_nhap = ten_dang_nhap;
        this.mat_khau = mat_khau;
    }

    public String getMa_tai_khoan() {
        return ma_tai_khoan;
    }

    public void setMa_tai_khoan(String ma_tai_khoan) {
        this.ma_tai_khoan = ma_tai_khoan;
    }

    public String getTen_dang_nhap() {
        return ten_dang_nhap;
    }

    public void setTen_dang_nhap(String ten_dang_nhap) {
        this.ten_dang_nhap = ten_dang_nhap;
    }

    public String getMat_khau() {
        return mat_khau;
    }

    public void setMat_khau(String mat_khau) {
        this.mat_khau = mat_khau;
    }

   
}
