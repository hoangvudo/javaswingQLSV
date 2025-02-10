package com.mycompany.quanlysinhviennew.DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author Administrator
 */
public class DBconnect {

    private static final String url = "jdbc:mysql://localhost:3306/qlsinhvien";
    private static final  String user = "root";
    private static final  String password = "";
    
    public static Connection getConnection(){
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            System.out.println("Loi ket noi " + e.getMessage());
            e.printStackTrace();
        }
        return conn;
    }
}
