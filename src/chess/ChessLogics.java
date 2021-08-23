package chess;

import figures.*;
import graphics.ChessPanel;

import javax.swing.*;

public abstract class ChessLogics {

    //Last Move[0] = last x, Last Move[1] = last y, Last Move[2] = new x, Last Move[3] = new y
    private static int[] LastMove = new int[4];
    public static String playerTurn = "White";
    private static boolean checkIsSet = false;
    private static int[] WhiteKing = {4, 0};
    private static int[] BlackKing = {4, 7};


    public static int[] getLastMove() {
        return LastMove;
    }

    public static void setLastMove(int[] lastMove) {
        LastMove = lastMove;
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
        return WhiteKing;
    }

    public static void setWhiteKing(int[] whiteKing) {
        WhiteKing = whiteKing;
    }

    public static int[] getBlackKing() {
        return BlackKing;
    }

    public static void setBlackKing(int[] blackKing) {
        BlackKing = blackKing;
    }

    // Main Move Function
    public static void moveFigure(int x1, int y1, int x2, int y2) {
        boolean conditionSpecial = nPassant(x1, y1, x2, y2)
                || casteling(x1, y1, x2, y2);
        boolean condition;

        if (Chess.getElementAt(x1, y1) instanceof Field) {
            ChessPanel.setCords(-1, -1, -1, -1);
            return;
        }

        condition = isPathClear(x1, y1, x2, y2)
                && Chess.getElementAt(x1, y1).isMoveValid(x2, y2)
                && (!isFigureFriendly(x1, y1, x2, y2)) && checkTurn(x1, y1)
                && discoverCheck(x1, y1, x2, y2);

        if (condition || conditionSpecial) {
            LastMove[0] = x1;
            LastMove[1] = y1;
            LastMove[2] = x2;
            LastMove[3] = y2;

            if (Chess.getElementAt(x1, y1) instanceof King) {
                if (Chess.getElementAt(x1, y1).getColor().equalsIgnoreCase("White")) {
                    WhiteKing[0] = x2;
                    WhiteKing[1] = y2;
                } else {
                    BlackKing[0] = x2;
                    BlackKing[1] = y2;
                }
            }
            checkIsSet = false;
            playerTurn = opositeColor(x1, y1);
            Chess.getElementAt(x1, y1).setMoved(true);
            Chess.getElementAt(x1, y1).setPosition(x2, y2);
            Chess.setElementAt(x2, y2, Chess.getElementAt(x1, y1));
            Chess.setElementAt(x1, y1, new Field());
            eightRank(x2, y2);
            Chess.nullBoardAttack();
            Chess.setBoardAttack();

            if (kingChecked(x2, y2))
                JOptionPane.showMessageDialog(null, "Check!");

        }
    }

    // to be Done

    private static boolean fieldCheck(String color, int x, int y) {
        try {
            if (Chess.getElementAt(x, y).isAttByOponent(color))
                return false;
            if (Chess.getElementAt(x, y).getColor().equalsIgnoreCase(color))
                return false;

        } catch (Exception e) {
            return false;
        }
        return true;
    }

