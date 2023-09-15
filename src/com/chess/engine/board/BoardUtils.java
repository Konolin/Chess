package com.chess.engine.board;

import java.util.HashMap;
import java.util.Map;

public class BoardUtils {
    public static final boolean[] FIRST_COLUMN = initColumn(0);
    public static final boolean[] SECOND_COLUMN = initColumn(1);
    public static final boolean[] SEVENTH_COLUMN = initColumn(6);
    public static final boolean[] EIGHTH_COLUMN = initColumn(7);

    public static final boolean[] SECOND_ROW = initRow(48);
    public static final boolean[] THIRD_ROW = initRow(32);
    public static final boolean[] FIFTH_ROW = initRow(16);
    public static final boolean[] SEVENTH_ROW = initRow(8);
    public static final boolean[] EIGHTH_ROW = initRow(0);;

    public static final Map<String, Integer> POSITION_TO_COORDINATE = initializePositionToCooridanteMap();
    public static final String[] ALGEBRAIC_NOTATION = initializeAlgebraicNotation();

    public static final int NUM_TILES = 64;

    public static final int NUM_TILES_PER_ROW = 8;
    private BoardUtils() {
        throw new RuntimeException("You cannot instantiate me!");
    }

    private static boolean[] initColumn(int columnNumber) {
        // Sets the corresponding values of a column to true
        final boolean[] column = new boolean[NUM_TILES];
        do {
            column[columnNumber] = true;
            columnNumber += NUM_TILES_PER_ROW;
        } while (columnNumber < NUM_TILES);
        return column;
    }

    // Sets the corresponding values of a row to true

    private static boolean[] initRow(int rowNumber) {
        final boolean[] row = new boolean[NUM_TILES];
        do {
            row[rowNumber] = true;
            rowNumber++;
        } while (rowNumber % NUM_TILES_PER_ROW != 0);
        return row;
    }

    private static String[] initializeAlgebraicNotation() {
        // TODO
        return new String[0];
    }

    private static Map<String, Integer> initializePositionToCooridanteMap() {
        // TODO
        Map<String, Integer> map = new HashMap<>();
        return map;
    }

    // check if the coordinate is not out of bounds
    public static boolean isValidCoordinate(final int coordinate) {
        return coordinate >= 0 && coordinate < NUM_TILES;
    }

    public static int getCoordinateAtPosition(final String position) {
        return POSITION_TO_COORDINATE.get(position);
    }

    // TODO in video ii int?
    public static String getPositionAtCoordinate(final int coordinate) {
        return ALGEBRAIC_NOTATION[coordinate];
    }
}
