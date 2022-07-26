package util;

import java.util.Arrays;

public class Util {

    public static int getNumericValue(char c) {
        return Integer.parseInt(Character.toString(c));
    }

    public static boolean isUpperCase(char t) {
        return t == toUpper(t);
    }

    public static char toUpper(char c) {
        return Character.toUpperCase(c);
    }

    public static boolean isValid(int file,int rank){
        return file >= 0 && file < 8 && rank >= 0 && rank < 8;
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
