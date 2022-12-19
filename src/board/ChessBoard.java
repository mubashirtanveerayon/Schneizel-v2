package board;

import util.*;

import java.util.*;

public class ChessBoard {

    public char[][] board;
    public char turn;
    public String[] fenParts;
    public int[] whiteKingPosition,blackKingPosition;
    public HashMap<Integer,Integer> pinnedPieces; // 1st element holds the 1D index of the pinned piece, 2nd element holds the index of util.Constants.HORIZONTAL_AND_DIAGONAL_DIRECTIONS it is being pinned from
    public HashMap<Integer,Integer> checkers; // 1D index of the opponent piece(the piece attacking the king)
                                                // index of direction to checker

    public GameState gs;

    public Set<Integer> pieceLocations;// holds 1D index of piece locations in a 2D array
                                        // used a set to avoid duplicate elements

    public ChessBoard(){
        fenParts = FenUtils.split(Constants.STARTING_FEN);
        gs =  GameState.NORMAL;
        initialize();
    }

    public ChessBoard(String fen){
        fenParts = FenUtils.split(fen);
        initialize();
    }

    public ChessBoard(String fen,boolean checkFen) {
        if(checkFen) {
            try {
                FenUtils.isValid(fen);
            } catch (Exception e) {
                return;
            }
        }
        fenParts = FenUtils.split(fen);
        initialize();
    }

    private void initialize(){
        //initialization of variables
        board = FenUtils.parse(fenParts);
        turn = fenParts[8].charAt(0);
        pinnedPieces = new HashMap<>();
        pieceLocations = new HashSet<>();
        whiteKingPosition = new int[2];
        blackKingPosition = new int[2];
        checkers = new HashMap<>();
        for(int i=0;i<8;i++){
            for(int j=0;j<8;j++){
                if(board[i][j] == Constants.WHITE_KING){
                    whiteKingPosition[0] = j;
                    whiteKingPosition[1] = i;
                }else if(board[i][j] == Constants.BLACK_KING){
                    blackKingPosition[0] = j;
                    blackKingPosition[1] = i;
                }
                if(board[i][j] != Constants.EMPTY_SQUARE){
                    pieceLocations.add(j+i*8);
                }
            }
        }

        checkBoard();

    }

    public void resetStats(){
        pinnedPieces.clear();
        checkers.clear();
        gs = GameState.NORMAL;
    }

    public void _checksAndPinnedPieces(){
        resetStats();
        int[] kingPosition = kingPosition();
        int file,rank,pinnedPieceIndex=0;
        boolean foundAlly,foundOpponentPiece;
        for(int i = 0; i<Constants.ALL_DIRECTIONS.length; i++){
            file = kingPosition[0]+Constants.ALL_DIRECTIONS[i][0];
            rank = kingPosition[1]+Constants.ALL_DIRECTIONS[i][1];
            foundOpponentPiece = false;
            foundAlly = false;
            while(Util.isValid(file,rank)){
                if(board[rank][file] == Constants.EMPTY_SQUARE){
                    file += Constants.ALL_DIRECTIONS[i][0];
                    rank += Constants.ALL_DIRECTIONS[i][1];
                    continue;
                }
                if(!Util.isEnemyPiece(turn,board[rank][file])){
                    if(foundAlly){
                        break;
                    }else {
                        foundAlly = true;
                        pinnedPieceIndex = file + rank * 8;
                        pinnedPieces.put(pinnedPieceIndex,i);
                    }
                }


                else if(Character.toUpperCase(board[rank][file]) != Constants.WHITE_PAWN && Character.toUpperCase(board[rank][file]) != Constants.WHITE_KNIGHT){
                    foundOpponentPiece = true;
                    if (!foundAlly) {
                        gs = GameState.CHECK;
                        checkers.put(file + rank * 8,i);
                    }
                    break;
                }

                else if(Character.toUpperCase(board[rank][file]) != Constants.WHITE_KNIGHT){
                    // found opponent pawn
                    if(Math.abs(kingPosition[0] - file) == 1){
                        if(turn == Constants.WHITE){
                            if(kingPosition[1] > rank){
                                gs = GameState.CHECK;
                                checkers.put(file + rank * 8,i);
                            }
                        }else{
                            if(kingPosition[1] < rank){
                                gs = GameState.CHECK;
                                checkers.put(file + rank * 8,i);
                            }
                        }
                    }
                    break;
                }else{
                    break;
                }
                file += Constants.ALL_DIRECTIONS[i][0];
                rank += Constants.ALL_DIRECTIONS[i][1];
            }
            if(!foundOpponentPiece && foundAlly){
                pinnedPieces.remove(pinnedPieceIndex);
            }
        }

        for(int i =0;i<Constants.KNIGHT_DIRECTION.length;i++){
            file = kingPosition[0]+Constants.KNIGHT_DIRECTION[i][0];
            rank = kingPosition[1]+Constants.KNIGHT_DIRECTION[i][1];
            if(Util.isValid(file,rank)){
                switch(board[rank][file]){
                    case Constants.BLACK_KNIGHT:
                        if(turn == Constants.WHITE){
                            gs = GameState.CHECK;
                            checkers.put(file + rank * 8,i);
                        }
                    case Constants.WHITE_KNIGHT:
                        if(turn == Constants.BLACK){
                            gs = GameState.CHECK;
                            checkers.put(file + rank * 8,i);
                        }
                }

            }
        }

    }


