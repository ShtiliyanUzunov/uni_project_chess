import chess.Game;
import chess.util.Agent;
import chess.util.GameInterface;

public class Application {

    public static void main(String[] args) {
        Game game = Game.builder()
                .gameInterface(GameInterface.GUI)
                .whiteAgent(Agent.HUMAN)
                .blackAgent(Agent.BOT)
                .moveDelay(1000)
                .build();
        game.start();
    }

}
