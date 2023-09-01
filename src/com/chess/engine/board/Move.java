package com.chess.engine.board;

import com.chess.engine.piece.Piece;

public abstract class Move {
    final Board board;
    final Piece movedPiece;
    final int destinationCoordinate;

    private Move(final Board board, final Piece movedPiece, final int destinationCoordinate) {
        this.board = board;
        this.movedPiece = movedPiece;
        this.destinationCoordinate = destinationCoordinate;
    }

    public int getDestinationCoordinate() {
        return this.destinationCoordinate;
    }

    public abstract Board execute();

    // a piece is moved to an empty tile
    public static final class MajorMove extends Move {
        public MajorMove(final Board board, final Piece movedPiece, final int destinationCoordinate) {
            super(board, movedPiece, destinationCoordinate);
        }

        @Override
        public Board execute() {
            final Board.Builder builder = new Board.Builder();

            // place all pieces of the current player on the new board, except for the moved piece
            for(final Piece piece : this.board.currentPlayer().getActivePieces()) {
                if (!this.movedPiece.equals(piece)) { // TODO hashcode and equals for pieces
                    builder.setPiece(piece);
                }
            }

            // place all pieces of the opponent on the new board
            for(final Piece piece : this.board.currentPlayer().getOpponent().getActivePieces()) {
                builder.setPiece(piece);
            }

            // set moved piece
            builder.setPiece(null);
            builder.setMoveMaker(this.board.currentPlayer().getOpponent().getAlliance());

            // build the new board and return it
            return builder.build();
        }
    }

    // a piece captures another piece
    public static final class AtackMove extends Move {
        final Piece attackedPiece;

        public AtackMove(final Board board, final Piece movedPiece, final int destinationCoordinate, final Piece attackedPiece) {
            super(board, movedPiece, destinationCoordinate);
            this.attackedPiece = attackedPiece;
        }

        @Override
        public Board execute() {
            return null;
            // TODO
        }
    }

}
