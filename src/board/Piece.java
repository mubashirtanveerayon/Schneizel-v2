package board;

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
        for(int[] direction:Constants.ALL_DIRECTIONS){
            int newFile = file+direction[0];
            int newRank = rank+direction[1];
            if(!Util.isValid(newFile,newRank)){
                continue;
            }
            boolean movePossible = false;
            if(cb.board[newRank][newFile] == Constants.EMPTY_SQUARE){
                movePossible = true;
            }else{
                if(Util.isAlly(cb.board[rank][file],cb.board[newRank][newFile])){
                    continue;
                }else{
                    movePossible = true;
                }
            }
            for(int[] dir:Constants.ALL_DIRECTIONS){
                int f=newFile,r=newRank;
                for(int i=0;Util.isValid(f+=dir[0],r+=dir[1]);i++){
                    if(cb.board[r][f] != Constants.EMPTY_SQUARE){
                        if(!Util.isAlly(cb.board[rank][file],cb.board[r][f])){
                            char piece = Character.toUpperCase(cb.board[r][f]);
                            if(i==0){
                                switch(piece){
                                    case Constants.WHITE_PAWN:{
                                        if(Util.isUpperCase(cb.board[rank][file])){
                                            if(Math.abs(newFile-f) == 1 && (newRank-r) == 1){
                                                movePossible = false;
                                            }
                                        }else{
                                            if(Math.abs(newFile-f) == 1 && (newRank-r) == -1){
                                                movePossible = false;
                                            }
                                        }
                                        break;
                                    }
                                    case Constants.WHITE_KING:{
                                        movePossible = false;
                                        break;
                                    }
                                }
                            }
                            if(movePossible){
                                switch(piece){
                                    case Constants.WHITE_QUEEN:{
                                        movePossible = false;
                                        break;
                                    }
                                    case Constants.WHITE_ROOK:{
                                        movePossible = !(dir[0] == 0 || dir[1] == 0);
                                        break;
                                    }
                                    case Constants.WHITE_BISHOP:{
                                        movePossible = (dir[0] == 0 || dir[1] == 0);
                                        break;
                                    }
                                }
                            }
                        }
                        break;
                    }
                }
            }
            for(int[] dir:Constants.KNIGHT_DIRECTION){
                int f = newFile+dir[0],r = newRank+dir[1];
                if(cb.board[r][f] != Constants.EMPTY_SQUARE && !Util.isAlly(cb.board[rank][file],cb.board[r][f])){
                    movePossible = Character.toUpperCase(cb.board[r][f]) == Constants.WHITE_KNIGHT;
                }
            }
            if(movePossible){
                moves.add(Util.cvtMove(file,rank,newFile,newRank));
            }
        }
        //TODO castling
        return moves;
    }

    public ArrayList<String> pawn(int file,int rank){
        int pinnedIndex = file + rank * 8;
        boolean pinned = cb.pinnedPieces.containsKey(pinnedIndex);
        if(pinned){
            if(cb.gs == GameState.CHECK){
                return moves; // pinned pieces cannot resolve check
            }
            int[] pinDirection = Constants.ALL_DIRECTIONS[cb.pinnedPieces.get(pinnedIndex)];
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
            int checkerIndex=0;
            for(Integer i:cb.checkers.keySet()){
                checkerIndex = i;
            }
            int checkerFile = checkerIndex % 8;
            int checkerRank = checkerIndex / 8;
            int[] kingPosition = cb.getKingPosition();
            if(Character.toUpperCase(cb.board[checkerRank][checkerFile]) != Constants.WHITE_KNIGHT && Character.toUpperCase(cb.board[checkerRank][checkerFile]) != Constants.WHITE_PAWN) {
                int[] checkDirection = Constants.ALL_DIRECTIONS[cb.checkers.get(checkerIndex)];
                if (checkDirection[0] == 0) {
                    if (Math.abs(file - checkerFile) == 1) {
                        // only possible move is to capture the checker why? https://lichess.org/editor/4k3/8/3q4/4P3/8/3K4/8/8_w_-_-_0_1?color=white
                        if (Math.abs(rank - checkerRank) == 1 && ((Util.isUpperCase(cb.board[rank][file]) && checkDirection[1] == -1) || (!Util.isUpperCase(cb.board[rank][file]) && checkDirection[1] == 1))) {
                            moves.add(Util.cvtMove(file, rank, file + checkDirection[0], rank + checkDirection[1]));
                        }
                    }
                } else if (checkDirection[1] == 0) {
                    boolean hasPotential = (Util.isUpperCase(cb.board[rank][file]) && kingPosition[1] - rank == -1) || (!Util.isUpperCase(cb.board[rank][file]) && kingPosition[1] - rank == 1);
                    if (file == checkerFile) {
                        // cannot resolve the check, why? https://lichess.org/editor/7k/8/2K3q1/6P1/8/8/8/8_w_-_-_0_1?color=white
                    }else if(hasPotential){
                        // possible move is to block the check how? https://lichess.org/editor/7k/8/1K2q3/3P4/8/8/8/8_w_-_-_0_1?color=white
                        int destRank = cb.turn == Constants.WHITE?rank-1:rank+1;
                        if(file>kingPosition[0] && file<checkerFile && destRank<8 && destRank>=0 && cb.board[destRank][file] == Constants.EMPTY_SQUARE){
                            moves.add(Util.cvtMove(file, rank, file, destRank));
                        }
                        if(Math.abs(file-checkerFile) == 1) {
                            // possible move is to capture the checker how? https://lichess.org/editor/7k/8/1K2q3/3P4/8/8/8/8_w_-_-_0_1?color=white
                            moves.add(Util.cvtMove(file, rank, checkerFile, checkerRank));
                        }
                    }
                }
            }else{
                // no way to block a check from knight or pawn, only possible move is to capture
                if(Math.abs(checkerFile-file) == 1 && Math.abs(checkerRank-rank) == 1){
                    if((cb.turn == Constants.WHITE && checkerRank<rank) || (cb.turn == Constants.BLACK && checkerRank>rank)){
                        moves.add(Util.cvtMove(file,rank,checkerFile,checkerRank));
                    }
                }else if(!cb.fenParts[10].equals("-")){
                    // en passant move to resolve the check!
                    if(Math.abs(Constants.FILES.indexOf(cb.fenParts[10].charAt(0)) - file) == 1){
                        if(Util.isUpperCase(cb.board[rank][file])){
                            if(rank-1 == 8-Integer.parseInt(Character.toString(cb.fenParts[10].charAt(10)))){
                                moves.add(Util.cvtMove(file, rank, Constants.FILES.indexOf(cb.fenParts[10].charAt(0)), 2));
                            }
                        }else{
                            if(rank+1 == 8-Integer.parseInt(Character.toString(cb.fenParts[10].charAt(10)))){
                                moves.add(Util.cvtMove(file, rank, Constants.FILES.indexOf(cb.fenParts[10].charAt(0)), 5));
                            }
                        }
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
            f = file + Constants.ALL_DIRECTIONS[startIndex][0];
            r = rank + Constants.ALL_DIRECTIONS[startIndex][1];
            if(Util.isValid(f,r)&&cb.board[r][f] != Constants.EMPTY_SQUARE){
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
        endIndex = Util.isUpperCase(cb.board[rank][file])?Constants.ALL_DIRECTIONS.length-1:5;

        for(int i=startIndex;i<=endIndex;i++){
            f = file + Constants.ALL_DIRECTIONS[i][0];
            r = rank + Constants.ALL_DIRECTIONS[i][1];
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
                int[] kingPosition = cb.getKingPosition();
                int enPassantPawn = Constants.FILES.indexOf(cb.fenParts[10].charAt(0)),direction = Util.getSign(file,kingPosition[0]);
                boolean enPassant = true;
                if((!Util.isUpperCase(cb.board[rank][file]) && rank == 4)||(Util.isUpperCase(cb.board[rank][file]) && rank == 3)){
                    if(kingPosition[1] == rank){
                        // possible occurrence https://lichess.org/editor/r2k3r/4p1pp/8/2K1Pp1q/8/8/PP1P1PP1/R7_w_k_-_0_1?color=white
                        // en passant reveals a check
                        // make en-passant move
                        boolean foundAnotherPiece = true;
                        for(int i=file+direction;i!=kingPosition[0];i+=direction){
                            foundAnotherPiece = cb.board[rank][i]!=Constants.EMPTY_SQUARE&&Character.toUpperCase(cb.board[rank][i]) != Constants.WHITE_KING;
                            if(foundAnotherPiece){
                                break;
                            }
                        }
                        if(!foundAnotherPiece){
                            //loop to the other side and see if there is an opponent rook or queen
                            for(int i=enPassantPawn-direction;i>0&&i<8;i-=direction){
                                if(cb.board[rank][i] != Constants.EMPTY_SQUARE){
                                    if(!Util.isAlly(cb.board[rank][file],cb.board[rank][i]) && (Character.toUpperCase(cb.board[rank][i]) == Constants.WHITE_ROOK || Character.toUpperCase(cb.board[rank][i]) == Constants.WHITE_QUEEN)){
                                        //en-passant reveals check
                                        enPassant = false;
                                    }
                                    break;
                                }
                            }
                        }
                        if(enPassant){
                            moves.add(Util.cvtMove(file, rank, Constants.FILES.indexOf(cb.fenParts[10].charAt(0)), 2));
                        }
                    }else {
                        moves.add(Util.cvtMove(file, rank, Constants.FILES.indexOf(cb.fenParts[10].charAt(0)), 2));
                    }
                }
            }
        }

        return moves;
    }

    public ArrayList<String> queen(final int file,final int rank){//complete
        int pinnedIndex = file + rank * 8;
        if(cb.pinnedPieces.containsKey(pinnedIndex)){
            if(cb.gs == GameState.CHECK){
                return moves; // a pinned piece cannot resolve check
            }
            int[] pinDirection = Constants.ALL_DIRECTIONS[cb.pinnedPieces.get(pinnedIndex)];
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
        }else if(cb.gs == GameState.CHECK){
            if(cb.checkers.size()>1){
                return moves;// a two-way check cannot be resolved without the king moving to a safe square
            }
            int checkerIndex=0;
            for(Integer i:cb.checkers.keySet()){
                checkerIndex = i;
            }
            int checkerFile = checkerIndex % 8;
            int checkerRank = checkerIndex / 8;
            if(Character.toUpperCase(cb.board[checkerRank][checkerFile]) != Constants.WHITE_KNIGHT || Character.toUpperCase(cb.board[checkerRank][checkerFile]) != Constants.WHITE_PAWN){
                int[] kingPosition = cb.getKingPosition();
                int[] checkDirection = Constants.ALL_DIRECTIONS[cb.checkers.get(checkerIndex)];

                if(checkDirection[1] == 0){
                    //bishop part
                    for(int i=checkerFile;Util.inBetween(checkerFile,kingPosition[0],i);i-=checkDirection[0]){
                        if((i+checkerRank)%2 == (file+rank)%2 && cb.canSlide(file,rank,i,checkerRank)){
                            moves.add(Util.cvtMove(file,rank,i,checkerRank));
                        }
                    }

                    //rook
                    if(Util.inBetween(kingPosition[0],checkerFile,file)){
                        // means the rook is in between the checker and the king, therefore it might be able to block the check
                        if(cb.canSlide(file,rank,file,kingPosition[1])){
                            moves.add(Util.cvtMove(file,rank,file,kingPosition[1]));
                        }
                    }
                }else if(checkDirection[0] == 0){
                    //bishop
                    for(int i=checkerRank;Util.inBetween(checkerRank,kingPosition[1],i);i-=checkDirection[1]){
                        if((i+checkerRank)%2 == (file+rank)%2 && cb.canSlide(file,rank,checkerFile,i)){
                            moves.add(Util.cvtMove(file,rank,checkerFile,i));
                        }
                    }

                    //rook
                    if(Util.inBetween(kingPosition[1],checkerRank,rank)){
                        // means the rook is in between the checker and the king, therefore it might be able to block the check
                        if(cb.canSlide(file,rank,kingPosition[0],rank)){
                            moves.add(Util.cvtMove(file,rank,kingPosition[0],rank));
                        }
                    }
                }else{

                    //rook
                    if(Util.inBetween(kingPosition[0],checkerFile,file)){
                        int f = checkerFile-checkDirection[0],r = checkerRank-checkDirection[1];
                        while(Util.inBetween(f+checkDirection[0],kingPosition[0],f)){
                            if(f == file){
                                if(cb.canSlide(file,rank,f,r)) {
                                    moves.add(Util.cvtMove(file, rank, f, r));
                                }
                                break;
                            }
                            f -= checkDirection[0];
                            r -= checkDirection[1];
                        }
                    }
                    if(Util.inBetween(kingPosition[1],checkerRank,rank)){
                        int f = checkerFile-checkDirection[0],r = checkerRank-checkDirection[1];
                        while(Util.inBetween(r+checkDirection[1],kingPosition[1],r)){
                            if(r == rank){
                                if(cb.canSlide(file,rank,f,r)) {
                                    moves.add(Util.cvtMove(file, rank, f, r));
                                }
                                break;
                            }
                            f -= checkDirection[0];
                            r -= checkDirection[1];
                        }
                    }

                    //bishop
                    if((file+rank)%2 == (checkerFile+checkerRank)%2){
                        int f = checkerFile,r = checkerRank;
                        while(Util.inBetween(f,kingPosition[0],f) && Util.inBetween(r,kingPosition[1],r)){
                            if(rank-file == r-f){
                                if(cb.canSlide(file,rank,f,r)) {
                                    moves.add(Util.cvtMove(file, rank, f, r));
                                }
                                break;
                            }
                            f -= checkDirection[0];
                            r -= checkDirection[1];
                        }
                    }
                }
            }else{
                // no way to block the check, only possible move is to capture
            }
            if(cb.canSlide(checkerFile,checkerRank,file,rank)){
                // capture
                moves.add(Util.cvtMove(file,rank,checkerFile,checkerRank));
            }
            return moves;
        }

        // generate normal moves
        int df,dr;
        for(int[] direction:Constants.ALL_DIRECTIONS){
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

    public ArrayList<String> rook(final int file,final int rank){//complete
        int pinnedIndex = file + rank * 8;
        if(cb.pinnedPieces.containsKey(pinnedIndex)){
            if(cb.gs == GameState.CHECK){
                return moves;
            }
            int[] pinDirection = Constants.ALL_DIRECTIONS[cb.pinnedPieces.get(pinnedIndex)];
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
        }else if(cb.gs==GameState.CHECK){
            if(cb.checkers.size()>1){
                return moves;
            }
            int checkerIndex=0;
            for(Integer i:cb.checkers.keySet()){
                checkerIndex = i;
            }
            int checkerFile = checkerIndex % 8;
            int checkerRank = checkerIndex / 8;
            if(Character.toUpperCase(cb.board[checkerRank][checkerFile]) != Constants.WHITE_KNIGHT && Character.toUpperCase(cb.board[checkerRank][checkerFile]) != Constants.WHITE_PAWN){
                int[] kingPosition = cb.getKingPosition();
                int[] checkDirection = Constants.ALL_DIRECTIONS[cb.checkers.get(checkerIndex)];
                if(checkDirection[0] == 0){
                    if(Util.inBetween(kingPosition[1],checkerRank,rank)){
                        // means the rook is in between the checker and the king, therefore it might be able to block the check
                        if(cb.canSlide(file,rank,kingPosition[0],rank)){
                            moves.add(Util.cvtMove(file,rank,kingPosition[0],rank));
                        }
                    }
                }else if(checkDirection[1] == 0){
                    if(Util.inBetween(kingPosition[0],checkerFile,file)){
                        // means the rook is in between the checker and the king, therefore it might be able to block the check
                        if(cb.canSlide(file,rank,file,kingPosition[1])){
                            moves.add(Util.cvtMove(file,rank,file,kingPosition[1]));
                        }
                    }
                }else{
                    // done

                    if(Util.inBetween(kingPosition[0],checkerFile,file)){
                        int f = checkerFile-checkDirection[0],r = checkerRank-checkDirection[1];
                        while(Util.inBetween(f+checkDirection[0],kingPosition[0],f)){
                            if(f == file){
                                if(cb.canSlide(file,rank,f,r)) {
                                    moves.add(Util.cvtMove(file, rank, f, r));
                                }
                                break;
                            }
                            f -= checkDirection[0];
                            r -= checkDirection[1];
                        }
                    }
                    if(Util.inBetween(kingPosition[1],checkerRank,rank)){
                        int f = checkerFile-checkDirection[0],r = checkerRank-checkDirection[1];
                        while(Util.inBetween(r+checkDirection[1],kingPosition[1],r)){
                            if(r == rank){
                                if(cb.canSlide(file,rank,f,r)) {
                                    moves.add(Util.cvtMove(file, rank, f, r));
                                }
                                break;
                            }
                            f -= checkDirection[0];
                            r -= checkDirection[1];
                        }
                    }
                }
            }else{
                // no way to block, only possible move is to capture the checker
            }
            if(file == checkerFile || rank == checkerRank){
                if(cb.canSlide(file,rank,checkerFile,checkerRank)){
                    moves.add(Util.cvtMove(file,rank,checkerFile,checkerRank));
                }
            }
            return moves;
        }

        // generate normal moves
        int df,dr;
        for(int i=0;i<4;i++){
            df = file + Constants.ALL_DIRECTIONS[i][0];
            dr = file + Constants.ALL_DIRECTIONS[i][1];
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
                df += Constants.ALL_DIRECTIONS[i][0];
                dr += Constants.ALL_DIRECTIONS[i][1];
            }
        }
        return moves;
    }

    public ArrayList<String> knight(final int file,final int rank){//complete
        if(cb.pinnedPieces.containsKey(file + rank * 8)){
            return moves; // a pinned knight cannot move
        }else if(cb.gs == GameState.CHECK){
            if(cb.checkers.size()>1){
                return moves;
            }
            int checkerIndex=0;
            for(Integer i:cb.checkers.keySet()){
                checkerIndex = i;
            }
            int checkerFile = checkerIndex % 8;
            int checkerRank = checkerIndex / 8;
            if(Character.toUpperCase(cb.board[checkerRank][checkerFile]) != Constants.WHITE_KNIGHT && Character.toUpperCase(cb.board[checkerRank][checkerFile]) != Constants.WHITE_PAWN){
                int[] checkDirection = Constants.ALL_DIRECTIONS[cb.checkers.get(checkerIndex)];
                int[] kingPosition = cb.getKingPosition();
                int currentFile=kingPosition[0]+checkDirection[0],currentRank=kingPosition[1] + checkDirection[1];
                while(moves.size()<2 && currentFile!=checkerFile && currentRank!=checkerRank){
                    if(currentFile == file || currentRank == rank){
                        continue;
                    }
                    int[] direction = Util.getDirection(file,rank,currentFile,currentRank);
                    for(int i=0;i<2;i++){
                        if(file+ Constants.KNIGHT_DIRECTION[i][0]*direction[0] == currentFile && rank+ Constants.KNIGHT_DIRECTION[i][1]*direction[1] == currentRank){
                            moves.add(Util.cvtMove(file,rank,currentFile,currentRank));
                            break;
                        }
                    }
                }

            }else{
                //only possible move is to capture
                if(checkerFile == file || checkerRank == rank){
                    return moves; // because when the knight moves it changes both, its file and rank, therefore it cannot reach to a tile sharing the same file or rank
                }
                int[] direction = Util.getDirection(file,rank,checkerFile,checkerRank);
                for(int i=0;i<2;i++){
                    if(file+Constants.KNIGHT_DIRECTION[i][0]*direction[0] == checkerFile && rank+Constants.KNIGHT_DIRECTION[i][1]*direction[1] == checkerRank){
                        moves.add(Util.cvtMove(file,rank,checkerFile,checkerRank));
                        break;
                    }
                }
            }
            return moves;
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

    public ArrayList<String> bishop(final int file,final int rank){//complete!
        int pinnedIndex = file + rank * 8;
        if(cb.pinnedPieces.containsKey(pinnedIndex)){
            if(cb.gs == GameState.CHECK){
                return moves;
            }
            int[] pinDirection = Constants.ALL_DIRECTIONS[cb.pinnedPieces.get(pinnedIndex)];
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
        }else if(cb.gs == GameState.CHECK){
            if(cb.checkers.size()>1){
                return moves;
            }
            int checkerIndex=0;
            for(Integer i:cb.checkers.keySet()){
                checkerIndex = i;
            }
            int checkerFile = checkerIndex % 8;
            int checkerRank = checkerIndex / 8;
            if(Character.toUpperCase(cb.board[checkerRank][checkerFile]) != Constants.WHITE_KNIGHT && Character.toUpperCase(cb.board[checkerRank][checkerFile]) != Constants.WHITE_PAWN){
                int[] checkDirection = Constants.ALL_DIRECTIONS[cb.checkers.get(checkerIndex)];
                int[] kingPosition = cb.getKingPosition();
                if(checkDirection[1] == 0){
                    for(int i=checkerFile;Util.inBetween(checkerFile+checkDirection[0],kingPosition[0],i);i-=checkDirection[0]){
                        if((i+checkerRank)%2 == (file+rank)%2 && cb.canSlide(file,rank,i,checkerRank)){
                            moves.add(Util.cvtMove(file,rank,i,checkerRank));
                        }
                    }
                }else if(checkDirection[0] == 0){
                    for(int i=checkerRank;Util.inBetween(checkerRank+checkDirection[1],kingPosition[1],i);i-=checkDirection[1]){
                        if((i+checkerRank)%2 == (file+rank)%2 && cb.canSlide(file,rank,checkerFile,i)){
                            moves.add(Util.cvtMove(file,rank,checkerFile,i));
                        }
                    }
                }else{
                    if((file+rank)%2 == (checkerFile+checkerRank)%2){
                        int f = checkerFile,r = checkerRank;
                        while(Util.inBetween(f+checkDirection[0],kingPosition[0],f) && Util.inBetween(r+checkDirection[1],kingPosition[1],r)){
                            if(rank-file == r-f){
                                if(cb.canSlide(file,rank,f,r)) {
                                    moves.add(Util.cvtMove(file, rank, f, r));
                                }
                                break;
                            }
                            f -= checkDirection[0];
                            r -= checkDirection[1];
                        }
                    }
                }
            }else{
                // only possible move is to capture
                if(cb.canSlide(file,rank,checkerFile,checkerRank)){
                    moves.add(Util.cvtMove(file,rank,checkerFile,checkerRank));
                }
            }


            return moves;
        }

        //normal moves
        int df,dr;
        for(int i = 4; i<Constants.ALL_DIRECTIONS.length; i++){
            df = file + Constants.ALL_DIRECTIONS[i][0];
            dr = file + Constants.ALL_DIRECTIONS[i][1];
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
                df += Constants.ALL_DIRECTIONS[i][0];
                dr += Constants.ALL_DIRECTIONS[i][1];
            }
        }
        return moves;
    }


}
