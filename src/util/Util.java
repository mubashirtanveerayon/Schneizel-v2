package util;

import exception.InvalidCoordinateException;

import java.util.Arrays;

public class Util {

    public static int[] getDirection(final int fromF,final int fromR,final int toF,final int toR){
        return new int[]{getSign(toF,fromF), getSign(toR,fromR)};
    }

    public static boolean inBetween(int from,int to,int i){
        return i>Math.min(from,to) && i<Math.max(from,to);
    }

    public static int getSign(final int val1, final int val2){
        int diff = val1-val2;
        return diff == 0?0:diff/Math.abs(diff);
    }

    public static boolean equalSign(int val1,int val2){
        return (val1>0 && val2>0) || (val1==0 && val2==0) || (val1<0 && val2<0);
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
        return piece1 != Constants.EMPTY_SQUARE && piece2 != Constants.EMPTY_SQUARE && (isUpperCase(piece1) && isUpperCase(piece2))||(!isUpperCase(piece1) && !isUpperCase(piece2));
    }

    public static boolean isEnemyPiece(char turn,char piece){
        return (turn==Constants.WHITE&&!Character.isUpperCase(piece))||(turn==Constants.BLACK&&Character.isUpperCase(piece));
    }

    public static boolean isValid(int file,int rank){
        return file >= 0 && file < 8 && rank >= 0 && rank < 8;
    }

    public static String parseMove(String stdMove){

        return null;
    }

    public static String cvtMove(int lf,int lr,int df,int dr) throws InvalidCoordinateException {
        if(!isValid(lf,lr)) throw new InvalidCoordinateException(lf,lr);
        if(!isValid(df,dr)) throw new InvalidCoordinateException(df,dr);
        return Character.toString(Constants.FILES.charAt(lf))+Integer.toString(8-lr)+Character.toString(Constants.FILES.charAt(df))+Integer.toString(8-dr);
    }

    //constructs a normal move, special moves are castling, en-passant, promotions
    public static String cvtMove(int lf,int lr,int df,int dr,char[][] board,String[] fenParts) throws InvalidCoordinateException {
        if(!isValid(lf,lr)) throw new InvalidCoordinateException(lf,lr);
        if(!isValid(df,dr)) throw new InvalidCoordinateException(df,dr);
        return String.valueOf(lf)+String.valueOf(lr)+String.valueOf(df)+String.valueOf(dr)+Constants.MOVE_SEPARATOR+board[dr][df]+Constants.MOVE_SEPARATOR+fenParts[9]+Constants.MOVE_SEPARATOR+fenParts[10];
    }

    public static char[][] getEmptyBoard(){
        char[][] board = new char[8][8];
        for(int i=0;i<8;i++){
            Arrays.fill(board[i], Constants.EMPTY_SQUARE);
        }
        return board;
    }



    public static void printBoard(char[][] board){
        printBoard(board,false);
    }

    public static void printBoard(char[][] boardChar,boolean flipped) {
        if(flipped){
            String board = "\n    7   6   5   4   3   2   1   0\n  +---+---+---+---+---+---+---+---+\n" ;
            for(int i=Constants.COLUMNS-1;i>=0;i--){
                board += String.valueOf( i) + " | ";
                for(int j = Constants.ROWS-1; j >= 0; j--){
                    board += (String.valueOf(boardChar[i][j]) + " | ");
                }
                board += String.valueOf( i) + "\n  +---+---+---+---+---+---+---+---+\n";
            }
            board+="    7   6   5   4   3   2   1   0\n";
            System.out.println(board);
        }else{
            String board = "\n    0   1   2   3   4   5   6   7\n  +---+---+---+---+---+---+---+---+\n";
            for (int i = 0; i < Constants.COLUMNS; i++) {
                board += String.valueOf(i) + " | ";
                for (int j = 0; j < Constants.ROWS; j++) {
                    board += (String.valueOf(boardChar[i][j]) + " | ");
                }         //r | n | b | q | k | b | n | r |
                board += String.valueOf(i) + "\n  +---+---+---+---+---+---+---+---+\n";
            }
            board += "    0   1   2   3   4   5   6   7\n";
            System.out.println(board);
        }
    }

    public static void printBoardClassic(char[][] board){
        printBoardClassic(board,false);
    }

    public static void printBoardClassic(char[][] boardChar,boolean flipped) {
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
