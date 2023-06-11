package il.cshaifasweng.OCSFMediatorExample.server.Events;

import il.cshaifasweng.OCSFMediatorExample.entities.Statistics;

import java.util.List;

public class showCourseStatEvent {
   private List<Statistics> courseStat;
   public showCourseStatEvent(List<Statistics> courseStat)
   {
       this.courseStat = courseStat;
   }
   public List<Statistics> getCourseStat()
    {
        return courseStat;
    }
}
