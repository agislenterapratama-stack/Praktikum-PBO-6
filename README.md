[README (2).md](https://github.com/user-attachments/files/34778050/README.2.md)
# Praktikum-PBO-6# Pengelola Data Nilai Siswa — Program Java Procedural

> **Satu file, tiga langkah:** input dari keyboard → tulis ke file teks → baca kembali file itu dan tampilkan di konsol.
> File program: [`NilaiSiswa.java`](NilaiSiswa.java) · File hasil: `laporan_nilai.txt` (terbentuk otomatis saat program dijalankan)

| | |
|---|---|
| **Nama** | Agis Lentera Pratama |
| **NIM** | L0325016 |
| **Bahasa** | Java (prosedural / fungsional, tanpa OOP — 1 class, `main` + 2 fungsi bantuan) |
| **Class yang dipakai** | `Scanner`, `FileWriter`, `BufferedWriter`, `FileReader`, `BufferedReader` |
| **Aturan kelulusan** | Nilai ≥ 70 → **Lulus**, nilai < 70 → **Tidak Lulus** |

---

## 1. Tujuan Program

1. Membaca data siswa (nama + nilai) dari keyboard.
2. Menentukan status kelulusan tiap siswa **langsung di dalam loop** input.
3. Menyimpan semua data ke `laporan_nilai.txt`, ditambah ringkasan kelas: rata-rata, jumlah lulus, dan jumlah tidak lulus.
4. Membuktikan file itu benar-benar tersimpan: membacanya kembali baris demi baris dan menampilkan isinya di konsol.
5. Menangani error file (`IOException`) dan menutup semua resource dengan benar.

## 2. Alur Program (untuk dijelaskan di presentasi)

```
Keyboard (Scanner)
      │
      ▼
┌─────────────────────────────┐
│ 1. Minta jumlah siswa (n)   │  ← divalidasi: harus angka bulat > 0
│ 2. Loop i = 1 … n:          │
│    • input nama             │  ← tidak boleh kosong
│    • input nilai (0–100)    │  ← divalidasi: harus angka 0–100
│    • status = nilai ≥ 70 ?  │
│      "Lulus" : "Tidak Lulus"│
│    • total += nilai; hitung │
│      jumlah lulus / tidak   │
│    • tulis 1 baris ke file  │
│ 3. Tulis ringkasan di akhir │
│    file (rata-rata, lulus,  │
│    tidak lulus, total)      │
└─────────────────────────────┘
      │  FileWriter + BufferedWriter (try-with-resources)
      ▼
  laporan_nilai.txt  ──▶  FileReader + BufferedReader
      │                       │
      │                       ▼
      │               readLine() sampai null
      │                       │
      └──────────▶  tampilkan isi file di konsol
```

**Kalimat pembuka presentasi (30 detik):**
> "Program ini mengelola nilai siswa dalam tiga langkah. Pertama, data dibaca dari keyboard dengan `Scanner`. Kedua, data ditulis ke `laporan_nilai.txt` memakai `FileWriter` dan `BufferedWriter`, lengkap dengan ringkasan kelas. Ketiga, file itu dibaca kembali dengan `FileReader` dan `BufferedReader`, lalu ditampilkan — jadi konsol di akhir adalah bukti bahwa file benar-benar tersimpan, bukan sekadar tulisan di layar."

## 3. Struktur File

```
nilai-siswa-java/
├── NilaiSiswa.java      ← satu-satunya file kode (semua logika di sini)
├── README.md            ← file ini (penjelasan siap presentasi)
└── laporan_nilai.txt    ← TERBENTUK OTOMATIS saat program dijalankan
```

Tidak ada package, tidak ada class lain, tidak ada library luar — sengaja dibuat satu file supaya mudah dijalankan di NetBeans maupun dari CMD/terminal.

## 4. Penjelasan Kode per Bagian

### 4.1 Import — "meminjam" class dari library Java

| Baris | Artinya |
|---|---|
| `import java.util.Scanner;` | Membaca input keyboard (`System.in`). |
| `import java.io.FileWriter;` | Membuka/membuat file teks untuk **ditulis**. |
| `import java.io.BufferedWriter;` | Pembungkus `FileWriter` — penulisan ditampung di buffer dulu sehingga lebih cepat. |
| `import java.io.FileReader;` | Membuka file teks yang sudah ada untuk **dibaca**. |
| `import java.io.BufferedReader;` | Pembungkus `FileReader` — menyediakan `readLine()` (baca per baris). |
| `import java.io.IOException;` | Jenis error file (file tidak bisa dibuat/dibaca/ditulis). |
| `import java.util.InputMismatchException;` | Error saat pengguna mengetik huruf di tempat angka. |
| `import java.util.Locale;` | Aturan format angka — dipakai supaya desimal memakai titik (`85.5`). |

### 4.2 `main()` — titik awal program

`public static void main(String[] args)` adalah method yang pertama kali dicari JVM. Semua alur (input → tulis file → baca file) dipanggil dari sini, berurutan.

### 4.3 Langkah 1a — Input jumlah siswa

```java
Scanner scanner = new Scanner(System.in);
scanner.useLocale(Locale.US);
```

* `new Scanner(System.in)` — `System.in` adalah keyboard.
* `useLocale(Locale.US)` — supaya angka desimal memakai titik (`85.5`, bukan `85,5`). Di laptop berbahasa Indonesia, tanpa ini `Scanner` bisa menolak angka bertitik.
* Jumlah siswa dibaca dengan `nextInt()` di dalam `while (true)` + `break`: pertanyaan diulang sampai jawabannya angka bulat **lebih dari 0**. Kalau pengguna mengetik huruf, `catch (InputMismatchException)` menampilkan pesan ramah dan mengulang — program tidak crash.

> **Jebakan klasik yang wajib dijelaskan saat presentasi:** setelah `nextInt()` / `nextDouble()`, tombol Enter (`\n`) masih tertinggal di buffer. Karena itu selalu ada `scanner.nextLine()` sesudahnya untuk "membersihkan" Enter itu. Kalau baris ini dihapus, `nextLine()` nama siswa berikutnya akan membaca Enter kosong dan nama jadi terlewat.

### 4.4 Langkah 1b — Loop input siswa + tulis file

```java
try (BufferedWriter writer =
        new BufferedWriter(new FileWriter("laporan_nilai.txt"))) {
    for (int i = 1; i <= jumlahSiswa; i++) {
        // input nama (nextLine), input nilai (nextDouble, 0–100)
        String status = tentukanStatus(nilai);
        writer.write(i + ". Nama: " + nama + " | Nilai: " + ... + " | Status: " + status);
        writer.newLine();
    }
    // ... tulis ringkasan ...
} catch (IOException e) { ... }
```

* `for (int i = 1; i <= jumlahSiswa; i++)` — loop berjalan tepat `jumlahSiswa` kali.
* **Status ditentukan di dalam loop**, tepat setelah nilai dibaca, lewat fungsi `tentukanStatus()` (lihat 4.6).
* `totalNilai += nilai` — menumpuk total untuk rata-rata; `jumlahLulus++` / `jumlahTidakLulus++` menghitung yang lulus/tidak.
* `writer.write(...)` menulis teks, `writer.newLine()` pindah baris — keduanya per siswa, jadi isi file tersusun satu siswa per baris.
* `String.format(Locale.US, "%.2f", nilai)` — nilai ditampilkan dengan 2 angka desimal (`85.50`).
* Setelah loop, ringkasan ditulis di akhir file: rata-rata kelas, jumlah lulus, jumlah tidak lulus, total siswa.

### 4.5 Langkah 2 — Baca kembali file dan tampilkan

```java
try (BufferedReader reader =
        new BufferedReader(new FileReader("laporan_nilai.txt"))) {
    String baris;
    while ((baris = reader.readLine()) != null) {
        System.out.println(baris);
    }
} catch (IOException e) { ... }
```

* `readLine()` membaca **satu baris penuh**. Kalau sudah sampai akhir file, ia mengembalikan `null` — itulah kondisi berhentinya `while`. Jadi loop ini berhenti sendiri tepat di baris terakhir, tanpa perlu tahu dulu ada berapa baris.
* Yang tampil di konsol pada tahap ini **bukan data dari memori, melainkan isi file yang baru dibaca** — ini bukti bahwa penulisan file berhasil.

### 4.6 Fungsi bantuan (gaya prosedural / fungsional)

Program ini tidak membuat objek sama sekali — logika yang dipakai ulang dipisah menjadi fungsi `static` supaya `main()` tetap pendek dan mudah dibaca:

```java
public static String tentukanStatus(double nilai) {
    return (nilai >= 70) ? "Lulus" : "Tidak Lulus";
}

public static double hitungRataRata(double total, int jumlah) {
    if (jumlah == 0) return 0;          // pengaman: jangan bagi dengan nol
    return total / (double) jumlah;     // (double) = casting, supaya hasilnya desimal
}
```

* `tentukanStatus` memakai **operator ternary** `kondisi ? A : B` — `if-else` versi satu baris. Ambang 70 persis mengikuti soal.
* `hitungRataRata` memakai **casting** `(double) jumlah`: tanpa ini, pembagian dua bilangan bulat di Java menghasilkan bilangan bulat (desimalnya hilang).
* `static` artinya fungsi bisa dipanggil langsung dari `main()` tanpa membuat objek — ciri gaya prosedural.

### 4.7 Langkah 3 — Exception handling & resource management

| Mekanisme | Di mana | Fungsinya |
|---|---|---|
| `try-with-resources` — `try (BufferedWriter writer = ...)` | Langkah 1b dan Langkah 2 | `writer` / `reader` **otomatis ditutup** (`close()`) setelah blok selesai, bahkan kalau terjadi error di tengah. File tidak terkunci dan data tidak hilang. |
| `catch (IOException e)` | Setelah tiap `try` file | Kalau file gagal dibuat/dibaca/ditulis, program menampilkan pesan ramah (`Terjadi kesalahan saat ...`) dan tidak crash. `e.getMessage()` berisi penyebab teknisnya. |
| `catch (InputMismatchException e)` | Input jumlah & nilai | Kalau pengguna mengetik huruf di tempat angka, input dibuang (`scanner.nextLine()`) dan pertanyaan diulang. |
| `scanner.close()` | Akhir `main()` | Menutup `Scanner` supaya tidak ada resource yang bocor. |

**Kenapa tidak `close()` manual saja?** Karena kalau error terjadi sebelum baris `close()` tercapai, file tetap terbuka. `try-with-resources` menutupnya dalam semua kondisi — itu alasan soal memintanya.

## 5. Cara Menjalankan

### Di NetBeans

1. Buat project Java baru (atau buka file ini).
2. Salin `NilaiSiswa.java` ke project (package default / tanpa package).
3. **Run File** (`Shift + F6`) atau tombol Run.
4. Ketik input di jendela Output, tekan Enter tiap jawaban.
5. File `laporan_nilai.txt` terbentuk di folder project — buka untuk memeriksa isinya.

### Dari CMD / Terminal

```bash
javac NilaiSiswa.java
java NilaiSiswa
```

> Catatan: `FileWriter("laporan_nilai.txt")` memakai path relatif — file selalu dibuat di **folder tempat program dijalankan**. Kalau dijalankan dua kali, file lama **ditimpa** (bukan ditambah). Itu perilaku bawaan `FileWriter` tanpa parameter `append`.

## 6. Contoh Input & Output (terverifikasi — contoh uji)

**Input yang diketik (3 siswa):**

```
Masukkan jumlah siswa: 3
Nama siswa ke-1: Budi
Nilai Budi (0-100): 85
Nama siswa ke-2: Siti
Nilai Siti (0-100): 65
Nama siswa ke-3: Andi
Nilai Andi (0-100): 90
```

**Output di konsol:**

```
Data berhasil ditulis ke file laporan_nilai.txt

===== ISI FILE laporan_nilai.txt =====
=================================
        LAPORAN NILAI SISWA
=================================
1. Nama: Budi | Nilai: 85.00 | Status: Lulus
2. Nama: Siti | Nilai: 65.00 | Status: Tidak Lulus
3. Nama: Andi | Nilai: 90.00 | Status: Lulus
=================================
RINGKASAN KELAS
Rata-rata nilai kelas : 80.00
Jumlah siswa lulus    : 2 siswa
Jumlah siswa tidak lulus : 1 siswa
Total siswa           : 3 siswa
===== AKHIR FILE =====

Program selesai.
```

**Isi `laporan_nilai.txt`** sama persis dengan blok di antara `===== ISI FILE =====` dan `===== AKHIR FILE =====` di atas. Perhitungan: (85 + 65 + 90) / 3 = 240 / 3 = **80.00**; lulus 2 (Budi, Andi), tidak lulus 1 (Siti).

## 7. Tabel Pengujian

| No | Skenario | Input | Hasil yang diharapkan | Status |
|---|---|---|---|---|
| 1 | Kasus normal | 3 siswa: Budi 85, Siti 65, Andi 90 | Rata-rata 80.00; lulus 2, tidak lulus 1 | ✓ Terverifikasi |
| 2 | Batas lulus (tepat 70) | 1 siswa, nilai 70 | Status **Lulus** (karena `>= 70`) | ✓ Sesuai aturan |
| 3 | Di bawah batas | 1 siswa, nilai 69.99 | Status **Tidak Lulus** | ✓ Sesuai aturan |
| 4 | Nilai di luar rentang | Nilai 105, lalu 85 | Pesan "Nilai harus antara 0 dan 100", diminta ulang, lalu diterima | ✓ Validasi aktif |
| 5 | Input bukan angka | Jumlah siswa: `tiga`, lalu `3` | Pesan "Input harus berupa angka bulat", diminta ulang | ✓ Validasi aktif |
| 6 | Nama kosong | Nama: (hanya Enter), lalu `Budi` | Pesan "Nama tidak boleh kosong", diminta ulang | ✓ Validasi aktif |
| 7 | Jumlah siswa 0 / negatif | Jumlah: `0`, lalu `2` | Pesan "Jumlah siswa harus lebih dari 0", diminta ulang | ✓ Validasi aktif |
| 8 | File dibaca kembali | Jalankan program sampai selesai | Isi file tampil lengkap di konsol setelah `===== ISI FILE =====` | ✓ Terverifikasi |

## 8. Pertanyaan yang Sering Ditanyakan Saat Presentasi

**Q: Kenapa pakai `BufferedWriter`, bukan `FileWriter` saja?**
A: `FileWriter` menulis langsung ke disk tiap kali dipanggil — lambat kalau sering. `BufferedWriter` menampung dulu di memori (buffer) lalu menulis sekaligus. Fungsinya sama, `BufferedWriter` hanya lebih efisien. Hal yang sama berlaku untuk `BufferedReader` vs `FileReader`.

**Q: Apa beda `FileWriter` dan `FileReader`?**
A: `FileWriter` = **menulis** (membuat file baru / menimpa file lama). `FileReader` = **membaca** file yang sudah ada. Keduanya bekerja pada file teks.

**Q: Kenapa `readLine()` berhenti di `null`?**
A: `readLine()` mengembalikan `null` kalau sudah tidak ada baris lagi (akhir file / EOF). Jadi `while ((baris = reader.readLine()) != null)` artinya "baca terus sampai habis" — tanpa perlu menghitung jumlah baris dulu.

**Q: Kenapa harus ada `scanner.nextLine()` setelah `nextInt()` / `nextDouble()`?**
A: `nextInt()` hanya mengambil angkanya; tombol Enter yang ditekan pengguna masih tertinggal di buffer. Tanpa dibersihkan, `nextLine()` berikutnya akan membaca Enter kosong itu dan input nama jadi terlewat. Ini bug paling umum di program `Scanner` — dan paling sering ditanyakan.

**Q: Apa yang terjadi kalau `laporan_nilai.txt` sudah ada?**
A: Ditimpa seluruhnya dengan data baru (perilaku default `new FileWriter(namaFile)`). Kalau ingin menambah tanpa menghapus isi lama, pakai `new FileWriter(namaFile, true)` — parameter kedua `true` berarti mode *append*.

**Q: Kenapa status dan rata-rata dipisah jadi fungsi, bukan ditulis langsung di `main()`?**
A: Supaya `main()` tetap pendek dan mudah dibaca (fokus pada alur), dan logikanya bisa dipakai ulang. Inilah inti gaya prosedural/fungsional: pecah masalah besar menjadi fungsi-fungsi kecil yang masing-masing mengerjakan satu hal.

**Q: Kenapa program ini tidak memakai OOP (class `Siswa`, ArrayList, dll.)?**
A: Karena soal memang meminta gaya prosedural/fungsional sederhana: satu file, alur lurus, fokus pada `Scanner` + file I/O + exception handling. Versi OOP (class `Siswa` dengan enkapsulasi, `ArrayList<Siswa>`) adalah pengembangan naturalnya — lihat bagian 9.

## 9. Keterbatasan & Pengembangan Lanjutan

* **File ditimpa tiap kali dijalankan** — data lama hilang. Pengembangan: mode *append* atau nama file berisi tanggal.
* **Data hanya tersimpan di file teks** — tidak bisa dicari/diurutkan dengan mudah. Pengembangan: simpan sebagai CSV, atau baca kembali ke `ArrayList`.
* **Tidak ada fitur edit/hapus siswa** — program sekali jalan. Pengembangan: menu (tambah / lihat / hapus / keluar) dan class `Siswa` (OOP).
* **Validasi nilai 0–100** sudah ada, tetapi nama tidak divalidasi karakternya (angka di nama masih diterima) — sengaja, supaya program tetap sederhana.

---

*Kode lengkap dengan komentar penjelasan tiap sintaks ada di [`NilaiSiswa.java`](NilaiSiswa.java) — komentarnya ditulis supaya bisa dibacakan langsung saat presentasi.*
