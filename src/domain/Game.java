package domain;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

import javax.swing.SwingUtilities;

import domain.event.CellChangedEvent;
import domain.event.CellChangedListener;
import domain.event.GameOverEvent;
import domain.event.GameOverListener;

public class Game {

	private static record Position(int col, int row) {
	}

	private final ArrayList<GameOverListener> gameOverListeners = new ArrayList<>();
	private final ArrayList<CellChangedListener> cellChangedListeners = new ArrayList<>();

	private final int fieldCols;
	private final int fieldRows;
	private final int winLength;

	private final Player[][] field;

	private final Player aiPlayer;
	private final Player humanPlayer;
	private Player whoseTurn;
	private Player winner;

	private static final Random random = new Random();

	public Game(int fieldCols, int fieldRows, int winLength, Player humanPlayer) {
		if (humanPlayer == Player.NONE) {
			throw new IllegalArgumentException("humanPlayer");
		}

		this.fieldCols = fieldCols;
		this.fieldRows = fieldRows;
		this.winLength = winLength;
		this.humanPlayer = humanPlayer;
		this.aiPlayer = humanPlayer == Player.X ? Player.O : Player.X;

		this.field = new Player[fieldRows][fieldCols];
		for (int row = 0; row < fieldRows; ++row) {
			for (int col = 0; col < fieldCols; ++col) {
				field[row][col] = Player.NONE;
			}
		}

		this.winner = Player.NONE;
		this.whoseTurn = Player.X;

		if (aiPlayer == whoseTurn) {
			SwingUtilities.invokeLater(() -> aiTurn());
		}
	}

	public Player aiPlayer() {
		return aiPlayer;
	}

	public Player humanPlayer() {
		return humanPlayer;
	}

	public Player whoseTurn() {
		return whoseTurn;
	}

	public Player winner() {
		return winner;
	}

	public boolean isGameOver() {
		return whoseTurn == Player.NONE;
	}

	public int fieldColumns() {
		return fieldCols;
	}

	public int fieldRows() {
		return fieldRows;
	}

	public Player cellState(int col, int row) {

		if (!isValidCell(col, row)) {
			throw new IndexOutOfBoundsException();
		}
		return field[row][col];
	}

	public synchronized void addGameOverListener(GameOverListener listener) {
		gameOverListeners.add(Objects.requireNonNull(listener));
	}

	public synchronized void addCellChangedListener(CellChangedListener listener) {
		cellChangedListeners.add(Objects.requireNonNull(listener));
	}

	private synchronized void notifyCellChanged(final CellChangedEvent e) {
		System.out.println("Raise Cell Changed");
		cellChangedListeners.forEach(l -> l.cellChanged(e));
	}

	private synchronized void notifyGameOver(final GameOverEvent e) {
		System.out.println("Raise Game Over");
		gameOverListeners.forEach(l -> l.gameOver(e));
	}

	/* Game & field logic */

	public void humanTurn(int col, int row) {

		if (whoseTurn != humanPlayer)
			return;
		if (!isValidCell(col, row))
			throw new IndexOutOfBoundsException();
		if (!isEmptyCell(col, row))
			return;

		field[row][col] = humanPlayer;
		notifyCellChanged(new CellChangedEvent(this, col, row, humanPlayer));
		if (checkGameOver(humanPlayer)) {
			notifyGameOver(new GameOverEvent(this, winner));
			return;
		}
		whoseTurn = aiPlayer;

		aiTurn();
	}

	private void aiTurn() {

		if (whoseTurn != aiPlayer)
			return;

		var turnPos = turnAiWinCell();
		if (turnPos == null) {
			turnPos = turnHumanWinCell();
		}
		if (turnPos == null) {

			int col, row;
			do {
				col = random.nextInt(fieldCols);
				row = random.nextInt(fieldRows);
				
			} while (!isEmptyCell(col, row));

			field[row][col] = aiPlayer;
			turnPos = new Position(col, row);
		}

		notifyCellChanged(new CellChangedEvent(this, turnPos.col, turnPos.row, aiPlayer));
		if (checkGameOver(aiPlayer)) {
			notifyGameOver(new GameOverEvent(this, winner));
			return;
		}
		whoseTurn = humanPlayer;
	}

	private Position turnAiWinCell() {

		for (int row = 0; row < fieldRows; ++row) {
			for (int col = 0; col < fieldCols; ++col) {
				if (isEmptyCell(col, row)) {
					field[row][col] = aiPlayer;
					if (checkWin(aiPlayer)) {
						return new Position(col, row);
					}
					field[row][col] = Player.NONE;
				}
			}
		}
		return null;
	}

	private Position turnHumanWinCell() {

		for (int row = 0; row < fieldRows; ++row) {
			for (int col = 0; col < fieldCols; ++col) {
				if (isEmptyCell(col, row)) {
					field[row][col] = humanPlayer;
					if (checkWin(humanPlayer)) {
						field[row][col] = aiPlayer;
						return new Position(col, row);
					}
					field[row][col] = Player.NONE;
				}
			}
		}
		return null;
	}

	private boolean checkGameOver(Player player) {
		assert player != Player.NONE : "Illegal player value";

		if (checkWin(player)) {
			whoseTurn = Player.NONE;
			winner = player;
			return true;
		}

		if (isFieldFull()) {
			whoseTurn = Player.NONE;
			winner = Player.NONE;
			return true;
		}

		return false;
	}

	private boolean checkWin(Player player) {

		for (int col = 0; col < fieldCols; ++col) {
			for (int row = 0; row < fieldRows; ++row) {

				if (checkLine(col, row, 1, 0, winLength, player))
					return true;

				if (checkLine(col, row, 1, 1, winLength, player))
					return true;

				if (checkLine(col, row, 0, 1, winLength, player))
					return true;

				if (checkLine(col, row, 1, -1, winLength, player))
					return true;
			}
		}

		return false;
	}

	private boolean checkLine(int col, int row, int dRow, int dCol, int len, Player player) {
		final int farRow = row + (len - 1) * dRow;
		final int farCol = col + (len - 1) * dCol;
		if (!isValidCell(farCol, farRow)) {
			return false;
		}
		for (int i = 0; i < len; ++i) {
			if (field[row + i * dRow][col + i * dCol] != player) {
				return false;
			}
		}
		return true;
	}

	private boolean isFieldFull() {

		for (int row = 0; row < fieldRows; ++row) {
			for (int col = 0; col < fieldCols; ++col) {
				if (field[row][col] == Player.NONE)
					return false;
			}
		}
		return true;
	}

	private boolean isValidCell(int col, int row) {

		return col >= 0 && col < fieldCols && row >= 0 && row < fieldRows;
	}

	private boolean isEmptyCell(int col, int row) {

		return field[row][col] == Player.NONE;
	}
}
