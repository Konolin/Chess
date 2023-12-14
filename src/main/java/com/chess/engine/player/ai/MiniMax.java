package com.chess.engine.player.ai;

import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import com.chess.engine.player.MoveTransition;

public class MiniMax implements MoveStrategy {
    private final BoardEvaluator boardEvaluator;
    private final int searchDepth;

    public MiniMax(int searchDepth) {
        this.boardEvaluator = new StandardBoardEvaluator();
        this.searchDepth = searchDepth;
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
        Move bestMove = null;
        int highestSeenValue = Integer.MIN_VALUE;
        int lowestSeenValue = Integer.MAX_VALUE;
        int currentValue;

        System.out.println(board.getCurrentPlayer() + "Thinking with depth = " + this.searchDepth);

        int numLegalMoves = board.getCurrentPlayer().getLegalMoves().size();

        for (final Move move : board.getCurrentPlayer().getLegalMoves()) {
            final MoveTransition moveTransition = board.getCurrentPlayer().makeMove(move);
            if (moveTransition.getMoveStatus().isDone()) {
                currentValue = board.getCurrentPlayer().getAlliance().isWhite() ?
                        min(moveTransition.getTransitionBoard(), this.searchDepth - 1, highestSeenValue, Integer.MAX_VALUE) :
                        max(moveTransition.getTransitionBoard(), this.searchDepth - 1, Integer.MIN_VALUE, lowestSeenValue);
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


        return bestMove;
    }

    public int min(final Board board, final int depth, final int alpha, int beta) {
        if (depth == 0 || isEndgameScenario(board)) {
            return this.boardEvaluator.evaluate(board, depth);
        }
        int lowestSeenValue = Integer.MAX_VALUE;
        // rate every possible move for the current player
        for (final Move move : board.getCurrentPlayer().getLegalMoves()) {
            final MoveTransition moveTransition = board.getCurrentPlayer().makeMove(move);
            if (moveTransition.getMoveStatus().isDone()) {
                // execute move and check the next depth level
                final int currentValue = max(moveTransition.getTransitionBoard(), depth - 1, alpha, beta);
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
        if (depth == 0 || isEndgameScenario(board)) {
            return this.boardEvaluator.evaluate(board, depth);
        }
        int highestSeenValue = Integer.MIN_VALUE;
        // rate every possible move for the current player
        for (final Move move : board.getCurrentPlayer().getLegalMoves()) {
            final MoveTransition moveTransition = board.getCurrentPlayer().makeMove(move);
            if (moveTransition.getMoveStatus().isDone()) {
                // execute move and check the next depth level
                final int currentValue = min(moveTransition.getTransitionBoard(), depth - 1, alpha, beta);
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
}
