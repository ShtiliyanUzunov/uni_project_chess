package chess;

import communication.EventBus;
import chess.figures.*;
import graphics.ChessPanel;
import lombok.Getter;
import lombok.Setter;

import javax.swing.*;

@Getter
@Setter
public class BoardMovement {

    private EventBus eventBus = EventBus.getEventBus();

    //Last Move[0] = last x, Last Move[1] = last y, Last Move[2] = new x, Last Move[3] = new y
    private int[] lastMove;
    private String playerTurn;
    private boolean checkIsSet;
    private int[] whiteKing;
    private int[] blackKing;

    public BoardMovement() {
        this.resetState();
    }

    // Main Move Function
    public void moveFigure(int x1, int y1, int x2, int y2) {
        Board board = GlobalState.getBoard();
        if (board.getElementAt(x1, y1) instanceof Field) {
            ChessPanel.setCords(-1, -1, -1, -1);
            return;
        }

        boolean nPassantCondition = nPassant(x1, y1, x2, y2)
                || casteling(x1, y1, x2, y2);


        boolean clearPathCondition =
                isPathClear(x1, y1, x2, y2)
                        && !isFigureFriendly(x1, y1, x2, y2)
                        && board.getElementAt(x1, y1).isTargetLocationValid(x2, y2)
                        && checkTurn(x1, y1)
                        && discoverCheck(x1, y1, x2, y2);

        if (clearPathCondition || nPassantCondition) {
            lastMove[0] = x1;
            lastMove[1] = y1;
            lastMove[2] = x2;
            lastMove[3] = y2;

            if (board.getElementAt(x1, y1) instanceof King) {
                if (board.getElementAt(x1, y1).getColor().equalsIgnoreCase("White")) {
                    whiteKing[0] = x2;
                    whiteKing[1] = y2;
                } else {
                    blackKing[0] = x2;
                    blackKing[1] = y2;
                }
            }
            checkIsSet = false;
            playerTurn = opositeColor(x1, y1);
            board.getElementAt(x1, y1).setMoved(true);
            board.getElementAt(x1, y1).setPosition(x2, y2);
            board.setElementAt(x2, y2, board.getElementAt(x1, y1));
            board.setElementAt(x1, y1, new Field());
            eightRank(x2, y2);
            board.nullBoardAttack();
            board.setBoardAttack();

            if (kingChecked(x2, y2))
                JOptionPane.showMessageDialog(null, "Check!");

        }
    }

    private boolean discoverCheck(int x1, int y1, int x2, int y2) {
        Board board = GlobalState.getBoard();

        if (board.getElementAt(x1, y1) instanceof King) {
            String myColor = board.getElementAt(x1, y1).getColor();
            Figure temp1 = board.getElementAt(x1, y1);
            Figure temp2 = board.getElementAt(x2, y2);
            board.setElementAt(x1, y1, new Field());
            board.setElementAt(x2, y2, temp1);
            board.nullBoardAttack();
            board.setBoardAttack();
            if (board.getElementAt(x2, y2).isAttByOpponent(myColor)) {
                board.setElementAt(x1, y1, temp1);
                board.setElementAt(x2, y2, temp2);
                board.nullBoardAttack();
                board.setBoardAttack();
                return false;
            } else {
                board.setElementAt(x1, y1, temp1);
                board.setElementAt(x2, y2, temp2);
                board.nullBoardAttack();
                board.setBoardAttack();
                return true;
            }
        }

        Figure temp1 = board.getElementAt(x1, y1);
        Figure temp2 = board.getElementAt(x2, y2);
        String color = board.getElementAt(x1, y1).getColor();
        board.setElementAt(x1, y1, new Field());
        board.setElementAt(x2, y2, temp1);
        board.nullBoardAttack();
        board.setBoardAttack();
        if (color.equalsIgnoreCase("White")) {
            if (board.getElementAt(whiteKing[0], whiteKing[1]).isAttByOpponent(
                    "White")) {
                board.setElementAt(x1, y1, temp1);
                board.setElementAt(x2, y2, temp2);
                board.nullBoardAttack();
                board.setBoardAttack();
                return false;
            } else {
                board.setElementAt(x1, y1, temp1);
                board.setElementAt(x2, y2, temp2);
                board.nullBoardAttack();
                board.setBoardAttack();
                return true;
            }
        } else {
            if (board.getElementAt(blackKing[0], blackKing[1]).isAttByOpponent(
                    "Black")) {
                board.setElementAt(x1, y1, temp1);
                board.setElementAt(x2, y2, temp2);
                board.nullBoardAttack();
                board.setBoardAttack();
                return false;
            } else {
                board.setElementAt(x1, y1, temp1);
                board.setElementAt(x2, y2, temp2);
                board.nullBoardAttack();
                board.setBoardAttack();
                return true;
            }
        }

    }

