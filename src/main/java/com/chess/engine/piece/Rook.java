/**
 * The {@code Rook} class represents the rook chess piece.
 * It extends the abstract Piece class and implements the behavior specific to a rook on a chessboard.
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

public class Rook extends Piece {
    /**
     * offsets for the rook's possible moves
     */
    private final static int[] CANDIDATE_MOVES_VECTOR_COORDINATES = {-8, -1, 1, 8};

    /**
     * Constructor for a rook that initializes its position, alliance, and type.
     *
     * @param pieceAlliance   the alliance of the rook
     * @param piecePosition   the position of the rook
     */
    public Rook(final Alliance pieceAlliance, final int piecePosition) {
        super(piecePosition, pieceAlliance, PieceType.ROOK, true);
    }

    /**
     * Constructor for a rook that initializes its position, alliance, type, and whether it is its first move.
     *
     * @param pieceAlliance   the alliance of the rook
     * @param piecePosition   the position of the rook
     * @param isFirstMove     whether it is the rook's first move
     */
    public Rook(final Alliance pieceAlliance, final int piecePosition, final boolean isFirstMove) {
        super(piecePosition, pieceAlliance, PieceType.ROOK, isFirstMove);
    }

    /**
     * Helper method that checks if the rook is in the first or eighth column and the destination offset vector is invalid.
     *
     * @param currentPosition      the current position of the rook
     * @param candidateOffset       the destination offset vector
     * @return {@code true} if the rook is in the first or eighth column and the destination offset vector is invalid
     */
    private static boolean isFirstOrEighthColumnExclusion(final int currentPosition, final int candidateOffset) {
        return BoardUtils.FIRST_COLUMN[currentPosition] && candidateOffset == -1 ||
                BoardUtils.EIGHTH_COLUMN[currentPosition] && candidateOffset == 1;
    }

    /**
     * Calculates the legal moves for a rook on the board.
     *
     * @param board     the board on which the rook is placed
     * @return {@code Collection<Move>} a collection of all the legal moves for the rook
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
                        break; // stop rook from moving further after capturing or being blocked by another piece
                    }
                }
            }
        }
        return ImmutableList.copyOf(legalMoves);
    }

    /**
     * Moves the rook to the destination coordinate and returns a new rook.
     *
     * @param move  the move to be made
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
