package com.chess.engine.player;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import com.chess.engine.piece.King;
import com.chess.engine.piece.Piece;

import java.util.Collection;

public abstract class Player {
    protected final Board board;
    protected final King playerKing;
    protected final Collection<Move> legalMoves;

    Player(final Board board, final Collection<Move> legalMoves, final Collection<Move> opponentMoves) {
        this.board = board;
        this.playerKing = establishKing();
        this.legalMoves = legalMoves;
    }

    private King establishKing() {
        for (final Piece piece : getActivePieces()) {
            if (piece.getPieceType().isKing()) {
                return (King) piece;
            }
        }
        throw new RuntimeException("Not a valid board, king is missing!");
    }

    public boolean isMoveLegal(final Move move) {
        return this.legalMoves.contains(move);
    }

    public boolean isInCheck() {
        return false;
        // TODO - implementation
    }

    public boolean isInCheckMate() {
        return false;
        // TODO - implementation
    }

    public boolean isInStalemate() {
        return false;
        // TODO - implementation
    }

    public boolean isCastled() {
        return false;
        // TODO - implementation
    }

    public MoveTransition makeMove(final Move move) {
        return null;
        // TODO - implementation
    }

    public abstract Collection<Piece> getActivePieces();

    public abstract Alliance getAlliance();

    public abstract Player getOpponent();
}
