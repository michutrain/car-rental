import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MainMenu implements ActionListener {
    // command line reader
    private BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

    public Connection con;

    // user is allowed 3 login attempts
    private int loginAttempts = 0;

    // components of the login window
    private String usernameField = "ora_ks376";
    private String passwordField = "a31356165";
    private JFrame mainFrame;


    /*
     * constructs login window and loads JDBC driver
     */
    public MainMenu()
    {
        mainFrame = new JFrame("User Login");

        JButton loginButton = new JButton("Log In");

        JPanel contentPane = new JPanel();
        mainFrame.setContentPane(contentPane);


        // layout components using the GridBag layout manager

        GridBagLayout gb = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();

        contentPane.setLayout(gb);
        contentPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // place the login button
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.insets = new Insets(5, 10, 10, 10);
        c.anchor = GridBagConstraints.CENTER;
        gb.setConstraints(loginButton, c);
        contentPane.add(loginButton);

        // OK button with action event handler
        loginButton.addActionListener(this);

        // anonymous inner class for closing the window
        mainFrame.addWindowListener(new WindowAdapter()
        {
            public void windowClosing(WindowEvent e)
            {
                System.exit(0);
            }
        });

        // size the window to obtain a best fit for the components
        mainFrame.pack();

        // center the frame
        Dimension d = mainFrame.getToolkit().getScreenSize();
        Rectangle r = mainFrame.getBounds();
        mainFrame.setLocation( (d.width - r.width)/2, (d.height - r.height)/2 );

        // make the window visible
        mainFrame.setVisible(true);

        try
        {
            // Load the Oracle JDBC driver
            DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
        }
        catch (SQLException ex)
        {
            System.out.println("Message: " + ex.getMessage());
            System.exit(-1);
        }
    }


    /*
     * connects to Oracle database named ug using user supplied username and password
     */
    private boolean connect()
    {
        String connectURL = "jdbc:oracle:thin:@localhost:1522:stu";

        try
        {
            con = DriverManager.getConnection(connectURL,usernameField,passwordField);

            System.out.println("\nConnected to Oracle!");
            return true;
        }
        catch (SQLException ex)
        {
            System.out.println("Message: " + ex.getMessage());
            return false;
        }
    }


    /*
     * event handler for login window
     */
    public void actionPerformed(ActionEvent e)
    {
        if ( connect() )
        {
            // if the username and password are valid,
            // remove the login window and display a text menu
            mainFrame.dispose();
            showMenu();
        }
        else
        {
            loginAttempts++;

            if (loginAttempts >= 3)
            {
                mainFrame.dispose();
                System.out.println("Login Attempt Failed");
                System.exit(-1);
            }
        }

    }


    /*
     * displays simple text interface
     */
    public void showMenu()
    {
        int choice;
        boolean quit;
        ClerkUI clerk = new ClerkUI(this);
        int currentChoice = 0;
        quit = false;

        try {
            while (!quit) {

                if(currentChoice == 1) {
                    CustomerUI c = new CustomerUI(this);
                } else if (currentChoice == 2) {
                    clerk.clerkMenu();
                } else if (currentChoice == 3) {
                    manualMode();
                } else {
                    System.out.print("---------------Main Menu---------------");
                    System.out.print("\nPlease choose one of the following: \n");

                    System.out.print("1:  Customer Menu\n");
                    System.out.print("2:  Clerk Menu\n");
                    System.out.print("3: Write custom SQL query");
                    System.out.print("5.  Quit\n>> ");
                }

                choice = Integer.parseInt(in.readLine());

                System.out.println(" ");

                switch(choice)
                {
                    case 1:  currentChoice = 1; break;
                    case 2:  currentChoice = 2; break;
                    case 3:  currentChoice = 3; break;
                    case 5:  quit = true;
                }
            }

            con.close();
            in.close();
            System.out.println("\nGood Bye!\n\n");
            System.exit(0);
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
            System.out.println(ex.getMessage());
           ex.printStackTrace();
        }
    }

    public static void main(String args[])
    {
        MainMenu b = new MainMenu();
    }

    private void manualMode() throws SQLException , IOException{
        System.out.println("1: Update Database");
        System.out.println("2: Query database");
        String str = in.readLine();
        if (str == "1") {
            System.out.println("Enter update SQL query");
            str = in.readLine();
            con.createStatement().executeUpdate(str);
        } else {
            System.out.println("Enter query");
            str = in.readLine();
            ResultSet rs = con.createStatement().executeQuery(str);
        }
    }
}
