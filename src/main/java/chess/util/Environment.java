package chess.util;

import lombok.Builder;

import java.util.List;

@Builder
public class Environment {

    List<Move> availableMoves;
    EncodedBoard state;

}
