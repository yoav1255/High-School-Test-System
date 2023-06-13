package il.cshaifasweng.OCSFMediatorExample.server.Events;

import il.cshaifasweng.OCSFMediatorExample.entities.Statistics;

import java.util.List;

public class ShowStatisticsDistributeEvent {
    List<Double> distribution;
    Statistics stats;
    public ShowStatisticsDistributeEvent(Statistics distribution){
        this.distribution = distribution.getDistribution();
        this.stats = distribution;
    }

    public List<Double> getDistribution() {
        return distribution;
    }

    public Statistics getStats() {
        return stats;
    }
}
