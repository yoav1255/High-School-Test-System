package il.cshaifasweng.OCSFMediatorExample.server.Events;

import il.cshaifasweng.OCSFMediatorExample.server.ocsf.ConnectionToClient;

public class NewClientEvent {
    private ConnectionToClient client;

    public NewClientEvent(ConnectionToClient client) {
        this.client = client;
    }

    public ConnectionToClient getClient() {
        return client;
    }
}
