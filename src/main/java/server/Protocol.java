package server;

import chess.util.EncodedBoard;
import chess.util.Move;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

public class Protocol {

    @Getter
    @Setter
    public static class Message<T> {
        private String Header;
        private T body;
    }

    public static Map<String, Class<?>> headerToClassMapping;

    public static final String PLAY_MOVE = "playMove";
    public static final String ENVIRONMENT_STATE = "environmentState";
    public static final String NEW_GAME = "newGame";
    public static final String CLOSE_CONNECTION = "closeConnection";
    public static final String SAVE_GAME = "saveGame";

    static {
        headerToClassMapping = new HashMap<>();

        headerToClassMapping.put(PLAY_MOVE, Move.class);
        headerToClassMapping.put(ENVIRONMENT_STATE, null);
        headerToClassMapping.put(NEW_GAME, null);
        headerToClassMapping.put(SAVE_GAME, null);
    }

}
