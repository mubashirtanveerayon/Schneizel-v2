package schneizel;

import server.board.ChessBoard;
import server.evaluation.Evaluation2;
import server.move.MoveManager;
import server.pgn.PGNParser;
import server.util.Constants;
import server.util.GameState;
import server.util.Util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class Engine2 implements Runnable{

    public ChessBoard cb;
    public MoveManager mm;

    public Evaluation2 ev;

    public String engineMove="";


    public boolean searchCancelled = false,searching = false;


    private ArrayList<HashMap<String,String>> book;

    private boolean useBook = false;

    public static final int DEFAULT_SEARCH_DEPTH = 4;


    private int depth = 4;

    public void setDepth(int value){
        if (value>0 && value<7){
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
    }

    public Engine2(){
        cb = new ChessBoard();
        mm = new MoveManager(cb);
        ev = new Evaluation2(cb);
    }

    private void loadBook(){
        if(book != null){
            return;
        }
        book = PGNParser.parseFile("data/lichess_elite_2022-03.pgn");
    }

    @Override
    public void run(){
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
            System.out.println("bestmove "+mm.cvt(engineMove)+ " score "+bestScore);
        }
        searchCancelled = false;

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


    public Thread beginSearch(){
        if(searching){
            return null;
        }
        Thread thread = new Thread(this);
        engineMove = "";
        searching = true;
        searchCancelled = false;
        thread.start();
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
