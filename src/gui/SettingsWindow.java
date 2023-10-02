package gui;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;

import domain.Player;

class SettingsWindow extends JFrame {

	private static final int FIELD_DIM_DEFAULT = 3;
	private static final int FIELD_DIM_MAX = 10;
	private static final int MODE_DEFAULT = 0;
	private static final Player HUMAN_PLAYER_DEFAULT = Player.X;

	private static final String TITLE_STR = "Новая игра";
	private static final String MODE_STR = "Выберите режим игры:";
	private static final String HUMAN_PLAYER_STR = "Выберите кем играть ('крестики' ходят первыми):";
	private static final String X_STR = "X";
	private static final String O_STR = "O";
	private static final String DIM_FSTR = "Выберите размеры поля (%d):";
	private static final String WINLEN_FSTR = "Выберите длину для победы (%d):";
	private static final String HUMAN_VS_AI_STR = "Человек против компьютера";
	private static final String HUMAN_VS_HUMAN_STR = "Человек против человека";
	private static final String START_STR = "Начать игру";
	private static final String UNSUPPORTED_STR = "Данный режим игры не поддерживается в текущей версии.";

	private static final int WIDTH = 350;
	private static final int HEIGHT = 230;
	private static final int PADDING = 10;
	private static final Dimension PADDING_DIM = new Dimension(PADDING, PADDING);

	private int mode = MODE_DEFAULT;
	private Player humanPlayer = HUMAN_PLAYER_DEFAULT;
	private int fieldDimension = FIELD_DIM_DEFAULT;
	private int winLength = FIELD_DIM_DEFAULT;

	// controls

	private final JLabel labelMode = new JLabel(MODE_STR);
	private final JLabel labelHumanPlayer = new JLabel(HUMAN_PLAYER_STR);
	private final JLabel labelDim = new JLabel();
	private final JLabel labelWinLen = new JLabel();

	private final JRadioButton radioHumanVsAi = new JRadioButton(HUMAN_VS_AI_STR);
	private final JRadioButton radioHumanVsHuman = new JRadioButton(HUMAN_VS_HUMAN_STR);
	private final JRadioButton radioX = new JRadioButton(X_STR);
	private final JRadioButton radioO = new JRadioButton(O_STR);
	private final JSlider sliderDim = new JSlider(FIELD_DIM_DEFAULT, FIELD_DIM_MAX);
	private final JSlider sliderWinLen = new JSlider(FIELD_DIM_DEFAULT, FIELD_DIM_DEFAULT);
	private final JButton buttonStart = new JButton(START_STR);

	private final GameWindow gameWindow;

	SettingsWindow(GameWindow gameWindow) {

		super(TITLE_STR);
		this.gameWindow = gameWindow;
		setMinimumSize(new Dimension(WIDTH, HEIGHT));
		setLocationRelativeTo(gameWindow);

		initControls();

		var frameContainer = new JPanel();
		frameContainer.setLayout(new BoxLayout(frameContainer, BoxLayout.PAGE_AXIS));
		frameContainer.setBorder(BorderFactory.createEmptyBorder(PADDING, PADDING, PADDING, PADDING));
		super.setContentPane(frameContainer);

		JPanel panelMode = new JPanel(new GridLayout(3, 1));
		JPanel panelHumanPlayer = new JPanel(new GridLayout(3, 1));
		JPanel panelDim = new JPanel(new GridLayout(2, 1));
		JPanel panelWinLen = new JPanel(new GridLayout(2, 1));

		ButtonGroup buttonGroupMode = new ButtonGroup();
		buttonGroupMode.add(radioHumanVsAi);
		buttonGroupMode.add(radioHumanVsHuman);
		panelMode.add(labelMode);
		panelMode.add(radioHumanVsAi);
		panelMode.add(radioHumanVsHuman);

		ButtonGroup buttonGroupHumanPlayer = new ButtonGroup();
		buttonGroupHumanPlayer.add(radioX);
		buttonGroupHumanPlayer.add(radioO);
		panelHumanPlayer.add(labelHumanPlayer);
		panelHumanPlayer.add(radioX);
		panelHumanPlayer.add(radioO);

		sliderDim.setPaintTicks(true);
		sliderDim.setSnapToTicks(true);
		panelDim.add(labelDim);
		panelDim.add(sliderDim);

		sliderWinLen.setPaintTicks(true);
		sliderWinLen.setSnapToTicks(true);
		panelWinLen.add(labelWinLen);
		panelWinLen.add(sliderWinLen);

		buttonStart.setAlignmentX(CENTER_ALIGNMENT);

		frameContainer.add(panelMode);
		frameContainer.add(Box.createRigidArea(PADDING_DIM));
		frameContainer.add(panelHumanPlayer);
		frameContainer.add(Box.createRigidArea(PADDING_DIM));
		frameContainer.add(panelDim);
		frameContainer.add(Box.createRigidArea(PADDING_DIM));
		frameContainer.add(panelWinLen);
		frameContainer.add(Box.createRigidArea(PADDING_DIM));
		frameContainer.add(buttonStart);
		super.pack();

		buttonStart.addActionListener(this::handleButtonStartPressed);

		radioHumanVsAi.addChangeListener(this::handleAControlStateChanged);
		radioX.addChangeListener(this::handleAControlStateChanged);
		sliderDim.addChangeListener(this::handleAControlStateChanged);
		sliderWinLen.addChangeListener(this::handleAControlStateChanged);
	}

	private void initControls() {
		radioHumanVsAi.setSelected(mode == 0);
		radioX.setSelected(humanPlayer == Player.X);
		sliderDim.setValue(fieldDimension);
		sliderWinLen.setValue(winLength);
		actualizeLabels();
		actualizeSliders();
	}

	private void actualizeValues() {
		mode = radioHumanVsAi.isSelected() ? 0 : 1;
		humanPlayer = radioX.isSelected() ? Player.X : Player.O;
		fieldDimension = sliderDim.getValue();
		winLength = sliderWinLen.getValue();
	}

	private void actualizeLabels() {
		labelDim.setText(String.format(DIM_FSTR, fieldDimension));
		labelWinLen.setText(String.format(WINLEN_FSTR, winLength));
		repaint();
	}

	private void actualizeSliders() {
		var dimValue = sliderDim.getValue();
		if (sliderWinLen.getValue() > dimValue) {
			sliderWinLen.setValue(dimValue);
		}
		sliderWinLen.setMaximum(dimValue);
		sliderWinLen.setEnabled(sliderWinLen.getMinimum() != sliderWinLen.getMaximum());
	}

	private void handleButtonStartPressed(ActionEvent e) {
		if (mode != 0) {
			try {
				JOptionPane.showMessageDialog(SettingsWindow.this, UNSUPPORTED_STR);
			} catch (HeadlessException ex) {
				ex.printStackTrace();
			}
			return;
		}
		setVisible(false);
		gameWindow.startNewGame(mode, humanPlayer, fieldDimension, fieldDimension, winLength);
		// gameWindow.requestFocus();
	}

	public void handleAControlStateChanged(ChangeEvent e) {
		actualizeValues();
		if (e.getSource() instanceof JSlider slider) {
			actualizeLabels();
			if (slider == sliderDim) {
				actualizeSliders();
			}
		}
	}
}
