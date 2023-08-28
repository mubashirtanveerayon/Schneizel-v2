package schneizel;

import server.board.ChessBoard;
import server.util.Constants;
import server.util.Util;

import java.util.ArrayList;

public class Evaluation {

    ChessBoard cb;



    public Evaluation(ChessBoard cb_){
        cb = cb_;
    }


    public float evaluateLeafNode(ArrayList<String> moves){
        float eval = evaluate();
        ArrayList<String> captures = new ArrayList<>();
        for(String move:moves){
            if (move.contains(Constants.KING_SIDE_CASTLING)){
                continue;
            }
            String[] moveParts = move.split(Constants.MOVE_SEPARATOR);
            if(moveParts[1].charAt(0) == Constants.EMPTY_SQUARE){
                continue;
            }
            captures.add(move);
        }
        return eval;
    }



    public float evaluate(){
        float eval = 0;

        eval += countMaterial();

        eval += evaluatePawnStructure();
        //pinned pieces disadvantage
        float numberOfPinnedPieces = cb.pinnedPieces.size();

        numberOfPinnedPieces *= cb.turn == Constants.WHITE? 1 : -1;

        eval -= numberOfPinnedPieces;

       // eval += evaluateOpponentKingPosition();

        return eval;
    }

    private float evaluateOpponentKingPosition(){
        float eval = 0;
        int[] opponentKing = cb.turn == Constants.WHITE? cb.blackKingPosition:cb.whiteKingPosition;
        int dstFromCenter = Math.abs(3 - opponentKing[0]) + Math.abs(3 - opponentKing[1]);
        int[] king = cb.kingPosition();
        int dstFromKing = Math.abs(king[0] - opponentKing[0]) + Math.abs(king[1] - opponentKing[1]);
        eval += (float)(dstFromKing+dstFromCenter) * Integer.parseInt(cb.fenParts[cb.fenParts.length-1])/ (cb.pieceLocations.size() * 12);
        eval *= cb.turn == Constants.WHITE? 1 : -1;
        return eval;
    }

    private float evaluatePawnStructure() {
        float eval=0;

        return eval;
    }

    private float countMaterial() {//whiteMaterial - blackMaterial
        float eval = 0;
        for(int pieceIndex:cb.pieceLocations) {
            eval += Character.isUpperCase(cb.board[pieceIndex / 8][pieceIndex % 8])?Util.getPieceValue(cb.board[pieceIndex / 8][pieceIndex % 8]):-Util.getPieceValue(cb.board[pieceIndex / 8][pieceIndex % 8]);
        }

        return eval;
    }


}
