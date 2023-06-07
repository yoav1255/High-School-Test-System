package il.cshaifasweng.OCSFMediatorExample.server.Events;

public class MoveObjectToNextPageEvent {
    private Object object;

    public MoveObjectToNextPageEvent(Object object) {
        this.object = object;
    }

    public Object getObject() {
        return object;
    }
}
