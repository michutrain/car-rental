import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;

public class MainMenu implements ActionListener {
    // command line reader
    private BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

    public Connection con;

    // user is allowed 3 login attempts
    private int loginAttempts = 0;

    // components of the login window
    private String usernameField = "ora_michu";
    private String passwordField = "a59315275";
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
        ClerkUI clerk = new ClerkUI(this);

        try {
            menu:
            while (true) {

                System.out.println("---------------Main Menu---------------");
                System.out.println("Please choose one of the following:");

                System.out.println("1:  Customer Menu ");
                System.out.println("2:  Clerk Menu");
                System.out.println("3:  Manual Mode");
                System.out.println("5.  Quit");

                switch(in.readLine())
                {
                    case "1":
                        new CustomerUI(this);
                        break;
                    case "2":
                        clerk.clerkMenu();
                        break;
                    case "3":
                        manualMode();
                        break;
                    case "5":
                        break menu;
                    default:
                        System.out.println("Unrecognized response");
                        System.out.println();
                        break;
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
        menu:
        while (true) {
            System.out.println("1: View all tables in database");
            System.out.println("2: Add data to a database");
            System.out.println("3: Delete data from a database");
            System.out.println("4: Update data in a database");
            System.out.println("5: View data in a database");
            System.out.println("6: Go back to main menu");

            switch (in.readLine()) {
                case "1":
                    ResultSet rs = con.createStatement().executeQuery("SELECT table_name FROM user_tables");
                    System.out.println("TABLE_NAME\n" +
                            "--------------------------------------------------------------------------------\n");
                    while (rs.next()) {
                        System.out.println(rs.getString(1));
                    }
                    System.out.println();
                    break;
                case "2":
                    System.out.println("Table to add to: ");
                    String table_name = in.readLine();
                    System.out.println("Row to add: ");
                    String data_to_add = in.readLine();
                    con.createStatement().executeUpdate("INSERT INTO " + table_name + " VALUES " + data_to_add);
                    System.out.println();
                    break;
                case "3":
                    System.out.println("Table to delete from: ");
                    String table_name2 = in.readLine();
                    System.out.println("Primary key of row to delete: ");
                    String pk = in.readLine();
                    System.out.println("Primary key field name");
                    String pkFieldName = in.readLine();
                    con.createStatement().executeUpdate("DELETE FROM " + table_name2 + " WHERE " +
                            pkFieldName + " = " + pk);
                    System.out.println();
                    break;
                case "4":
                    System.out.println("Table to update: ");
                    String table_name3 = in.readLine();
                    System.out.println("Primary key of row to update: ");
                    String pk3 = in.readLine();
                    System.out.println("Primary key field name");
                    String pkfieldname3 = in.readLine();
                    System.out.println("Name of column you want to update: ");
                    String columnToUpdate = in.readLine();
                    System.out.println("New value for column: ");
                    String columnValue = in.readLine();

                    con.createStatement().executeUpdate("UPDATE " + table_name3 + " SET " +
                            columnToUpdate + " = " + columnValue + " WHERE " + pkfieldname3 + " = " + pk3);
                    System.out.println();
                    break;
                case "5":
                    System.out.println("Table's data to view: ");
                    String table_name4 = in.readLine();
                    ResultSet rs4 = con.createStatement().executeQuery("SELECT * FROM " + table_name4);
                    ResultSetMetaData resultSetMetaData = rs4.getMetaData();
                    int columnCount = resultSetMetaData.getColumnCount();

                    while (rs4.next()) {
                        for (int i = 1; i <= columnCount; i++) {
                            System.out.print(rs4.getObject(i) + "   ");
                        }
                        System.out.println();
                    }
                    System.out.println();
                    break;
                case "6":
                    break menu;
                default:
                    System.out.println("Unrecognized option");
            }
        }
    }
}
