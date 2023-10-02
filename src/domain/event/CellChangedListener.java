package domain.event;

import java.util.EventListener;

public interface CellChangedListener extends EventListener {
	public void cellChanged(CellChangedEvent e);
}
