package domain.event;

import java.util.EventObject;

import domain.Game;
import domain.Player;

public class CellChangedEvent extends EventObject {

	private final int colIndex;
	private final int rowIndex;
	private final Player state;

	public CellChangedEvent(Game source, int colIndex, int rowIndex, Player state) {
		super(source);
		this.colIndex = colIndex;
		this.rowIndex = rowIndex;
		this.state = state;
	}

	public int getColIndex() {
		return colIndex;
	}

	public int getRowIndex() {
		return rowIndex;
	}

	public Player getState() {
		return state;
	}
}
