import Util.Branch;
import Util.TimeInterval;
import Util.IDGen;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;

public class ClerkUI {
    BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
    private Clerk clerk;
    private MainMenu mainMenu;

    public ClerkUI(MainMenu mainMenu) {
        this.mainMenu = mainMenu;
        clerk = new Clerk(mainMenu.con);
    }

    private void dailyRentalReport(String bid, String date) throws SQLException {
        Branch b;
        String dateStr;

        if (!bid.isEmpty()) {
            b = Branch.getBranch(bid);

            if (b == null) {
                System.out.println("Unknown Branch name given");
                System.out.println();
                System.out.println("Available branches: ");
                for (Branch x : Branch.values()) {
                    System.out.println(x.getLoc());
                }
            }
        } else {
            b = null;
        }

        if (date.isEmpty()) {
            dateStr = new Date(System.currentTimeMillis()).toString();
        } else {
            dateStr = date;
        }

        clerk.generateDailyRentalsReport(b, dateStr);
    }

    private void dailyReturnReport(String bid, String date) throws SQLException {
        Branch b;
        String dateStr;

        if (!bid.isEmpty()) {
            b = Branch.getBranch(bid);

            if (b == null) {
                System.out.println("Unknown Branch name given");
                System.out.println();
                System.out.println("Available branches: ");
                for (Branch x : Branch.values()) {
                    System.out.println(x.getLoc());
                }
            }
        } else {
            b = null;
        }

        if (date.isEmpty()) {
            dateStr = new Date(System.currentTimeMillis()).toString();
        } else {
            dateStr = date;
        }

        clerk.generateDailyReturnsReport(b, dateStr);
    }

    private void reportsMenu() {
        int choice = 0;
        boolean quit = false;
        try {

            while (!quit) {
                System.out.print("\nReport Menu: \n");
                System.out.print("1:  Daily Rentals Report\n");
                System.out.print("2:  Daily Returns Report\n");
                System.out.print("5:  Back to Clerk Menu\n");
                choice = Integer.parseInt(in.readLine());

                if (choice == 1) {
                    System.out.print("\nBranch location-city (optional): \n");
                    String bid = in.readLine();
                    System.out.println("What day would you like to look at (enter in format yyyy-mm-dd) (optional. None provided means use today's date): ");
                    String date = in.readLine();
                    dailyRentalReport(bid, date);
                } else if (choice == 2) {
                    System.out.print("\nBranch location-city (optional): \n");
                    String bid = in.readLine();
                    System.out.println("What day would you like to look at (enter in format yyyy-mm-dd) (optional. None provided means use today's date): ");
                    String date = in.readLine();
                    dailyReturnReport(bid, date);
                } else if (choice == 5) {
                    quit = true;
                } else {
                    System.out.print("Invalid Choice - Please select again \n");
                }

                System.out.println(" ");
            }

            System.out.println("Returning to Clerk Menu\n");
            clerkMenu();
        } catch (IOException | SQLException e) {
            e.getMessage();
            e.printStackTrace();
            System.exit(-1);

/*
                try {
                    MainMenu.con.close();
                    System.exit(-1);
                } catch (SQLException ex) {
                    System.out.println("Message: " + ex.getMessage());
                }
*/
        }
    }

    public void returnVehicle() {
        try {
            System.out.print("Enter Vehicle ID to return: \n");
            String bid = in.readLine();
            if (bid.isEmpty()) {
                System.out.println("No Vehicle selected");
            } else {
                // TODO: If vehicle hasn't been selected, Log an error
                // TODO: If vehicle was rented, update the database to return it
            }
        } catch (IOException e) {
            e.getMessage();
            e.printStackTrace();

/*                try
                {
                    MainMenu.con.close();
                    System.exit(-1);
                }
                catch (SQLException ex)
                {
                    System.out.println("Message: " + ex.getMessage());
                }*/
        }
    }

