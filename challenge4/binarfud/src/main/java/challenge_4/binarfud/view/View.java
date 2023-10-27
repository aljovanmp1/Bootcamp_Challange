package challenge_4.binarfud.view;

import challenge_4.binarfud.utlis.Constants;

public class View {
    public static String determineHeaderContent(String sentence) {
        String result = "";
        result += Constants.NEWLINE;
        result += Constants.LINEWITHBREAK;
        result += sentence + Constants.NEWLINE;
        result += Constants.LINEWITHBREAK + Constants.NEWLINE;
        return result;
    }

    public static void printError(String state){
        String sentence = "";
        String footer = "";
        switch(state) {
            case Constants.WRONGINPUT:
                sentence+= "Mohon masukkan input\n";
                sentence+= "pilihan anda";
                footer+= "(Y) untuk lanjut\n";
                footer+= "(n) untuk keluar";
                break;
            case Constants.EMPTYORDER:
                sentence+= "Minimal 1 jumlah\n";
                sentence+= "pesanan!";
                break;
            default:
                break;
        }

        String result = "";
        result += Constants.NEWLINE;
        result += Constants.LINEWITHBREAK;
        result += sentence + Constants.NEWLINE;
        result += Constants.LINEWITHBREAK;
        result += footer + Constants.NEWLINE;
        
        System.out.print(result);
    }

    

}
