package com.chess.pgn;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.piece.*;

public class FenUtilities {
    private FenUtilities() {
        throw new RuntimeException("not instantiable");
    }

    public static Board createGameFromFEN(final String fenString) {
        final String[] fenPartitions = fenString.trim().split(" ");
        final Board.Builder builder = new Board.Builder();
        final boolean whiteKingSideCastle = whiteKingSideCastle(fenPartitions[2]);
        final boolean whiteQueenSideCastle = whiteQueenSideCastle(fenPartitions[2]);
        final boolean blackKingSideCastle = blackKingSideCastle(fenPartitions[2]);
        final boolean blackQueenSideCastle = blackQueenSideCastle(fenPartitions[2]);
        final String enPassantString = fenPartitions[3];
        final String gameConfiguration = fenPartitions[0];
        final char[] boardTiles = gameConfiguration.replaceAll("/", "")
                .replaceAll("8", "--------")
                .replaceAll("7", "-------")
                .replaceAll("6", "------")
                .replaceAll("5", "-----")
                .replaceAll("4", "----")
                .replaceAll("3", "---")
                .replaceAll("2", "--")
                .replaceAll("1", "-")
                .toCharArray();
        int i = 0;
        while (i < boardTiles.length) {
            switch (boardTiles[i]) {
                case 'r':
                    builder.setPieceAtPosition(new Rook(Alliance.BLACK, i));
                    i++;
                    break;
                case 'n':
                    builder.setPieceAtPosition(new Knight(Alliance.BLACK, i));
                    i++;
                    break;
                case 'b':
                    builder.setPieceAtPosition(new Bishop(Alliance.BLACK, i));
                    i++;
                    break;
                case 'q':
                    builder.setPieceAtPosition(new Queen(Alliance.BLACK, i));
                    i++;
                    break;
                case 'k':
                    builder.setPieceAtPosition(new King(Alliance.BLACK, i, blackKingSideCastle, blackQueenSideCastle));
                    i++;
                    break;
                case 'p':
                    builder.setPieceAtPosition(new Pawn(Alliance.BLACK, i));
                    if (!enPassantString.equals("-")) {
                        if (BoardUtils.getPositionAtCoordinate(i - 8).equals(enPassantString)) {
                            builder.setEnPassantPawn(new Pawn(Alliance.BLACK, i));
                        }
                    }
                    i++;
                    break;
                case 'R':
                    builder.setPieceAtPosition(new Rook(Alliance.WHITE, i));
                    i++;
                    break;
                case 'N':
                    builder.setPieceAtPosition(new Knight(Alliance.WHITE, i));
                    i++;
                    break;
                case 'B':
                    builder.setPieceAtPosition(new Bishop(Alliance.WHITE, i));
                    i++;
                    break;
                case 'Q':
                    builder.setPieceAtPosition(new Queen(Alliance.WHITE, i));
                    i++;
                    break;
                case 'K':
                    builder.setPieceAtPosition(new King(Alliance.WHITE, i, whiteKingSideCastle, whiteQueenSideCastle));
                    i++;
                    break;
                case 'P':
                    builder.setPieceAtPosition(new Pawn(Alliance.WHITE, i));
                    if (!enPassantString.equals("-")) {
                        if (BoardUtils.getPositionAtCoordinate(i + 8).equals(enPassantString)) {
                            builder.setEnPassantPawn(new Pawn(Alliance.WHITE, i));
                        }
                    }
                    i++;
                    break;
                case '-':
                    i++;
                    break;
                default:
                    throw new RuntimeException("Invalid FEN String " + gameConfiguration);
            }
        }
        builder.setMoveMaker(moveMaker(fenPartitions[1]));
        return builder.build();
    }

    private static boolean whiteKingSideCastle(final String fenCastleString) {
        return fenCastleString.contains("K");
    }

    private static boolean whiteQueenSideCastle(final String fenCastleString) {
        return fenCastleString.contains("Q");
    }

    private static boolean blackKingSideCastle(final String fenCastleString) {
        return fenCastleString.contains("k");
    }

    private static boolean blackQueenSideCastle(final String fenCastleString) {
        return fenCastleString.contains("q");
    }

    private static Alliance moveMaker(final String moveMakerString) {
        if (moveMakerString.equals("w")) {
            return Alliance.WHITE;
        } else if (moveMakerString.equals("b")) {
            return Alliance.BLACK;
        }
        throw new RuntimeException("Invalid FEN String " + moveMakerString);
    }

    public static String createFENFromGame(final Board board) {
        return calculateBoardText(board) + " " +
                calculateCurrentPlayerText(board) + " " +
                calculateCastleText(board) + " " +
                calculateEnPassantText(board) + " " +
                "0 1"; //TODO - clock, move number
    }

    private static String calculateBoardText(final Board board) {
        final StringBuilder builder = new StringBuilder();
        for (int i = 0; i < BoardUtils.NUM_TILES; i++) {
            final String tileText = board.getTileAtCoordinate(i).toString();
            builder.append(tileText);
        }
        builder.insert(8, "/");
        builder.insert(17, "/");
        builder.insert(26, "/");
        builder.insert(35, "/");
        builder.insert(44, "/");
        builder.insert(53, "/");
        builder.insert(62, "/");

        return builder.toString()
                .replaceAll("--------", "8")
                .replaceAll("-------", "7")
                .replaceAll("------", "6")
                .replaceAll("-----", "5")
                .replaceAll("----", "4")
                .replaceAll("---", "3")
                .replaceAll("--", "2")
                .replaceAll("-", "1");
    }

    private static String calculateCurrentPlayerText(final Board board) {
        return board.getCurrentPlayer().toString().substring(0, 1).toLowerCase();
    }

    private static String calculateCastleText(final Board board) {
        StringBuilder builder = new StringBuilder();
        if (board.getWhitePlayer().isKingSideCastleCapable()) {
            builder.append("K");
        }
        if (board.getWhitePlayer().isQueenSideCastleCapable()) {
            builder.append("Q");
        }
        if (board.getBlackPlayer().isKingSideCastleCapable()) {
            builder.append("k");
        }
        if (board.getBlackPlayer().isQueenSideCastleCapable()) {
            builder.append("q");
        }
        final String result = builder.toString();
        return result.isEmpty() ? "-" : result;
    }

    private static String calculateEnPassantText(final Board board) {
        final Pawn enPassantPawn = board.getEnPassantPawn();
        if (enPassantPawn != null) {
            // the position behind the pawn
            return BoardUtils.getPositionAtCoordinate(enPassantPawn.getPiecePosition() +
                    8 * enPassantPawn.getPieceAlliance().getOppositeDirection());
        }
        return "-";
    }
}
