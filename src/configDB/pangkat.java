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
public class pangkat {
    private String namadb = "pbo2_2310010474";
    private String url = "jdbc:mysql://localhost:3306/" + namadb;
    private String username = "root";
    private String password = "";
    private Connection koneksi; 
    public Integer VAR_idpangkat = null;
    public String VAR_nip = null;
    public String VAR_pangkat = null;    
    public boolean validasi = false;

    public pangkat(){
        try {
            Driver mysqlDriver = new com.mysql.cj.jdbc.Driver();
            DriverManager.registerDriver(mysqlDriver);
            koneksi = DriverManager.getConnection(url, username, password);
            System.out.println("Berhasil dikoneksikan ke database");
        } catch (SQLException error) {
            JOptionPane.showMessageDialog(null, error.getMessage());
        }
    }

    public void SimpanAnggota(Integer idpangkat, Integer nip, String pangkat){
        try {
            // gunakan nama tabel yang benar
            String sql = "INSERT INTO pangkat (id_pangkat, nip, pangkat) "
                       + "VALUES('"+idpangkat+"', '"+nip+"', '"+pangkat+"')";

            // cek apakah id sudah ada
            String cekPrimary = "SELECT * FROM pangkat WHERE id_pangkat = '"+idpangkat+"'";

            Statement check = koneksi.createStatement();
            ResultSet data = check.executeQuery(cekPrimary);

            if (data.next()) {
                JOptionPane.showMessageDialog(null, "ID Pangkat sudah terdaftar!");                
                this.VAR_nip = data.getString("nip");                
                this.VAR_pangkat = data.getString("pangkat");
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
    public void ubahAnggota(Integer idpangkat, Integer nip, String pangkat){
        try {
            // nama tabel dan kolom disesuaikan
            String sql = "UPDATE pangkat SET nip=?, pangkat=? WHERE id_pangkat=?";

            PreparedStatement perintah = koneksi.prepareStatement(sql);
            perintah.setInt(1, nip);
            perintah.setString(2, pangkat);
            perintah.setInt(3, idpangkat);
            perintah.executeUpdate();

            JOptionPane.showMessageDialog(null, "Data berhasil diubah!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Terjadi kesalahan: " + e.getMessage());
            e.printStackTrace();
        }
    }
    public void hapusAnggota01(Integer idpangkat){
        try {
            // hapus dari tabel yang benar
            String sql = "DELETE FROM pangkat WHERE id_pangkat='"+idpangkat+"'";
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
            modelTable.addColumn("ID Pangkat");
            modelTable.addColumn("Nip");
            modelTable.addColumn("Pangkat");
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


