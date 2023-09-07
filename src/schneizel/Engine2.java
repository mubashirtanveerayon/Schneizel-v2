package schneizel;

import server.board.ChessBoard;
import server.evaluation.Evaluation2;
import server.move.MoveManager;
import server.pgn.PGNParser;
import server.util.Constants;
import server.util.FenUtils;
import server.util.GameState;
import server.util.Util;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;

public class Engine2 implements Runnable{

    public ChessBoard cb;
    public MoveManager mm;

    public Evaluation2 ev;

    public String engineMove="";


    public boolean searchCancelled = false,searching = false;


    private ArrayList<HashMap<String,String>> book;

    public boolean useBook = true;

    public boolean useTranspositionTable = false;


    public static final int MAX_PLY_TO_USE_BOOK = 5;
    public static final int DEFAULT_SEARCH_DEPTH = 4;

    public int bookMoveType = Constants.BOOK_RANDOM;

    long searchStartTime = 0;

    private int depth = 4;


//    public HashMap<String, Float> transpositionTable;

    public static final int MAX_TABLE_ENTRIES = 100000;

    public void setDepth(int value){
        if (value>0 && value<6){
            depth = value;
        }
    }

    public int getDepth(){
        return depth;
    }


    public Engine2(String fen){
        cb = new ChessBoard(fen);
        mm = new MoveManager(cb);
        ev = new Evaluation2(cb);
//        transpositionTable = new HashMap<>();
    }

    public Engine2(){
        cb = new ChessBoard();
        mm = new MoveManager(cb);
        ev = new Evaluation2(cb);
//        transpositionTable = new HashMap<>();
    }

    public void loadBook(){
        if(book != null || !useBook){
            return;
        }
        File directory = new File("data");
        if(directory.isDirectory()) {
            for (File file : directory.listFiles()) {
                if (file.getName().endsWith(".pgn")) {
                    book = PGNParser.parseFile(file.getPath(),150);
                    System.out.println("Book loaded: "+file.getName());
                    return;
                }
            }
        }
        System.out.println("Could not find any book");
        useBook = false;
    }


    private String readBook(){
        if(book == null || !useBook){
            return null;
        }
        ArrayList<String> availableMoves = new ArrayList<>();
        //long boardKey = cb.generateZobristKey();
        String fen = FenUtils.cat(cb.fenParts);
        System.out.println("Reading book...");
        for(HashMap<String,String> game:book){
            String moveText = game.get("Moves");
            ChessBoard tcb = new ChessBoard();
            MoveManager tmm = new MoveManager(tcb);
            for(String seg:moveText.split(" ")) {
                String move = PGNParser.parseSAN(seg,tmm);
                if(move == null){
                    continue;
                }
                if(FenUtils.cat(tcb.fenParts).equals(fen)){//FenUtils.cat(tcb.fenParts).equals(FenUtils.cat(cb.fenParts))//tcb.generateZobristKey() == boardKey
                    availableMoves.add(move);
                    break;
                }else{
                    tmm.makeMove(move);
                }

            }
        }

        System.out.println(availableMoves.size() + " available moves");

        if(availableMoves.isEmpty()){
            return null;
        }

        if(bookMoveType == Constants.BOOK_RANDOM) {
            //return a random move from available moves
            return availableMoves.get(new Random().nextInt(availableMoves.size()));
        }

        if(bookMoveType == Constants.BOOK_MOSTLY_PLAYED) {

            //return the move that has the most occurrence
            String mostOccurred = null;
            int mostOccurrence = 0;
            for (String move : availableMoves) {
                int frequency = Collections.frequency(availableMoves, move);
                if (frequency > mostOccurrence) {
                    mostOccurred = move;
                    mostOccurrence = frequency;
                }
            }
            return mostOccurred;
        }

        //return the best move based on evaluation
        String bestMove=availableMoves.get(0);
        float score,bestScore = Float.NEGATIVE_INFINITY;
        for(String move:availableMoves){
            mm.makeMove(move);
            score = -ev.evaluate();
            mm.undoMove(move);
            if(score > bestScore){
                bestScore = score;
                bestMove = move;
            }
        }
        return bestMove;



    }

    @Override
    public void run(){
        System.out.println("Calculating...");
        if(useBook && Integer.parseInt(cb.fenParts[12])< MAX_PLY_TO_USE_BOOK){
            loadBook();
            String move = readBook();
            if(move != null){
                engineMove = move;
                System.out.println("bestmove "+mm.cvt(engineMove)+ " score book");
                System.out.println("Time taken "+(System.currentTimeMillis() - searchStartTime)+" ms");
                searching = false;
                searchCancelled = false;
                searchStartTime = 0;
                setDepth(DEFAULT_SEARCH_DEPTH);
                return;
            }
        }
        System.out.println("Commencing search at depth: "+depth);
        ArrayList<String> moves = mm.getAllMoves();
        float score,bestScore = Float.NEGATIVE_INFINITY;
        orderMove(moves);
        for(String move:moves){
            if(searchCancelled){
                break;
            }
            mm.makeMove(move);
            score = -negamax(depth, Float.NEGATIVE_INFINITY,Float.POSITIVE_INFINITY);
            mm.undoMove(move);
            if(score>bestScore){
                bestScore = score;
                engineMove = move;
            }
            System.out.println(mm.cvt(move)+" score "+score);
        }
        searching = false;
        setDepth(DEFAULT_SEARCH_DEPTH);

        if(searchCancelled){
            engineMove = "";
            System.out.println("Search was cancelled");
        }else{
            if(engineMove.isEmpty()){
                engineMove = moves.get(0);
            }
            System.out.println("bestmove "+mm.cvt(engineMove)+ " score "+bestScore);
            System.out.println("Time taken "+(System.currentTimeMillis() - searchStartTime)+" ms");
        }

        searchCancelled = false;
        searchStartTime = 0;

    }


