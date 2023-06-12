package il.cshaifasweng.OCSFMediatorExample.server.Events;

import il.cshaifasweng.OCSFMediatorExample.entities.CustomMessage;

public class NewMessageEvent {
    private CustomMessage message;

    public NewMessageEvent(CustomMessage message) {
        this.message = message;
    }

    public CustomMessage getMessage() {
        return message;
    }
}
