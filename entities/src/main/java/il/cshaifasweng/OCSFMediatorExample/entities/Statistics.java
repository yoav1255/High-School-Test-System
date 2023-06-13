package il.cshaifasweng.OCSFMediatorExample.entities;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

public class Statistics implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    String scheduleTestId;
    Double avgGrade;
    int median;
    String id;
    List<Double> distribution;

    public String getRange() {
        int startRange = 0;
        int endRange = 9;
        String range = startRange + "-" + endRange;

        int lastIndex = distribution.size() - 1;
        if (lastIndex > 0) {
            range = startRange + "-" + (endRange + (lastIndex * 10));
        }

        return range;
    }

    public String getPercentage() {
        StringBuilder sb = new StringBuilder();
        int rangeSize = 10; // Assuming each range represents 10 grades

        for (int i = 0; i < distribution.size(); i++) {
            int startRange = i * rangeSize;
            int endRange = startRange + rangeSize - 1;
            double percentage = distribution.get(i);

            String range = startRange + "-" + endRange;
            String percentageString = String.format("%.2f", percentage);

            sb.append(range).append(": ").append(percentageString).append("%\n");
        }

        return sb.toString();
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


    public void setScheduleTestId(String scheduleTestId) {
        this.scheduleTestId = scheduleTestId;
    }

    public String getScheduleTestId() {
        return scheduleTestId;
    }

    public String getStudentId() { return id;}
    public void setId(String id){this.id = id;}

}
