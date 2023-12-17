/**
 * The {@code Rook} class represents the rook chess piece.
 * It extends the abstract Piece class and implements the behavior specific to a rook on a chessboard.
 */

package com.chess.engine.piece;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.board.MoveUtils;
import com.chess.engine.board.MoveUtils.Line;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import java.util.*;

public class Rook extends Piece {
    /**
     * The candidate move vector coordinates for a rook.
     */
    private final static int[] CANDIDATE_MOVES_VECTOR_COORDINATES = {-8, -1, 1, 8};

    /**
     * The pre-computed legal moves for each tile coordinate on a chessboard for a rook
     */
    private final static Map<Integer, Line[]> PRECOMPUTED_LEGAL_MOVES = computeLegalMoves();

    /**
     * Constructor for a rook that initializes its position, alliance, and type.
     *
     * @param pieceAlliance the alliance of the rook
     * @param piecePosition the position of the rook
     */
    public Rook(final Alliance pieceAlliance, final int piecePosition) {
        super(piecePosition, pieceAlliance, PieceType.ROOK, true);
    }

    /**
     * Constructor for a rook that initializes its position, alliance, type, and whether it is its first move.
     *
     * @param pieceAlliance the alliance of the rook
     * @param piecePosition the position of the rook
     * @param isFirstMove   whether it is the rook's first move
     */
    public Rook(final Alliance pieceAlliance, final int piecePosition, final boolean isFirstMove) {
        super(piecePosition, pieceAlliance, PieceType.ROOK, isFirstMove);
    }

    /**
     * Helper method that checks if the rook is in the first or eighth column and the destination offset vector is invalid.
     *
     * @param currentPosition the current position of the rook
     * @param candidateOffset the destination offset vector
     * @return {@code true} if the rook is in the first or eighth column and the destination offset vector is invalid
     */
    private static boolean isFirstOrEighthColumnExclusion(final int currentPosition, final int candidateOffset) {
        return BoardUtils.FIRST_COLUMN[currentPosition] && candidateOffset == -1 ||
                BoardUtils.EIGHTH_COLUMN[currentPosition] && candidateOffset == 1;
    }

    /**
     * Initializes and returns a mapping of tile coordinates to legal moves for the rook.
     *
     * @return An immutable mapping of tile coordinates to legal moves for the rook
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
     * Calculates the legal moves for a rook on the board.
     *
     * @param board the board on which the rook is placed
     * @return a collection of all the legal moves for the rook
     */
    @Override
    public Collection<Move> calculateLegalMoves(final Board board) {
        final List<Move> legalMoves = new ArrayList<>();
        for (final MoveUtils.Line line : PRECOMPUTED_LEGAL_MOVES.get(this.piecePosition)) {
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
     * Moves the rook to the destination coordinate and returns a new rook.
     *
     * @param move the move to be made
     * @return {@code Rook} a new rook at the destination coordinate
     */
    @Override
    public Rook movePiece(final Move move) {
        return new Rook(move.getMovedPiece().getPieceAlliance(), move.getDestinationCoordinate(), false);
    }

    @Override
    public String toString() {
        return PieceType.ROOK.toString();
    }
}
