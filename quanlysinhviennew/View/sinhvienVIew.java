/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.mycompany.quanlysinhviennew.View;

import com.mycompany.quanlysinhviennew.DAO.DBconnect;
import com.mycompany.quanlysinhviennew.DAO.DiemDAO;
import com.mycompany.quanlysinhviennew.DAO.DiemDAOimpl;
import com.mycompany.quanlysinhviennew.DAO.khoaDAOimpl;
import com.mycompany.quanlysinhviennew.DAO.khoaDAO;
import com.mycompany.quanlysinhviennew.DAO.monHocDAO;
import com.mycompany.quanlysinhviennew.DAO.monHocDAOimpl;
import com.mycompany.quanlysinhviennew.model.Diem;
import com.mycompany.quanlysinhviennew.model.Khoa;
import com.mycompany.quanlysinhviennew.model.monHoc;
import com.mycompany.quanlysinhviennew.model.sinhVien;
import com.mycompany.quanlysinhviennew.service.DiemService;
import com.mycompany.quanlysinhviennew.service.DiemServiceimpl;
import com.mycompany.quanlysinhviennew.service.khoaService;
import com.mycompany.quanlysinhviennew.service.khoaServiceimpl;
import com.mycompany.quanlysinhviennew.service.monHocService;
import com.mycompany.quanlysinhviennew.service.monHocServiceimpl;
import com.mycompany.quanlysinhviennew.service.sinhVienService;
import com.mycompany.quanlysinhviennew.service.sinhVienServiceimpl;
import java.awt.Image;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;



/**
 *
 * @author Administrator
 */
public class sinhvienVIew extends javax.swing.JFrame {
    DefaultTableModel dfModel;

    List<sinhVien> list;
    private khoaDAO KhoaDAO = new khoaDAOimpl();

    private khoaService khoaService = new khoaServiceimpl();
    private monHocDAO MonHocDAO = new monHocDAOimpl();
    private monHocService MonHocService = new monHocServiceimpl();
    private DiemService diemService = new DiemServiceimpl();

    sinhVienService SinhVienService = null;
    private int monHocCount = 0;  // Số môn học 
    private int khoaCount = 0;    // Số khoa 
    private int sinhVienCount = 0; // Số sinh viên
    /**
     * Creates new form sinhvienVIew
     */
    public sinhvienVIew() {
        initComponents();
         this.SinhVienService = new sinhVienServiceimpl();
        list = SinhVienService.getList();
        dfModel = (DefaultTableModel) tblSinhVien.getModel();
        dfModel.setColumnIdentifiers(new Object[] {"Mã Sinh Viên", "Họ Tên", "Ngày Sinh", "Giới Tính", "Địa Chỉ","SDT", "Mã Khoa", "Mã Môn Học"});
        ShowTable();
        // Load số lượng khi khởi tạo
        loadCounts(); // Load số lượng từ cơ sở dữ liệu hoặc nguồn dữ liệu khác
        updateCountLabels(); // Hiển thị số lượng lên các label
        loadKhoa();
        loadMonHoc();
        loadDiem();
    }
    public void ShowTable() {
        try {
        Connection conn = DBconnect.getConnection();
        String query = "SELECT * FROM sinhvien";
        PreparedStatement pstmt = conn.prepareStatement(query);
        ResultSet rs = pstmt.executeQuery();

        DefaultTableModel model = (DefaultTableModel) tblSinhVien.getModel();
        model.setRowCount(0);

        while (rs.next()) {
            Object[] row = new Object[]{
                rs.getString("MaSV"),
                rs.getString("HoTen"),
                rs.getDate("NgaySinh"),
                rs.getInt("GioiTinh") == 1 ? "Nam" : "Nữ",
                rs.getString("DiaChi") != null ? rs.getString("DiaChi") : "",
                rs.getString("DienThoai") != null ? rs.getString("DienThoai") : "",
                rs.getString("MaKhoa") != null ? rs.getString("MaKhoa") : "",
                rs.getString("MaMH") != null ? rs.getString("MaMH") : ""

            };
            model.addRow(row);
            
            // Hiển thị ảnh nếu có
            byte[] imageData = rs.getBytes("Anh");
            if (imageData != null) {
                ImageIcon icon = new ImageIcon(imageData);
                Image img = icon.getImage(); 
                Image newimg = img.getScaledInstance(anhlaber.getWidth(), anhlaber.getHeight(), java.awt.Image.SCALE_SMOOTH); 
                icon = new ImageIcon(newimg);
                anhlaber.setIcon(icon);
            }
        }

        rs.close();
        pstmt.close();
        conn.close();
    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(null, "Lỗi khi tải dữ liệu lên bảng!\n" + e.getMessage());
    }
}
    private void loadKhoa() {
    DefaultTableModel model = (DefaultTableModel) tblKhoa.getModel();
    model.setRowCount(0); // Xóa dữ liệu cũ

    List<Khoa> list = KhoaDAO.getList();
    for (Khoa k : list) {
        model.addRow(new Object[]{ k.getMaKhoa(), k.getTenKhoa() });
    }
}


