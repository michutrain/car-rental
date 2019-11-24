import Util.Branch;

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

    public void rentVehicle() {
        try {
            System.out.print("Location: \n");
            String loc = in.readLine();

            System.out.print("Vehicle Type: \n");
            String vType = in.readLine();

            System.out.print("Pickup Day: \n");
            String puDay = in.readLine();

            System.out.print("Pickup Time: \n");
            String puTime = in.readLine();

            System.out.print("Return Day: \n");
            String rDay = in.readLine();

            System.out.print("Return Time: \n");
            String rTime = in.readLine();

            if (loc.isEmpty() || vType.isEmpty() || puDay.isEmpty() || puTime.isEmpty() || rDay.isEmpty() || rTime.isEmpty()) {
                System.out.print("Invalid Parameters\n");
                return;
            }
            boolean available = false;
            // TODO: check if vehicle is available (SQL)
            if (available) {
                boolean confirmed = false;
                String pNum = "";
                String name = "";
                while (!confirmed) {

                    if (pNum.isEmpty()) {
                        System.out.print("Please provide a valid Phone Number:\n");
                        pNum = in.readLine();
                    } else if (name.isEmpty()) {
                        System.out.print("Please Provide a Valid Name\n");
                        name = in.readLine();
                    } else {
                        // TODO: Check if customer is part of database
                        // Customer is part of Database: change vehicle status to rented
                        // Customer is not part of Database: Ask for Necessary information to add to database and add them
                        confirmed = true;
                    }
                }
            } else {
                System.out.print("No Available Vehicles\n");
            }
        } catch (IOException e) {
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
                    System.out.print("4:  Cancel a Reservation\n"); // TODO: Needs to be implemented
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
