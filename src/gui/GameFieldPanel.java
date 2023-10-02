package gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Objects;

import javax.swing.JPanel;

import domain.Game;
import domain.Player;
import domain.event.CellChangedEvent;
import domain.event.CellChangedListener;
import domain.event.GameOverEvent;
import domain.event.GameOverListener;

public class GameFieldPanel extends JPanel {

	private static final String MSG_WIN_HUMAN = "Победил игрок!";
	private static final String MSG_WIN_AI = "Победил компьютер!";
	private static final String MSG_DRAW = "Ничья!";

	private static final int CELL_PADDING = 5;

	private Game game;

	private int panelWidth;
	private int panelHeight;

	private int cellWidth;
	private int cellHeight;

	GameFieldPanel() {
		super(true);

		super.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				update(e);
			}
		});
	}

	void startNewGame(Game game) {
		this.game = Objects.requireNonNull(game);
		this.game.addCellChangedListener(new CellChangedListener() {

			@Override
			public void cellChanged(CellChangedEvent e) {
				GameFieldPanel.this.repaint();
			}

		});
		this.game.addGameOverListener(new GameOverListener() {

			@Override
			public void gameOver(GameOverEvent e) {
				GameFieldPanel.this.repaint();
			}

		});

		repaint();
	}

	protected void update(MouseEvent e) {
		if (game == null || game.isGameOver()) {
			return;
		}

		int colIndex = e.getX() / cellWidth;
		int rowIndex = e.getY() / cellHeight;
		System.out.printf("column=%d, row=%d\n", colIndex, rowIndex);
		game.humanTurn(colIndex, rowIndex);
	}

	/*
	 * Rendering
	 */

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		var g2D = (Graphics2D) g;
		g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		render(g2D);
	}

	private void render(Graphics g) {
		if (game == null) {
			return;
		}

		panelWidth = getWidth();
		panelHeight = getHeight();
		int fieldCols = game.fieldColumns();
		int fieldRows = game.fieldRows();

		cellWidth = panelWidth / fieldCols;
		cellHeight = panelHeight / fieldRows;

		g.setColor(Color.BLACK);

		for (int row = 0; row < fieldRows; ++row) {
			int y = row * cellHeight;
			g.drawLine(0, y, panelWidth, y);
		}
		for (int col = 0; col < fieldCols; ++col) {
			int x = col * cellWidth;
			g.drawLine(x, 0, x, panelHeight);
		}

		for (int row = 0; row < fieldRows; ++row) {
			for (int col = 0; col < fieldCols; ++col) {
				if (game.cellState(col, row) == Player.NONE)
					continue;

				if (game.cellState(col, row) == game.humanPlayer()) {

					g.setColor(Color.BLUE);
					g.fillOval(col * cellWidth + CELL_PADDING,
							row * cellHeight + CELL_PADDING,
							cellWidth - CELL_PADDING * 2,
							cellHeight - CELL_PADDING * 2);

				} else if (game.cellState(col, row) == game.aiPlayer()) {

					g.setColor(Color.RED);
					g.fillOval(col * cellWidth + CELL_PADDING,
							row * cellHeight + CELL_PADDING,
							cellWidth - CELL_PADDING * 2,
							cellHeight - CELL_PADDING * 2);

				} else {
					throw new RuntimeException(
							"Unexpected value " + game.cellState(col, row) + " in cell: column=" + col + " row=" + row);
				}
			}
		}

		if (game.isGameOver()) {
			showMessageGameOver(g);
		}
	}

	private void showMessageGameOver(Graphics g) {
		assert game != null;

		g.setColor(Color.DARK_GRAY);
		g.fillRect(0, 200, getWidth(), 52);
		g.setColor(Color.YELLOW);
		g.setFont(new Font(getFont().getFontName(), Font.BOLD, 42));

		if (game.winner() == game.aiPlayer()) {
			g.drawString(MSG_WIN_AI, 20, getHeight() / 2);
		} else if (game.winner() == game.humanPlayer()) {
			g.drawString(MSG_WIN_HUMAN, 70, getHeight() / 2);
		} else {
			g.drawString(MSG_DRAW, 180, getHeight() / 2);
		}
	}

}
