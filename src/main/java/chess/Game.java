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
    private int moveDelay;

    private static EventBus eventBus = EventBus.getEventBus();

    public void start() {
        configureAgents();
        startInterface();
    }



    private void configureAgents() {
        configureAgent(whiteAgent, "white");
        configureAgent(blackAgent, "black");
    }

    private void configureAgent(Agent agent, String color) {
        if (agent == Agent.HUMAN) {
            return;
        }

        NaiveAgent naiveAgent = new NaiveAgent();

        Board board = GlobalContext.getBoard();
        eventBus.register(ChannelNames.MOVE_FINISHED, (Object param) -> {
            if (board.getPlayerTurn().equalsIgnoreCase(color)) {
                try {
                    Thread.sleep(moveDelay);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                naiveAgent.play();
            }
            return null;
        });
    }

    private void startInterface() {
        switch (gameInterface) {
            case GUI:
                Runnable guiThread = new Runnable() {
                    @Override
                    public void run() {
                        new ChessFrame();
                    }
                };
                guiThread.run();
                break;
            case CONSOLE:
                throw new IllegalArgumentException("Console interface not supported yet");
            case HEADLESS:
                throw new IllegalArgumentException("Headless interface not supported yet");
        }
    }

}
