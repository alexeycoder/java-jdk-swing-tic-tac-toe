package domain.event;

import java.util.EventObject;

import domain.Game;
import domain.Player;

public class GameOverEvent extends EventObject {

	private final Player winner;

	public GameOverEvent(Game source, Player winner) {
		super(source);
		this.winner = winner;
	}

	public Player getWinner() {
		return winner;
	}
}