    public void rentVehicle() throws IOException, SQLException {
//        String carType = "Compact"; // TODO: THESE NEED TO START AS NULL - THEY ONLY HAVE VALUES FOR TESTING PURPOSES
//        String location = "YVR - Vancouver";
//        String puDay = "1998-12-18";
//        String puTime = "12:00:00";
//        String rDay = "1998-12-19";
//        String rTime = "12:00:00";

        String carType = "";
        String location = "";
        String puDay = "";
        String puTime = "";
        String rDay = "";
        String rTime = "";
        while(location.isEmpty()){
            System.out.print("\nLocation: ");
            location = in.readLine();
        }

        while(carType.isEmpty()){
            System.out.print("\nCar Type: ");
            carType = in.readLine();
        }
        while(puDay.isEmpty()){
            System.out.print("Pickup Day (YYYY-MM-DD): \n");
            puDay = in.readLine();
        }

        while(puTime.isEmpty()){
            System.out.print("Pickup Time (HH:MM:SS): \n");
            puTime = in.readLine();
        }

        while(rDay.isEmpty()){
            System.out.print("Return Day (YYYY-MM-DD): \n");
            rDay = in.readLine();
        }
        while(rTime.isEmpty()){
            System.out.print("Return Time (HH:MM:SS): \n");
            rTime = in.readLine();
        }

        Timestamp pickUpTime = Timestamp.valueOf(puDay + " " + puTime);
        Timestamp dropTime = Timestamp.valueOf(rDay + " " + rTime);
        TimeInterval timeInterval = new TimeInterval(puTime, puDay, rTime, rDay);
        Customer c = new Customer(mainMenu);

        int available = c.getAvailableVehiclesCount(carType, location, timeInterval);

        if (available > 0) {

            boolean confirmed = false;
            Long pNum = 0L;
            String name = "";
            while (!confirmed) {
                String dlicense = "";
                if (pNum == 0L) {
                    System.out.print("Please provide a valid Phone Number:\n");
                    pNum = Long.getLong(in.readLine());
                } else if (name.isEmpty()) {
                    System.out.print("Please Provide a Valid Name\n");
                    name = in.readLine();
                } else {
                    boolean isValid = c.validCustomer(pNum.toString());
                    if (!isValid) {
                        System.out.print("No Existing Customer Found - Please Register:\n");

                        System.out.print("Driver's License:\n");
                        dlicense = in.readLine();

                        System.out.print("Address:\n");
                        String address = in.readLine();

                        c.addCustomer(dlicense, name, pNum, address);

                        System.out.print("Confirmed " + name + " added to Database\n");
                    }

                    ResultSet rs = c.getAvailableVehicles(carType, location);
                    rs.next();
                    long vid = rs.getLong("VID");
                    long odometer = rs.getLong("ODOMETER");
                    int rid = IDGen.getNextRID();

                    while(dlicense.isEmpty()) {
                        System.out.print("Driver's License:\n");
                        dlicense = in.readLine();
                    }

                    int confNum = IDGen.getNextConfNum();

                    clerk.rentVehicle(rid, vid, dlicense, pickUpTime, dropTime, odometer);

                    System.out.print("Reservation made for a " + carType + " from " + location + " confirmed\n" +
                            "Pickup: " + puDay + " " + puTime + "\n" +
                            "Return: " + rDay + " " + rTime + "\n" +
                            "Confirmation Number: " + confNum + "\n");
                    confirmed = true;
                }
            }
        } else {
            System.out.print("No Available Vehicles\n");
        }


    }

    public void clerkMenu() throws SQLException{
        int firstChoice = 0;
        boolean quit;
        quit = false;

        try {
/*
                // disable auto commit mode
                MainMenu.con.setAutoCommit(false);
*/

            while (!quit) {
                if (firstChoice == 0) {
                    System.out.print("\nClerk Menu: \n");
                    System.out.print("1:  Rent a Vehicle\n");
                    System.out.print("2:  Return a Vehicle\n");
                    System.out.print("3:  Reports\n");
                    System.out.print("5:  Back to Main Menu\n");
                    firstChoice = Integer.parseInt(in.readLine());
                }
                if (firstChoice == 1) {
                    rentVehicle();
                    firstChoice = 0;
                } else if (firstChoice == 2) {
                    returnVehicle();
                    firstChoice = 0;
                } else if (firstChoice == 3) {
                    reportsMenu();
                    firstChoice = 0;
                } else if (firstChoice == 5) {
                    quit = true;
                } else {
                    System.out.println("Invalid Choice - Please select again");
                    firstChoice = 0;
                }

                System.out.println(" ");
            }

            System.out.println("Returning to Home Screen\n");
            mainMenu.showMenu();
        }
//        catch (SQLException ex) {
//            System.out.println("Message: " + ex.getMessage());
//        }
        catch (IOException e) {
            System.out.println("IOException!");

/*                try
                {
                    MainMenu.con.close();
                    System.exit(-1);
                }
                catch (SQLException ex)
                {
                    System.out.println("Message: " + ex.getMessage());
                }*/
        }
    }

}
