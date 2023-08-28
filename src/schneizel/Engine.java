package schneizel;

import server.board.ChessBoard;
import server.move.MoveManager;
import server.util.Constants;
import server.util.GameState;
import server.util.Util;

import java.util.ArrayList;

public class Engine {

    public ChessBoard cb;
    public MoveManager mm;

    public Evaluation ev;
    public int depth = 5;

    public Engine(){
        cb = new ChessBoard();
        mm = new MoveManager(cb);
        ev = new Evaluation(cb);
    }

    public Engine(ChessBoard cb_){
        cb = cb_;
        mm = new MoveManager(cb);
        ev = new Evaluation (cb);
    }

    public Engine(String fen){
        cb = new ChessBoard(fen);
        mm = new MoveManager(cb);
        ev = new Evaluation (cb);
    }

    public void setDepth(int value){
        if (depth>0 && depth < 6){
            depth = value;
        }
    }

    public String search(){
        ArrayList<String> moves = mm.getAllMoves();

        orderMoves(moves);

        String bestMove = moves.get(0);
        float score,bestScore = cb.turn == Constants.WHITE?Float.NEGATIVE_INFINITY:Float.POSITIVE_INFINITY;
        for(String move:moves){
            mm.makeMove(move);
            score = minimax(depth,Float.NEGATIVE_INFINITY, Float.POSITIVE_INFINITY,cb.turn != Constants.WHITE);
            mm.undoMove(move);
            if(cb.turn == Constants.WHITE){
                if (score>bestScore){
                    bestScore = score;
                    bestMove = move;
                }
            }else{
                if (score<bestScore){
                    bestScore = score;
                    bestMove = move;
                }
            }
        }

        //System.out.println("best score "+bestScore);
        return bestMove;
    }

    public float minimax(int depth,float alpha,float beta,boolean maximizing){

        ArrayList<String> moves = mm.getAllMoves();
        orderMoves(moves);
        if (moves.isEmpty()){
            if(cb.gs == GameState.CHECK){
                if(cb.turn == Constants.WHITE){
                    return Float.NEGATIVE_INFINITY;
                }else{
                    return Float.POSITIVE_INFINITY;
                }
            }else {
                return 0;
            }
        }else if(Integer.parseInt(cb.fenParts[11]) == 100){
            return 0;
        }else if(depth == 0){
            return ev.evaluate();
        }

        float score,bestScore = maximizing?Float.NEGATIVE_INFINITY:Float.POSITIVE_INFINITY;

        for (String move:moves){
            mm.makeMove(move);
            score = minimax(depth - 1, alpha,beta,!maximizing);
            mm.undoMove(move);
            if(maximizing){
                bestScore = Math.max(bestScore,score);
                alpha = Math.max(alpha,score);
                if (beta <= alpha){
                    break;
                }
            }else{
                bestScore = Math.min(bestScore,score);
                beta = Math.min(beta,score);
                if (beta <= alpha){
                    break;
                }
            }
        }

        return bestScore;
    }




    private void orderMoves(ArrayList<String> moves) {

        ArrayList<String> bestMoves = new ArrayList<>();
        for(String move:moves){
            if (!move.contains(Constants.KING_SIDE_CASTLING)){
                String[] moveParts = move.split(Constants.MOVE_SEPARATOR);
                if (moveParts[1].charAt(0) != Constants.EMPTY_SQUARE){
                    if (Util.getPieceValue(cb.board[Integer.parseInt(Character.toString(move.charAt(1)))][Integer.parseInt(Character.toString(move.charAt(0)))]) < Util.getPieceValue(moveParts[1].charAt(0))){
                        bestMoves.add(move);
                    }else if(moveParts.length == Constants.PROMOTION_MOVE_LENGTH && !moveParts[moveParts.length-1].equals(Constants.EN_PASSANT_NOTATION)){
                        bestMoves.add(move);
                    }
                }else if(moveParts.length == Constants.PROMOTION_MOVE_LENGTH && !moveParts[moveParts.length-1].equals(Constants.EN_PASSANT_NOTATION)){
                    bestMoves.add(move);
                }
            }
        }
        moves.removeAll(bestMoves);
        moves.addAll(0,bestMoves);
    }


}
