import Util.TimeInterval;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Customer {

    private PreparedStatement addVehicle;
    private PreparedStatement addCustomer;
    private PreparedStatement makeReservation;
    private PreparedStatement getAvailableVehicle;
    private PreparedStatement getAvailableVehiclesDetails;
    private PreparedStatement getVehicleStatement;
    private PreparedStatement getCustomerByPhoneNum;
    private PreparedStatement getAvailableVehiclesCount;
    private PreparedStatement getCustomerInformation;

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

    private final String getCustomerByPhoneNumQuery =
        "SELECT count(*) FROM Customer WHERE phoneNum = ?";

    private final String availableVehiclesDetailsQuery = 
        "SELECT * FROM VEHICLE WHERE vtname = ? and location = ?";

    private final String getAvailableVehiclesCountQuery =
        "SELECT * FROM Vehicle";

    private final String getCustomerInformationQuery =
            "SELECT * FROM CUSTOMER WHERE name = ? AND CELLPHONE = ?";

    public Customer(MainMenu mainMenu) {
        this.mainMenu = mainMenu;
        
        try {
            addVehicle = this.mainMenu.con.prepareStatement(addVehicleQuery);

            addCustomer = this.mainMenu.con.prepareStatement(addCustomerQuery); 

            makeReservation = this.mainMenu.con.prepareStatement(makeReservationQuery);

            getAvailableVehicle = this.mainMenu.con.prepareStatement(getAvailableVehicleQuery);
            
            getVehicleStatement = this.mainMenu.con.prepareStatement(getVehicleQuery);

            getCustomerByPhoneNum = this.mainMenu.con.prepareStatement(getCustomerByPhoneNumQuery);

            getAvailableVehiclesDetails = this.mainMenu.con.prepareStatement(availableVehiclesDetailsQuery);

            getAvailableVehiclesCount = this.mainMenu.con.prepareStatement(getAvailableVehiclesCountQuery);

            getCustomerInformation = this.mainMenu.con.prepareStatement(getCustomerInformationQuery);
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

    public void addCustomer(String dlicense, String name, long phoneNum, String address) throws SQLException {
        addCustomer.setString(1, dlicense);
        addCustomer.setString(2, name);
        addCustomer.setLong(3, phoneNum);
        addCustomer.setString(4, address);

        addVehicle.executeUpdate();
    }

    public ResultSet getCustomerInformation(String name, long phoneNum) throws SQLException {
        getCustomerInformation.setString(1, name == null ? "*" : name);
        getCustomerInformation.setString(2, phoneNum == 0 ? "*" : Long.toString(phoneNum));
        return getAvailableVehiclesDetails.executeQuery();
    }

    private void setVehicleStatus(long vid, int status) throws SQLException { // 0: available, 1: rented, 2: maintainence
        getVehicleStatement.setInt(1, status);
        getVehicleStatement.setLong(2, vid);
        getVehicleStatement.executeQuery();
    }

        
    public int makeReservation(String vtname, String dlincense, TimeInterval interval) throws SQLException{
        // TODO: Make a reservation for the parameters
        // TODO: Set the vehicle Status to reserved
        // TODO: return the confirmation number (reservation CONFNO)
        
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

    public boolean validCustomer(String phoneNum) throws SQLException {
        getCustomerByPhoneNum.setString(1, phoneNum);
        ResultSet results = getCustomerByPhoneNum.executeQuery();
        return results.getInt("total") > 0;
    }

    public int getAvailableVehiclesCount(String carType, String location, TimeInterval interval) throws SQLException {
        Statement stmt = mainMenu.con.createStatement();

        String sqlStatement = getAvailableVehiclesCountQuery;

        if (carType != null) {
            sqlStatement += " WHERE vtname = " + carType;

            if (location != null) {
                sqlStatement += " AND location = " + location;
            }

            if (interval != null) {
                // TODO
            }
        }

        else if (location != null) {
            sqlStatement += "WHERE location = " + location;

            if (interval != null) {
                // TODO
            }
        }
        else if (interval != null) {
            // TODO
        }

//        boolean isFirstAttributeToFilr = true;
//        if (carType != null)
//            sqlStatement += "WHERE
//            isFirstAttributeToFilter false;

//            sqlStatement += " vtname " + carType;
//

//        if (location != null)
//            if (!isFirstAttributeToFier) {
//                sqlStatement += " AND";
//            } else {
//                sqlStatement += " WHERE";
//                isFirstAttributeToFilter = false;
//            }
//
//            sqlStatement += " location = " + location;
//        }
//
//        if (timestamp != null) {
//            if (!isFirstAttributeToFilter) {
//                sqlStatement += " AND";
//            } else {
//                sqlStatement += "WHERE ";
//            }
//        }

        sqlStatement += " ORDER BY location, vtname";

        ResultSet results = stmt.executeQuery(sqlStatement);
        return results.getInt("total");
    }

    public ResultSet getAvailableVehicles(String carType, String location) throws SQLException {
        getAvailableVehiclesDetails.setString(1, carType == null ? "*" : carType);
        getAvailableVehiclesDetails.setString(2, location == null ? "*" : location);
        return getAvailableVehiclesDetails.executeQuery();
    }

    public void showAvailableVehiclesDetails(String carType, String location) throws SQLException {
        ResultSet rs;

        // TODO: This needs to work if any/all of the above params are empty
        // TODO: If all params are empty, returns all available vehicles at that branch
        rs = getAvailableVehicles(carType, location);

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

            String vLocation = rs.getString("LOCATION");
            System.out.printf("%-20.20s", vLocation);
            count++;
        }
    }
}