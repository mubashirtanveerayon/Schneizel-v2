import java.util.ArrayList;

public class Move {

    public ChessBoard cb;
    public Piece piece;

    private ArrayList<String> moves;

    public Move(ChessBoard cb){
        this.cb = cb;
    }

    public Move(){
        this.cb = new ChessBoard();
    }


    private void initialize(){
        piece = new Piece(cb);
        moves = new ArrayList<>();
    }

    public ArrayList<String> getValidMoves(){
        int file,rank;
        moves.clear();

        return moves;
    }


    public void getAllValidMoves(char turn){
        moves.clear();
        int file,rank;
        for(Integer index:cb.pieceLocations){
            file = index % 8;
            rank = index / 8;
            if((turn == Constants.WHITE && Util.isUpperCase(cb.board[rank][file])) || turn == Constants.BLACK && !Util.isUpperCase(cb.board[rank][file])){
                moves.addAll(piece.generateMove(file, rank));
            }
        }
    }


    public void makeMove(String move){

        if(move.equalsIgnoreCase(Constants.KING_SIDE_CASTLING) || move.equalsIgnoreCase(Constants.QUEEN_SIDE_CASTLING)){
            int rank = cb.turn == Constants.WHITE?7:0;
            if(move.equalsIgnoreCase(Constants.KING_SIDE_CASTLING)) {
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
            }else{
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
            }
            cb.fenParts[rank] = FenUtils.getFileFen(cb.board[rank]);

            if(cb.turn  == Constants.WHITE){
                cb.fenParts[9] = cb.fenParts[9].replace(Character.toString(Constants.WHITE_KING),"").replace(Character.toString(Constants.WHITE_QUEEN),"");
            }else{
                cb.fenParts[9] = cb.fenParts[9].replace(Character.toString(Constants.BLACK_KING),"").replace(Character.toString(Constants.BLACK_QUEEN),"");
            }
            cb.turn  = cb.turn  == Constants.WHITE?Constants.BLACK:Constants.WHITE;
            cb.fenParts[8] = Character.toString(cb.turn);
            cb.fenParts[10] = "-";
        }else if(move.charAt(1) == move.charAt(3)){
            int rank = 8-Integer.parseInt(String.valueOf(move.charAt(1)));
            int locFile = Constants.FILES.indexOf(move.charAt(0));
            int destFile = Constants.FILES.indexOf(move.charAt(2));
            if(cb.board[rank][locFile] == Constants.WHITE_KING){
                cb.whiteKingPosition[0] = destFile;
                cb.whiteKingPosition[1] = rank;
            }else if(cb.board[rank][locFile] == Constants.BLACK_KING){
                cb.blackKingPosition[0] = destFile;
                cb.blackKingPosition[1] = rank;
            }
            cb.board[rank][destFile] = cb.board[rank][locFile];
            cb.board[rank][locFile] = Constants.EMPTY_SQUARE;
            cb.pieceLocations.add(destFile + rank * 8);
            cb.pieceLocations.remove((Object)(locFile + rank * 8));
            if(rank == 0 || rank == 7){
                switch(cb.board[rank][locFile]){
                    case Constants.WHITE_ROOK:
                        if(locFile==0){
                            cb.fenParts[9] = cb.fenParts[9].replace(Character.toString(Constants.WHITE_QUEEN),"");
                        }else if(locFile==7){
                            cb.fenParts[9] = cb.fenParts[9].replace(Character.toString(Constants.WHITE_KING), "");
                        }
                        break;
                    case Constants.WHITE_KING:
                        cb.fenParts[9] = cb.fenParts[9].replace(Character.toString(Constants.WHITE_KING),"").replace(Character.toString(Constants.WHITE_QUEEN),"");
                        break;
                    case Constants.BLACK_ROOK:
                        if(locFile==0){
                            cb.fenParts[9] = cb.fenParts[9].replace(Character.toString(Constants.BLACK_QUEEN),"");
                        }else if(locFile==7) {
                            cb.fenParts[9] = cb.fenParts[9].replace(Character.toString(Constants.BLACK_KING), "");
                        }
                        break;
                    case Constants.BLACK_KING:
                        cb.fenParts[9] = cb.fenParts[9].replace(Character.toString(Constants.BLACK_KING),"").replace(Character.toString(Constants.BLACK_QUEEN),"");
                        break;
                }
                switch(cb.board[rank][destFile]){
                    case Constants.WHITE_ROOK:
                        if(destFile==0){
                            cb.fenParts[9] = cb.fenParts[9].replace(Character.toString(Constants.WHITE_QUEEN),"");
                        }else if(destFile==7){
                            cb.fenParts[9] = cb.fenParts[9].replace(Character.toString(Constants.WHITE_KING), "");
                        }
                        break;
                    case Constants.BLACK_ROOK:
                        if(destFile==0){
                            cb.fenParts[9] = cb.fenParts[9].replace(Character.toString(Constants.BLACK_QUEEN),"");
                        }else if(destFile==7) {
                            cb.fenParts[9] = cb.fenParts[9].replace(Character.toString(Constants.BLACK_KING), "");
                        }
                        break;
                }
            }

            cb.fenParts[rank] = FenUtils.getFileFen(cb.board[rank]);
            cb.fenParts[10] = "-";
            cb.turn  = cb.turn  == Constants.WHITE?Constants.BLACK:Constants.WHITE;
            cb.fenParts[8] = Character.toString(cb.turn);
        }else{
            int locRank = 8-Integer.parseInt(String.valueOf(move.charAt(1)));
            int destRank = 8-Integer.parseInt(String.valueOf(move.charAt(3)));
            int locFile = Constants.FILES.indexOf(move.charAt(0));
            int destFile = Constants.FILES.indexOf(move.charAt(2));
            if(cb.board[locRank][locFile] == Constants.WHITE_KING){
                cb.whiteKingPosition[0] = destFile;
                cb.whiteKingPosition[1] = destRank;
            }else if(cb.board[locRank][locFile] == Constants.BLACK_KING){
                cb.blackKingPosition[0] = destFile;
                cb.blackKingPosition[1] = destRank;
            }
            if(locRank == 0 || locRank == 7){
                switch(cb.board[locRank][locFile]){
                    case Constants.WHITE_ROOK:
                        if(locFile==0){
                            cb.fenParts[9] = cb.fenParts[9].replace(Character.toString(Constants.WHITE_QUEEN),"");
                        }else if(locFile==7){
                            cb.fenParts[9] = cb.fenParts[9].replace(Character.toString(Constants.WHITE_KING), "");
                        }
                        break;
                    case Constants.WHITE_KING:
                        cb.fenParts[9] = cb.fenParts[9].replace(Character.toString(Constants.WHITE_KING),"").replace(Character.toString(Constants.WHITE_QUEEN),"");
                        break;
                    case Constants.BLACK_ROOK:
                        if(locFile==0){
                            cb.fenParts[9] = cb.fenParts[9].replace(Character.toString(Constants.BLACK_QUEEN),"");
                        }else if(locFile==7) {
                            cb.fenParts[9] = cb.fenParts[9].replace(Character.toString(Constants.BLACK_KING), "");
                        }
                        break;
                    case Constants.BLACK_KING:
                        cb.fenParts[9] = cb.fenParts[9].replace(Character.toString(Constants.BLACK_KING),"").replace(Character.toString(Constants.BLACK_QUEEN),"");
                        break;
                }
            }

            if(destRank == 0 || destRank == 7){
                switch(cb.board[destRank][destFile]){
                    case Constants.WHITE_ROOK:
                        if(destFile==0){
                            cb.fenParts[9] = cb.fenParts[9].replace(Character.toString(Constants.WHITE_QUEEN),"");
                        }else if(destFile==7){
                            cb.fenParts[9]= cb.fenParts[9].replace(Character.toString(Constants.WHITE_KING), "");
                        }
                        break;
                    case Constants.BLACK_ROOK:
                        if(destFile==0){
                            cb.fenParts[9] = cb.fenParts[9].replace(Character.toString(Constants.BLACK_QUEEN),"");
                        }else if(destFile==7) {
                            cb.fenParts[9] = cb.fenParts[9].replace(Character.toString(Constants.BLACK_KING), "");
                        }
                        break;
                }
            }

            cb.board[destRank][destFile] = cb.board[locRank][locFile];

            cb.board[locRank][locFile] = Constants.EMPTY_SQUARE;
            cb.pieceLocations.add(destFile + destRank * 8);
            cb.pieceLocations.remove((Object)(locFile + locRank * 8));

            String enPassantSquare = "-";
            if(Character.toUpperCase(cb.board[destRank][destFile]) == Constants.WHITE_PAWN){
                // en-passant
                if(Math.abs(locFile - destFile) == 1 && (locRank == 3 || locRank == 4) && Character.toUpperCase(cb.board[locRank][destFile]) == Constants.WHITE_PAWN){
                    cb.board[locRank][destFile] = Constants.EMPTY_SQUARE;
                    cb.pieceLocations.remove((Object)(destFile + locRank * 8));
                }else if (destRank == 0 || destRank == 7){
                    // pawn-promotion
                    cb.board[destRank][destFile] = move.charAt(move.length()-1);
                }else if(Math.abs(destRank - locRank) == 2) {
                    // double pawn push -> creates new en-passant square
                    switch(destRank){
                        case 3 :
                            enPassantSquare = move.charAt(0) + "2";
                            break;
                        case 4:
                            enPassantSquare = move.charAt(0) + "5";
                            break;
                    }
                }
            }

            cb.fenParts[locRank] = FenUtils.getFileFen(cb.board[locRank]);
            cb.fenParts[destRank]  = FenUtils.getFileFen(cb.board[destRank]);

            cb.fenParts[10] = enPassantSquare;

            cb.turn  = cb.turn  == Constants.WHITE?Constants.BLACK:Constants.WHITE;

            cb.fenParts[8] = Character.toString(cb.turn);
        }
        cb.fenParts[11] = String.valueOf(Integer.parseInt(cb.fenParts[11])+1);
        cb.fenParts[12] = String.valueOf(Integer.parseInt(cb.fenParts[12])+1);

        cb.checksAndPinnedPieces();

    }

