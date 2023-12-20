/**
 * The {@code Board} class represents a move in a chess game. It is an abstract class
 * with various subclasses representing different types of moves, such as
 * standard moves, attack moves, castling moves, pawn promotions, and null moves.
 * <p>
 * The class includes methods for executing moves on a chess board, checking
 * for equality between moves, and generating legal moves.
 * <p>
 * The Move class is part of a larger chess engine implementation, and it
 * interacts with other classes such as Board, Piece, Rook, and Pawn.
 */

package com.chess.engine.board;

import com.bitboards.BitBoard;
import com.chess.engine.board.Board.Builder;
import com.chess.engine.piece.Pawn;
import com.chess.engine.piece.Piece;
import com.chess.engine.piece.Rook;
import lombok.Getter;

public abstract class Move {
    @Getter
    protected final Board board;
    @Getter
    protected final Piece movedPiece;
    @Getter
    protected final int destinationCoordinate;
    protected final boolean isFirstMove;

    private Move(final Board board,
                 final Piece movedPiece,
                 final int destinationCoordinate) {
        this.board = board;
        this.movedPiece = movedPiece;
        this.destinationCoordinate = destinationCoordinate;
        this.isFirstMove = movedPiece.isFirstMove();
    }

    private Move(final Board board,
                 final int destinationCoordinate) {
        this.board = board;
        this.movedPiece = null;
        this.destinationCoordinate = destinationCoordinate;
        this.isFirstMove = false;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + this.movedPiece.getPiecePosition();
        result = prime * result + this.movedPiece.hashCode();
        result = prime * result + this.movedPiece.getPiecePosition();
        return result;
    }

