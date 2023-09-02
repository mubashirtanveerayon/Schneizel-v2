package server.evaluation;

import server.board.ChessBoard;
import server.util.Constants;
import server.util.Map;
import server.util.Util;

public class Evaluation2 {

    ChessBoard cb;



    public Evaluation2(ChessBoard cb_){
        cb = cb_;
    }



    public float evaluate(){
        float eval = 0;
        eval += countMaterialAndPositionalScore2();
        eval -= cb.pinnedPieces.size()/3.7f;
        eval += opponentKingPosition();
        return cb.turn == Constants.WHITE?eval:-eval;
    }


    private float opponentKingPosition(){
        float eval = 0;
        int[] opponentKing = cb.turn == Constants.WHITE? cb.blackKingPosition:cb.whiteKingPosition;
        int dstFromCenter = Math.abs(3 - opponentKing[0]) + Math.abs(3 - opponentKing[1]);
        int[] king = cb.kingPosition();
        int dstBetweenKing = Math.abs(king[0] - opponentKing[0]) + Math.abs(king[1] - opponentKing[1]);
        eval += (float)(dstFromCenter - dstBetweenKing) * Integer.parseInt(cb.fenParts[cb.fenParts.length-1])/ (cb.pieceLocations.size() * 12);
        return eval;
    }

    private float countMaterial(){//faulty
        float eval = 0;
        for(int index:cb.pieceLocations){
            eval += Character.isUpperCase(cb.board[index / 8][index % 8])? Util.getPieceValue(cb.board[index / 8][index % 8]):-Util.getPieceValue(cb.board[index/8][index%8]);
        }
        return eval;
    }

    private float countMaterialAndPositionalScore2() {//whiteMaterial - blackMaterial
        float eval = 0;
        //float positionalAdvantage = (float) cb.countPieces(cb.turn == Constants.WHITE)/(Integer.parseInt(cb.fenParts[cb.fenParts.length-1]) * 10);
        float positionalAdvantage = (float) cb.pieceLocations.size()/(Integer.parseInt(cb.fenParts[cb.fenParts.length-1]) * 10);
        for(int pieceIndex:cb.pieceLocations) {
            int file = pieceIndex % 8,rank = pieceIndex / 8;
            switch(Character.toUpperCase(cb.board[rank][file])){
                case Constants.WHITE_PAWN:
                    if(Character.isUpperCase(cb.board[rank][file])){
                        eval += Constants.PAWN_VALUE;
                        eval += Map.PAWN[pieceIndex] * positionalAdvantage;
                    }else{
                        eval -= Constants.PAWN_VALUE;
                        eval -= Map.PAWN[Map.MIRRORED_MAP[pieceIndex]] * positionalAdvantage;
                    }
                    break;
                case Constants.WHITE_ROOK:
                    if(Character.isUpperCase(cb.board[rank][file])){
                        eval += Constants.ROOK_VALUE;
                        eval += Map.ROOK[pieceIndex] * positionalAdvantage;
                    }else{
                        eval -= Constants.ROOK_VALUE;
                        eval -= Map.ROOK[Map.MIRRORED_MAP[pieceIndex]] * positionalAdvantage;
                    }
                    break;

                case Constants.WHITE_KNIGHT:
                    if(Character.isUpperCase(cb.board[rank][file])){
                        eval += Constants.KNIGHT_VALUE;
                        eval += Map.KNIGHT[pieceIndex] * positionalAdvantage;
                    }else{
                        eval -= Constants.KNIGHT_VALUE;
                        eval -= Map.KNIGHT[Map.MIRRORED_MAP[pieceIndex]] * positionalAdvantage;
                    }
                    break;
                case Constants.WHITE_BISHOP:
                    if(Character.isUpperCase(cb.board[rank][file])){
                        eval += Constants.BISHOP_VALUE;
                        eval += Map.BISHOP[pieceIndex] * positionalAdvantage;
                    }else{
                        eval -= Constants.BISHOP_VALUE;
                        eval -= Map.BISHOP[Map.MIRRORED_MAP[pieceIndex]] * positionalAdvantage;
                    }
                    break;

                case Constants.WHITE_QUEEN:
                    if(Character.isUpperCase(cb.board[rank][file]) ){
                        eval += Constants.QUEEN_VALUE;
                    }else{
                        eval -= Constants.QUEEN_VALUE;
                    }
                    break;
            }


        }

        return eval;
    }



