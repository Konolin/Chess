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

public class WhitePlayer extends Player {
    public WhitePlayer(final Board board, final Collection<Move> whiteStandardMoves, final Collection<Move> blackStandardMoves) {
        super(board, whiteStandardMoves, blackStandardMoves);
    }

    @Override
    public Collection<Piece> getActivePieces() {
        return this.board.getWhitePieces();
    }

    @Override
    public Alliance getAlliance() {
        return Alliance.WHITE;
    }

    @Override
    public Player getOpponent() {
        return this.board.getBlackPlayer();
    }

    @Override
    public Collection<Move> calculateKingCastles(final Collection<Move> playerLegals, final Collection<Move> opponentsLegals) {
        final List<Move> kingCastles = new ArrayList<>();

        if (this.playerKing.isFirstMove() && !this.isInCheck()) {
            // whites king side castle
            // check if the tiles between king and rook are empty
            if (!this.board.getTileAtCoordinate(61).isTileOccupied() && !this.board.getTileAtCoordinate(62).isTileOccupied()) {
                // check if rook tile is occupied by a rook who hasn't made it's first move yet
                final Tile rookTile = this.board.getTileAtCoordinate(63);
                if (rookTile.isTileOccupied() && rookTile.getPieceOnTile().isFirstMove() && rookTile.getPieceOnTile().getPieceType().isRook()) {
                    // check if tiles between them are not attacked
                    if (Player.calculateAttacksOnTile(61, opponentsLegals).isEmpty() &&
                            Player.calculateAttacksOnTile(62, opponentsLegals).isEmpty()) {
                        kingCastles.add(new Move.KingSideCastleMove(this.board, this.playerKing, 62,
                                (Rook) rookTile.getPieceOnTile(), rookTile.getTileCoordinate(), 61));
                    }
                }
            }

            // white queen side castle
            // check if the tiles between king and rook are empty
            if (!this.board.getTileAtCoordinate(59).isTileOccupied() && !this.board.getTileAtCoordinate(58).isTileOccupied() &&
                    !this.board.getTileAtCoordinate(57).isTileOccupied()) {
                // check if rook tile is occupied by a rook who hasn't made it's first move yet
                final Tile rookTile = this.board.getTileAtCoordinate(56);
                if (rookTile.isTileOccupied() && rookTile.getPieceOnTile().isFirstMove() && rookTile.getPieceOnTile().getPieceType().isRook()) {
                    // check if tiles between them are not attacked
                    if (Player.calculateAttacksOnTile(59, opponentsLegals).isEmpty() &&
                            Player.calculateAttacksOnTile(58, opponentsLegals).isEmpty() &&
                            Player.calculateAttacksOnTile(57, opponentsLegals).isEmpty()) {
                        kingCastles.add(new Move.QueenSideCastleMove(this.board, this.playerKing, 58,
                                (Rook) rookTile.getPieceOnTile(), rookTile.getTileCoordinate(), 59));
                    }
                }
            }
        }

        return ImmutableList.copyOf(kingCastles);
    }

    @Override
    public String toString() {
        return "White";
    }
}
