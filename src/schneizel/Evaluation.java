package schneizel;

import server.board.ChessBoard;
import server.move.MoveManager;
import server.util.Constants;
import server.util.Map;
import server.util.Util;

import java.util.ArrayList;

public class Evaluation {

    ChessBoard cb;



    public Evaluation(ChessBoard cb_){
        cb = cb_;
    }


//    public float evaluateLeafNode(MoveManager mm, ArrayList<String> moves){
//        ArrayList<String> captures = new ArrayList<>();
//
//        for(String move:moves){
//            if (move.contains(Constants.KING_SIDE_CASTLING)){
//                continue;
//            }
//            String[] moveParts = move.split(Constants.MOVE_SEPARATOR);
//            if(moveParts[1].charAt(0) == Constants.EMPTY_SQUARE){
//                continue;
//            }
//            captures.add(move);
//        }
//        if(captures.isEmpty()){
//            return evaluate();
//        }else{
//            for(String captureMove:captures){
//                mm.makeMove(captureMove);
//
//            }
//        }
//
//    }



    public float evaluate(){
        float eval = 0;

        //eval += countMaterialAndPositionalScore();

        eval += countMaterial();

//        eval += evaluatePawnStructure();
        //pinned pieces disadvantage
//        float numberOfPinnedPieces = cb.pinnedPieces.size();
//
//        numberOfPinnedPieces *= cb.turn == Constants.WHITE? 1 : -1;
//
//        eval -= numberOfPinnedPieces/3.7f;
//
//        eval += evaluateOpponentKingPosition();

        return cb.turn == Constants.WHITE?eval:-eval;
    }

    private float evaluateOpponentKingPosition(){
        float eval = 0;
        int[] opponentKing = cb.turn == Constants.WHITE? cb.blackKingPosition:cb.whiteKingPosition;
        int dstFromCenter = Math.abs(3 - opponentKing[0]) + Math.abs(3 - opponentKing[1]);
        int[] king = cb.kingPosition();
        int dstBetweenKing = Math.abs(king[0] - opponentKing[0]) + Math.abs(king[1] - opponentKing[1]);
        eval += (float)(dstFromCenter - dstBetweenKing) * Integer.parseInt(cb.fenParts[cb.fenParts.length-1])/ (cb.pieceLocations.size() * 12);
        eval *= cb.turn == Constants.WHITE? 1 : -1;
        return eval;
    }

    private float evaluatePawnStructure() {
        float eval=0;

        return eval;
    }

    private float countMaterial(){
        float eval = 0;
        for(int index:cb.pieceLocations){
            eval += Character.isUpperCase(cb.board[index / 8][index % 8])?Util.getPieceValue(cb.board[index / 8][index % 8]):-Util.getPieceValue(cb.board[index/8][index%8]);
        }
        return eval;
    }

    private float countMaterialAndPositionalScore() {//whiteMaterial - blackMaterial
        float eval = 0;
        //float positionalAdvantage = (float) cb.countPieces(cb.turn == Constants.WHITE)/(Integer.parseInt(cb.fenParts[cb.fenParts.length-1]) * 10);
        float positionalAdvantage = (float) cb.pieceLocations.size()/(Integer.parseInt(cb.fenParts[cb.fenParts.length-1]) * 10);
        for(int pieceIndex:cb.pieceLocations) {
            int file = pieceIndex % 8,rank = pieceIndex / 8;
            switch(cb.board[rank][file]){
                case Constants.WHITE_PAWN:
                    eval += Constants.PAWN_VALUE;
                    eval += Map.PAWN[pieceIndex] * positionalAdvantage;
                    break;
                case Constants.WHITE_ROOK:
                    eval += Constants.ROOK_VALUE;
                    eval += Map.ROOK[pieceIndex] * positionalAdvantage;
                    break;

                case Constants.WHITE_KNIGHT:
                    eval += Constants.KNIGHT_VALUE;
                    eval += Map.KNIGHT[pieceIndex] * positionalAdvantage;
                    break;
                case Constants.WHITE_BISHOP:
                    eval += Constants.BISHOP_VALUE;
                    eval += Map.BISHOP[pieceIndex] * positionalAdvantage;
                    break;

                case Constants.WHITE_QUEEN:
                    eval += Constants.QUEEN_VALUE;
                    break;
                case Constants.BLACK_PAWN:
                    eval -= Constants.PAWN_VALUE;
                    eval -= Map.PAWN[Map.MIRRORED_MAP[pieceIndex]] * positionalAdvantage;
                    break;
                case Constants.BLACK_ROOK:
                    eval -= Constants.ROOK_VALUE;
                    eval -= Map.ROOK[Map.MIRRORED_MAP[pieceIndex]] * positionalAdvantage;
                    break;

                case Constants.BLACK_KNIGHT:
                    eval -= Constants.KNIGHT_VALUE;
                    eval -= Map.KNIGHT[Map.MIRRORED_MAP[pieceIndex]] * positionalAdvantage;
                    break;
                case Constants.BLACK_BISHOP:
                    eval -= Constants.BISHOP_VALUE;
                    eval -= Map.BISHOP[Map.MIRRORED_MAP[pieceIndex]] * positionalAdvantage;
                    break;

                case Constants.BLACK_QUEEN:
                    eval -= Constants.QUEEN_VALUE;
                    break;
            }


        }

        return eval;
    }




}
