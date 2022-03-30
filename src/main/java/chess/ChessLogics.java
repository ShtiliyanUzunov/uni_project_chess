package chess;

import figures.*;
import graphics.ChessPanel;
import org.springframework.stereotype.Service;

import javax.swing.*;

@Service
public class ChessLogics {

    //Last Move[0] = last x, Last Move[1] = last y, Last Move[2] = new x, Last Move[3] = new y
    private static int[] lastMove = new int[4];
    public static String playerTurn = "White";
    private static boolean checkIsSet = false;
    private static int[] whiteKing = {4, 0};
    private static int[] blackKing = {4, 7};


    public static int[] getLastMove() {
        return lastMove;
    }

    public static void setLastMove(int[] lastMove) {
        ChessLogics.lastMove = lastMove;
    }

    public static String getPlayerTurn() {
        return playerTurn;
    }

    public static void setPlayerTurn(String playerTurn) {
        ChessLogics.playerTurn = playerTurn;
    }

    public static boolean isCheckIsSet() {
        return checkIsSet;
    }

    public static void setCheckIsSet(boolean checkIsSet) {
        ChessLogics.checkIsSet = checkIsSet;
    }

    public static int[] getWhiteKing() {
        return whiteKing;
    }

    public static void setWhiteKing(int[] whiteKing) {
        ChessLogics.whiteKing = whiteKing;
    }

    public static int[] getBlackKing() {
        return blackKing;
    }

    public static void setBlackKing(int[] blackKing) {
        ChessLogics.blackKing = blackKing;
    }

    // Main Move Function
    public static void moveFigure(int x1, int y1, int x2, int y2) {
        boolean nPassantCondition = nPassant(x1, y1, x2, y2)
                || casteling(x1, y1, x2, y2);

        if (Board.getElementAt(x1, y1) instanceof Field) {
            ChessPanel.setCords(-1, -1, -1, -1);
            return;
        }

        boolean clearPathCondition = isPathClear(x1, y1, x2, y2)
                && Board.getElementAt(x1, y1).isMoveValid(x2, y2)
                && (!isFigureFriendly(x1, y1, x2, y2)) && checkTurn(x1, y1)
                && discoverCheck(x1, y1, x2, y2);

        if (clearPathCondition || nPassantCondition) {
            lastMove[0] = x1;
            lastMove[1] = y1;
            lastMove[2] = x2;
            lastMove[3] = y2;

            if (Board.getElementAt(x1, y1) instanceof King) {
                if (Board.getElementAt(x1, y1).getColor().equalsIgnoreCase("White")) {
                    whiteKing[0] = x2;
                    whiteKing[1] = y2;
                } else {
                    blackKing[0] = x2;
                    blackKing[1] = y2;
                }
            }
            checkIsSet = false;
            playerTurn = opositeColor(x1, y1);
            Board.getElementAt(x1, y1).setMoved(true);
            Board.getElementAt(x1, y1).setPosition(x2, y2);
            Board.setElementAt(x2, y2, Board.getElementAt(x1, y1));
            Board.setElementAt(x1, y1, new Field());
            eightRank(x2, y2);
            Board.nullBoardAttack();
            Board.setBoardAttack();

            if (kingChecked(x2, y2))
                JOptionPane.showMessageDialog(null, "Check!");

        }
    }


    private static boolean fieldCheck(String color, int x, int y) {
        try {
            if (Board.getElementAt(x, y).isAttByOpponent(color))
                return false;
            if (Board.getElementAt(x, y).getColor().equalsIgnoreCase(color))
                return false;

        } catch (Exception e) {
            return false;
        }
        return true;
    }

    private static boolean checkMate(String color) {
        if (!checkIsSet)
            return false;
        //System.out.printf("\nCheckmate Bitch\n%s", color);
        int x, y;
        if (color.equalsIgnoreCase("White")) {
            x = whiteKing[0];
            y = whiteKing[1];
        } else {
            x = blackKing[0];
            y = blackKing[1];
        }
        // Move the King
        boolean condField = fieldCheck(color, x + 1, y)
                || fieldCheck(color, x + 1, y + 1)
                || fieldCheck(color, x + 1, y - 1)
                || fieldCheck(color, x, y - 1) || fieldCheck(color, x, y + 1)
                || fieldCheck(color, x - 1, y - 1)
                || fieldCheck(color, x - 1, y)
                || fieldCheck(color, x - 1, y + 1);

        // Find check Sources
        Figure lastMovedPiece = Board.getElementAt(lastMove[2], lastMove[3]);
        Board.setElementAt(lastMove[2], lastMove[3], new Field());
        Board.nullBoardAttack();
        Board.setBoardAttack();
        Board.getElementAt(x, y).isAttByOpponent(color);
        Board.setElementAt(lastMove[2], lastMove[3], lastMovedPiece);
        Board.nullBoardAttack();
        Board.setBoardAttack();

        return !condField;
    }

