package il.cshaifasweng.OCSFMediatorExample.server.Events;

import il.cshaifasweng.OCSFMediatorExample.entities.Statistics;

import java.util.List;

public class ShowCourseStatEvent {
   private List<Statistics> courseStat;
   public ShowCourseStatEvent(List<Statistics> courseStat)
   {
       this.courseStat = courseStat;
   }
   public List<Statistics> getCourseStat()
    {
        return courseStat;
    }
}
