package piece;

import board.ChessBoard;
import util.Constants;
import util.Util;

import java.util.ArrayList;

public class Piece {

    ChessBoard cb;
    ArrayList<String> moves;

    public Piece(ChessBoard cb){
        this.cb = cb;
        moves = new ArrayList<>();
    }


    public ArrayList<String> generateMove(int file,int rank,boolean pseudoLegal){
        switch(Character.toUpperCase(cb.board[rank][file])){
            case Constants.WHITE_KING:
                return king(file,rank,pseudoLegal);
            case Constants.WHITE_PAWN:
                return pawn(file,rank,pseudoLegal);
            case Constants.WHITE_ROOK:
                return rook(file,rank,pseudoLegal);
            case Constants.WHITE_BISHOP:
                return bishop(file,rank,pseudoLegal);
            case Constants.WHITE_KNIGHT:
                return knight(file,rank,pseudoLegal);
            case Constants.WHITE_QUEEN:
                return queen(file,rank,pseudoLegal);
            default:
                return null;
        }
    }

    public ArrayList<String> pawn(int file,int rank,boolean attacking){
        moves.clear();
        if(!attacking && cb.pinnedPieces.containsKey(file + rank * 8)){
            return moves; // a pinned knight cannot move
        }
        int f,r;
        // generating diagonal moves
        int startIndex = Util.isUpperCase(cb.board[rank][file])?6:4;
        int endIndex = Util.isUpperCase(cb.board[rank][file])?Constants.HORIZONTAL_AND_DIAGONAL_DIRECTIONS.length-1:5;

        if(attacking){
            for(int i=startIndex;i<=endIndex;i++){
                f = file + Constants.HORIZONTAL_AND_DIAGONAL_DIRECTIONS[i][0];
                r = rank + Constants.HORIZONTAL_AND_DIAGONAL_DIRECTIONS[i][1];
                if(Util.isValid(f,r)){
                    if(cb.board[rank][file] == Constants.EMPTY_SQUARE){
                        moves.add(Util.cvtMove(file,rank,f,r));
                    }
                }
            }
            return moves;
        }
        for(int i=startIndex;i<=endIndex;i++){
            f = file + Constants.HORIZONTAL_AND_DIAGONAL_DIRECTIONS[i][0];
            r = rank + Constants.HORIZONTAL_AND_DIAGONAL_DIRECTIONS[i][1];
            if(Util.isValid(f,r)){
                if(cb.board[rank][file] != Constants.EMPTY_SQUARE && !Util.isAlly(cb.board[rank][file],cb.board[r][f])){
                    moves.add(Util.cvtMove(file,rank,f,r));
                }
            }
        }
        // generating pushes
        startIndex = Util.isUpperCase(cb.board[rank][file])?1:0;
        endIndex = (Util.isUpperCase(cb.board[rank][file]) && rank == 6) || (!Util.isUpperCase(cb.board[rank][file]) && rank == 1)?2:1;
        for(int i=0;i<endIndex;i++){
            f = file + Constants.HORIZONTAL_AND_DIAGONAL_DIRECTIONS[startIndex][0];
            r = rank + Constants.HORIZONTAL_AND_DIAGONAL_DIRECTIONS[startIndex][1];
            if(cb.board[r][f] != Constants.EMPTY_SQUARE){
                break;
            }else{
                moves.add(Util.cvtMove(file,rank,f,r));
            }
        }


        if(cb.fenParts[10].equals("-")){
            // do nothing
        }else{
            if(Math.abs(file-Constants.FILES.indexOf(cb.fenParts[10].charAt(0))) == 1){
                if(Util.isUpperCase(cb.board[rank][file]) && rank == 3){
                    moves.add(Util.cvtMove(file,rank,Constants.FILES.indexOf(cb.fenParts[10].charAt(0)),2));
                }else if(!Util.isUpperCase(cb.board[rank][file]) && rank == 4){
                    moves.add(Util.cvtMove(file,rank,Constants.FILES.indexOf(cb.fenParts[10].charAt(0)),5));
                }

            }
        }

        return moves;
    }

    public ArrayList<String> king(int file,int rank,boolean pseudoLegal){
        return null;
    }

    public ArrayList<String> queen(int file,int rank,boolean pseudoLegal){
        return null;
    }

    public ArrayList<String> rook(int file,int rank,boolean pseudoLegal){
        return null;
    }

    public ArrayList<String> knight(int file,int rank,boolean pseudoLegal){
        moves.clear();
        if(!pseudoLegal && cb.pinnedPieces.containsKey(file + rank * 8)){
            return moves; // a pinned knight cannot move
        }
        int df,dr;
        for(int[] direction:Constants.KNIGHT_DIRECTION){
            df = file + direction[0];
            dr = rank + direction[1];
            if(Util.isValid(df,dr) && !Util.isAlly(cb.board[dr][df],cb.board[rank][file])){
                moves.add(Util.cvtMove(file,rank,df,dr));
            }
        }

        return moves;
    }

    public ArrayList<String> bishop(int file,int rank,boolean pseudoLegal){
        moves.clear();
        int pinnedIndex = file + rank * 8;
        if(!pseudoLegal && cb.pinnedPieces.containsKey(pinnedIndex)){
            int[] pinDirection = Constants.HORIZONTAL_AND_DIAGONAL_DIRECTIONS[cb.pinnedPieces.get(pinnedIndex)];
            if(pinDirection[0] == 0 || pinDirection[1] == 0){
                return moves; // a bishop pinned by rook or horizontally by queen cannot move
            }else{
                // can move along pinned squares
                boolean foundKing=false,foundEnemyPiece=false;
                int df=file,dr=rank;
                while(!foundEnemyPiece || !foundKing){
                    if(!foundEnemyPiece){
                        df += pinDirection[0];
                        dr += pinDirection[1];
                        if(cb.board[dr][df] == Constants.EMPTY_SQUARE) {
                            moves.add(Util.cvtMove(file, rank, df, dr));
                        }else{
                            foundEnemyPiece = true;
                            df = file;
                            dr = rank;
                        }
                    }else {
                        df -= pinDirection[0];
                        dr -= pinDirection[1];
                        if(Character.toUpperCase(cb.board[dr][df]) != Constants.WHITE) {
                            moves.add(Util.cvtMove(file, rank, df, dr));
                        }else{
                            foundKing = true;
                        }
                    }
                }
                return moves;
            }
        }

        // TODO generate normal moves

        return null;
    }


}
