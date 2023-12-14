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
import com.chess.engine.board.Tile;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Queen extends Piece {
    /**
     * The candidate move vector coordinates for a queen piece.
     */
    private final static int[] CANDIDATE_MOVES_VECTOR_COORDINATES = {-9, -8, -7, -1, 1, 7, 8, 9};

    /**
     * Constructor for the Queen class.
     *
     * @param pieceAlliance   The alliance (color) of the queen.
     * @param piecePosition   The initial position of the queen on the board.
     */
    public Queen(final Alliance pieceAlliance, final int piecePosition) {
        super(piecePosition, pieceAlliance, PieceType.QUEEN, true);
    }

    /**
     * Constructor for the Queen class.
     *
     * @param pieceAlliance   The alliance (color) of the queen.
     * @param piecePosition   The initial position of the queen on the board.
     * @param isFirstMove     Whether it is the queen's first move.
     */
    public Queen(final Alliance pieceAlliance, final int piecePosition, final boolean isFirstMove) {
        super(piecePosition, pieceAlliance, PieceType.QUEEN, isFirstMove);
    }

    /**
     * Helper method to check if the queen is in the first or eighth column and the destination offset vector is invalid.
     *
     * @param currentPosition   The current position of the queen.
     * @param candidateOffset   The destination offset vector.
     * @return                  {@code true} if the queen is in the first or eighth column and the destination offset vector is invalid.
     */
    private static boolean isFirstOrEighthColumnExclusion(final int currentPosition, final int candidateOffset) {
        return BoardUtils.FIRST_COLUMN[currentPosition] && (candidateOffset == -9 || candidateOffset == -1 || candidateOffset == 7) ||
                BoardUtils.EIGHTH_COLUMN[currentPosition] && (candidateOffset == -7 || candidateOffset == 1 || candidateOffset == 9);
    }

    /**
     * Calculates the legal moves for the queen on the board.
     *
     * @param board The board on which the queen is placed.
     * @return      A collection of the legal moves for the queen.
     */
    @Override
    public Collection<Move> calculateLegalMoves(final Board board) {
        final List<Move> legalMoves = new ArrayList<>();

        for (final int candidateCoordinateOffset : CANDIDATE_MOVES_VECTOR_COORDINATES) {
            int candidateDestinationCoordinate = this.piecePosition;

            // while next position is in-bounds
            while (BoardUtils.isValidCoordinate(candidateDestinationCoordinate)) {
                // jump over the cases where the destination offset vector is not valid
                if (isFirstOrEighthColumnExclusion(candidateDestinationCoordinate, candidateCoordinateOffset)) {
                    break;
                }

                // calculate next position, check if in-bounds and decide the move type
                candidateDestinationCoordinate += candidateCoordinateOffset;
                if (BoardUtils.isValidCoordinate(candidateDestinationCoordinate)) {
                    final Tile candidateDestinationTile = board.getTileAtCoordinate(candidateDestinationCoordinate);

                    if (!candidateDestinationTile.isTileOccupied()) {
                        // make normal move
                        legalMoves.add(new Move.MajorMove(board, this, candidateDestinationCoordinate));
                    } else {
                        final Piece pieceAtDestination = candidateDestinationTile.getPieceOnTile();
                        final Alliance pieceAlliance = pieceAtDestination.getPieceAlliance();

                        if (this.pieceAlliance != pieceAlliance) {
                            // make attacking move if next tile is occupied by opponent piece
                            legalMoves.add(new Move.MajorAttackMove(board, this, candidateDestinationCoordinate, pieceAtDestination));
                        }
                        break; // stop the queen from moving further after capturing or being blocked by friendly piece
                    }
                }
            }
        }
        return ImmutableList.copyOf(legalMoves);
    }

    /**
     * Creates a new queen with an updated position after a move.
     *
     * @param move  The move to be applied to the queen.
     * @return      A new queen with the updated position.
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
