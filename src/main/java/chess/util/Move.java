package chess.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Move {

    public Move(int xFrom, int yFrom, int xTo, int yTo) {
        currentPosition = new int[] {xFrom, yFrom};
        targetPosition = new int[] {xTo, yTo};
    }

    private int[] currentPosition;
    private int[] targetPosition;

}
