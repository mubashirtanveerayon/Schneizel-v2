package move;

import board.ChessBoard;
import util.Constants;
import util.FenUtils;

public class Move {

    public int locFile,locRank,destFile,destRank;
    public char captured;

    public String castlingFen,enPassantFen;

    public boolean kingSideCastling,queenSideCastling,enPassant;

}