    // To Be Done
    private static boolean checkMate(String color) {
        if (!checkIsSet)
            return false;
        //System.out.printf("\nCheckmate Bitch\n%s", color);
        int x, y;
        if (color.equalsIgnoreCase("White")) {
            x = WhiteKing[0];
            y = WhiteKing[1];
        } else {
            x = BlackKing[0];
            y = BlackKing[1];
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
        Figure lastMovedPiece = Chess.getElementAt(LastMove[2], LastMove[3]);
        Chess.setElementAt(LastMove[2], LastMove[3], new Field());
        Chess.nullBoardAttack();
        Chess.setBoardAttack();
        Chess.getElementAt(x, y).isAttByOponent(color);
        Chess.setElementAt(LastMove[2], LastMove[3], lastMovedPiece);
        Chess.nullBoardAttack();
        Chess.setBoardAttack();

        return !condField;
    }

    private static boolean discoverCheck(int x1, int y1, int x2, int y2) {

        if (Chess.getElementAt(x1, y1) instanceof King) {
            String myColor = Chess.getElementAt(x1, y1).getColor();
            Figure temp1 = Chess.getElementAt(x1, y1);
            Figure temp2 = Chess.getElementAt(x2, y2);
            Chess.setElementAt(x1, y1, new Field());
            Chess.setElementAt(x2, y2, temp1);
            Chess.nullBoardAttack();
            Chess.setBoardAttack();
            if (Chess.getElementAt(x2, y2).isAttByOponent(myColor)) {
                Chess.setElementAt(x1, y1, temp1);
                Chess.setElementAt(x2, y2, temp2);
                Chess.nullBoardAttack();
                Chess.setBoardAttack();
                return false;
            } else {
                Chess.setElementAt(x1, y1, temp1);
                Chess.setElementAt(x2, y2, temp2);
                Chess.nullBoardAttack();
                Chess.setBoardAttack();
                return true;
            }
        }

        Figure temp1 = Chess.getElementAt(x1, y1);
        Figure temp2 = Chess.getElementAt(x2, y2);
        String color = Chess.getElementAt(x1, y1).getColor();
        Chess.setElementAt(x1, y1, new Field());
        Chess.setElementAt(x2, y2, temp1);
        Chess.nullBoardAttack();
        Chess.setBoardAttack();
        if (color.equalsIgnoreCase("White")) {
            if (Chess.getElementAt(WhiteKing[0], WhiteKing[1]).isAttByOponent(
                    "White")) {
                Chess.setElementAt(x1, y1, temp1);
                Chess.setElementAt(x2, y2, temp2);
                Chess.nullBoardAttack();
                Chess.setBoardAttack();
                return false;
            } else {
                Chess.setElementAt(x1, y1, temp1);
                Chess.setElementAt(x2, y2, temp2);
                Chess.nullBoardAttack();
                Chess.setBoardAttack();
                return true;
            }
        } else {
            if (Chess.getElementAt(BlackKing[0], BlackKing[1]).isAttByOponent(
                    "Black")) {
                Chess.setElementAt(x1, y1, temp1);
                Chess.setElementAt(x2, y2, temp2);
                Chess.nullBoardAttack();
                Chess.setBoardAttack();
                return false;
            } else {
                Chess.setElementAt(x1, y1, temp1);
                Chess.setElementAt(x2, y2, temp2);
                Chess.nullBoardAttack();
                Chess.setBoardAttack();
                return true;
            }
        }

    }

    private static boolean kingChecked(int x1, int y1) {
        if (Chess.getElementAt(x1, y1).getColor().equalsIgnoreCase("White")) {
            if (Chess.getElementAt(BlackKing[0], BlackKing[1]).isAttByOponent(
                    "Black")) {
                checkIsSet = true;
                return true;
            }
        } else {
            if (Chess.getElementAt(WhiteKing[0], WhiteKing[1]).isAttByOponent(
                    "White")) {
                checkIsSet = true;
                return true;
            }
        }
        return false;
    }

    private static boolean isPathClear(int x1, int y1, int x2, int y2) {
        if (Chess.getElementAt(x1, y1) instanceof Knight)
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
                if (!(Chess.getElementAt(x1, temp1) instanceof Field))
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
                if (!(Chess.getElementAt(temp1, y1) instanceof Field))
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
                if (!(Chess.getElementAt(temp1, temp3) instanceof Field))
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
                if (!(Chess.getElementAt(temp1, temp4) instanceof Field))
                    return false;
            }
        }
        return true;
    }

    private static boolean checkTurn(int x1, int y1) {
        return Chess.getElementAt(x1, y1).getColor().equalsIgnoreCase(playerTurn);
    }

    private static String opositeColor(int x1, int y1) {
        if (Chess.getElementAt(x1, y1).getColor().equalsIgnoreCase("White"))
            return "Black";
        else
            return "White";
    }

    private static boolean isFigureFriendly(int x1, int y1, int x2, int y2) {
        if (Chess.getElementAt(x2, y2) instanceof Field)
            return false;
        return Chess.getElementAt(x1, y1).getColor().equalsIgnoreCase(Chess.getElementAt(x2, y2)
                .getColor());
    }

    // Special Moves Functions
    private static boolean casteling(int x1, int y1, int x2, int y2) {

        if (!(Chess.getElementAt(x1, y1) instanceof King))
            return false;
        if (Chess.getElementAt(x1, y1).isMoved())
            return false;
        if (Chess.getElementAt(x1, y1).isAttByOponent(
                Chess.getElementAt(x1, y1).getColor()))
            return false;
        if (!(Math.abs(x1 - x2) == 2 && y1 == y2))
            return false;
        boolean condSideEmpty;
        boolean condSideNotAtt;
        boolean rookNotMoved;

        // Queen Side Casteling
        if (x1 > x2) {
            condSideEmpty = Chess.getElementAt(x1 - 1, y1) instanceof Field
                    && Chess.getElementAt(x1 - 2, y1) instanceof Field
                    && Chess.getElementAt(x1 - 3, y1) instanceof Field;
            condSideNotAtt = !Chess.getElementAt(x1 - 1, y1).isAttByOponent(
                    Chess.getElementAt(x1, y1).getColor())
                    && !Chess.getElementAt(x1 - 2, y1).isAttByOponent(
                    Chess.getElementAt(x1, y1).getColor())
                    && !Chess.getElementAt(x1 - 3, y1).isAttByOponent(
                    Chess.getElementAt(x1, y1).getColor());
            rookNotMoved = !Chess.getElementAt(0, y1).isMoved()
                    && Chess.getElementAt(0, y1) instanceof Rook;
            if (condSideEmpty && condSideNotAtt && rookNotMoved) {
                Chess.setElementAt(3, y1, Chess.getElementAt(0, y1));
                Chess.setElementAt(0, y1, new Field());
                return true;
            }
        }
        // King Side Casteling
        if (x1 < x2) {
            condSideEmpty = Chess.getElementAt(x1 + 1, y1) instanceof Field
                    && Chess.getElementAt(x1 + 2, y1) instanceof Field;
            condSideNotAtt = !Chess.getElementAt(x1 + 1, y1).isAttByOponent(
                    Chess.getElementAt(x1, y1).getColor())
                    && !Chess.getElementAt(x1 + 2, y1).isAttByOponent(
                    Chess.getElementAt(x1, y1).getColor());
            rookNotMoved = !Chess.getElementAt(7, y1).isMoved()
                    && Chess.getElementAt(7, y1) instanceof Rook;
            if (condSideEmpty && condSideNotAtt && rookNotMoved) {
                Chess.setElementAt(5, y1, Chess.getElementAt(7, y1));
                Chess.getElementAt(5, y1).setPosition(5, y1);
                Chess.setElementAt(7, y1, new Field());
                return true;
            }
        }

        return false;
    }


    private static void eightRank(int x2, int y2) {
        if (!(Chess.getElementAt(x2, y2) instanceof Pawn)) {
            return;
        }
        if ((Chess.getElementAt(x2, y2).getColor().equalsIgnoreCase("White") && y2 == 7)
                || (Chess.getElementAt(x2, y2).getColor().equalsIgnoreCase("Black") && y2 == 0)) {

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
                    Chess.setElementAt(x2, y2, new Queen(Chess.getElementAt(x2, y2)
                            .getColor(), x2, y2));
                    Chess.getElementAt(x2, y2).setPosition(x2, y2);
                    break;
                }
                case 1: {
                    Chess.setElementAt(x2, y2, new Rook(Chess.getElementAt(x2, y2)
                            .getColor(), x2, y2));
                    Chess.getElementAt(x2, y2).setPosition(x2, y2);
                    break;
                }
                case 2: {
                    Chess.setElementAt(x2, y2, new Knight(Chess
                            .getElementAt(x2, y2).getColor(), x2, y2));
                    Chess.getElementAt(x2, y2).setPosition(x2, y2);
                    break;
                }
                case 3: {
                    Chess.setElementAt(x2, y2, new Bishop(Chess
                            .getElementAt(x2, y2).getColor(), x2, y2));
                    Chess.getElementAt(x2, y2).setPosition(x2, y2);
                    break;
                }
            }
        }

    }

    private static boolean nPassant(int x1, int y1, int x2, int y2) {
        boolean positionCondition;
        boolean lastMoveCondition;
        if (!(Chess.getElementAt(x1, y1) instanceof Pawn))
            return false;
        if (Chess.getElementAt(x1, y1).getColor().equalsIgnoreCase("White")) {
            positionCondition = (y1 == 4) && (y2 == 5) && (Math.abs(x1 - x2) == 1);
            lastMoveCondition = (Chess.getElementAt(LastMove[2], LastMove[3]) instanceof Pawn)
                    && (Math.abs(LastMove[1] - LastMove[3]) == 2)
                    && (x2 == LastMove[2]);
            if (positionCondition && lastMoveCondition) {
                Chess.setElementAt(LastMove[2], LastMove[3], new Field());
                return true;
            }
        } else {
            positionCondition = (y1 == 3) && (y2 == 2) && (Math.abs(x1 - x2) == 1);
            lastMoveCondition = (Chess.getElementAt(LastMove[2], LastMove[3]) instanceof Pawn)
                    && (Math.abs(LastMove[1] - LastMove[3]) == 2 && (x2 == LastMove[2]));
            if (positionCondition && lastMoveCondition) {
                Chess.setElementAt(LastMove[2], LastMove[3], new Field());
                return true;
            }
        }
        return false;
    }

}
