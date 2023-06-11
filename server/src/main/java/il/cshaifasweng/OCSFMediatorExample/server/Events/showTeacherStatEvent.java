package il.cshaifasweng.OCSFMediatorExample.server.Events;
import il.cshaifasweng.OCSFMediatorExample.entities.Statistics;

import java.util.List;
import java.util.Map;

public class showTeacherStatEvent {
    private List<Statistics> teacherStat;
    public showTeacherStatEvent(List<Statistics> teacherStat)
    {
        this.teacherStat = teacherStat;
    }
    public List<Statistics> getTeacherStat()
    {
        return teacherStat;
    }
}
