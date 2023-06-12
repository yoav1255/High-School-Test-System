package il.cshaifasweng.OCSFMediatorExample.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ExtraTime")
public class ExtraTime implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @OneToOne
    private ScheduledTest scheduledTest;
    private String subCourse;
    private String teacherName;
    private int extraTime;
    private String explanation;


    public ExtraTime(String teacherName, int extraTime, String subCourse, String explanation, ScheduledTest scheduledTest) {
        this.teacherName = teacherName;
        this.extraTime = extraTime;
        this.subCourse = subCourse;
        this.explanation = explanation;
        this.scheduledTest = scheduledTest;
    }
    public ExtraTime() {    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public int getExtraTime() {
        return extraTime;
    }

    public ScheduledTest getScheduledTest() {
        return scheduledTest;
    }

    public void setScheduledTest(ScheduledTest scheduledTest) {
        this.scheduledTest = scheduledTest;
    }

    public void setExtraTime(int extraTime) {
        this.extraTime = extraTime;
    }

    public String getSubCourse() {
        return subCourse;
    }

    public void setSubCourse(String subCourse) {
        this.subCourse = subCourse;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }
}
