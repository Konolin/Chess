package com.chess.engine.board;

import java.util.ArrayList;
import java.util.List;

public class MoveUtils {
    public static final Move NULL_MOVE = new Move.NullMove();

    /**
     *  A {@code Line} represents a line of coordinates on the chess board.
     */
    public static class Line {
        private final List<Integer> coordinates;

        public Line() {
            this.coordinates = new ArrayList<>();
        }

        public void addCoordinate(int coordinate) {
            this.coordinates.add(coordinate);
        }

        public List<Integer> getLineCoordinates() {
            return this.coordinates;
        }

        public boolean isEmpty() {
            return this.coordinates.isEmpty();
        }
    }
}
