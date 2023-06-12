package il.cshaifasweng.OCSFMediatorExample.server.Events;

import il.cshaifasweng.OCSFMediatorExample.server.ocsf.ConnectionToClient;

public class DeleteClientEvent {
    private ConnectionToClient client;

    public DeleteClientEvent(ConnectionToClient client) {
        this.client = client;
    }

    public ConnectionToClient getClient() {
        return client;
    }
}
