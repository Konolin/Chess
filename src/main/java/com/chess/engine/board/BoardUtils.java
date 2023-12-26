/**
 * The {@code BoardUtils} class provides utility methods and constants for chess board operations.
 * It includes information about algebraic notation, board dimensions, and coordinate mappings.
 */

package com.chess.engine.board;

import com.google.common.collect.ImmutableMap;

import java.util.HashMap;
import java.util.Map;

public class BoardUtils {
    public static final int NUM_TILES = 64;
    public static final int NUM_TILES_PER_ROW = 8;
    public static final String[] ALGEBRAIC_NOTATION = initializeAlgebraicNotation();
    public static final Map<String, Integer> POSITION_TO_COORDINATE = initializePositionToCooridanteMap();
    public static final boolean[] FIRST_COLUMN = initColumn(0);
    public static final boolean[] SECOND_COLUMN = initColumn(1);
    public static final boolean[] SEVENTH_COLUMN = initColumn(6);
    public static final boolean[] EIGHTH_COLUMN = initColumn(7);

    public static final boolean[] EIGHTH_ROW = initRow(56);
    public static final boolean[] SEVENTH_ROW = initRow(48);
    public static final boolean[] FIFTH_ROW = initRow(32);
    public static final boolean[] THIRD_ROW = initRow(16);
    public static final boolean[] SECOND_ROW = initRow(8);
    public static final boolean[] FIRST_ROW = initRow(0);

    /**
     * Prevents instantiation of the {@code BoardUtils} class.
     * Instances of this class should not be created.
     */
    private BoardUtils() {
        throw new RuntimeException("Not instantiable.");
    }

    /**
     * Initializes an array indicating the values of a specific column on the chess board.
     *
     * @param columnNumber The column number to initialize.
     * @return An array indicating the values of the specified column.
     */
    private static boolean[] initColumn(int columnNumber) {
        final boolean[] column = new boolean[NUM_TILES];
        for (int i = columnNumber; i < NUM_TILES; i += NUM_TILES_PER_ROW) {
            column[i] = true;
        }
        return column;
    }

    /**
     * Initializes an array indicating the values of a specific row on the chess board.
     *
     * @param rowNumber The row number to initialize.
     * @return An array indicating the values of the specified row.
     */
    private static boolean[] initRow(int rowNumber) {
        final boolean[] row = new boolean[NUM_TILES];
        do {
            row[rowNumber++] = true;
        } while (rowNumber % NUM_TILES_PER_ROW != 0);
        return row;
    }

    /**
     * Initializes an array representing algebraic notation for each tile on the chess board.
     *
     * @return An array containing algebraic notation for each tile.
     */
    private static String[] initializeAlgebraicNotation() {
        return new String[]{
                "a8", "b8", "c8", "d8", "e8", "f8", "g8", "h8",
                "a7", "b7", "c7", "d7", "e7", "f7", "g7", "h7",
                "a6", "b6", "c6", "d6", "e6", "f6", "g6", "h6",
                "a5", "b5", "c5", "d5", "e5", "f5", "g5", "h5",
                "a4", "b4", "c4", "d4", "e4", "f4", "g4", "h4",
                "a3", "b3", "c3", "d3", "e3", "f3", "g3", "h3",
                "a2", "b2", "c2", "d2", "e2", "f2", "g2", "h2",
                "a1", "b1", "c1", "d1", "e1", "f1", "g1", "h1"
        };
    }

    /**
     * Initializes a mapping from algebraic position notation to board coordinates.
     *
     * @return An immutable map containing the mapping from position to coordinate.
     */
    private static Map<String, Integer> initializePositionToCooridanteMap() {
        final Map<String, Integer> positionCoordinate = new HashMap<>();
        for (int i = 0; i < NUM_TILES; i++) {
            positionCoordinate.put(ALGEBRAIC_NOTATION[i], i);
        }
        return ImmutableMap.copyOf(positionCoordinate);
    }

    /**
     * Checks if the given coordinate is within the valid bounds of the chess board.
     *
     * @param coordinate The coordinate to check.
     * @return {@code true} if the coordinate is valid, {@code false} otherwise.
     */
    public static boolean isValidCoordinate(final int coordinate) {
        return coordinate >= 0 && coordinate < NUM_TILES;
    }

    /**
     * Retrieves the board coordinate corresponding to a given algebraic position.
     *
     * @param position The algebraic position notation (e.g., "a1", "e5").
     * @return The board coordinate corresponding to the specified position.
     */
    public static int getCoordinateAtPosition(final String position) {
        return POSITION_TO_COORDINATE.get(position);
    }

    /**
     * Retrieves the algebraic position corresponding to a given board coordinate.
     *
     * @param coordinate The board coordinate.
     * @return The algebraic position notation corresponding to the specified
     */
    public static String getPositionAtCoordinate(final int coordinate) {
        return ALGEBRAIC_NOTATION[coordinate];
    }

    /**
     * Check if current game is over.
     * @param board The current board.
     * @return {@code true} if the game is over, {@code false} otherwise.
     */
    public static boolean isEndGame(final Board board) {
        return board.getCurrentPlayer().isInCheckMate() ||
                board.getCurrentPlayer().isInStalemate() ||
                board.getCurrentPlayer().getOpponent().isInCheckMate();
    }

    /**
     * Check if the current board is in a state of immediate threat.
     * @param board The current board.
     * @return {@code true} if the board is in a state of immediate threat, {@code false} otherwise.
     */
    public static boolean isThreatenedBoardImmediate(final Board board) {
        return board.getWhitePlayer().isInCheck() || board.getBlackPlayer().isInCheck();
    }
}
