import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;

public class customer {
//    Connection con;
    public void customerMenu(Connection con){
        branch b = new branch();
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        int choice;
        boolean quit;
        int currentChoice = 0;

        quit = false;

    try
    {
        // disable auto commit mode
        con.setAutoCommit(false);

        while (!quit)
        {
            // This needs to be broken into function calls like in Util.Branch
            System.out.print("\n\nPlease choose one of the following: \n");
            System.out.print("\nCustomer Menu: \n");
            System.out.print("Car Type:\n");
            System.out.print("1:  Fast Car\n");
            System.out.print("2:  Slow Car\n");

            System.out.print("Location\n");
            System.out.print("3:  Place A\n");
            System.out.print("4:  Place B\n");

            System.out.print("Time Interval\n");
            System.out.print("5:  1 hour\n");
            System.out.print("6:  2 hours\n");

            System.out.print("50.  Back\n>> ");

            choice = Integer.parseInt(in.readLine());

            System.out.println(" ");

            switch(choice)
            {
                case 1:   System.out.print("\nFast Car Chosen\n");; break;
                case 50:  quit = true;
            }
        }

        System.out.println("\nReturning to Home Screen\n\n");
        b.showMenu(con);
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
    catch (SQLException ex) {
        System.out.println("Message: " + ex.getMessage());
    }
    }
}