    private boolean kingChecked(int x1, int y1) {
        Board board = GlobalState.getBoard();

        if (board.getElementAt(x1, y1).getColor().equalsIgnoreCase("White")) {
            if (board.getElementAt(blackKing[0], blackKing[1]).isAttByOpponent(
                    "Black")) {
                checkIsSet = true;
                return true;
            }
        } else {
            if (board.getElementAt(whiteKing[0], whiteKing[1]).isAttByOpponent(
                    "White")) {
                checkIsSet = true;
                return true;
            }
        }
        return false;
    }

    public boolean isPathClear(int x1, int y1, int x2, int y2) {
        Board board = GlobalState.getBoard();

        if (board.getElementAt(x1, y1) instanceof Knight)
            return true;

        int temp1, temp2, temp3, temp4;
        if (x1 == x2) {
            if (y1 > y2) {
                temp1 = y2;
                temp2 = y1;
            } else {
                temp1 = y1;
                temp2 = y2;
            }
            for (temp1 = temp1 + 1; temp1 < temp2; temp1++) {
                if (!(board.getElementAt(x1, temp1) instanceof Field))
                    return false;
            }
        }

        if (y1 == y2) {
            if (x1 > x2) {
                temp1 = x2;
                temp2 = x1;
            } else {
                temp1 = x1;
                temp2 = x2;
            }
            for (temp1 = temp1 + 1; temp1 < temp2; temp1++) {
                if (!(board.getElementAt(temp1, y1) instanceof Field))
                    return false;
            }
        }

        if (((x1 > x2) && (y1 > y2)) || ((x1 < x2) && (y1 < y2))) {
            if (x1 > x2) {
                temp1 = x2;
                temp2 = x1;
                temp3 = y2;
                temp4 = y1;
            } else {
                temp1 = x1;
                temp2 = x2;
                temp3 = y1;
                temp4 = y2;
            }
            for (temp1 = temp1 + 1, temp3 = temp3 + 1; temp1 < temp2
                    && temp3 < temp4; temp1++, temp3++) {
                if (!(board.getElementAt(temp1, temp3) instanceof Field))
                    return false;
            }
        }

        if (((x1 > x2) && (y1 < y2)) || ((x1 < x2) && (y1 > y2))) {
            // temp1 - po-malkoto x
            // temp 2 -po-golqmoto x
            // temp 3 - po-malkoto y
            // temp 4 - pogolqmoto y
            if (x1 > x2) {
                temp1 = x2;
                temp2 = x1;
                temp3 = y1;
                temp4 = y2;
            } else {
                temp1 = x1;
                temp2 = x2;
                temp3 = y2;
                temp4 = y1;
            }
            for (temp1 = temp1 + 1, temp4 = temp4 - 1; temp1 < temp2
                    && temp4 > temp3; temp1++, temp4--) {
                if (!(board.getElementAt(temp1, temp4) instanceof Field))
                    return false;
            }
        }
        return true;
    }

    private boolean checkTurn(int x1, int y1) {
        Board board = GlobalState.getBoard();
        return board.getElementAt(x1, y1).getColor().equalsIgnoreCase(playerTurn);
    }

    private String opositeColor(int x1, int y1) {
        Board board = GlobalState.getBoard();
        if (board.getElementAt(x1, y1).getColor().equalsIgnoreCase("White"))
            return "Black";
        else
            return "White";
    }

    private boolean isFigureFriendly(int x1, int y1, int x2, int y2) {
        Board board = GlobalState.getBoard();
        if (board.getElementAt(x2, y2) instanceof Field)
            return false;
        return board.getElementAt(x1, y1).getColor().equalsIgnoreCase(board.getElementAt(x2, y2)
                .getColor());
    }

