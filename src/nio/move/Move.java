package nio.move;

import board.ChessBoard;
import util.Constants;
import util.FenUtils;
import util.Util;

import java.util.ArrayList;

public class Move {

    public ChessBoard cb;

    public Move(ChessBoard cb) {
        this.cb = cb;
    }

    public Move() {
        this.cb = new ChessBoard();
    }

    public void makeMove(String move){
        if(move.contains(Constants.KING_SIDE_CASTLING)){
            int rank = cb.turn == Constants.WHITE?7:0;
            cb.board[rank][6] = cb.board[rank][4];
            cb.board[rank][4] = Constants.EMPTY_SQUARE;
            cb.board[rank][5] = cb.board[rank][7];
            cb.board[rank][7] = Constants.EMPTY_SQUARE;
            cb.pieceLocations.add(6 + rank * 8);
            cb.pieceLocations.remove((Object)(4 + rank * 8));
            cb.pieceLocations.add(5 + rank * 8);
            cb.pieceLocations.remove((Object)(7 + rank * 8));
            if(cb.turn == Constants.WHITE){
                cb.whiteKingPosition[0] = 6;
                cb.whiteKingPosition[1] = 7;
            }else{
                cb.blackKingPosition[0] = 6;
                cb.blackKingPosition[1] = 0;
            }
            cb.fenParts[rank] = FenUtils.getRank(cb.board[rank]);
            if(cb.turn  == Constants.WHITE){
                cb.fenParts[9] = cb.fenParts[9].replace(Character.toString(Constants.WHITE_KING),"").replace(Character.toString(Constants.WHITE_QUEEN),"");
            }else{
                cb.fenParts[9] = cb.fenParts[9].replace(Character.toString(Constants.BLACK_KING),"").replace(Character.toString(Constants.BLACK_QUEEN),"");
            }
            cb.turn  = cb.turn  == Constants.WHITE?Constants.BLACK:Constants.WHITE;
            cb.fenParts[8] = Character.toString(cb.turn);
            cb.fenParts[10] = "-";
        }else if(move.contains(Constants.QUEEN_SIDE_CASTLING)){
            int rank = cb.turn == Constants.WHITE?7:0;
            cb.board[rank][2] = cb.board[rank][4];
            cb.board[rank][4] = Constants.EMPTY_SQUARE;
            cb.board[rank][3] = cb.board[rank][0];
            cb.board[rank][0] = Constants.EMPTY_SQUARE;
            cb.pieceLocations.add(2 + rank * 8);
            cb.pieceLocations.remove((Object)(4 + rank * 8));
            cb.pieceLocations.add(3 + rank * 8);
            cb.pieceLocations.remove((Object)(0 + rank * 8));
            if(cb.turn == Constants.WHITE){
                cb.whiteKingPosition[0] = 2;
                cb.whiteKingPosition[1] = 7;
            }else{
                cb.blackKingPosition[0] = 2;
                cb.blackKingPosition[1] = 0;
            }
            cb.fenParts[rank] = FenUtils.getRank(cb.board[rank]);
            if(cb.turn  == Constants.WHITE){
                cb.fenParts[9] = cb.fenParts[9].replace(Character.toString(Constants.WHITE_KING),"").replace(Character.toString(Constants.WHITE_QUEEN),"");
            }else{
                cb.fenParts[9] = cb.fenParts[9].replace(Character.toString(Constants.BLACK_KING),"").replace(Character.toString(Constants.BLACK_QUEEN),"");
            }
            cb.turn  = cb.turn  == Constants.WHITE?Constants.BLACK:Constants.WHITE;
            cb.fenParts[8] = Character.toString(cb.turn);
            cb.fenParts[10] = "-";
        }else if(move.charAt(1) == move.charAt(3)){
            int rank = move.charAt(1);
            int locFile = move.charAt(0);
            int destFile = move.charAt(2);
            if(cb.board[rank][locFile] == Constants.WHITE_KING){
                cb.whiteKingPosition[0] = destFile;
                cb.whiteKingPosition[1] = rank;
            }else if(cb.board[rank][locFile] == Constants.BLACK_KING){
                cb.blackKingPosition[0] = destFile;
                cb.blackKingPosition[1] = rank;
            }

            if(rank == 0 || rank == 7){
                switch(cb.board[rank][locFile]){
                    case Constants.WHITE_ROOK:
                        if(locFile==0 && cb.fenParts[9].contains(String.valueOf(Constants.WHITE_QUEEN))){
                            cb.fenParts[9] = cb.fenParts[9].replace(Character.toString(Constants.WHITE_QUEEN),"");
                        }else if(locFile==7 && cb.fenParts[9].contains(String.valueOf(Constants.WHITE_KING))){
                            cb.fenParts[9] = cb.fenParts[9].replace(Character.toString(Constants.WHITE_KING), "");
                        }
                        break;
                    case Constants.WHITE_KING:
                        if(cb.fenParts[9].contains(String.valueOf(Constants.WHITE_QUEEN)) || cb.fenParts[9].contains(String.valueOf(Constants.WHITE_KING))) {
                            cb.fenParts[9] = cb.fenParts[9].replace(Character.toString(Constants.WHITE_KING), "").replace(Character.toString(Constants.WHITE_QUEEN), "");
                        }
                        break;
                    case Constants.BLACK_ROOK:
                        if(locFile==0 && cb.fenParts[9].contains(String.valueOf(Constants.BLACK_QUEEN))){
                            cb.fenParts[9] = cb.fenParts[9].replace(Character.toString(Constants.BLACK_QUEEN),"");
                        }else if(locFile==7 && cb.fenParts[9].contains(String.valueOf(Constants.BLACK_KING))) {
                            cb.fenParts[9] = cb.fenParts[9].replace(Character.toString(Constants.BLACK_KING), "");
                        }
                        break;
                    case Constants.BLACK_KING:
                        if(cb.fenParts[9].contains(String.valueOf(Constants.BLACK_QUEEN)) || cb.fenParts[9].contains(String.valueOf(Constants.BLACK_KING))) {
                            cb.fenParts[9] = cb.fenParts[9].replace(Character.toString(Constants.BLACK_KING), "").replace(Character.toString(Constants.BLACK_QUEEN), "");
                        }
                        break;
                }
                switch(cb.board[rank][destFile]){
                    case Constants.WHITE_ROOK:
                        if(destFile==0 && cb.fenParts[9].contains(String.valueOf(Constants.WHITE_QUEEN))){
                            cb.fenParts[9] = cb.fenParts[9].replace(Character.toString(Constants.WHITE_QUEEN),"");
                        }else if(destFile==7 && cb.fenParts[9].contains(String.valueOf(Constants.WHITE_KING))){
                            cb.fenParts[9] = cb.fenParts[9].replace(Character.toString(Constants.WHITE_KING), "");
                        }
                        break;
                    case Constants.BLACK_ROOK:
                        if(destFile==0 && cb.fenParts[9].contains(String.valueOf(Constants.BLACK_QUEEN))){
                            cb.fenParts[9] = cb.fenParts[9].replace(Character.toString(Constants.BLACK_QUEEN),"");
                        }else if(destFile==7 && cb.fenParts[9].contains(String.valueOf(Constants.BLACK_KING))) {
                            cb.fenParts[9] = cb.fenParts[9].replace(Character.toString(Constants.BLACK_KING), "");
                        }
                        break;
                }
            }

            cb.board[rank][destFile] = cb.board[rank][locFile];
            cb.board[rank][locFile] = Constants.EMPTY_SQUARE;
            cb.pieceLocations.add(destFile + rank * 8);
            cb.pieceLocations.remove((Object)(locFile + rank * 8));

            cb.fenParts[rank] = FenUtils.getRank(cb.board[rank]);
            cb.fenParts[10] = "-";
            cb.turn  = cb.turn  == Constants.WHITE?Constants.BLACK:Constants.WHITE;
            cb.fenParts[8] = Character.toString(cb.turn);

        }else if(move.charAt(0) == move.charAt(2)){
            int file = move.charAt(0);
            int locRank = move.charAt(1);
            int destRank = move.charAt(3);
            if(cb.board[locRank][file] == Constants.WHITE_KING){
                cb.whiteKingPosition[0] = file;
                cb.whiteKingPosition[1] = destRank;
            }else if(cb.board[locRank][file] == Constants.BLACK_KING){
                cb.blackKingPosition[0] = file;
                cb.blackKingPosition[1] = destRank;
            }

            if(locRank == 0 || locRank == 7){
                switch(cb.board[locRank][file]){
                    case Constants.BLACK_ROOK:{
                        if(file==0 && cb.fenParts[9].contains(String.valueOf(Constants.BLACK_QUEEN))){
                            cb.fenParts[9] = cb.fenParts[9].replace(Character.toString(Constants.BLACK_QUEEN),"");
                        }else if(file==7 && cb.fenParts[9].contains(String.valueOf(Constants.BLACK_KING))) {
                            cb.fenParts[9] = cb.fenParts[9].replace(Character.toString(Constants.BLACK_KING), "");
                        }
                        break;
                    }
                    case Constants.WHITE_ROOK:{
                        if(file==0 && cb.fenParts[9].contains(String.valueOf(Constants.WHITE_QUEEN))){
                            cb.fenParts[9] = cb.fenParts[9].replace(Character.toString(Constants.WHITE_QUEEN),"");
                        }else if(file==7 && cb.fenParts[9].contains(String.valueOf(Constants.WHITE_KING))) {
                            cb.fenParts[9] = cb.fenParts[9].replace(Character.toString(Constants.WHITE_KING), "");
                        }
                        break;
                    }
                    case Constants.BLACK_KING:{
                        if(cb.fenParts[9].contains(String.valueOf(Constants.BLACK_QUEEN)) || cb.fenParts[9].contains(String.valueOf(Constants.BLACK_KING))) {
                            cb.fenParts[9] = cb.fenParts[9].replace(Character.toString(Constants.BLACK_KING), "").replace(Character.toString(Constants.BLACK_QUEEN), "");
                        }
                        break;
                    }
                    case Constants.WHITE_KING:{
                        if(cb.fenParts[9].contains(String.valueOf(Constants.WHITE_QUEEN)) || cb.fenParts[9].contains(String.valueOf(Constants.WHITE_KING))) {
                            cb.fenParts[9] = cb.fenParts[9].replace(Character.toString(Constants.WHITE_KING), "").replace(Character.toString(Constants.WHITE_QUEEN), "");
                        }
                        break;
                    }
                }
            }
            if(destRank == 0 || destRank == 7){
                switch(cb.board[destRank][file]){
                    case Constants.WHITE_ROOK:
                        if(file==0 && cb.fenParts[9].contains(String.valueOf(Constants.WHITE_QUEEN))){
                            cb.fenParts[9] = cb.fenParts[9].replace(Character.toString(Constants.WHITE_QUEEN),"");
                        }else if(file==7 && cb.fenParts[9].contains(String.valueOf(Constants.WHITE_KING))){
                            cb.fenParts[9] = cb.fenParts[9].replace(Character.toString(Constants.WHITE_KING), "");
                        }
                        break;
                    case Constants.BLACK_ROOK:
                        if(file==0 && cb.fenParts[9].contains(String.valueOf(Constants.BLACK_QUEEN))){
                            cb.fenParts[9] = cb.fenParts[9].replace(Character.toString(Constants.BLACK_QUEEN),"");
                        }else if(file==7 && cb.fenParts[9].contains(String.valueOf(Constants.BLACK_KING))) {
                            cb.fenParts[9] = cb.fenParts[9].replace(Character.toString(Constants.BLACK_KING), "");
                        }
                        break;
                }
            }

            //double pawn push -> creates en-passant square
            if(locRank == 1 && cb.board[locRank][file] == Constants.BLACK_PAWN && destRank == 3){
                cb.fenParts[10] = Constants.FILES.charAt(file)+"6";
            }else if(locRank == 6 && cb.board[locRank][file] == Constants.WHITE_PAWN && destRank == 4){
                cb.fenParts[10] = Constants.FILES.charAt(file)+"3";
            }else{
                cb.fenParts[10] = "-";
            }

            cb.board[destRank][file] = cb.board[locRank][file];
            cb.board[locRank][file] = Constants.EMPTY_SQUARE;
            cb.pieceLocations.add(file + destRank * 8);
            cb.pieceLocations.remove((Object)(file+locRank*8));
            cb.fenParts[locRank] = FenUtils.getRank(cb.board[locRank]);
            cb.fenParts[destRank] = FenUtils.getRank(cb.board[destRank]);
            cb.turn  = cb.turn  == Constants.WHITE?Constants.BLACK:Constants.WHITE;
            cb.fenParts[8] = Character.toString(cb.turn);

        }else{
            int locFile = move.charAt(0);
            int destFile = move.charAt(2);
            int locRank = move.charAt(1);
            int destRank = move.charAt(3);
            if(cb.board[locRank][locFile] == Constants.WHITE_KING){
                cb.whiteKingPosition[0] = destFile;
                cb.whiteKingPosition[1] = destRank;
            }else if(cb.board[locRank][locFile] == Constants.BLACK_KING){
                cb.blackKingPosition[0] = destFile;
                cb.blackKingPosition[1] = destRank;
            }

            if(locRank == 0 || locRank == 7){
                switch(cb.board[locRank][locFile]){
                    case Constants.BLACK_KING:{
                        if(cb.fenParts[9].contains(String.valueOf(Constants.BLACK_QUEEN)) || cb.fenParts[9].contains(String.valueOf(Constants.BLACK_KING))) {
                            cb.fenParts[9] = cb.fenParts[9].replace(Character.toString(Constants.BLACK_KING), "").replace(Character.toString(Constants.BLACK_QUEEN), "");
                        }
                        break;
                    }
                    case Constants.WHITE_KING:{
                        if(cb.fenParts[9].contains(String.valueOf(Constants.WHITE_QUEEN)) || cb.fenParts[9].contains(String.valueOf(Constants.WHITE_KING))) {
                            cb.fenParts[9] = cb.fenParts[9].replace(Character.toString(Constants.WHITE_KING), "").replace(Character.toString(Constants.WHITE_QUEEN), "");
                        }
                        break;
                    }
                }
            }
            if(destRank == 0 || destRank == 7){
                switch(cb.board[destRank][destFile]){
                    case Constants.WHITE_ROOK:
                        if(destFile==0 && cb.fenParts[9].contains(String.valueOf(Constants.WHITE_QUEEN))){
                            cb.fenParts[9] = cb.fenParts[9].replace(Character.toString(Constants.WHITE_QUEEN),"");
                        }else if(destFile==7 && cb.fenParts[9].contains(String.valueOf(Constants.WHITE_KING))){
                            cb.fenParts[9] = cb.fenParts[9].replace(Character.toString(Constants.WHITE_KING), "");
                        }
                        break;
                    case Constants.BLACK_ROOK:
                        if(destFile==0 && cb.fenParts[9].contains(String.valueOf(Constants.BLACK_QUEEN))){
                            cb.fenParts[9] = cb.fenParts[9].replace(Character.toString(Constants.BLACK_QUEEN),"");
                        }else if(destFile==7 && cb.fenParts[9].contains(String.valueOf(Constants.BLACK_KING))) {
                            cb.fenParts[9] = cb.fenParts[9].replace(Character.toString(Constants.BLACK_KING), "");
                        }
                        break;
                }
            }

            //en-passant
            if(!cb.fenParts[10].equals("-") && destFile == Constants.FILES.indexOf(cb.fenParts[10].charAt(0))){
                if(locRank == 3 && cb.board[locRank][locFile] == Constants.WHITE_PAWN){
                    cb.board[locRank][destFile] = Constants.EMPTY_SQUARE;
                    cb.pieceLocations.remove((Object)(destFile+locRank*8));
                }else if(locRank == 4 && cb.board[locRank][locFile] == Constants.BLACK_PAWN){
                    cb.board[locRank][destFile] = Constants.EMPTY_SQUARE;
                    cb.pieceLocations.remove((Object)(destFile+locRank*8));
                }
            }


            cb.board[destRank][destFile] = cb.board[locRank][locFile];
            cb.board[locRank][locFile] = Constants.EMPTY_SQUARE;
            cb.pieceLocations.add(destFile + destRank * 8);
            cb.pieceLocations.remove((Object)(locFile+locRank*8));
            cb.fenParts[locRank] = FenUtils.getRank(cb.board[locRank]);
            cb.fenParts[destRank] = FenUtils.getRank(cb.board[destRank]);
            cb.turn  = cb.turn  == Constants.WHITE?Constants.BLACK:Constants.WHITE;
            cb.fenParts[10] = "-";
            cb.fenParts[8] = Character.toString(cb.turn);

        }
    }
}

