/**
 * The {@code Queen} class represents the queen chess piece.
 * It extends the abstract Piece class and implements the behavior specific to a queen on a chessboard.
 * Queens can move horizontally, vertically, and diagonally across the board.
 */

package com.chess.engine.piece;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.board.MoveUtils.Line;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import java.util.*;

public class Queen extends Piece {
    /**
     * The candidate move vector coordinates for a queen piece.
     */
    private final static int[] CANDIDATE_MOVES_VECTOR_COORDINATES = {-9, -8, -7, -1, 1, 7, 8, 9};

    /**
     * The pre-computed legal moves for each tile coordinate on a chessboard for a queen piece.
     */
    private final static Map<Integer, Line[]> PRECOMPUTED_LEGAL_MOVES = computeLegalMoves();

    /**
     * Constructor for the Queen class.
     *
     * @param pieceAlliance The alliance (color) of the queen.
     * @param piecePosition The initial position of the queen on the board.
     */
    public Queen(final Alliance pieceAlliance, final int piecePosition) {
        super(piecePosition, pieceAlliance, PieceType.QUEEN, true);
    }

    /**
     * Constructor for the Queen class.
     *
     * @param pieceAlliance The alliance (color) of the queen.
     * @param piecePosition The initial position of the queen on the board.
     * @param isFirstMove   Whether it is the queen's first move.
     */
    public Queen(final Alliance pieceAlliance, final int piecePosition, final boolean isFirstMove) {
        super(piecePosition, pieceAlliance, PieceType.QUEEN, isFirstMove);
    }

    /**
     * Helper method to check if the queen is in the first or eighth column and the destination offset vector is invalid.
     *
     * @param currentPosition The current position of the queen.
     * @param candidateOffset The destination offset vector.
     * @return {@code true} if the queen is in the first or eighth column and the destination offset vector is invalid.
     */
    private static boolean isFirstOrEighthColumnExclusion(final int currentPosition, final int candidateOffset) {
        return BoardUtils.FIRST_COLUMN[currentPosition] && (candidateOffset == -9 || candidateOffset == -1 || candidateOffset == 7) ||
                BoardUtils.EIGHTH_COLUMN[currentPosition] && (candidateOffset == -7 || candidateOffset == 1 || candidateOffset == 9);
    }

    /**
     * Initializes and returns a mapping of tile coordinates to legal moves for the queen.
     *
     * @return An immutable mapping of tile coordinates to legal moves for the queen.
     */
    private static Map<Integer, Line[]> computeLegalMoves() {
        Map<Integer, Line[]> legalMoves = new HashMap<>();
        for (int position = 0; position < BoardUtils.NUM_TILES; position++) {
            List<Line> lines = new ArrayList<>();
            for (int offset : CANDIDATE_MOVES_VECTOR_COORDINATES) {
                Line line = new Line();
                int destination = position;
                while (BoardUtils.isValidCoordinate(destination)) {
                    if (isFirstOrEighthColumnExclusion(destination, offset)) {
                        break;
                    }
                    destination += offset;
                    if (BoardUtils.isValidCoordinate(destination)) {
                        line.addCoordinate(destination);
                    }
                }
                if (!line.isEmpty()) {
                    lines.add(line);
                }
            }
            if (!lines.isEmpty()) {
                legalMoves.put(position, lines.toArray(new Line[0]));
            }
        }
        return ImmutableMap.copyOf(legalMoves);
    }

    /**
     * Calculates the legal moves for the queen on the board.
     *
     * @param board The board on which the queen is placed.
     * @return A collection of the legal moves for the queen.
     */
    @Override
    public Collection<Move> calculateLegalMoves(final Board board) {
        final List<Move> legalMoves = new ArrayList<>();
        for (final Line line : PRECOMPUTED_LEGAL_MOVES.get(this.piecePosition)) {
            for (final int candidateDestinationCoordinate : line.getLineCoordinates()) {
                final Piece pieceAtDestination = board.getPiece(candidateDestinationCoordinate);
                if (pieceAtDestination == null) {
                    legalMoves.add(new Move.MajorMove(board, this, candidateDestinationCoordinate));
                } else {
                    if (this.pieceAlliance != pieceAtDestination.getPieceAlliance()) {
                        legalMoves.add(new Move.MajorAttackMove(board, this, candidateDestinationCoordinate,
                                pieceAtDestination));
                    }
                    break;
                }
            }
        }
        return ImmutableList.copyOf(legalMoves);
    }

    /**
     * Creates a new queen with an updated position after a move.
     *
     * @param move The move to be applied to the queen.
     * @return A new queen with the updated position.
     */
    @Override
    public Queen movePiece(final Move move) {
        return new Queen(move.getMovedPiece().getPieceAlliance(), move.getDestinationCoordinate());
    }

    @Override
    public String toString() {
        return PieceType.QUEEN.toString();
    }
}
