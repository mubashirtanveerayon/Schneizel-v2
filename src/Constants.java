public class Constants {

    public static final String VERSION = "1.0-rw";

    public static final int ROWS = 8;

    public static final int COLUMNS = 8;

    public static final char WHITE = 'w';

    public static final char BLACK = 'b';

    public static final String WHITE_PIECE_CHAR = "PNBRQK";

    public static final char WHITE_PAWN = 'P';

    public static final char WHITE_KNIGHT = 'N';

    public static final char WHITE_BISHOP = 'B';

    public static final char WHITE_ROOK = 'R';

    public static final char WHITE_QUEEN = 'Q';

    public static final char WHITE_KING = 'K';

    public static final char BLACK_PAWN = 'p';

    public static final char BLACK_ROOK = 'r';

    public static final char BLACK_QUEEN = 'q';

    public static final char BLACK_KING = 'k';

    public static final char BLACK_KNIGHT = 'n';

    public static final char BLACK_BISHOP = 'b';

    public static final char EMPTY_SQUARE = '-';

    public static final float PAWN_VALUE = 1.5f;

    public static final float KNIGHT_VALUE = 3.0f;

    public static final float BISHOP_VALUE = 3.0f;

    public static final float ROOK_VALUE = 5.5f;

    public static final float QUEEN_VALUE = 8.5f;

    public static final float KING_VALUE = 1.0f;

    public static final String FILES = "abcdefgh";

    public static final String RANKS = "87654321";

    public static String KING_SIDE_CASTLING = "O-O";

    public static String QUEEN_SIDE_CASTLING = "O-O-O";

    //                                                                      index mustn't be modified
    public static final int[][] ALL_DIRECTIONS = {{1,0},{-1,0},{0,1},{0,-1},{1,1},{-1,1},{-1,-1},{1,-1}};

    //                                                                      index mustn't be modified
    public static final int[][] KNIGHT_DIRECTION= {{1, 2},{2,1},{2, -1}, {1, -2},  {-2, -1}, {-2, 1}, {-1, -2}, {-1, 2}};

    public static final  String STARTING_FEN="rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
//                                              0       1       2 3 4 5     6       7      8  9  10 11 12
}
