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

    public Move(int xFrom, int yFrom, int xTo, int yTo, boolean cloneFigures) {
        sourcePosition = new int[] {xFrom, yFrom};
        targetPosition = new int[] {xTo, yTo};
        this.figName = GlobalContext.getBoard().getElementAt(xFrom, yFrom).getShortName();

        if (cloneFigures) {
            sourceFigure = deepClone(GlobalContext.getBoard().getElementAt(xFrom, yFrom));
            targetFigure = deepClone(GlobalContext.getBoard().getElementAt(xTo, yTo));
        }
    }

    private int[] sourcePosition;
    private int[] targetPosition;
    private Figure sourceFigure;
    private Figure targetFigure;
    private String figName;
    private static final char[] xCoords = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h'};

    public Figure getSourceFigure() {
        return deepClone(sourceFigure);
    }

    public Figure getTargetFigure() {
        return deepClone(targetFigure);
    }

    @PureFunction
    private Figure deepClone(Figure object) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(object);
            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(bais);
            return (Figure)ois.readObject();
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String toString() {
        return String.format("%s %c%d -> %c%d<br/>", figName, xCoords[sourcePosition[0]], sourcePosition[1] + 1,
                xCoords[targetPosition[0]], targetPosition[1] + 1);
    }

}
