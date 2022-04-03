package graphics;

import chess.Board;
import chess.services.GlobalContext;
import chess.util.Move;
import communication.ChannelNames;
import communication.EventBus;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

public class InfoPanel extends JPanel {

    private final EventBus eventBus = EventBus.getEventBus();

    private final JLabel description;

    InfoPanel(ChessFrame parent) {
        super();

        setSize(300, 600);


        JPanel container = new JPanel();
        container.setLayout(new GridLayout(1, 1));

        add(container);

        container.add(description = new JLabel());
        description.setHorizontalAlignment(SwingConstants.LEFT);

        eventBus.register(ChannelNames.UI_INFO_UPDATE, this::onInfoUpdate);
    }

    private Void onInfoUpdate(Object obj) {
        Board board = GlobalContext.getBoard();
        List<Move> moves = board.getAvailableMovesForPlayer();
        StringBuilder movesDescription = new StringBuilder();

        moves.forEach(movesDescription::append);

        String header = "INFO Panel" +
                ". . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .<br/><br/>";
        String availableMoves = String.format("Available moves: %d<br/><br/>", moves.size());
        String playerTurn =String.format("Player turn: %s<br/><br/>", board.getPlayerTurn().toUpperCase());

        String descriptionText = header + playerTurn + availableMoves + movesDescription;

        description.setText(String.format("<html>%s</html>", descriptionText));


        return null;
    }

}
