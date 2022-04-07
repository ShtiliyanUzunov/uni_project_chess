package chess.util;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class EncodedBoard {

    private Integer [][] encodedBoard;
    private int[] whiteKingCoordinates;
    private int[] blackKingCoordinates;
    private int[] lastMove;
    private int[] material;
    private String playerTurn;
    private GameStatus gameStatus;

}
