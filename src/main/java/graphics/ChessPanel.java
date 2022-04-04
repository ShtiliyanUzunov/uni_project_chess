package graphics;

import chess.figures.Field;
import chess.*;
import chess.services.BoardMovement;
import chess.services.GlobalContext;
import chess.util.Move;
import communication.ChannelNames;
import communication.EventBus;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.ImageIcon;

import javax.swing.JPanel;

public class ChessPanel extends JPanel {

    private final BoardMovement boardMovement = GlobalContext.getBoardMovement();
    private final ChessFrame parent;

    private static int xFrom = -1, xTo = -1, yFrom = -1, yTo = -1;
    private boolean indicatorSelect = false;

    private static final EventBus eventBus = EventBus.getEventBus();

    private final Image moveOption = new ImageIcon("images\\Move_Option.png").getImage();
    private final Image select = new ImageIcon("images\\Select.png").getImage();
    private final Image attByWhite = new ImageIcon("images\\AttByWhite.png").getImage();
    private final Image attByBlack = new ImageIcon("images\\AttByBlack.png").getImage();

    ChessPanel(ChessFrame parent) {
        super();
        this.parent = parent;
        setSize(600 , 600);
        addMouseListener(new MA());
        eventBus.register(ChannelNames.MOVE_FINISHED, this::onMoveFinished);
    }

    private Void onMoveFinished(Object o) {
        repaint();
        return null;
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
                repaint();
            } else {
                xTo = e.getX() / 75;
                yTo = 7 - e.getY() / 75;
                boardMovement.moveFigure(xFrom, yFrom, xTo, yTo);

                nullifyFromSelection();
                nullifyToSelection();
                indicatorSelect = false;
            }
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

            if (parent.getShowAvailableMoves().getState()) {
                List<Move> availableMoves = board.getElementAt(xFrom, yFrom).getAvailableMoves();
                availableMoves.forEach(move -> {
                    int[] targetPosition = move.getTargetPosition();
                    int x = targetPosition[0];
                    int y = targetPosition[1];
                    g.drawImage(moveOption, x * 75, 525 - y * 75, null);
                });

            }
        }
    }

}
