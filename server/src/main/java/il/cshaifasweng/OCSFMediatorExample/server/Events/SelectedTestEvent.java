package il.cshaifasweng.OCSFMediatorExample.server.Events;

import il.cshaifasweng.OCSFMediatorExample.entities.ScheduledTest;

public class SelectedTestEvent {
    private ScheduledTest selectedTest;

    public SelectedTestEvent(ScheduledTest scheduledTest){this.selectedTest=scheduledTest;}
    public ScheduledTest getSelectedTestEvent(){return selectedTest;}
}
