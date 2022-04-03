package graphics;

import communication.ChannelNames;
import communication.EventBus;

import javax.swing.*;

public class PopupController {

    public void registerEvents() {
        EventBus.getEventBus().register(ChannelNames.UI_CHECK, (Object param) -> {
            JOptionPane.showMessageDialog(null, "Check!");
            return null;
        });

        EventBus.getEventBus().register(ChannelNames.UI_CHECKMATE, (Object param) -> {
            JOptionPane.showMessageDialog(null, "Checkmate!!!");
            return null;
        });

        EventBus.getEventBus().register(ChannelNames.UI_STALEMATE, (Object param) -> {
            JOptionPane.showMessageDialog(null, "Stalemate!");
            return null;
        });
    }

}
