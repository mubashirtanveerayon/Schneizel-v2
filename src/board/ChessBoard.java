package board;

import move.Move;
import util.Constants;
import util.FenUtils;
import util.GameState;
import util.Util;

import java.util.HashMap;

public class ChessBoard {

    public char[][] board;
    public int halfMove;
    public int fullMove;
    public char turn;
    public String[] fenParts;
    public int[] whiteKingPosition,blackKingPosition;
    public HashMap<Integer,Integer> pinnedPieces; // the array is for storing the direction the piece is being pinned from
    public int checkPiece = -1; // 1D index of the opponent piece(the piece attacking the king)
    public GameState gs;

    public ChessBoard(){
        fenParts = FenUtils.split(Constants.STARTING_FEN);
        gs =  GameState.NORMAL;
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
        pinnedPieces = new HashMap<>();
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

    public void resetStats(){
        pinnedPieces.clear();
        checkPiece = -1;
        gs = GameState.NORMAL;
    }

    public void checkAndPinnedPieces(){
        resetStats();
        int[] kingPosition = turn == Constants.WHITE?whiteKingPosition:blackKingPosition;
        int file,rank;
        boolean foundAlly,foundOpponentPiece;
        for(int i = 0; i<Constants.HORIZONTAL_AND_DIAGONAL_DIRECTIONS.length; i++){
            file = kingPosition[0]+Constants.HORIZONTAL_AND_DIAGONAL_DIRECTIONS[i][0];
            rank = kingPosition[1]+Constants.HORIZONTAL_AND_DIAGONAL_DIRECTIONS[i][1];
            foundOpponentPiece = false;
            foundAlly = false;
            while(Util.isValid(file,rank)){
                if(board[rank][file] == Constants.EMPTY_SQUARE){
                    file += Constants.HORIZONTAL_AND_DIAGONAL_DIRECTIONS[i][0];
                    rank += Constants.HORIZONTAL_AND_DIAGONAL_DIRECTIONS[i][1];
                    continue;
                }
                if((Util.isUpperCase(board[rank][file]) && turn == Constants.WHITE) || (!Util.isUpperCase(board[rank][file]) && turn == Constants.BLACK)){
                    if(foundAlly){
                        pinnedPieces.remove(pinnedPieces.size()-1);
                        break;
                    }else {
                        foundAlly = true;
                        pinnedPieces.put(file + rank * 8, i);
                    }
                }else if(Character.toUpperCase(board[rank][file]) != Constants.WHITE_PAWN){
                    foundOpponentPiece = true;
                    if (!foundAlly) {
                        gs = GameState.CHECK;
                        checkPiece = file + rank * 8;
                    }
                    break;
                }else{
                    // found opponent pawn
                    if(Math.abs(kingPosition[0] - file) == 1){
                        if(turn == Constants.WHITE){
                            if(kingPosition[1] > rank){
                                gs = GameState.CHECK;
                            }
                        }else{
                            if(kingPosition[1] < rank){
                                gs = GameState.CHECK;
                                checkPiece = file + rank * 8;
                            }
                        }
                    }
                    break;
                }
                file += Constants.HORIZONTAL_AND_DIAGONAL_DIRECTIONS[i][0];
                rank += Constants.HORIZONTAL_AND_DIAGONAL_DIRECTIONS[i][1];
            }
            if(!foundOpponentPiece){
                pinnedPieces.remove(pinnedPieces.size()-1);
            }
        }

        // TODO check -> knight Done
        if(gs == GameState.CHECK){
            return;
        }
        for(int[] direction:Constants.KNIGHT_DIRECTION){
            file = kingPosition[0]+direction[0];
            rank = kingPosition[1]+direction[1];
            if(Util.isValid(file,rank)){
                switch(turn){
                    case Constants.WHITE:
                        if(board[rank][file] == Constants.BLACK_KNIGHT){
                            gs = GameState.CHECK;
                            checkPiece = file + rank * 8;
                        }
                    case Constants.BLACK:
                        if(board[rank][file] == Constants.WHITE_KNIGHT){
                            gs = GameState.CHECK;
                            checkPiece = file + rank * 8;
                        }
                }

                if(gs == GameState.CHECK){
                    break;
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
