package chess.services;

import chess.figures.Field;
import chess.figures.Figure;
import chess.util.EncodedBoard;
import chess.util.Move;
import communication.ChannelNames;
import communication.EventBus;

import java.util.ArrayList;
import java.util.List;

public class History {

    private int currentState = 0;

    private List<EncodedBoard> boardHistory = new ArrayList<>();

    public void saveState() {
        boardHistory = boardHistory.subList(0, currentState);
        boardHistory.add(GlobalContext.getBoard().getEncodedState());
        currentState++;
    }

    public void clear() {
        currentState = 0;
        boardHistory.clear();
    }

    public Void moveToStart(Object obj) {
        currentState = 1;
        restoreState(-1);

        EventBus.getEventBus().post(ChannelNames.MOVE_FINISHED, null);
        return null;
    }

    public Void forward(Object obj) {
        forwardWrapper(true);
        return null;
    }

    private void forwardWrapper(boolean applyNotification) {
        if (currentState == boardHistory.size()) {
            return;
        }

        restoreState(0);
        currentState++;

        if (applyNotification) {
            EventBus.getEventBus().post(ChannelNames.MOVE_FINISHED, null);
        }

    }

    public Void backwards(Object obj) {
        backwardsWrapper(true);
        return null;
    }

    private void backwardsWrapper(boolean applyNotification) {
        if (currentState - 1 == 0) {
            return;
        }

        currentState--;
        restoreState(-1);
        if (applyNotification) {
            EventBus.getEventBus().post(ChannelNames.MOVE_FINISHED, null);
        }
    }

    private void restoreState(int increment) {
        EncodedBoard state = boardHistory.get(currentState + increment);
        GlobalContext.getBoard().restoreFromEncodedState(state);
    }

    public String getHistoryInfo() {
        return String.format("Move: %s/%s <br/>", currentState - 1, boardHistory.size() - 1);
    }

}
