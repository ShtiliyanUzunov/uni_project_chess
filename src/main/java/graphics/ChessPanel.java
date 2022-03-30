package graphics;

import chess.figures.Field;
import chess.*;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.ImageIcon;

import javax.swing.JPanel;

public class ChessPanel extends JPanel {

    private final BoardMovement boardMovement = GlobalState.getBoardMovement();
    private ChessFrame parent;

    private static int x1 = -1, x2 = -1, y1 = -1, y2 = -1;
    private boolean indicatorSelect = false;
    private final Image select = new ImageIcon("images\\Select.png").getImage();
    private final Image attByWhite = new ImageIcon("images\\AttByWhite.png").getImage();
    private final Image attByBlack = new ImageIcon("images\\AttByBlack.png").getImage();

    ChessPanel(ChessFrame parent) {
        super();
        this.parent = parent;
        addMouseListener(new MA());
    }

    private class MA extends MouseAdapter {
        public void mouseClicked(MouseEvent e) {
            Board board = GlobalState.getBoard();

            if (x1 < 0 && y1 < 0) {
                x1 = e.getX() / 75;
                y1 = 7 - e.getY() / 75;

                if (board.getElementAt(x1, y1) instanceof Field) {
                    x1 = -1;
                    y1 = -1;
                    return;
                }
                if (board.getElementAt(x1, y1).getColor().equalsIgnoreCase(boardMovement.getPlayerTurn()))
                    indicatorSelect = true;
                else {
                    x1 = -1;
                    y1 = -1;
                }

            } else {
                x2 = e.getX() / 75;
                y2 = 7 - e.getY() / 75;
                boardMovement.moveFigure(x1, y1, x2, y2);

                x1 = -1;
                x2 = -1;
                y1 = -1;
                y2 = -1;
                indicatorSelect = false;
            }
            repaint();
        }
    }

    public static void setCords(int a, int b, int c, int d) {
        x1 = a;
        y1 = b;
        x2 = c;
        y2 = d;
    }

    public void paint(Graphics g) {
        Board board = GlobalState.getBoard();

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
            g.drawImage(select, x1 * 75, 525 - y1 * 75, null);
        }

    }

}
