package chess;

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
        GlobalState.getBoard().initializeBoard();
        GlobalState.getBoardMovement().resetState();
    }

    private void saveGame(File outputPath) {
        FileOutputStream fos;
        ObjectOutputStream oos;
        try {
            fos = new FileOutputStream(outputPath);
            oos = new ObjectOutputStream(fos);

            oos.writeObject(GlobalState.getBoard());
            oos.writeObject(GlobalState.getBoardMovement().getLastMove());
            oos.writeObject(GlobalState.getBoardMovement().getPlayerTurn());
            oos.writeObject(GlobalState.getBoardMovement().getWhiteKing());
            oos.writeObject(GlobalState.getBoardMovement().getBlackKing());
            oos.writeBoolean(GlobalState.getBoardMovement().isCheckIsSet());
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

            GlobalState.setBoard((Board) ois.readObject());
            GlobalState.getBoardMovement().setLastMove((int[]) ois.readObject());
            GlobalState.getBoardMovement().setPlayerTurn((String) ois.readObject());
            GlobalState.getBoardMovement().setWhiteKing((int[]) ois.readObject());
            GlobalState.getBoardMovement().setBlackKing((int[]) ois.readObject());
            GlobalState.getBoardMovement().setCheckIsSet(ois.readBoolean());
            fis.close();
            ois.close();

        } catch (ClassNotFoundException | IOException e) {

            e.printStackTrace();
        }

    }


}
