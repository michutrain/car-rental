package Util;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class TimeInterval {

    private Timestamp from;
    private Timestamp to;

    public TimeInterval(Timestamp from, Timestamp to) {
        this.from = from;
        this.to = to;
    }

    /**
     * Constructs a time interval from the given parameters. Times should be in hh:mm:ss format,
     * days should be in yyyy-[m]m-[d]d format
     */
    public TimeInterval(String fromTime, String fromDay, String toTime, String toDay) {
        this.from =  Timestamp.valueOf(fromDay + " " + fromTime);
        this.to = Timestamp.valueOf(toDay + " " + toTime);
    }

    public Timestamp getFrom() {
        return from;
    }

    public Timestamp getTo() {
        return to;
    }

}
