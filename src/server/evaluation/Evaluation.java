package server.evaluation;

import server.board.ChessBoard;
import server.util.Constants;
import server.util.Util;

public class Evaluation {

    ChessBoard cb;



    public Evaluation(ChessBoard cb_){
        cb = cb_;
    }



    public float evaluate3(){
        float eval = 0;
        float positionalAdvantage = cb.pieceLocations.size() /(20.7f);
        if (cb.whiteToMove) {

            for(int pieceIndex:cb.pieceLocations) {
                final int rank = pieceIndex / 8;
                final int file = pieceIndex % 8;
                char piece = cb.board[rank][file];

                switch (Character.toUpperCase(piece)) {
                    case Constants.WHITE_PAWN:
                        int dr;
                        if (Character.isUpperCase(piece)) {
                            eval += Constants.PAWN_VALUE;
                            eval += Map.PAWN[pieceIndex] * positionalAdvantage;
                            eval += (3-rank) * 0.07f;
                            dr = -1;
                        } else {
                            eval -= Constants.PAWN_VALUE;
                            eval -= Map.PAWN[63 - pieceIndex] * positionalAdvantage;
                            eval -= (rank-4) * 0.07f ;
                            dr = 1;
                        }
                        boolean doubled = false;
                        int blockageCount = 0;
                        int j = file;
                        for (int df = -1; df < 2; df++) {
                            for (int i = rank + dr; Util.isValid(j += df, i); i += dr) {
                                if (Character.toUpperCase(cb.board[i][j]) == Constants.WHITE_PAWN) {
                                    blockageCount++;
                                    if (Util.isEnemyPiece(cb.whiteToMove, cb.board[i][j])) {

                                    }else{
                                        doubled = true;
                                    }
                                    break;
                                }
                            }
                        }
                        int coveredBy = 0;
                        if (Util.isValid(file - 1, rank - dr)) {
                            if (Character.toUpperCase(cb.board[rank - dr][file - 1]) == Constants.WHITE_PAWN && Util.isAlly(cb.board[rank][file], cb.board[rank - dr][file - 1])) {
                                coveredBy++;
                            }
                        }
                        if (Util.isValid(file + 1, rank - dr)) {
                            if (Character.toUpperCase(cb.board[rank - dr][file + 1]) == Constants.WHITE_PAWN && Util.isAlly(cb.board[rank][file], cb.board[rank - dr][file + 1])) {
                                coveredBy++;
                            }
                        }

                        eval += coveredBy * Constants.COVERED_PAWN_BONUS * -dr;
                        eval += (3-blockageCount)*Constants.PASSED_PAWN_BONUS * -dr;
                        if(doubled){
                            eval -= Constants.DOUBLED_PAWN_PENALTY * -dr;
                        }

                        break;
                    case Constants.WHITE_ROOK:
                        if (Character.isUpperCase(piece)) {
                            eval += Constants.ROOK_VALUE;
                            eval += Map.ROOK[pieceIndex] * positionalAdvantage;
                        } else {
                            eval -= Constants.ROOK_VALUE;
                            eval -= Map.ROOK[63 - pieceIndex] * positionalAdvantage;
                        }
                        break;
                    case Constants.WHITE_KNIGHT:
                        if (Character.isUpperCase(piece)) {
                            eval += Constants.KNIGHT_VALUE;
                            eval += Map.KNIGHT[pieceIndex] * positionalAdvantage;
                        } else {
                            eval -= Constants.KNIGHT_VALUE;
                            eval -= Map.KNIGHT[63 - pieceIndex] * positionalAdvantage;
                        }
                        break;
                    case Constants.WHITE_BISHOP:
                        if (Character.isUpperCase(piece)) {
                            eval += Constants.BISHOP_VALUE;
                            eval += Map.BISHOP[pieceIndex] * positionalAdvantage;
                        } else {
                            eval -= Constants.BISHOP_VALUE;
                            eval -= Map.BISHOP[63 - pieceIndex] * positionalAdvantage;
                        }
                        break;
                    case Constants.WHITE_QUEEN:
                        if (Character.isUpperCase(piece)) {
                            eval += Constants.QUEEN_VALUE;
                        } else {
                            eval -= Constants.QUEEN_VALUE;
                        }
                        break;

                }
            }

        }else{
            for(int pieceIndex:cb.pieceLocations) {
                final int rank = pieceIndex / 8;
                final int file = pieceIndex % 8;
                char piece = cb.board[rank][file];

                switch (Character.toUpperCase(piece)) {
                    case Constants.WHITE_PAWN:
                        int dr;
                        if (Character.isUpperCase(piece)) {
                            eval -= Constants.PAWN_VALUE;
                            eval -= Map.PAWN[pieceIndex] * positionalAdvantage;
                            eval -= (3-rank) * 0.07f;
                            dr = -1;
                        } else {
                            eval += Constants.PAWN_VALUE;
                            eval += Map.PAWN[63 - pieceIndex] * positionalAdvantage;
                            eval += (rank-4) * 0.07f ;
                            dr = 1;
                        }
                        boolean doubled = false;
                        int blockageCount = 0;
                        int j = file;
                        for (int df = -1; df < 2; df++) {
                            for (int i = rank + dr; Util.isValid(j += df, i); i += dr) {
                                if (Character.toUpperCase(cb.board[i][j]) == Constants.WHITE_PAWN) {
                                    blockageCount++;
                                    if (Util.isEnemyPiece(cb.whiteToMove, cb.board[i][j])) {

                                    }else{
                                        doubled = true;
                                    }
                                    break;
                                }
                            }
                        }
                        int coveredBy = 0;
                        if (Util.isValid(file - 1, rank - dr)) {
                            if (Character.toUpperCase(cb.board[rank - dr][file - 1]) == Constants.WHITE_PAWN && Util.isAlly(cb.board[rank][file], cb.board[rank - dr][file - 1])) {
                                coveredBy++;
                            }
                        }
                        if (Util.isValid(file + 1, rank - dr)) {
                            if (Character.toUpperCase(cb.board[rank - dr][file + 1]) == Constants.WHITE_PAWN && Util.isAlly(cb.board[rank][file], cb.board[rank - dr][file + 1])) {
                                coveredBy++;
                            }
                        }

                        eval += coveredBy * Constants.COVERED_PAWN_BONUS * dr;
                        eval += (3-blockageCount)*Constants.PASSED_PAWN_BONUS * dr;
                        if(doubled){
                            eval -= Constants.DOUBLED_PAWN_PENALTY * dr;
                        }


                        break;
                    case Constants.WHITE_ROOK:
                        if (Character.isUpperCase(piece)) {
                            eval -= Constants.ROOK_VALUE;
                            eval -= Map.ROOK[pieceIndex] * positionalAdvantage;
                        } else {
                            eval += Constants.ROOK_VALUE;
                            eval += Map.ROOK[63 - pieceIndex] * positionalAdvantage;
                        }
                        break;
                    case Constants.WHITE_KNIGHT:
                        if (Character.isUpperCase(piece)) {
                            eval -= Constants.KNIGHT_VALUE;
                            eval -= Map.KNIGHT[pieceIndex] * positionalAdvantage;
                        } else {
                            eval += Constants.KNIGHT_VALUE;
                            eval += Map.KNIGHT[63 - pieceIndex] * positionalAdvantage;
                        }
                        break;
                    case Constants.WHITE_BISHOP:
                        if (Character.isUpperCase(piece)) {
                            eval -= Constants.BISHOP_VALUE;
                            eval -= Map.BISHOP[pieceIndex] * positionalAdvantage;
                        } else {
                            eval += Constants.BISHOP_VALUE;
                            eval += Map.BISHOP[63 - pieceIndex] * positionalAdvantage;
                        }
                        break;
                    case Constants.WHITE_QUEEN:
                        if (Character.isUpperCase(piece)) {
                            eval -= Constants.QUEEN_VALUE;
                        } else {
                            eval += Constants.QUEEN_VALUE;
                        }
                        break;

                }
            }
        }


        int[] opponentKing = cb.whiteToMove? cb.blackKingPosition:cb.whiteKingPosition;
        int dstFromCenter = Math.abs(3 - opponentKing[0]) + Math.abs(3 - opponentKing[1]);
        int[] king = cb.kingPosition();
        int dstBetweenKing = Math.abs(king[0] - opponentKing[0]) + Math.abs(king[1] - opponentKing[1]);
        eval += (float)(dstFromCenter - dstBetweenKing) /( cb.pieceLocations.size() );

        eval -= cb.pinnedPieces.size() * Constants.PINNED_PIECES_PENALTY;

        return eval;
    }


