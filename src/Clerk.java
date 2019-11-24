import Util.Branch;

import java.sql.*;

public class Clerk {
    private Connection con;

    private PreparedStatement rentVehicle;
    private PreparedStatement returnVehicle;
    private PreparedStatement getRentalId;

    private PreparedStatement rentalsReport;
    private PreparedStatement returnReport;

    private final String rentVehicleQuery =
            "INSERT INTO Rental" +
            "(rid, vid, dlicense, fromTimestamp, toTimestamp, odometer)" +
            "VALUES (?, ?, ?, ?, ?, ?)";

    private final String returnVehicleQuery =
            "INSERT INTO RETURN" +
            "(rid, vid, stamp, VALUE)" +
             "VALUES (?, ?, ?, ?)";

    private final String getRentalIdQuery =
            "SELECT rid FROM Rental WHERE vid = ?";

    private final String rentalsReportQuery =
            " FROM Vehicle v" +
            " WHERE [v.location = givenBranch AND] v.vid in " +
            "(SELECT r.vid FROM Rental r " +
                    "WHERE DATE(r.fromTimestamp) = ?)";


    public Clerk(Connection con) {
        this.con = con;
        try {
            rentVehicle = con.prepareStatement(rentVehicleQuery);
            returnVehicle = con.prepareStatement(returnVehicleQuery);
            getRentalId = con.prepareStatement(getRentalIdQuery);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void rentVehicle(long rid, long vid, String dlicense, Timestamp from, Timestamp to, long odometer) throws SQLException {
        rentVehicle.setLong(1, rid);
        rentVehicle.setLong(2, vid);
        rentVehicle.setString(3, dlicense);
        rentVehicle.setTimestamp(4,  from);
        rentVehicle.setTimestamp(5, to);
        rentVehicle.setLong(6, odometer);

        rentVehicle.executeUpdate();
    }

    void returnVehicle(long rid, long vid, Timestamp stamp, long fullTank, long odometer, long value) throws SQLException {
        returnVehicle.setLong(1, rid);
        returnVehicle.setLong(2, vid);
        returnVehicle.setTimestamp(3, stamp);
        returnVehicle.setLong(4, value);

        returnVehicle.executeUpdate();
    }

    /**
     * Generates daily rentals report
     * @param br The branch specified. If null, will generate report for all branches
     * @param currentDate The currentDate. If null, get the currentDate. Format of currentDate should
     *                    be "YYYY-MM-DD"
     */
    ResultSet[] generateDailyRentalsReport(Branch br, String currentDate) throws SQLException {
        StringBuilder query = new StringBuilder();

        String date;
        if (currentDate == null) {
            date = new Date(System.currentTimeMillis()).toString();
        } else {
            date = currentDate;
        }


        query.append(" FROM Vehicle v WHERE");

        if (br != null) {
            query.append( " v.branch = '").append(br.getLoc()).append("' AND");
        }

        query.append( " v.vid IN")
                .append(" (SELECT r.vid FROM Rental r WHERE TRUNC(CAST(r.fromTimestamp AS DATE)) = TO_DATE('")
                .append(date)
                .append("','YYYY-MM-DD'))");


        ResultSet allVehiclesRentedOutToday = con.createStatement()
                .executeQuery("SELECT *" + query + " ORDER BY v.branch, v.vtname");

        ResultSet numVehiclesPerCategory = con.createStatement()
                .executeQuery("SELECT v.vtname, COUNT(*)" + query + " GROUP BY v.vtname");

        ResultSet numRentalsPerBranch = con.createStatement().
                executeQuery("SELECT v.branch, COUNT(*)" + query + " GROUP BY v.branch");

        ResultSet totalNumOfVehiclesRentedToday = con.createStatement().
                executeQuery("SELECT COUNT(*)" + query);

        return new ResultSet[] {allVehiclesRentedOutToday, numVehiclesPerCategory, numRentalsPerBranch, totalNumOfVehiclesRentedToday};
    }

    /**
     * Generates daily returns report
     * @param br The branch specified. If null, will generate report for all branches
     */
    ResultSet[] generateDailyReturnsReport(Branch br, String currentDate) throws SQLException {
        String date;
        if (currentDate == null) {
            date = new Date(System.currentTimeMillis()).toString();
        } else {
            date = currentDate;
        }

        ResultSet allVehiclesReturnedToday = con.createStatement()
                .executeQuery(getallVehiclesReturnedTodayQS(br, date));

        ResultSet numVehiclesPerCategory = con.createStatement()
                .executeQuery(numVehiclesPerCategory(br, date));

        ResultSet revenuePerCategory = con.createStatement()
                .executeQuery(revenuePerCategory(br, date));

        ResultSet subtotalsForVehicleAndRevenuePerBr = con.createStatement()
                .executeQuery(subtotalsForVehicleAndRevenuePerBr(br, date));

        ResultSet grandTotals = con.createStatement()
                .executeQuery(grandTotals(br, date));

        return new ResultSet[]{allVehiclesReturnedToday, numVehiclesPerCategory, revenuePerCategory,
                subtotalsForVehicleAndRevenuePerBr, grandTotals};
    }

    private String getallVehiclesReturnedTodayQS(Branch br, String date) {
        String toRet = "SELECT * FROM Vehicle v WHERE";

        if (br != null) {
            toRet += " v.branch = '" + br.getLoc() + "' AND";
        }

        toRet += " v.vid IN (SELECT r.vid FROM Return r WHERE TRUNC(CAST(r.stamp AS DATE)) = TO_DATE('" +
                date
                + "','YYYY-MM-DD'))  ORDER BY v.branch, v.vtname";

        return toRet;
    }

    private String numVehiclesPerCategory(Branch br, String date) {
        String toRet = "SELECT v.vtname, COUNT(*) FROM Vehicle v WHERE";

        if (br != null) {
            toRet += " v.branch = '" + br.getLoc() + "' AND";
        }

        toRet += " v.vid IN (SELECT r.vid FROM Return r WHERE TRUNC(CAST(r.stamp AS DATE)) = TO_DATE('" +
                date +
                "','YYYY-MM-DD')) GROUP BY v.vtname";

        return toRet;
    }

    private String revenuePerCategory(Branch br, String date) {
        String toRet = "SELECT v.vtname, SUM(r.value) FROM Return r, Vehicle v WHERE";

        if (br != null) {
            toRet += " v.branch = '" + br.getLoc() + "' AND";
        }


        toRet += " v.vid = r.vid AND TRUNC(CAST(r.stamp AS DATE)) = TO_DATE('" +
        date +
        "','YYYY-MM-DD') GROUP BY v.vtname";

        return toRet;
    }

    private String subtotalsForVehicleAndRevenuePerBr(Branch br, String date) {
        String toRet = "SELECT v.branch, COUNT(v.vid), SUM(r.value) FROM Return r, Vehicle v WHERE";

        if (br != null) {
            toRet += " v.branch = '" + br.getLoc() + "' AND";
        }

        toRet += " v.vid = r.vid AND TRUNC(CAST(r.stamp AS DATE)) = TO_DATE('" +
                date +
                "','YYYY-MM-DD') GROUP BY v.branch";

        return toRet;
    }

    private String grandTotals(Branch br, String date) {
        String toRet = "SELECT COUNT(v.vid), SUM(r.value) FROM Return r, Vehicle v WHERE";

        if (br != null) {
            toRet += " v.branch = '" + br.getLoc() + "' AND";
        }

        toRet += " v.vid = r.vid AND TRUNC(CAST(r.stamp AS DATE)) = TO_DATE('" +
                date +
                "','YYYY-MM-DD')";

        return toRet;
    }

    public Long getRentalId(Long vid) throws SQLException{
        getRentalId.setLong(1, vid);
        ResultSet result = getRentalId.executeQuery();
        return result.getLong("rid");
    }
}
