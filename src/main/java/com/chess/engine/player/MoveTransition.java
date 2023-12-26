package com.chess.engine.player;

import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import lombok.Getter;

@Getter
public class MoveTransition {
    private final Board fromBoard;
    private final Board toBoard;
    private final Move move;
    private final MoveStatus moveStatus;

    public MoveTransition(final Board fromBoard, final Board toBoard, final Move move, final MoveStatus moveStatus) {
        this.fromBoard = fromBoard;
        this.toBoard = toBoard;
        this.move = move;
        this.moveStatus = moveStatus;
    }
}
