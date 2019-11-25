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
                    " (dlicense, name, phoneNum, address)" +
                    " VALUES (?, ?, ?, ?)";

    private final String makeReservationQuery =
            "INSERT INTO Reservation" +
                    " (vtname, dlincense, fromTimestamp, toTimestamp)" +
                    " VALUES (?, ?, ?, ?)";

    private final String getAvailableVehicleQuery =
            "SELECT vid FROM Vehicle WHERE vtname = ? limit 1";

    private final String getVehicleQuery =
            "UPDATE Vehicle SET status = ? WHERE vid = ?";

    private final String getCustomerByDLicenseQuery =
            "SELECT COUNT(*) FROM Customer WHERE DLICENSE = ?";

    private final String getCustomerByPhoneNumQuery =
            "SELECT COUNT(*) FROM Customer WHERE cellphone = ?";

    private final String getAvailableVehiclesDetailsQuery =
            "SELECT * FROM VEHICLE WHERE vtname = ? and branch = ?";

    private final String getAvailableVehiclesCountQuery =
            "SELECT COUNT(*) AS total FROM Vehicle";

    private final String getVehicleStatusQuery =
            "SELECT STATUS FROM Vehicle WHERE vid = ?";

//    private final String getCustomerInformationQuery =
//            "SELECT * FROM CUSTOMER WHERE name = ? AND CELLPHONE = ?";

    public Customer(MainMenu mainMenu) {
        this.mainMenu = mainMenu;

        try {
            addVehicle = this.mainMenu.con.prepareStatement(addVehicleQuery);

            addCustomer = this.mainMenu.con.prepareStatement(addCustomerQuery);

            makeReservation = this.mainMenu.con.prepareStatement(makeReservationQuery);

            getAvailableVehicle = this.mainMenu.con.prepareStatement(getAvailableVehicleQuery);

            getVehicleStatement = this.mainMenu.con.prepareStatement(getVehicleQuery);

            getCustomerByDLicense = this.mainMenu.con.prepareStatement(getCustomerByDLicenseQuery);

            getAvailableVehiclesDetails = this.mainMenu.con.prepareStatement(getAvailableVehiclesDetailsQuery);

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

        addVehicle.executeUpdate();
    }

    public ResultSet getCustomerInformation(String name, long phoneNum) throws SQLException {
        Statement stmt = mainMenu.con.createStatement();
        String sqlStatement = getAvailableVehiclesCountQuery;

        if (name != null) {
            sqlStatement += " WHERE name = " + name;

            if (phoneNum != 0) {
                sqlStatement += " AND phoneNum = " + phoneNum;
            }

        } else if (phoneNum != 0) {
            sqlStatement += "WHERE phoneNum = " + phoneNum;
        }

        return stmt.executeQuery(sqlStatement);
    }

    private void setVehicleStatus(long vid, int status) throws SQLException { // 0: available, 1: rented, 2: maintainence
        getVehicleStatement.setInt(1, status);
        getVehicleStatement.setLong(2, vid);
        getVehicleStatement.executeQuery();
    }

    public int makeReservation(String vtname, String dlincense, TimeInterval interval) throws SQLException {
        getAvailableVehicle.setString(1, vtname);
        ResultSet result = getAvailableVehicle.executeQuery();
        if (result.next()) {
            setVehicleStatus(result.getInt("vid"), 2);
            makeReservation.setString(1, vtname);
            makeReservation.setString(2, dlincense);
            makeReservation.setString(3, interval.getFrom().toString());
            makeReservation.setString(4, interval.getTo().toString());

            return makeReservation.executeUpdate();
        }

        return 0; //incase no exist
    }

    public boolean isCurrentlyRented(Long vid) throws SQLException{
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
        String type =  "\'" + carType + "\'";
        String loc = "\'" + branch + "\'";
        int ret = 0;

        if (carType != null) {
            sqlStatement += " WHERE vtname = " + type;

            if (branch != null) {
                sqlStatement += " AND branch = " + loc;
            }

            if (interval != null) {
                sqlStatement += " AND status = 0 ";
            }

        } else if (branch != null) {
            sqlStatement += "WHERE branch = " + loc;

            if (interval != null) {
                sqlStatement += " AND status = 0 ";
            }

        } else if (interval != null) {
            sqlStatement += "WHERE status = 0 ";
        }

        sqlStatement += " ORDER BY branch, vtname";

        ResultSet results = stmt.executeQuery(sqlStatement);
        while(results.next()){
            ret = results.getInt("total");
        }
        return ret;
    }

    public ResultSet getAvailableVehicles(String carType, String location) throws SQLException {
        Statement stmt = mainMenu.con.createStatement();
        String sqlStatement = getAvailableVehiclesCountQuery;
        String type =  "\'" + carType + "\'";
        String loc = "\'" + location + "\'";
        if (carType != null) {
            sqlStatement += " WHERE vtname = " + type;

            if (location != null) {
                sqlStatement += " AND branch = " + loc;
            }

        } else if (location != null) {
            sqlStatement += "WHERE branch = " + loc;
        }

        return stmt.executeQuery(sqlStatement);
    }

    public void showAvailableVehiclesDetails(String carType, String location) throws SQLException {
        Statement stmt = mainMenu.con.createStatement();
        String sqlStatement = getAvailableVehiclesDetailsQuery;
        String type =  "\'" + carType + "\'";
        String loc = "\'" + location + "\'";
        if (carType != null) {
            sqlStatement += " WHERE vtname = " + type;

            if (location != null) {
                sqlStatement += " AND branch = " + loc;
            }

        } else if (location != null) {
            sqlStatement += "WHERE branch = " + loc;
        }

        sqlStatement += " ORDER BY branch, vtname";
        System.out.println("ORDERED SUCCESSFULLY");
        ResultSet rs = stmt.executeQuery(sqlStatement);
        System.out.println("ORDERED SUCCESSFULLY and got to next line");
        // get info on ResultSet
        ResultSetMetaData rsmd = rs.getMetaData();

        // get number of columns
        int numCols = rsmd.getColumnCount() > 15 ? 15 : rsmd.getColumnCount();

        System.out.println();

        // display column names;
        for (int i = 0; i < numCols; i++) {
            // get column name and print it
            System.out.printf("%-15s", rsmd.getColumnName(i + 1));
        }

        int count = 0;
        while (rs.next() && count < 15) {
            String make = rs.getString("MAKE");
            System.out.printf("%-10.10s", make);

            String model = rs.getString("MODEL");
            System.out.printf("%-20.20s", model);

            String vLocation = rs.getString("BRANCH");
            System.out.printf("%-20.20s", vLocation);
            count++;
        }
    }
}
