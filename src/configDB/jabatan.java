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
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import java.sql.ResultSetMetaData;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author ASUS
 */
public class jabatan {
    private String namadb = "pbo2_2310010474";
    private String url = "jdbc:mysql://localhost:3306/" + namadb;
    private String username = "root";
    private String password = "";
    private Connection koneksi; 
    public Integer VAR_idjabatan = null;
    public Integer VAR_nip = null;
    public String VAR_jabatan = null;    
    public String VAR_masakerjatahun = null;    
    public String VAR_masakerjabulan = null;    
    public boolean validasi = false;
    
    public jabatan(){
        try {
            Driver mysqlDriver = new com.mysql.cj.jdbc.Driver();
            DriverManager.registerDriver(mysqlDriver);
            koneksi = DriverManager.getConnection(url, username, password);
            System.out.println("Berhasil dikoneksikan ke database");
        } catch (SQLException error) {
            JOptionPane.showMessageDialog(null, error.getMessage());
        }
    }
    
    public void SimpanAnggota(Integer idjabatan, Integer nip, String jabatan, String masakerjatahun, String masakerjabulan){
        try {
            // gunakan nama tabel yang benar
            String sql = "INSERT INTO jabatan (id_jabatan, nip, jabatan, masa_kerja_tahun, masa_kerja_bulan)"
                       + "VALUES('"+idjabatan+"', '"+nip+"', '"+jabatan+"','"+masakerjatahun+"', '"+masakerjabulan+"')";

            // cek apakah id sudah ada
            String cekPrimary = "SELECT * FROM jabatan WHERE id_jabatan = '"+idjabatan+"'";

            Statement check = koneksi.createStatement();
            ResultSet data = check.executeQuery(cekPrimary);

            if (data.next()) {
                JOptionPane.showMessageDialog(null, "ID sudah terdaftar!");                
                this.VAR_idjabatan = data.getInt("Id Jabatan");                
                this.VAR_nip = data.getInt("Nip");
                this.VAR_jabatan = data.getString("Jabatan");
                this.VAR_masakerjatahun = data.getString("Masa Kerja Tahun");
                this.VAR_masakerjabulan = data.getString("Masa Kerja Bulan");
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
    
    public void ubahAnggota(Integer idjabatan, Integer nip, String jabatan, String masakerjatahun, String masakerjabulan){
        try {
            // nama tabel dan kolom disesuaikan
            String sql = "UPDATE jabatan SET nip=?, jabatan=?, masa_kerja_tahun=?, masa_kerja_bulan=? WHERE id_jabatan=?";

            PreparedStatement perintah = koneksi.prepareStatement(sql);
            perintah.setInt(1, nip);
            perintah.setString(2, jabatan);
            perintah.setString(3, masakerjatahun);
            perintah.setString(4, masakerjabulan);
            perintah.setInt(5, idjabatan);
            perintah.executeUpdate();

            JOptionPane.showMessageDialog(null, "Data berhasil diubah!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Terjadi kesalahan: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public void hapusAnggota01(Integer idjabatan){
        try {
            // hapus dari tabel yang benar
            String sql = "DELETE FROM jabatan WHERE id_jabatan='"+idjabatan+"'";
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
            modelTable.addColumn("ID Jabatan");
            modelTable.addColumn("Nip");
            modelTable.addColumn("Jabatan");
            modelTable.addColumn("Masa Kerja Tahun");
            modelTable.addColumn("Masa Kerja Bulan");
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
