import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;

public class customer {
    Connection con;
    String carType = "";
    String location = "";
    int choice;
    BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
    String timeInterval = ""; // TODO: What type should the time interval be? Should it be a timestamp fromTime and toTime?
    PreparedStatement ps;

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

    public void makeReservation(){
        try {
            System.out.print("\nLocation: ");
            location = in.readLine();

            System.out.print("\nCar Type: ");
            carType = in.readLine();

            System.out.print("\nTime Interval: ");
            timeInterval = in.readLine();
        }
        catch (IOException e)
        {
            System.out.println("IOException!");
        }

    }

    public void showAvailableVehicles() {
        Statement stmt;
        ResultSet rs;

        try
        {
            stmt = con.createStatement();
        // TODO: This needs to be the available vehicles, not all vehicles


            System.out.println(" ");
            // TODO: Have an input for the user to input the carType, location, and timeinterval
            // TODO: If none of the above are provided, display all available vehicles at the branch
            makeReservation();
            if(carType.isEmpty() && location.isEmpty() && timeInterval.isEmpty()) {
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
            } else {
                rs = stmt.executeQuery("SELECT COUNT(*) AS total FROM VEHICLE"); // TODO: Needs to have a condition
                System.out.println("Number of Available Vehicles:");
                rs.next();
                System.out.println(rs.getInt("total") +"\n");
                // TODO: Need to add a way to get the details of the vehicles
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

    public void customerMenu(Connection con){
        this.con = con;
        MainMenu b = new MainMenu();
        int firstChoice = 0;
        boolean quit;
        quit = false;

        try
        {
            // disable auto commit mode
            con.setAutoCommit(false);

            while (!quit)
            {
                if(firstChoice == 0){
                    System.out.print("\nCustomer Menu: \n");
                    System.out.print("1:  View Available Vehicles\n");
                    System.out.print("2:  Make a Reservation\n");
                    firstChoice = Integer.parseInt(in.readLine());
                }
                if(firstChoice == 1) {
                    showAvailableVehicles();
                    break;
                } else if (firstChoice == 2) {
                    makeReservation();
                    break;
                } else {
                    System.out.println("Invalid Choice - Please select again");
                    firstChoice = 0;
                }

                System.out.println(" ");
            }

            System.out.println("Returning to Home Screen\n");
            b.showMenu(con);
        }
        catch (SQLException ex) {
            System.out.println("Message: " + ex.getMessage());
        }
        catch (IOException e)
        {
            System.out.println("IOException!");

            try
            {
                con.close();
                System.exit(-1);
            }
            catch (SQLException ex)
            {
                System.out.println("Message: " + ex.getMessage());
            }
        }
    }
}