    private static boolean discoverCheck(int x1, int y1, int x2, int y2) {

        if (Board.getElementAt(x1, y1) instanceof King) {
            String myColor = Board.getElementAt(x1, y1).getColor();
            Figure temp1 = Board.getElementAt(x1, y1);
            Figure temp2 = Board.getElementAt(x2, y2);
            Board.setElementAt(x1, y1, new Field());
            Board.setElementAt(x2, y2, temp1);
            Board.nullBoardAttack();
            Board.setBoardAttack();
            if (Board.getElementAt(x2, y2).isAttByOpponent(myColor)) {
                Board.setElementAt(x1, y1, temp1);
                Board.setElementAt(x2, y2, temp2);
                Board.nullBoardAttack();
                Board.setBoardAttack();
                return false;
            } else {
                Board.setElementAt(x1, y1, temp1);
                Board.setElementAt(x2, y2, temp2);
                Board.nullBoardAttack();
                Board.setBoardAttack();
                return true;
            }
        }

        Figure temp1 = Board.getElementAt(x1, y1);
        Figure temp2 = Board.getElementAt(x2, y2);
        String color = Board.getElementAt(x1, y1).getColor();
        Board.setElementAt(x1, y1, new Field());
        Board.setElementAt(x2, y2, temp1);
        Board.nullBoardAttack();
        Board.setBoardAttack();
        if (color.equalsIgnoreCase("White")) {
            if (Board.getElementAt(whiteKing[0], whiteKing[1]).isAttByOpponent(
                    "White")) {
                Board.setElementAt(x1, y1, temp1);
                Board.setElementAt(x2, y2, temp2);
                Board.nullBoardAttack();
                Board.setBoardAttack();
                return false;
            } else {
                Board.setElementAt(x1, y1, temp1);
                Board.setElementAt(x2, y2, temp2);
                Board.nullBoardAttack();
                Board.setBoardAttack();
                return true;
            }
        } else {
            if (Board.getElementAt(blackKing[0], blackKing[1]).isAttByOpponent(
                    "Black")) {
                Board.setElementAt(x1, y1, temp1);
                Board.setElementAt(x2, y2, temp2);
                Board.nullBoardAttack();
                Board.setBoardAttack();
                return false;
            } else {
                Board.setElementAt(x1, y1, temp1);
                Board.setElementAt(x2, y2, temp2);
                Board.nullBoardAttack();
                Board.setBoardAttack();
                return true;
            }
        }

    }

    private static boolean kingChecked(int x1, int y1) {
        if (Board.getElementAt(x1, y1).getColor().equalsIgnoreCase("White")) {
            if (Board.getElementAt(blackKing[0], blackKing[1]).isAttByOpponent(
                    "Black")) {
                checkIsSet = true;
                return true;
            }
        } else {
            if (Board.getElementAt(whiteKing[0], whiteKing[1]).isAttByOpponent(
                    "White")) {
                checkIsSet = true;
                return true;
            }
        }
        return false;
    }

