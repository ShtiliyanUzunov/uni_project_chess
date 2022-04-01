package chess.services;

import chess.Board;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class History {

    private boolean historyEnabled = false;

    private int currentMove = 0;

    private List<DataOutputStream> boardHistory = new ArrayList<>();

    public void saveBoardState() {
        if (!historyEnabled) {
            return;
        }

        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            Board b = GlobalContext.getBoard();
            oos.writeObject(b);
        } catch (IOException ignored) { }

    }

}
