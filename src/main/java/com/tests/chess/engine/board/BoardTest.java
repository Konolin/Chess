package com.tests.chess.engine.board;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.piece.*;
import com.chess.engine.player.MoveTransition;
import com.chess.engine.player.ai.MiniMax;
import com.chess.engine.player.ai.MoveStrategy;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {
    private static int calculatedActivesFor(final Board board, final Alliance alliance) {
        int count = 0;
        for (final Piece p : board.getAllPieces()) {
            if (p.getPieceAllegiance().equals(alliance)) {
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
//        assertTrue(board.currentPlayer().isKingSideCastleCapable());
//        assertTrue(board.currentPlayer().isQueenSideCastleCapable());
        assertEquals(board.getCurrentPlayer(), board.getWhitePlayer());
        assertEquals(board.getCurrentPlayer().getOpponent(), board.getBlackPlayer());
        assertFalse(board.getCurrentPlayer().getOpponent().isInCheck());
        assertFalse(board.getCurrentPlayer().getOpponent().isInCheckMate());
        assertFalse(board.getCurrentPlayer().getOpponent().isCastled());
//        assertTrue(board.currentPlayer().getOpponent().isKingSideCastleCapable());
//        assertTrue(board.currentPlayer().getOpponent().isQueenSideCastleCapable());
        assertEquals("White", board.getWhitePlayer().toString());
        assertEquals("Black", board.getBlackPlayer().toString());

//        final Iterable<Piece> allPieces = board.getAllPieces();
//        final Iterable<Move> allMoves = Iterables.concat(board.whitePlayer().getLegalMoves(), board.blackPlayer().getLegalMoves());
//        for(final Move move : allMoves) {
//            assertFalse(move.isAttack());
//            assertFalse(move.isCastlingMove());
//            assertEquals(MoveUtils.exchangeScore(move), 1);
//        }
//
//        assertEquals(Iterables.size(allMoves), 40);
//        assertEquals(Iterables.size(allPieces), 32);
//        assertFalse(BoardUtils.isEndGame(board));
//        assertFalse(BoardUtils.isThreatenedBoardImmediate(board));
//        assertEquals(StandardBoardEvaluator.get().evaluate(board, 0), 0);
//        assertEquals(board.getPiece(35), null);
    }

    // TODO - decomentat dupa implementare getToBoard
//    @Test
//    public void testBoardConsistency() {
//        final Board board = Board.createStandardBoard();
//        assertEquals(board.currentPlayer(), board.whitePlayer());
//
//        final MoveTransition t1 = board.currentPlayer()
//                .makeMove(MoveFactory.createMove(board, BoardUtils.getCoordinateAtPosition("e2"),
//                        BoardUtils.getCoordinateAtPosition("e4")));
//        final MoveTransition t2 = t1.getToBoard()
//                .currentPlayer()
//                .makeMove(MoveFactory.createMove(t1.getToBoard(), BoardUtils.getCoordinateAtPosition("e7"),
//                        BoardUtils.getCoordinateAtPosition("e5")));
//
//        final MoveTransition t3 = t2.getToBoard()
//                .currentPlayer()
//                .makeMove(MoveFactory.createMove(t2.getToBoard(), BoardUtils.getCoordinateAtPosition("g1"),
//                        BoardUtils.getCoordinateAtPosition("f3")));
//        final MoveTransition t4 = t3.getToBoard()
//                .currentPlayer()
//                .makeMove(MoveFactory.createMove(t3.getToBoard(), BoardUtils.getCoordinateAtPosition("d7"),
//                        BoardUtils.getCoordinateAtPosition("d5")));
//
//        final MoveTransition t5 = t4.getToBoard()
//                .currentPlayer()
//                .makeMove(MoveFactory.createMove(t4.getToBoard(), BoardUtils.getCoordinateAtPosition("e4"),
//                        BoardUtils.getCoordinateAtPosition("d5")));
//        final MoveTransition t6 = t5.getToBoard()
//                .currentPlayer()
//                .makeMove(MoveFactory.createMove(t5.getToBoard(), BoardUtils.getCoordinateAtPosition("d8"),
//                        BoardUtils.getCoordinateAtPosition("d5")));
//
//        final MoveTransition t7 = t6.getToBoard()
//                .currentPlayer()
//                .makeMove(MoveFactory.createMove(t6.getToBoard(), BoardUtils.getCoordinateAtPosition("f3"),
//                        BoardUtils.getCoordinateAtPosition("g5")));
//        final MoveTransition t8 = t7.getToBoard()
//                .currentPlayer()
//                .makeMove(MoveFactory.createMove(t7.getToBoard(), BoardUtils.getCoordinateAtPosition("f7"),
//                        BoardUtils.getCoordinateAtPosition("f6")));
//
//        final MoveTransition t9 = t8.getToBoard()
//                .currentPlayer()
//                .makeMove(MoveFactory.createMove(t8.getToBoard(), BoardUtils.getCoordinateAtPosition("d1"),
//                        BoardUtils.getCoordinateAtPosition("h5")));
//        final MoveTransition t10 = t9.getToBoard()
//                .currentPlayer()
//                .makeMove(MoveFactory.createMove(t9.getToBoard(), BoardUtils.getCoordinateAtPosition("g7"),
//                        BoardUtils.getCoordinateAtPosition("g6")));
//
//        final MoveTransition t11 = t10.getToBoard()
//                .currentPlayer()
//                .makeMove(MoveFactory.createMove(t10.getToBoard(), BoardUtils.getCoordinateAtPosition("h5"),
//                        BoardUtils.getCoordinateAtPosition("h4")));
//        final MoveTransition t12 = t11.getToBoard()
//                .currentPlayer()
//                .makeMove(MoveFactory.createMove(t11.getToBoard(), BoardUtils.getCoordinateAtPosition("f6"),
//                        BoardUtils.getCoordinateAtPosition("g5")));
//
//        final MoveTransition t13 = t12.getToBoard()
//                .currentPlayer()
//                .makeMove(MoveFactory.createMove(t12.getToBoard(), BoardUtils.getCoordinateAtPosition("h4"),
//                        BoardUtils.getCoordinateAtPosition("g5")));
//        final MoveTransition t14 = t13.getToBoard()
//                .currentPlayer()
//                .makeMove(MoveFactory.createMove(t13.getToBoard(), BoardUtils.getCoordinateAtPosition("d5"),
//                        BoardUtils.getCoordinateAtPosition("e4")));
//
//        assertTrue(t14.getToBoard().whitePlayer().getActivePieces().size() == calculatedActivesFor(t14.getToBoard(), Alliance.WHITE));
//        assertTrue(t14.getToBoard().blackPlayer().getActivePieces().size() == calculatedActivesFor(t14.getToBoard(), Alliance.BLACK));
//
//    }

    @Test
    public void testInvalidBoard() {
        final Board.Builder builder = new Board.Builder();
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
        assertThrows(RuntimeException.class, builder::build);
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

    @Test
    public void testFoolsMate() {
        final Board board = Board.createStandardBoard();
        final MoveTransition t1 = board.getCurrentPlayer()
                .makeMove(Move.MoveFactory.createMove(board, BoardUtils.getCoordinateAtPosition("f2"),
                        BoardUtils.getCoordinateAtPosition("f3")));

        assertTrue(t1.getMoveStatus().isDone());

        final MoveTransition t2 = t1.getTransitionBoard().getCurrentPlayer()
                .makeMove(Move.MoveFactory.createMove(t1.getTransitionBoard(), BoardUtils.getCoordinateAtPosition("e7"),
                        BoardUtils.getCoordinateAtPosition("e5")));

        assertTrue(t2.getMoveStatus().isDone());

        final MoveTransition t3 = t2.getTransitionBoard().getCurrentPlayer()
                .makeMove(Move.MoveFactory.createMove(t2.getTransitionBoard(), BoardUtils.getCoordinateAtPosition("g2"),
                        BoardUtils.getCoordinateAtPosition("g4")));

        assertTrue(t3.getMoveStatus().isDone());

        final MoveStrategy strategy = new MiniMax(4);
        final Move aiMove = strategy.execute(t3.getTransitionBoard());
        final Move bestMove = Move.MoveFactory.createMove(t3.getTransitionBoard(), BoardUtils.getCoordinateAtPosition("d8"),
                BoardUtils.getCoordinateAtPosition("h4"));
        assertEquals(aiMove, bestMove);
    }
}