    public void checkBoard(){
        resetStats();
        //temporary debug code exists
//        try{
//            FenUtils.isValid(FenUtils.cat(fenParts));
//        }catch(Exception e){
//            e.printStackTrace();
//            System.out.println(FenUtils.cat(fenParts));
//            System.exit(0);
//        }
        int[] kingPos = kingPosition();
        int file,rank ,pinnedPieceIndex=0;
        boolean foundAlly,foundEnemyPiece;

        for(int i=0;i<Constants.ALL_DIRECTIONS.length;i++){
            foundAlly = false;
            foundEnemyPiece = false;
            file = kingPos[0]+Constants.ALL_DIRECTIONS[i][0];
            rank = kingPos[1]+Constants.ALL_DIRECTIONS[i][1];
            while(!foundEnemyPiece&&Util.isValid(file, rank)){
                if(board[rank][file] != Constants.EMPTY_SQUARE){

                    if(Util.isEnemyPiece(turn,board[rank][file])){

                        foundEnemyPiece = true;
                        switch(Character.toUpperCase(board[rank][file])){
                            case Constants.WHITE_KING:
                            case Constants.WHITE_PAWN:
                            case Constants.WHITE_KNIGHT:
                                if(foundAlly){
                                    pinnedPieces.remove(pinnedPieceIndex);
                                }
                                break;
                            case Constants.WHITE_QUEEN:{
                                if(!foundAlly){
                                    gs = GameState.CHECK;
                                    checkers.put(file + rank * 8,i);
                                }
                                break;
                            }
                            case Constants.WHITE_ROOK:{
                                if(Constants.ALL_DIRECTIONS[i][0] == 0  || Constants.ALL_DIRECTIONS[i][1] == 0){
                                    if(!foundAlly){
                                        gs = GameState.CHECK;
                                        checkers.put(file + rank * 8,i);
                                    }
                                }else{
                                    if(foundAlly){
                                        pinnedPieces.remove(pinnedPieceIndex);
                                    }
                                }
                                break;
                            }
                            case Constants.WHITE_BISHOP:{
                                if(Constants.ALL_DIRECTIONS[i][0] != 0  && Constants.ALL_DIRECTIONS[i][1] != 0){
                                    if(!foundAlly){
                                        gs = GameState.CHECK;
                                        checkers.put(file + rank * 8,i);
                                    }
                                }else{
                                    if(foundAlly){
                                        pinnedPieces.remove(pinnedPieceIndex);
                                    }
                                }
                                break;
                            }


                        }
                    }else{
                        if(foundAlly){
                            break;
                        }else{
                            foundAlly = true;
                            pinnedPieceIndex = file + rank * 8;
                            pinnedPieces.put(pinnedPieceIndex,i);
                        }
                    }
                }
                file+=Constants.ALL_DIRECTIONS[i][0];
                rank+=Constants.ALL_DIRECTIONS[i][1];

            }
            if(foundAlly && !foundEnemyPiece){
                pinnedPieces.remove(pinnedPieceIndex);
            }
        }

        for(int i =0;i<Constants.KNIGHT_DIRECTION.length;i++){
            file = kingPos[0]+Constants.KNIGHT_DIRECTION[i][0];
            rank = kingPos[1]+Constants.KNIGHT_DIRECTION[i][1];
            if(Util.isValid(file,rank)){
                switch(board[rank][file]){
                    case Constants.BLACK_KNIGHT:
                        if(turn == Constants.WHITE){
                            gs = GameState.CHECK;
                            checkers.put(file + rank * 8,i);
                        }
                        break;
                    case Constants.WHITE_KNIGHT:
                        if(turn == Constants.BLACK){
                            gs = GameState.CHECK;
                            checkers.put(file + rank * 8,i);
                        }
                        break;
                }

            }
        }


    }