    // Special Moves Functions
    private boolean casteling(int x1, int y1, int x2, int y2) {
        Board board = GlobalState.getBoard();
        if (!(board.getElementAt(x1, y1) instanceof King))
            return false;
        if (board.getElementAt(x1, y1).isMoved())
            return false;
        if (board.getElementAt(x1, y1).isAttByOpponent(
                board.getElementAt(x1, y1).getColor()))
            return false;
        if (!(Math.abs(x1 - x2) == 2 && y1 == y2))
            return false;
        boolean condSideEmpty;
        boolean condSideNotAtt;
        boolean rookNotMoved;

        // Queen Side Casteling
        if (x1 > x2) {
            condSideEmpty = board.getElementAt(x1 - 1, y1) instanceof Field
                    && board.getElementAt(x1 - 2, y1) instanceof Field
                    && board.getElementAt(x1 - 3, y1) instanceof Field;
            condSideNotAtt = !board.getElementAt(x1 - 1, y1).isAttByOpponent(
                    board.getElementAt(x1, y1).getColor())
                    && !board.getElementAt(x1 - 2, y1).isAttByOpponent(
                    board.getElementAt(x1, y1).getColor())
                    && !board.getElementAt(x1 - 3, y1).isAttByOpponent(
                    board.getElementAt(x1, y1).getColor());
            rookNotMoved = !board.getElementAt(0, y1).isMoved()
                    && board.getElementAt(0, y1) instanceof Rook;
            if (condSideEmpty && condSideNotAtt && rookNotMoved) {
                board.setElementAt(3, y1, board.getElementAt(0, y1));
                board.setElementAt(0, y1, new Field());
                return true;
            }
        }
        // King Side Casteling
        if (x1 < x2) {
            condSideEmpty = board.getElementAt(x1 + 1, y1) instanceof Field
                    && board.getElementAt(x1 + 2, y1) instanceof Field;
            condSideNotAtt = !board.getElementAt(x1 + 1, y1).isAttByOpponent(
                    board.getElementAt(x1, y1).getColor())
                    && !board.getElementAt(x1 + 2, y1).isAttByOpponent(
                    board.getElementAt(x1, y1).getColor());
            rookNotMoved = !board.getElementAt(7, y1).isMoved()
                    && board.getElementAt(7, y1) instanceof Rook;
            if (condSideEmpty && condSideNotAtt && rookNotMoved) {
                board.setElementAt(5, y1, board.getElementAt(7, y1));
                board.getElementAt(5, y1).setPosition(5, y1);
                board.setElementAt(7, y1, new Field());
                return true;
            }
        }

        return false;
    }


    private void eightRank(int x2, int y2) {
        Board board = GlobalState.getBoard();

        if (!(board.getElementAt(x2, y2) instanceof Pawn)) {
            return;
        }
        if ((board.getElementAt(x2, y2).getColor().equalsIgnoreCase("White") && y2 == 7)
                || (board.getElementAt(x2, y2).getColor().equalsIgnoreCase("Black") && y2 == 0)) {

            String[] choice = new String[4];
            choice[0] = "Queen";
            choice[1] = "Rook";
            choice[2] = "Knight";
            choice[3] = "Bishop";

            int a = JOptionPane.showOptionDialog(null, "Chess",
                    "Choose a Figure", JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE, null, choice, null);

            switch (a) {
                case -1:
                    eightRank(x2, y2);
                    break;
                case 0: {
                    board.setElementAt(x2, y2, new Queen(board.getElementAt(x2, y2)
                            .getColor(), x2, y2, board));
                    board.getElementAt(x2, y2).setPosition(x2, y2);
                    break;
                }
                case 1: {
                    board.setElementAt(x2, y2, new Rook(board.getElementAt(x2, y2)
                            .getColor(), x2, y2, board));
                    board.getElementAt(x2, y2).setPosition(x2, y2);
                    break;
                }
                case 2: {
                    board.setElementAt(x2, y2, new Knight(board
                            .getElementAt(x2, y2).getColor(), x2, y2, board));
                    board.getElementAt(x2, y2).setPosition(x2, y2);
                    break;
                }
                case 3: {
                    board.setElementAt(x2, y2, new Bishop(board
                            .getElementAt(x2, y2).getColor(), x2, y2, board));
                    board.getElementAt(x2, y2).setPosition(x2, y2);
                    break;
                }
            }
        }

    }

    private boolean nPassant(int x1, int y1, int x2, int y2) {
        Board board = GlobalState.getBoard();

        boolean positionCondition;
        boolean lastMoveCondition;
        if (!(board.getElementAt(x1, y1) instanceof Pawn))
            return false;
        if (board.getElementAt(x1, y1).getColor().equalsIgnoreCase("White")) {
            positionCondition = (y1 == 4) && (y2 == 5) && (Math.abs(x1 - x2) == 1);
            lastMoveCondition = (board.getElementAt(lastMove[2], lastMove[3]) instanceof Pawn)
                    && (Math.abs(lastMove[1] - lastMove[3]) == 2)
                    && (x2 == lastMove[2]);
            if (positionCondition && lastMoveCondition) {
                board.setElementAt(lastMove[2], lastMove[3], new Field());
                return true;
            }
        } else {
            positionCondition = (y1 == 3) && (y2 == 2) && (Math.abs(x1 - x2) == 1);
            lastMoveCondition = (board.getElementAt(lastMove[2], lastMove[3]) instanceof Pawn)
                    && (Math.abs(lastMove[1] - lastMove[3]) == 2 && (x2 == lastMove[2]));
            if (positionCondition && lastMoveCondition) {
                board.setElementAt(lastMove[2], lastMove[3], new Field());
                return true;
            }
        }
        return false;
    }

    public void resetState() {
        this.setLastMove(new int[4]);
        this.setPlayerTurn("White");
        this.setCheckIsSet(false);
        int[] whiteKing = {4, 0};
        int[] blackKing = {4, 7};
        this.setWhiteKing(whiteKing);
        this.setBlackKing(blackKing);
    }

}
