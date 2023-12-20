package com.chess.engine.player.ai;

import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import com.chess.engine.piece.Piece;
import com.chess.engine.player.Player;

public final class StandardBoardEvaluator implements BoardEvaluator {
    private static final int CHECK_BONUS = 50;
    private static final int CHECK_MATE_BONUS = 10000;
    private static final int DEPTH_BONUS = 100;
    private static final int CASTLE_BONUS = 60;
    private static int ATTACK_MULTIPLIER = 2;
    private static int MOBILITY_MULTIPLIER = 7;

    @Override
    public int evaluate(final Board board, final int depth) {
        return scorePlayer(board.getWhitePlayer(), depth) - scorePlayer(board.getBlackPlayer(), depth);
    }

    private int scorePlayer(final Player player, final int depth) {
        return pieceValue(player) +
                mobility(player) +
                check(player) +
                checkmate(player, depth) +
                castled(player) +
                attacks(player);
    }

    private static int castled(final Player player) {
        return player.isCastled() ? CASTLE_BONUS : 0;
    }

    private static int checkmate(final Player player, int depth) {
        return player.getOpponent().isInCheckMate() ? CHECK_MATE_BONUS * depthBonus(depth) : 0;
    }

    private static int depthBonus(int depth) {
        return depth == 0 ? 1 : DEPTH_BONUS * depth;
    }

    private static int check(final Player player) {
        return player.getOpponent().isInCheck() ? CHECK_BONUS : 0;
    }

    private static int mobility(final Player player) {
        final int mobilityRatio = player.getLegalMoves().size() * 10 / player.getOpponent().getLegalMoves().size();
        return MOBILITY_MULTIPLIER * mobilityRatio;
    }

    private static int pieceValue(final Player player) {
        int pieceValueScore = 0;
        for (final Piece piece : player.getActivePieces()) {
            pieceValueScore += piece.getPieceValue();
        }
        return pieceValueScore;
    }

    private int attacks(final Player player) {
        int attackScore = 0;
        for (final Move move : player.getLegalMoves()) {
            if (move.isAttack()) {
                if (move.getAttackedPiece().getPieceValue() > move.getMovedPiece().getPieceValue()) {
                    attackScore++;
                }
            }
        }
        return attackScore * ATTACK_MULTIPLIER;
    }

    private static int pawnStructure(final Player player) {
        return PawnStructureAnalyzer.get().pawnStructureScore(player);
    }
}
