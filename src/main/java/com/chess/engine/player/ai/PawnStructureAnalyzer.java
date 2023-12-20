package com.chess.engine.player.ai;

import com.chess.engine.player.Player;

public class PawnStructureAnalyzer {
    private static final PawnStructureAnalyzer INSTANCE = new PawnStructureAnalyzer();
    private static final int ISOLATED_PAWN_PENALTY = -10;
    private static final int DOUBLED_PAWN_PENALTY = -10;
    private static final int PASSED_PAWN_BONUS = 20;

    private PawnStructureAnalyzer() {
    }

    public static PawnStructureAnalyzer get() {
        return INSTANCE;
    }

    public int pawnStructureScore(final Player player) {
        return calculateIsolatedPawnPenalty(player) +
               calculateDoubledPawnPenalty(player) +
               calculatePassedPawnBonus(player);
    }

    private static int calculateIsolatedPawnPenalty(final Player player) {
        return 0;
    }

    private static int calculateDoubledPawnPenalty(final Player player) {
        return 0;
    }

    private static int calculatePassedPawnBonus(final Player player) {
        return 0;
    }
}
