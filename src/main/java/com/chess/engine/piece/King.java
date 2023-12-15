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
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import lombok.Getter;

import java.util.*;

@Getter
public class King extends Piece {
    /**
     * Array of possible move coordinates for the king.
     */
    private final static int[] CANDIDATE_MOVE_COORDINATE = {-9, -8, -7, -1, 1, 7, 8, 9};

    /**
     * Precomputed legal moves for each tile coordinate on the board.
     */
    private final Map<Integer, int[]> PRECOMPUTED_LEGAL_MOVES = computeLegalMoves();

    private final boolean kingSideCastleCapable;
    private final boolean queenSideCastleCapable;
    private final boolean isCastled;

    /**
     * Constructs a King object with the specified properties.
     *
     * @param pieceAlliance          The alliance (color) of the king.
     * @param piecePosition          The current position of the king on the chess board.
     * @param kingSideCastleCapable  Indicates whether the king can castle on the king side.
     * @param queenSideCastleCapable Indicates whether the king can castle on the queen side.
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
     * @param pieceAlliance          The alliance (color) of the king.
     * @param piecePosition          The current position of the king on the chess board.
     * @param isFirstMove            Indicates whether it is the king's first move.
     * @param isCastled              Indicates whether the king has already castled.
     * @param kingSideCastleCapable  Indicates whether the king can castle on the king side.
     * @param queenSideCastleCapable Indicates whether the king can castle on the queen side.
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
     * Initializes and returns a mapping of tile coordinates to legal moves for the king.
     *
     * @return An immutable mapping of tile coordinates to legal moves for the king.
     */
    private static Map<Integer, int[]> computeLegalMoves() {
        Map<Integer, int[]> legalMoves = new HashMap<>();
        for (int position = 0; position < BoardUtils.NUM_TILES; position++) {
            int[] legalOffsets = new int[CANDIDATE_MOVE_COORDINATE.length];
            int legalOffsetsLength = 0;
            for (int offset : CANDIDATE_MOVE_COORDINATE) {
                if (isFirstOrEighthColumnExclusion(position, offset)) {
                    continue;
                }
                int destination = position + offset;
                if (BoardUtils.isValidCoordinate(destination)) {
                    legalOffsets[legalOffsetsLength++] = offset;
                }
            }
            if (legalOffsetsLength > 0) {
                legalMoves.put(position, Arrays.copyOf(legalOffsets, legalOffsetsLength));
            }
        }
        return ImmutableMap.copyOf(legalMoves);
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
        for (int offset : PRECOMPUTED_LEGAL_MOVES.get(this.piecePosition)) {
            final int candidateDestination = this.piecePosition + offset;
            final Piece pieceAtCandidatePosition = board.getPiece(candidateDestination);
            if (pieceAtCandidatePosition == null) {
                legalMoves.add(new Move.MajorMove(board, this, candidateDestination));
            } else {
                if (pieceAtCandidatePosition.getPieceAlliance() != this.pieceAlliance) {
                    legalMoves.add(new Move.MajorAttackMove(board, this, candidateDestination, pieceAtCandidatePosition));
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
