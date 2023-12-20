package com.chess;

import com.bitboards.BitBoard;
import com.chess.engine.board.Board;
import com.chess.gui.Table;

public class JChess {
    public static void main(String[] args) {
        Board board = Board.createStandardBoard();
        System.out.println(board);

        System.out.print(BitBoard.get());

        Table.get().show();
    }
}

// TODO - make a single place for all sliding piece logic