    public void unMakeMove(String move){
        cb.turn = cb.turn == Constants.WHITE?Constants.BLACK:Constants.WHITE;
        if(move.equalsIgnoreCase(Constants.KING_SIDE_CASTLING) || move.equalsIgnoreCase(Constants.QUEEN_SIDE_CASTLING)){
            int rank = cb.turn == Constants.WHITE?7:0;
            if(move.equalsIgnoreCase(Constants.KING_SIDE_CASTLING)) {
                cb.board[rank][4] = cb.board[rank][6];
                cb.board[rank][6] = Constants.EMPTY_SQUARE;
                cb.board[rank][7] = cb.board[rank][5];
                cb.board[rank][5] = Constants.EMPTY_SQUARE;
                cb.pieceLocations.add(4 + rank * 8);
                cb.pieceLocations.remove((Object)(6 + rank * 8));
                cb.pieceLocations.add(7 + rank * 8);
                cb.pieceLocations.remove((Object)(5 + rank * 8));
            }else{
                cb.board[rank][4] = cb.board[rank][2];
                cb.board[rank][2] = Constants.EMPTY_SQUARE;
                cb.board[rank][0] = cb.board[rank][3];
                cb.board[rank][3] = Constants.EMPTY_SQUARE;
                cb.pieceLocations.add(4 + rank * 8);
                cb.pieceLocations.remove((Object)(2 + rank * 8));
                cb.pieceLocations.add(0 + rank * 8);
                cb.pieceLocations.remove((Object)(3 + rank * 8));
            }
            if(cb.turn == Constants.WHITE){
                cb.whiteKingPosition[0] = 4;
                cb.whiteKingPosition[1] = 7;
                // TODO: can't say if both side castling was possible before the move was made, for now I am living this as is
                if(move.equalsIgnoreCase(Constants.KING_SIDE_CASTLING)){
                    cb.fenParts[9] += Constants.WHITE_KING;
                }else{
                    cb.fenParts[9] += Constants.WHITE_QUEEN;
                }
            }else{
                cb.blackKingPosition[0] = 4;
                cb.blackKingPosition[1] = 0;
                // TODO: can't say if both side castling was possible before the move was made, for now I am living this as is
                if(move.equalsIgnoreCase(Constants.KING_SIDE_CASTLING)){
                    cb.fenParts[9] += Constants.BLACK_KING;
                }else{
                    cb.fenParts[9] += Constants.BLACK_QUEEN;
                }
            }
            cb.fenParts[rank] = FenUtils.getFileFen(cb.board[rank]);
            cb.fenParts[8] = Character.toString(cb.turn);
            cb.fenParts[10] = "-";
        }else if(move.charAt(1) == move.charAt(3)){ // TODO
            int rank = 8-Integer.parseInt(String.valueOf(move.charAt(1)));
            int locFile = Constants.FILES.indexOf(move.charAt(0));
            int destFile = Constants.FILES.indexOf(move.charAt(2));
            if(cb.board[rank][locFile] == Constants.WHITE_KING){
                cb.whiteKingPosition[0] = destFile;
                cb.whiteKingPosition[1] = rank;
            }else if(cb.board[rank][locFile] == Constants.BLACK_KING){
                cb.blackKingPosition[0] = destFile;
                cb.blackKingPosition[1] = rank;
            }
            cb.board[rank][destFile] = cb.board[rank][locFile];
            cb.board[rank][locFile] = Constants.EMPTY_SQUARE;
            cb.pieceLocations.add(destFile + rank * 8);
            cb.pieceLocations.remove((Object)(locFile + rank * 8));
            if(rank == 0 || rank == 7){
                switch(cb.board[rank][locFile]){
                    case Constants.WHITE_ROOK:
                        if(locFile==0){
                            cb.fenParts[9] = cb.fenParts[9].replace(Character.toString(Constants.WHITE_QUEEN),"");
                        }else if(locFile==7){
                            cb.fenParts[9] = cb.fenParts[9].replace(Character.toString(Constants.WHITE_KING), "");
                        }
                        break;
                    case Constants.WHITE_KING:
                        cb.fenParts[9] = cb.fenParts[9].replace(Character.toString(Constants.WHITE_KING),"").replace(Character.toString(Constants.WHITE_QUEEN),"");
                        break;
                    case Constants.BLACK_ROOK:
                        if(locFile==0){
                            cb.fenParts[9] = cb.fenParts[9].replace(Character.toString(Constants.BLACK_QUEEN),"");
                        }else if(locFile==7) {
                            cb.fenParts[9] = cb.fenParts[9].replace(Character.toString(Constants.BLACK_KING), "");
                        }
                        break;
                    case Constants.BLACK_KING:
                        cb.fenParts[9] = cb.fenParts[9].replace(Character.toString(Constants.BLACK_KING),"").replace(Character.toString(Constants.BLACK_QUEEN),"");
                        break;
                }
                switch(cb.board[rank][destFile]){
                    case Constants.WHITE_ROOK:
                        if(destFile==0){
                            cb.fenParts[9] = cb.fenParts[9].replace(Character.toString(Constants.WHITE_QUEEN),"");
                        }else if(destFile==7){
                            cb.fenParts[9] = cb.fenParts[9].replace(Character.toString(Constants.WHITE_KING), "");
                        }
                        break;
                    case Constants.BLACK_ROOK:
                        if(destFile==0){
                            cb.fenParts[9] = cb.fenParts[9].replace(Character.toString(Constants.BLACK_QUEEN),"");
                        }else if(destFile==7) {
                            cb.fenParts[9] = cb.fenParts[9].replace(Character.toString(Constants.BLACK_KING), "");
                        }
                        break;
                }
            }

            cb.fenParts[rank] = FenUtils.getFileFen(cb.board[rank]);
            cb.fenParts[10] = "-";
            cb.turn  = cb.turn  == Constants.WHITE?Constants.BLACK:Constants.WHITE;
            cb.fenParts[8] = Character.toString(cb.turn);
        }else{
            int locRank = 8-Integer.parseInt(String.valueOf(move.charAt(1)));
            int destRank = 8-Integer.parseInt(String.valueOf(move.charAt(3)));
            int locFile = Constants.FILES.indexOf(move.charAt(0));
            int destFile = Constants.FILES.indexOf(move.charAt(2));
            if(cb.board[locRank][locFile] == Constants.WHITE_KING){
                cb.whiteKingPosition[0] = destFile;
                cb.whiteKingPosition[1] = destRank;
            }else if(cb.board[locRank][locFile] == Constants.BLACK_KING){
                cb.blackKingPosition[0] = destFile;
                cb.blackKingPosition[1] = destRank;
            }
            if(locRank == 0 || locRank == 7){
                switch(cb.board[locRank][locFile]){
                    case Constants.WHITE_ROOK:
                        if(locFile==0){
                            cb.fenParts[9] = cb.fenParts[9].replace(Character.toString(Constants.WHITE_QUEEN),"");
                        }else if(locFile==7){
                            cb.fenParts[9] = cb.fenParts[9].replace(Character.toString(Constants.WHITE_KING), "");
                        }
                        break;
                    case Constants.WHITE_KING:
                        cb.fenParts[9] = cb.fenParts[9].replace(Character.toString(Constants.WHITE_KING),"").replace(Character.toString(Constants.WHITE_QUEEN),"");
                        break;
                    case Constants.BLACK_ROOK:
                        if(locFile==0){
                            cb.fenParts[9] = cb.fenParts[9].replace(Character.toString(Constants.BLACK_QUEEN),"");
                        }else if(locFile==7) {
                            cb.fenParts[9] = cb.fenParts[9].replace(Character.toString(Constants.BLACK_KING), "");
                        }
                        break;
                    case Constants.BLACK_KING:
                        cb.fenParts[9] = cb.fenParts[9].replace(Character.toString(Constants.BLACK_KING),"").replace(Character.toString(Constants.BLACK_QUEEN),"");
                        break;
                }
            }

            if(destRank == 0 || destRank == 7){
                switch(cb.board[destRank][destFile]){
                    case Constants.WHITE_ROOK:
                        if(destFile==0){
                            cb.fenParts[9] = cb.fenParts[9].replace(Character.toString(Constants.WHITE_QUEEN),"");
                        }else if(destFile==7){
                            cb.fenParts[9]= cb.fenParts[9].replace(Character.toString(Constants.WHITE_KING), "");
                        }
                        break;
                    case Constants.BLACK_ROOK:
                        if(destFile==0){
                            cb.fenParts[9] = cb.fenParts[9].replace(Character.toString(Constants.BLACK_QUEEN),"");
                        }else if(destFile==7) {
                            cb.fenParts[9] = cb.fenParts[9].replace(Character.toString(Constants.BLACK_KING), "");
                        }
                        break;
                }
            }

            cb.board[destRank][destFile] = cb.board[locRank][locFile];

            cb.board[locRank][locFile] = Constants.EMPTY_SQUARE;
            cb.pieceLocations.add(destFile + destRank * 8);
            cb.pieceLocations.remove((Object)(locFile + locRank * 8));

            String enPassantSquare = "-";
            if(Character.toUpperCase(cb.board[destRank][destFile]) == Constants.WHITE_PAWN){
                // en-passant
                if(Math.abs(locFile - destFile) == 1 && (locRank == 3 || locRank == 4) && Character.toUpperCase(cb.board[locRank][destFile]) == Constants.WHITE_PAWN){
                    cb.board[locRank][destFile] = Constants.EMPTY_SQUARE;
                    cb.pieceLocations.remove((Object)(destFile + locRank * 8));
                }else if (destRank == 0 || destRank == 7){
                    // pawn-promotion
                    cb.board[destRank][destFile] = move.charAt(move.length()-1);
                }else if(Math.abs(destRank - locRank) == 2) {
                    // double pawn push -> creates new en-passant square
                    switch(destRank){
                        case 3 :
                            enPassantSquare = move.charAt(0) + "2";
                            break;
                        case 4:
                            enPassantSquare = move.charAt(0) + "5";
                            break;
                    }
                }
            }

            cb.fenParts[locRank] = FenUtils.getFileFen(cb.board[locRank]);
            cb.fenParts[destRank]  = FenUtils.getFileFen(cb.board[destRank]);

            cb.fenParts[10] = enPassantSquare;

            cb.turn  = cb.turn  == Constants.WHITE?Constants.BLACK:Constants.WHITE;

            cb.fenParts[8] = Character.toString(cb.turn);
        }
        cb.fenParts[11] = String.valueOf(Integer.parseInt(cb.fenParts[11])+1);
        cb.fenParts[12] = String.valueOf(Integer.parseInt(cb.fenParts[12])+1);

        cb.checksAndPinnedPieces();

    }



















}
