package parser;

import move.Move;
import util.Constants;

public class MoveParser {

    public static Move parse(String str){
        String[] parts = str.split(Constants.MOVE_SEPARATOR);
        Move mv = new Move();
        mv.enPassant = str.contains(Constants.EN_PASSANT_NOTATION);
        mv.kingSideCastling = str.contains(Constants.KING_SIDE_CASTLING);
        mv.queenSideCastling = str.contains(Constants.QUEEN_SIDE_CASTLING);
        if(mv.kingSideCastling || mv.queenSideCastling){
            mv.castlingFen = parts[1];
            mv.enPassantFen = parts[2];
        }else{
            mv.castlingFen = parts[2];
            mv.enPassantFen = parts[3];
            mv.locFile = Integer.parseInt(String.valueOf(parts[0].charAt(0)));
            mv.locRank = Integer.parseInt(String.valueOf(parts[0].charAt(1)));
            mv.destFile = Integer.parseInt(String.valueOf(parts[0].charAt(2)));
            mv.destRank = Integer.parseInt(String.valueOf(parts[0].charAt(3)));
        }

        return mv;
    }



}