    public boolean canSlide(int fromF, int fromR, int toF, int toR){
        int[] direction = Util.getDirection(fromF,fromR,toF,toR);
        if(direction[0] != 0 && direction[1] != 0 && Math.abs(fromF-toF) != Math.abs(fromR-toR)){
            return false;
        }
        int f=fromF+direction[0],r = fromR+direction[1];
        while(Util.isValid(f,r)){
            if(f == toF && r == toR){
                return true;
            }else if(board[r][f] == Constants.EMPTY_SQUARE) {
                f += direction[0];
                r += direction[1];
            }else{
                break;
            }
        }
        return false;
    }


    public boolean squareUnderAttack(final int file,final int rank){
        for(int[] dir:Constants.ALL_DIRECTIONS){
            int f = file,r = rank;
            for(int i=0;Util.isValid(f+=dir[0],r+=dir[1]);i++){
                if(board[r][f] == Constants.EMPTY_SQUARE){
                    continue;
                }else if(Util.isEnemyPiece(turn,board[r][f])){
                    switch(Util.toUpper(board[r][f])){
                        case Constants.WHITE_QUEEN:{
                            return true;
                        }
                        case Constants.WHITE_ROOK:{
                            if(dir[0] == 0 || dir[1] == 0){
                                return true;
                            }
                            break;
                        }
                        case Constants.WHITE_BISHOP:{
                            if(dir[0] == 0 || dir[1] == 0){
                                break;
                            }else{
                                return true;
                            }
                        }
                        case Constants.WHITE_KING:{
                            if(i==0){
                                return true;
                            }
                            break;
                        }
                        case Constants.WHITE_PAWN:{
                            if(i==0 && dir[0] != 0 && dir[1] != 0){
                                if((turn == Constants.BLACK && rank<r) || (turn == Constants.WHITE && rank>r)){
                                    return true;
                                }
                            }
                            break;
                        }
                    }
                }
                break;
            }

        }

        for(int[] dir:Constants.KNIGHT_DIRECTION){
            int f = file+dir[0],r = rank+dir[1];
            if(Util.isValid(f,r) && board[r][f] != Constants.EMPTY_SQUARE && Util.isEnemyPiece(turn,board[r][f])){
                if(Character.toUpperCase(board[r][f]) == Constants.WHITE_KNIGHT){
                    return true;
                }
            }
        }

        return false;
    }


public boolean _isSquareSafe(int file,int rank){
        for(int[] dir:Constants.ALL_DIRECTIONS){
            int f=file,r=rank;
            for(int i=0;Util.isValid(f+=dir[0],r+=dir[1]);i++){
                if(board[r][f] != Constants.EMPTY_SQUARE){
                    if(Util.isEnemyPiece(turn,board[r][f])){
                        char piece = Character.toUpperCase(board[r][f]);
                        switch(piece){
                            case Constants.WHITE_PAWN:{
                                if(i==0){
                                    if(turn == Constants.WHITE){
                                        if(Math.abs(file-f) == 1 && (rank-r) == 1){
                                            return false;
                                        }
                                    }else{
                                        if(Math.abs(file-f) == 1 && (rank-r) == -1){
                                            return false;
                                        }
                                    }
                                }
                                break;
                            }
                            case Constants.WHITE_KING:{
                                if(i==0) {
                                    return false;
                                }
                                break;
                            }
                            case Constants.WHITE_QUEEN:{
                                return false;
                            }
                            case Constants.WHITE_ROOK:{
                                if(dir[0] == 0 || dir[1] == 0){
                                    return false;
                                }
                                break;
                            }
                            case Constants.WHITE_BISHOP:{
                                if(!(dir[0] == 0 || dir[1] == 0)){
                                    return false;
                                }
                                break;
                            }
                        }
                    }

                    break;
                }
            }
        }
        for(int[] dir:Constants.KNIGHT_DIRECTION){
            int f = file+dir[0],r = rank+dir[1];
            if(Util.isValid(f,r) && board[r][f] != Constants.EMPTY_SQUARE && Util.isEnemyPiece(turn,board[r][f])){
                if(Character.toUpperCase(board[r][f]) == Constants.WHITE_KNIGHT){
                    return false;
                }
            }
        }
        return true;
    }

    public int[] kingPosition(){
        return turn == Constants.WHITE?whiteKingPosition:blackKingPosition;
    }

}
