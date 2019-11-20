package Util;

import java.sql.Timestamp;

public class TimeInterval {

    private Timestamp from;
    private Timestamp to;

    public TimeInterval(Timestamp from, Timestamp to) {
        this.from = from;
        this.to = to;
    }

    public TimeInterval(String fromTime, String fromDay, String toTime, String toDay) {
        // TODO
    }

    public Timestamp getFrom() {
        return from;
    }

    public Timestamp getTo() {
        return to;
    }

}
