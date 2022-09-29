package util;

import exception.InvalidFenException;

import java.util.Arrays;

public class FenUtils {


    public static String generate(char[][] board){
        String fen = "";
        for(int i=0;i<board.length-1;i++){
            fen+=FenUtils.getRank(board[i])+"/";
        }
        fen+=FenUtils.getRank(board[board.length-1])+" w - - 0 1";
        return fen;
    }

    public static char[][] parse(String[] fenParts){
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

    public static String getRank(char[] rank){
        String fen="";
        int f = -1;
        for(int i=0;i<rank.length;i++){
            if(rank[i] == Constants.EMPTY_SQUARE){
                if(f<0) {
                    f=0;
                }
                f++;
                if(i+1 == rank.length){
                    fen+=String.valueOf(f);
                }
            }else{
                if(f>0){
                    fen+=String.valueOf(f);
                    f=-1;
                }
                fen+=rank[i];
            }
        }
        return fen;
    }


    public static boolean isValid(String fen) throws InvalidFenException {
        String[] parts = fen.split("/");
        if(parts.length!=8){
            throw new InvalidFenException("Fen string is invalid! Rank missing! "+fen);
        }
        String[] fenUtils = parts[7].split(" ");
        if(fenUtils.length!=6){
            throw new InvalidFenException("Fen string is invalid! Some parts are missing at the end! "+fen);
        }
        try{
            Integer.parseInt(fenUtils[5]);
            Integer.parseInt(fenUtils[4]);
        }catch(Exception e){
            throw new InvalidFenException("Fen string is invalid! Could not parse halfmove or fullmove! "+fen);
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
