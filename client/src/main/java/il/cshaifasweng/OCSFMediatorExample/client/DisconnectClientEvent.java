package il.cshaifasweng.OCSFMediatorExample.client;

public class DisconnectClientEvent {
    SimpleClient client;

    public DisconnectClientEvent(SimpleClient client) {
        this.client = client;
    }

    public SimpleClient getClient() {
        return client;
    }

    public void setClient(SimpleClient client) {
        this.client = client;
    }
}
