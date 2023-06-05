package il.cshaifasweng.OCSFMediatorExample.server.Events;

import java.util.List;

public class ShowAllCoursesNamesEvent {
    private List<String> courseList;
    public ShowAllCoursesNamesEvent(List<String> courseList) {
        this.courseList = courseList;
    }
    public List<String> getCourseList() {
        return courseList;
    }
}
