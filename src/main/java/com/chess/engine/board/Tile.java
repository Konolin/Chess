/**
 * The {@code Board} class represents a single tile on a chess board. Tiles can be either empty or occupied by a piece.
 * This class is abstract and has two concrete implementations: EmptyTile and OccupiedTile.
 */

package com.chess.engine.board;

import com.chess.engine.piece.Piece;
import com.google.common.collect.ImmutableMap;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public abstract class Tile {
    private static final Map<Integer, EmptyTile> EMPTY_TILES_CACHE = createAllPossibleEmptyTiles();
    protected final int tileCoordinate;

    private Tile(final int tileCoordinate) {
        this.tileCoordinate = tileCoordinate;
    }

    /**
     * Creates a map of size 64 containing only EmptyTile objects and a corresponding Integer index.
     *
     * @return A map of empty tiles.
     */
    private static Map<Integer, EmptyTile> createAllPossibleEmptyTiles() {
        final Map<Integer, EmptyTile> emptyTileMap = new HashMap<>();
        for (int i = 0; i < BoardUtils.NUM_TILES; i++) {
            emptyTileMap.put(i, new EmptyTile(i));
        }
        return ImmutableMap.copyOf(emptyTileMap);
    }

    /**
     * Creates a Tile object based on the given tile coordinate and piece.
     *
     * @param tileCoordinate The coordinate of the tile.
     * @param piece          The piece on the tile. If null, returns an empty tile from the cache.
     * @return Tile object representing the specified coordinate and piece.
     */
    public static Tile createTile(final int tileCoordinate, final Piece piece) {
        return piece != null ? new OccupiedTile(tileCoordinate, piece) : EMPTY_TILES_CACHE.get(tileCoordinate);
    }

    /**
     * Checks if the tile is occupied by a piece.
     *
     * @return True if the tile is occupied, false otherwise.
     */
    public abstract boolean isTileOccupied();

    /**
     * Gets the piece on the tile.
     *
     * @return The piece on the tile, or null if the tile is empty.
     */
    public abstract Piece getPieceOnTile();

    /**
     * Represents an empty tile on the chess board.
     */
    public static final class EmptyTile extends Tile {
        private EmptyTile(final int tileCoordinate) {
            super(tileCoordinate);
        }

        @Override
        public String toString() {
            return "-";
        }

        @Override
        public boolean isTileOccupied() {
            return false;
        }

        @Override
        public Piece getPieceOnTile() {
            return null;
        }
    }

    /**
     * Represents an occupied tile on the chess board with a piece.
     */
    @Getter
    public static final class OccupiedTile extends Tile {
        private final Piece pieceOnTile;

        private OccupiedTile(final int tileCoordinate, final Piece pieceOnTile) {
            super(tileCoordinate);
            this.pieceOnTile = pieceOnTile;
        }

        @Override
        public String toString() {
            // black pieces <=> lower case; white pieces <=> upper case
            return getPieceOnTile().getPieceAlliance().isBlack() ? getPieceOnTile().toString().toLowerCase() : getPieceOnTile().toString();
        }

        @Override
        public boolean isTileOccupied() {
            return true;
        }
    }
}