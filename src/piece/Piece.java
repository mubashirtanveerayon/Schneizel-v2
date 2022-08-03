package piece;

import board.ChessBoard;
import util.Constants;
import util.GameState;
import util.Util;

import java.util.ArrayList;

public class Piece {

    ChessBoard cb;
    ArrayList<String> moves;

    public Piece(ChessBoard cb){
        this.cb = cb;
        moves = new ArrayList<>();
    }


    public ArrayList<String> generateMove(int file,int rank){
        moves.clear();
        switch(Character.toUpperCase(cb.board[rank][file])){
            case Constants.WHITE_KING:
                return king(file,rank);
            case Constants.WHITE_PAWN:
                return pawn(file,rank);
            case Constants.WHITE_ROOK:
                return rook(file,rank);
            case Constants.WHITE_BISHOP:
                return bishop(file,rank);
            case Constants.WHITE_KNIGHT:
                return knight(file,rank);
            case Constants.WHITE_QUEEN:
                return queen(file,rank);
            default:
                return null;
        }
    }

    public ArrayList<String> king(final int file,final int rank){
        return null;
    }

    public ArrayList<String> pawn(int file,int rank){
        int pinnedIndex = file + rank * 8;
        boolean pinned = cb.pinnedPieces.containsKey(pinnedIndex);
        if(pinned){
            if(cb.gs == GameState.CHECK){
                return moves; // pinned pieces cannot resolve check
            }
            int[] pinDirection = Constants.HORIZONTAL_AND_DIAGONAL_DIRECTIONS[cb.pinnedPieces.get(pinnedIndex)];
            if(pinDirection[0] != 0 ){
                if(pinDirection[1] != 0){
                    // only possible move is to capture the pinner
                    if((Util.isUpperCase(cb.board[rank][file]) && pinDirection[1] == -1) || (!Util.isUpperCase(cb.board[rank][file]) && pinDirection[1] == 1)){
                        if(cb.board[rank + pinDirection[1]][file + pinDirection[0]] != Constants.EMPTY_SQUARE){
                            moves.add(Util.cvtMove(file,rank,file + pinDirection[0],rank + pinDirection[1]));
                        }
                    }
                    return moves;
                }
            }else{
                // generate pushes, that is done below
            }
        }else if(cb.gs == GameState.CHECK){
            if(cb.checkers.size()>1){
                return moves; // a two-way check cannot be resolved without the king moving to a safe square
            }
            int checkerIndex = cb.checkers.get(0);
            int checkerFile = checkerIndex % 8;
            int checkerRank = checkerIndex / 8;
            int[] kingPosition = cb.turn == Constants.WHITE?cb.whiteKingPosition:cb.blackKingPosition;
            if(Character.toUpperCase(cb.board[checkerRank][checkerFile]) != Constants.WHITE_KNIGHT) {
                int[] checkDirection = Util.getDirection(kingPosition[0], kingPosition[1], checkerFile, checkerRank);
                if (checkDirection[0] == 0) {
                    if (Math.abs(file - checkerFile) == 1) {
                        // only possible move is to capture the checker why? https://lichess.org/editor/4k3/8/3q4/4P3/8/3K4/8/8_w_-_-_0_1?color=white
                        if (Math.abs(rank - checkerRank) == 1 && ((Util.isUpperCase(cb.board[rank][file]) && checkDirection[1] == -1) || (!Util.isUpperCase(cb.board[rank][file]) && checkDirection[1] == 1))) {
                            moves.add(Util.cvtMove(file, rank, file + checkDirection[0], rank + checkDirection[1]));
                        }
                    }
                } else if (checkDirection[1] == 0) {
                    boolean hasPotential = (Util.isUpperCase(cb.board[rank][file]) && kingPosition[1] < rank) || (!Util.isUpperCase(cb.board[rank][file]) && kingPosition[1] > rank);
                    if (file == checkerFile) {
                        // cannot resolve the check, why? https://lichess.org/editor/7k/8/2K3q1/4P3/8/8/8/8_w_-_-_0_1?color=white
                    }else if(Math.abs(rank - checkerRank) == 1){
                        // possible move is to block the check how? https://lichess.org/editor/7k/8/1K2q3/3P4/8/8/8/8_w_-_-_0_1?color=white
                        int destRank = cb.turn == Constants.WHITE?rank-1:rank+1;
                        if(hasPotential && file>kingPosition[0] && file<checkerFile && destRank<8 && destRank>=0 && cb.board[destRank][file] == Constants.EMPTY_SQUARE){
                            moves.add(Util.cvtMove(file, rank, file, destRank));
                        }
                        if(Math.abs(file-checkerFile) == 1 && hasPotential ) {
                            // possible move is to capture the checker how? https://lichess.org/editor/7k/8/1K2q3/3P4/8/8/8/8_w_-_-_0_1?color=white
                            moves.add(Util.cvtMove(file, rank, checkerFile, checkerRank));
                        }
                    }
                }
            }else{
                // no way to block a check from knight, only possible move is to capture
                if(Math.abs(checkerFile-file) == 1 && Math.abs(checkerRank-rank) == 1){
                    if((cb.turn == Constants.WHITE && checkerRank<rank) || (cb.turn == Constants.BLACK && checkerRank>rank)){
                        moves.add(Util.cvtMove(file,rank,checkerFile,checkerRank));
                    }
                }
            }
            return moves;
        }

        // generating pushes
        int f,r;
        int startIndex = Util.isUpperCase(cb.board[rank][file])?1:0;
        int endIndex = (Util.isUpperCase(cb.board[rank][file]) && rank == 6) || (!Util.isUpperCase(cb.board[rank][file]) && rank == 1)?2:1;
        for(int i=0;i<endIndex;i++){
            f = file + Constants.HORIZONTAL_AND_DIAGONAL_DIRECTIONS[startIndex][0];
            r = rank + Constants.HORIZONTAL_AND_DIAGONAL_DIRECTIONS[startIndex][1];
            if(cb.board[r][f] != Constants.EMPTY_SQUARE){
                break;
            }else{
                moves.add(Util.cvtMove(file,rank,f,r));
            }
        }

        if(pinned){
            return moves; // a pawn pinned vertically cannot move diagonally
        }

        // generating diagonal moves
        startIndex = Util.isUpperCase(cb.board[rank][file])?6:4;
        endIndex = Util.isUpperCase(cb.board[rank][file])?Constants.HORIZONTAL_AND_DIAGONAL_DIRECTIONS.length-1:5;

        for(int i=startIndex;i<=endIndex;i++){
            f = file + Constants.HORIZONTAL_AND_DIAGONAL_DIRECTIONS[i][0];
            r = rank + Constants.HORIZONTAL_AND_DIAGONAL_DIRECTIONS[i][1];
            if(Util.isValid(f,r)){
                if(cb.board[rank][file] != Constants.EMPTY_SQUARE && !Util.isAlly(cb.board[rank][file],cb.board[r][f])){
                    moves.add(Util.cvtMove(file,rank,f,r));
                }
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

    public ArrayList<String> queen(final int file,final int rank){
        int pinnedIndex = file + rank * 8;
        if(cb.pinnedPieces.containsKey(pinnedIndex)){
            int[] pinDirection = Constants.HORIZONTAL_AND_DIAGONAL_DIRECTIONS[cb.pinnedPieces.get(pinnedIndex)];
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
                        moves.add(Util.cvtMove(file, rank, df, dr));
                        df = file;
                        dr = rank;
                    }
                }else {
                    df -= pinDirection[0];
                    dr -= pinDirection[1];
                    if(cb.board[dr][df] == Constants.EMPTY_SQUARE) {
                        moves.add(Util.cvtMove(file, rank, df, dr));
                    }else{
                        foundKing = true;
                    }
                }
            }
            return moves;
        }

        // TODO generate normal moves
        int df,dr;
        for(int[] direction:Constants.HORIZONTAL_AND_DIAGONAL_DIRECTIONS){
            df = file + direction[0];
            dr = file + direction[1];
            while(Util.isValid(df,dr)){
                if(cb.board[dr][df] == Constants.EMPTY_SQUARE){
                    moves.add(Util.cvtMove(file,rank,df,dr));
                }else{
                    if(Util.isAlly(cb.board[rank][file],cb.board[dr][df])){

                    }else{
                        moves.add(Util.cvtMove(file,rank,df,dr));
                    }
                    break;
                }
                df += direction[0];
                dr += direction[1];
            }
        }
        return moves;
    }

