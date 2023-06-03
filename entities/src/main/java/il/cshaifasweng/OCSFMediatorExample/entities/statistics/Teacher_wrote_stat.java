package il.cshaifasweng.OCSFMediatorExample.entities.statistics;

import il.cshaifasweng.OCSFMediatorExample.entities.Teacher;

import java.util.List;

public class Teacher_wrote_stat {
    Teacher teacher;
    String scheduleTestId;
    List<Integer> gradesPerScheduleTest;
    Double avgGrade;
    int median;
    List<Double> distribution;
}
