import Util.Branch;
import Util.IDGen;
import Util.TimeInterval;

import java.sql.*;

public class Customer {

    private PreparedStatement addVehicle;
    private PreparedStatement addCustomer;
    private PreparedStatement makeReservation;
    private PreparedStatement getAvailableVehicle;
    private PreparedStatement getAvailableVehiclesDetails;
    private PreparedStatement getVehicleStatement;
    private PreparedStatement getCustomerByDLicense;
    private PreparedStatement getAvailableVehiclesCount;
    private PreparedStatement getCustomerInformation;
    private PreparedStatement getVehicleStatus;

    private MainMenu mainMenu;

    private final String addVehicleQuery =
            "INSERT INTO Vehicle (vid, vlicense, make, model," +
                    " year, color, odometer, status, vtname, location, city)" +
                    " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

    private final String addCustomerQuery =
            "INSERT INTO Customer" +
                    " (dlicense, name, cellphone, address)" +
                    " VALUES (?, ?, ?, ?)";

    private final String makeReservationQuery =
            "INSERT INTO Reservation" +
                    " (vtname, dlicense, fromTimestamp, toTimestamp)" +
                    " VALUES (?, ?, ?, ?)";

    private final String getAvailableVehicleQuery =
            "SELECT vid FROM Vehicle WHERE status = '0' AND vtname = ? AND branch = ? AND rownum = 1";

    private final String getVehicleQuery =
            "UPDATE Vehicle SET status = ? WHERE vid = ?";

    private final String getCustomerByDLicenseQuery =
            "SELECT COUNT(*) AS total FROM Customer WHERE dlicense = ?";

    private final String getAvailableVehiclesCountQuery =
            "SELECT COUNT(*) AS total FROM Vehicle WHERE status = '0'";

    private final String getVehicleStatusQuery =
            "SELECT status FROM Vehicle WHERE vid = ?";


    public Customer(MainMenu mainMenu) {
        this.mainMenu = mainMenu;

        try {
            addVehicle = this.mainMenu.con.prepareStatement(addVehicleQuery);

            addCustomer = this.mainMenu.con.prepareStatement(addCustomerQuery);

            makeReservation = this.mainMenu.con.prepareStatement(makeReservationQuery);

            getAvailableVehicle = this.mainMenu.con.prepareStatement(getAvailableVehicleQuery);

            getVehicleStatement = this.mainMenu.con.prepareStatement(getVehicleQuery);

            getCustomerByDLicense = this.mainMenu.con.prepareStatement(getCustomerByDLicenseQuery);

            getAvailableVehiclesCount = this.mainMenu.con.prepareStatement(getAvailableVehiclesCountQuery);

//            getCustomerInformation = this.mainMenu.con.prepareStatement(getCustomerInformationQuery);
            getVehicleStatus = this.mainMenu.con.prepareStatement(getVehicleStatusQuery);
        } catch (SQLException ex) {
            System.out.println("Message: " + ex.getMessage());
        }
    }

    void addVehicle(long vid, long vlicense, String make, String model, long year, String color, long odometer,
                    String status, String vtname, String location, String city) throws SQLException {
        addVehicle.setLong(1, vid);
        addVehicle.setLong(2, vlicense);
        addVehicle.setString(3, make);
        addVehicle.setString(4, model);
        addVehicle.setLong(5, year);
        addVehicle.setString(6, color);
        addVehicle.setLong(7, odometer);
        addVehicle.setString(8, status);
        addVehicle.setString(9, vtname);
        addVehicle.setString(10, location);
        addVehicle.setString(11, city);

        addVehicle.executeUpdate();
    }

    public void addCustomer(String dlicense, String name, String phoneNum, String address) throws SQLException {
        addCustomer.setString(1, dlicense);
        addCustomer.setString(2, name);
        addCustomer.setString(3, phoneNum);
        addCustomer.setString(4, address);

        addCustomer.executeUpdate();
    }

