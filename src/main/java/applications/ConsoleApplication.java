package applications;

import chess.Game;
import chess.util.Agent;
import chess.util.GameInterface;

public class ConsoleApplication {

    public static void main(String[] args) {
        Game game = Game.builder()
                .gameInterface(GameInterface.CONSOLE)
                .whiteAgent(Agent.BOT)
                .blackAgent(Agent.BOT)
                .enableSystemLogging(false)
                .moveDelay(0)
                .build();
        game.start();
    }

}
