package user_interface.graphics;

import chess.services.GlobalContext;
import communication.ChannelNames;
import communication.EventBus;

import javax.swing.*;

public class PopupController {

    public void registerEvents() {
        EventBus.getEventBus().register(ChannelNames.CHECK, (Object param) -> {
            if (GlobalContext.getConfiguration().isEnablePopups()) {
                JOptionPane.showMessageDialog(null, "Check!");

            } else {
                System.out.println("Check!");
            }
            return null;
        });

        EventBus.getEventBus().register(ChannelNames.CHECKMATE, (Object param) -> {
            if (GlobalContext.getConfiguration().isEnablePopups()) {
                JOptionPane.showMessageDialog(null, "Checkmate!!!");
            } else {
                System.out.println("Checkmate!!!");
            }
            return null;
        });

        EventBus.getEventBus().register(ChannelNames.STALEMATE, (Object param) -> {
            if (GlobalContext.getConfiguration().isEnablePopups()) {
                JOptionPane.showMessageDialog(null, "Stalemate!");
            } else {
                System.out.println("Stalemate!");
            }
            return null;
        });
    }

}