    public float evaluate(){
        float eval = 0;
        eval += countMaterialAndPositionalScore();
        eval -= cb.pinnedPieces.size()/3.5f;
        eval += opponentKingPosition();
        return eval;
    }

    public float evaluate2(){
        float eval = 0;
        eval += countMaterialAndPositionalScore();
        eval -= cb.pinnedPieces.size()/3.5f;
        eval += opponentKingPosition();
        eval -= countAttackedSquares();
        return eval;
    }


    private float opponentKingPosition(){
        float eval = 0;
        int[] opponentKing = cb.whiteToMove? cb.blackKingPosition:cb.whiteKingPosition;
        int dstFromCenter = Math.abs(3 - opponentKing[0]) + Math.abs(3 - opponentKing[1]);
        int[] king = cb.kingPosition();
        int dstBetweenKing = Math.abs(king[0] - opponentKing[0]) + Math.abs(king[1] - opponentKing[1]);
        eval += (float)(dstFromCenter - dstBetweenKing) * cb.fullMoveClock/ (cb.pieceLocations.size() * 12);
        return eval;
    }

    public int countAttackedSquares(){
        int count = 0;
        for(int i=0;i<8;i++){
            for(int j=0;j<8;j++){
                if(cb.board[j][i] == Constants.EMPTY_SQUARE || !Util.isEnemyPiece(cb.whiteToMove,cb.board[j][i])){
                    if(cb.squareUnderAttack(i,j)){
                        count ++;
                    }
                }
            }
        }
        return count;
    }

