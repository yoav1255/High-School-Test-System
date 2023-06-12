package il.cshaifasweng.OCSFMediatorExample.server.Events;

import java.util.ArrayList;
import java.util.List;

public class QuestionAddedEvent {
    private List<Object> objectList = new ArrayList<>(); // 0. bool, 1 quest Id

    public QuestionAddedEvent(List<Object> objectList) {
        this.objectList = objectList;
    }
    public List<Object> getObjectList() {
        return objectList;
    }
}
