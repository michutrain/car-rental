import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;

public class customer {

    public void addVehicle() {
//        try {
//            ps = con.prepareStatement(
//            "INSERT INTO VEHICLE(vid, vlicense, make, model, year, color, odometer, status, vtname, location, city)" +
//                    "VALUES (1234, '4321', 'Ford', 'Mustang', '2019', 'Red', 0, 'True', 'car', 'Kits', 'Vancouver')");
//            ps.executeUpdate();
//            System.out.println("Car Added\n");
//        } catch (SQLException ex) {
//            System.out.println("Message: " + ex.getMessage());
//        }
    }

    public void addCustomer(String name, String phoneNum){
        // TODO: Add customer to database
    }

    public int makeReservation(String carType, String location, String fromDay, String fromTime, String toDay, String toTime){
        // TODO: Make a reservation for the parameters
        // TODO: Set the vehicle Status to reserved
        // TODO: return the confirmation number (reservation CONFNO)
        return 0; // stub
    }

    public boolean validCustomer(String name, String phoneNum){
        // TODO: Check is a customer is a valid customer or not
        return false;
    }

    public int getAvailableVehiclesCount(String carType, String location, String fromDay, String fromTime, String toDay, String toTime) {
        // TODO: Get the number of available vehicles matching the inputs (inputs could be null) and return it as a string
        return 0; // Stub
    }

    public void showAvailableVehiclesDetails(String carType, String location, String fromDay, String fromTime, String toDay, String toTime) {
        Statement stmt;
        ResultSet rs;

        try
        {
            // TODO: This needs to work if any/all of the above params are empty
            // TODO: If all params are empty, returns all available vehicles at that branch
            stmt = MainMenu.con.createStatement();
            rs = stmt.executeQuery("SELECT * FROM VEHICLE"); // TODO: Needs to have a condition

            // get info on ResultSet
            ResultSetMetaData rsmd = rs.getMetaData();

            // get number of columns
            int numCols = rsmd.getColumnCount() > 15 ? 15 : rsmd.getColumnCount();

            System.out.println(" ");

            // display column names;
            for (int i = 0; i < numCols; i++)
            {
                // get column name and print it
                System.out.printf("%-15s", rsmd.getColumnName(i+1));
            }
            int count = 0;
            while(rs.next() && count < 15)
            {
                String make = rs.getString("MAKE");
                System.out.printf("%-10.10s", make);

                String model = rs.getString("MODEL");
                System.out.printf("%-20.20s", model);

                String vLocation = rs.getString("LOCATION");
                System.out.printf("%-20.20s", vLocation);
                count++;
            }

            // close the statement;
            // the ResultSet will also be closed
            stmt.close();
        }
        catch (SQLException ex)
        {
            System.out.println("Message: " + ex.getMessage());
        }
    }

}