    private float countMaterialAndPositionalScore() {//whiteMaterial - blackMaterial
        float eval = 0;
        //float positionalAdvantage = (float) cb.countPieces(cb.turn == Constants.WHITE)/(Integer.parseInt(cb.fenParts[cb.fenParts.length-1]) * 10);
        float positionalAdvantage = (float) cb.pieceLocations.size()/(Integer.parseInt(cb.fenParts[cb.fenParts.length-1]) * 10);
        for(int pieceIndex:cb.pieceLocations) {
            int file = pieceIndex % 8,rank = pieceIndex / 8;
            switch(Character.toUpperCase(cb.board[rank][file])){
                case Constants.WHITE_PAWN:
                    if(cb.turn == Constants.WHITE){
                        if(Character.isUpperCase(cb.board[rank][file])){
                            eval += Constants.PAWN_VALUE;
                            eval += Map.PAWN[pieceIndex] * positionalAdvantage;
                        }else{
                            eval -= Constants.PAWN_VALUE;
                            eval -= Map.PAWN[Map.MIRRORED_MAP[pieceIndex]] * positionalAdvantage;
                        }
                    }else{
                        if(Character.isUpperCase(cb.board[rank][file])){
                            eval -= Constants.PAWN_VALUE;
                            eval -= Map.PAWN[pieceIndex] * positionalAdvantage;
                        }else{
                            eval += Constants.PAWN_VALUE;
                            eval += Map.PAWN[Map.MIRRORED_MAP[pieceIndex]] * positionalAdvantage;
                        }
                    }

                    break;
                case Constants.WHITE_ROOK:
                    if(cb.turn == Constants.WHITE){
                        if(Character.isUpperCase(cb.board[rank][file])){
                            eval += Constants.ROOK_VALUE;
                            eval += Map.ROOK[pieceIndex] * positionalAdvantage;
                        }else{
                            eval -= Constants.ROOK_VALUE;
                            eval -= Map.ROOK[Map.MIRRORED_MAP[pieceIndex]] * positionalAdvantage;
                        }
                    }else{
                        if(Character.isUpperCase(cb.board[rank][file])){
                            eval -= Constants.ROOK_VALUE;
                            eval -= Map.ROOK[pieceIndex] * positionalAdvantage;
                        }else{
                            eval += Constants.ROOK_VALUE;
                            eval += Map.ROOK[Map.MIRRORED_MAP[pieceIndex]] * positionalAdvantage;
                        }
                    }
                    break;

                case Constants.WHITE_KNIGHT:
                    if(cb.turn == Constants.WHITE){
                        if(Character.isUpperCase(cb.board[rank][file])){
                            eval += Constants.KNIGHT_VALUE;
                            eval += Map.KNIGHT[pieceIndex] * positionalAdvantage;
                        }else{
                            eval -= Constants.KNIGHT_VALUE;
                            eval -= Map.KNIGHT[Map.MIRRORED_MAP[pieceIndex]] * positionalAdvantage;
                        }
                    }else{
                        if(Character.isUpperCase(cb.board[rank][file])){
                            eval -= Constants.KNIGHT_VALUE;
                            eval -= Map.KNIGHT[pieceIndex] * positionalAdvantage;
                        }else{
                            eval += Constants.KNIGHT_VALUE;
                            eval += Map.KNIGHT[Map.MIRRORED_MAP[pieceIndex]] * positionalAdvantage;
                        }
                    }
                    break;
                case Constants.WHITE_BISHOP:
                    if(cb.turn == Constants.WHITE){
                        if(Character.isUpperCase(cb.board[rank][file])){
                            eval += Constants.BISHOP_VALUE;
                            eval += Map.BISHOP[pieceIndex] * positionalAdvantage;
                        }else{
                            eval -= Constants.BISHOP_VALUE;
                            eval -= Map.BISHOP[Map.MIRRORED_MAP[pieceIndex]] * positionalAdvantage;
                        }
                    }else{
                        if(Character.isUpperCase(cb.board[rank][file])){
                            eval -= Constants.BISHOP_VALUE;
                            eval -= Map.BISHOP[pieceIndex] * positionalAdvantage;
                        }else{
                            eval += Constants.BISHOP_VALUE;
                            eval += Map.BISHOP[Map.MIRRORED_MAP[pieceIndex]] * positionalAdvantage;
                        }
                    }
                    break;

                case Constants.WHITE_QUEEN:
                    if(Util.isEnemyPiece(cb.turn,cb.board[rank][file]) ){
                        eval -= Constants.QUEEN_VALUE;
                    }else{
                        eval += Constants.QUEEN_VALUE;
                    }
                    break;
            }


        }

        return eval;
    }


}
