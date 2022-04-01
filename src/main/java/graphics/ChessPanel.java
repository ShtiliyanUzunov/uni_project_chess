package graphics;

import chess.figures.Field;
import chess.*;
import chess.services.GlobalContext;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.ImageIcon;

import javax.swing.JPanel;

public class ChessPanel extends JPanel {

    private final BoardMovement boardMovement = GlobalContext.getBoardMovement();
    private ChessFrame parent;

    private static int xFrom = -1, xTo = -1, yFrom = -1, yTo = -1;
    private boolean indicatorSelect = false;
    private final Image select = new ImageIcon("images\\Select.png").getImage();
    private final Image attByWhite = new ImageIcon("images\\AttByWhite.png").getImage();
    private final Image attByBlack = new ImageIcon("images\\AttByBlack.png").getImage();

    ChessPanel(ChessFrame parent) {
        super();
        this.parent = parent;
        addMouseListener(new MA());
    }

    private void nullifyFromSelection() {
        xFrom = -1;
        yFrom = -1;
    }

    private void nullifyToSelection() {
        xTo = -1;
        yTo = -1;
    }

    private class MA extends MouseAdapter {
        public void mouseClicked(MouseEvent e) {
            Board board = GlobalContext.getBoard();

            if (xFrom < 0 && yFrom < 0) {
                xFrom = e.getX() / 75;
                yFrom = 7 - e.getY() / 75;

                if (board.getElementAt(xFrom, yFrom) instanceof Field) {
                    nullifyFromSelection();
                    return;
                }
                if (board.getElementAt(xFrom, yFrom).getColor().equalsIgnoreCase(board.getPlayerTurn()))
                    indicatorSelect = true;
                else {
                    nullifyFromSelection();
                }
            } else {
                xTo = e.getX() / 75;
                yTo = 7 - e.getY() / 75;
                boardMovement.moveFigure(xFrom, yFrom, xTo, yTo);

                nullifyFromSelection();
                nullifyToSelection();
                indicatorSelect = false;
            }
            repaint();
        }
    }

    public void paint(Graphics g) {
        Board board = GlobalContext.getBoard();

        ImageIcon Icon = new ImageIcon("images\\Board.png");
        Image temp = Icon.getImage();
        g.drawImage(temp, 0, 0, null);
        for (int i = 7; i >= 0; i--)
            for (int j = 7; j >= 0; j--) {
                if (!(board.getElementAt(i, j) instanceof Field)) {
                    temp = board.getElementAt(i, j).getIcon().getImage();
                    g.drawImage(temp, i * 75, 525 - j * 75, null);
                }

                if (!parent.getShowAttacks().getState()) {
                    continue;
                }

                if (board.getElementAt(i, j).isAttByBlack())
                    g.drawImage(attByBlack, i * 75, 525 - j * 75, null);
                if (board.getElementAt(i, j).isAttByWhite())
                    g.drawImage(attByWhite, i * 75, 525 - j * 75, null);

            }
        if (indicatorSelect) {
            g.drawImage(select, xFrom * 75, 525 - yFrom * 75, null);
        }

    }

}