    private static boolean isPathClear(int x1, int y1, int x2, int y2) {
        if (Board.getElementAt(x1, y1) instanceof Knight)
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
                if (!(Board.getElementAt(x1, temp1) instanceof Field))
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
                if (!(Board.getElementAt(temp1, y1) instanceof Field))
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
                if (!(Board.getElementAt(temp1, temp3) instanceof Field))
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
                if (!(Board.getElementAt(temp1, temp4) instanceof Field))
                    return false;
            }
        }
        return true;
    }

    private static boolean checkTurn(int x1, int y1) {
        return Board.getElementAt(x1, y1).getColor().equalsIgnoreCase(playerTurn);
    }

    private static String opositeColor(int x1, int y1) {
        if (Board.getElementAt(x1, y1).getColor().equalsIgnoreCase("White"))
            return "Black";
        else
            return "White";
    }

    private static boolean isFigureFriendly(int x1, int y1, int x2, int y2) {
        if (Board.getElementAt(x2, y2) instanceof Field)
            return false;
        return Board.getElementAt(x1, y1).getColor().equalsIgnoreCase(Board.getElementAt(x2, y2)
                .getColor());
    }

    // Special Moves Functions
    private static boolean casteling(int x1, int y1, int x2, int y2) {

        if (!(Board.getElementAt(x1, y1) instanceof King))
            return false;
        if (Board.getElementAt(x1, y1).isMoved())
            return false;
        if (Board.getElementAt(x1, y1).isAttByOpponent(
                Board.getElementAt(x1, y1).getColor()))
            return false;
        if (!(Math.abs(x1 - x2) == 2 && y1 == y2))
            return false;
        boolean condSideEmpty;
        boolean condSideNotAtt;
        boolean rookNotMoved;

        // Queen Side Casteling
        if (x1 > x2) {
            condSideEmpty = Board.getElementAt(x1 - 1, y1) instanceof Field
                    && Board.getElementAt(x1 - 2, y1) instanceof Field
                    && Board.getElementAt(x1 - 3, y1) instanceof Field;
            condSideNotAtt = !Board.getElementAt(x1 - 1, y1).isAttByOpponent(
                    Board.getElementAt(x1, y1).getColor())
                    && !Board.getElementAt(x1 - 2, y1).isAttByOpponent(
                    Board.getElementAt(x1, y1).getColor())
                    && !Board.getElementAt(x1 - 3, y1).isAttByOpponent(
                    Board.getElementAt(x1, y1).getColor());
            rookNotMoved = !Board.getElementAt(0, y1).isMoved()
                    && Board.getElementAt(0, y1) instanceof Rook;
            if (condSideEmpty && condSideNotAtt && rookNotMoved) {
                Board.setElementAt(3, y1, Board.getElementAt(0, y1));
                Board.setElementAt(0, y1, new Field());
                return true;
            }
        }
        // King Side Casteling
        if (x1 < x2) {
            condSideEmpty = Board.getElementAt(x1 + 1, y1) instanceof Field
                    && Board.getElementAt(x1 + 2, y1) instanceof Field;
            condSideNotAtt = !Board.getElementAt(x1 + 1, y1).isAttByOpponent(
                    Board.getElementAt(x1, y1).getColor())
                    && !Board.getElementAt(x1 + 2, y1).isAttByOpponent(
                    Board.getElementAt(x1, y1).getColor());
            rookNotMoved = !Board.getElementAt(7, y1).isMoved()
                    && Board.getElementAt(7, y1) instanceof Rook;
            if (condSideEmpty && condSideNotAtt && rookNotMoved) {
                Board.setElementAt(5, y1, Board.getElementAt(7, y1));
                Board.getElementAt(5, y1).setPosition(5, y1);
                Board.setElementAt(7, y1, new Field());
                return true;
            }
        }

        return false;
    }


    private static void eightRank(int x2, int y2) {
        if (!(Board.getElementAt(x2, y2) instanceof Pawn)) {
            return;
        }
        if ((Board.getElementAt(x2, y2).getColor().equalsIgnoreCase("White") && y2 == 7)
                || (Board.getElementAt(x2, y2).getColor().equalsIgnoreCase("Black") && y2 == 0)) {

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
                    Board.setElementAt(x2, y2, new Queen(Board.getElementAt(x2, y2)
                            .getColor(), x2, y2));
                    Board.getElementAt(x2, y2).setPosition(x2, y2);
                    break;
                }
                case 1: {
                    Board.setElementAt(x2, y2, new Rook(Board.getElementAt(x2, y2)
                            .getColor(), x2, y2));
                    Board.getElementAt(x2, y2).setPosition(x2, y2);
                    break;
                }
                case 2: {
                    Board.setElementAt(x2, y2, new Knight(Board
                            .getElementAt(x2, y2).getColor(), x2, y2));
                    Board.getElementAt(x2, y2).setPosition(x2, y2);
                    break;
                }
                case 3: {
                    Board.setElementAt(x2, y2, new Bishop(Board
                            .getElementAt(x2, y2).getColor(), x2, y2));
                    Board.getElementAt(x2, y2).setPosition(x2, y2);
                    break;
                }
            }
        }

    }

    private static boolean nPassant(int x1, int y1, int x2, int y2) {
        boolean positionCondition;
        boolean lastMoveCondition;
        if (!(Board.getElementAt(x1, y1) instanceof Pawn))
            return false;
        if (Board.getElementAt(x1, y1).getColor().equalsIgnoreCase("White")) {
            positionCondition = (y1 == 4) && (y2 == 5) && (Math.abs(x1 - x2) == 1);
            lastMoveCondition = (Board.getElementAt(lastMove[2], lastMove[3]) instanceof Pawn)
                    && (Math.abs(lastMove[1] - lastMove[3]) == 2)
                    && (x2 == lastMove[2]);
            if (positionCondition && lastMoveCondition) {
                Board.setElementAt(lastMove[2], lastMove[3], new Field());
                return true;
            }
        } else {
            positionCondition = (y1 == 3) && (y2 == 2) && (Math.abs(x1 - x2) == 1);
            lastMoveCondition = (Board.getElementAt(lastMove[2], lastMove[3]) instanceof Pawn)
                    && (Math.abs(lastMove[1] - lastMove[3]) == 2 && (x2 == lastMove[2]));
            if (positionCondition && lastMoveCondition) {
                Board.setElementAt(lastMove[2], lastMove[3], new Field());
                return true;
            }
        }
        return false;
    }

}
