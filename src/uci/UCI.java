package uci;

import board.ChessBoard;
import move.MoveManager;
import util.FenUtils;
import util.Util;

import java.util.Scanner;

public class UCI {

    public static void main(String[] args) {

        char[][] board = new char[][]{
                {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                {' ', ' ', ' ', ' ', ' ', 'R', 'k', ' '},
                {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                {' ', ' ', ' ', ' ', 'K', ' ', ' ', ' '}};


        String fen = FenUtils.generate(board,"b - - 0 1");


        Scanner sc = new Scanner(System.in);
        String input;
        ChessBoard cb=new ChessBoard();
        MoveManager mm=new MoveManager(cb);
        boolean exit=false,flip=false;
        while(!exit&&(input=sc.nextLine())!=null){
            String[] partsBySpace = input.split(" ");
            switch(partsBySpace[0].toLowerCase()){
                case "go":
                    if(input.contains("perft")){
                        int depth = Integer.parseInt(partsBySpace[2]);
                        mm.moveGenerationTest(depth,true);
                    }
                    break;
                case "position":
                    switch(partsBySpace[1].toLowerCase()){
                        case "fen":
                            cb = new ChessBoard(input.split("fen ")[1]);
                            mm = new MoveManager(cb);
                            break;
                        case "startpos":
                            //make a move
                            mm.makeMove(mm.parse(partsBySpace[3]));
                            break;
                    }
                    break;
                case "d":
                    Util.printBoardStd(cb.board,flip);
                    System.out.println("Fen: "+ FenUtils.cat(cb.fenParts));
                    break;
                case "exit":
                    exit = true;
                    break;
                case "fen":
                    System.out.println("Fen: "+ FenUtils.cat(cb.fenParts));
                    break;
            }
        }
    }


}
