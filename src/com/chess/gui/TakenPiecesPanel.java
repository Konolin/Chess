package com.chess.gui;

import com.chess.engine.board.Move;
import com.chess.engine.piece.Piece;
import com.google.common.primitives.Ints;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.chess.gui.Table.MoveLog;

public class TakenPiecesPanel extends JPanel {
    private static final EtchedBorder PANEL_BORDER = new EtchedBorder(EtchedBorder.RAISED);
    private static final Color PANEL_COLOR = new Color(200, 200, 200);
    private static final Dimension TAKEN_PIECES_DIMENSION = new Dimension(40, 80);
    private static final int TAKEN_PIECES_REDUCTION = 15;

    private final JPanel northPanel;
    private final JPanel southPanel;

    public TakenPiecesPanel() {
        super(new BorderLayout());
        this.setBackground(PANEL_COLOR);
        this.setBorder(PANEL_BORDER);
        this.northPanel = new JPanel(new GridLayout(8, 2));
        this.southPanel = new JPanel(new GridLayout(8, 2));
        this.northPanel.setBackground(PANEL_COLOR);
        this.southPanel.setBackground(PANEL_COLOR);
        this.add(this.northPanel, BorderLayout.NORTH);
        this.add(this.southPanel, BorderLayout.SOUTH);
        setPreferredSize(TAKEN_PIECES_DIMENSION);
    }

    public void redo(final MoveLog moveLog) {
        this.northPanel.removeAll();
        this.southPanel.removeAll();

        final List<Piece> whiteTakenPieces = new ArrayList<>();
        final List<Piece> blackTakenPieces = new ArrayList<>();

        for (final Move move : moveLog.getMoves()) {
            if (move.isAttack()) {
                final Piece takenPiece = move.getAttackedPiece();
                if (takenPiece.getPieceAlliance().isWhite()) {
                    whiteTakenPieces.add(takenPiece);
                } else {
                    blackTakenPieces.add(takenPiece);
                }
            }
        }

        whiteTakenPieces.sort((piece1, piece2) -> Ints.compare(piece1.getPieceValue(), piece2.getPieceValue()));
        blackTakenPieces.sort((piece1, piece2) -> Ints.compare(piece1.getPieceValue(), piece2.getPieceValue()));

        for (List<Piece> takenPieces : List.of(whiteTakenPieces, blackTakenPieces)) {
            for (final Piece takenPiece : takenPieces) {
                try {
                    final BufferedImage image;
                    try (FileInputStream fis = new FileInputStream(Table.DEFAULT_PIECE_ICON_PATH + takenPiece.getPieceAlliance().toString().charAt(0) + takenPiece + ".gif")) {
                        image = ImageIO.read(fis);
                    }
                    if (image != null) {
                        final ImageIcon icon = new ImageIcon(image);
                        final JLabel imageLabel = new JLabel(new ImageIcon(icon.getImage().getScaledInstance(icon.getIconWidth() - TAKEN_PIECES_REDUCTION, icon.getIconWidth() - TAKEN_PIECES_REDUCTION, Image.SCALE_SMOOTH)));

                        if (takenPieces == whiteTakenPieces) {
                            this.southPanel.add(imageLabel);
                        } else {
                            this.northPanel.add(imageLabel);
                        }
                    } else {
                        System.out.println("TakenPiecesPanel::Image could not be loaded.");
                    }
                } catch (final IOException e) {
                    e.printStackTrace();
                }
            }
        }
        validate();
    }
}
