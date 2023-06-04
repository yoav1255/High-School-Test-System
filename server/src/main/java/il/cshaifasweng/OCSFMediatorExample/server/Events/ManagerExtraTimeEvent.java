package il.cshaifasweng.OCSFMediatorExample.server.Events;

public class ManagerExtraTimeEvent {
    Boolean decision;

    public ManagerExtraTimeEvent(Boolean decision) {
        this.decision = decision;
    }

    public Boolean getDecision() {
        return decision;
    }

    public void setDecision(Boolean decision) {
        this.decision = decision;
    }
}
