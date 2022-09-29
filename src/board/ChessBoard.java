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

    public String prevCastle,prevEnpassant;
    public char captured;
    public ChessBoard(){
        fenParts = FenUtils.split(Constants.STARTING_FEN);
        gs =  GameState.NORMAL;
        initialize();
    }

    public ChessBoard(String fen) {
        if(!FenUtils.isValid(fen)){
            return;
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
        prevCastle = fenParts[9];
        prevEnpassant = fenParts[10];
        captured = ' ';
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

        checksAndPinnedPieces();

    }

    public void resetStats(){
        pinnedPieces.clear();
        checkers.clear();
        gs = GameState.NORMAL;
    }

    public void checksAndPinnedPieces(){
        resetStats();
        int[] kingPosition = turn == Constants.WHITE?whiteKingPosition:blackKingPosition;
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
                if((Util.isUpperCase(board[rank][file]) && turn == Constants.WHITE) || (!Util.isUpperCase(board[rank][file]) && turn == Constants.BLACK)){
                    if(foundAlly){
                        break;
                    }else {
                        foundAlly = true;
                        pinnedPieceIndex = file + rank * 8;
                        pinnedPieces.put(pinnedPieceIndex,i);
                    }
                }else if(Character.toUpperCase(board[rank][file]) != Constants.WHITE_PAWN && Character.toUpperCase(board[rank][file]) != Constants.WHITE_KNIGHT){
                    foundOpponentPiece = true;
                    if (!foundAlly) {
                        gs = GameState.CHECK;
                        checkers.put(file + rank * 8,i);
                    }
                    break;
                }else if(Character.toUpperCase(board[rank][file]) != Constants.WHITE_KNIGHT){
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

public boolean isSquareSafe(int file,int rank){
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

    public static void main(String[] args) {
        char[][] board = new char[][]{{' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                                      {' ', ' ', 'q', ' ', ' ', 'p', ' ', ' '},
                                      {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                                      {' ', ' ', ' ', ' ', 'n', ' ', ' ', ' '},
                                      {' ', ' ', ' ', ' ', ' ', ' ', 'N', ' '},
                                      {' ', ' ', 'k', ' ', 'b', ' ', ' ', ' '},
                                      {' ', 'P', ' ', ' ', ' ', ' ', ' ', ' '},
                                      {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '}};


        String fen = FenUtils.generate(board);


//        System.out.println(fen);
        ChessBoard cb = new ChessBoard();

        Scanner sc = new Scanner(System.in);
        Util.printBoard(cb.board,false);
        int startF = sc.nextInt(),startR = sc.nextInt();
        while(true){
            char prev = cb.board[startR][startF];
            cb.board[startR][startF] = 'O';
            Util.printBoard(cb.board,false);

            cb.board[startR][startF] = prev;
            System.out.println("square safe: "+cb.isSquareSafe(startF,startR));

            char in = sc.next().charAt(0);
            if(in == 'w'){
                startR-=1;
            }else if(in == 'a'){
                startF-=1;
            }else if(in == 's'){
                startR+=1;
            }else if(in == 'd'){
                startF+=1;
            }
        }
//
//        util.Util.printBoard(cb.board,false);
//        move.Move move = new move.Move(cb);
//        System.out.println(cb.checkPieces.size());
//        move.makeMove("d8e2");
//        util.Util.printBoard(cb.board,false);
//        System.out.println(util.FenUtils.toString(cb.fenParts));
//        System.out.println(cb.pieceLocations.size());
//        System.out.println(cb.checkPieces.get(0)%8);
//        System.out.println((int)cb.checkPieces.get(0)/8);
//        System.out.println(cb.board[(int)cb.checkPieces.get(0)/8][cb.checkPieces.get(0)%8]);
//        System.out.println(cb.fen);
//        System.out.println(util.FenUtils.isFenValid(cb.fen));
//        cb = new board.ChessBoard("7N/1b4N1/5R1k/6b1/KBp4p/5q2/6Q1/7n b - - 0 1");
//        cb.updateFen("e2d2");
//        util.Util.printBoard(cb.board,false);
//        cb = new board.ChessBoard(cb.fen);
//        util.Util.printBoard(cb.board,false);
//        System.out.println(cb.fen);

//        String tf = "6Q1";
//        char[] file = util.FenUtils.parseFile(tf);
//        String rf = util.FenUtils.getFileFen(file);
//        System.out.println(rf);

//        String tf = "7N/1b3RN1/7k/6b1/KBp4p/5q2/6Q1/7n w - - 0 1";
//        String move = "f7f6";
//        String rf = util.FenUtils.updateFen(tf,move);
//        System.out.println("7N/1b4N1/5R1k/6b1/KBp4p/5q2/6Q1/7n b - - 0 1");
//        System.out.println(rf);
//        String[][] board = new String[8][8];
//        int k=0;
//        for(int i=0;i<board.length;i++){
//            Arrays.fill(board[i],String.valueOf(k)+" ");
//            k++;
//
//            for(int j=0;j<board[i].length;j++){
//                board[i][j] = String.valueOf(i)+","+String.valueOf(j)+" ";
//            }
//        }
//        for(int i=0;i<board.length;i++){
//            for(int j=0;j<board[i].length;j++){
//                System.out.print(board[i][j]);
//            }
//            System.out.println();
//        }

        //7N/1b4N1/5R1k/6b1/KBp4p/5q2/6Q1/7n b - - 1 2
        //7N/1b4N1/5R1k/6b1/KBp4p/5q2/6Q1/7n b - - 0 1
    }

}
