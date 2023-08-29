import schneizel.Engine;
import server.board.ChessBoard;
import server.move.MoveManager;
import server.pgn.PGNParser;
import server.uci.UCI;
import server.util.FenUtils;

import java.util.ArrayList;
import java.util.HashMap;
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

        UCI cli = new UCI();
        cli.toggle();
        cli.run();


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


    }

}
