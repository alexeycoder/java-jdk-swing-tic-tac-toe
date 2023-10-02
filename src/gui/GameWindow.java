package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import domain.Game;
import domain.Player;

public class GameWindow extends JFrame {

	private static final String TITLE_STR = "Крестики-Нолики";
	private static final String NEW_GAME_STR = "Новая игра";
	private static final String EXIT_STR = "Выход";

	private static final int WIDTH = 500;
	private static final int HEIGHT = 500;

	private final JButton buttonStart;
	private final JButton buttonExit;
	private final GameFieldPanel panelGameField;
	private final SettingsWindow frameSettings;

	public GameWindow() {

		super.setDefaultCloseOperation(EXIT_ON_CLOSE);
		super.setTitle(TITLE_STR);
		super.setMinimumSize(new Dimension(WIDTH, HEIGHT));
		super.setLocationRelativeTo(null);

		panelGameField = new GameFieldPanel();
		buttonStart = new JButton(NEW_GAME_STR);
		buttonExit = new JButton(EXIT_STR);

		JPanel panelBottom = new JPanel(new GridLayout(1, 2));
		panelBottom.add(buttonStart);
		panelBottom.add(buttonExit);

		super.getContentPane().add(panelBottom, BorderLayout.SOUTH);
		super.getContentPane().add(panelGameField);

		frameSettings = new SettingsWindow(this);

		assignListeners();

		super.setVisible(true);
		frameSettings.setVisible(true);
	}

	private void assignListeners() {

		buttonExit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// System.exit(0);
				GameWindow.this
						.dispatchEvent(new WindowEvent(GameWindow.this, WindowEvent.WINDOW_CLOSING));
			}
		});

		buttonStart.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				frameSettings.setVisible(true);
			}
		});
	}

	public void startNewGame(int mode, Player humanPlayer, int cols, int rows, int winLen) {
		panelGameField.startNewGame(new Game(cols, rows, winLen, humanPlayer));
	}
}
