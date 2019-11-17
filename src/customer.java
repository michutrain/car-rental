import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;

public class customer {
    Connection con;
    int carType = 0;
    String location = "";
    int choice;
    BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
    String timeInterval = ""; // TODO: What type should the time interval be? Should it be a timestamp fromTime and toTime?

    public void carTypeMenu(){
        try {
            System.out.print("Please choose one of the following: \n");
            System.out.print("Car Type:\n");

            // TODO: Get cars From DataBase to show as options
            System.out.print("1:  Fast Car\n");
            System.out.print("2:  Slow Car\n");
            System.out.print("50.  Back\n>> ");
            choice = Integer.parseInt(in.readLine());

            carType = choice;
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
//        catch (SQLException ex) {
//            System.out.println("Message: " + ex.getMessage());
//        }
    }

    public void locationMenu(){
        try {
            System.out.print("\nPlease Enter your location: \n");

            // TODO: Get locations From DataBase to compare with input? Or get a list of locations and get the ints
            location = in.readLine();
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
//        catch (SQLException ex) {
//            System.out.println("Message: " + ex.getMessage());
//        }
    }

    public void timeMenu(){
        try {
            System.out.print("\nPlease Enter your Time Interval \n");

            // TODO: How do we want to handle this?
            timeInterval = in.readLine();
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
//        catch (SQLException ex) {
//            System.out.println("Message: " + ex.getMessage());
//        }
    }

    public void customerMenu(Connection con){
        this.con = con;
        MainMenu b = new MainMenu();

        boolean quit;
        quit = false;

        try
        {
            // disable auto commit mode
            con.setAutoCommit(false);

            while (!quit)
            {
                System.out.print("\nCustomer Menu: \n");
                // This needs to be broken into function calls like in branch
                if(carType == 0) {
                    carTypeMenu();
                } else if (location.isEmpty()) {
                    locationMenu();
                } else if (timeInterval.isEmpty()) {
                    timeMenu();
                } else { // TODO: If the car is avaible or not....
                    System.out.print("Car Selection Finished - Exiting\n");
                    quit = true;
                }

                System.out.println(" ");
            }

            System.out.println("Returning to Home Screen\n");
            b.showMenu(con);
        }
        catch (SQLException ex) {
            System.out.println("Message: " + ex.getMessage());
        }
    }
}
