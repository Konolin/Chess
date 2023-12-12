package com.chess.engine.player;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import com.chess.engine.board.Tile;
import com.chess.engine.piece.Piece;
import com.chess.engine.piece.Rook;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class BlackPlayer extends Player {
    public BlackPlayer(final Board board, final Collection<Move> whiteStandardMoves, final Collection<Move> blackStandardMoves) {
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
        return this.board.getWhitePlayer();
    }

    @Override
    protected Collection<Move> calculateKingCastles(final Collection<Move> playerLegals, final Collection<Move> opponentsLegals) {
        final List<Move> kingCastles = new ArrayList<>();

        if (this.playerKing.isFirstMove() && !this.isInCheck()) {
            // black king side castle
            // check if the tiles between king and rook are empty
            if (!this.board.getTileAtCoordinate(5).isTileOccupied() && !this.board.getTileAtCoordinate(6).isTileOccupied()) {
                // check if rook tile is occupied by a rook who hasn't made it's first move yet
                final Tile rookTile = this.board.getTileAtCoordinate(7);
                if (rookTile.isTileOccupied() && rookTile.getPieceOnTile().isFirstMove() && rookTile.getPieceOnTile().getPieceType().isRook()) {
                    // check if tiles between them are not attacked
                    if (Player.calculateAttacksOnTile(5, opponentsLegals).isEmpty() &&
                            Player.calculateAttacksOnTile(6, opponentsLegals).isEmpty()) {
                        kingCastles.add(new Move.KingSideCastleMove(this.board, this.playerKing, 6,
                                (Rook) rookTile.getPieceOnTile(), rookTile.getTileCoordinate(), 5));
                    }
                }
            }

            // black queen side castle
            // check if the tiles between king and rook are empty
            if (!this.board.getTileAtCoordinate(1).isTileOccupied() && !this.board.getTileAtCoordinate(2).isTileOccupied() &&
                    !this.board.getTileAtCoordinate(3).isTileOccupied()) {
                // check if rook tile is occupied by a rook who hasn't made it's first move yet
                final Tile rookTile = this.board.getTileAtCoordinate(0);
                if (rookTile.isTileOccupied() && rookTile.getPieceOnTile().isFirstMove() && rookTile.getPieceOnTile().getPieceType().isRook()) {
                    // check if tiles between them are not attacked
                    if (Player.calculateAttacksOnTile(1, opponentsLegals).isEmpty() &&
                            Player.calculateAttacksOnTile(2, opponentsLegals).isEmpty() &&
                            Player.calculateAttacksOnTile(3, opponentsLegals).isEmpty()) {
                        kingCastles.add(new Move.QueenSideCastleMove(this.board, this.playerKing, 2,
                                (Rook) rookTile.getPieceOnTile(), rookTile.getTileCoordinate(), 3));
                    }
                }
            }
        }

        return ImmutableList.copyOf(kingCastles);
    }

    @Override
    public String toString() {
        return "Black";
    }
}
