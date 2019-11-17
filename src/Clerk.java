import Util.Branch;

import java.sql.*;

public class Clerk {
    private Statement stmt;

    private PreparedStatement rentVehicle;
    private PreparedStatement returnVehicle;

    private PreparedStatement rentalsReport;
    private PreparedStatement returnReport;

    void initialize() {
        try {
            stmt = MainMenu.con.createStatement();

            rentVehicle = MainMenu.con.prepareStatement(
                    "INSERT INTO RENTAL" +
                    "(rid, vid, dlicense, fromTimestamp, toTimestamp, odometer, cardName, cardNo, ExpDate, confNo) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"
            );

            returnVehicle = MainMenu.con.prepareStatement(
                    "INSERT INTO RETURN" +
                            "(rid, stamp, fulltank, odometer, VALUE)" +
                            "VALUES (?, ?, ?, ?, ?)"
            );

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
        calculateAndDisplayCost(rid, stamp, fullTank, odometer, value);
    }

    void calculateAndDisplayCost(long rid, Timestamp stamp, long fullTank, long odometer, long value) throws SQLException {

        ResultSet rs = stmt.executeQuery("SELECT * FROM RENTAL WHERE RID = " + rid);
        long vid = rs.getLong("VID");
        Timestamp from = rs.getTimestamp("FROMTIMESTAMP");
        long confNum = rs.getLong("CONFNO");

        rs = stmt.executeQuery("SELECT * FROM VEHICLETYPE WHERE VTNAME IN (" +
                "SELECT VTNAME FROM VEHICLE WHERE VLICENSE = " + vid + ")");

        // TODO: CALCULATE THE COST
    }

    /**
     * Generates daily rentals report
     * @param br The Branch specified. If null, will generate report for all branches
     */
    void generateDailyRentalsReport(Branch br) throws SQLException {
        ResultSet rs;

        if (br == null) {
            rs = stmt.executeQuery(
                            "SELECT BRANCH, VTNAME " +
                                "FROM VEHICLE " +
                                "WHERE VLICENSE IN (" +
                                    "SELECT VLICENSE " +
                                    "FROM RENTAL" +
                                ")" +
                                "GROUP BY BRANCH, VTNAME");
        } else {
            rs = stmt.executeQuery(
                        "SELECT VTNAME " +
                            "FROM VEHICLE " +
                            "WHERE VLICENSE IN (" +
                                "SELECT VLICENSE " +
                                "FROM RENTAL" +
                            ")" +
                            "GROUP BY VTNAME"
            );
        }



    }

    /**
     * Generates daily returns report
     * @param br The Branch specified. If null, will generate report for all branches
     */
    void generateDailyReturnsReport(Branch br) {

    }
}
