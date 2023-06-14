package il.cshaifasweng.OCSFMediatorExample.entities;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Statistics implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    String scheduleTestId;
    Double avgGrade;
    int median;
    String studentId;
    List<Double> distribution;
    String range;


    public void setMedian(int median) {
        this.median = median;
    }

    public int getMedian() {
        return median;
    }

    public void setAvgGrade(Double avgGrade) {
        this.avgGrade = avgGrade;
    }

    public Double getAvgGrade() {
        return avgGrade;
    }

   public void setDistribution(List<Double> distribution) {
        this.distribution = distribution;
    }

    public List<Double> getDistribution() {
        return distribution;
    }


    public void setScheduleTestId(String scheduleTestId) {
        this.scheduleTestId = scheduleTestId;
    }

    public String getScheduleTestId() {
        return scheduleTestId;
    }

    public String getStudentId() {
        return studentId;
    }
    public void setStudentId(String studentId){this.studentId = studentId;}

    public String setRange(String range) {return range;}

    public String getRange(int i) {
        if(i == 9){return  (i * 10) + "-" + ((i + 1) * 10);}
        else{return  (i * 10) + "-" + ((i + 1) * 10 - 1);}
    }

}
