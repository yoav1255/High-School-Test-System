package il.cshaifasweng.OCSFMediatorExample.client;

import javax.persistence.*;
import java.sql.Time;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "ScheduledTest")
public class ScheduledTest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private Date date;
    private Time time; //Todo check Time and Date format
    @ManyToOne
    @JoinColumn(name = "examForm_id")
    private ExamForm examForm;
    @OneToMany(mappedBy = "scheduledTest")
    private List<StudentTest> studentTests;
    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;
    private int submissions;

}
