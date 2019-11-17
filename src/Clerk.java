import oracle.sql.TIMESTAMP;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

public class Clerk {
    private PreparedStatement rentVehicle;
    private PreparedStatement returnVehicle;

    private PreparedStatement rentalsReport;
    private PreparedStatement returnReport;

    void initialize() {
        try {
            rentVehicle = branch.con.prepareStatement(
                    "INSERT INTO Rental" +
                    "(rid, vid, dlicense, fromTimestamp, toTimestamp, odometer, cardName, cardNo, ExpDate, confNo)" +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"
            );

            returnVehicle = branch.con.prepareStatement(
                    "INSERT INTO \"Return\"" +
                            "(rid, stamp, fulltank, odometer, \"value\")" +
                            "VALUES (?, ?, ?, ?, ?)");
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
        rentVehicle.clearParameters();
    }

    void returnVehicle(long rid, Timestamp stamp, long fullTank, long odometer, long value) throws SQLException {
        returnVehicle.setLong(0, rid);
        returnVehicle.setTimestamp(1, stamp);
        returnVehicle.setLong(2, fullTank);
        returnVehicle.setLong(3, odometer);
        returnVehicle
    }

    /**
     * Generates daily rentals report
     * @param br The branch specified. If null, will generate report for all branches
     */
    void generateDailyRentalsReport(branch br) {

    }

    /**
     * Generates daily returns report
     * @param br The branch specified. If null, will generate report for all branches
     */
    void generateDailyReturnsReport(branch br) {

    }
}
