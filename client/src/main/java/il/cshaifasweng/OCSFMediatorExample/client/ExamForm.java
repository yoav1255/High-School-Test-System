package il.cshaifasweng.OCSFMediatorExample.client;

import javax.persistence.*;
import java.sql.Time;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "ExamForm")
public class ExamForm {
    @Id
    private String code; //TODO construct the code properly
    private int timeLimit;
    @ManyToOne
    @JoinColumn(name="course_id")
    private Course course;
    @ManyToOne
    @JoinColumn(name="subject_id")
    private Subject subject;
    @OneToMany(mappedBy = "examForm")
    private List<ScheduledTest> scheduledTests;
    @ManyToMany(mappedBy = "examForms")
    private List<Question> questions;
}
