package chess.agents;

import chess.Board;
import chess.services.BoardMovement;
import chess.services.GlobalContext;
import chess.util.Move;
import communication.ChannelNames;
import communication.EventBus;
import lombok.Getter;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//Plays a random move
public class NaiveAgent {

    private final EventBus eventBus = EventBus.getEventBus();
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final int delay;
    private boolean playAllowed = true;

    public NaiveAgent(int delay) {
        this.delay = delay;
        registerEvents();
    }

    public void stop() {
        executorService.shutdown();
    }

    private synchronized void registerEvents() {
        eventBus.register(ChannelNames.PAUSE_AGENTS, (Object obj) -> {
            setPlayAllowed(false);
            return null;
        });

        eventBus.register(ChannelNames.RESUME_AGENTS, (Object obj) -> {
            if (getPlayAllowed()) {
                return null;
            }

            setPlayAllowed(true);
            play();
            return null;
        });
    }

    private synchronized void setPlayAllowed(boolean allowed) {
        playAllowed = allowed;
    }

    private synchronized boolean getPlayAllowed() {
        return playAllowed;
    }

    public synchronized void play() {
        executorService.execute(() -> {
            applyDelay();

            if (playAllowed) {
                playAMove();
            }
        });
    }

    private void applyDelay() {
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private synchronized void playAMove() {
        Board board = GlobalContext.getBoard();
        BoardMovement boardMovement = GlobalContext.getBoardMovement();

        List<Move> moves = board.getAvailableMovesForPlayer();
        Random rand = new Random();
        Move move = moves.get(rand.nextInt(moves.size()));

        int[] from = move.getSourcePosition();
        int[] to = move.getTargetPosition();
        boardMovement.moveFigure(from[0], from[1], to[0], to[1]);
    }

}
