package domain.event;

import java.util.EventListener;

public interface GameOverListener extends EventListener {
	void gameOver(GameOverEvent e);
}