    private void loadCounts() {
        // TODO: Load số lượng từ cơ sở dữ liệu hoặc nguồn dữ liệu khác
        //  Load số sinh viên
        try (Connection conn = DBconnect.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("SELECT COUNT(*) FROM sinhvien");
             ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                sinhVienCount = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi tải số lượng sinh viên: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
        //  Load số khoa (điều chỉnh truy vấn SQL cho bảng khoa)
        try (Connection conn = DBconnect.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("SELECT COUNT(*) FROM khoa"); // Thay 'khoa' bằng tên bảng khoa của bạn
             ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                khoaCount = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi tải số lượng khoa: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
        //  Load số môn học (điều chỉnh truy vấn SQL cho bảng môn học)
        try (Connection conn = DBconnect.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("SELECT COUNT(*) FROM monhoc");  // Thay 'monhoc' bằng tên bảng môn học của bạn
             ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                monHocCount = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi tải số lượng môn học: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateCountLabels() {
        // Cập nhật các JLabel hiển thị số lượng
        lblSinhVien.setText(String.valueOf(sinhVienCount));
        lblKhoa.setText(String.valueOf(khoaCount));
        lblMonHoc.setText(String.valueOf(monHocCount));
    }
    
    public void saveImageToDatabase(File imageFile, String maSV) {
    try {
        FileInputStream fis = new FileInputStream(imageFile);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buf = new byte[1024];
        for (int readNum; (readNum = fis.read(buf)) != -1;) {
            bos.write(buf, 0, readNum);
        }
        byte[] imageBytes = bos.toByteArray();

        Connection conn = DBconnect.getConnection();
        String query = "UPDATE sinhvien SET Anh = ? WHERE MaSV = ?";
        PreparedStatement pstmt = conn.prepareStatement(query);
        pstmt.setBytes(1, imageBytes);
        pstmt.setString(2, maSV);
        pstmt.executeUpdate();

        JOptionPane.showMessageDialog(null, "Lưu ảnh thành công!");

    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(null, "Lỗi khi lưu ảnh!\n" + e.getMessage());
    }
}
    private void loadMonHoc() {
    DefaultTableModel model = (DefaultTableModel) tblMonHoc.getModel();
    model.setRowCount(0); // Xóa dữ liệu cũ

    List<monHoc> list = MonHocDAO.getAllMonHoc();
    for (monHoc mh : list) {
        model.addRow(new Object[]{ mh.getMaMon(), mh.getTenMon(), mh.getSoTinChi() });
    }
}
    
    public void loadDiem() {
    DefaultTableModel model = (DefaultTableModel) tblDiem.getModel();
    model.setRowCount(0);

    DiemDAO diemDAO = new DiemDAOimpl();
    List<Diem> list = diemDAO.getAllDiem();

    for (Diem diem : list) {
        model.addRow(new Object[]{
            diem.getMaSinhVien(),
            diem.getMaMonHoc(),
            diem.getDiemQT(),
            diem.getDiemThi(),
            diem.getDiemTB()
        });
    }
}

    
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        btnMonHoc = new javax.swing.JButton();
        btnSinhVien = new javax.swing.JButton();
        btnKhoa = new javax.swing.JButton();
        btnThongKe = new javax.swing.JButton();
        btnDiem = new javax.swing.JButton();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblSinhVien = new javax.swing.JTable();
        btnTimKiem = new javax.swing.JButton();
        txtSearch = new javax.swing.JTextField();
        btnCapNhat = new javax.swing.JButton();
        btnXoa = new javax.swing.JButton();
        btnReset = new javax.swing.JButton();
        btnThem = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        txtMaSV = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        txtHoten = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        txtNgaySinh = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        radioNam = new javax.swing.JRadioButton();
        radioNu = new javax.swing.JRadioButton();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtDiaChi = new javax.swing.JTextArea();
        jLabel11 = new javax.swing.JLabel();
        txtSDT = new javax.swing.JTextField();
        anhlaber = new javax.swing.JLabel();
        btnThemAnh = new javax.swing.JButton();
        jLabel24 = new javax.swing.JLabel();
        txtMaKhoa = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        txtMonHoc = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblKhoa = new javax.swing.JTable();
        btnKhoaReset = new javax.swing.JButton();
        btnKhoaXoa = new javax.swing.JButton();
        btnKhoaCapNhat = new javax.swing.JButton();
        btnThemKhoa = new javax.swing.JButton();
        btnKhoaTimKiem = new javax.swing.JButton();
        txtKhoaSearch = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtTenKhoa = new javax.swing.JTextField();
        txtmakhoakhoa = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        btnThemMH = new javax.swing.JButton();
        btnXoaMH = new javax.swing.JButton();
        jButton21 = new javax.swing.JButton();
        btnCapNhatMH = new javax.swing.JButton();
        btnTimKiemMH = new javax.swing.JButton();
        txtSearchMH = new javax.swing.JTextField();
        jScrollPane4 = new javax.swing.JScrollPane();
        tblMonHoc = new javax.swing.JTable();
        jLabel4 = new javax.swing.JLabel();
        txtTenMH = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        txtMaMH = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        txtSoTC = new javax.swing.JTextField();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        tblDiem = new javax.swing.JTable();
        btnResetDiem = new javax.swing.JButton();
        btnCapNhatDiem = new javax.swing.JButton();
        btnThemDiem = new javax.swing.JButton();
        btnTimKiemDiem = new javax.swing.JButton();
        txtDiem = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txtMSVDiem = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        txtMHDiem = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        txtDiemQT = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        txtDiemThi = new javax.swing.JTextField();
        jPanel6 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        lblMonHoc = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        lblKhoa = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        jLabel19 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        lblSinhVien = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/Person-Male-Light-icon (2).png"))); // NOI18N
        jLabel1.setText("1");

        btnMonHoc.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnMonHoc.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/class.png"))); // NOI18N
        btnMonHoc.setText("MÔN HỌC");
        btnMonHoc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMonHocActionPerformed(evt);
            }
        });

        btnSinhVien.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnSinhVien.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/student.png"))); // NOI18N
        btnSinhVien.setText("THÔNG TIN SINH VIÊN");
        btnSinhVien.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSinhVienActionPerformed(evt);
            }
        });

        btnKhoa.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnKhoa.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/khoa.png"))); // NOI18N
        btnKhoa.setText("KHOA");
        btnKhoa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnKhoaActionPerformed(evt);
            }
        });

        btnThongKe.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnThongKe.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/monhoc.png"))); // NOI18N
        btnThongKe.setText("THỐNG KÊ");
        btnThongKe.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThongKeActionPerformed(evt);
            }
        });

        btnDiem.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnDiem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/point.png"))); // NOI18N
        btnDiem.setText("ĐIỂM");
        btnDiem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDiemActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btnKhoa, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(btnThongKe, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(btnMonHoc, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(btnSinhVien, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(btnDiem, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jSeparator1)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(54, 54, 54)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(8, 8, 8)
                .addComponent(btnSinhVien, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27)
                .addComponent(btnKhoa, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(36, 36, 36)
                .addComponent(btnMonHoc, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 38, Short.MAX_VALUE)
                .addComponent(btnDiem, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(42, 42, 42)
                .addComponent(btnThongKe, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(67, 67, 67))
        );

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, 620));

        tblSinhVien.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "MÃ SINH VIÊN", "HỌ TÊN", "NGÀY SINH", "GIỚI TÍNH", "ĐỊA CHỈ", "ĐIỆN THOẠI", "MÃ KHOA", "MÃ MÔN HỌC"
            }
        ));
        tblSinhVien.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblSinhVienMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblSinhVien);

        btnTimKiem.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnTimKiem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/search-icon (2).png"))); // NOI18N
        btnTimKiem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTimKiemActionPerformed(evt);
            }
        });

        btnCapNhat.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnCapNhat.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/Pencil-icon.png"))); // NOI18N
        btnCapNhat.setText("CẬP NHẬT");
        btnCapNhat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCapNhatActionPerformed(evt);
            }
        });

        btnXoa.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnXoa.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/delete-icon.png"))); // NOI18N
        btnXoa.setText("XÓA");
        btnXoa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaActionPerformed(evt);
            }
        });

        btnReset.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnReset.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/Refresh-icon.png"))); // NOI18N
        btnReset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnResetActionPerformed(evt);
            }
        });

        btnThem.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnThem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/add-icon.png"))); // NOI18N
        btnThem.setText("THÊM");
        btnThem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemActionPerformed(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/student.png"))); // NOI18N
        jLabel7.setText("HỌ VÀ TÊN");

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/teacher.png"))); // NOI18N
        jLabel8.setText("MÃ SINH VIÊN");

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/icons8_denied_32px.png"))); // NOI18N
        jLabel9.setText("GIỚI TÍNH");

        jLabel10.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/submit1.png"))); // NOI18N
        jLabel10.setText("NGÀY SINH");

        buttonGroup1.add(radioNam);
        radioNam.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        radioNam.setText("NAM");

        buttonGroup1.add(radioNu);
        radioNu.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        radioNu.setText("NỮ");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/update.png"))); // NOI18N
        jLabel2.setText("ĐỊA CHỈ");

        txtDiaChi.setColumns(20);
        txtDiaChi.setRows(5);
        jScrollPane2.setViewportView(txtDiaChi);

        jLabel11.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/arrow-right-circle-icon (2).png"))); // NOI18N
        jLabel11.setText("SDT");

        anhlaber.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        btnThemAnh.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnThemAnh.setText("THÊM ẢNH");
        btnThemAnh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemAnhActionPerformed(evt);
            }
        });

        jLabel24.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel24.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/khoa.png"))); // NOI18N
        jLabel24.setText("MÃ KHOA");

        jLabel12.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/class.png"))); // NOI18N
        jLabel12.setText("MÔN HỌC");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap(22, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 263, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(btnTimKiem)))
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addGap(92, 92, 92)
                                        .addComponent(btnThem, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addGap(9, 9, 9)
                                        .addComponent(btnThemAnh, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(2, 2, 2)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(jLabel8)
                                        .addGap(18, 18, 18)
                                        .addComponent(txtMaSV, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(jLabel7)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(jPanel2Layout.createSequentialGroup()
                                                .addComponent(radioNam)
                                                .addGap(18, 18, 18)
                                                .addComponent(radioNu))
                                            .addGroup(jPanel2Layout.createSequentialGroup()
                                                .addGap(1, 1, 1)
                                                .addComponent(txtHoten, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(jLabel10)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(txtNgaySinh, javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(18, 18, 18)
                                .addComponent(anhlaber, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(26, 26, 26)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(btnCapNhat)
                                .addGap(16, 16, 16)
                                .addComponent(btnXoa, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btnReset, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel2)
                                    .addComponent(jLabel11))
                                .addGap(28, 28, 28)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtSDT, javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 241, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(jLabel12, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel24, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txtMaKhoa, javax.swing.GroupLayout.DEFAULT_SIZE, 185, Short.MAX_VALUE)
                                    .addComponent(txtMonHoc))))
                        .addGap(21, 21, 21))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 963, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(350, 350, 350)
                        .addComponent(btnTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 323, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(btnXoa, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(btnCapNhat, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(btnReset, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(btnThem))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(3, 3, 3)
                                .addComponent(txtSearch)))))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel8)
                            .addComponent(txtMaSV, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(21, 21, 21)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel7)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(txtHoten)
                                .addGap(7, 7, 7)))
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel10)
                            .addComponent(txtNgaySinh, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel11)
                            .addComponent(txtSDT, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel24)
                            .addComponent(txtMaKhoa, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(7, 7, 7)
                        .addComponent(anhlaber, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnThemAnh, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel9)
                            .addComponent(radioNam)
                            .addComponent(radioNu)
                            .addComponent(jLabel12)
                            .addComponent(txtMonHoc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(34, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("tab1", jPanel2);

        tblKhoa.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "MÃ KHOA", "TÊN KHOA"
            }
        ));
        tblKhoa.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblKhoaMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(tblKhoa);

        btnKhoaReset.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnKhoaReset.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/Refresh-icon.png"))); // NOI18N
        btnKhoaReset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnKhoaResetActionPerformed(evt);
            }
        });

        btnKhoaXoa.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnKhoaXoa.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/delete-icon.png"))); // NOI18N
        btnKhoaXoa.setText("XÓA");
        btnKhoaXoa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnKhoaXoaActionPerformed(evt);
            }
        });

        btnKhoaCapNhat.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnKhoaCapNhat.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/Pencil-icon.png"))); // NOI18N
        btnKhoaCapNhat.setText("CẬP NHÂT");
        btnKhoaCapNhat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnKhoaCapNhatActionPerformed(evt);
            }
        });

        btnThemKhoa.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnThemKhoa.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/add-icon.png"))); // NOI18N
        btnThemKhoa.setText("THÊM");
        btnThemKhoa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemKhoaActionPerformed(evt);
            }
        });

        btnKhoaTimKiem.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnKhoaTimKiem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/search-icon (2).png"))); // NOI18N
        btnKhoaTimKiem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnKhoaTimKiemActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/khoa.png"))); // NOI18N
        jLabel3.setText("MÃ KHOA");

        jLabel13.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel13.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/edit-icon (2).png"))); // NOI18N
        jLabel13.setText("TÊN KHOA");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 910, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnKhoaReset, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(71, 71, 71)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel3Layout.createSequentialGroup()
                                    .addComponent(txtTenKhoa, javax.swing.GroupLayout.PREFERRED_SIZE, 204, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(0, 0, Short.MAX_VALUE))
                                .addGroup(jPanel3Layout.createSequentialGroup()
                                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel3Layout.createSequentialGroup()
                                            .addComponent(txtmakhoakhoa, javax.swing.GroupLayout.PREFERRED_SIZE, 204, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .addGroup(jPanel3Layout.createSequentialGroup()
                                            .addComponent(txtKhoaSearch)
                                            .addGap(18, 18, 18)
                                            .addComponent(btnKhoaTimKiem)
                                            .addGap(51, 51, 51)))
                                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(btnThemKhoa, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(btnKhoaCapNhat, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                                .addGap(410, 410, 410)
                                .addComponent(btnKhoaXoa, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addGap(252, 252, 252))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 363, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnKhoaTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnThemKhoa)
                    .addComponent(txtKhoaSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(23, 23, 23)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtmakhoakhoa, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnKhoaCapNhat))
                .addGap(25, 25, 25)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(txtTenKhoa, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnKhoaXoa))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 24, Short.MAX_VALUE)
                .addComponent(btnKhoaReset)
                .addGap(15, 15, 15))
        );

        jTabbedPane1.addTab("tab2", jPanel3);

        btnThemMH.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnThemMH.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/add-icon.png"))); // NOI18N
        btnThemMH.setText("THÊM");
        btnThemMH.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemMHActionPerformed(evt);
            }
        });

        btnXoaMH.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnXoaMH.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/delete-icon.png"))); // NOI18N
        btnXoaMH.setText("XÓA");
        btnXoaMH.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaMHActionPerformed(evt);
            }
        });

        jButton21.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton21.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/Refresh-icon.png"))); // NOI18N

        btnCapNhatMH.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnCapNhatMH.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/Pencil-icon.png"))); // NOI18N
        btnCapNhatMH.setText("CẬP NHẬT");
        btnCapNhatMH.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCapNhatMHActionPerformed(evt);
            }
        });

        btnTimKiemMH.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnTimKiemMH.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/search-icon (2).png"))); // NOI18N
        btnTimKiemMH.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTimKiemMHActionPerformed(evt);
            }
        });

        tblMonHoc.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "MÃ MÔN HỌC", "TÊN MÔN HỌC", "SỐ TÍN CHỈ"
            }
        ));
        tblMonHoc.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblMonHocMouseClicked(evt);
            }
        });
        jScrollPane4.setViewportView(tblMonHoc);

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/arrow-right-circle-icon (2).png"))); // NOI18N
        jLabel4.setText("TÊN MÔN HỌC");

        jLabel14.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/Pencil-icon.png"))); // NOI18N
        jLabel14.setText("MÃ MÔN HỌC");

        jLabel15.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel15.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/database-icon (2).png"))); // NOI18N
        jLabel15.setText("SỐ TÍN CHỈ");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel4Layout.createSequentialGroup()
                                .addGap(157, 157, 157)
                                .addComponent(txtSearchMH, javax.swing.GroupLayout.DEFAULT_SIZE, 284, Short.MAX_VALUE)
                                .addGap(18, 18, 18)
                                .addComponent(btnTimKiemMH)
                                .addGap(170, 170, 170))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel4Layout.createSequentialGroup()
                                .addGap(70, 70, 70)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel14)
                                    .addComponent(jLabel4)
                                    .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtMaMH, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtTenMH, javax.swing.GroupLayout.PREFERRED_SIZE, 204, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtSoTC, javax.swing.GroupLayout.PREFERRED_SIZE, 204, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(1, 1, 1)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnThemMH, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnCapNhatMH)))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jButton21, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnXoaMH, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(188, 188, 188))
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 943, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(18, Short.MAX_VALUE)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 328, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtSearchMH, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnTimKiemMH, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(29, 29, 29))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addComponent(btnThemMH)
                        .addGap(18, 18, 18)))
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtMaMH, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel14)
                    .addComponent(btnCapNhatMH))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(txtTenMH, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnXoaMH))
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jButton21))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtSoTC, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel15))))
                .addGap(30, 30, 30))
        );

        jTabbedPane1.addTab("tab3", jPanel4);

        tblDiem.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "MÃ SINH VIÊN", "MÃ MÔN HỌC", "ĐIỂM QT", "ĐIỂM THI", "ĐIỂM TB"
            }
        ));
        tblDiem.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblDiemMouseClicked(evt);
            }
        });
        jScrollPane5.setViewportView(tblDiem);

        btnResetDiem.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnResetDiem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/Refresh-icon.png"))); // NOI18N
        btnResetDiem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnResetDiemActionPerformed(evt);
            }
        });

        btnCapNhatDiem.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnCapNhatDiem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/Pencil-icon.png"))); // NOI18N
        btnCapNhatDiem.setText("CẬP NHẬT");
        btnCapNhatDiem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCapNhatDiemActionPerformed(evt);
            }
        });

        btnThemDiem.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnThemDiem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/add-icon.png"))); // NOI18N
        btnThemDiem.setText("THÊM");
        btnThemDiem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemDiemActionPerformed(evt);
            }
        });

        btnTimKiemDiem.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnTimKiemDiem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/search-icon (2).png"))); // NOI18N
        btnTimKiemDiem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTimKiemDiemActionPerformed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/student.png"))); // NOI18N
        jLabel5.setText("MÃ SINH VIÊN");

        jLabel16.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel16.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/class.png"))); // NOI18N
        jLabel16.setText("MÃ MÔN HỌC");

        jLabel17.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel17.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/Move-icon (2).png"))); // NOI18N
        jLabel17.setText("ĐIỂM QT");

        jLabel18.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel18.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/Adressbook-icon (2).png"))); // NOI18N
        jLabel18.setText("ĐIỂM THI");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addGap(64, 64, 64)
                .addComponent(txtDiem, javax.swing.GroupLayout.DEFAULT_SIZE, 266, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(btnTimKiemDiem)
                .addGap(53, 53, 53)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel18)
                        .addGap(18, 18, 18)
                        .addComponent(txtDiemThi, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel17)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(btnThemDiem)
                        .addGap(18, 18, 18)
                        .addComponent(btnCapNhatDiem, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(31, 31, 31)
                        .addComponent(btnResetDiem, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(176, 176, 176))
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane5)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(jLabel16)
                                .addGap(18, 18, 18)
                                .addComponent(txtMHDiem, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addGap(18, 18, 18)
                                .addComponent(txtMSVDiem, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(221, 221, 221)
                        .addComponent(txtDiemQT, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 282, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnResetDiem)
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnCapNhatDiem)
                        .addComponent(btnThemDiem))
                    .addComponent(btnTimKiemDiem, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtDiem))
                .addGap(45, 45, 45)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(txtMSVDiem, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel17)
                    .addComponent(txtDiemQT, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(23, 23, 23)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16)
                    .addComponent(txtMHDiem, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel18)
                    .addComponent(txtDiemThi, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(136, 136, 136))
        );

        jTabbedPane1.addTab("tab4", jPanel5);

        jPanel7.setBackground(new java.awt.Color(255, 102, 102));
        jPanel7.setForeground(new java.awt.Color(255, 255, 255));

        jLabel20.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(255, 255, 255));
        jLabel20.setText("MÔN HỌC");

        jLabel21.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/Computer-Photo-Media-Creative-icon (3).png"))); // NOI18N

        lblMonHoc.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        lblMonHoc.setForeground(new java.awt.Color(255, 255, 255));
        lblMonHoc.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGap(16, 16, 16)
                        .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblMonHoc, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel20)))
                .addGap(29, 29, 29))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel21)
                    .addComponent(lblMonHoc, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel20)
                .addGap(17, 17, 17))
        );

        jPanel8.setBackground(new java.awt.Color(0, 204, 204));

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("KHOA");

        jLabel22.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/File-Document-File-Home-Folder-Folder-Home-Document-icon (2).png"))); // NOI18N
        jLabel22.setText("jLabel22");

        lblKhoa.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        lblKhoa.setForeground(new java.awt.Color(255, 255, 255));
        lblKhoa.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel6))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGap(33, 33, 33)
                        .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 39, Short.MAX_VALUE)
                        .addComponent(lblKhoa, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(21, 21, 21))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addContainerGap(33, Short.MAX_VALUE)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel22, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lblKhoa, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel6)
                .addGap(20, 20, 20))
        );

        jPanel9.setBackground(new java.awt.Color(0, 153, 102));

        jLabel19.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(255, 255, 255));
        jLabel19.setText("SINH VIÊN");

        jLabel23.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/User-Group-icon (2).png"))); // NOI18N

        lblSinhVien.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        lblSinhVien.setForeground(new java.awt.Color(255, 255, 255));
        lblSinhVien.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGap(31, 31, 31)
                        .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 55, Short.MAX_VALUE)
                        .addComponent(lblSinhVien, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(26, 26, 26))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                .addContainerGap(17, Short.MAX_VALUE)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel23, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lblSinhVien, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel19)
                .addGap(27, 27, 27))
        );

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, 229, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(50, 50, 50)
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 49, Short.MAX_VALUE)
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(28, 28, 28))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(402, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("tab5", jPanel6);

        getContentPane().add(jTabbedPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(208, -46, -1, 670));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnSinhVienActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSinhVienActionPerformed
        // TODO add your handling code here:
                jTabbedPane1.setSelectedIndex(0);
    }//GEN-LAST:event_btnSinhVienActionPerformed

    private void btnKhoaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnKhoaActionPerformed
        // TODO add your handling code here:
                jTabbedPane1.setSelectedIndex(1);

    }//GEN-LAST:event_btnKhoaActionPerformed

    private void btnMonHocActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMonHocActionPerformed
        // TODO add your handling code here:
                jTabbedPane1.setSelectedIndex(2);

    }//GEN-LAST:event_btnMonHocActionPerformed

    private void btnDiemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDiemActionPerformed
        // TODO add your handling code here:
                jTabbedPane1.setSelectedIndex(3);

    }//GEN-LAST:event_btnDiemActionPerformed

    private void btnThongKeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThongKeActionPerformed
        // TODO add your handling code here:
                jTabbedPane1.setSelectedIndex(4);

    }//GEN-LAST:event_btnThongKeActionPerformed

    private void btnThemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemActionPerformed
       try {
        String maSV = txtMaSV.getText();
        String hoTen = txtHoten.getText();
        String ngaySinh = txtNgaySinh.getText();
        int gioiTinh = radioNam.isSelected() ? 1 : 0;  // 1 = Nam, 0 = Nữ
        String diaChi = txtDiaChi.getText();
        String sdt = txtSDT.getText();
        String makhoa = txtMaKhoa.getText();
        String hinhanh = null;
        String monHoc = txtMonHoc.getText();
        
        Connection conn = DBconnect.getConnection();
        String sql = "INSERT INTO SinhVien (MaSV, HoTen, NgaySinh, GioiTinh, DiaChi, DienThoai, MaKhoa, Anh, MaMH) VALUES (?, ?, ?, ?, ?, ?,?,?,?)";
        
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, maSV);
        pstmt.setString(2, hoTen);
        pstmt.setString(3, ngaySinh);
        pstmt.setInt(4, gioiTinh);  // Lưu số thay vì chuỗi
        pstmt.setString(5, diaChi);
        pstmt.setString(6, sdt);
        pstmt.setString(7, makhoa);
        pstmt.setString(8, hinhanh);
        pstmt.setString(9, makhoa);

        
        int rows = pstmt.executeUpdate();
        ShowTable();
        loadCounts();
        updateCountLabels();
        if (rows > 0) {
            JOptionPane.showMessageDialog(this, "Thêm sinh viên thành công!");
        }
        
        conn.close();
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Lỗi: " + e.getMessage());
    }
    }//GEN-LAST:event_btnThemActionPerformed

    private void btnCapNhatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCapNhatActionPerformed
        // TODO add your handling code here:
         try {
             // Chuyển giới tính thành số trước khi lưu
        int gioiTinh = (radioNam.isSelected()) ? 1 : 0; 
        Connection conn = DBconnect.getConnection();
        String sql = "UPDATE SinhVien SET HoTen=?, NgaySinh=?, GioiTinh=?, DiaChi=?, DienThoai=?, MaKhoa=?, MaMH=? WHERE MaSV=?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
          
        pstmt.setString(1, txtHoten.getText());
        pstmt.setString(2, txtNgaySinh.getText());
        pstmt.setInt(3, gioiTinh); // Dùng setInt thay vì setString
        pstmt.setString(4, txtDiaChi.getText());
        pstmt.setString(5, txtSDT.getText());
        pstmt.setString(6, txtMaKhoa.getText());
        pstmt.setString(7, txtMonHoc.getText());
        pstmt.setString(8, txtMaSV.getText());

                int rows = pstmt.executeUpdate();
        conn.close();

        if (rows > 0) {
            JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
            ShowTable(); // Load lại dữ liệu sau khi cập nhật
        } else {
            JOptionPane.showMessageDialog(this, "Không tìm thấy sinh viên để cập nhật!");
        }
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Lỗi cập nhật: " + e.getMessage());
    }
    }//GEN-LAST:event_btnCapNhatActionPerformed

    private void btnXoaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoaActionPerformed
        // TODO add your handling code here:
        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn xóa?", "Xác nhận", JOptionPane.YES_NO_OPTION);
    if (confirm == JOptionPane.YES_OPTION) {
        try {
            Connection conn = DBconnect.getConnection();
            String sql = "DELETE FROM sinhvien WHERE MaSV=?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, txtMaSV.getText());

            int rows = pstmt.executeUpdate();
            conn.close();

            if (rows > 0) {
                JOptionPane.showMessageDialog(this, "Xóa thành công!");
                ShowTable(); // Load lại dữ liệu sau khi xóa
                loadCounts(); // Load số lượng từ cơ sở dữ liệu hoặc nguồn dữ liệu khác
                updateCountLabels(); // Hiển thị số lượng lên các label
            } else {
                JOptionPane.showMessageDialog(this, "Không tìm thấy sinh viên để xóa!");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi xóa: " + e.getMessage());
        }
    }
    }//GEN-LAST:event_btnXoaActionPerformed

    private void btnResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResetActionPerformed
        // TODO add your handling code here:
        txtMaSV.setText("");
        txtHoten.setText("");
        txtNgaySinh.setText("");
        txtDiaChi.setText("");
        txtSDT.setText("");
        txtMaKhoa.setText("");
        txtMonHoc.setText("");
    }//GEN-LAST:event_btnResetActionPerformed

    private void tblSinhVienMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblSinhVienMouseClicked
        // TODO add your handling code here:
         int selectedRow = tblSinhVien.getSelectedRow();
    if (selectedRow >= 0) {
        // Lấy dữ liệu từ các cột của hàng được chọn
        String maHocVien = dfModel.getValueAt(selectedRow, 0).toString();
        String hoTen = dfModel.getValueAt(selectedRow, 1).toString();
        String ngaySinh = dfModel.getValueAt(selectedRow, 2).toString();
        String gioiTinh = dfModel.getValueAt(selectedRow, 3).toString();
        String soDienThoai = dfModel.getValueAt(selectedRow, 5).toString();
        String diaChi = dfModel.getValueAt(selectedRow, 4).toString();
        String maKhoa = dfModel.getValueAt(selectedRow, 6).toString(); // Cột "Mã Khoa"
        String monHoc = dfModel.getValueAt(selectedRow, 7).toString(); 


        // Gán dữ liệu lên các trường nhập liệu
        txtMaSV.setText(maHocVien);
        txtHoten.setText(hoTen);
        txtNgaySinh.setText(ngaySinh);
        txtSDT.setText(soDienThoai);
        txtDiaChi.setText(diaChi);
        radioNam.setSelected(gioiTinh.equals("Nam"));
        radioNu.setSelected(gioiTinh.equals("Nữ"));
        txtMaKhoa.setText(maKhoa);
        txtMonHoc.setText(monHoc);
    }
    }//GEN-LAST:event_tblSinhVienMouseClicked

    private void btnTimKiemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTimKiemActionPerformed
        // TODO add your handling code here:
         try {
        Connection conn = DBconnect.getConnection();
        String sql = "SELECT * FROM SinhVien WHERE MaSV=?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, txtSearch.getText()); // Lấy mã sinh viên từ ô tìm kiếm

        ResultSet rs = pstmt.executeQuery();

        DefaultTableModel model = (DefaultTableModel) tblSinhVien.getModel();
        model.setRowCount(0); // Xóa dữ liệu cũ

        while (rs.next()) {
            String gioiTinh;
            if (rs.getObject("GioiTinh") instanceof Integer) {
                gioiTinh = (rs.getInt("GioiTinh") == 1) ? "Nam" : "Nữ";
            } else {
                gioiTinh = rs.getString("GioiTinh");
            }

            Object[] row = {
                rs.getString("MaSV"),
                rs.getString("HoTen"),
                rs.getString("NgaySinh"),
                gioiTinh,
                rs.getString("DiaChi"),
                rs.getString("DienThoai")
            };
            model.addRow(row);
        }

        conn.close();
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Lỗi tìm kiếm: " + e.getMessage());
    }
    }//GEN-LAST:event_btnTimKiemActionPerformed

    private void btnThemAnhActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemAnhActionPerformed
        // TODO add your handling code here:
         JFileChooser fileChooser = new JFileChooser();
    int result = fileChooser.showOpenDialog(this);

    if (result == JFileChooser.APPROVE_OPTION) {
        File selectedFile = fileChooser.getSelectedFile();
        ImageIcon icon = new ImageIcon(selectedFile.getAbsolutePath());
        
        // Đảm bảo kích thước ảnh phù hợp với kích thước của anhlaber
        Image img = icon.getImage(); 
        Image newimg = img.getScaledInstance(anhlaber.getWidth(), anhlaber.getHeight(), java.awt.Image.SCALE_SMOOTH); 
        icon = new ImageIcon(newimg);
        
        anhlaber.setIcon(icon);
    }
    }//GEN-LAST:event_btnThemAnhActionPerformed

    private void btnThemKhoaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemKhoaActionPerformed
        // TODO add your handling code here:
         String maKhoa = txtmakhoakhoa.getText();
         String tenKhoa = txtTenKhoa.getText();

    if (maKhoa.isEmpty() || tenKhoa.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!");
        return;
    }

    Khoa k = new Khoa(maKhoa, tenKhoa);
    if (khoaService.addKhoa(k)) {
        JOptionPane.showMessageDialog(this, "Thêm thành công!");
        loadKhoa();
        loadCounts(); // Load số lượng từ cơ sở dữ liệu hoặc nguồn dữ liệu khác
        updateCountLabels(); // Hiển thị số lượng lên các label
    } else {
        JOptionPane.showMessageDialog(this, "Thêm thất bại!");
    }
    }//GEN-LAST:event_btnThemKhoaActionPerformed

    private void tblKhoaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblKhoaMouseClicked
        // TODO add your handling code here:
        int selectedRow = tblKhoa.getSelectedRow(); // Lấy chỉ mục dòng được chọn
        if (selectedRow >= 0) {
            // Lấy dữ liệu từ bảng và hiển thị vào các ô nhập liệu
            txtmakhoakhoa.setText(tblKhoa.getValueAt(selectedRow, 0).toString());
            txtTenKhoa.setText(tblKhoa.getValueAt(selectedRow, 1).toString());
        }
    }//GEN-LAST:event_tblKhoaMouseClicked

    private void btnKhoaCapNhatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnKhoaCapNhatActionPerformed
        // TODO add your handling code here:
        String maKhoa = txtmakhoakhoa.getText();
        String tenKhoa = txtTenKhoa.getText();

    if (maKhoa.isEmpty() || tenKhoa.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!");
        return;
    }

    Khoa k = new Khoa(maKhoa, tenKhoa);
    if (khoaService.updateKhoa(k)) {
        JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
        loadKhoa();
    } else {
        JOptionPane.showMessageDialog(this, "Cập nhật thất bại!");
    }
    }//GEN-LAST:event_btnKhoaCapNhatActionPerformed

    private void btnKhoaXoaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnKhoaXoaActionPerformed
        // TODO add your handling code here:
        String maKhoa = txtmakhoakhoa.getText();

    if (maKhoa.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Vui lòng nhập mã khoa cần xóa!");
        return;
    }

    int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn xóa?", "Xác nhận", JOptionPane.YES_NO_OPTION);
    if (confirm == JOptionPane.YES_OPTION) {
        if (khoaService.deleteKhoa(maKhoa)) {
            JOptionPane.showMessageDialog(this, "Xóa thành công!");
            loadKhoa();
            loadCounts(); // Load số lượng từ cơ sở dữ liệu hoặc nguồn dữ liệu khác
            updateCountLabels(); // Hiển thị số lượng lên các label
        } else {
            JOptionPane.showMessageDialog(this, "Xóa thất bại!");
        }
    }
    }//GEN-LAST:event_btnKhoaXoaActionPerformed

    private void btnKhoaResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnKhoaResetActionPerformed
        // TODO add your handling code here:
        txtmakhoakhoa.setText("");
        txtTenKhoa.setText("");
    }//GEN-LAST:event_btnKhoaResetActionPerformed

    private void btnKhoaTimKiemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnKhoaTimKiemActionPerformed
        // TODO add your handling code here:
        String tenKhoa = txtKhoaSearch.getText();

    if (tenKhoa.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Vui lòng nhập tên khoa cần tìm!");
        return;
    }

    DefaultTableModel model = (DefaultTableModel) tblKhoa.getModel();
    model.setRowCount(0);

    List<Khoa> list = khoaService.searchKhoaByName(tenKhoa);
    for (Khoa k : list) {
        model.addRow(new Object[]{ k.getMaKhoa(), k.getTenKhoa() });
    }
    }//GEN-LAST:event_btnKhoaTimKiemActionPerformed

    private void tblMonHocMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblMonHocMouseClicked
        int selectedRow = tblMonHoc.getSelectedRow(); // Lấy dòng được chọn

            if (selectedRow != -1) { // Kiểm tra nếu có dòng được chọn
                String maMH = tblMonHoc.getValueAt(selectedRow, 0).toString();
                String tenMH = tblMonHoc.getValueAt(selectedRow, 1).toString();
                String soTinChi = tblMonHoc.getValueAt(selectedRow, 2).toString();

                // Hiển thị dữ liệu lên các JTextField
                txtMaMH.setText(maMH);
                txtTenMH.setText(tenMH);
                txtSoTC.setText(soTinChi);
            }
    }//GEN-LAST:event_tblMonHocMouseClicked

    private void btnThemMHActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemMHActionPerformed
        // TODO add your handling code here:
         String maMH = txtMaMH.getText();
    String tenMH = txtTenMH.getText();
    int soTinChi = Integer.parseInt(txtSoTC.getText());

    monHoc mh = new monHoc(maMH, tenMH, soTinChi);
    if (MonHocService.insertMonHoc(mh)) {
        JOptionPane.showMessageDialog(this, "Thêm thành công!");
        loadMonHoc();
        loadCounts(); // Load số lượng từ cơ sở dữ liệu hoặc nguồn dữ liệu khác
        updateCountLabels(); // Hiển thị số lượng lên các label
    } else {
        JOptionPane.showMessageDialog(this, "Thêm thất bại!");
    }
    }//GEN-LAST:event_btnThemMHActionPerformed

    private void btnCapNhatMHActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCapNhatMHActionPerformed
        // TODO add your handling code here:
        String maMH = txtMaMH.getText();
    String tenMH = txtTenMH.getText();
    int soTinChi = Integer.parseInt(txtSoTC.getText());

    monHoc mh = new monHoc(maMH, tenMH, soTinChi);
    if (MonHocService.updateMonHoc(mh)) {
        JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
        loadMonHoc();
    } else {
        JOptionPane.showMessageDialog(this, "Cập nhật thất bại!");
    }
    }//GEN-LAST:event_btnCapNhatMHActionPerformed

    private void btnXoaMHActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoaMHActionPerformed
        // TODO add your handling code here:
         String maMH = txtMaMH.getText();
    int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn xóa?", "Xác nhận", JOptionPane.YES_NO_OPTION);
    if (confirm == JOptionPane.YES_OPTION) {
        if (MonHocService.deleteMonHoc(maMH)) {
            JOptionPane.showMessageDialog(this, "Xóa thành công!");
            loadMonHoc();
            loadCounts(); // Load số lượng từ cơ sở dữ liệu hoặc nguồn dữ liệu khác
            updateCountLabels(); // Hiển thị số lượng lên các label
        } else {
            JOptionPane.showMessageDialog(this, "Xóa thất bại!");
        }
    }
    }//GEN-LAST:event_btnXoaMHActionPerformed

    private void btnTimKiemMHActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTimKiemMHActionPerformed
        // TODO add your handling code here:
        String tenMon = txtSearchMH.getText();

    DefaultTableModel model = (DefaultTableModel) tblMonHoc.getModel();
    model.setRowCount(0); // Xóa dữ liệu cũ
    List<monHoc> list = MonHocService.findByName(tenMon);

    for (monHoc mh : list) {
        model.addRow(new Object[]{ mh.getMaMon(), mh.getTenMon(), mh.getSoTinChi() });
    }
    }//GEN-LAST:event_btnTimKiemMHActionPerformed

    private void btnThemDiemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemDiemActionPerformed
        String maSV = txtMSVDiem.getText();
        String maMH = txtMHDiem.getText();
    
    try {
        float diemQT = Float.parseFloat(txtDiemQT.getText());
        float diemThi = Float.parseFloat(txtDiemThi.getText());

        // Tính điểm trung bình
        float diemTB = (diemQT + diemThi) / 2;

        // Tạo đối tượng Diem với DiemTB
        Diem diem = new Diem(maSV, maMH, diemQT, diemThi, diemTB);

        if (diemService.insertDiem(diem)) {
            JOptionPane.showMessageDialog(this, "Thêm điểm thành công!");
            loadDiem(); // Load lại danh sách điểm
        } else {
            JOptionPane.showMessageDialog(this, "Thêm thất bại!");
        }
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this, "Vui lòng nhập điểm hợp lệ!");
    }
    }//GEN-LAST:event_btnThemDiemActionPerformed

    private void btnCapNhatDiemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCapNhatDiemActionPerformed
        String maSV = txtMSVDiem.getText();
        String maMH = txtMHDiem.getText();

    try {
        float diemQT = Float.parseFloat(txtDiemQT.getText());
        float diemThi = Float.parseFloat(txtDiemThi.getText());

        // Tính điểm trung bình
        float diemTB = (diemQT + diemThi) / 2;

        // Tạo đối tượng Diem với DiemTB
        Diem diem = new Diem(maSV, maMH, diemQT, diemThi, diemTB);

        if (diemService.updateDiem(diem)) {
            JOptionPane.showMessageDialog(this, "Cập nhật điểm thành công!");
            loadDiem(); // Load lại danh sách điểm
        } else {
            JOptionPane.showMessageDialog(this, "Cập nhật thất bại!");
        }
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this, "Vui lòng nhập điểm hợp lệ!");
    }
    }//GEN-LAST:event_btnCapNhatDiemActionPerformed

    private void btnTimKiemDiemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTimKiemDiemActionPerformed
        // TODO add your handling code here:
        String maSV  = txtDiem.getText();

    DefaultTableModel model = (DefaultTableModel) tblDiem.getModel();
    model.setRowCount(0); // Xóa dữ liệu cũ
    List<Diem> list = diemService.findByMaSV(maSV);

    for (Diem d : list) {
        model.addRow(new Object[]{ d.getMaSinhVien(), d.getMaMonHoc(), d.getDiemQT(), d.getDiemThi() });
    }
    }//GEN-LAST:event_btnTimKiemDiemActionPerformed

    private void tblDiemMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDiemMouseClicked
        // TODO add your handling code here:
        int selectedRow = tblDiem.getSelectedRow(); // Lấy dòng đang chọn
    if (selectedRow != -1) { // Kiểm tra nếu có dòng nào đó được chọn
        txtMSVDiem.setText(tblDiem.getValueAt(selectedRow, 0).toString());
        txtMHDiem.setText(tblDiem.getValueAt(selectedRow, 1).toString());
        txtDiemQT.setText(tblDiem.getValueAt(selectedRow, 2).toString());
        txtDiemThi.setText(tblDiem.getValueAt(selectedRow, 3).toString());
    }
    }//GEN-LAST:event_tblDiemMouseClicked

    private void btnResetDiemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResetDiemActionPerformed
        // TODO add your handling code here:
        txtMSVDiem.setText("");
        txtMHDiem.setText("");
        txtDiemQT.setText("");
        txtDiemThi.setText("");
    }//GEN-LAST:event_btnResetDiemActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(sinhvienVIew.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(sinhvienVIew.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(sinhvienVIew.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(sinhvienVIew.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new sinhvienVIew().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel anhlaber;
    private javax.swing.JButton btnCapNhat;
    private javax.swing.JButton btnCapNhatDiem;
    private javax.swing.JButton btnCapNhatMH;
    private javax.swing.JButton btnDiem;
    private javax.swing.JButton btnKhoa;
    private javax.swing.JButton btnKhoaCapNhat;
    private javax.swing.JButton btnKhoaReset;
    private javax.swing.JButton btnKhoaTimKiem;
    private javax.swing.JButton btnKhoaXoa;
    private javax.swing.JButton btnMonHoc;
    private javax.swing.JButton btnReset;
    private javax.swing.JButton btnResetDiem;
    private javax.swing.JButton btnSinhVien;
    private javax.swing.JButton btnThem;
    private javax.swing.JButton btnThemAnh;
    private javax.swing.JButton btnThemDiem;
    private javax.swing.JButton btnThemKhoa;
    private javax.swing.JButton btnThemMH;
    private javax.swing.JButton btnThongKe;
    private javax.swing.JButton btnTimKiem;
    private javax.swing.JButton btnTimKiemDiem;
    private javax.swing.JButton btnTimKiemMH;
    private javax.swing.JButton btnXoa;
    private javax.swing.JButton btnXoaMH;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton jButton21;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JLabel lblKhoa;
    private javax.swing.JLabel lblMonHoc;
    private javax.swing.JLabel lblSinhVien;
    private javax.swing.JRadioButton radioNam;
    private javax.swing.JRadioButton radioNu;
    private javax.swing.JTable tblDiem;
    private javax.swing.JTable tblKhoa;
    private javax.swing.JTable tblMonHoc;
    private javax.swing.JTable tblSinhVien;
    private javax.swing.JTextArea txtDiaChi;
    private javax.swing.JTextField txtDiem;
    private javax.swing.JTextField txtDiemQT;
    private javax.swing.JTextField txtDiemThi;
    private javax.swing.JTextField txtHoten;
    private javax.swing.JTextField txtKhoaSearch;
    private javax.swing.JTextField txtMHDiem;
    private javax.swing.JTextField txtMSVDiem;
    private javax.swing.JTextField txtMaKhoa;
    private javax.swing.JTextField txtMaMH;
    private javax.swing.JTextField txtMaSV;
    private javax.swing.JTextField txtMonHoc;
    private javax.swing.JTextField txtNgaySinh;
    private javax.swing.JTextField txtSDT;
    private javax.swing.JTextField txtSearch;
    private javax.swing.JTextField txtSearchMH;
    private javax.swing.JTextField txtSoTC;
    private javax.swing.JTextField txtTenKhoa;
    private javax.swing.JTextField txtTenMH;
    private javax.swing.JTextField txtmakhoakhoa;
    // End of variables declaration//GEN-END:variables
}
