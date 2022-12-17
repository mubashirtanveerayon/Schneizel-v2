package test;

import board.ChessBoard;
import move.MoveManager;
import util.FenUtils;
import util.Util;

import java.util.ArrayList;

public class main {

    public static void main(String[] args) {
        char[][] board = new char[][]{
                {' ', ' ', ' ', ' ', ' ', ' ', 'k', ' '},
                {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                {' ', ' ', ' ', ' ', ' ', ' ', 'p', ' '},
                {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                {' ', ' ', ' ', ' ', 'K', ' ', 'R', ' '}};


        String fen = FenUtils.generate(board,"b - - 0 1");


        System.out.println(fen);
        //ChessBoard cb = new ChessBoard("1k3q2/3P2p1/p2RPB2/8/8/p4BK1/1prPN3/7r w - - 0 1");
        ChessBoard cb = new ChessBoard(fen);

        Util.printBoard(cb.board);


//        System.out.println(cb.blackKingPosition[0]);
//        System.out.println(cb.blackKingPosition[1]);


//        System.out.println(cb.pinnedPieces.size());
//
//        for(int index:cb.pinnedPieces.keySet()) {
//            System.out.println(index % 8+", "+index/8);
//            System.out.println(cb.pinnedPieces.get(index));
//        }

        MoveManager mg = new MoveManager(cb);

        ArrayList<String> moves = mg.getAllMoves();

        System.out.println(moves.size());
        System.out.println(moves);
        mg.moveGenerationTest(1,true);
        //h1f1,h1h2,h1h3,h1h4,h1h5,h1h6,h1h7,h1h8
    }

}
