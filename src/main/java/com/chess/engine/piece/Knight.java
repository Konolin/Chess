/**
 * The {@code Knight} class represents a knight chess piece.
 * It extends the abstract Piece class.
 */

package com.chess.engine.piece;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.board.Tile;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import java.util.*;

public class Knight extends Piece {
    /**
     * Array of possible move coordinates for the knight.
     */
    private final static int[] CANDIDATE_MOVE_COORDINATES = {-17, -15, -10, -6, 6, 10, 15, 17};

    /**
     * Precomputed legal moves for each tile coordinate on the board.
     */
    private final static Map<Integer, int[]> PRECOMPUTED_LEGAL_MOVES = computeLegalMoves();

    /**
     * Creates a new {@code Knight} instance with the specified alliance and position.
     *
     * @param pieceAlliance The alliance (BLACK or WHITE) of the knight.
     * @param piecePosition The initial position of the knight on the board.
     */
    public Knight(final Alliance pieceAlliance, final int piecePosition) {
        super(piecePosition, pieceAlliance, PieceType.KNIGHT, true);
    }

    /**
     * Creates a new {@code Knight} instance with the specified alliance, position, and move status.
     *
     * @param pieceAlliance The alliance (BLACK or WHITE) of the knight.
     * @param piecePosition The initial position of the knight on the board.
     * @param isFirstMove   A boolean indicating whether it is the knight's first move.
     */
    public Knight(final Alliance pieceAlliance, final int piecePosition, final boolean isFirstMove) {
        super(piecePosition, pieceAlliance, PieceType.KNIGHT, isFirstMove);
    }

    private static boolean isFirstColumnExclusion(final int currentPosition, final int candidateOffset) {
        return BoardUtils.FIRST_COLUMN[currentPosition] && (candidateOffset == -17 || candidateOffset == -10 ||
                candidateOffset == 6 || candidateOffset == 15);
    }

    private static boolean isSecondColumnExclusion(final int currentPosition, final int candidateOffset) {
        return BoardUtils.SECOND_COLUMN[currentPosition] && (candidateOffset == -10 || candidateOffset == 6);
    }

    private static boolean isSeventhColumnExclusion(final int currentPosition, final int candidateOffset) {
        return BoardUtils.SEVENTH_COLUMN[currentPosition] && (candidateOffset == -6 || candidateOffset == 10);
    }

    private static boolean isEighthColumnExclusion(final int currentPosition, final int candidateOffset) {
        return BoardUtils.EIGHTH_COLUMN[currentPosition] && (candidateOffset == -15 || candidateOffset == -6 ||
                candidateOffset == 10 || candidateOffset == 17);
    }

    /**
     * Computes the legal moves for each tile coordinate on the board.
     *
     * @return An immutable map of tile coordinates to legal moves.
     */
    private static Map<Integer, int[]> computeLegalMoves() {
        Map<Integer, int[]> legalMoves = new HashMap<>();
        for (int position = 0; position < BoardUtils.NUM_TILES; position++) {
            int[] legalOffsets = new int[CANDIDATE_MOVE_COORDINATES.length];
            int legalOffsetsLength = 0;
            for (int offset : CANDIDATE_MOVE_COORDINATES) {
                if (isFirstColumnExclusion(position, offset) ||
                        isSecondColumnExclusion(position, offset) ||
                        isSeventhColumnExclusion(position, offset) ||
                        isEighthColumnExclusion(position, offset)) {
                    continue;
                }
                int destination = position + offset;
                if (BoardUtils.isValidCoordinate(destination)) {
                    legalOffsets[legalOffsetsLength++] = destination;
                }
            }
            legalMoves.put(position, Arrays.copyOf(legalOffsets, legalOffsetsLength));
        }
        return ImmutableMap.copyOf(legalMoves);
    }

    /**
     * Calculates the legal moves for the knight on the given board.
     *
     * @param board The current state of the chess board.
     * @return A collection of legal moves for the knight.
     */
    @Override
    public Collection<Move> calculateLegalMoves(final Board board) {
        final List<Move> legalMoves = new ArrayList<>();
        for (int offset : PRECOMPUTED_LEGAL_MOVES.get(this.piecePosition)) {
            final int candidatePosition = this.piecePosition + offset;
            final Piece pieceAtCandidatePosition =  board.getPiece(candidatePosition);
            if (pieceAtCandidatePosition == null) {
                legalMoves.add(new Move.MajorMove(board, this, candidatePosition));
            } else {
                if (pieceAtCandidatePosition.getPieceAlliance() != this.pieceAlliance) {
                    legalMoves.add(new Move.MajorAttackMove(board, this, candidatePosition, pieceAtCandidatePosition));
                }
            }
        }
        return ImmutableList.copyOf(legalMoves);
    }

    /**
     * Creates a new {@code Knight} instance after a move.
     *
     * @param move The move to be applied to the knight.
     * @return A new knight with the updated position.
     */
    @Override
    public Knight movePiece(final Move move) {
        return new Knight(move.getMovedPiece().getPieceAlliance(), move.getDestinationCoordinate());
    }

    @Override
    public String toString() {
        return PieceType.KNIGHT.toString();
    }
}
