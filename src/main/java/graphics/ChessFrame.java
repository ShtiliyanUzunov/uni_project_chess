package graphics;

import communication.ChannelNames;
import communication.EventBus;
import lombok.Getter;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.io.File;

public class ChessFrame extends JFrame {

    private final ChessPanel chessPanel;
    private final InfoPanel infoPanel;

    private JMenuItem quit;
    private JMenuItem about;
    private JMenuItem newGame;
    private JMenuItem loadGame;
    private JMenuItem saveGame;

    @Getter
    private JCheckBoxMenuItem showAttacks;
    @Getter
    private JCheckBoxMenuItem showAvailableMoves;

    private JFileChooser fileChooser;

    private final EventBus eventBus = EventBus.getEventBus();

    public ChessFrame() {
        super();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        initializeFileChooser();
        createMenuBar();

        BorderLayout l = new BorderLayout();
        setLayout(l);

        add(chessPanel = new ChessPanel(this));
        add(infoPanel = new InfoPanel(this),BorderLayout.EAST);

        setTitle("Chess Game");
        setVisible(true);
        setSize(900, 660);
        setLocationRelativeTo(null);
        setResizable(false);
        PopupController popupController = new PopupController();
        popupController.registerEvents();

    }

    private void initializeFileChooser() {
        fileChooser = new JFileChooser();
        fileChooser.setMultiSelectionEnabled(false);
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setFileFilter(new FileFilter() {

            @Override
            public String getDescription() {
                return ".chess";
            }

            @Override
            public boolean accept(File f) {
                return f.isDirectory()
                        || f.getName().toLowerCase().endsWith(".chess");
            }
        });

    }

    private void createMenuBar() {
        JMenuBar menu = new JMenuBar();
        setJMenuBar(menu);

        JMenu gameMenu;
        menu.add(gameMenu = new JMenu("Game"));

        JMenu viewMenu;
        menu.add(viewMenu = new JMenu("View"));
        viewMenu.add(showAttacks = new JCheckBoxMenuItem("Show attacks", null, false));
        viewMenu.add(showAvailableMoves = new JCheckBoxMenuItem("Show available moves", null, true));
        setShowAttacksAction();
        setShowAvailableMovesAction();

        JMenu aboutMenu;
        menu.add(aboutMenu = new JMenu("About"));

        gameMenu.add(newGame = new JMenuItem("New game"));
        gameMenu.add(saveGame = new JMenuItem("Save game"));
        gameMenu.add(loadGame = new JMenuItem("Load game"));
        gameMenu.add(quit = new JMenuItem("Quit"));
        aboutMenu.add(about = new JMenuItem("About"));

        setQuitAction();
        setNewGameAction();
        setSaveGameAction();
        setLoadGameAction();
        setAboutAction();
    }

    private void delayedRepaint() {
        try {
            Thread.sleep(1);
        } catch (InterruptedException interruptedException) {
            interruptedException.printStackTrace();
        }
        chessPanel.repaint();
    }

    private void setShowAvailableMovesAction() {
        showAvailableMoves.addChangeListener(e -> {
            delayedRepaint();
        });
    }

    private void setShowAttacksAction() {
        showAttacks.addChangeListener(e -> {
            delayedRepaint();
        });
    }

    private void setAboutAction() {
        about.addActionListener(e -> new AboutDialog());
    }

    private void setLoadGameAction() {
        loadGame.addActionListener(e -> {
            fileChooser.showOpenDialog(null);
            File input = fileChooser.getSelectedFile();
            if (input != null) {
                eventBus.post(ChannelNames.UI_LOAD_GAME, input);
                chessPanel.repaint();
            }
        });

    }

    private void setSaveGameAction() {
        saveGame.addActionListener(e -> {
            fileChooser.showSaveDialog(null);
            File output = fileChooser.getSelectedFile();

            if (output != null) {
                eventBus.post(ChannelNames.UI_SAVE_GAME, output);
            }
        });

    }

    private void setNewGameAction() {
        newGame.addActionListener(e -> {
            eventBus.post(ChannelNames.UI_NEW_GAME, null);
            chessPanel.repaint();
        });
    }

    private void setQuitAction() {
        quit.addActionListener(e -> System.exit(0));
    }


}