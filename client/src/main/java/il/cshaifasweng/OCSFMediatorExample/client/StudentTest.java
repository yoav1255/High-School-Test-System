package il.cshaifasweng.OCSFMediatorExample.client;

import javax.persistence.*;
import java.time.Duration;

@Entity
@Table(name = "StudentTest")
public class StudentTest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;
    @ManyToOne
    @JoinColumn(name = "scheduledTest_id")
    private ScheduledTest scheduledTest;
    private int grade;
    private Duration timeToComplete; //TODO check how to use Duration properly



}
