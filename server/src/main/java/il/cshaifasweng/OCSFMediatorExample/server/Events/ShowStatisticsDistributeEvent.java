package il.cshaifasweng.OCSFMediatorExample.server.Events;

import java.util.List;

public class ShowStatisticsDistributeEvent {
    List<Double> distribution;
    public ShowStatisticsDistributeEvent(List<Double> distribution){this.distribution = distribution;}

    public List<Double> getDistribution() {
        return distribution;
    }
}
