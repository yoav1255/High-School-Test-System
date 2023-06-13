package il.cshaifasweng.OCSFMediatorExample.server.Events;

import il.cshaifasweng.OCSFMediatorExample.entities.Statistics;

public class ShowStudentStatEvent {
    private Statistics studentStat;
    public ShowStudentStatEvent(Statistics studentStat)
    {
        this.studentStat = studentStat;
    }
    public Statistics getStudentStat()
    {
        return studentStat;
    }

    public void setStudentStat(Statistics studentStat) {
        this.studentStat = studentStat;
    }

}
