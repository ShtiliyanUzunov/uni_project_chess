package chess.services;

import chess.Board;
import communication.ChannelNames;
import communication.EventBus;

import java.io.*;

public class GameOperations {

    private EventBus eventBus = EventBus.getEventBus();

    public GameOperations () {
        eventBus.register(ChannelNames.UI_NEW_GAME, (Object param) -> {
            this.newGame();
            return null;
        });

        eventBus.register(ChannelNames.UI_SAVE_GAME, (Object param) -> {
            File p = (File) param;
            this.saveGame(p);
            return null;
        });

        eventBus.register(ChannelNames.UI_LOAD_GAME, (Object param) -> {
            File p = (File) param;
            this.loadGame(p);
            return null;
        });
    }

    private void newGame() {
        GlobalContext.getBoard().initializeBoard();
        GlobalContext.getBoardMovement().resetState();
    }

    private void saveGame(File outputPath) {
        FileOutputStream fos;
        ObjectOutputStream oos;
        try {
            fos = new FileOutputStream(outputPath);
            oos = new ObjectOutputStream(fos);

            oos.writeObject(GlobalContext.getBoard());
            oos.writeObject(GlobalContext.getBoardMovement().getLastMove());
            oos.writeObject(GlobalContext.getBoardMovement().getPlayerTurn());
            oos.close();
            fos.close();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void loadGame(File inputPath) {
        FileInputStream fis;
        ObjectInputStream ois;
        try {
            fis = new FileInputStream(inputPath);
            ois = new ObjectInputStream(fis);

            GlobalContext.setBoard((Board) ois.readObject());
            GlobalContext.getBoardMovement().setLastMove((int[]) ois.readObject());
            GlobalContext.getBoardMovement().setPlayerTurn((String) ois.readObject());
            fis.close();
            ois.close();

        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }

    }


}
