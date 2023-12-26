/**
 * The {@code Board} class represents a chess board.
 * It includes methods for creating a standard chess board, calculating active pieces, and handling legal moves.
 */

package com.chess.engine.board;

import com.chess.engine.Alliance;
import com.chess.engine.piece.*;
import com.chess.engine.player.BlackPlayer;
import com.chess.engine.player.Player;
import com.chess.engine.player.WhitePlayer;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import lombok.Getter;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Board {
    private final List<Tile> gameBoard;
    @Getter
    private final Collection<Piece> whitePieces;
    @Getter
    private final Collection<Piece> blackPieces;

    @Getter
    private final WhitePlayer whitePlayer;
    @Getter
    private final BlackPlayer blackPlayer;
    @Getter
    private final Player currentPlayer;

    @Getter
    private final Pawn enPassantPawn;

    @Getter
    private final Move transitionMove;

    /**
     * Constructs a new {@code Board} using the provided {@code Builder}.
     *
     * @param builder The {@code Builder} used to construct the board.
     */
    private Board(final Builder builder) {
        this.gameBoard = createGameBoard(builder);
        this.whitePieces = calculateActivePieces(this.gameBoard, Alliance.WHITE);
        this.blackPieces = calculateActivePieces(this.gameBoard, Alliance.BLACK);
        this.enPassantPawn = builder.enPassantPawn;

        final Collection<Move> whiteStandardMoves = calculateLegalMoves(this.whitePieces);
        final Collection<Move> blackStandardMoves = calculateLegalMoves(this.blackPieces);

        this.whitePlayer = new WhitePlayer(this, whiteStandardMoves, blackStandardMoves);
        this.blackPlayer = new BlackPlayer(this, whiteStandardMoves, blackStandardMoves);
        this.currentPlayer = builder.nextMoveMaker.choosePlayer(this.whitePlayer, this.blackPlayer);

        this.transitionMove = builder.transitionMove != null ? builder.transitionMove : MoveUtils.NULL_MOVE;
    }

    /**
     * Calculates the active pieces on the game board belonging to the specified alliance.
     *
     * @param gameBoard The list of tiles representing the game board.
     * @param alliance  The alliance of the pieces to be considered.
     * @return An immutable collection of active pieces belonging to the specified alliance.
     * @throws NullPointerException If any tile on the game board is occupied, but the associated piece is null.
     */
    private static Collection<Piece> calculateActivePieces(final List<Tile> gameBoard, final Alliance alliance) {
        final List<Piece> activePieces = gameBoard.stream()
                .filter(Tile::isTileOccupied)
                .map(Tile::getPieceOnTile)
                .filter(piece -> piece != null && piece.getPieceAlliance() == alliance)
                .collect(Collectors.toList());
        return ImmutableList.copyOf(activePieces);
    }


    /**
     * Creates the game board based on the configuration provided by the {@code Builder}.
     * Each tile on the board is initialized with the corresponding piece
     * or with an empty tile if no piece is configured for that position.
     *
     * @param builder The {@code Builder} containing the board configuration.
     * @return A list of tiles representing the game board.
     * @throws NullPointerException If the builder's configuration contains a null value at any position.
     */
    private static List<Tile> createGameBoard(final Builder builder) {
        final Tile[] tiles = new Tile[BoardUtils.NUM_TILES];
        for (int i = 0; i < BoardUtils.NUM_TILES; i++) {
            tiles[i] = Tile.createTile(i, builder.boardConfig.get(i));
        }
        return ImmutableList.copyOf(tiles);
    }

    /**
     * Creates a standard chess board with pieces in their initial positions.
     * The initial player to move is set to be the white player.
     *
     * @return A standard chess board.
     */
    public static Board createStandardBoard() {
        final Builder builder = new Builder();
        // Add black pieces to the board.
        builder.setPieceAtPosition(new Rook(Alliance.BLACK, 0));
        builder.setPieceAtPosition(new Knight(Alliance.BLACK, 1));
        builder.setPieceAtPosition(new Bishop(Alliance.BLACK, 2));
        builder.setPieceAtPosition(new Queen(Alliance.BLACK, 3));
        builder.setPieceAtPosition(new King(Alliance.BLACK, 4, true, true));
        builder.setPieceAtPosition(new Bishop(Alliance.BLACK, 5));
        builder.setPieceAtPosition(new Knight(Alliance.BLACK, 6));
        builder.setPieceAtPosition(new Rook(Alliance.BLACK, 7));
        for (int i = 0; i < 8; i++) {
            builder.setPieceAtPosition(new Pawn(Alliance.BLACK, i + 8));
        }

        // Add white pieces to the board.
        for (int i = 0; i < 8; i++) {
            builder.setPieceAtPosition(new Pawn(Alliance.WHITE, i + 48));
        }
        builder.setPieceAtPosition(new Rook(Alliance.WHITE, 56));
        builder.setPieceAtPosition(new Knight(Alliance.WHITE, 57));
        builder.setPieceAtPosition(new Bishop(Alliance.WHITE, 58));
        builder.setPieceAtPosition(new Queen(Alliance.WHITE, 59));
        builder.setPieceAtPosition(new King(Alliance.WHITE, 60, true, true));
        builder.setPieceAtPosition(new Bishop(Alliance.WHITE, 61));
        builder.setPieceAtPosition(new Knight(Alliance.WHITE, 62));
        builder.setPieceAtPosition(new Rook(Alliance.WHITE, 63));

        // Set the initial player to move as the white player.
        builder.setMoveMaker(Alliance.WHITE);
        return builder.build();
    }

    /**
     * Returns a string representation of the current board state.
     * Each tile on the board is represented by its text representation.
     * The board is formatted with ranks and files for better readability.
     *
     * @return A string representing the current board.
     */
    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        for (int i = 0; i < BoardUtils.NUM_TILES; i++) {
            final String tileText = this.gameBoard.get(i).toString();
            builder.append(String.format("%3s", tileText));
            if (((i + 1) % BoardUtils.NUM_TILES_PER_ROW) == 0) {
                builder.append("\n");
            }
        }
        return builder.toString();
    }

    /**
     * Retrieves all pieces on the board, both white and black.
     *
     * @return A collection containing all pieces on the board.
     */
    public Collection<Piece> getAllPieces() {
        return ImmutableList.copyOf(
                Stream.concat(whitePieces.stream(), blackPieces.stream()).collect(Collectors.toList()));
    }

    /**
     * Retrieves all legal moves of a given collection of pieces
     *
     * @param pieces The pieces for witch the legal moves are calculated
     * @return A collection of legal moves for the provided collection
     */
    private Collection<Move> calculateLegalMoves(Collection<Piece> pieces) {
        return ImmutableList.copyOf(pieces.stream()
                .flatMap(piece -> piece.calculateLegalMoves(this).stream())
                .collect(Collectors.toList()));
    }

    /**
     * Retrieves the tile at the specified coordinate on the game board.
     *
     * @param tileCoordinate The specified coordinate of the wanted tile
     * @return The tile at the specified coordinate
     * @throws IndexOutOfBoundsException If the provided coordinate is not within the valid range.
     */
    public Tile getTileAtCoordinate(final int tileCoordinate) {
        return gameBoard.get(tileCoordinate);
    }

    /**
     * Retrieves all legal moves for both white and black players.
     *
     * @return An iterable containing all legal moves.
     */
    public Iterable<Move> getAllLegalMoves() {
        return Iterables.unmodifiableIterable(
                Iterables.concat(this.whitePlayer.getLegalMoves(), this.blackPlayer.getLegalMoves()));
    }

    public Piece getPiece(final int position) {
        return this.gameBoard.get(position).getPieceOnTile();
    }

    /**
     * The {@code Builder} class is responsible for constructing instances of the {@code Board} class.
     * It allows for configuring the initial state of the board before creating an immutable {@code Board} instance.
     */
    public static class Builder {
        private final Map<Integer, Piece> boardConfig;
        private Alliance nextMoveMaker;
        private Pawn enPassantPawn;
        private Move transitionMove;

        /**
         * Constructs a new {@code Builder} with an empty board configuration.
         */
        public Builder() {
            this.boardConfig = new HashMap<>();
        }

        /**
         * Places a piece at the specified position on the board.
         *
         * @param piece The piece to be placed on the board.
         * @return The current builder instance for method chaining.
         */
        public Builder setPieceAtPosition(final Piece piece) {
            this.boardConfig.put(piece.getPiecePosition(), piece);
            return this;
        }

        /**
         * Sets the alliance of the player to make the next move on the board.
         *
         * @param nextMoveMaker The alliance of the player making the next move.
         * @return The current builder instance for method chaining.
         */
        public Builder setMoveMaker(final Alliance nextMoveMaker) {
            this.nextMoveMaker = nextMoveMaker;
            return this;
        }

        /**
         * Sets the pawn that is eligible for en passant capture on the board.
         *
         * @param enPassantPawn The pawn eligible for en passant capture.
         * @return The current builder instance for method chaining.
         */
        public Builder setEnPassantPawn(Pawn enPassantPawn) {
            this.enPassantPawn = enPassantPawn;
            return this;
        }

        /**
         * Sets the move that resulted in the current board state.
         *
         * @param transitionMove The move that resulted in the current board state.
         * @return The current builder instance for method chaining.
         */
        public Builder setTransitionMove(Move transitionMove) {
            this.transitionMove = transitionMove;
            return this;
        }

        /**
         * Builds and returns an immutable instance of the {@code Board} class based on the current configuration.
         *
         * @return An immutable instance of the {@code Board} class.
         */
        public Board build() {
            return new Board(this);
        }
    }
}
