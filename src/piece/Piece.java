package piece;

import board.ChessBoard;
import util.Constants;

import java.util.ArrayList;

public class Piece {

    ChessBoard cb;

    public Piece(ChessBoard cb){
        this.cb = cb;
    }


    public ArrayList<String> generateMove(int file,int rank,boolean isPinned){
        switch(Character.toUpperCase(cb.board[rank][file])){
            case Constants.WHITE_KING:
                return king(file,rank,isPinned);
            case Constants.WHITE_PAWN:
                return pawn(file,rank,isPinned);
            case Constants.WHITE_ROOK:
                return rook(file,rank,isPinned);
            case Constants.WHITE_BISHOP:
                return bishop(file,rank,isPinned);
            case Constants.WHITE_KNIGHT:
                return knight(file,rank,isPinned);
            case Constants.WHITE_QUEEN:
                return queen(file,rank,isPinned);
            default:
                return null;
        }
    }

    public ArrayList<String> pawn(int file,int rank,boolean isPinned){
        return null;
    }

    public ArrayList<String> king(int file,int rank,boolean isPinned){
        return null;
    }

    public ArrayList<String> queen(int file,int rank,boolean isPinned){
        return null;
    }

    public ArrayList<String> rook(int file,int rank,boolean isPinned){
        return null;
    }

    public ArrayList<String> knight(int file,int rank,boolean isPinned){
        return null;
    }

    public ArrayList<String> bishop(int file,int rank,boolean isPinned){
        return null;
    }



}
