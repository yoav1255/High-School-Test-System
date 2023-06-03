package il.cshaifasweng.OCSFMediatorExample.server.Events;
import il.cshaifasweng.OCSFMediatorExample.entities.Statistics;

import java.util.List;

public class ShowAllStatisticEvent {
    private List<Statistics> statisticsList;
    public ShowAllStatisticEvent(List<Statistics> statisticsList) {this.statisticsList = statisticsList;}

    public List<Statistics> getStatisticsList() {
        return statisticsList;
    }
}
