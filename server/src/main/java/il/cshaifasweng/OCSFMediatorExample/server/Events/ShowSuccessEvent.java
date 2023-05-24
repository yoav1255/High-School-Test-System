package il.cshaifasweng.OCSFMediatorExample.server.Events;

public class ShowSuccessEvent {
    private String msg;

    public ShowSuccessEvent(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }
}
