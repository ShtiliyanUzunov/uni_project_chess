package chess.util;

import chess.figures.Figure;
import chess.services.GlobalContext;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.*;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class Move implements Serializable {

    public Move(int xFrom, int yFrom, int xTo, int yTo) {
        sourcePosition = new int[] {xFrom, yFrom};
        targetPosition = new int[] {xTo, yTo};
        this.figName = GlobalContext.getBoard().getElementAt(xFrom, yFrom).getShortName();
        nr = GlobalContext.getBoard().getHistory().getSize();
    }

    private int nr;
    private int[] sourcePosition;
    private int[] targetPosition;
    private String figName;
    private static final char[] xCoords = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h'};


    public String toString() {
        return String.format("%s %c%d -> %c%d<br/>", figName, xCoords[sourcePosition[0]], sourcePosition[1] + 1,
                xCoords[targetPosition[0]], targetPosition[1] + 1);
    }

    public String toConsoleString() {
        return String.format("%d. [%s] %c%d -> %c%d",nr, figName, xCoords[sourcePosition[0]], sourcePosition[1] + 1,
                xCoords[targetPosition[0]], targetPosition[1] + 1);
    }

}
