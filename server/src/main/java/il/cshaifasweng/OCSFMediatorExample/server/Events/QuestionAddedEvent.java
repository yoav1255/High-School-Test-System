package il.cshaifasweng.OCSFMediatorExample.server.Events;

public class QuestionAddedEvent {
    private String str;

    public String getStr() {
        return str;
    }

    public QuestionAddedEvent(String str) {
        this.str = str;
        System.out.println("QuestionAddedEvent happend!");
    }

}
