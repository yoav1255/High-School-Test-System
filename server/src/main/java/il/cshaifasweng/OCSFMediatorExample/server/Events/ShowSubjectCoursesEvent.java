package il.cshaifasweng.OCSFMediatorExample.server.Events;

import il.cshaifasweng.OCSFMediatorExample.entities.Course;

import java.util.List;

public class ShowSubjectCoursesEvent {
    List<Course> courses;

    public ShowSubjectCoursesEvent(List<Course> courses) {
        this.courses = courses;
    }

    public List<Course> getCourses() {
        return courses;
    }
}
