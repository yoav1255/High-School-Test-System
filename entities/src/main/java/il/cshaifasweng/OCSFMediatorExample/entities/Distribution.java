package il.cshaifasweng.OCSFMediatorExample.entities;

public class Distribution {
    String range;
    Double percentage;

    public Distribution(String range, Double percentage) {
        this.range = range;
        this.percentage = percentage;
    }

    public String getRange() {
        return range;
    }

    public Double getPercentage() {
        return percentage;
    }

    public void setRange(String range) {
        this.range = range;
    }

    public void setPercentage(Double percentage) {
        this.percentage = percentage;
    }
}
