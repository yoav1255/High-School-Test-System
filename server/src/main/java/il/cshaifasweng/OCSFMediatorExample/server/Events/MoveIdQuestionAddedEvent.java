package il.cshaifasweng.OCSFMediatorExample.server.Events;

public class MoveIdQuestionAddedEvent {
    private String TeacherId;
    private String QuestId;

    public MoveIdQuestionAddedEvent(String TeacherId, String QuestId) {
        this.TeacherId = TeacherId;
        this.QuestId = QuestId;
    }

    public String getQuestId() {
        return QuestId;
    }

    public String getTeacherId() {
        return TeacherId;
    }
}
