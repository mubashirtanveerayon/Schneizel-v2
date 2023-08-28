package server.uci;

import schneizel.Engine;
import server.board.ChessBoard;
import server.move.MoveManager;
import server.util.Constants;
import server.util.FenUtils;
import server.util.GameState;
import server.util.Util;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class UCI {

    private boolean running = false,initialized = false;


    Scanner sc;
//    ChessBoard cb;
//    MoveManager mm;
    Engine engine;

    String input="";

    public void toggle(){
        if (!running){
            sc = new Scanner(System.in);
            engine = new Engine();
            running = true;
        }
    }

    private Thread inputThread(){
        return new Thread(){

            @Override
            public void run(){
                while(running) {
                    input = sc.nextLine();
                }
            }

        };
    }

    public void run() {

        if(initialized || !running){
            return;
        }else {
            initialized = true;
            Thread t=inputThread();
            t.start();
        }

        boolean saveToLog = false;
        boolean flip=false;
        String output="";
        while(running){
            System.out.print("");
            if (input.isEmpty()){
                continue;
            }
            String[] partsBySpace = input.split(" ");
            switch(partsBySpace[0].toLowerCase()){
                case "uci":
                    output = "id name Schneizel 2\nid author see AUTHORS file\nuciok";
                    if(saveToLog) {
                        Util.writeToLog(output);
                    }else{
                        System.out.println(output);
                    }
                    break;
                case "go":
                    if (partsBySpace.length == 1){
                        long currentTime = System.nanoTime();
                        output = "bestmove "+engine.mm.cvt(engine.search());
                        output+="\nTime taken: " + TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - currentTime) + " ms";
                        print(output,saveToLog);
                    }
                    else if(partsBySpace[1].equals("perft")){
                        int depth = Integer.parseInt(partsBySpace[2]);
                        long currentTime = System.nanoTime();
                        output = engine. mm.moveGenerationTest(depth, true);
                        output+="\nTime taken: " + TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - currentTime) + " ms";
                        print(output,saveToLog);
                    }else if(partsBySpace[1].equals("depth")){
                        int depth = Integer.parseInt(partsBySpace[2]);
                        long currentTime = System.nanoTime();
                        int prevdepth = engine.depth;
                        engine.setDepth(depth);
                        output = "bestmove "+engine.mm.cvt(engine.search());
                        output+="\nTime taken: " + TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - currentTime) + " ms";
                        print(output,saveToLog);
                        engine.setDepth(prevdepth);
                    }
                    break;
                case "position":
                    switch(partsBySpace[1].toLowerCase()){
                        case "fen":
                            engine = new Engine(input.split("fen ")[1]);
                            break;
                        case "startpos":
                            engine = new Engine();
                            if(partsBySpace.length<3){
                                break;
                            }
                        case "thispos":
                            switch(partsBySpace[2].toLowerCase()){
                                case "move":
                                    if(Character.isDigit(partsBySpace[3].charAt(0))||partsBySpace[3].contains(Constants.KING_SIDE_CASTLING)||partsBySpace[3].contains(Constants.QUEEN_SIDE_CASTLING)){
                                        String[] moves = input.split("move ")[1].split(",");
                                        for(String move:moves){
                                            engine.mm.makeMove(move);
                                        }
                                    }else{
                                        for(int i=3;i<partsBySpace.length;i++){
                                            engine.mm.makeMove(engine.mm.parse(partsBySpace[i]));
                                        }
                                    }
                                    break;
                                case "undomove":
                                    if(Character.isDigit(partsBySpace[3].charAt(0))||partsBySpace[3].contains(Constants.KING_SIDE_CASTLING)||partsBySpace[3].contains(Constants.QUEEN_SIDE_CASTLING)){
                                        String[] moves = input.split("undomove ")[1].split(",");
                                        for(String move:moves){
                                            engine.mm.undoMove(move);
                                        }
                                    }
                                    break;
                            }
                            print("Fen " + FenUtils.cat(engine.cb.fenParts),saveToLog);
                            break;
                    }

                    break;
                case "moves":
                    ArrayList<String> moves = engine.mm.getAllMoves();
                    //Util.writeToLog(moves.toString());
                    String std="";
                    for(String move:moves){
                        std+=engine.mm.cvt(move)+" ";
                    }
                    print(std.trim(),saveToLog);
                    break;
                case "d":
                    output = Util.getBoardVisualStd(engine.cb.board,flip);
                    output+="\n"+Util.getBoardVisual(engine.cb.board);
                    output+="\n"+("Fen: "+ FenUtils.cat(engine.cb.fenParts));
                    print(output,saveToLog);
                    break;
                case "quit":
                    running = false;
                    break;
                case "fen":
                    print("Fen: "+ FenUtils.cat(engine.cb.fenParts),saveToLog);
                    break;
                case "flip":
                    flip = !flip;
                    break;
                case "save":
                    saveToLog = !saveToLog;
                    System.out.println(saveToLog);
                    break;
                case "push":
                    if (partsBySpace.length == 1){
                        long currTime = System.nanoTime();
                        String move = engine.search();
                        long timeTaken = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - currTime);
                        engine.mm.makeMove(move);
                        output = "played "+move+"\n";
                        output += "Time taken: "+timeTaken + " ms\n";
                        output += "Fen " + FenUtils.cat(engine.cb.fenParts);
                        print(output,saveToLog);
                        break;
                    }
                    try{
                        engine.mm.makeMove(engine.mm.getAllMoves().get(Integer.parseInt(partsBySpace[1])-1));
                        print("Fen " + FenUtils.cat(engine.cb.fenParts), saveToLog);
                    }catch(Exception e) {
                        engine.mm.makeMove(engine.mm.parse(partsBySpace[1]));
                        print("Fen " + FenUtils.cat(engine.cb.fenParts), saveToLog);
                    }
                    break;
                case "state":
                    print(engine.cb.gs.toString(),saveToLog);
                    break;
                case "checkers":
                    if(engine.cb.gs == GameState.CHECK){
                        for(Integer index:engine.cb.checkers.keySet()){
                            print(Util.cvtCoord(index),saveToLog);
                        }
                    }else{
                        print(engine.cb.gs,saveToLog);
                    }
                    break;
                case "pinned":
                    for(Integer index:engine.cb.pinnedPieces.keySet()){
                        print(Util.cvtCoord(index),saveToLog);
                    }
                    break;
                case "stats":
                    print(engine.cb.stats(),saveToLog);
                    break;
                case "list":
                    output = "";
                    int i=1;
                    for(String move : engine.mm.getAllMoves()){
                        output += Integer.toString(i) + ". "+engine.mm.cvt(move) + " eval ";
                        engine.mm.makeMove(move);
                        output += engine.ev.evaluate()+"\n";
                        engine.mm.undoMove(move);
                        i+=1;
                    }
                    print(output,saveToLog);
                    break;

            }
            input = "";
        }

    }

    private static void print(Object o,boolean saveToLog){
        if(saveToLog){
            Util.writeToLog(o.toString());
        }else{
            System.out.println(o);
        }
    }



}
// position startpos server.move c2c3 c7c5 c3c4 b7b5 a2a3 b5c4 d2d3 d8b6 b1c3 c8b7 a3a4
// ,g1f3,g1h3,c1d2,c1e3,c1f4,c1g5,c1h6,d1c2,d1d2,d1a4,e1d2
// position startpos server.move c2c3 c7c5 c3c4 b7b5 a2a3 b5c4 d2d3 d8b6 b1c3 c8b7 a3a4 d7d5 a1a2 d5d4 e1d2 d4c3 -> solved
// position startpos server.move c2c3 c7c5 c3c4 b7b5 a2a3 b5c4 d2d3 d8b6 b1c3 c8b7 a3a4 d7d5 a1a2 d5d4 e1d2 b8d7 g1h3 -> depth 1 d7 not found after going perft 2 and then 1, fen changes somewhere in between
// position fen r3r1k1/pp3pbp/1qp3p1/2B5/2bP4/Q1n2N2/P2R1PPP/5K1R w - - 0 19->did not find d2d3
// position fen 8/4k3/8/8/8/8/4r2r/1R2K2R w K - 2 2 -> cant detect check, problem in cb.checkBoard()
// position fen 8/4k3/8/8/8/8/2r4r/R3K1R1 b Q - 3 2 -> go perft 1=correct output[check detects], go perft 2=incorrect output[cant detect check]