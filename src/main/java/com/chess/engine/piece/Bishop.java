/**
 * The {@code Bishop} class represents a bishop chess piece on the board.
 * It extends the abstract class Piece and defines the specific behavior of a bishop.
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

public class Bishop extends Piece {
    /**
     * The vector coordinates representing possible moves for a bishop.
     * The bishop can move diagonally in any direction, represented by these offsets.
     */
    private final static int[] CANDIDATE_MOVES_VECTOR_COORDINATES = {-9, -7, 7, 9};

    /**
     * The precomputed legal moves for each tile coordinate on the board.
     * The bishop can move diagonally in any direction, represented by these offsets.
     */
    private final static Map<Integer, Line[]> PRECOMPUTED_LEGAL_MOVES = computeLegalMoves();

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

    private static boolean isFirstOrEighthColumnExclusion(int destinationCoordinate, int candidateOffset) {
        return BoardUtils.FIRST_COLUMN[destinationCoordinate] && (candidateOffset == -9 || candidateOffset == 7) ||
                BoardUtils.EIGHTH_COLUMN[destinationCoordinate] && (candidateOffset == -7 || candidateOffset == 9);
    }

    /**
     * Initializes and returns a mapping of tile coordinates to legal moves for the bishop.
     *
     * @return An immutable mapping of tile coordinates to legal moves for the bishop.
     */
    private static Map<Integer, Line[]> computeLegalMoves() {
        Map<Integer, Line[]> legalMoves = new HashMap<>();
        for (int position = 0; position < BoardUtils.NUM_TILES; position++) {
            List<Line> lines = new ArrayList<>();
            for (final int offset : CANDIDATE_MOVES_VECTOR_COORDINATES) {
                int destination = position;
                Line line = new Line();
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
     * Calculates all legal moves for the bishop on the given board.
     *
     * @param board The current chess board.
     * @return A collection of legal moves for the bishop.
     */
    @Override
    public Collection<Move> calculateLegalMoves(final Board board) {
        final List<Move> legalMoves = new ArrayList<>();
        for (final Line line : PRECOMPUTED_LEGAL_MOVES.get(this.piecePosition)) {
            for (final int candidatePosition : line.getLineCoordinates()) {
                final Piece pieceAtCandidatePosition = board.getPiece(candidatePosition);
                if (pieceAtCandidatePosition == null) {
                    legalMoves.add(new Move.MajorMove(board, this, candidatePosition));
                } else {
                    if (pieceAtCandidatePosition.getPieceAlliance() != this.pieceAlliance) {
                        legalMoves.add(new Move.MajorAttackMove(board, this, candidatePosition, pieceAtCandidatePosition));
                    }
                    break;
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
