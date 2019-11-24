import Util.Branch;

import java.sql.*;

public class Clerk {
    private Connection con;

    private PreparedStatement rentVehicle;
    private PreparedStatement returnVehicle;

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
     * @param currentDate The currentDate. If null, get the currentDate. Format of currentDate should
     *                    be "YYYY-MM-DD"
     */
    ResultSet[] generateDailyRentalsReport(Branch br, String currentDate) throws SQLException {
        String query = generateBaseQueryString(br, currentDate, "Rental");

        ResultSet allVehiclesRentedOutToday = con.createStatement()
                .executeQuery("SELECT *" +
                        query + " ORDER BY v.branch, v.vtname;");

        ResultSet numVehiclesPerCategory = con.createStatement()
                .executeQuery("SELECT v.vtname, COUNT(*)" + query + " GROUP BY v.vtname;");

        ResultSet numRentalsPerBranch = con.createStatement().
                executeQuery("SELECT v.branch, COUNT(*)" + query + " GROUP BY v.branch;");

        ResultSet totalNumOfVehiclesRentedToday = con.createStatement().
                executeQuery("SELECT COUNT(*)" + query + ";");

        return new ResultSet[] {allVehiclesRentedOutToday, numVehiclesPerCategory, numRentalsPerBranch, totalNumOfVehiclesRentedToday};
    }

    /**
     * Generates daily returns report
     * @param br The branch specified. If null, will generate report for all branches
     */
    ResultSet[] generateDailyReturnsReport(Branch br, String currentDate) throws SQLException {
        String query = generateBaseQueryString(br, currentDate, "Return");

        ResultSet allVehiclesReturnedToday = con.createStatement()
                .executeQuery("SELECT *" + query + " ORDER BY v.branch, v.vtname;");

        ResultSet numVehiclesPerCategory = con.createStatement()
                .executeQuery("SELECT *" + query + " GROUP BY v.vtname;");

        ResultSet revenuePerCategory = con.createStatement()
                .executeQuery("SELECT SUM(r.value)" + query + " GROUP BY v.vtname;");

        ResultSet subtotalsForVehicleAndRevnuePerBr = con.createStatement()
                .executeQuery("SELECT COUNT(v.vid), SUM(r.value)" + query + " GROUP BY BRANCH;");

        ResultSet grandTotals = con.createStatement()
                .executeQuery("SELECT SUM(r.value)" + query + ";");

        return new ResultSet[]{allVehiclesReturnedToday, numVehiclesPerCategory, revenuePerCategory,
                subtotalsForVehicleAndRevnuePerBr, grandTotals};
    }

    /**
     * Returns the base query structure to use for generating daily reports
     * It aggregates the similar behaviour in the two generate methods
     *
     * The format of the base string is as such:
     *
     * "FROM Vehicle v WHERE [v.branch = br.location AND] v.vid IN
     *                                         (SELECT r.vid FROM [reportTable] r WHERE
     *                                         [r.fromTimestamp's Date] = [todays date or the given date]
     *
     * @param reportTable Either \"Rental\" or \"Return\"
     * @return A query string that filters for all vehicles rented/returned today in the given branch
     */
    private String generateBaseQueryString(Branch br, String currentDate, String reportTable) {
        StringBuilder query = new StringBuilder();

        String date;
        if (currentDate == null) {
            date = new Date(System.currentTimeMillis()).toString();
        } else {
            date = currentDate;
        }


        query.append(" FROM Vehicle v WHERE");

        if (br != null) {
            query.append( " v.branch = ").append(br.getLoc()).append(" AND");
        }

        query.append( " v.vid IN")
                .append(" (SELECT r.vid FROM ")
                .append(reportTable)
                .append(" r WHERE TRUNC(CAST(r.fromTimestamp AS DATE)) = TO_DATE('")
                .append(date)
                .append("','YYYY-MM-DD'))");

       return query.toString();
    }
}
