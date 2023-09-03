import schneizel.Engine;
import schneizel.Engine2;
import server.board.ChessBoard;
import server.move.MoveManager;
import server.pgn.PGNParser;
import server.uci.UCI;
import server.util.Constants;
import server.util.FenUtils;
import server.util.GameState;
import server.util.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    public static void main(String[] args) {


//
//        ChessBoard cb = new ChessBoard();
//        MoveManager mm = new MoveManager(cb);
//
//        ArrayList<String> pgn = (PGNParser.getMoveText("data/game1.pgn"));
//        System.out.println(pgn);
//        System.out.println(cb.stats());
//        for(String san : pgn){
//            String move = PGNParser.parseSAN(san,mm);
//            if(move == null){
//                System.out.println("move is null: "+san);
//                System.out.println(FenUtils.cat(cb.fenParts));
//                break;
//            }else{
//                mm.makeMove(move);
//                System.out.println(cb.stats());
//            }
//        }




//        String pgn = "ed6f7";
//        Pattern pattern = Pattern.compile("[a-h][1-8]");
//        Matcher matcher = pattern.matcher(pgn);
//        String[] coords = new String[2];
//        while(matcher.find()){
//            System.out.println(matcher.group());
//        }
//        System.out.println("0102".substring(2,4));
//        Engine engine = new Engine("r3r1k1/pp3pbp/1qp1b1p1/2B5/2BP4/Q1n2N2/P4PPP/3R1K1R w - - 4 18");
//        System.out.println(engine.cb.stats());
//        String move = (engine.search());
//        System.out.println(engine.mm.cvt(move));
//        System.out.println(engine.cb.stats());

//        ArrayList<String> a = new ArrayList<>();
//
//        a.add("a");
//        a.add("b");
//        a.add("c");
//        System.out.println(a.toString());
//        ArrayList<String> n = new ArrayList<>();
//
//        for(String s:a){
//            if(s.equals("b")){
//                n.add(s);
//            }
//        }
//        a.removeAll(n);
//        for (int i=0;i<n.size();i++){
//            a.add(i,n.get(i));
//        }

//        String s = a.get(1);
//        a.remove(s);
//        a.add(0,s);
//
//        System.out.println(a.toString());


        boolean debugging = false;
        if(debugging) {
            //debug();

            Engine2 engine = new Engine2("2q1k3/RP5P/6p1/5R2/8/8/6P1/3K4 w - - 0 1");





            ArrayList<String> moves = engine.mm.getAllMoves();

            for(String move:moves){
                System.out.print(engine.mm.cvt(move)+" ");
            }
            System.out.println();

            long currentTime = System.currentTimeMillis();

            engine.orderMove(moves);

            long timeTaken = System.currentTimeMillis() - currentTime;

            for(String move:moves){
                System.out.print(engine.mm.cvt(move)+" ");
            }
            System.out.println();
            System.out.println("Time taken: "+timeTaken+" ms");


//            ArrayList<String> moves = engine.mm.getAllMoves();
//            System.out.println(moves);
//            engine.orderMoves(moves);
//            System.out.println(moves);
//
//
//            String g=null;
//            System.out.println("b "+g);

        }else{
            UCI cli = new UCI();
            cli.toggle();
            cli.run();
        }
    }
    
    
    public static void debug(){
        Engine engine = new Engine();
        boolean flip=false,running = true,saveToLog = false;
        Scanner sc = new Scanner(System.in);
        String input = "";
        String output="";
        while(running ){
            input = sc.nextLine();
            String[] partsBySpace = input.split(" ");
            switch(partsBySpace[0].toLowerCase()){
                case "uci":
                    output = "id name Schneizel 2\nid author Ayon\nuciok";
                    print(output,saveToLog);
                    break;
                case "isready":
                    output = "readyok";
                    print(output,saveToLog);
                    break;
                case "go":
                    System.out.println("Thinking...");
                    if (partsBySpace.length > 1){

                        if(partsBySpace[1].equals("perft")){
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
                            engine.setDepth(prevdepth);
                            print(output,saveToLog);

                        }else{
                            long currentTime = System.nanoTime();
                            output = "bestmove "+engine.mm.cvt(engine.search());
                            output+="\nTime taken: " + TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - currentTime) + " ms";
                            print(output,saveToLog);
                        }
                    }else{
                        long currentTime = System.nanoTime();
                        output = "bestmove "+engine.mm.cvt(engine.search());
                        output+="\nTime taken: " + TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - currentTime) + " ms";
                        print(output,saveToLog);
                    }

                    break;
                case "ucinewgame":
                    input = "position startpos";
                    partsBySpace = input.split(" ");
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
                                case "moves":
                                    if(Character.isDigit(partsBySpace[3].charAt(0))||partsBySpace[3].contains(Constants.KING_SIDE_CASTLING)){
                                        String[] moves = input.split("move ")[1].split(",");
                                        for(String move:moves){
                                            engine.makeMove(move);
                                        }
                                    }else{
                                        for(int i=3;i<partsBySpace.length;i++){
                                            engine.makeMove(engine.mm.parse(partsBySpace[i]));
                                        }
                                    }
                                    break;
                                case "undomove":
                                    if(Character.isDigit(partsBySpace[3].charAt(0))||partsBySpace[3].contains(Constants.KING_SIDE_CASTLING)||partsBySpace[3].contains(Constants.QUEEN_SIDE_CASTLING)){
                                        String move = input.split("undomove ")[1];
                                        engine.undoMove(move);
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
                    output = std;
                    print(output,saveToLog);
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
                        engine.makeMove(move);
                        output = "played "+engine.mm.cvt(move)+"\n";
                        output += "Time taken: "+timeTaken + " ms\n";
                        output += "Fen " + FenUtils.cat(engine.cb.fenParts);
                        print(output,saveToLog);
                        break;
                    }
                    try{
                        engine.makeMove(engine.mm.getAllMoves().get(Integer.parseInt(partsBySpace[1])-1));
                        print("Fen " + FenUtils.cat(engine.cb.fenParts),saveToLog);
                    }catch(Exception e) {
                        engine.makeMove(engine.mm.parse(partsBySpace[1]));
                        print("Fen " + FenUtils.cat(engine.cb.fenParts),saveToLog);
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
                        print(output,saveToLog);
                    }
                    break;
                case "pinned":
                    for(Integer index:engine.cb.pinnedPieces.keySet()){
                        print(Util.cvtCoord(index),saveToLog);
                    }
                    break;
                case "stats":
                    output = engine.cb.stats();
                    print(output,saveToLog);
                    break;
                case "list":
                    output = "";
                    int i=1;
                    for(String move : engine.mm.getAllMoves()){
                        output += Integer.toString(i) + ". "+engine.mm.cvt(move) + " eval ";
                        engine.makeMove(move);
                        output += engine.ev.evaluate()+"\n";
                        engine.undoMove(move);
                        i+=1;
                    }
                    print(output,saveToLog);
                    break;
                case "eval":
                    output = "eval ";
                    float evaluation = engine.ev.evaluate();
                    output += String.valueOf(evaluation);
                    print(output,saveToLog);
                    break;
                case "pieces":
                    output = "total: "+engine.cb.pieceLocations.size()+"\n";
                    for(int index:engine.cb.pieceLocations){
                        output += engine.cb.board[index/8][index%8]+"\n";
                    }
                    print(output,saveToLog);
            }
        }
    }

    public static void print(String o,boolean saveToLog){
        if(saveToLog&&!Util.loggerInitialized()){
            Util.initLogger();
        }
        if(saveToLog){
            Util.writeToLog(o);
        }else{
            System.out.println(o);
        }
    }
    

}
//position fen rnbqkbnr/pp2pppp/8/8/2ppP3/8/PP2PPPP/RNBQKBNR w KQkq - 0 1->wrong best move
//position fen r3kbnr/ppp1pppp/8/3P1b2/2q5/1P6/P3PPPP/RNBQKB1R b KQkq - 0 8->wrong best move