package com.tests.chess.engine.board;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.Board.Builder;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.board.Move.MoveFactory;
import com.chess.engine.board.MoveUtils;
import com.chess.engine.piece.*;
import com.chess.engine.player.MoveTransition;
import com.google.common.collect.Iterables;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestBoard {
    private static int calculatedActivesFor(final Board board, final Alliance alliance) {
        int count = 0;
        for (final Piece p : board.getAllPieces()) {
            if (p.getPieceAlliance().equals(alliance)) {
                count++;
            }
        }
        return count;
    }

    @Test
    public void initialBoard() {
        final Board board = Board.createStandardBoard();
        assertEquals(board.getCurrentPlayer().getLegalMoves().size(), 20);
        assertEquals(board.getCurrentPlayer().getOpponent().getLegalMoves().size(), 20);
        assertFalse(board.getCurrentPlayer().isInCheck());
        assertFalse(board.getCurrentPlayer().isInCheckMate());
        assertFalse(board.getCurrentPlayer().isCastled());
        assertTrue(board.getCurrentPlayer().isKingSideCastleCapable());
        assertTrue(board.getCurrentPlayer().isQueenSideCastleCapable());
        assertEquals(board.getCurrentPlayer(), board.getWhitePlayer());
        assertEquals(board.getCurrentPlayer().getOpponent(), board.getBlackPlayer());
        assertFalse(board.getCurrentPlayer().getOpponent().isInCheck());
        assertFalse(board.getCurrentPlayer().getOpponent().isInCheckMate());
        assertFalse(board.getCurrentPlayer().getOpponent().isCastled());
        assertTrue(board.getCurrentPlayer().getOpponent().isKingSideCastleCapable());
        assertTrue(board.getCurrentPlayer().getOpponent().isQueenSideCastleCapable());
        assertEquals("White", board.getWhitePlayer().toString());
        assertEquals("Black", board.getBlackPlayer().toString());

        final Iterable<Piece> allPieces = board.getAllPieces();
        final Iterable<Move> allMoves = Iterables.concat(board.getWhitePlayer().getLegalMoves(), board.getBlackPlayer().getLegalMoves());
        for(final Move move : allMoves) {
            assertFalse(move.isAttack());
            assertFalse(move.isCastlingMove());
            assertEquals(MoveUtils.exchangeScore(move), 1);
        }

        assertEquals(Iterables.size(allMoves), 40);
        assertEquals(Iterables.size(allPieces), 32);
        assertFalse(BoardUtils.isEndGame(board));
        assertFalse(BoardUtils.isThreatenedBoardImmediate(board));
        assertNull(board.getPiece(35));
    }

    @Test
    public void testPlainKingMove() {
        final Builder builder = new Builder();
        // Black Layout
        builder.setPieceAtPosition(new King(Alliance.BLACK, 4, false, false));
        builder.setPieceAtPosition(new Pawn(Alliance.BLACK, 12));
        // White Layout
        builder.setPieceAtPosition(new Pawn(Alliance.WHITE, 52));
        builder.setPieceAtPosition(new King(Alliance.WHITE, 60, false, false));
        builder.setMoveMaker(Alliance.WHITE);
        // Set the current player
        final Board board = builder.build();

        assertEquals(board.getWhitePlayer().getLegalMoves().size(), 6);
        assertEquals(board.getBlackPlayer().getLegalMoves().size(), 6);
        assertFalse(board.getCurrentPlayer().isInCheck());
        assertFalse(board.getCurrentPlayer().isInCheckMate());
        assertFalse(board.getCurrentPlayer().getOpponent().isInCheck());
        assertFalse(board.getCurrentPlayer().getOpponent().isInCheckMate());
        assertEquals(board.getCurrentPlayer(), board.getWhitePlayer());
        assertEquals(board.getCurrentPlayer().getOpponent(), board.getBlackPlayer());
//        BoardEvaluator evaluator = StandardBoardEvaluator.get();
//        System.out.println(evaluator.evaluate(board, 0));
//        assertEquals(StandardBoardEvaluator.get().evaluate(board, 0), 0);
//
//        final Move move = MoveFactory.createMove(board, BoardUtils.INSTANCE.getCoordinateAtPosition("e1"),
//                BoardUtils.INSTANCE.getCoordinateAtPosition("f1"));
//
//        final MoveTransition moveTransition = board.getCurrentPlayer()
//                .makeMove(move);
//
//        assertEquals(moveTransition.getTransitionMove(), move);
//        assertEquals(moveTransition.getFromBoard(), board);
//        assertEquals(moveTransition.getToBoard().getCurrentPlayer(), moveTransition.getToBoard().getBlackPlayer());
//
//        assertTrue(moveTransition.getMoveStatus().isDone());
//        assertEquals(moveTransition.getToBoard().getWhitePlayer().getPlayerKing().getPiecePosition(), 61);
//        System.out.println(moveTransition.getToBoard());
    }

    @Test
    public void testBoardConsistency() {
        final Board board = Board.createStandardBoard();
        assertEquals(board.getCurrentPlayer(), board.getWhitePlayer());

        final MoveTransition t1 = board.getCurrentPlayer()
                .makeMove(MoveFactory.createMove(board, BoardUtils.getCoordinateAtPosition("e2"),
                        BoardUtils.getCoordinateAtPosition("e4")));
        final MoveTransition t2 = t1.getToBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t1.getToBoard(), BoardUtils.getCoordinateAtPosition("e7"),
                        BoardUtils.getCoordinateAtPosition("e5")));

        final MoveTransition t3 = t2.getToBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t2.getToBoard(), BoardUtils.getCoordinateAtPosition("g1"),
                        BoardUtils.getCoordinateAtPosition("f3")));
        final MoveTransition t4 = t3.getToBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t3.getToBoard(), BoardUtils.getCoordinateAtPosition("d7"),
                        BoardUtils.getCoordinateAtPosition("d5")));

        final MoveTransition t5 = t4.getToBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t4.getToBoard(), BoardUtils.getCoordinateAtPosition("e4"),
                        BoardUtils.getCoordinateAtPosition("d5")));
        final MoveTransition t6 = t5.getToBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t5.getToBoard(), BoardUtils.getCoordinateAtPosition("d8"),
                        BoardUtils.getCoordinateAtPosition("d5")));

        final MoveTransition t7 = t6.getToBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t6.getToBoard(), BoardUtils.getCoordinateAtPosition("f3"),
                        BoardUtils.getCoordinateAtPosition("g5")));
        final MoveTransition t8 = t7.getToBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t7.getToBoard(), BoardUtils.getCoordinateAtPosition("f7"),
                        BoardUtils.getCoordinateAtPosition("f6")));

        final MoveTransition t9 = t8.getToBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t8.getToBoard(), BoardUtils.getCoordinateAtPosition("d1"),
                        BoardUtils.getCoordinateAtPosition("h5")));
        final MoveTransition t10 = t9.getToBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t9.getToBoard(), BoardUtils.getCoordinateAtPosition("g7"),
                        BoardUtils.getCoordinateAtPosition("g6")));

        final MoveTransition t11 = t10.getToBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t10.getToBoard(), BoardUtils.getCoordinateAtPosition("h5"),
                        BoardUtils.getCoordinateAtPosition("h4")));
        final MoveTransition t12 = t11.getToBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t11.getToBoard(), BoardUtils.getCoordinateAtPosition("f6"),
                        BoardUtils.getCoordinateAtPosition("g5")));

        final MoveTransition t13 = t12.getToBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t12.getToBoard(), BoardUtils.getCoordinateAtPosition("h4"),
                        BoardUtils.getCoordinateAtPosition("g5")));
        final MoveTransition t14 = t13.getToBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t13.getToBoard(), BoardUtils.getCoordinateAtPosition("d5"),
                        BoardUtils.getCoordinateAtPosition("e4")));

        assertEquals(t14.getToBoard().getWhitePlayer().getActivePieces().size(), calculatedActivesFor(t14.getToBoard(), Alliance.WHITE));
        assertEquals(t14.getToBoard().getBlackPlayer().getActivePieces().size(), calculatedActivesFor(t14.getToBoard(), Alliance.BLACK));

    }

    @Test(expected = RuntimeException.class)
    public void testInvalidBoard() {

        final Builder builder = new Builder();
        // Black Layout
        builder.setPieceAtPosition(new Rook(Alliance.BLACK, 0));
        builder.setPieceAtPosition(new Knight(Alliance.BLACK, 1));
        builder.setPieceAtPosition(new Bishop(Alliance.BLACK, 2));
        builder.setPieceAtPosition(new Queen(Alliance.BLACK, 3));
        builder.setPieceAtPosition(new Bishop(Alliance.BLACK, 5));
        builder.setPieceAtPosition(new Knight(Alliance.BLACK, 6));
        builder.setPieceAtPosition(new Rook(Alliance.BLACK, 7));
        builder.setPieceAtPosition(new Pawn(Alliance.BLACK, 8));
        builder.setPieceAtPosition(new Pawn(Alliance.BLACK, 9));
        builder.setPieceAtPosition(new Pawn(Alliance.BLACK, 10));
        builder.setPieceAtPosition(new Pawn(Alliance.BLACK, 11));
        builder.setPieceAtPosition(new Pawn(Alliance.BLACK, 12));
        builder.setPieceAtPosition(new Pawn(Alliance.BLACK, 13));
        builder.setPieceAtPosition(new Pawn(Alliance.BLACK, 14));
        builder.setPieceAtPosition(new Pawn(Alliance.BLACK, 15));
        // White Layout
        builder.setPieceAtPosition(new Pawn(Alliance.WHITE, 48));
        builder.setPieceAtPosition(new Pawn(Alliance.WHITE, 49));
        builder.setPieceAtPosition(new Pawn(Alliance.WHITE, 50));
        builder.setPieceAtPosition(new Pawn(Alliance.WHITE, 51));
        builder.setPieceAtPosition(new Pawn(Alliance.WHITE, 52));
        builder.setPieceAtPosition(new Pawn(Alliance.WHITE, 53));
        builder.setPieceAtPosition(new Pawn(Alliance.WHITE, 54));
        builder.setPieceAtPosition(new Pawn(Alliance.WHITE, 55));
        builder.setPieceAtPosition(new Rook(Alliance.WHITE, 56));
        builder.setPieceAtPosition(new Knight(Alliance.WHITE, 57));
        builder.setPieceAtPosition(new Bishop(Alliance.WHITE, 58));
        builder.setPieceAtPosition(new Queen(Alliance.WHITE, 59));
        builder.setPieceAtPosition(new Bishop(Alliance.WHITE, 61));
        builder.setPieceAtPosition(new Knight(Alliance.WHITE, 62));
        builder.setPieceAtPosition(new Rook(Alliance.WHITE, 63));
        //white to move
        builder.setMoveMaker(Alliance.WHITE);
        //build the board
        builder.build();
    }

    @Test
    public void testAlgebraicNotation() {
        assertEquals(BoardUtils.getPositionAtCoordinate(0), "a8");
        assertEquals(BoardUtils.getPositionAtCoordinate(1), "b8");
        assertEquals(BoardUtils.getPositionAtCoordinate(2), "c8");
        assertEquals(BoardUtils.getPositionAtCoordinate(3), "d8");
        assertEquals(BoardUtils.getPositionAtCoordinate(4), "e8");
        assertEquals(BoardUtils.getPositionAtCoordinate(5), "f8");
        assertEquals(BoardUtils.getPositionAtCoordinate(6), "g8");
        assertEquals(BoardUtils.getPositionAtCoordinate(7), "h8");
    }

    @Test
    public void mem() {
        final Runtime runtime = Runtime.getRuntime();
        long start, end;
        runtime.gc();
        start = runtime.freeMemory();
        Board board = Board.createStandardBoard();
        end = runtime.freeMemory();
        System.out.println("That took " + (start - end) + " bytes.");

    }

}
