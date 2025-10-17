import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.awt.Font;

public class ProductMenu extends JFrame {
    public static void main(String[] args) {
        // buat object window
        ProductMenu menu = new ProductMenu();

        // atur ukuran window
        menu.setSize(700, 600);

        // letakkan window di tengah layar
        menu.setLocationRelativeTo(null);

        // isi window
        menu.setContentPane(menu.mainPanel);

        // ubah warna background
        menu.getContentPane().setBackground(Color.WHITE);

        // tampilkan window
        menu.setVisible(true);

        // agar program ikut berhenti saat window diclose
        menu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    // index baris yang diklik
    private int selectedIndex = -1;
    // list untuk menampung semua produk
    private Database database;

    private JPanel mainPanel;
    private JTextField idField;
    private JTextField namaField;
    private JTextField hargaField;
    private JTable productTable;
    private JButton addUpdateButton;
    private JButton cancelButton;
    private JComboBox<String> kategoriComboBox;
    private JComboBox<String> kondisiComboBox;
    private JButton deleteButton;
    private JLabel titleLabel;
    private JLabel idLabel;
    private JLabel namaLabel;
    private JLabel hargaLabel;
    private JLabel kategoriLabel;
    private JLabel kondisiLabel;

    // constructor
    public ProductMenu() {
        // inisialisasi listProduct

        // object database
        database = new Database();


        // isi tabel produk
        productTable.setModel(setTable());

        // ubah styling title
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 20f));

        // atur isi combo box
        String[] kategoriData = {"???", "Elektronik", "Makanan", "Minuman", "Pakaian", "Alat Tulis"};
        kategoriComboBox.setModel(new DefaultComboBoxModel<>(kategoriData));

        // atur isi combo box
        String[] kondisiData = {"???", "Baru", "Bekas", "Hancur"};
        kondisiComboBox.setModel(new DefaultComboBoxModel<>(kondisiData));

        // sembunyikan button delete
        deleteButton.setVisible(false);

