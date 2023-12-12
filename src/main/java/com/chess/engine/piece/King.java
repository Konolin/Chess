/**
 * The {@code King} class represents the king chess piece.
 * <p>
 * It extends the abstract Piece class and includes additional properties such as castling capabilities.
 */

package com.chess.engine.piece;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.board.Tile;
import com.google.common.collect.ImmutableList;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
public class King extends Piece {
    /**
     * Array of possible move coordinates for the king.
     */
    private final static int[] CANDIDATE_MOVE_COORDINATE = {-9, -8, -7, -1, 1, 7, 8, 9};

    private final boolean kingSideCastleCapable;
    private final boolean queenSideCastleCapable;
    private final boolean isCastled;

    /**
     * Constructs a King object with the specified properties.
     *
     * @param pieceAlliance            The alliance (color) of the king.
     * @param piecePosition            The current position of the king on the chess board.
     * @param kingSideCastleCapable    Indicates whether the king can castle on the king side.
     * @param queenSideCastleCapable   Indicates whether the king can castle on the queen side.
     */
    public King(final Alliance pieceAlliance, final int piecePosition, final boolean kingSideCastleCapable, final boolean queenSideCastleCapable) {
        super(piecePosition, pieceAlliance, PieceType.KING, true);
        this.isCastled = false;
        this.kingSideCastleCapable = kingSideCastleCapable;
        this.queenSideCastleCapable = queenSideCastleCapable;
    }

    /**
     * Constructs a King object with additional properties.
     *
     * @param pieceAlliance            The alliance (color) of the king.
     * @param piecePosition            The current position of the king on the chess board.
     * @param isFirstMove              Indicates whether it is the king's first move.
     * @param isCastled                Indicates whether the king has already castled.
     * @param kingSideCastleCapable    Indicates whether the king can castle on the king side.
     * @param queenSideCastleCapable   Indicates whether the king can castle on the queen side.
     */
    public King(final Alliance pieceAlliance, final int piecePosition, final boolean isFirstMove, final boolean isCastled, final boolean kingSideCastleCapable, final boolean queenSideCastleCapable) {
        super(piecePosition, pieceAlliance, PieceType.KING, isFirstMove);
        this.isCastled = isCastled;
        this.kingSideCastleCapable = kingSideCastleCapable;
        this.queenSideCastleCapable = queenSideCastleCapable;
    }

    private static boolean isFirstOrEighthColumnExclusion(final int currentPosition, final int candidateOffset) {
        return BoardUtils.FIRST_COLUMN[currentPosition] && (candidateOffset == -9 || candidateOffset == -1 || candidateOffset == 7) ||
                BoardUtils.EIGHTH_COLUMN[currentPosition] && (candidateOffset == -7 || candidateOffset == 1 || candidateOffset == 9);
    }

    /**
     * Calculates all legal moves for the king on the given chess board.
     *
     * @param board The current chess board.
     * @return A collection of legal moves for the king.
     */
    @Override
    public Collection<Move> calculateLegalMoves(final Board board) {
        final List<Move> legalMoves = new ArrayList<>();

        for (final int currentCandidateOffset : CANDIDATE_MOVE_COORDINATE) {
            final int candidateDestinationCoordinate = this.piecePosition + currentCandidateOffset;

            // Jump over the cases where the candidate offset is not valid
            if (isFirstOrEighthColumnExclusion(this.piecePosition, currentCandidateOffset)) {
                continue;
            }

            // Check if the candidate destination is in-bounds
            if (BoardUtils.isValidCoordinate(candidateDestinationCoordinate)) {
                final Tile candidateDestinationTile = board.getTileAtCoordinate(candidateDestinationCoordinate);

                if (!candidateDestinationTile.isTileOccupied()) {
                    // Make a normal move
                    legalMoves.add(new Move.MajorMove(board, this, candidateDestinationCoordinate));
                } else {
                    final Piece pieceAtDestination = candidateDestinationTile.getPieceOnTile();
                    final Alliance pieceAlliance = pieceAtDestination.getPieceAlliance();

                    if (this.pieceAlliance != pieceAlliance) {
                        // Make an attacking move if the next tile is occupied by an opponent piece
                        legalMoves.add(new Move.MajorAttackMove(board, this, candidateDestinationCoordinate, pieceAtDestination));
                    }
                }
            }
        }
        return ImmutableList.copyOf(legalMoves);
    }

    /**
     * Creates a new King object after making a move.
     *
     * @param move The move made by the king.
     * @return A new King object reflecting the move.
     */
    @Override
    public King movePiece(final Move move) {
        return new King(move.getMovedPiece().getPieceAlliance(), move.getDestinationCoordinate(), false, move.isCastlingMove(), false, false);
    }

    @Override
    public String toString() {
        return PieceType.KING.toString();
    }
}
