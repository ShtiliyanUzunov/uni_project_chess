package chess;

import chess.agents.NaiveAgent;
import chess.services.GlobalContext;
import chess.util.Agent;
import chess.util.GameInterface;
import communication.ChannelNames;
import communication.EventBus;
import graphics.ChessFrame;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class Game {

    private Agent whiteAgent;
    private Agent blackAgent;
    private GameInterface gameInterface;
    private NaiveAgent naiveAgent;
    private int moveDelay;

    private static EventBus eventBus = EventBus.getEventBus();

    public void start() {
        configureAgent();
        configureNotifications();
        startInterface();

        GlobalContext.getConfiguration().setEnableHistory(true);
    }

    private void configureNotifications() {
        GlobalContext.getConfiguration().setEnablePopups(true);

        if (whiteAgent == Agent.BOT && blackAgent == Agent.BOT) {
            GlobalContext.getConfiguration().setEnablePopups(false);
        }

    }

    private void configureAgent() {
        naiveAgent = new NaiveAgent(moveDelay);
        configureAgent(whiteAgent, "white");
        configureAgent(blackAgent, "black");
    }

    private void configureAgent(Agent agent, String color) {
        if (agent == Agent.HUMAN) {
            return;
        }

        Board board = GlobalContext.getBoard();

        if (whiteAgent == Agent.BOT) {
            naiveAgent.play();
        }

        eventBus.register(ChannelNames.MOVE_FINISHED, (Object param) -> {
            if (board.getPlayerTurn().equalsIgnoreCase(color)) {
                if (!board.isGameOver()) {
                    naiveAgent.play();
                }
            }
            return null;
        });
    }

    private void startInterface() {
        switch (gameInterface) {
            case GUI:
                new ChessFrame();
                break;
            case CONSOLE:
                throw new IllegalArgumentException("Console interface not supported yet");
            case HEADLESS:
                throw new IllegalArgumentException("Headless interface not supported yet");
        }
    }

}
