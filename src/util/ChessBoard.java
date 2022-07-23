package util;

public class ChessBoard {

    public char[][] board;
    String fen;
    int halfMove;
    int fullMove;
    char turn;

    public ChessBoard(){
        fen = Constants.STARTING_FEN;

        initialize();
    }

    public ChessBoard(String fen){
        if(!FenUtils.isFenValid(fen)){
            return;
        }
        this.fen = fen;
        initialize();
    }

    private void initialize(){
        board = FenUtils.parseFen(fen);
        String[] fenPartsBySpace = fen.split(" ");
        halfMove = (Integer.parseInt(fenPartsBySpace[4]));
        fullMove = (Integer.parseInt(fenPartsBySpace[5]));
        turn = fenPartsBySpace[1].charAt(0);
    }

    public void move(String move){
        StringBuilder newFen = new StringBuilder();
        String[] fenPartsBySlash = fen.split("/");
        String[] fenPartsBySpace = fen.split(" ");
        if(move.equalsIgnoreCase(Constants.KING_SIDE_CASTLING) || move.equalsIgnoreCase(Constants.QUEEN_SIDE_CASTLING)){
            int rank = turn == Constants.WHITE?7:0;
            if(move.equalsIgnoreCase(Constants.KING_SIDE_CASTLING)) {
                board[rank][6] = board[rank][4];
                board[rank][4] = Constants.EMPTY_SQUARE;
                board[rank][5] = board[rank][7];
                board[rank][7] = Constants.EMPTY_SQUARE;
            }else{
                board[rank][2] = board[rank][4];
                board[rank][4] = Constants.EMPTY_SQUARE;
                board[rank][3] = board[rank][0];
                board[rank][0] = Constants.EMPTY_SQUARE;
            }
            String newFileFen = FenUtils.getFileFen(board[rank]);
            for(int i=0;i<fenPartsBySlash.length;i++){
                if(i==rank){
                    newFen.append(newFileFen);
                }else{
                    if(i+1 == fenPartsBySlash.length){
                        newFen.append(fenPartsBySlash[i].split(" ")[0]);
                    }else {
                        newFen.append(fenPartsBySlash[i]);
                    }
                }
                if(i+1!=fenPartsBySlash.length){
                    newFen.append("/");
                }
            }
            String castling;
            if(turn == Constants.WHITE){
                castling = fenPartsBySpace[2].replace(Character.toString(Constants.WHITE_KING),"").replace(Character.toString(Constants.WHITE_QUEEN),"");
            }else{
                castling = fenPartsBySpace[2].replace(Character.toString(Constants.BLACK_KING),"").replace(Character.toString(Constants.BLACK_QUEEN),"");
            }
            turn = turn == Constants.WHITE?Constants.BLACK:Constants.WHITE;
            fullMove++;
            halfMove++;
            newFen.append(" ").append(turn).append(" ").append(castling).append(" ").append("-").append(" ").append(halfMove).append(" ").append(fullMove);

        }else if(move.charAt(1) == move.charAt(3)){
            int rank = 8-Integer.parseInt(String.valueOf(move.charAt(1)));
            int locFile = Constants.FILES.indexOf(move.charAt(0));
            int destFile = Constants.FILES.indexOf(move.charAt(2));
            board[rank][destFile] = board[rank][locFile];
            board[rank][locFile] = Constants.EMPTY_SQUARE;

            if(rank == 0 || rank == 7){
                switch(board[rank][locFile]){
                    case Constants.WHITE_ROOK:
                        if(locFile==0){
                            fenPartsBySpace[2] = fenPartsBySpace[2].replace(Character.toString(Constants.WHITE_QUEEN),"");
                        }else if(locFile==7){
                            fenPartsBySpace[2] = fenPartsBySpace[2].replace(Character.toString(Constants.WHITE_KING), "");
                        }
                        break;
                    case Constants.WHITE_KING:
                        fenPartsBySpace[2] = fenPartsBySpace[2].replace(Character.toString(Constants.WHITE_KING),"").replace(Character.toString(Constants.WHITE_QUEEN),"");
                        break;
                    case Constants.BLACK_ROOK:
                        if(locFile==0){
                            fenPartsBySpace[2] = fenPartsBySpace[2].replace(Character.toString(Constants.BLACK_QUEEN),"");
                        }else if(locFile==7) {
                            fenPartsBySpace[2] = fenPartsBySpace[2].replace(Character.toString(Constants.BLACK_KING), "");
                        }
                        break;
                    case Constants.BLACK_KING:
                        fenPartsBySpace[2] = fenPartsBySpace[2].replace(Character.toString(Constants.BLACK_KING),"").replace(Character.toString(Constants.BLACK_QUEEN),"");
                        break;
                }
                switch(board[rank][destFile]){
                    case Constants.WHITE_ROOK:
                        if(destFile==0){
                            fenPartsBySpace[2] = fenPartsBySpace[2].replace(Character.toString(Constants.WHITE_QUEEN),"");
                        }else if(destFile==7){
                            fenPartsBySpace[2] = fenPartsBySpace[2].replace(Character.toString(Constants.WHITE_KING), "");
                        }
                        break;
                    case Constants.BLACK_ROOK:
                        if(destFile==0){
                            fenPartsBySpace[2] = fenPartsBySpace[2].replace(Character.toString(Constants.BLACK_QUEEN),"");
                        }else if(destFile==7) {
                            fenPartsBySpace[2] = fenPartsBySpace[2].replace(Character.toString(Constants.BLACK_KING), "");
                        }
                        break;
                }
            }

            String newFileFen = FenUtils.getFileFen(board[rank]);
            for(int i=0;i<fenPartsBySlash.length;i++){
                if(i==rank){
                    newFen.append(newFileFen);
                }else{
                    if(i+1 == fenPartsBySlash.length){
                        newFen.append(fenPartsBySlash[i].split(" ")[0]);
                    }else {
                        newFen.append(fenPartsBySlash[i]);
                    }
                }
                if(i+1!=fenPartsBySlash.length){
                    newFen.append("/");
                }
            }
            turn = turn == Constants.WHITE?Constants.BLACK:Constants.WHITE;
            fullMove++;
            halfMove++;
            newFen.append(" ").append(turn).append(" ").append(fenPartsBySpace[2]).append(" ").append("-").append(" ").append(halfMove).append(" ").append(fullMove);
        }else{
            int locRank = 8-Integer.parseInt(String.valueOf(move.charAt(1)));
            int destRank = 8-Integer.parseInt(String.valueOf(move.charAt(3)));
            int locFile = Constants.FILES.indexOf(move.charAt(0));
            int destFile = Constants.FILES.indexOf(move.charAt(2));

            if(locRank == 0 || locRank == 7){
                switch(board[locRank][locFile]){
                    case Constants.WHITE_ROOK:
                        if(locFile==0){
                            fenPartsBySpace[2] = fenPartsBySpace[2].replace(Character.toString(Constants.WHITE_QUEEN),"");
                        }else if(locFile==7){
                            fenPartsBySpace[2] = fenPartsBySpace[2].replace(Character.toString(Constants.WHITE_KING), "");
                        }
                        break;
                    case Constants.WHITE_KING:
                        fenPartsBySpace[2] = fenPartsBySpace[2].replace(Character.toString(Constants.WHITE_KING),"").replace(Character.toString(Constants.WHITE_QUEEN),"");
                        break;
                    case Constants.BLACK_ROOK:
                        if(locFile==0){
                            fenPartsBySpace[2] = fenPartsBySpace[2].replace(Character.toString(Constants.BLACK_QUEEN),"");
                        }else if(locFile==7) {
                            fenPartsBySpace[2] = fenPartsBySpace[2].replace(Character.toString(Constants.BLACK_KING), "");
                        }
                        break;
                    case Constants.BLACK_KING:
                        fenPartsBySpace[2] = fenPartsBySpace[2].replace(Character.toString(Constants.BLACK_KING),"").replace(Character.toString(Constants.BLACK_QUEEN),"");
                        break;
                }
            }

            if(destRank == 0 || destRank == 7){
                switch(board[destRank][destFile]){
                    case Constants.WHITE_ROOK:
                        if(destFile==0){
                            fenPartsBySpace[2] = fenPartsBySpace[2].replace(Character.toString(Constants.WHITE_QUEEN),"");
                        }else if(destFile==7){
                            fenPartsBySpace[2] = fenPartsBySpace[2].replace(Character.toString(Constants.WHITE_KING), "");
                        }
                        break;
                    case Constants.BLACK_ROOK:
                        if(destFile==0){
                            fenPartsBySpace[2] = fenPartsBySpace[2].replace(Character.toString(Constants.BLACK_QUEEN),"");
                        }else if(destFile==7) {
                            fenPartsBySpace[2] = fenPartsBySpace[2].replace(Character.toString(Constants.BLACK_KING), "");
                        }
                        break;
                }
            }

            board[destRank][destFile] = board[locRank][locFile];
            board[locRank][locFile] = Constants.EMPTY_SQUARE;

            String enPassantSquare = "-";
            if(Character.toUpperCase(board[destRank][destFile]) == Constants.WHITE_PAWN){
                // en-passant
                if(Math.abs(locFile - destFile) == 1 && (locRank == 3 || locRank == 4) && Character.toUpperCase(board[locRank][destFile]) == Constants.WHITE_PAWN){
                    board[locRank][destFile] = Constants.EMPTY_SQUARE;
                }else if (destRank == 0 || destRank == 7){
                    // pawn-promotion
                    board[destRank][destFile] = move.charAt(move.length()-1);
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

            String newLocFileFen = FenUtils.getFileFen(board[locRank]);
            String newDestFileFen = FenUtils.getFileFen(board[destRank]);

            for(int i=0;i<fenPartsBySlash.length;i++){
                if(i==locRank){
                    newFen.append(newLocFileFen);
                }else if(i==destRank){
                    newFen.append(newDestFileFen);
                }else{
                    if(i+1 == fenPartsBySlash.length){
                        newFen.append(fenPartsBySlash[i].split(" ")[0]);
                    }else {
                        newFen.append(fenPartsBySlash[i]);
                    }
                }
                if(i+1!=fenPartsBySlash.length){
                    newFen.append("/");
                }
            }
            turn = turn == Constants.WHITE?Constants.BLACK:Constants.WHITE;
            fullMove++;
            halfMove++;
            newFen.append(" ").append(turn).append(" ").append(fenPartsBySpace[2]).append(" ").append(enPassantSquare).append(" ").append(halfMove).append(" ").append(fullMove);

        }
        fen = newFen.toString();
    }


    public static void main(String[] args) {
        ChessBoard cb = new ChessBoard("7N/1b3RN1/7k/6b1/KBp4p/5q2/6Q1/7n w - - 0 1");
//        FenUtils.printFile(cb.board[0]);
        Util.printBoard(cb.board,false);

        cb.move("f7f6");
        Util.printBoard(cb.board,false);
        System.out.println(cb.fen);
        System.out.println(FenUtils.isFenValid(cb.fen));
//        cb = new ChessBoard("7N/1b4N1/5R1k/6b1/KBp4p/5q2/6Q1/7n b - - 0 1");
//        cb.updateFen("e2d2");
//        Util.printBoard(cb.board,false);
//        cb = new ChessBoard(cb.fen);
//        Util.printBoard(cb.board,false);
//        System.out.println(cb.fen);

//        String tf = "6Q1";
//        char[] file = FenUtils.parseFile(tf);
//        String rf = FenUtils.getFileFen(file);
//        System.out.println(rf);

//        String tf = "7N/1b3RN1/7k/6b1/KBp4p/5q2/6Q1/7n w - - 0 1";
//        String move = "f7f6";
//        String rf = FenUtils.updateFen(tf,move);
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
