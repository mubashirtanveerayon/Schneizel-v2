package util;

import java.util.Arrays;

public class FenUtils {

    public static char[][] parseFen(String[] fenParts){
        int file = 0;
        int rank = 0;
        char[][] board = Util.getEmptyBoard();
        for(int i=0;i<8;i++){
            for(char c:fenParts[i].toCharArray()){
                if(Character.isDigit(c)){
                    file += Util.getNumericValue(c);
                }else{
                    board[rank][file] = c;
                    file++;
                }
            }
            rank++;
            file = 0;
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
            System.out.println("Fen string is invalid! Rank missing! "+fen);
            return false;
        }
        String[] fenUtils = parts[7].split(" ");
        if(fenUtils.length!=6){
            System.out.println("Fen string is invalid! Some parts are missing at the end! "+fen);
            return false;
        }
        try{
            Integer.parseInt(fenUtils[5]);
            Integer.parseInt(fenUtils[4]);
        }catch(Exception e){
            System.out.println("Fen string is invalid! Could not parse halfmove or fullmove! "+fen);
            return false;
        }
        return true;
    }

    public static String[] split(String fen){
        String[] split = new String[13];
        String[] fenPartsBySlash = fen.split("/");
        String[] fenPartsBySpace = fen.split(" ");
        for(int i=0;i<fenPartsBySlash.length;i++){
            if(i+1 == fenPartsBySlash.length){
                split[i] = fenPartsBySlash[i].split(" ")[0];
            }else {
                split[i] = fenPartsBySlash[i];
            }
        }
        for(int i=1;i<fenPartsBySpace.length;i++){
            split[7+i] = fenPartsBySpace[i];
        }
        return split;
    }

    public static String toString(String[] fenParts){
        String fen = "";
        for(int i=0;i<fenParts.length;i++){
            if(i < 7){
                fen += fenParts[i] + "/";
            }else if(i==7){
                fen += fenParts[i];
            }else{
                fen += " "+fenParts[i];
            }
        }
        return  fen;
    }



}
