package com.chess.engine.piece;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import lombok.Getter;

import java.util.Collection;

/**
 * The abstract class representing a chess piece.
 */
public abstract class Piece {
    @Getter
    protected final PieceType pieceType;
    @Getter
    protected final int piecePosition;
    @Getter
    protected final Alliance pieceAlliance;
    protected final boolean isFirstMove;
    private final int cachedHashCode;

    Piece(final int piecePosition, final Alliance pieceAlliance, final PieceType pieceType, final boolean isFirstMove) {
        this.pieceAlliance = pieceAlliance;
        this.piecePosition = piecePosition;
        this.pieceType = pieceType;
        this.isFirstMove = isFirstMove;
        this.cachedHashCode = computeHashCode();
    }

    private int computeHashCode() {
        final int prime = 31;
        int result = pieceType.hashCode();
        result = prime * result + pieceAlliance.hashCode();
        result = prime * result + piecePosition;
        result = prime * result + (isFirstMove ? 1 : 0);
        return result;
    }

    public int getPieceValue() {
        return this.pieceType.getPieceValue();
    }

    @Override
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        }

        if (!(other instanceof Piece otherPiece)) {
            return false;
        }

        return piecePosition == otherPiece.getPiecePosition() && pieceType == otherPiece.getPieceType() &&
                pieceAlliance == otherPiece.getPieceAlliance() && isFirstMove == otherPiece.isFirstMove();
    }

    @Override
    public int hashCode() {
        return this.cachedHashCode;
    }

    public abstract Collection<Move> calculateLegalMoves(final Board board);

    public abstract Piece movePiece(Move move);

    public boolean isFirstMove() {
        return this.isFirstMove;
    }

    /**
     * Enumeration representing different types of chess pieces.
     */
    public enum PieceType {
        PAWN("P", 100) {
            @Override
            public boolean isKing() {
                return false;
            }

            @Override
            public boolean isRook() {
                return false;
            }
        },
        KNIGHT("N", 300) {
            @Override
            public boolean isKing() {
                return false;
            }

            @Override
            public boolean isRook() {
                return false;
            }
        },
        BISHOP("B", 300) {
            @Override
            public boolean isKing() {
                return false;
            }

            @Override
            public boolean isRook() {
                return false;
            }
        },
        ROOK("R", 500) {
            @Override
            public boolean isKing() {
                return false;
            }

            @Override
            public boolean isRook() {
                return true;
            }
        },
        QUEEN("Q", 900) {
            @Override
            public boolean isKing() {
                return false;
            }

            @Override
            public boolean isRook() {
                return false;
            }
        },
        KING("K", 10000) {
            @Override
            public boolean isKing() {
                return true;
            }

            @Override
            public boolean isRook() {
                return false;
            }
        };

        private final String pieceName;
        @Getter
        private final int pieceValue;

        PieceType(final String pieceName, final int pieceValue) {
            this.pieceName = pieceName;
            this.pieceValue = pieceValue;
        }

        @Override
        public String toString() {
            return this.pieceName;
        }

        public abstract boolean isKing();

        public abstract boolean isRook();
    }
}
