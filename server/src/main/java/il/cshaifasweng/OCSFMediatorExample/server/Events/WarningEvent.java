package il.cshaifasweng.OCSFMediatorExample.server.Events;

import il.cshaifasweng.OCSFMediatorExample.entities.Warning;
public class WarningEvent {
	private Warning warning;

	public Warning getWarning() {
		return warning;
	}

	public WarningEvent(Warning warning) {
		this.warning = warning;
	}
}
