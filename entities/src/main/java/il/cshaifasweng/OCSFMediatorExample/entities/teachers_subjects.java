package il.cshaifasweng.OCSFMediatorExample.entities;
import il.cshaifasweng.OCSFMediatorExample.entities.Subject;
import il.cshaifasweng.OCSFMediatorExample.entities.Teacher;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "teachers_subjects")
public class teachers_subjects implements Serializable {


    @Id
    @ManyToOne
    @JoinColumn(name = "subject_code")
    private Subject subject;

    @ManyToOne
    @JoinColumn(name = "teacher_code")
    private Teacher teacher;

    public teachers_subjects() {
    }

    public Subject getSubject() {
        return subject;
    }

    public int getSubjectCode() {
        return subject.getCode();
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public Teacher getTeacher(){return teacher;}
    public String getTeacherCode() {
        return teacher.getId();
    }

    public void setTeacher(Teacher teacher) {this.teacher = teacher;
    }
}
