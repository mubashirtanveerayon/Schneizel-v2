package board;

import move.Move;
import util.Constants;
import util.FenUtils;
import util.Util;

public class ChessBoard {

    public char[][] board;
//    public String fen;
    public int halfMove;
    public int fullMove;
    public char turn;
    public String[] fenParts;
    public int[] whiteKingPosition,blackKingPosition;

    public ChessBoard(){
        fenParts = FenUtils.split(Constants.STARTING_FEN);
        initialize();
    }

    public ChessBoard(String fen){
        if(!FenUtils.isFenValid(fen)){
            return;
        }
        fenParts = FenUtils.split(fen);
        initialize();
    }

    private void initialize(){
        board = FenUtils.parseFen(fenParts);
        halfMove = (Integer.parseInt(fenParts[11]));
        fullMove = (Integer.parseInt(fenParts[12]));
        turn = fenParts[1].charAt(0);

        whiteKingPosition = new int[2];
        blackKingPosition = new int[2];

        for(int i=0;i<8;i++){
            for(int j=0;j<8;j++){
                if(board[i][j] == Constants.WHITE_KING){
                    whiteKingPosition[0] = j;
                    whiteKingPosition[1] = i;
                }else if(board[i][j] == Constants.BLACK_KING){
                    blackKingPosition[0] = j;
                    blackKingPosition[1] = i;
                }
            }
        }

    }

    public static void main(String[] args) {
        ChessBoard cb = new ChessBoard("7N/1b3RN1/7k/6b1/KBp4p/5q2/6Q1/7n w - - 0 1");
//        FenUtils.printFile(cb.board[0]);
        Util.printBoard(cb.board,false);
        Move move = new Move(cb);

        move.makeMove("f7f6");
        Util.printBoard(cb.board,false);
        System.out.println(FenUtils.toString(cb.fenParts));
//        System.out.println(cb.fen);
//        System.out.println(FenUtils.isFenValid(cb.fen));
//        cb = new ChessBoard("7N/1b4N1/5R1k/6b1/KBp4p/5q2/6Q1/7n b - - 0 1");
//        cb.updateFen("e2d2");
//        Util.printBoard(cb.board,false);
//        cb = new ChessBoard(cb.fen);
//        Util.printBoard(cb.board,false);
//        System.out.println(cb.fen);

//        String tf = "6Q1";
//        char[] file = FenUtils.parseFile(tf);
//        String rf = FenUtils.getFileFen(file);
//        System.out.println(rf);

//        String tf = "7N/1b3RN1/7k/6b1/KBp4p/5q2/6Q1/7n w - - 0 1";
//        String move = "f7f6";
//        String rf = FenUtils.updateFen(tf,move);
//        System.out.println("7N/1b4N1/5R1k/6b1/KBp4p/5q2/6Q1/7n b - - 0 1");
//        System.out.println(rf);
//        String[][] board = new String[8][8];
//        int k=0;
//        for(int i=0;i<board.length;i++){
//            Arrays.fill(board[i],String.valueOf(k)+" ");
//            k++;
//
//            for(int j=0;j<board[i].length;j++){
//                board[i][j] = String.valueOf(i)+","+String.valueOf(j)+" ";
//            }
//        }
//        for(int i=0;i<board.length;i++){
//            for(int j=0;j<board[i].length;j++){
//                System.out.print(board[i][j]);
//            }
//            System.out.println();
//        }

        //7N/1b4N1/5R1k/6b1/KBp4p/5q2/6Q1/7n b - - 1 2
        //7N/1b4N1/5R1k/6b1/KBp4p/5q2/6Q1/7n b - - 0 1
    }

}
