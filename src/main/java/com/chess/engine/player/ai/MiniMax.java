package com.chess.engine.player.ai;

import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.board.MoveUtils;
import com.chess.engine.piece.Piece;
import com.chess.engine.player.MoveTransition;

import java.util.Collection;
import java.util.Comparator;
import java.util.stream.Collectors;

public class MiniMax implements MoveStrategy {

    private final BoardEvaluator boardEvaluator;
    private final int searchDepth;
    private int positionsEvaluated;

    public MiniMax(int searchDepth) {
        this.boardEvaluator = new StandardBoardEvaluator();
        this.searchDepth = searchDepth;
        this.positionsEvaluated = 0;
    }

    private static boolean isEndgameScenario(final Board board) {
        return board.getCurrentPlayer().isInCheckMate() || board.getCurrentPlayer().isInStalemate();
    }

    @Override
    public String toString() {
        return "MiniMax";
    }

    @Override
    public Move execute(Board board) {
        final long startTime = System.currentTimeMillis();
        Move bestMove = MoveUtils.NULL_MOVE;
        int highestSeenValue = Integer.MIN_VALUE;
        int lowestSeenValue = Integer.MAX_VALUE;
        int currentValue;

        final int numLegalMoves = board.getCurrentPlayer().getLegalMoves().size();

        System.out.println(board.getCurrentPlayer() + "Thinking with depth = " + this.searchDepth);

        for (final Move move : sortLegalMoves(board.getCurrentPlayer().getLegalMoves())) {
            final MoveTransition moveTransition = board.getCurrentPlayer().makeMove(move);
            if (moveTransition.getMoveStatus().isDone()) {
                currentValue = board.getCurrentPlayer().getAlliance().isWhite() ?
                        min(moveTransition.getToBoard(), this.searchDepth - 1, highestSeenValue, Integer.MAX_VALUE) :
                        max(moveTransition.getToBoard(), this.searchDepth - 1, Integer.MIN_VALUE, lowestSeenValue);
                if (board.getCurrentPlayer().getAlliance().isWhite() && currentValue >= highestSeenValue) {
                    highestSeenValue = currentValue;
                    bestMove = move;
                } else if (board.getCurrentPlayer().getAlliance().isBlack() && currentValue <= lowestSeenValue) {
                    lowestSeenValue = currentValue;
                    bestMove = move;
                }
            }
        }

        final long executionTime = System.currentTimeMillis() - startTime;
        final double seconds = executionTime / 1000.0;
        final double rate = (seconds > 0) ? (numLegalMoves / seconds) : 0;

        System.out.println("Execution time: " + String.format("%.2f", seconds) + " s, legal moves: " + numLegalMoves +
                ", rate: " + String.format("%.2f", rate) + " moves/s");
        System.out.println("Positions evaluated: " + positionsEvaluated);


        return bestMove;
    }

    public int min(final Board board, final int depth, final int alpha, int beta) {
        positionsEvaluated++;
        if (depth == 0 || isEndgameScenario(board)) {
            return this.boardEvaluator.evaluate(board, depth);
        }
        int lowestSeenValue = Integer.MAX_VALUE;
        // rate every possible move for the current player
        for (final Move move : board.getCurrentPlayer().getLegalMoves()) {
            final MoveTransition moveTransition = board.getCurrentPlayer().makeMove(move);
            if (moveTransition.getMoveStatus().isDone()) {
                // execute move and check the next depth level
                final int currentValue = max(moveTransition.getToBoard(), depth - 1, alpha, beta);
                if (currentValue <= lowestSeenValue) {
                    lowestSeenValue = currentValue;
                }
                if (lowestSeenValue <= alpha) {
                    return lowestSeenValue;
                }
                beta = Math.min(beta, lowestSeenValue);
            }
        }
        return lowestSeenValue;
    }

    public int max(final Board board, final int depth, int alpha, final int beta) {
        positionsEvaluated++;
        if (depth == 0 || isEndgameScenario(board)) {
            return this.boardEvaluator.evaluate(board, depth);
        }
        int highestSeenValue = Integer.MIN_VALUE;
        // rate every possible move for the current player
        for (final Move move : board.getCurrentPlayer().getLegalMoves()) {
            final MoveTransition moveTransition = board.getCurrentPlayer().makeMove(move);
            if (moveTransition.getMoveStatus().isDone()) {
                // execute move and check the next depth level
                final int currentValue = min(moveTransition.getToBoard(), depth - 1, alpha, beta);
                if (currentValue >= highestSeenValue) {
                    highestSeenValue = currentValue;
                }
                if (highestSeenValue >= beta) {
                    return highestSeenValue;
                }
                alpha = Math.max(alpha, highestSeenValue);
            }
        }
        return highestSeenValue;
    }

    public Collection<Move> sortLegalMoves(Collection<Move> legalMoves) {
        return legalMoves.stream()
                .sorted(Comparator.comparingInt(this::calculateMoveValue).reversed())
                .collect(Collectors.toList());
    }

    // TODO - more work here
    private int calculateMoveValue(Move move) {
        int moveValue = 0;
        Piece movedPiece = move.getMovedPiece();
        Piece attackedPiece = move.getAttackedPiece();

        if (attackedPiece != null) {
            moveValue = 10 * attackedPiece.getPieceValue() - movedPiece.getPieceValue();
        }

        if (move.isPromotingMove()) {
            moveValue += Piece.PieceType.QUEEN.getPieceValue();
        }

        if (move.isCastlingMove()) {
            moveValue += 300;
        }

        return moveValue;
    }
}
