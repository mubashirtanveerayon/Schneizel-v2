package server.move;

import server.util.Constants;
import server.util.Util;

public final class Move {

    public int locFile, locRank, destFile, destRank,halfMoveClock;
    public char capturedPiece ;
    public char promotionPiece = Constants.EMPTY_SQUARE;

    public boolean isKingSideCastling = false, isQueenSideCastling = false, isEnPassant = false;

    String castlingFEN;

    String enPassantSquare;


    public Move(int lf,int lr,int df,int dr,char[][] board,String castle,String enPassant,int halfMove){
        locFile = lf;
        locRank = lr;
        destFile = df;
        destRank = dr;
        capturedPiece = board[dr][df];
        castlingFEN = castle;
        enPassantSquare = enPassant;
        halfMoveClock = halfMove;
    }

    @Override
    public String toString(){
        String algebra = Util.cvtMove(locFile,locRank,destFile,destRank);
        if(promotionPiece != Constants.EMPTY_SQUARE){
            algebra += promotionPiece;
        }
        return algebra;
    }

    public boolean equals(Object o){
        if(o == this)return true;
        Move m = (Move)o;
        return promotionPiece == m.promotionPiece && locFile == m.locFile && locRank == m.locRank && destFile == m.destFile && destRank == m.destRank && capturedPiece == m.capturedPiece && castlingFEN.equals(m.castlingFEN) && enPassantSquare.equals(m.enPassantSquare) && halfMoveClock == m.halfMoveClock && isKingSideCastling == m.isKingSideCastling && isQueenSideCastling == m.isQueenSideCastling && isEnPassant == m.isEnPassant;
    }



}
