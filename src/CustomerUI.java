import Util.TimeInterval;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;

public class CustomerUI {
    private BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

    MainMenu mainMenu;

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

            System.out.print("Reservation made for a " + carType + " from " + dlicense + " confirmed\n" +
                    "Pickup: " + puDay + " " + puTime + "\n" +
                    "Return: " + rDay + " " + rTime + "\n" +
                    "Confirmation Number: " + confNum + "\n");

        } else {
            System.out.println("No Available Vehicles");
        }
    }

    public void showAvailableVehicles() throws SQLException, IOException {
        Customer c = new Customer(mainMenu);
//        try {
        System.out.println("Location:");
        String loc = in.readLine();

        System.out.println("Vehicle Type:");
        String vType = in.readLine();

        System.out.println("Pickup Day:");
        String puDay = in.readLine();

        System.out.println("Pickup Time:");
        String puTime = in.readLine();

        System.out.println("Return Day:");
        String rDay = in.readLine();

        System.out.println("Return Time:");
        String rTime = in.readLine();


        TimeInterval timeInterval = null;

        try {
            timeInterval = new TimeInterval(puTime, puDay, rTime, rDay);
        } catch (Exception e) {}

        int count = c.getAvailableVehiclesCount(vType, loc, timeInterval);
        System.out.print("Available Vehicles:" + count + "\n");

        System.out.println("See Vehicles Details: ");
        System.out.println("1: Yes");
        System.out.println("2: No");
        int details = Integer.parseInt(in.readLine());
        if (details == 1) {
            c.showAvailableVehiclesDetails(vType, loc);
        }
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
