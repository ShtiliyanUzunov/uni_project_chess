package graphics;

import chess.Chess;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.io.File;

public class ChessFrame extends JFrame {

	private final ChessPanel chessPanel;

	private JMenuItem quit;
	private JMenuItem about;
	private JMenuItem newGame;
	private JMenuItem loadGame;
	private JMenuItem saveGame;
	
	private JFileChooser fileChooser;
	
	public ChessFrame() {
		super();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		initializeFileChooser();
		createMenuBar();
		add(chessPanel = new ChessPanel());
		setTitle("Chess Game");
		setVisible(true);
		setSize(606, 652);
		setLocationRelativeTo(null);
		setResizable(false);
		
	}

	
	private void initializeFileChooser() {
		fileChooser= new JFileChooser();
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
		//private JMenu optionsMenu;
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


	private void setAboutAction() {
		about.addActionListener(e -> new AboutDialog());
	}


	private void setLoadGameAction() {
		loadGame.addActionListener(e -> {
			fileChooser.showOpenDialog(null);
			File input = fileChooser.getSelectedFile();
			if (input!=null){
				Chess.loadGame(input);

				chessPanel.repaint();
			}
		});
		
	}


	private void setSaveGameAction() {
		saveGame.addActionListener(e -> {
			fileChooser.showSaveDialog(null);
			File output = fileChooser.getSelectedFile();

			if(output!=null){
				Chess.saveGame(output);
			}
		});
		
	}


	private void setNewGameAction() {
		newGame.addActionListener(e -> {
			Chess.initializeBoard();
			chessPanel.repaint();
		});
	}


	private void setQuitAction() {
		quit.addActionListener(e -> System.exit(0));
	}



}