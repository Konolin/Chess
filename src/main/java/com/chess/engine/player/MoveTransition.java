package com.chess.engine.player;

import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import lombok.Getter;

public class MoveTransition {
    @Getter
    private final Board transitionBoard;
    private final Move move;
    @Getter
    private final MoveStatus moveStatus;

    public MoveTransition(final Board transitionBoard, final Move move, final MoveStatus moveStatus) {
        this.transitionBoard = transitionBoard;
        this.move = move;
        this.moveStatus = moveStatus;
    }
}