    @Override
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Move otherMove)) {
            return false;
        }
        return getCurrentCoordinate() == otherMove.getCurrentCoordinate() &&
                getDestinationCoordinate() == otherMove.getDestinationCoordinate() &&
                getMovedPiece().equals(otherMove.getMovedPiece());
    }

    /**
     * Retrieves the current coordinate of the piece that is part of this move.
     * The current coordinate represents the position on the chess board from
     * which the piece is being moved.
     *
     * @return The current coordinate of the piece on the chess board.
     */
    public int getCurrentCoordinate() {
        return this.movedPiece.getPiecePosition();
    }

    public boolean isAttack() {
        return false;
    }

    public boolean isCastlingMove() {
        return false;
    }

    public boolean isPromotingMove() {
        return false;
    }

    public Piece getAttackedPiece() {
        return null;
    }

    /**
     * Places pieces on the specified board builder according to the current game state.
     * This method adds all active pieces of the current player to the board, excluding the moved piece,
     * and then adds all active pieces of the opponent to the board.
     *
     * @param builder The Board.Builder object on which to place the pieces.
     * @return The modified Board.Builder object with pieces added based on the current game state.
     */
    protected Board.Builder placePieces(Board.Builder builder) {
        // place all pieces of the current player on the new board, except for the moved piece
        for (final Piece piece : this.board.getCurrentPlayer().getActivePieces()) {
            if (!this.movedPiece.equals(piece)) {
                builder.setPieceAtPosition(piece);
            }
        }
        // place all pieces of the opponent on the new board
        for (final Piece piece : this.board.getCurrentPlayer().getOpponent().getActivePieces()) {
            builder.setPieceAtPosition(piece);
        }
        return builder;
    }

    /**
     * Executes the current move on the chess board, resulting in a new board state.
     * The method creates a new board by updating the positions of pieces after the move.
     *
     * @return The new chess board state after executing the move.
     */
    public Board execute() {
        final Board.Builder builder = placePieces(new Board.Builder());
        // set the moved piece and change the current move maker
        builder.setPieceAtPosition(this.movedPiece.movePiece(this));
        BitBoard.Piece.WHITE_KNIGHTS.setBits(this.movedPiece.getPiecePosition());
        builder.setMoveMaker(this.board.getCurrentPlayer().getOpponent().getAlliance());
        // TODO - print bitboard representation of the board
        // System.out.println(BitBoard.get() + "\n");
        // build the new board and return it
        return builder.build();
    }

    /**
     * Represents a major move in chess, where a piece moves to an empty square on the board.
     * Inherits from the Move class.
     */
    public static final class MajorMove extends Move {
        public MajorMove(final Board board,
                         final Piece movedPiece,
                         final int destinationCoordinate) {
            super(board, movedPiece, destinationCoordinate);
        }

        @Override
        public boolean equals(final Object other) {
            return this == other || other instanceof MajorMove && super.equals(other);
        }

        @Override
        public String toString() {
            return movedPiece.getPieceType().toString() + BoardUtils.getPositionAtCoordinate(this.destinationCoordinate);
        }
    }

    /**
     * Represents a major attack move in chess, where a piece captures an opponent's piece.
     * Inherits from the AttackMove class.
     */
    public static class MajorAttackMove extends AttackMove {
        public MajorAttackMove(final Board board,
                               final Piece piece,
                               final int destinationCoordinate,
                               final Piece pieceAttacked) {
            super(board, piece, destinationCoordinate, pieceAttacked);
        }

        @Override
        public boolean equals(final Object other) {
            return this == other || other instanceof MajorAttackMove && super.equals(other);
        }

        @Override
        public String toString() {
            return movedPiece.getPieceType() + BoardUtils.getPositionAtCoordinate(this.destinationCoordinate);
        }
    }

    /**
     * Represents a general attack move in chess, where a piece captures an opponent's piece.
     * Inherits from the Move class.
     */
    public static class AttackMove extends Move {
        @Getter
        final Piece attackedPiece;

        public AttackMove(final Board board,
                          final Piece movedPiece,
                          final int destinationCoordinate,
                          final Piece attackedPiece) {
            super(board, movedPiece, destinationCoordinate);
            this.attackedPiece = attackedPiece;
        }

        @Override
        public boolean isAttack() {
            return true;
        }

        @Override
        public int hashCode() {
            return this.attackedPiece.hashCode() + super.hashCode();
        }

        @Override
        public boolean equals(final Object other) {
            if (this == other) {
                return true;
            }
            if (!(other instanceof AttackMove otherAttackMove)) {
                return false;
            }
            return super.equals(otherAttackMove) && getAttackedPiece().equals(otherAttackMove.getAttackedPiece());
        }
    }

    /**
     * Represents a pawn move in chess, where a pawn advances to an empty square.
     * Inherits from the Move class.
     */
    public static class PawnMove extends Move {
        public PawnMove(final Board board,
                        final Piece movedPiece,
                        final int destinationCoordinate) {
            super(board, movedPiece, destinationCoordinate);
        }

        @Override
        public boolean equals(final Object other) {
            return this == other || other instanceof PawnMove && super.equals(other);
        }

        @Override
        public String toString() {
            return BoardUtils.getPositionAtCoordinate(this.destinationCoordinate);
        }
    }

    /**
     * Represents a pawn attack move in chess, where a pawn captures an opponent's piece.
     * Inherits from the AttackMove class.
     */
    public static class PawnAttackMove extends AttackMove {
        public PawnAttackMove(final Board board,
                              final Piece movedPiece,
                              final int destinationCoordinate,
                              final Piece attackedPiece) {
            super(board, movedPiece, destinationCoordinate, attackedPiece);
        }

        @Override
        public boolean equals(final Object other) {
            return this == other || other instanceof PawnAttackMove && super.equals(other);
        }

        @Override
        public String toString() {
            return BoardUtils.getPositionAtCoordinate(this.movedPiece.getPiecePosition()).charAt(0)
                    + "x" + BoardUtils.getPositionAtCoordinate(this.destinationCoordinate);
        }
    }

    /**
     * Represents a pawn en passant attack move in chess, where a pawn captures an opponent's pawn
     * that has just moved two squares forward from its starting position.
     * Inherits from the PawnAttackMove class.
     */
    public static final class PawnEnPassantAttackMove extends PawnAttackMove {
        public PawnEnPassantAttackMove(final Board board,
                                       final Piece movedPiece,
                                       final int destinationCoordinate,
                                       final Piece attackedPiece) {
            super(board, movedPiece, destinationCoordinate, attackedPiece);
        }

        @Override
        public boolean equals(final Object other) {
            return this == other || other instanceof PawnEnPassantAttackMove && super.equals(other);
        }

        /**
         * Executes the pawn en passant attack move on the chess board, resulting in a new board state.
         * The method creates a new builder to construct the updated board state after capturing the opponent's pawn.
         *
         * @return The new chess board state after executing the pawn en passant attack move.
         */
        @Override
        public Board execute() {
            final Builder builder = new Builder();
            for (final Piece piece : this.board.getCurrentPlayer().getActivePieces()) {
                if (!this.movedPiece.equals(piece)) {
                    builder.setPieceAtPosition(piece);
                }
            }
            for (final Piece piece : this.board.getCurrentPlayer().getOpponent().getActivePieces()) {
                if (!piece.equals(this.getAttackedPiece())) {
                    builder.setPieceAtPosition(piece);
                }
            }
            builder.setPieceAtPosition(this.movedPiece.movePiece(this));
            builder.setMoveMaker(this.board.getCurrentPlayer().getOpponent().getAlliance());
            return builder.build();
        }
    }

    /**
     * Represents a pawn promotion move in chess, where a pawn is promoted to another piece upon reaching the last rank.
     * Inherits from the Move class and contains a decorated move that represents the pawn's movement (pawn move or pawn attack).
     */
    public static class PawnPromotion extends Move {
        final Move decoratedMove;
        final Pawn promotedPawn;
        final Piece promotionPiece;

        public PawnPromotion(final Move decoratorMove, Piece promotionPiece) {
            super(decoratorMove.getBoard(), decoratorMove.getMovedPiece(), decoratorMove.getDestinationCoordinate());
            this.decoratedMove = decoratorMove;
            this.promotedPawn = (Pawn) decoratedMove.getMovedPiece();
            this.promotionPiece = promotionPiece;
        }

        /**
         * Executes the pawn promotion move on the chess board, resulting in a new board state.
         * The method creates a new builder to construct the updated board state after promoting the pawn.
         *
         * @return The new chess board state after executing the pawn promotion move.
         */
        @Override
        public Board execute() {
            final Board pawnMoveBoard = this.decoratedMove.execute();
            final Board.Builder builder = placePieces(new Board.Builder());
            builder.setPieceAtPosition(this.promotedPawn.getPromotionPiece().movePiece(this));
            builder.setMoveMaker(pawnMoveBoard.getCurrentPlayer().getAlliance());
            return builder.build();
        }

        @Override
        public boolean isAttack() {
            return this.decoratedMove.isAttack();
        }

        @Override
        public Piece getAttackedPiece() {
            return this.decoratedMove.getAttackedPiece();
        }

        @Override
        public String toString() {
            return BoardUtils.getPositionAtCoordinate(this.movedPiece.getPiecePosition()) + "=";
                    // + this.promotionPiece.getPieceType(); - TODO - fix this
        }

        @Override
        public int hashCode() {
            return this.decoratedMove.hashCode() + (31 * promotedPawn.hashCode());
        }

        @Override
        public boolean equals(final Object other) {
            return this == other || other instanceof PawnPromotion && super.equals(other);
        }

        @Override
        public boolean isPromotingMove() {
            return true;
        }
    }

    /**
     * Represents a pawn jump move in chess, where a pawn advances two squares from its starting position.
     * Inherits from the PawnMove class.
     */
    public static final class PawnJump extends PawnMove {
        public PawnJump(final Board board,
                        final Piece movedPiece,
                        final int destinationCoordinate) {
            super(board, movedPiece, destinationCoordinate);
        }

        /**
         * Executes the pawn jump move in chess, resulting in a new board state.
         * The method creates a new builder to construct the updated board state after the pawn jump.
         * The pawn becomes eligible to be captured via en passant, and it changes the player's turn.
         *
         * @return The new chess board state after executing the pawn jump move.
         */
        @Override
        public Board execute() {
            final Board.Builder builder = placePieces(new Board.Builder());
            // execute pawn jump, make pawn eligible to be captured via en passant, change players turn
            final Pawn movedPawn = (Pawn) this.movedPiece.movePiece(this);
            builder.setPieceAtPosition(movedPawn);
            builder.setEnPassantPawn(movedPawn);
            builder.setMoveMaker(this.board.getCurrentPlayer().getOpponent().getAlliance());
            return builder.build();
        }
    }

    /**
     * Represents an abstract castle move in chess, where the king moves two squares towards a rook.
     * Inherits from the Move class and serves as a base class for KingSideCastleMove and QueenSideCastleMove.
     */
    static abstract class CastleMove extends Move {
        @Getter
        protected final Rook castleRook;
        protected final int castleRookStart;
        protected final int castleRookDestination;

        public CastleMove(final Board board,
                          final Piece movedPiece,
                          final int destinationCoordinate,
                          final Rook castleRook,
                          final int castleRookStart,
                          final int castleRookDestination) {
            super(board, movedPiece, destinationCoordinate);
            this.castleRook = castleRook;
            this.castleRookStart = castleRookStart;
            this.castleRookDestination = castleRookDestination;
        }

        @Override
        public boolean isCastlingMove() {
            return true;
        }

        @Override
        public Board execute() {
            final Builder builder = new Builder();
            for (final Piece piece : this.board.getCurrentPlayer().getActivePieces()) {
                if (!this.movedPiece.equals(piece) && !this.castleRook.equals(piece)) {
                    builder.setPieceAtPosition(piece);
                }
            }
            for (final Piece piece : this.board.getCurrentPlayer().getOpponent().getActivePieces()) {
                builder.setPieceAtPosition(piece);
            }
            builder.setPieceAtPosition(this.movedPiece.movePiece(this));
            builder.setPieceAtPosition(new Rook(this.castleRook.getPieceAlliance(), this.castleRookDestination, false));
            builder.setMoveMaker(this.board.getCurrentPlayer().getOpponent().getAlliance());
            return builder.build();
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = super.hashCode();
            result = prime * result + this.castleRook.hashCode();
            result = prime * result + this.castleRookDestination;
            return result;
        }

        @Override
        public boolean equals(final Object other) {
            if (this == other) {
                return true;
            }
            if (!(other instanceof CastleMove otherCastleMove)) {
                return false;
            }
            return super.equals(otherCastleMove) && this.castleRook.equals(otherCastleMove.getCastleRook());
        }
    }

    /**
     * Represents a king-side castle move in chess, where the king moves two squares towards the king's side,
     * and the rook moves to the square next to the king.
     * Inherits from the CastleMove class.
     */
    public static final class KingSideCastleMove extends CastleMove {
        public KingSideCastleMove(final Board board,
                                  final Piece movedPiece,
                                  final int destinationCoordinate,
                                  final Rook castleRook,
                                  final int castleRookStart,
                                  final int castleRookDestination) {
            super(board, movedPiece, destinationCoordinate, castleRook, castleRookStart, castleRookDestination);
        }

        @Override
        public boolean equals(final Object other) {
            return this == other || other instanceof KingSideCastleMove && super.equals(other);
        }

        @Override
        public String toString() {
            return "O-O";
        }
    }

    /**
     * Represents a queen-side castle move in chess, where the king moves two squares towards the queen's side,
     * and the rook moves to the square next to the king.
     * Inherits from the CastleMove class.
     */
    public static final class QueenSideCastleMove extends CastleMove {
        public QueenSideCastleMove(final Board board,
                                   final Piece movedPiece,
                                   final int destinationCoordinate,
                                   final Rook castleRook,
                                   final int castleRookStart,
                                   final int castleRookDestination) {
            super(board, movedPiece, destinationCoordinate, castleRook, castleRookStart, castleRookDestination);
        }

        @Override
        public boolean equals(final Object other) {
            return this == other || other instanceof QueenSideCastleMove && super.equals(other);
        }

        @Override
        public String toString() {
            return "O-O-O";
        }
    }

    /**
     * Represents a null move in chess, which is an invalid move used to signify the lack of a valid move.
     * Inherits from the Move class and is a singleton instance.
     */
    public static final class NullMove extends Move {
        public NullMove() {
            super(null, -1);
        }

        @Override
        public Board execute() {
            throw new RuntimeException("Cannot execute null move!");
        }

        @Override
        public int getCurrentCoordinate() {
            return -1;
        }
    }

    /**
     * The MoveFactory class is a utility class responsible for creating specific move instances
     * based on the given board state and coordinates.
     */
    public static class MoveFactory {
        private MoveFactory() {
            throw new RuntimeException("Not instantiable");
        }

        public static Move createMove(final Board board,
                                      final int currentCoordinate,
                                      final int destinationCoordinate) {
            for (final Move move : board.getAllLegalMoves()) {
                if (move.getCurrentCoordinate() == currentCoordinate &&
                        move.getDestinationCoordinate() == destinationCoordinate) {
                    return move;
                }
            }
            return MoveUtils.NULL_MOVE;
        }
    }
}
