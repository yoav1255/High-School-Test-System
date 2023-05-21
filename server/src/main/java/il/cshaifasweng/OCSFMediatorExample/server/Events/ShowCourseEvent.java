package il.cshaifasweng.OCSFMediatorExample.server.Events;

import il.cshaifasweng.OCSFMediatorExample.entities.Course;

public class ShowCourseEvent {
    private Course course;

    public ShowCourseEvent(Course course) {
        this.course = course;
    }

    public Course getCourse() {
        return course;
    }
}
