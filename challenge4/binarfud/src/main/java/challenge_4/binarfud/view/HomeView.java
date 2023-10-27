package challenge_4.binarfud.view;

public class HomeView extends View{
    private HomeView(){}

    public static void welcomeMessage() {
        System.out.print(determineHeaderContent("Selamat datang di binarfud"));
    }

    public static void mainMenuOption() {
        System.out.println("Silakan pilih menu:");
        System.out.println("1. Kelola Merchant");
        System.out.println("2. Kelola User");
        System.out.println("3. Pesan makan");
        System.out.println("0. Keluar aplikasi");
    }

}
