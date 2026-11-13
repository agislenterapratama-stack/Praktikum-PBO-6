/*
 * ============================================================================
 *  PROGRAM: Pengelola Data Nilai Siswa (Procedural / Fungsional, 1 file)
 *  File   : NilaiSiswa.java
 *  Penulis: Agis Lentera Pratama (L0325016)
 * ----------------------------------------------------------------------------
 *  CARA MENJALANKAN (di NetBeans: Run File, di CMD/Terminal):
 *      javac NilaiSiswa.java
 *      java NilaiSiswa
 *
 *  ALUR PROGRAM (3 langkah, sesuai soal):
 *      1. Input  dari keyboard (Scanner)  -> tulis ke laporan_nilai.txt
 *         (FileWriter + BufferedWriter), lengkap dengan ringkasan.
 *      2. Baca   kembali file tersebut (FileReader + BufferedReader)
 *         baris demi baris, lalu tampilkan di konsol.
 *      3. Semua proses file dibungkus try-catch / try-with-resources
 *         supaya IOException tertangani dan file selalu tertutup.
 * ============================================================================
 */

// --- PENJELASAN IMPORT -------------------------------------------------------
// import = "meminjam" class dari library Java supaya bisa dipakai di program.
// java.io.*  = class untuk baca/tulis file. java.util.* = Scanner, Locale, dll.
import java.io.BufferedReader;   // Membaca file teks baris demi baris (cepat).
import java.io.BufferedWriter;   // Menulis file teks baris demi baris (cepat).
import java.io.FileReader;       // Membuka file teks untuk DIBACA.
import java.io.FileWriter;       // Membuka/membuat file teks untuk DITULIS.
import java.io.IOException;      // Jenis error yang dilempar saat file bermasalah.
import java.util.InputMismatchException; // Error saat input bukan angka.
import java.util.Locale;         // Aturan format angka (titik vs koma desimal).
import java.util.Scanner;        // Membaca input dari keyboard (System.in).

// public class NilaiSiswa = "wadah" utama program.
// Nama class HARUS sama persis dengan nama file: NilaiSiswa.java
public class NilaiSiswa {

