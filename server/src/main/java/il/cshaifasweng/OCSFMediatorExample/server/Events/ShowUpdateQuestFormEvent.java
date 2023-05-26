package il.cshaifasweng.OCSFMediatorExample.server.Events;

import java.util.List;

public class ShowUpdateQuestFormEvent {
    List<Object> setTeacherAndQuest;

    public ShowUpdateQuestFormEvent(List<Object> setTeacherAndQuest) {
        this.setTeacherAndQuest = setTeacherAndQuest;
    }
    public List<Object> getSetTeacherAndQuest() {
        return setTeacherAndQuest;
    }
}


