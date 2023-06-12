package il.cshaifasweng.OCSFMediatorExample.server.Events;

import il.cshaifasweng.OCSFMediatorExample.entities.Course;

import java.util.List;

public class ShowAllCoursesNamesEvent {
    private List<Course> courseList;
    public ShowAllCoursesNamesEvent(List<Course> courseList) {
        this.courseList = courseList;
    }
    public List<Course> getCourseList() {
        return courseList;
    }
}