    // public static void main(String[] args) = titik awal program.
    // JVM selalu mencari method main() ini pertama kali saat program dijalankan.
    // String[] args = argumen dari command line (di sini tidak dipakai).
    public static void main(String[] args) {

        // --- LANGKAH 1a: SIAPKAN SCANNER (INPUT KEYBOARD) -------------------
        // new Scanner(System.in) = membuat objek pembaca dari keyboard.
        // System.in = aliran input standar, yaitu keyboard.
        Scanner scanner = new Scanner(System.in);

        // useLocale(Locale.US) = pakai titik (.) sebagai pemisah desimal,
        // contoh: 85.5 - bukan koma (85,5). Tanpa ini, di laptop berbahasa
        // Indonesia Scanner bisa menolak angka desimal bertitik.
        scanner.useLocale(Locale.US);

        // --- VARIABEL PENAMPUNG (disiapkan sebelum loop) --------------------
        // int    = bilangan bulat, double = bilangan desimal (nilai & rata-rata).
        int jumlahSiswa = 0;      // Berapa siswa yang akan diinput.
        double totalNilai = 0;    // Jumlah semua nilai, untuk menghitung rata-rata.
        int jumlahLulus = 0;      // Penghitung siswa yang lulus.
        int jumlahTidakLulus = 0; // Penghitung siswa yang tidak lulus.

        // --- MINTA JUMLAH SISWA (dengan validasi: harus bilangan > 0) -------
        // while (true) + break = ulangi pertanyaan sampai jawabannya valid.
        while (true) {
            System.out.print("Masukkan jumlah siswa: ");
            try {
                // nextInt() = membaca satu bilangan bulat dari keyboard.
                jumlahSiswa = scanner.nextInt();
                // nextLine() di bawah ini WAJIB: nextInt() hanya mengambil
                // angkanya, tombol Enter (\n) masih tertinggal di buffer.
                // Kalau tidak dibersihkan, nextLine() nama siswa berikutnya
                // akan membaca Enter kosong itu dan namanya jadi terlewat.
                scanner.nextLine();
                if (jumlahSiswa <= 0) {
                    System.out.println("Jumlah siswa harus lebih dari 0. Coba lagi.");
                } else {
                    break; // Input valid -> keluar dari while.
                }
            } catch (InputMismatchException e) {
                // Blok ini jalan kalau pengguna mengetik huruf, bukan angka.
                // e = objek error-nya; pesannya tidak perlu ditampilkan.
                System.out.println("Input harus berupa angka bulat. Coba lagi.");
                scanner.nextLine(); // Buang input yang salah dari buffer.
            }
        }

        // --- LANGKAH 1b: TULIS DATA KE FILE (FileWriter + BufferedWriter) ---
        // try-with-resources: tulis ( ... ) setelah kata try.
        // Artinya: objek di dalam kurung OTOMATIS ditutup (close) setelah
        // blok selesai - bahkan kalau terjadi error di tengah jalan.
        // FileWriter("laporan_nilai.txt") = buat file baru (atau timpa file
        // lama dengan nama yang sama) di folder yang sama dengan program.
        // BufferedWriter = pembungkus FileWriter supaya penulisan per baris
        // lebih cepat (ditampung dulu di buffer, baru ditulis sekaligus).
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("laporan_nilai.txt"))) {

            // write() = menulis teks ke file (belum pindah baris).
            // newLine() = pindah ke baris baru (sama seperti menekan Enter).
            writer.write("=================================");
            writer.newLine();
            writer.write("        LAPORAN NILAI SISWA      ");
            writer.newLine();
            writer.write("=================================");
            writer.newLine();

            // --- LOOPING: baca data tiap siswa, satu per satu ---------------
            // for (int i = 1; i <= jumlahSiswa; i++) artinya:
            // mulai i dari 1; selama i <= jumlahSiswa, jalankan isi loop,
            // lalu i++ (i bertambah 1) dan ulangi. Jadi loop jalan tepat
            // sebanyak jumlah siswa.
            for (int i = 1; i <= jumlahSiswa; i++) {

                // --- Input NAMA (String) ---
                // nextLine() = membaca satu baris teks penuh (boleh ada spasi).
                System.out.print("Nama siswa ke-" + i + ": ");
                String nama = scanner.nextLine().trim(); // trim() = buang spasi ujung.
                // Kalau nama dikosongkan (hanya Enter), minta ulang.
                while (nama.isEmpty()) {
                    System.out.print("Nama tidak boleh kosong. Nama siswa ke-" + i + ": ");
                    nama = scanner.nextLine().trim();
                }

                // --- Input NILAI (double, dengan validasi 0 s.d. 100) -------
                double nilai = 0;
                while (true) {
                    System.out.print("Nilai " + nama + " (0-100): ");
                    try {
                        // nextDouble() = membaca satu bilangan desimal.
                        nilai = scanner.nextDouble();
                        scanner.nextLine(); // Bersihkan Enter sisa, seperti di atas.
                        if (nilai < 0 || nilai > 100) {
                            System.out.println("Nilai harus antara 0 dan 100. Coba lagi.");
                        } else {
                            break; // Nilai valid -> keluar dari while.
                        }
                    } catch (InputMismatchException e) {
                        System.out.println("Input harus berupa angka. Coba lagi.");
                        scanner.nextLine(); // Buang input yang salah.
                    }
                }

                // --- TENTUKAN STATUS KELULUSAN (di dalam loop, langsung) ----
                // Memanggil fungsi tentukanStatus() di bawah (lihat paling bawah).
                // Aturan soal: nilai >= 70 -> "Lulus", nilai < 70 -> "Tidak Lulus".
                String status = tentukanStatus(nilai);

                // --- HITUNG TOTAL & JUMLAH LULUS / TIDAK LULUS ---------------
                // totalNilai += nilai  sama artinya dengan: totalNilai = totalNilai + nilai
                totalNilai += nilai;
                // if-else: kalau statusnya "Lulus", tambah penghitung lulus,
                // kalau bukan, tambah penghitung tidak lulus.
                if (status.equals("Lulus")) {
                    jumlahLulus++;       // jumlahLulus = jumlahLulus + 1
                } else {
                    jumlahTidakLulus++;  // jumlahTidakLulus = jumlahTidakLulus + 1
                }

                // --- TULIS SATU BARIS DATA SISWA KE FILE --------------------
                // String.format("%.2f", nilai) = format nilai jadi 2 angka
                // di belakang koma, contoh: 85.5 -> "85.50".
                writer.write(i + ". Nama: " + nama
                        + " | Nilai: " + String.format(Locale.US, "%.2f", nilai)
                        + " | Status: " + status);
                writer.newLine();
            }

            // --- RINGKASAN DI AKHIR FILE ------------------------------------
            // Rata-rata dihitung lewat fungsi hitungRataRata() di bawah.
            double rataRata = hitungRataRata(totalNilai, jumlahSiswa);
            writer.write("=================================");
            writer.newLine();
            writer.write("RINGKASAN KELAS");
            writer.newLine();
            writer.write("Rata-rata nilai kelas : " + String.format(Locale.US, "%.2f", rataRata));
            writer.newLine();
            writer.write("Jumlah siswa lulus    : " + jumlahLulus + " siswa");
            writer.newLine();
            writer.write("Jumlah siswa tidak lulus : " + jumlahTidakLulus + " siswa");
            writer.newLine();
            writer.write("Total siswa           : " + jumlahSiswa + " siswa");
            writer.newLine();

            // Pesan ke konsol: penulisan file sudah selesai.
            System.out.println("\nData berhasil ditulis ke file laporan_nilai.txt");

        // --- EXCEPTION HANDLING (penulisan file) -----------------------------
        // catch (IOException e) = kalau file gagal dibuat/ditulis (misalnya
        // folder tidak bisa ditulisi), program tidak crash: tampilkan pesan
        // yang ramah, lalu hentikan program dengan return.
        // e.getMessage() = pesan error dari Java (penyebab teknisnya).
        } catch (IOException e) {
            System.out.println("Terjadi kesalahan saat menulis file: " + e.getMessage());
            scanner.close(); // Tutup Scanner sebelum keluar.
            return;          // Berhenti; tidak lanjut ke Langkah 2.
        }

        // --- LANGKAH 2: BACA KEMBALI FILE (FileReader + BufferedReader) ------
        // Polanya sama: try-with-resources supaya reader otomatis tertutup.
        // FileReader = membuka file yang SUDAH ADA untuk dibaca.
        // BufferedReader = pembungkusnya, menyediakan readLine().
        System.out.println("\n===== ISI FILE laporan_nilai.txt =====");
        try (BufferedReader reader = new BufferedReader(new FileReader("laporan_nilai.txt"))) {

            // String baris = variabel penampung satu baris yang sedang dibaca.
            // readLine() = membaca satu baris penuh dari file.
            // Kalau sudah sampai baris terakhir, readLine() mengembalikan
            // null - itulah tanda berhenti (kondisi while menjadi false).
            // Jadi loop ini berhenti sendiri tepat di akhir file.
            String baris;
            while ((baris = reader.readLine()) != null) {
                // Tampilkan baris yang baru dibaca ke konsol.
                System.out.println(baris);
            }
            System.out.println("===== AKHIR FILE =====");

        } catch (IOException e) {
            // Jalan kalau file tidak ditemukan / tidak bisa dibaca.
            System.out.println("Terjadi kesalahan saat membaca file: " + e.getMessage());
        }

        // --- PENUTUP: TUTUP SCANNER -----------------------------------------
        // close() = menutup Scanner supaya tidak ada resource yang bocor.
        scanner.close();
        System.out.println("\nProgram selesai.");
    }

    // ========================================================================
    //  FUNGSI-FUNGSI BANTUAN (procedural / fungsional)
    //  static = bisa dipanggil langsung dari main() tanpa membuat objek.
    //  Dipisah jadi fungsi supaya main() tetap pendek dan logika ini bisa
    //  dipakai ulang / diuji sendiri - inilah gaya prosedural-fungsional.
    // ========================================================================

    // tentukanStatus: menerima nilai, mengembalikan status kelulusan.
    // double nilai  = input  (parameter). String = output (return value).
    // Operator ternary  kondisi ? nilaiJikaBenar : nilaiJikaSalah
    // adalah if-else versi satu baris.
    public static String tentukanStatus(double nilai) {
        return (nilai >= 70) ? "Lulus" : "Tidak Lulus";
    }

    // hitungRataRata: total dibagi jumlah siswa.
    // (double) jumlah = casting: ubah int jadi double supaya hasil
    // pembagiannya desimal (bukan pembagian bulat, misal 240/3 = 80.0).
    public static double hitungRataRata(double total, int jumlah) {
        if (jumlah == 0) {
            return 0; // Pengaman: hindari pembagian dengan nol.
        }
        return total / (double) jumlah;
    }
}
