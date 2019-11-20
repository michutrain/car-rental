import Util.Branch;
import oracle.sql.TIMESTAMP;

import java.sql.*;

public class Clerk {
    private Connection con;

    private PreparedStatement rentVehicle;
    private PreparedStatement returnVehicle;

    private PreparedStatement rentalsReport;
    private PreparedStatement returnReport;

    private final String rentVehicleQuery =
            "INSERT INTO Rental" +
            "(rid, vid, dlicense, fromTimestamp, toTimestamp, odometer, cardName, cardNo, ExpDate, confNo)" +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    private final String returnVehicleQuery =
            "INSERT INTO \"Return\"" +
            "(rid, stamp, fulltank, odometer, \"value\")" +
             "VALUES (?, ?, ?, ?, ?)";

    public Clerk(Connection con) {
        try {
            rentVehicle = con.prepareStatement(rentVehicleQuery);
            returnVehicle = con.prepareStatement(returnVehicleQuery);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void rentVehicle(long rid, long vid, String dlicense, Timestamp from, Timestamp to, long odometer,
                     String cardName, String cardNo, Date expDate, long confNo) throws SQLException {
        rentVehicle.setLong(0, rid);
        rentVehicle.setLong(1, vid);
        rentVehicle.setString(2, dlicense);
        rentVehicle.setTimestamp(3,  from);
        rentVehicle.setTimestamp(4, to);
        rentVehicle.setLong(5, odometer);
        rentVehicle.setString(6, cardName);
        rentVehicle.setString(7, cardNo);
        rentVehicle.setDate(8, expDate);
        rentVehicle.setLong(9, confNo);

        rentVehicle.executeUpdate();
    }

    void returnVehicle(long rid, Timestamp stamp, long fullTank, long odometer, long value) throws SQLException {
        returnVehicle.setLong(0, rid);
        returnVehicle.setTimestamp(1, stamp);
        returnVehicle.setLong(2, fullTank);
        returnVehicle.setLong(3, odometer);
        returnVehicle.setLong(4, value);

        returnVehicle.executeUpdate();
    }

    /**
     * Generates daily rentals report
     * @param br The branch specified. If null, will generate report for all branches
     */
    void generateDailyRentalsReport(Branch br) {
        // TODO
    }

    /**
     * Generates daily returns report
     * @param br The branch specified. If null, will generate report for all branches
     */
    void generateDailyReturnsReport(Branch br) {
        // TODO
    }
}
