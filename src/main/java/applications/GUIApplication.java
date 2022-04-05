package applications;

import chess.Game;
import chess.util.Agent;
import chess.util.GameInterface;

public class GUIApplication {

    public static void main(String[] args) {
        Game game = Game.builder()
                .gameInterface(GameInterface.GUI)
                .whiteAgent(Agent.BOT)
                .blackAgent(Agent.BOT)
                .moveDelay(500)
                .enableSystemLogging(false)
                .build();
        game.start();
    }

}
