package user_interface.console;

import chess.util.Move;
import communication.ChannelNames;
import communication.EventBus;

public class ConsoleInterface {

    private EventBus eventBus = EventBus.getEventBus();

    public ConsoleInterface() {
        eventBus.register(ChannelNames.MOVE_FINISHED, (Object obj) -> {
            if (obj == null) {
                return null;
            }

            Move move = (Move) obj;
            System.out.println(move.toConsoleString());

            return null;
        });

        EventBus.getEventBus().register(ChannelNames.CHECK, (Object param) -> {
            System.out.println("Check!");
            return null;
        });

        EventBus.getEventBus().register(ChannelNames.CHECKMATE, (Object param) -> {
            System.out.println("Checkmate!!!");
            return null;
        });

        EventBus.getEventBus().register(ChannelNames.STALEMATE, (Object param) -> {
            System.out.println("Stalemate!");
            return null;
        });

        EventBus.getEventBus().register(ChannelNames.INSUFFICIENT_MATERIAL, (Object param) -> {
            System.out.println("Insufficient material!");
            return null;
        });
    }

}
