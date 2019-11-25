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
        int firstChoice = 0;
        boolean quit;
        quit = false;

        try {
            while (!quit) {
                if (firstChoice == 0) {
                    System.out.println("Customer Menu: ");
                    System.out.println("1:  View Available Vehicles");
                    System.out.println("2:  Make a Reservation");
                    System.out.println("5:  Back to Main Menu");
                    firstChoice = Integer.parseInt(in.readLine());
                }
                if (firstChoice == 1) {
                    showAvailableVehicles();
                    firstChoice = 0;
                } else if (firstChoice == 2) {
                    makeReservation();
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
        } catch (IOException e) {
            System.out.println("IOException!");

            try {
                this.mainMenu.con.close();
                System.exit(-1);
            } catch (SQLException ex) {
                System.out.println("Message: " + ex.getMessage());
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

            boolean confirmed = false;
            String pNum = "";
            String name = "";
            String dlicense = "";
            String address = "";
            while (!confirmed) {

                if (pNum.isEmpty()) {
                    System.out.println("Please provide a valid phone number:");
                    pNum = in.readLine();

                } else if (name.isEmpty()) {
                    System.out.println("Please provide a valid name:");
                    name = in.readLine();

                } else {
                    boolean isValid = c.validCustomer(pNum);
                    if (!isValid) {
                        System.out.println("No Existing Customer Found - " + name + " added to Customers Database");

                        System.out.println("Please provide a valid driver's license id");
                        dlicense = in.readLine();

                        System.out.println("Please provide a valid address");
                        address = in.readLine();

                        c.addCustomer(dlicense, name, pNum, address);
                    }
                    int confNum = c.makeReservation(carType, location, timeInterval);
                    System.out.print("Reservation made for a " + carType + " from " + location + " confirmed\n" +
                            "Pickup: " + puDay + " " + puTime + "\n" +
                            "Return: " + rDay + " " + rTime + "\n" +
                            "Confirmation Number: " + confNum + "\n");
                    confirmed = true;
                }
            }
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

            int count = c.getAvailableVehiclesCount(vType, loc, new TimeInterval(puTime, puDay, rTime, rDay));
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
