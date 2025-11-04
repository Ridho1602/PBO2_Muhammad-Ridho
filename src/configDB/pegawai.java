/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package configDB;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Driver;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author ASUS
 */
public class pegawai {
    private String namadb = "pbo2_2310010474";
    private String url = "jdbc:mysql://localhost:3306/" + namadb;
    private String username = "root";
    private String password = "";
    private Connection koneksi; 
    public Integer VAR_nip = null;
    public String VAR_nama = null;
    public String VAR_tempatlahir = null;    
    public String VAR_alamat = null;    
    public String VAR_status = null;    
    public boolean validasi = false;
    
    public pegawai(){
        try {
            Driver mysqlDriver = new com.mysql.cj.jdbc.Driver();
            DriverManager.registerDriver(mysqlDriver);
            koneksi = DriverManager.getConnection(url, username, password);
            System.out.println("Berhasil dikoneksikan ke database");
        } catch (SQLException error) {
            JOptionPane.showMessageDialog(null, error.getMessage());
        }
    }
     
    public void SimpanAnggota(Integer nip, String nama, String tempatlahir, String alamat, String status){
        try {
            // gunakan nama tabel yang benar
            String sql = "INSERT INTO pegawai (nip, nama, tempat_lahir, alamat, status)"
                       + "VALUES('"+nip+"', '"+nama+"', '"+tempatlahir+"','"+alamat+"', '"+status+"')";

            // cek apakah id sudah ada
            String cekPrimary = "SELECT * FROM pegawai WHERE nip = '"+nip+"'";

            Statement check = koneksi.createStatement();
            ResultSet data = check.executeQuery(cekPrimary);

            if (data.next()) {
                JOptionPane.showMessageDialog(null, "NIP sudah terdaftar!");                
                this.VAR_nip = data.getInt("nip");                
                this.VAR_nama = data.getString("nama");
                this.VAR_tempatlahir = data.getString("tempat lahir");
                this.VAR_alamat = data.getString("alamat");
                this.VAR_status = data.getString("status");
                this.validasi = true;
            } else {
                this.validasi = false;
                Statement perintah = koneksi.createStatement();
                perintah.executeUpdate(sql);
                JOptionPane.showMessageDialog(null, "Data berhasil disimpan!");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Terjadi kesalahan: " + e.getMessage());
            e.printStackTrace();
        }
    } 
    
    public void ubahAnggota(Integer nip, String nama, String tempatlahir, String alamat, String status){
        try {
            // nama tabel dan kolom disesuaikan
            String sql = "UPDATE pegawai SET nama=?, tempat_lahir=?, alamat=?, status=? WHERE nip=?";

            PreparedStatement perintah = koneksi.prepareStatement(sql);
            perintah.setString(1, nama);
            perintah.setString(2, tempatlahir);
            perintah.setString(3, alamat);
            perintah.setString(4, status);
            perintah.setInt(5, nip);
            perintah.executeUpdate();

            JOptionPane.showMessageDialog(null, "Data berhasil diubah!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Terjadi kesalahan: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public void hapusAnggota01(Integer nip){
        try {
            // hapus dari tabel yang benar
            String sql = "DELETE FROM pegawai WHERE nip='"+nip+"'";
            Statement perintah = koneksi.createStatement();
            perintah.execute(sql);
            JOptionPane.showMessageDialog(null, "Data berhasil dihapus!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Terjadi kesalahan: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public void tampilDataAnggota(JTable komponenTabel, String SQL){
        try {
            Statement perintah = koneksi.createStatement();
            ResultSet data = perintah.executeQuery(SQL);
            ResultSetMetaData meta = data.getMetaData();
            int jumKolom = meta.getColumnCount();
            DefaultTableModel modelTable = new DefaultTableModel();
            modelTable.addColumn("Nip");
            modelTable.addColumn("Nama");
            modelTable.addColumn("Tempat Lahir");
            modelTable.addColumn("Alamat");
            modelTable.addColumn("Status");
            modelTable.getDataVector().clear();
            modelTable.fireTableDataChanged();
            while (data.next()){
                Object[] row = new Object[jumKolom];
                for (int i = 1; i <= jumKolom; i++){
                    row [i - 1] = data.getObject(i);
                }
                modelTable.addRow(row);
            }
            
            komponenTabel.setModel(modelTable);
        } catch (Exception e) {
            
        }
    }

}