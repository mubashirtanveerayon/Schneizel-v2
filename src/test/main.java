package test;

import board.ChessBoard;
import move.MoveManager;
import util.Constants;
import util.FenUtils;
import util.Util;

import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class main {

    public static void main(String[] args) {

        char[][] board = new char[][]{
                {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                {' ', ' ', 'p', ' ', ' ', 'k', ' ', ' '},
                {' ', ' ', ' ', 'K', ' ', ' ', ' ', ' '},
                {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '}};


        String fen = FenUtils.generate(board,"w - - 0 1");

        ChessBoard cb=new ChessBoard("r3kbnr/pb1npppp/1q6/2p5/P1pp4/2NP3N/RP1KPPPP/2BQ1B1R b kq - 3 9");

        MoveManager mm = new MoveManager(cb);

        //System.out.println(cb.gs);
        System.out.println(mm.moveGenerationTestWOLog(1,true));
        //System.out.println(cb.pinnedPieces.containsKey(11));
        System.out.println(cb.pinnedPieces);
        System.out.println(mm.moveGenerationTestWOLog(2,true));
        System.out.println(cb.pinnedPieces);
        //System.out.println(cb.squareUnderAttack(4,7));
        Util.printBoardStd(cb.board);
        Util.printBoard(cb.board);
        System.out.println(mm.moveGenerationTestWOLog(1,true));
        System.out.println(cb.pinnedPieces);
        //not a problem in squareUnderAttack()
        //see mm.king()->if inCheck
        //System.out.println(cb.board[6][3]);
        //System.out.println(moves);




    }

}
