package chess.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Move {

    public Move(int xFrom, int yFrom, int xTo, int yTo, String figName) {
        currentPosition = new int[] {xFrom, yFrom};
        targetPosition = new int[] {xTo, yTo};
        this.figName = figName;
    }

    private int[] currentPosition;
    private int[] targetPosition;
    private String figName;
    private static final char[] xCoords = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h'};

    public String toString() {
        return String.format("%s %c%d -> %c%d<br/>", figName, xCoords[currentPosition[0]], currentPosition[1] + 1,
                xCoords[targetPosition[0]], targetPosition[1] + 1);
    }

}