    public void orderMove(ArrayList<String>moves){
        HashMap<String,Float> movesWithScore = new HashMap<>();
        float score;
        String[] moveParts;
        for(String move:moves){
            score = 0;
            moveParts = move.split(Constants.MOVE_SEPARATOR);
            if(move.contains(Constants.KING_SIDE_CASTLING)){
                score += Constants.CASTLING_SCORE;
            } else {
                if (move.contains(Constants.EN_PASSANT_NOTATION)) {
                    score += Constants.EN_PASSANT_SCORE;
                }else {
                    if (moveParts[1].charAt(0) != Constants.EMPTY_SQUARE) {
                        float valueDiff = Util.getPieceValue(moveParts[1].charAt(0)) - Util.getPieceValue(cb.board[Integer.parseInt(Character.toString(move.charAt(1)))][Integer.parseInt(Character.toString(move.charAt(0)))]);
                        score += Constants.CAPTURE_SCORE * valueDiff;
                    }
                    if (moveParts.length == Constants.PROMOTION_MOVE_LENGTH) {
                        score += Constants.PROMOTION_SCORE + Util.getPieceValue(moveParts[moveParts.length - 1].charAt(0));
                    }
                }
            }


            score = -score;
            movesWithScore.put(move,score);
        }



        movesWithScore = Util.sortHashMap(movesWithScore);

        moves.clear();

        moves.addAll(movesWithScore.keySet());


    }

    public void make(String move){
        mm.makeMove(move);

//        if(!useTranspositionTable){
//            return;
//        }
//        if(!move.contains(Constants.KING_SIDE_CASTLING) ){
//            if(move.charAt(5) != Constants.EMPTY_SQUARE || move.contains(Constants.EN_PASSANT_NOTATION)){
//                transpositionTable.clear();
//            }
//        }
//        System.out.println( "positions stored "+transpositionTable.size());
    }



    public Thread beginSearch(){
        if(searching){
            return null;
        }
        Thread thread = new Thread(this);
        engineMove = "";
        searching = true;
        searchCancelled = false;
        thread.start();
        searchStartTime = System.currentTimeMillis();
        return thread;
    }

    private float negamax(int depth,float alpha,float beta){
        ArrayList<String> moves = mm.getAllMoves();
        if(moves.isEmpty()){
            if(cb.gs == GameState.CHECK){
                return Float.NEGATIVE_INFINITY;
            }else{
                return 0;
            }
        }else if(Integer.parseInt(cb.fenParts[11]) == 100){
            return 0;
        }else if(depth == 0){
//            if(useTranspositionTable) {
//                String fen = FenUtils.cat(cb.fenParts, true);
//                if (transpositionTable.containsKey(fen)) {
//                    return transpositionTable.get(fen);
//                }
//                float eval = ev.evaluate();
//                savePosition(fen, eval);
//                return eval;
//            }else {
//                return searchAllCaptures(alpha,beta);
//            }
            return ev.evaluate();
        }

        orderMove(moves);
        float score;
        for(String move : moves){
            mm.makeMove(move);
            score = -negamax(depth-1,-beta,-alpha);
            mm.undoMove(move);
            if(score>=beta){
                return beta;
            }
            alpha = Math.max(score,alpha);
        }
        return alpha;
    }

//    private void savePosition(String fen, float eval) {
//
//
//        if(transpositionTable.size() < MAX_SIZE_TTABLE){
//            transpositionTable.put(fen,eval);
//        }else{
//            for(String key: transpositionTable.keySet()){
//                transpositionTable.remove(key);
//                break;
//            }
//            transpositionTable.put(fen,eval);
//        }
//
//
//    }

//    private void savePosition(String key, float eval) {
//
//
//        if(transpositionTable.size() < MAX_TABLE_ENTRIES){
//            transpositionTable.put(key,eval);
//        }else{
//            for(String k: transpositionTable.keySet()){
//                transpositionTable.remove(k);
//                break;
//            }
//            transpositionTable.put(key,eval);
//        }


//    }



    float searchAllCaptures(float alpha,float beta){
        float eval = ev.evaluate();
        if(eval >= beta){
            return beta;
        }

        alpha = Math.max(alpha,eval);

        if(depth == 0){
            return alpha;
        }

        ArrayList<String> moves = mm.getAllMoves();

        for(String move : moves){
            if(!move.contains(Constants.KING_SIDE_CASTLING) && move.charAt(5) != Constants.EMPTY_SQUARE){
                mm.makeMove(move);
                eval = -searchAllCaptures(-beta,-alpha);
                mm.undoMove(move);
                if(eval>=beta){
                    return beta;
                }
                alpha = Math.max(alpha,eval);
            }
        }
        return alpha;
    }




}
