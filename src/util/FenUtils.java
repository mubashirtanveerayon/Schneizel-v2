package util;

import java.util.Arrays;

public class FenUtils {

    public static char[][] parseFen(String fen){
        int file = 0;
        int rank = 0;
        char[][] board = Util.getEmptyBoard();
        for(char c:fen.split(" ")[0].toCharArray()){
            if(c == '/'){
                rank++;
                file = 0;
            }else if(Character.isDigit(c)){
                file += Util.getNumericValue(c);
            }else{
                board[rank][file] = c;
                file++;
            }
        }
        return board;
    }


    public static void printFile(char[] file){
        for(char c:file){
            System.out.print(c);
        }
        System.out.println();
    }

    public static char[] parseFile(String fileFen){
        char[] file = new char[8];
        Arrays.fill(file, Constants.EMPTY_SQUARE);
        int f = 0;
        for(char c : fileFen.toCharArray()){
            if(Character.isDigit(c)) {
                f += Util.getNumericValue(c);
            }else{
                file[f] = c;
                f++;
            }
        }
        return file;
    }

    public static String getFileFen(char[] file){
        String fen="";
        int f = -1;
        for(int i=0;i<file.length;i++){
            if(file[i] == Constants.EMPTY_SQUARE){
                if(f<0) {
                    f=0;
                }
                f++;
                if(i+1 == file.length){
                    fen+=String.valueOf(f);
                }
            }else{
                if(f>0){
                    fen+=String.valueOf(f);
                    f=-1;
                }
                fen+=file[i];
            }
        }
        return fen;
    }


    public static boolean isFenValid(String fen){
        String[] parts = fen.split("/");
        if(parts.length!=8){
            return false;
        }
        String[] fenUtils = parts[7].split(" ");
        if(fenUtils.length!=6){
            return false;
        }
        try{
            Integer.parseInt(fenUtils[5]);
            Integer.parseInt(fenUtils[4]);
        }catch(Exception e){
            return false;
        }
        return true;
    }

}
