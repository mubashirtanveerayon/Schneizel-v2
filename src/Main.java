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


        //position fen 5k2/n1Q4R/4p3/p3P2p/8/6K1/1P5P/2N5 w - - 2 42->wrong best move
        //position fen 1rb1k2r/1p1p1p2/p1n1p1pp/2Q1P3/6n1/2N2N2/PP3PPP/2KR1B1R w - - 1 16->wrong best move
        //position fen rnbqkbnr/pp2pppp/8/8/2ppP3/8/PP2PPPP/RNBQKBNR w KQkq - 0 1->wrong best move
        //position fen r3kbnr/ppp1pppp/8/3P1b2/2q5/1P6/P3PPPP/RNBQKB1R b KQkq - 0 8->wrong best move
        boolean debugging = false;
        if (debugging) {
            //debug();


            float n = Float.POSITIVE_INFINITY * -0;
            System.out.println(n);

//            ChessBoard cb = new ChessBoard();
//            long start = System.nanoTime();
//            FenUtils.cat(cb.fenParts);
//            System.out.println("fen " +(System.nanoTime() - start));
//            start = System.nanoTime();
//            cb.generateZobristKey(true);
//            System.out.println("key " +(System.nanoTime() - start));


//            HashMap<String,Integer> map = new HashMap<>();
//            map.put("a",1);
//            map.put("b",2);
//
//            map.put("v",0);
//
//           for(String s:map.keySet()){
//               map.put(s,3);
//           }
//
//            System.out.println(String.valueOf(Float.NEGATIVE_INFINITY));
//

//            Engine2 engine = new Engine2("rn2k1nr/1R2b3/8/p1p1p1pp/b2pN3/3P1P1P/qP2P1B1/2BQ1KNR w kq - 1 16");
//
//
//            System.out.println(engine.cb.stats());
//
//            long currentTime = System.currentTimeMillis();
//
//            engine.setDepth(6);
//            engine.beginSearch();
//
//            while(engine.searching){
//                System.out.print("");
//            }
//
//            long timeTaken = System.currentTimeMillis() - currentTime;
//
//            System.out.println("Time taken: "+timeTaken+" ms");


//            ArrayList<String> moves = engine.mm.getAllMoves();
//            System.out.println(moves);
//            engine.orderMoves(moves);
//            System.out.println(moves);
//
//
//            String g=null;
//            System.out.println("b "+g);

        } else {
            UCI cli = new UCI();
            cli.toggle();
            cli.run();
        }
    }
}
