package com.bitboards;

public class MyBitBoard {
    private static ChessBitSet whiteKnights;
    private static ChessBitSet whiteBishops;
    private static ChessBitSet whiteRooks;
    private static ChessBitSet whiteQueens;
    private static ChessBitSet whiteKing;
    private static ChessBitSet whitePawns;
    private static ChessBitSet blackKnights;
    private static ChessBitSet blackBishops;
    private static ChessBitSet blackRooks;
    private static ChessBitSet blackQueens;
    private static ChessBitSet blackKing;
    private static ChessBitSet blackPawns;

    private static MyBitBoard INSTANCE = new MyBitBoard();

    private MyBitBoard() {
        whiteRooks = new ChessBitSet();
        whiteRooks.set(56);
        whiteRooks.set(63);

        whiteKnights = new ChessBitSet();
        whiteKnights.set(57);
        whiteKnights.set(62);

        whiteBishops = new ChessBitSet();
        whiteBishops.set(58);
        whiteBishops.set(61);

        whiteQueens = new ChessBitSet();
        whiteQueens.set(59);

        whiteKing = new ChessBitSet();
        whiteKing.set(60);

        whitePawns = new ChessBitSet();
        for (int i = 48; i < 56; i++) {
            whitePawns.set(i);
        }

        blackRooks = new ChessBitSet();
        blackRooks.set(0);
        blackRooks.set(7);

        blackKnights = new ChessBitSet();
        blackKnights.set(1);
        blackKnights.set(6);

        blackBishops = new ChessBitSet();
        blackBishops.set(2);
        blackBishops.set(5);

        blackQueens = new ChessBitSet();
        blackQueens.set(3);

        blackKing = new ChessBitSet();
        blackKing.set(4);

        blackPawns = new ChessBitSet();
        for (int i = 8; i < 16; i++) {
            blackPawns.set(i);
        }
    }

    public static MyBitBoard get() {
        return INSTANCE;
    }

    public String toString() {
        final char[] tiles = new char[64];

        final ChessBitSet allKnights = BitBoard.Piece.allKnights();
        for (int currentKnightLocation = allKnights.nextSetBit(0); currentKnightLocation >= 0;
             currentKnightLocation = allKnights.nextSetBit(currentKnightLocation + 1)) {
            tiles[currentKnightLocation] = 'N';
        }

        final ChessBitSet allBishops = BitBoard.Piece.allBishops();
        for (int currentBishopLocation = allBishops.nextSetBit(0); currentBishopLocation >= 0;
             currentBishopLocation = allBishops.nextSetBit(currentBishopLocation + 1)) {
            tiles[currentBishopLocation] = 'B';
        }

        final ChessBitSet allRooks = BitBoard.Piece.allRooks();
        for (int currentRookLocation = allRooks.nextSetBit(0); currentRookLocation >= 0;
             currentRookLocation = allRooks.nextSetBit(currentRookLocation + 1)) {
            tiles[currentRookLocation] = 'R';
        }

        final ChessBitSet allPawns = BitBoard.Piece.allPawns();
        for (int currentPawnLocation = allPawns.nextSetBit(0); currentPawnLocation >= 0;
             currentPawnLocation = allPawns.nextSetBit(currentPawnLocation + 1)) {
            tiles[currentPawnLocation] = 'P';
        }

        final ChessBitSet allQueens = BitBoard.Piece.allQueens();
        for (int currentQueenLocation = allQueens.nextSetBit(0); currentQueenLocation >= 0;
             currentQueenLocation = allQueens.nextSetBit(currentQueenLocation + 1)) {
            tiles[currentQueenLocation] = 'Q';
        }

        final ChessBitSet allKings = BitBoard.Piece.allKings();
        for (int currentKingLocation = allKings.nextSetBit(0); currentKingLocation >= 0;
             currentKingLocation = allKings.nextSetBit(currentKingLocation + 1)) {
            tiles[currentKingLocation] = 'K';
        }

        final StringBuilder result = new StringBuilder();
        for (int i = 0; i < tiles.length; i++) {
            if (tiles[i] != 0) {
                result.append(tiles[i]);
            } else {
                result.append("-");
            }
            result.append(" ");
            if ((i + 1) % 8 == 0) {
                result.append("\n");
            }
        }
        return result.toString();
    }


}
