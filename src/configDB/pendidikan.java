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
public class pendidikan {
    private String namadb = "pbo2_2310010474";
    private String url = "jdbc:mysql://localhost:3306/" + namadb;
    private String username = "root";
    private String password = "";
    private Connection koneksi; 
    public Integer VAR_idpendidikan = null;
    public String VAR_namapendidikan = null;
    public Integer VAR_nip = null;    
    public String VAR_jurusan = null;    
    public String VAR_tingkatpendidikan = null;    
    public String VAR_tahunlulus = null;    
    public boolean validasi = false;
    
    public pendidikan(){
        try {
            Driver mysqlDriver = new com.mysql.cj.jdbc.Driver();
            DriverManager.registerDriver(mysqlDriver);
            koneksi = DriverManager.getConnection(url, username, password);
            System.out.println("Berhasil dikoneksikan ke database");
        } catch (SQLException error) {
            JOptionPane.showMessageDialog(null, error.getMessage());
        }
    }
    
    public void SimpanAnggota(Integer idpendidikan, String namapendidikan, Integer nip, String jurusan, String tingkatpendidikan, String tahunlulus){
        try {
            // gunakan nama tabel yang benar
            String sql = "INSERT INTO pendidikan (id_pendidikan, nama_pendidikan, nip, jurusan, tingkat_pendidikan, tahun_lulus)"
                       + "VALUES('"+idpendidikan+"', '"+namapendidikan+"', '"+nip+"','"+jurusan+"', '"+tingkatpendidikan+"', '"+tahunlulus+"')";

            // cek apakah id sudah ada
            String cekPrimary = "SELECT * FROM pendidikan WHERE id_pendidikan = '"+idpendidikan+"'";

            Statement check = koneksi.createStatement();
            ResultSet data = check.executeQuery(cekPrimary);

            if (data.next()) {
                JOptionPane.showMessageDialog(null, "ID sudah terdaftar!");                
                this.VAR_idpendidikan = data.getInt("ID");                
                this.VAR_namapendidikan = data.getString("Nama Pendidikan");
                this.VAR_nip = data.getInt("NIP");
                this.VAR_jurusan = data.getString("Jurusan");
                this.VAR_tingkatpendidikan = data.getString("Tingkat Pendidikan");
                this.VAR_tahunlulus = data.getString("Tahun Lulus");
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
    
    public void ubahAnggota(Integer idpendidikan, String namapendidikan, Integer nip, String jurusan, String tingkatpendidikan, String tahunlulus){
        try {
            // nama tabel dan kolom disesuaikan
            String sql = "UPDATE pendidikan SET nama_pendidikan=?, nip=?, jurusan=?, tingkat_pendidikan=?, tahun_lulus=? WHERE id_pendidikan=?";

            PreparedStatement perintah = koneksi.prepareStatement(sql);
            perintah.setString(1, namapendidikan);
            perintah.setInt(2, nip);
            perintah.setString(3, jurusan);
            perintah.setString(4, tingkatpendidikan);
            perintah.setString(5, tahunlulus);
            perintah.setInt(6, nip);
            perintah.executeUpdate();

            JOptionPane.showMessageDialog(null, "Data berhasil diubah!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Terjadi kesalahan: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public void hapusAnggota01(Integer idpendidikan){
        try {
            // hapus dari tabel yang benar
            String sql = "DELETE FROM pendidikan WHERE id_pendidikan='"+idpendidikan+"'";
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
            modelTable.addColumn("ID Pendidikan");
            modelTable.addColumn("Nama Pendidikan");
            modelTable.addColumn("Nip");
            modelTable.addColumn("Jurusan");
            modelTable.addColumn("Tingkat Pendidikan");
            modelTable.addColumn("Tahub Lulus");
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
