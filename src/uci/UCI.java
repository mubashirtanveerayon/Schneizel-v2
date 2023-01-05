package uci;

import board.ChessBoard;
import move.MoveManager;
import util.Constants;
import util.FenUtils;
import util.Util;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class UCI {

    public static void main(String[] args) {

        char[][] board = new char[][]{
                {' ', 'k', ' ', ' ', ' ', ' ', ' ', ' '},
                {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                {' ', ' ', ' ', ' ', ' ', ' ', ' ', 'b'},
                {' ', ' ', ' ', ' ', ' ', ' ', 'P', ' '},
                {' ', ' ', ' ', ' ', ' ', 'Q', ' ', ' '},
                {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                {' ', ' ', ' ', 'K', ' ', ' ', ' ', ' '}};


        String fen = FenUtils.generate(board,"w - - 0 1");


        Scanner sc = new Scanner(System.in);
        String input;
        ChessBoard cb=new ChessBoard();
        MoveManager mm=new MoveManager(cb);
        //Util.writeToLog(mm.getAllMoves());
        boolean exit=false,flip=false;
        while(!exit&&(input=sc.nextLine())!=null){
            String[] partsBySpace = input.split(" ");
            switch(partsBySpace[0].toLowerCase()){
                case "uci":
                    Util.writeToLog("id name Schneizel 2");
                    Util.writeToLog("id author see AUTHORS file");
                    Util.writeToLog("uciok");
                    break;
                case "go":
                    if(input.contains("perft")){
                        int depth = Integer.parseInt(partsBySpace[2]);
                        long currentTime = System.nanoTime();
                        mm.moveGenerationTest(depth,true);
                        Util.writeToLog("Time taken: "+ TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - currentTime)+" ms");
                    }
                    break;
                case "position":
                    switch(partsBySpace[1].toLowerCase()){
                        case "fen":
                            cb = new ChessBoard(input.split("fen ")[1]);
                            mm = new MoveManager(cb);
                            break;
                        case "startpos":
                            cb = new ChessBoard();
                            mm = new MoveManager(cb);
                            if(partsBySpace.length<3){
                                break;
                            }
                        case "thispos":
                            switch(partsBySpace[2].toLowerCase()){
                                case "move":
                                    if(Character.isDigit(partsBySpace[3].charAt(0))||partsBySpace[3].contains(Constants.KING_SIDE_CASTLING)||partsBySpace[3].contains(Constants.QUEEN_SIDE_CASTLING)){
                                        String[] moves = input.split("move ")[1].split(",");
                                        for(String move:moves){
                                            mm.makeMove(move);
                                        }
                                    }else{
                                        for(int i=3;i<partsBySpace.length;i++){
                                            mm.makeMove(mm.parse(partsBySpace[i]));
                                        }
                                    }
                                    break;
                                case "undomove":
                                    if(Character.isDigit(partsBySpace[3].charAt(0))||partsBySpace[3].contains(Constants.KING_SIDE_CASTLING)||partsBySpace[3].contains(Constants.QUEEN_SIDE_CASTLING)){
                                        String[] moves = input.split("undomove ")[1].split(",");
                                        for(String move:moves){
                                            mm.undoMove(move);
                                        }
                                    }
                                    break;
                            }
                            break;
                    }
                    break;
                case "moves":
                    ArrayList<String> moves = mm.getAllMoves();
                    Util.writeToLog(moves.toString());
                    String std="";
                    for(String move:moves){
                        std+=mm.cvt(move)+" ";
                    }
                    Util.writeToLog(std.trim());
                    break;
                case "d":
                    Util.printBoardStd(cb.board,flip);
                    Util.printBoard(cb.board);
                    Util.writeToLog("Fen: "+ FenUtils.cat(cb.fenParts));
                    break;
                case "quit":
                    exit = true;
                    break;
                case "fen":
                    Util.writeToLog("Fen: "+ FenUtils.cat(cb.fenParts));
                    break;
                case "flip":
                    flip = !flip;
                    break;
            }
        }
    }


}
// position startpos move c2c3 c7c5 c3c4 b7b5 a2a3 b5c4 d2d3 d8b6 b1c3 c8b7
// ,g1f3,g1h3,c1d2,c1e3,c1f4,c1g5,c1h6,d1c2,d1d2,d1a4,e1d2
//position startpos move e2e3 d7d6 g1f3 e7e5 f3h4 c8g4 f1b5->go perft 1: output doesnt match