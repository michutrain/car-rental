import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;

public class CustomerUI {
    private BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

    public void makeReservation(){
        try {
            String carType;
            String location;
            customer c = new customer();
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


            if(location.isEmpty() || carType.isEmpty() || puDay.isEmpty() || puTime.isEmpty() || rDay.isEmpty() || rTime.isEmpty()){
                System.out.print("Invalid Parameters\n");
            }

            int available = c.getAvailableVehiclesCount(carType, location, puDay, puTime, rDay, rTime);
            if(available > 0) {

                boolean confirmed = false;
                String pNum = "";
                String name = "";
                while(!confirmed) {
                    if(pNum.isEmpty()){
                        System.out.print("Please provide a valid Phone Number:\n");
                        pNum = in.readLine();
                    }
                    else if (name.isEmpty()){
                        System.out.print("Please Provide a Valid Name\n");
                        name = in.readLine();
                    } else {
                        boolean isValid = c.validCustomer(name, pNum);
                        if(!isValid){
                            c.addCustomer(name, pNum);
                            System.out.print("No Existing Customer Found - " + name + " added to Customers Database\n");
                        }
                        int confNum = c.makeReservation(carType, location, puDay, puTime, rDay, rTime);
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
        catch (IOException e)
        {
            System.out.println("IOException!");
        }

    }

    public void showAvailableVehicles() {
        customer c = new customer();
        try{
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

            int count = c.getAvailableVehiclesCount(vType, loc, puDay, puTime, rDay, rTime);
            System.out.print("Available Vehicles: " + count +  "\n");

            System.out.print("See Vehicles Details: \n");
            System.out.print("1: Yes\n");
            System.out.print("2: No\n");
            int details = Integer.parseInt(in.readLine());
            if(details == 1){
                c.showAvailableVehiclesDetails(vType, loc, puDay, puTime, rDay, rTime);
            }
        }
        catch (IOException e)
        {
            System.out.println("IOException!");

            try
            {
                MainMenu.con.close();
                System.exit(-1);
            }
            catch (SQLException ex)
            {
                System.out.println("Message: " + ex.getMessage());
            }
        }
    }

    public void customerMenu(){
        MainMenu b = new MainMenu();
        int firstChoice = 0;
        boolean quit;
        quit = false;

        try
        {
            while (!quit)
            {
                if(firstChoice == 0){
                    System.out.print("\nCustomer Menu: \n");
                    System.out.print("1:  View Available Vehicles\n");
                    System.out.print("2:  Make a Reservation\n");
                    System.out.print("5:  Back to Main Menu\n");
                    firstChoice = Integer.parseInt(in.readLine());
                }
                if(firstChoice == 1) {
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
            b.showMenu();
        }
        catch (IOException e)
        {
            System.out.println("IOException!");

            try
            {
                MainMenu.con.close();
                System.exit(-1);
            }
            catch (SQLException ex)
            {
                System.out.println("Message: " + ex.getMessage());
            }
        }
    }

}