    public ArrayList<String> rook(final int file,final int rank){
        int pinnedIndex = file + rank * 8;
        if(cb.pinnedPieces.containsKey(pinnedIndex)){
            int[] pinDirection = Constants.HORIZONTAL_AND_DIAGONAL_DIRECTIONS[cb.pinnedPieces.get(pinnedIndex)];
            if(pinDirection[0] != 0 && pinDirection[1] != 0){
                // a rook pinned by bishop or diagonally by queen cannot move
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
                            moves.add(Util.cvtMove(file, rank, df, dr));
                            df = file;
                            dr = rank;
                        }
                    }else {
                        df -= pinDirection[0];
                        dr -= pinDirection[1];
                        if(cb.board[dr][df] == Constants.EMPTY_SQUARE) {
                            moves.add(Util.cvtMove(file, rank, df, dr));
                        }else{
                            foundKing = true;
                        }
                    }
                }
            }
            return moves;
        }

        // TODO generate normal moves
        int df,dr;
        for(int i=0;i<4;i++){
            df = file + Constants.HORIZONTAL_AND_DIAGONAL_DIRECTIONS[i][0];
            dr = file + Constants.HORIZONTAL_AND_DIAGONAL_DIRECTIONS[i][1];
            while(Util.isValid(df,dr)){
                if(cb.board[dr][df] == Constants.EMPTY_SQUARE){
                    moves.add(Util.cvtMove(file,rank,df,dr));
                }else{
                    if(Util.isAlly(cb.board[rank][file],cb.board[dr][df])){

                    }else{
                        moves.add(Util.cvtMove(file,rank,df,dr));
                    }
                    break;
                }
                df += Constants.HORIZONTAL_AND_DIAGONAL_DIRECTIONS[i][0];
                dr += Constants.HORIZONTAL_AND_DIAGONAL_DIRECTIONS[i][1];
            }
        }
        return moves;
    }

    public ArrayList<String> knight(final int file,final int rank){
        if(cb.pinnedPieces.containsKey(file + rank * 8)){
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

    public ArrayList<String> bishop(final int file,final int rank){
        int pinnedIndex = file + rank * 8;
        if(cb.pinnedPieces.containsKey(pinnedIndex)){
            int[] pinDirection = Constants.HORIZONTAL_AND_DIAGONAL_DIRECTIONS[cb.pinnedPieces.get(pinnedIndex)];
            if(pinDirection[0] == 0 || pinDirection[1] == 0){
                // a bishop pinned by rook or horizontally by queen cannot move
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
                            moves.add(Util.cvtMove(file, rank, df, dr));
                            df = file;
                            dr = rank;
                        }
                    }else {
                        df -= pinDirection[0];
                        dr -= pinDirection[1];
                        if(cb.board[dr][df] == Constants.EMPTY_SQUARE) {
                            moves.add(Util.cvtMove(file, rank, df, dr));
                        }else{
                            foundKing = true;
                        }
                    }
                }
            }
            return moves;
        }

        // TODO generate normal moves
        int df,dr;
        for(int i=4;i<Constants.HORIZONTAL_AND_DIAGONAL_DIRECTIONS.length;i++){
            df = file + Constants.HORIZONTAL_AND_DIAGONAL_DIRECTIONS[i][0];
            dr = file + Constants.HORIZONTAL_AND_DIAGONAL_DIRECTIONS[i][1];
            while(Util.isValid(df,dr)){
                if(cb.board[dr][df] == Constants.EMPTY_SQUARE){
                    moves.add(Util.cvtMove(file,rank,df,dr));
                }else{
                    if(Util.isAlly(cb.board[rank][file],cb.board[dr][df])){

                    }else{
                        moves.add(Util.cvtMove(file,rank,df,dr));
                    }
                    break;
                }
                df += Constants.HORIZONTAL_AND_DIAGONAL_DIRECTIONS[i][0];
                dr += Constants.HORIZONTAL_AND_DIAGONAL_DIRECTIONS[i][1];
            }
        }
        return moves;
    }


}
