package il.cshaifasweng.OCSFMediatorExample.server.Events;
import il.cshaifasweng.OCSFMediatorExample.entities.Statistics;

import java.util.List;


public class ShowTeacherStatEvent {
    private List<Statistics> teacherStat;
    public ShowTeacherStatEvent(List<Statistics> teacherStat)
    {
        this.teacherStat = teacherStat;
    }
    public List<Statistics> getTeacherStat()
    {
        return teacherStat;
    }
}