    public ResultSet getCustomerInformation(String name, long phoneNum) throws SQLException {
        Statement stmt = mainMenu.con.createStatement();
        String sqlStatement = getAvailableVehiclesCountQuery;

        if (name != null) {
            sqlStatement += " AND name = " + name;
        }

        if (phoneNum != 0) {
            sqlStatement += " AND phoneNum = " + phoneNum;
        }

        return stmt.executeQuery(sqlStatement);
    }

    private void setVehicleStatus(long vid, int status) throws SQLException { // 0: available, 1: rented, 2: maintainence
        getVehicleStatement.setInt(1, status);
        getVehicleStatement.setLong(2, vid);
        getVehicleStatement.executeUpdate();
    }

    public int makeReservation(String branch, String vtname, String dlicense, TimeInterval interval) throws SQLException {
        getAvailableVehicle.setString(1, vtname);
        getAvailableVehicle.setString(2, branch);
        ResultSet result = getAvailableVehicle.executeQuery();
        if (result.next()) {
            setVehicleStatus(result.getInt("vid"), 1);
            makeReservation.setString(1, vtname);
            makeReservation.setString(2, dlicense);
            makeReservation.setTimestamp(3, interval.getFrom());
            makeReservation.setTimestamp(4, interval.getTo());
            makeReservation.executeUpdate();
            return IDGen.getNextConfNum();
        }

        return 0; //incase doesn't exist
    }

    public boolean isCurrentlyRented(Long vid) throws SQLException {
        getVehicleStatus.setLong(1, vid);
        ResultSet result = getVehicleStatus.executeQuery();
        result.next();
        return result.getLong("status") == 1;
    }

    public boolean validCustomer(String dlicense) throws SQLException {
        getCustomerByDLicense.setString(1, dlicense);
        ResultSet results = getCustomerByDLicense.executeQuery();
        results.next();
        return results.getInt("total") > 0;
    }

    public int getAvailableVehiclesCount(String carType, String branch, TimeInterval interval) throws SQLException {
        Statement stmt = mainMenu.con.createStatement();
        String sqlStatement = getAvailableVehiclesCountQuery;

        if (!carType.isEmpty()) {
            sqlStatement += " AND vtname = \'" + carType + "\'";
        }

        if (!branch.isEmpty()) {
            sqlStatement += " AND branch = \'" + branch + "\'";
        } else {
            sqlStatement += " AND branch = '" + Branch.getDefault() + "'";
        }

        if (interval != null) {
            sqlStatement += " AND status = 0 ";
        }

        sqlStatement += " ORDER BY branch, vtname";

        //System.out.println("'" + sqlStatement.replaceFirst("AND", "WHERE") + "'"); // testing purposes

        ResultSet results = stmt.executeQuery(sqlStatement);

        results.next();
        return results.getInt("total");
    }

    public void showAvailableVehiclesDetails(String carType, String location) throws SQLException {
        Statement stmt = mainMenu.con.createStatement();
        String sqlStatement = "SELECT * FROM Vehicle WHERE status = '0'";

        String type = "\'" + carType + "\'";
        String loc = "\'" + location + "\'";

        if (!carType.isEmpty()) {
            sqlStatement += " AND vtname = " + type;
        }

        if (!location.isEmpty()) {
            sqlStatement += " AND branch = " + loc;
        }

        sqlStatement += " ORDER BY branch, vtname";
        ResultSet rs = stmt.executeQuery(sqlStatement);
        // get info on ResultSet
        ResultSetMetaData rsmd = rs.getMetaData();

        // get number of columns
        int numCols = rsmd.getColumnCount();

        // display column names;
        for (int i = 1; i <= numCols; i++) {
            // get column name and print it
            System.out.printf("%-10.10s", rsmd.getColumnName(i));
        }

        System.out.println();

        for (int row = 0; row < 15; row++) {
            if (!rs.next()) break;
            for (int i = 1; i <= numCols; i++) {
                System.out.printf("%-10.10s", rs.getString(i));
            }
            System.out.println();
        }
    }

    public ResultSet getAllDetailsAboutReservation(int confNum) throws SQLException {
        return mainMenu.con.createStatement().executeQuery("SELECT * FROM RESERVATION WHERE confNo = " + confNum);
    }
}
