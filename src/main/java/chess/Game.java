package chess;

import chess.agents.NaiveAgent;
import chess.services.GlobalContext;
import chess.util.Agent;
import chess.util.GameInterface;
import communication.ChannelNames;
import communication.EventBus;
import user_interface.console.ConsoleInterface;
import user_interface.graphics.ChessFrame;
import lombok.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
    private boolean enableSystemLogging;
    private int moveDelay;

    private static EventBus eventBus = EventBus.getEventBus();

    public void start() {
        configureAgent();
        configureNotifications();
        configureLogging();
        startInterface();
    }

    private void configureNotifications() {
        GlobalContext.getConfiguration().setEnablePopups(true);

        if (whiteAgent == Agent.BOT && blackAgent == Agent.BOT) {
            GlobalContext.getConfiguration().setEnablePopups(false);
        }

    }

    private void configureLogging() {
        GlobalContext.getConfiguration().setEnableLogging(enableSystemLogging);
    }

    private void configureAgent() {
        List<String> colors = new ArrayList<>();
        if (whiteAgent == Agent.BOT) {
            colors.add("White");
        }

        if (blackAgent == Agent.BOT) {
            colors.add("Black");
        }

        if (colors.size() == 0) {
            return;
        }

        naiveAgent = new NaiveAgent(moveDelay);
        Board board = GlobalContext.getBoard();

        if (whiteAgent == Agent.BOT) {
            naiveAgent.play();
        }

        eventBus.register(ChannelNames.MOVE_FINISHED, (Object param) -> {
            if (colors.contains(board.getPlayerTurn())) {
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
                new ConsoleInterface();
                break;
            case HEADLESS:
                throw new IllegalArgumentException("Headless interface not supported yet");
        }
    }

}
