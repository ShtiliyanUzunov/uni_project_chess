package user_interface.headless;

import chess.services.GlobalContext;
import chess.util.Move;
import communication.ChannelNames;
import communication.EventBus;

public class HeadlessInterface {

    private EventBus eventBus = EventBus.getEventBus();

    public HeadlessInterface() {
        EventBus.getEventBus().register(ChannelNames.CHECKMATE, (Object param) -> {
            printNrOfMoves();
            System.out.println("Checkmate!!!");
            return null;
        });

        EventBus.getEventBus().register(ChannelNames.STALEMATE, (Object param) -> {
            printNrOfMoves();
            System.out.println("Stalemate!");
            return null;
        });

        EventBus.getEventBus().register(ChannelNames.INSUFFICIENT_MATERIAL, (Object param) -> {
            printNrOfMoves();
            System.out.println("Insufficient material!");
            return null;
        });
    }

    private void printNrOfMoves() {
        int size = GlobalContext.getBoard().getHistory().getSize();
        System.out.printf("Game ended in %d moves%n", size);
    }

}
