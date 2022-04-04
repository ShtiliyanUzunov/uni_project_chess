package chess.services;

import chess.Board;
import communication.ChannelNames;
import communication.EventBus;

import java.io.*;

public class GameServices {

    private EventBus eventBus = EventBus.getEventBus();

    public GameServices() {
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
    }

    private void saveGame(File outputPath) {
        FileOutputStream fos;
        ObjectOutputStream oos;
        try {
            fos = new FileOutputStream(outputPath);
            oos = new ObjectOutputStream(fos);

            oos.writeObject(GlobalContext.getBoard());
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
            fis.close();
            ois.close();

        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }

    }


}
