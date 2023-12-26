package com.tests.chess.engine.board;

import com.chess.engine.board.Move;
import com.chess.gui.Table;
import com.chess.pgn.FenUtilities;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestMoveGeneration {
    public int generateMovesTest(int depth) {
        if (depth == 0) {
            return 1;
        }

        Iterable<Move> legalMoves = Table.get().getGameBoard().getCurrentPlayer().getLegalMoves();
        int numMoves = 0;

        for (Move move : legalMoves) {
            Table.get().getGameBoard().getCurrentPlayer().makeMove(move);
            numMoves += generateMovesTest(depth - 1);
            Table.get().getGameBoard().getCurrentPlayer().undoMove(move);
        }

        return numMoves;
    }

    @Test
    public void testMoveGenerationStandardBoard() {
        Table.get().setChessBoard(FenUtilities.createGameFromFEN("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1"));

        int numMoves = generateMovesTest(1);
        assertEquals(20, numMoves);

        numMoves = generateMovesTest(2);
        assertEquals(400, numMoves);

//        numMoves = generateMovesTest(3);
//        assertEquals(8902, numMoves);

//        numMoves = generateMovesTest(4);
//        assertEquals(197281, numMoves);

//        numMoves = generateMovesTest(5);
//        assertEquals(4865609, numMoves);
    }

    @Test // https://www.chessprogramming.org/Perft_Results
    public void testMoveGenerationPosition2() {
        Table.get().setChessBoard(FenUtilities.createGameFromFEN("r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq - "));

        int numMoves = generateMovesTest(1);
        assertEquals(48, numMoves);

//        numMoves = generateMovesTest(2);
//        assertEquals(2039	, numMoves);

//        numMoves = generateMovesTest(3);
//        assertEquals(97862, numMoves);

//        numMoves = generateMovesTest(4);
//        assertEquals(4085603, numMoves);
    }

    @Test
    public void testMoveGenerationPosition3() {
        Table.get().setChessBoard(FenUtilities.createGameFromFEN("8/2p5/3p4/KP5r/1R3p1k/8/4P1P1/8 w - - "));

        int numMoves = generateMovesTest(1);
        assertEquals(14, numMoves);

        numMoves = generateMovesTest(2);
        assertEquals(191	, numMoves);

        numMoves = generateMovesTest(3);
        assertEquals(2812, numMoves);

        numMoves = generateMovesTest(4);
        assertEquals(43238, numMoves);
    }
}
