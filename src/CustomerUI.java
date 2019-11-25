import Util.Branch;
import Util.TimeInterval;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;

public class CustomerUI {
    private BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

    private MainMenu mainMenu;

    public CustomerUI(MainMenu mainMenu) throws SQLException {

        this.mainMenu = mainMenu;

        try {
            main:
            while (true) {
                System.out.println("Customer Menu: ");
                System.out.println("1:  View Available Vehicles");
                System.out.println("2:  Make a Reservation");
                System.out.println("5:  Back to Main Menu");

                switch (in.readLine()) {
                    case "1":
                        showAvailableVehicles();
                        break;
                    case "2":
                        makeReservation();
                        break;
                    case "5":
                        break main;
                    default:
                        System.out.println("Invalid Choice - Please select again");
                        break;
                }
            }

            System.out.println("Returning to Home Screen");
        } catch (IOException e) {
            System.out.println("IOException!");

            try {
                this.mainMenu.con.close();
                System.exit(-1);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void makeReservation() throws SQLException, IOException {
        String carType;
        String location;
        Customer c = new Customer(mainMenu);
        System.out.println("Location: ");
        location = in.readLine();

        System.out.println("Car Type: ");
        carType = in.readLine();

        System.out.println("Pickup Day (YYYY-MM-DD):");
        String puDay = in.readLine();

        System.out.println("Pickup Time (HH:MM:SS):");
        String puTime = in.readLine();

        System.out.println("Return Day (YYYY-MM-DD):");
        String rDay = in.readLine();

        System.out.println("Return Time (HH:MM:SS):");
        String rTime = in.readLine();


        if (location.isEmpty() || carType.isEmpty() || puDay.isEmpty() || puTime.isEmpty() || rDay.isEmpty() || rTime.isEmpty()) {
            System.out.println("Invalid Parameters");
        }

        TimeInterval timeInterval = new TimeInterval(puTime, puDay, rTime, rDay);
        int available = c.getAvailableVehiclesCount(carType, location, timeInterval);
        if (available > 0) {

            System.out.println("Please provide a valid driver's license id");
            String dlicense = in.readLine();

            boolean isValid = c.validCustomer(dlicense);
            if (!isValid) {
                System.out.println("No Existing Customer Found");

                System.out.println("Please provide a valid name:");
                String name = in.readLine();

                System.out.println("Please provide a valid phone number:");
                String pNum = in.readLine();

                System.out.println("Please provide a valid address");
                String address = in.readLine();

                c.addCustomer(dlicense, name, pNum, address);
            }

            int confNum = c.makeReservation(location, carType, dlicense, timeInterval);

            System.out.println("Reservation made for a " + carType + " from " + dlicense + " confirmed\n" +
                    "Pickup: " + puDay + " " + puTime + "\n" +
                    "Return: " + rDay + " " + rTime + "\n" +
                    "Confirmation Number: " + confNum);

        } else {
            System.out.println("No Available Vehicles");
        }
    }

    public void showAvailableVehicles() throws SQLException, IOException {
        Customer c = new Customer(mainMenu);
//        try {
        System.out.println("Location: (default: " + Branch.getDefault().getLoc() + ")");
        String loc = in.readLine();

        System.out.println("Vehicle Type (HH:MM:SS):");
        String vType = in.readLine();

        System.out.println("Pickup Day (YYYY-MM-DD):");
        String puDay = in.readLine();

        System.out.println("Pickup Time (HH:MM:SS):");
        String puTime = in.readLine();

        System.out.println("Return Day (YYYY-MM-DD):");
        String rDay = in.readLine();

        System.out.println("Return Time:");
        String rTime = in.readLine();

        int startYear = Integer.parseInt(puDay.substring(0, 4));
        int endYear = Integer.parseInt(rDay.substring(0, 4));

        if ( startYear < 1900 || startYear > 2100 || endYear < 1900 || endYear > 2100) {
            System.out.println("Invalid year range given");
            return;
        }

        TimeInterval timeInterval;

        try {
            timeInterval = new TimeInterval(puTime, puDay, rTime, rDay);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid date/time format given. Please try again");
            return;
        }

        int count = c.getAvailableVehiclesCount(vType, loc, timeInterval);
        System.out.println("Available Vehicles:" + count);

        System.out.println("See Vehicles Details: ");
        System.out.println("1: Yes");
        System.out.println("2: No");
        int details = Integer.parseInt(in.readLine());
        if (details == 1) {
            c.showAvailableVehiclesDetails(vType, loc);
        }

        mainMenu.showMenu();
//        } catch (IOException e) {
//            System.out.println("IOException!");
//
//            try {
//                mainMenu.con.close();
//                System.exit(-1);
//            } catch (SQLException ex) {
//                System.out.println("Message: " + ex.getMessage());
//            }
//        }
    }
}
