package il.cshaifasweng.OCSFMediatorExample.client;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "Subject")
public class Subject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int code;
    private String name;
    @OneToMany(mappedBy = "subject")
    private List<Course> courses;
    @ManyToMany
    @JoinTable(name="teachers_subjects",
            joinColumns = @JoinColumn(name="subject_code"),
            inverseJoinColumns = @JoinColumn(name="teacher_code"))
    private List<Teacher> teachers;
    @OneToMany(mappedBy = "subject")
    private List<Question> questions;
    @OneToMany(mappedBy = "subject")
    private List<ExamForm> examForms;
}
