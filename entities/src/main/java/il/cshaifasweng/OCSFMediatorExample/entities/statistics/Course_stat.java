package il.cshaifasweng.OCSFMediatorExample.entities.statistics;

import il.cshaifasweng.OCSFMediatorExample.entities.Course;

import java.util.List;

public class Course_stat {
    Course course;
    String scheduleTestId;
    List<Integer> gradesPerScheduleTest;
    Double avgGrade;
    int median;
    List<Double> distribution;


}
