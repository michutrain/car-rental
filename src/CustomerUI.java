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
                    System.out.print("\nCustomer Menu: \n");
                    System.out.print("1:  View Available Vehicles\n");
                    System.out.print("2:  Make a Reservation\n");
                    System.out.print("5:  Back to Main Menu\n");
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
        System.out.print("\nLocation: ");
        location = in.readLine();

        System.out.print("\nCar Type: ");
        carType = in.readLine();

        System.out.print("Pickup Day: \n");
        String puDay = in.readLine();

        System.out.print("Pickup Time: \n");
        String puTime = in.readLine();

        System.out.print("Return Day: \n");
        String rDay = in.readLine();

        System.out.print("Return Time: \n");
        String rTime = in.readLine();


        if (location.isEmpty() || carType.isEmpty() || puDay.isEmpty() || puTime.isEmpty() || rDay.isEmpty() || rTime.isEmpty()) {
            System.out.print("Invalid Parameters\n");
        }

        int available = 0;
        // int available = c.getAvailableVehiclesCount(carType, location, puDay, puTime, rDay, rTime); // TODO Replace times and days with one timeInterval
        if (available > 0) {

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
                    boolean isValid = c.validCustomer(pNum);
                    if (!isValid) {
                        // c.addCustomer(pNum); TODO FIX
                        System.out.print("No Existing Customer Found - " + name + " added to Customers Database\n");
                    }
                    int confNum = 0;//  c.makeReservation(carType, location, puDay, puTime, rDay, rTime); TODO FIX
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

    public void showAvailableVehicles() throws SQLException, IOException {
        Customer c = new Customer(this.mainMenu);
//        try {
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

            int count = c.getAvailableVehiclesCount(vType, loc, new TimeInterval(puDay, puTime, rDay, rTime));
            System.out.print("Available Vehicles: " + count + "\n");

            System.out.print("See Vehicles Details: \n");
            System.out.print("1: Yes\n");
            System.out.print("2: No\n");
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
