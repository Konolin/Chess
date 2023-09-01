package com.chess.engine.board;

import com.chess.engine.piece.Piece;

public abstract class Move {
    final Board board;
    final Piece movedPiece;
    final int destinationCoordinate;

    public static final Move NULL_MOVE = new NullMove();

    private Move(final Board board, final Piece movedPiece, final int destinationCoordinate) {
        this.board = board;
        this.movedPiece = movedPiece;
        this.destinationCoordinate = destinationCoordinate;
    }

    public int getCurrentCoordinate() {
        return this.movedPiece.getPiecePosition();
    }

    public int getDestinationCoordinate() {
        return this.destinationCoordinate;
    }

    public Piece getMovedPiece() {
        return this.movedPiece;
    }

    public Board execute() {
        final Board.Builder builder = new Board.Builder();

        // place all pieces of the current player on the new board, except for the moved piece
        for (final Piece piece : this.board.currentPlayer().getActivePieces()) {
            if (!this.movedPiece.equals(piece)) { // TODO hashcode and equals for pieces
                builder.setPiece(piece);
            }
        }

        // place all pieces of the opponent on the new board
        for (final Piece piece : this.board.currentPlayer().getOpponent().getActivePieces()) {
            builder.setPiece(piece);
        }

        builder.setPiece(this.movedPiece.movePiece(this));
        builder.setMoveMaker(this.board.currentPlayer().getOpponent().getAlliance());

        // build the new board and return it
        return builder.build();
    }

    // a piece is moved to an empty tile
    public static final class MajorMove extends Move {
        public MajorMove(final Board board, final Piece movedPiece, final int destinationCoordinate) {
            super(board, movedPiece, destinationCoordinate);
        }
    }

    // a piece captures another piece
    public static class AttackMove extends Move {
        final Piece attackedPiece;

        public AttackMove(final Board board, final Piece movedPiece, final int destinationCoordinate, final Piece attackedPiece) {
            super(board, movedPiece, destinationCoordinate);
            this.attackedPiece = attackedPiece;
        }

        @Override
        public Board execute() {
            return null;
            // TODO
        }
    }

    public static class PawnMove extends Move {
        public PawnMove(final Board board, final Piece movedPiece, final int destinationCoordinate) {
            super(board, movedPiece, destinationCoordinate);
        }
    }

    public static class PawnAttackMove extends AttackMove {
        public PawnAttackMove(final Board board, final Piece movedPiece, final int destinationCoordinate, final Piece attackedPiece) {
            super(board, movedPiece, destinationCoordinate, attackedPiece);
        }
    }

    public static final class PawnEnPassantAttackMove extends PawnAttackMove {
        public PawnEnPassantAttackMove(final Board board, final Piece movedPiece, final int destinationCoordinate, final Piece attackedPiece) {
            super(board, movedPiece, destinationCoordinate, attackedPiece);
        }
    }

    public static final class PawnJump extends PawnMove {
        public PawnJump(final Board board, final Piece movedPiece, final int destinationCoordinate) {
            super(board, movedPiece, destinationCoordinate);
        }
    }

    static abstract class CastleMove extends Move {
        public CastleMove(final Board board, final Piece movedPiece, final int destinationCoordinate) {
            super(board, movedPiece, destinationCoordinate);
        }
    }

    public static final class KingSideCastleMove extends CastleMove {
        public KingSideCastleMove(final Board board, final Piece movedPiece, final int destinationCoordinate) {
            super(board, movedPiece, destinationCoordinate);
        }
    }

    public static final class QueenSideCastleMove extends CastleMove {
        public QueenSideCastleMove(final Board board, final Piece movedPiece, final int destinationCoordinate) {
            super(board, movedPiece, destinationCoordinate);
        }
    }

    public static final class NullMove extends Move {
        public NullMove() {
            super(null, null, -1);
        }

        @Override
        public Board execute() {
            throw new RuntimeException("Cannot execute null move!");
        }
    }

    public static class MoveFactory {
        private MoveFactory() {
            throw new RuntimeException("Not instantiable");
        }

        public static Move createMove(final Board board, final int currentCoordinate, final int destinationCoordinate) {
            for (final Move move : board.getAllLegalMoves()) {
                if (move.getCurrentCoordinate() == currentCoordinate &&
                        move.getDestinationCoordinate() == destinationCoordinate) {
                    return move;
                }
            }
            return NULL_MOVE;
        }
    }
}
