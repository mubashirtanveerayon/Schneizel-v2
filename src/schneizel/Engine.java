package schneizel;

import server.board.ChessBoard;
import server.move.MoveManager;
import server.util.Constants;
import server.util.GameState;
import server.util.Util;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;

public class Engine {

    public ChessBoard cb;
    public MoveManager mm;

    public Evaluation ev;
    public int depth = 4;

    private long[][][] zArray;

    HashMap<Long, Float > transpositionTable;
    long turnRandomValue;

    public Engine(){
        cb = new ChessBoard();
        mm = new MoveManager(cb);
        ev = new Evaluation(cb);
        initZobristArray();
    }

    public Engine(ChessBoard cb_){
        cb = cb_;
        mm = new MoveManager(cb);
        ev = new Evaluation (cb);
        initZobristArray();
    }

    public Engine(String fen){
        cb = new ChessBoard(fen);
        mm = new MoveManager(cb);
        ev = new Evaluation (cb);
        initZobristArray();
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
                System.out.println(mm.cvt(move)+" eval "+score);
            }else{
                System.out.println(mm.cvt(move)+" eval "+score);
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
            long posKey = generateZobristKey();
            if(transpositionTable.containsKey(posKey)){
                return transpositionTable.get(posKey);
            }
            float eval = ev.evaluate();
            savePosition(posKey,eval);
            return eval;
//            return ev.evaluate();
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


    public void makeMove(String move){
        mm.makeMove(move);
        if(!move.contains(Constants.KING_SIDE_CASTLING) ){
            if(move.split(Constants.MOVE_SEPARATOR)[1].charAt(0) != Constants.EMPTY_SQUARE || move.contains(Constants.EN_PASSANT_NOTATION)){
                transpositionTable.clear();
            }
        }
    }

    public void undoMove(String move){
        mm.undoMove(move);
        if(!move.contains(Constants.KING_SIDE_CASTLING) ){
            if(move.split(Constants.MOVE_SEPARATOR)[1].charAt(0) != Constants.EMPTY_SQUARE || move.contains(Constants.EN_PASSANT_NOTATION)){
                transpositionTable.clear();
            }
        }
    }

    private void initZobristArray(){
        transpositionTable = new HashMap<>();
        SecureRandom rand = new SecureRandom();
        zArray = new long[2][6][64];
        for (int i=0;i<2;i++){
            for(int j=0;j<6;j++){
                for(int k=0;k<64;k++){
                    zArray[i][j][k] = rand.nextLong();
                }
            }
        }
        turnRandomValue = rand.nextLong();
    }


    public long generateZobristKey(){
        long key = 0;
        for(int pieceIndex:cb.pieceLocations){
            switch(cb.board[pieceIndex/8][pieceIndex%8]){
                case Constants.WHITE_PAWN:
                    key ^= zArray[1][0][pieceIndex];
                    break;
                case Constants.BLACK_PAWN:
                    key ^= zArray[0][0][pieceIndex];
                    break;
                case Constants.WHITE_QUEEN:
                    key ^= zArray[1][1][pieceIndex];
                    break;
                case Constants.BLACK_QUEEN:
                    key ^= zArray[0][1][pieceIndex];
                    break;
                case Constants.WHITE_ROOK:
                    key ^= zArray[1][2][pieceIndex];
                    break;
                case Constants.BLACK_ROOK:
                    key ^= zArray[0][2][pieceIndex];
                    break;
                case Constants.WHITE_BISHOP:
                    key ^= zArray[1][3][pieceIndex];
                    break;
                case Constants.BLACK_BISHOP:
                    key ^= zArray[0][3][pieceIndex];
                    break;
                case Constants.WHITE_KNIGHT:
                    key ^= zArray[1][4][pieceIndex];
                    break;
                case Constants.BLACK_KNIGHT:
                    key ^= zArray[0][4][pieceIndex];
                    break;
                case Constants.WHITE_KING:
                    key ^= zArray[1][5][pieceIndex];
                    break;
                case Constants.BLACK_KING:
                    key ^= zArray[0][5][pieceIndex];
                    break;
            }
        }
        if (cb.turn == Constants.WHITE){
            key ^= turnRandomValue;
        }
        return key;
    }

    public void savePosition(long key, float eval){
        transpositionTable.put(key,eval);
    }


}
