/**
 * The {@code Bishop} class represents a bishop chess piece on the board.
 * It extends the abstract class Piece and defines the specific behavior of a bishop.
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

public class Bishop extends Piece {
    /**
     * The vector coordinates representing possible moves for a bishop.
     * The bishop can move diagonally in any direction, represented by these offsets.
     */
    private final static int[] CANDIDATE_MOVES_VECTOR_COORDINATES = {-9, -7, 7, 9};

    /**
     * Constructs a Bishop object with the specified alliance and position.
     *
     * @param pieceAlliance The alliance (color) of the bishop.
     * @param piecePosition The initial position of the bishop on the board.
     */
    public Bishop(final Alliance pieceAlliance,
                  final int piecePosition) {
        super(piecePosition, pieceAlliance, PieceType.BISHOP, true);
    }

    /**
     * Constructs a Bishop object with the specified alliance, position, and move history.
     *
     * @param pieceAlliance The alliance (color) of the bishop.
     * @param piecePosition The initial position of the bishop on the board.
     * @param isFirstMove   Flag indicating whether it's the bishop's first move.
     */
    public Bishop(final Alliance pieceAlliance,
                  final int piecePosition,
                  final boolean isFirstMove) {
        super(piecePosition, pieceAlliance, PieceType.BISHOP, isFirstMove);
    }

    private boolean isFirstOrEighthColumnExclusion(int destinationCoordinate, int candidateOffset) {
        return BoardUtils.FIRST_COLUMN[destinationCoordinate] && (candidateOffset == -9 || candidateOffset == 7) ||
                BoardUtils.EIGHTH_COLUMN[destinationCoordinate] && (candidateOffset == -7 || candidateOffset == 9);
    }

    /**
     * Calculates all legal moves for the bishop on the given board.
     *
     * @param board The current chess board.
     * @return A collection of legal moves for the bishop.
     */
    @Override
    public Collection<Move> calculateLegalMoves(final Board board) {
        final List<Move> legalMoves = new ArrayList<>();

        for (final int candidateCoordinateOffset : CANDIDATE_MOVES_VECTOR_COORDINATES) {
            int candidateDestinationCoordinate = this.piecePosition;

            // while candidate destination is in-bounds
            while (BoardUtils.isValidCoordinate(candidateDestinationCoordinate)) {
                // jump over the cases where the candidate vectors are not valid
                if (isFirstOrEighthColumnExclusion(candidateDestinationCoordinate, candidateCoordinateOffset)) {
                    break;
                }

                // calculate next candidate destination, check if it's in-bounds and decide the move type
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
                        break; // stop bishop from moving further after capturing or being blocked by friendly piece
                    }
                }
            }
        }
        return ImmutableList.copyOf(legalMoves);
    }


    /**
     * Moves the bishop to a new position based on the provided move.
     *
     * @param move The move to be executed.
     * @return A new Bishop object with the updated position.
     */
    @Override
    public Bishop movePiece(final Move move) {
        return new Bishop(move.getMovedPiece().getPieceAlliance(), move.getDestinationCoordinate());
    }

    @Override
    public String toString() {
        return PieceType.BISHOP.toString();
    }
}
