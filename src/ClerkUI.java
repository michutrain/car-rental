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

        ResultSet[] report = clerk.generateDailyRentalsReport(b, dateStr);

        ResultSet vehiclesRentedOut = report[0];

        System.out.println();
        System.out.println("Vehicles rented out today:");
        System.out.println();
        System.out.println("Vehicle ID");
        while (vehiclesRentedOut.next()) {
            System.out.println(vehiclesRentedOut.getInt("vid"));
        }

        ResultSet numVehicleRentedPerCategory = report[1];
        System.out.println();
        System.out.println("The number of vehicles rented per category today");
        System.out.println();
        System.out.println("Vehicle category  |  Number rented   ");
        while (numVehicleRentedPerCategory.next()) {
            System.out.println(numVehicleRentedPerCategory.getString(1) + "      " +
                    numVehicleRentedPerCategory.getLong(2));
        }

        ResultSet numRentalsPerBranch = report[2];
        System.out.println();
        System.out.println("The number of vehicles rented per branch today");
        System.out.println();
        System.out.println("Branch  |  Vehicles rented   ");
        while (numRentalsPerBranch.next()) {
            System.out.println(numRentalsPerBranch.getString(1) + "      " +
                    numRentalsPerBranch.getLong(2));
        }


        ResultSet totalNumOfVehiclesRentedToday = report[3];
        System.out.println();
        System.out.println("The total number of vehicles rented out today");
        System.out.println();
        System.out.println("Number of vehicles rented: ");
        while(totalNumOfVehiclesRentedToday.next()) {
            System.out.println(totalNumOfVehiclesRentedToday.getLong(1));
        }
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

        ResultSet[] report = clerk.generateDailyReturnsReport(b, dateStr);

        ResultSet allVehiclesReturnedToday = report[0];

        System.out.println();
        System.out.println("The vehicles returned today");
        System.out.println();
        System.out.println("Vehicle ID");

        while (allVehiclesReturnedToday.next()) {
            System.out.println(allVehiclesReturnedToday.getInt("vid"));
        }

        ResultSet numVehiclesPerCategory = report[1];

        System.out.println();
        System.out.println("The number of vehicles returned today by category");
        System.out.println();
        System.out.println("Vehicle category     |   Number");

        while (allVehiclesReturnedToday.next()) {
            System.out.println(allVehiclesReturnedToday.getString(1) + "       " +
                    allVehiclesReturnedToday.getLong(2));
        }

        ResultSet revenuePerCategory = report[2];

        System.out.println();
        System.out.println("Revenue per vehicle category earned today");
        System.out.println();
        System.out.println("Vehicle category     |   Revenue earned");

        while (allVehiclesReturnedToday.next()) {
            System.out.println(allVehiclesReturnedToday.getString(1) + "       " +
                    allVehiclesReturnedToday.getLong(2));
        }

        ResultSet subtotalsForVehicleAndRevenuePerBr = report[3];

        System.out.println();
        System.out.println("Subtotals for the number of vehicles and revenue per branch today");
        System.out.println();
        System.out.println("Branch     |   Number of vehicles    |   Revenue earned");

        while (subtotalsForVehicleAndRevenuePerBr.next()) {
            System.out.println(
                    subtotalsForVehicleAndRevenuePerBr.getString(1) + "       " +
                    subtotalsForVehicleAndRevenuePerBr.getLong(2) + "        " +
                    subtotalsForVehicleAndRevenuePerBr.getLong(3)
            );
        }

        ResultSet grandTotals = report[4];

        System.out.println();
        System.out.println("Grand totals for today");
        System.out.println();
        System.out.println("Vehicles returned     |   Revenue earned");

        while (grandTotals.next()) {
            System.out.println(grandTotals.getLong(1) + "          " + grandTotals.getLong(2));
        }
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

    public void returnVehicle() throws SQLException {
        try {
            Long vid = 0L;
            System.out.print("Enter Vehicle ID to return: \n");
            vid = Long.parseLong(in.readLine());
            while  (vid == 0L) {
                System.out.println("No Vehicle selected Please Try Again\n");
                System.out.print("Enter Vehicle ID to return: \n");
                vid = Long.parseLong(in.readLine());
            }
            Customer c = new Customer(mainMenu);
            boolean isRented = c.isCurrentlyRented(vid);

            if(!isRented) {
                System.out.print("Vehicle entered is currently not rented. Exiting to clerk menu\n");
                clerkMenu();
            } else {
                Long rid = clerk.getRentalId(vid);
                Timestamp timestamp = new Timestamp(System.currentTimeMillis());

                Long fullTank = 0L;
                System.out.print("Enter Vehicle Tank Level: \n");
                fullTank = Long.parseLong(in.readLine());

                if(fullTank == 0L){
                    System.out.print("Invalid Tank Level - setting tank level to 0 \n");
                }

                Long odometer = 0L;
                System.out.print("Enter Vehicle Odometer: \n");
                odometer = Long.parseLong(in.readLine());
                if(odometer == 0L) {
                    System.out.print("Invalid odometer - setting odometer to 0 \n");
                }
// TODO: Calculate amount owing
                Long amount = 0L;
                clerk.returnVehicle(rid, vid, timestamp, fullTank, odometer, amount);
            }
        } catch (IOException e) {
            e.getMessage();
            e.printStackTrace();
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
