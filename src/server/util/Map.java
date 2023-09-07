package server.util;

public class Map {

    //flipping index for black pieces

    //rank = pieceIndex / 8
    //file = pieceIndex % 8
    //newRank = 7 - rank
    //newIndex = pieceIndex % 8 + (7 - pieceIndex / 8) * 8


    public static final int[] MIRRORED_MAP = new int[]{
            63, 62, 61, 60, 59, 58, 57, 56,
            55, 54, 53, 52, 51, 50, 49, 48,
            47, 46, 45, 44, 43, 42, 41, 40,
            39, 38, 37, 36, 35, 34, 33, 32,
            31, 30, 29, 28, 27, 26, 25, 24,
            23, 22, 21, 20, 19, 18, 17, 16,
            15, 14, 13, 12, 11, 10, 9, 8,
            7, 6, 5, 4, 3, 2, 1, 0
    };
    public static final float[] PAWN = new float[]

            {
                    5, 5, 5, 5, 5, 5, 5, 5,
                    2, 2, 3, 3.5f, 3.5f, 3, 2, 2,
                    1.5f, 1.5f, 1.5f, 2.5f, 2.5f, 2, 1.5f, 1.5f,
                    1, 1, 1, 2, 2, 1, 1, 1,
                    .5f, .5f, 2, 2, 2, .5f, .5f, .5f,
                    0, 0, 0, 1, 1, 0, 0, 0,
                    -0.3f, -0.3f, -0.3f, -0.55f, -0.55f, -0.3f, -0.3f, -0.3f,
                    0, 0, 0, 0, 0, 0, 0, 0
            };

    // knight positional score
    public static final float[] KNIGHT = new float[]
            {
                    -.5f, 0, 0, 0, 0, 0, 0, -.5f,
                    -.5f, 0, 0, 1, 1, 0, 0, -.5f,
                    -.5f, .5f, 2, 2, 2, 2, .5f, -.5f,
                    -.5f, 1, 2, 2.5f, 2.5f, 2, 1, -.5f,
                    -.5f, 1, 2, 2.5f, 2.5f, 2, 1, -.5f,
                    -.5f, .5f, 2, 1, 1, 2, .5f, -.5f,
                    -.5f, 0, 0, 0, 0, 0, 0, -.5f,
                    -.5f, -0.5f, 0, 0, 0, 0, -0.5f, -.5f
            };

    // bishop positional score
    public static final float[] BISHOP = new float[]
            {
                    0, 0, 0, 0, 0, 0, 0, 0,
                    0, 0, 0, 0, 0, 0, 0, 0,
                    0, 0, 0, 1, 1, 0, 0, 0,
                    0, 0, 1, 1.5f, 1.5f, 1, 0, 0,
                    0, 0, 1, 1.5f, 1.5f, 1, 0, 0,
                    0, 1, 0, 0, 0, 0, 1, 0,
                    0, 1.7f, 0, 0, 0, 0, 1.7f, 0,
                    0, 0, -1f, 0, 0, -1f, 0, 0

            };

    // rook positional score
    public static final float[] ROOK = new float[]
            {
                    4, 4, 4, 4, 4, 4, 4, 4,
                    4, 4, 4, 4, 4, 4, 4, 4,
                    0, 0, 1, 2, 2, 1, 0, 0,
                    0, 0, 1, 2, 2, 1, 0, 0,
                    0, 0, 1, 2, 2, 1, 0, 0,
                    0, 0, 1, 2, 2, 1, 0, 0,
                    0, 0, 1, 2, 2, 1, 0, 0,
                    0, 0, 0, 1.5f, 0, 1.5f, 0, 0

            };


}