    private float countMaterial(){//whiteMaterial - blackMaterial
        float eval = 0;
        for(int index:cb.pieceLocations){
            eval += Character.isUpperCase(cb.board[index / 8][index % 8])? Util.getPieceValue(cb.board[index / 8][index % 8]):-Util.getPieceValue(cb.board[index/8][index%8]);
        }
        return eval;
    }

    private float countMaterialAndPositionalScore2() {//whiteMaterial - blackMaterial
        float eval = 0;
        //float positionalAdvantage = (float) cb.countPieces(cb.whiteToMove)/(Integer.parseInt(cb.fenParts[cb.fenParts.length-1]) * 10);
        float positionalAdvantage = (float) cb.pieceLocations.size()/(cb.fullMoveClock * 10);
        for(int pieceIndex:cb.pieceLocations) {
            int file = pieceIndex % 8,rank = pieceIndex / 8;
            switch(Character.toUpperCase(cb.board[rank][file])){
                case Constants.WHITE_PAWN:
                    if(Character.isUpperCase(cb.board[rank][file])){
                        eval += Constants.PAWN_VALUE;
                        eval += Map.PAWN[pieceIndex] * positionalAdvantage;
                    }else{
                        eval -= Constants.PAWN_VALUE;
                        eval -= Map.PAWN[63-pieceIndex] * positionalAdvantage;
                    }
                    break;
                case Constants.WHITE_ROOK:
                    if(Character.isUpperCase(cb.board[rank][file])){
                        eval += Constants.ROOK_VALUE;
                        eval += Map.ROOK[pieceIndex] * positionalAdvantage;
                    }else{
                        eval -= Constants.ROOK_VALUE;
                        eval -= Map.ROOK[63-pieceIndex] * positionalAdvantage;
                    }
                    break;

                case Constants.WHITE_KNIGHT:
                    if(Character.isUpperCase(cb.board[rank][file])){
                        eval += Constants.KNIGHT_VALUE;
                        eval += Map.KNIGHT[pieceIndex] * positionalAdvantage;
                    }else{
                        eval -= Constants.KNIGHT_VALUE;
                        eval -= Map.KNIGHT[63-pieceIndex] * positionalAdvantage;
                    }
                    break;
                case Constants.WHITE_BISHOP:
                    if(Character.isUpperCase(cb.board[rank][file])){
                        eval += Constants.BISHOP_VALUE;
                        eval += Map.BISHOP[pieceIndex] * positionalAdvantage;
                    }else{
                        eval -= Constants.BISHOP_VALUE;
                        eval -= Map.BISHOP[63-pieceIndex] * positionalAdvantage;
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



    private float countMaterialAndPositionalScore() {//ally - enemy
        float eval = 0;
        //float positionalAdvantage = (float) cb.countPieces(cb.whiteToMove)/(Integer.parseInt(cb.fenParts[cb.fenParts.length-1]) * 10);
        float positionalAdvantage = (float) cb.pieceLocations.size()/(cb.fullMoveClock * 12.5f);
        for(int pieceIndex:cb.pieceLocations) {
            int file = pieceIndex % 8,rank = pieceIndex / 8;
            switch(Character.toUpperCase(cb.board[rank][file])){
                case Constants.WHITE_PAWN:
                    if(cb.whiteToMove){
                        if(Character.isUpperCase(cb.board[rank][file])){
                            eval += Constants.PAWN_VALUE;
                            eval += Map.PAWN[pieceIndex] * positionalAdvantage;
                            eval += (3-rank) * 0.07f ;
                        }else{
                            eval -= Constants.PAWN_VALUE;
                            eval -= Map.PAWN[63-pieceIndex] * positionalAdvantage;
                            eval -= (rank-4) * 0.07f ;
                        }
                    }else{
                        if(Character.isUpperCase(cb.board[rank][file])){
                            eval -= Constants.PAWN_VALUE;
                            eval -= Map.PAWN[pieceIndex] * positionalAdvantage;
                            eval -= (3-rank) * 0.07f ;
                        }else{
                            eval += Constants.PAWN_VALUE;
                            eval += Map.PAWN[63-pieceIndex] * positionalAdvantage;
                            eval += (rank-4)  * 0.07f ;
                        }
                    }

                    break;
                case Constants.WHITE_ROOK:
                    if(cb.whiteToMove){
                        if(Character.isUpperCase(cb.board[rank][file])){
                            eval += Constants.ROOK_VALUE;
                            eval += Map.ROOK[pieceIndex] * positionalAdvantage;
                            eval += (3-rank) * 0.2f ;
                        }else{
                            eval -= Constants.ROOK_VALUE;
                            eval -= Map.ROOK[63-pieceIndex] * positionalAdvantage;
                            eval -= (rank-4) * 0.2f ;
                        }
                    }else{
                        if(Character.isUpperCase(cb.board[rank][file])){
                                eval -= Constants.ROOK_VALUE;
                                eval -= Map.ROOK[pieceIndex] * positionalAdvantage;
                                eval -= (3-rank) * 0.2f ;
                            }else{
                                eval += Constants.ROOK_VALUE;
                                eval += Map.ROOK[63-pieceIndex] * positionalAdvantage;
                                eval += (rank-4) * 0.2f ;
                        }
                    }
                    break;

                case Constants.WHITE_KNIGHT:
                    if(cb.whiteToMove){
                        if(Character.isUpperCase(cb.board[rank][file])){
                            eval += Constants.KNIGHT_VALUE;
                            eval += Map.KNIGHT[pieceIndex] * positionalAdvantage;
                        }else{
                            eval -= Constants.KNIGHT_VALUE;
                            eval -= Map.KNIGHT[63-pieceIndex] * positionalAdvantage;
                        }
                    }else{
                        if(Character.isUpperCase(cb.board[rank][file])){
                            eval -= Constants.KNIGHT_VALUE;
                            eval -= Map.KNIGHT[pieceIndex] * positionalAdvantage;
                        }else{
                            eval += Constants.KNIGHT_VALUE;
                            eval += Map.KNIGHT[63-pieceIndex] * positionalAdvantage;
                        }
                    }
                    break;
                case Constants.WHITE_BISHOP:
                    if(cb.whiteToMove){
                        if(Character.isUpperCase(cb.board[rank][file])){
                            eval += Constants.BISHOP_VALUE;
                            eval += Map.BISHOP[pieceIndex] * positionalAdvantage;
                        }else{
                            eval -= Constants.BISHOP_VALUE;
                            eval -= Map.BISHOP[63-pieceIndex] * positionalAdvantage;
                        }
                    }else{
                        if(Character.isUpperCase(cb.board[rank][file])){
                            eval -= Constants.BISHOP_VALUE;
                            eval -= Map.BISHOP[pieceIndex] * positionalAdvantage;
                        }else{
                            eval += Constants.BISHOP_VALUE;
                            eval += Map.BISHOP[63-pieceIndex] * positionalAdvantage;
                        }
                    }
                    break;

                case Constants.WHITE_QUEEN:
                    if(Util.isEnemyPiece(cb.whiteToMove,cb.board[rank][file]) ){
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
