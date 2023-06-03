package il.cshaifasweng.OCSFMediatorExample.entities;

import java.util.List;

public class Statistics {
    String Id;
    String scheduleTestId;
    List<Integer> gradesPerScheduleTest;
    Double avgGrade;
    int median;
    List<Double> distribution;

    public void setId(String id) {
        Id = id;
    }

    public String getId() {
        return Id;
    }

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

    public void setGradesPerScheduleTest(List<Integer> gradesPerScheduleTest) {
        this.gradesPerScheduleTest = gradesPerScheduleTest;
    }

    public List<Integer> getGradesPerScheduleTest() {
        return gradesPerScheduleTest;
    }

    public void setScheduleTestId(String scheduleTestId) {
        this.scheduleTestId = scheduleTestId;
    }

    public String getScheduleTestId() {
        return scheduleTestId;
    }
}