        // saat tombol add/update ditekan
        addUpdateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectedIndex == -1) {
                    insertData();
                } else {
                    updateData();
                }
            }
        });

        // saat tombol delete ditekan
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO: tambahkan konfirmasi sebelum menghapus data
                int confirm = JOptionPane.showConfirmDialog(
                        ProductMenu.this,                      // parent component (bisa juga "this")
                        "Apakah kamu yakin ingin menghapus data ini?", // pesan konfirmasi
                        "Konfirmasi Hapus",                    // judul dialog
                        JOptionPane.YES_NO_OPTION,             // tombol yang ditampilkan
                        JOptionPane.QUESTION_MESSAGE           // ikon tanda tanya
                );

                // jika user menekan "Yes"
                if (confirm == JOptionPane.YES_OPTION) {
                    deleteData();
                }
            }
        });

        // saat tombol cancel ditekan
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearForm();
            }
        });

        // saat salah satu baris tabel ditekan
        productTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                // ubah selectedIndex menjadi baris tabel yang dikliki
                selectedIndex = productTable.getSelectedRow();

                // simpan value textfield dan combo box
                String curId = productTable.getModel().getValueAt(selectedIndex, 1).toString();
                String curNama = productTable.getModel().getValueAt(selectedIndex, 2).toString();
                String curHarga = productTable.getModel().getValueAt(selectedIndex, 3).toString();
                String curKategori = productTable.getModel().getValueAt(selectedIndex, 4).toString();
                String curKondisi = productTable.getModel().getValueAt(selectedIndex, 5).toString();

                // ubah isi textfield dan combo box
                idField.setText(curId);
                namaField.setText(curNama);
                hargaField.setText(curHarga);
                kategoriComboBox.setSelectedItem(curKategori);
                kondisiComboBox.setSelectedItem(curKondisi);

                // ubah button "Add" menjadi "Update"
                addUpdateButton.setText("Update");

                // tampilkan button delete
                deleteButton.setVisible(true);

            }
        });
    }

    public final DefaultTableModel setTable() {
        // tentukan kolom tabel
        Object[] cols = { "No", "ID Produk", "Nama", "Harga", "Kategori", "kondisi"};

        // buat objek tabel dengan kolom yang sudah dibuat
        DefaultTableModel tmp = new DefaultTableModel(null, cols);

        try {
            ResultSet resultSet = database.selectQuery("SELECT * FROM product");

            // isi tabel dengan hasil
            int i = 0;

            while (resultSet.next()) {
                Object[] row = new Object[6];
                row[0] = i + 1;
                row[1] = resultSet.getString("id");
                row[2] = resultSet.getString("nama");
                row[3] = resultSet.getString("harga");
                row[4] = resultSet.getString("kategori");
                row[5] = resultSet.getString("kondisi");
                tmp.addRow(row);
                i++;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return tmp; // return juga harus diganti
    }

    public void insertData() {
        try {
            // ambil value dari textfield dan combobox
            String id = idField.getText();
            String nama = namaField.getText();
            double harga = Double.parseDouble(hargaField.getText());
            String kategori = kategoriComboBox.getSelectedItem().toString();
            String kondisi = kondisiComboBox.getSelectedItem().toString();

            // Cek input kosong
            if (id.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Silahkan isi Id produk", "Input Kosong", JOptionPane.WARNING_MESSAGE);
                return;
            } else if (nama.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Silahkan isi Nama produk", "Input Kosong", JOptionPane.WARNING_MESSAGE);
                return;
            } else if (kategori == "???"){
                JOptionPane.showMessageDialog(null, "Silahkan isi Kategori produk", "Input Kosong", JOptionPane.WARNING_MESSAGE);
                return;
            } else if (kondisi == "???") {
                JOptionPane.showMessageDialog(null, "Silahkan isi Kondisi produk", "Input Kosong", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Cek id
            String checkQuery = "SELECT * FROM product WHERE id = '" + id + "'";
            ResultSet rs = database.selectQuery(checkQuery);

            if (rs.next()) {
                JOptionPane.showMessageDialog(null,"ID " + id + " sudah ada ! Gunakan ID lain.");
                return;
            }

            // tambahkan data ke dalam list
            String sqlQuery = "INSERT INTO product VALUES ('" + id + "', '" + nama + "', " + harga + ", '" + kategori
                    + "', '" + kondisi + "')";
            database.insertUpdateDeleteQuery(sqlQuery);

            // update tabel
            productTable.setModel(setTable());

            // bersihkan form
            clearForm();

            // feedback
            System.out.println("Insert berhasil");
            JOptionPane.showMessageDialog(null, "Data berhasil ditambahkan");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Harga harus berupa angka!", "Error",
                    JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Kesalahan SQL: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void updateData() {
        try {
            // ambil data dari form
            String id = idField.getText();
            String nama = namaField.getText();
            double harga = Double.parseDouble(hargaField.getText());
            String kategori = kategoriComboBox.getSelectedItem().toString();
            String kondisi = kondisiComboBox.getSelectedItem().toString();

            if (nama.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Silahkan isi Nama produk", "Input Kosong", JOptionPane.WARNING_MESSAGE);
                return;
            } else if (kategori == "???"){
                JOptionPane.showMessageDialog(null, "Silahkan isi Kategori produk", "Input Kosong", JOptionPane.WARNING_MESSAGE);
                return;
            } else if (kondisi == "???") {
                JOptionPane.showMessageDialog(null, "Silahkan isi Kondisi produk", "Input Kosong", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // ubah data produk di list
            String sqlQuery = "UPDATE product SET "
                    + "nama = '" + nama + "', "
                    + "harga = " + harga + ", "
                    + "kategori = '" + kategori + "', "
                    + "kondisi = '" + kondisi + "' "
                    + "WHERE id = '" + id + "'";
            database.insertUpdateDeleteQuery(sqlQuery);


            // update tabel
            productTable.setModel(setTable());

            // bersihkan form
            clearForm();

            // feedback
            System.out.println("Update berhasil");
            JOptionPane.showMessageDialog(null, "Data berhasil diubah");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Harga harus berupa angka!", "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public void deleteData() {
        try {
            // ambil id dari form atau tabel
            String id = idField.getText();

            // buat query delete
            String sqlQuery = "DELETE FROM product WHERE id = '" + id + "'";

            // jalankan query menggunakan class database helper
            database.insertUpdateDeleteQuery(sqlQuery);

            // update tabel
            productTable.setModel(setTable());

            // bersihkan form
            clearForm();

            // feedback
            System.out.println("Delete berhasil");
            JOptionPane.showMessageDialog(null, "Data berhasil dihapus");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Terjadi kesalahan saat menghapus data: " + e.getMessage());
        }
    }

    public void clearForm() {
        // kosongkan semua texfield dan combo box
        idField.setText("");
        namaField.setText("");
        hargaField.setText("");
        kategoriComboBox.setSelectedIndex(0);
        kondisiComboBox.setSelectedIndex(0);

        // ubah button "Update" menjadi "Add"
        addUpdateButton.setText("Add");

        // sembunyikan button delete
        deleteButton.setVisible(false);

        // ubah selectedIndex menjadi -1 (tidak ada baris yang dipilih)
        selectedIndex = -1;

    }
}