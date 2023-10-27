package challenge_4.binarfud.view;

import java.util.List;

import challenge_4.binarfud.model.Merchant;

public class MerchantView {
    public static void merchantOptions() {
        System.out.println("\nSilakan pilih menu:");
        System.out.println("1. Tambah merchant");
        System.out.println("2. Edit status merchant (tutup/buka)");
        System.out.println("3. Lihat merchant yang buka");
        System.out.println("4. Kelola product");
        System.out.println("\n99. Kembali ke menu utama");
        System.out.println("0. Keluar aplikasi");
    }

    public static void editProductOptions() {
        System.out.println("\nOpsi pengaturan:");
        System.out.println("1. Nama produk");
        System.out.println("2. Harga produk");
        System.out.println("\n99. Kembali ke menu utama");
        System.out.println("0. Keluar aplikasi");
    }

    public static void addOrEditProductOptions() {
        System.out.println("\nSilahkan pilih:");
        System.out.println("1. Tambah produk");
        System.out.println("2. Edit produk");
        System.out.println("3. Hapus produk");
        System.out.println("\n99. Kembali ke menu utama");
        System.out.println("0. Keluar aplikasi");
    }

    public static void showMerchantAvailability(List<Merchant> merchants){
        int ind = 1;
        System.out.printf("\n%s %-20s %s\n", "no." , "merchant", "status");
        for(Merchant merchant : merchants){
            String openStatus = Boolean.TRUE.equals(merchant.getOpen()) ? "open" : "close";
            System.out.printf("%-3d %-20s %s\n", ind++, merchant.getMerchantName(), openStatus);
        }
    }

    public static void showMerchants(List<Merchant> merchants){
        int ind = 1;
        System.out.printf("\n%s %-20s %-30s %s\n", "no." , "merchant", "lokasi", "status");
        for(Merchant merchant : merchants){
            String openStatus = Boolean.TRUE.equals(merchant.getOpen()) ? "open" : "close";
            System.out.printf("%-3d %-20s %-30s %s\n", ind++, merchant.getMerchantName(), merchant.getMerchantLocation(), openStatus);
        }
    }

    public static void showMerchantsWithPagination(List<Merchant> merchants, int dataPerPage, int page){
        int ind = ((page - 1) * dataPerPage) + 1;
        System.out.printf("\n%s %-20s %-30s %s\n", "no." , "merchant", "lokasi", "status");
        for(Merchant merchant : merchants){
            String openStatus = Boolean.TRUE.equals(merchant.getOpen()) ? "open" : "close";
            System.out.printf("%-3d %-20s %-30s %s\n", ind++, merchant.getMerchantName(), merchant.getMerchantLocation(), openStatus);
        }
    }

    public static void showMerchantPage(int currentPage, int totalPages){
        System.out.println("\nhalaman: " + currentPage + " total halaman: " + totalPages);
        System.out.println("Masukkan halaman atau 0 untuk kembali");
    }

    public static void showAndPickMerchantPagination(int currentPage, int totalPages){
        System.out.println("\nhalaman: " + currentPage + " total halaman: " + totalPages);
        System.out.println("Masukkan halaman atau 0 untuk memlih merchant");
    }

}
