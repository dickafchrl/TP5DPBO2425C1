# JANJI
Saya Dicka Fachrunaldo Kartamiharja mengerjakan TP 4 dalam mata kuliah DPBO untuk keberkahanNya maka saya tidak melakukan kecurangan seperti yang telah dispesifikasikan. Aamiin.

# TP5DPBO2425C1

Pada dasarnya, program ini sama dengan TP 4 hanya saja di ubah pada beberapa bagian nya. Yaitu :
yang pertama adalah code saat ini menggunakan database dari sql dan tidak menggunakan arraylist lagi.
Dan yang kedua, kode ini sudah memiliki error handling apabila ada tabel data yang masih kosong / belum terisi.

## Penjelasan Alur Penggunaan Program

Program ini digunakan untuk menampung dan mengelola data produk dari sebuah toko (seperti Borma atau swalayan).
Setiap produk memiliki beberapa atribut, yaitu ID, Nama, Harga, Kategori, dan Kondisi.

Ketika program dijalankan:

Akan muncul jendela aplikasi utama (ProductMenu) dengan tampilan form input di bagian atas dan tabel data di bagian bawah.

Pengguna dapat menambahkan data baru dengan mengisi kolom:
- ID Produk
- Nama Produk
- Harga (dalam angka)
- Kategori (dipilih dari JComboBox berisi opsi seperti Elektronik, Makanan, Minuman, dll.)
- Kondisi (dipilih dari JComboBox seperti Baru, Bekas, Hancur)

Lalu menekan tombol Add untuk menyimpan data ke dalam ArrayList (penyimpanan sementara).
Data yang berhasil ditambahkan langsung muncul pada tabel di bawah.

Untuk mengubah data, pengguna tinggal klik salah satu baris pada tabel, lalu kolom input akan otomatis terisi dengan data yang dipilih.
Setelah mengedit nilainya, tekan tombol Update untuk menyimpan perubahan.

Untuk menghapus data, pilih baris data di tabel lalu tekan tombol Delete.
Akan muncul dialog konfirmasi sebelum data benar-benar dihapus.

Tombol Cancel digunakan untuk membersihkan form input dan mengembalikan tombol Add/Update ke mode awal.


## penjelasan alur

### Class Product
Kelas ini merepresentasikan sebuah entitas produk dan memiliki 5 atribut bersifat private:
- id → kode unik produk
- nama → nama produk
- harga → harga dalam tipe double
- kategori → jenis produk (dipilih dari combo box)
- kondisi → keadaan barang (baru/bekas/hancur)

Setiap atribut memiliki getter dan setter agar bisa diakses dan diubah dari kelas lain.

### Class ProductMenu
Merupakan kelas utama GUI yang menampilkan jendela aplikasi dan mengatur semua logika program.
- main()
1. Membuat objek ProductMenu
2. Mengatur ukuran jendela, posisi di tengah layar, warna latar belakang, dan visibilitas GUI
3. Menentukan agar program berhenti saat jendela ditutup (EXIT_ON_CLOSE)

- Atribut GUI
Berisi berbagai komponen dari Java Swing:
1. JTextField untuk input ID, Nama, dan Harga
2. JComboBox untuk memilih Kategori dan Kondisi
3. JTable untuk menampilkan daftar produk
4. JButton untuk operasi Add/Update, Delete, dan Cancel
5. JLabel untuk label teks tampilan
6. ArrayList<Product> sebagai penyimpanan sementara data produk

- Constructor ProductMenu()
1. Menginisialisasi listProduct
2. Memanggil populateList() untuk menambahkan data awal (dummy)
3. Mengatur isi tabel dengan setTable()
4. Mengatur isi JComboBox kategori dan kondisi
5. Mengatur tampilan dan aksi tombol:
    a. addUpdateButton → menambah atau memperbarui data
    b. deleteButton → menghapus data (dengan dialog konfirmasi)
    c. cancelButton → membersihkan form
    d. Mengatur event klik tabel (MouseListener) agar data bisa dipilih untuk diedit atau dihapus.

## Dokumentasi
https://github.com/user-attachments/assets/7937839b-d3ca-42a4-81bf-d16271e49ba1
