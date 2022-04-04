package chess.services;

import chess.Board;
import chess.figures.Field;
import chess.figures.Figure;
import chess.util.Move;
import communication.ChannelNames;
import communication.EventBus;

import java.util.ArrayList;
import java.util.List;

public class History {

    private boolean historyEnabled = false;

    private int currentMove = 0;

    private List<Move> boardHistory = new ArrayList<>();

    public void saveMove(Move move) {
        if (!GlobalContext.getConfiguration().isEnableHistory()) {
            return;
        }

        boardHistory = boardHistory.subList(0, currentMove);
        boardHistory.add(move);
        currentMove++;
    }

    public void clear() {
        currentMove = 0;
        boardHistory.clear();
    }

    public Void moveToStart(Object obj) {
        while (currentMove != 0) {
            backwardsWrapper(false);
        }

        EventBus.getEventBus().post(ChannelNames.MOVE_FINISHED, null);
        return null;
    }

    public Void forward(Object obj) {
        forwardWrapper(true);
        return null;
    }

    private void forwardWrapper(boolean applyNotification) {
        if (!GlobalContext.getConfiguration().isEnableHistory()) {
            return;
        }

        if (currentMove == boardHistory.size()) {
            return;
        }

        restoreMove(false);
        currentMove++;

        if (applyNotification) {
            EventBus.getEventBus().post(ChannelNames.MOVE_FINISHED, null);
        }

    }

    public Void backwards(Object obj) {
        backwardsWrapper(true);
        return null;
    }

    private void backwardsWrapper(boolean applyNotification) {
        if (!GlobalContext.getConfiguration().isEnableHistory()) {
            return;
        }

        if (currentMove == 0) {
            return;
        }

        currentMove--;
        restoreMove(true);
        if (applyNotification) {
            EventBus.getEventBus().post(ChannelNames.MOVE_FINISHED, null);
        }
    }

    private void restoreMove(boolean backwards) {
        Move move = boardHistory.get(currentMove);
        Figure source = move.getSourceFigure();
        Figure target = backwards ? move.getTargetFigure(): new Field();

        int[] cordsSource = backwards ? move.getSourcePosition() : move.getTargetPosition();
        int[] cordsTarget = backwards ? move.getTargetPosition() : move.getSourcePosition();

        source.setBoard(GlobalContext.getBoard());
        target.setBoard(GlobalContext.getBoard());

        if (backwards) {
            GlobalContext.getBoard().setPlayerTurn(source.getColor());
        } else {
            GlobalContext.getBoard().setPlayerTurn(source.oppositeColor());
        }


        GlobalContext.getBoard().setElementAt(cordsSource[0], cordsSource[1], source);
        GlobalContext.getBoard().setElementAt(cordsTarget[0], cordsTarget[1], target);
    }

    public String getHistoryInfo() {
        return String.format("Move: %s/%s <br/>", currentMove, boardHistory.size());
    }

}
