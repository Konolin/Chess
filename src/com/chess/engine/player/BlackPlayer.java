package com.chess.engine.player;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import com.chess.engine.piece.Piece;

import java.util.Collection;

public class BlackPlayer extends Player {
    public BlackPlayer(Board board, Collection<Move> whiteStandardMoves, Collection<Move> blackStandardMoves) {
        super(board, blackStandardMoves, whiteStandardMoves);
    }

    @Override
    public Collection<Piece> getActivePieces() {
        return this.board.getBlackPieces();
    }

    @Override
    public Alliance getAlliance() {
        return Alliance.BLACK;
    }

    @Override
    public Player getOpponent() {
        return this.board.whitePlayer();
    }
}
