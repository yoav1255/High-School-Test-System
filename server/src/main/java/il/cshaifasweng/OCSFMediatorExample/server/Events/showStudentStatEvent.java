package il.cshaifasweng.OCSFMediatorExample.server.Events;

import il.cshaifasweng.OCSFMediatorExample.entities.Statistics;

public class showStudentStatEvent {
    private Statistics studentStat;
    public showStudentStatEvent(Statistics studentStat)
    {
        this.studentStat = studentStat;
    }
    public Statistics getStudentStat()
    {
        return studentStat;
    }

}
