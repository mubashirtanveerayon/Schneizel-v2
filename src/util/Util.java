package util;

import java.util.Arrays;

public class Util {

    public static int[] getDirection(final int fromF,final int fromR,final int toF,final int toR){
        return new int[]{sign(toF,fromF),sign(toR,fromR)};
    }

    public static int sign(final int val1,final int val2){
        int diff = val1-val2;
        return diff == 0?0:diff/Math.abs(diff);
    }

    public static int getNumericValue(char c) {
        return Integer.parseInt(Character.toString(c));
    }

    public static boolean isUpperCase(char t) {
        return t == toUpper(t);
    }

    public static char toUpper(char c) {
        return Character.toUpperCase(c);
    }

    public static boolean isAlly(char piece1,char piece2){
        return (isUpperCase(piece1) && isUpperCase(piece2))||(!isUpperCase(piece1) && !isUpperCase(piece2));
    }

    public static boolean isValid(int file,int rank){
        return file >= 0 && file < 8 && rank >= 0 && rank < 8;
    }

    public static String cvtMove(int lf,int lr,int df,int dr){
        return Character.toString(Constants.FILES.charAt(lf))+Character.toString(Constants.RANKS.charAt(8-lr))+Character.toString(Constants.FILES.charAt(df))+Character.toString(Constants.RANKS.charAt(8-dr));
    }

    public static char[][] getEmptyBoard(){
        char[][] board = new char[8][8];
        for(int i=0;i<8;i++){
            Arrays.fill(board[i], Constants.EMPTY_SQUARE);
        }
        return board;
    }

    public static void printBoard(char[][] boardChar,boolean flipped) {
        if(flipped){
            String board = "\n    h   g   f   e   d   c   b   a\n  +---+---+---+---+---+---+---+---+\n" ;
            for(int i=Constants.COLUMNS-1;i>=0;i--){
                board += String.valueOf(Constants.ROWS - i) + " | ";
                for(int j = Constants.ROWS-1; j >= 0; j--){
                    board += (String.valueOf(boardChar[i][j]) + " | ");
                }
                board += String.valueOf(Constants.ROWS - i) + "\n  +---+---+---+---+---+---+---+---+\n";
            }
            board+="    h   g   f   e   d   c   b   a\n";
            System.out.println(board);
        }else{
            String board = "\n    a   b   c   d   e   f   g   h\n  +---+---+---+---+---+---+---+---+\n";
            for (int i = 0; i < Constants.COLUMNS; i++) {
                board += String.valueOf(Constants.ROWS - i) + " | ";
                for (int j = 0; j < Constants.ROWS; j++) {
                    board += (String.valueOf(boardChar[i][j]) + " | ");
                }         //r | n | b | q | k | b | n | r |
                board += String.valueOf(Constants.ROWS - i) + "\n  +---+---+---+---+---+---+---+---+\n";
            }
            board += "    a   b   c   d   e   f   g   h\n";
            System.out.println(board);
        }
    }
}
