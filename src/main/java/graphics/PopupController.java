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
    }

}
