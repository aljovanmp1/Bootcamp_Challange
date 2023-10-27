package challenge_4.binarfud.view;

import java.util.List;

import challenge_4.binarfud.model.User;

public class UserView {
    public static void userOptions() {
        System.out.println("\nSilakan pilih menu:");
        System.out.println("1. Menambahkan user");
        System.out.println("2. Edit user");
        System.out.println("3. Hapus user\n");
        System.out.println("99. Kembali ke menu utama");
        System.out.println("0. Keluar aplikasi");
    }

    public static void showUsers(List<User> users) {
        int ind = 1;
        System.out.printf("\n%s %-20s %-20s\n", "no.", "username", "email");
        for (User user : users) {
            System.out.printf("%-3d %-20s %-20s\n", ind++, user.getUserName(), user.getEmailAddress());
        }
    }

    public static void editOptions() {
        System.out.println("\nSilakan pilih:");
        System.out.println("1. Edit email");
        System.out.println("2. Edit password\n");
        System.out.println("99. Kembali ke menu utama");
        System.out.println("0. Keluar aplikasi");
    }
}
