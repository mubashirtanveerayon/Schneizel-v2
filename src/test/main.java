package test;

import board.ChessBoard;
import move.MoveManager;
import util.FenUtils;

import java.util.Scanner;

public class main {

    public static void main(String[] args) {

        char[][] board = new char[][]{
                {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                {' ', ' ', ' ', ' ', ' ', 'k', ' ', ' '},
                {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                {' ', ' ', ' ', ' ', 'K', 'R', ' ', ' '}};


        String fen = FenUtils.generate(board,"b - - 0 1");

        ChessBoard cb=new ChessBoard(fen);
        MoveManager mm=new MoveManager(cb);

        System.out.println(cb.gs);
        for(Integer checkerIndex:cb.checkers.keySet()) {
            System.out.println(checkerIndex%8);
            System.out.println(checkerIndex/8);
        }
    }

}
