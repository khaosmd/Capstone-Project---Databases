//Classes that are used in the program are imported

import java.io.*;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.sql.Date;
import java.util.InputMismatchException;
import java.util.Scanner;
import static java.lang.Integer.parseInt;

//Create the Capstone1 class and the main method

public class Capstone1 {

    public static void main(String[] args) {

        //Try block to test for input mismatch exceptions.
        //A project object is declared and the array list that will contain all the projects is created.
        //A scanner is constructed, which is used to read the user's input based on a message printed to screen.
        //this input is assigned to a variable and parsed from a string to an integer.

        try {

            Project project;
            ArrayList<Project> projectList = new ArrayList<>();

            Scanner sc = new Scanner(System.in);
            System.out.println("Enter \"1\" if you'd like to load projects from the database. Type \"2\" if you'd like to capture" +
                    " the details of a project manually");

            String choiceString = sc.nextLine();
            int choice = parseInt(choiceString);

            //Try and catch block to catch SQL exceptions.
            //The readDatabase method is run with the projectList parameter if user inputs 1, and the captureProject
            // method is run if 2 is inputted and the project is added to the projectList array.
            //Any SQL exception is caught and a message is printed to the screen.

            try {
                if (choice == 1) {
                    readDatabase(projectList);

                } else if (choice == 2) {
                    project = captureProject();

                    projectList.add(project);
                }
            } catch (SQLException e) {
                System.out.println("SQL Exception.");
                e.printStackTrace();
            }

            //A message is printed to the screen asking for user input and this input is parsed to an integer.
            //If the user enters 1, the updateDetails method is run.

            System.out.println("Would you like to update the details of a project? Type \"1\" for yes or \"2\" for no");
            String updateProject = sc.nextLine();
            int updateP = parseInt(updateProject);

            if (updateP == 1) {
                updateDetails();
            }

            //A message is printed to the screen asking for user input and this input is parsed to an integer.
            //If the user enters 1, the viewIncomplete method is run.

            System.out.println("Would you like to see a list of projects that still need to be completed? Type \"1\" for yes " +
                    "or \"2\" for no");

            String incompleteString = sc.nextLine();
            int incomplete = parseInt(incompleteString);

            if (incomplete == 1) {
                viewIncomplete();
            }

            //A message is printed to the screen asking for user input and this input is parsed to an integer.
            //If the user enters 1, the overdue method is run.

            System.out.println("Would you like to see a list of projects that are past the due date? Type \"1\" for yes " +
                    "or \"2\" for no");

            String pastDueString = sc.nextLine();
            int pastDue = parseInt(pastDueString);

            if (pastDue == 1) {
                overdue();
            }

            //A message is printed to the screen asking the user for input. If the user enters 1, this string is
            //parsed to an integer and an if statement is used to run the select method.

            System.out.println("Would you like to Find and select a project? Type \"1\" for yes " +
                    "or \"2\" for no");

            String findString = sc.nextLine();
            int find = parseInt(findString);

            if (find == 1) {
                select();
            }

            //An array list of type invoice is constructed.
            //A for loop is used to iterate from the project in index position 0 of the projectList array to the last
            //index position, and an if statement is used to test whether the fee variable doesn't equal the paid variable
            //(accessed via the getters).
            //If this is true, an invoice is created by calling the invoice constructor and setting the parameters of the
            //invoice by getting them from the customer corresponding to the project with the index value that makes the
            //if statement true.
            //This invoice is then added to the invoice arraylist. The status of its corresponding project is set to
            //finalised, and a completion date (current date) is added.

            ArrayList<Invoice> invoiceList = new ArrayList<>();

            for (int i = 0; i < projectList.size(); i++) {
                if (projectList.get(i).getFee() != projectList.get(i).getPaid()) {

                    Invoice invoice = new Invoice();
                    Person customer = projectList.get(i).person[2];
                    invoice.setCustomer(customer.getF_name());
                    invoice.setCustomer(customer.getS_name());
                    invoice.setTelephone(customer.getTel());
                    invoice.setEmailAdd(customer.getEmail());
                    invoice.setPhysAdd(customer.getAddress());
                    invoice.setPaidAmount(projectList.get(i).getPaid());
                    invoice.setTotal(projectList.get(i).getFee());

                    invoiceList.add(invoice);

                    projectList.get(i).setStatus("finalised");
                   projectList.get(i).setCompletionDate(LocalDate.now());
                }
            }

            //A try and catch block that prints an error message to screen if the invoice or project files can't be
            //created.
            //The createInvoiceFile method is run.

            try {
                createInvoiceFile(invoiceList);
            } catch (IOException e) {
                System.out.println("The file could not be created");
                e.printStackTrace();
            }

            //Catch block that prints an error to screen if the user enters a number when it should have been a letter, and
            //vice versa, for all the above user inputs in the corresponding try block.

        } catch (InputMismatchException e) {
            System.out.println("You typed a number when you should have typed a letter or vice versa.");
            e.printStackTrace();
        }
    }

    //readDatabase method that takes the projectList array list as a parameter.
    //A connection object called connection is created which connects to the database. A statement object is also created,
    //as well as a string query. The statement is executed, which sends the query to the database and returns a result
    //of type ResultSet. The maximum number of rows of the first column is then stored in a variable that will be used
    //to loop through all the rows of the database table.

    public static void readDatabase(ArrayList<Project> projectList) throws SQLException {
        try {
            Connection connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/poisepms?useSSL=false", "otheruser",
                    "swordfish");

            Statement stmt = connection.createStatement();

            String query = "SELECT MAX(ProjectNo) FROM projects";

            ResultSet result;

            result = stmt.executeQuery(query);

            int max = 0;

            while (result.next()) {
                max = result.getInt(1);
            }

            //Variables are declared and initiated as well as the project object and person array.

            Project project;
            int number;
            String projectName = null;
            String building = null;
            String projectAddress = null;
            int erf = 0;
            float fee = 0;
            float paid = 0;
            LocalDate deadline = null;
            String status;
            LocalDate completionDate;

            Person[] person;

            //A for loop is created that loops through all the rows in the table (from 1 to the max/last row). Queries
            //are stored as strings, and these strings are used in prepared statements which are then executed. The queries
            //find the values of the specified columns for a specific row (determined by the project number), and stores
            //these results as variables that will be used as parameters in the Project constructor in order to create
            //a project object.

            for (int i = 1; i >= 1 & i <= max; i++) {

                String projectNumber = Integer.toString(i);
                number = i;

                String nameQuery = "select ProjectName from projects where ProjectNo = ?";
                String buildingQuery = "select Building from projects where ProjectNo = ?";
                String projectAddressQuery = "select ProjectAddress from projects where ProjectNo = ?";
                String erfQuery = "select ERF from projects where ProjectNo = ?";
                String feeQuery = "select Fee from projects where ProjectNo = ?";
                String paidQuery = "select Paid from projects where ProjectNo = ?";
                String deadlineQuery = "select Deadline from projects where ProjectNo = ?";
                String statusQuery = "select Status from projects where ProjectNo = ?";
                String completionDateQuery = "select CompletDate from projects where ProjectNo = ?";

                PreparedStatement preparedStatement1 = connection.prepareStatement(nameQuery);
                preparedStatement1.setString(1, projectNumber);
                ResultSet results1 = preparedStatement1.executeQuery();

                while (results1.next()) {
                    projectName = results1.getString("ProjectName");
                }

                PreparedStatement preparedStatement2 = connection.prepareStatement(buildingQuery);
                preparedStatement2.setString(1, projectNumber);
                ResultSet results2 = preparedStatement2.executeQuery();

                while (results2.next()) {
                    building = results2.getString("Building");
                }

                PreparedStatement preparedStatement3 = connection.prepareStatement(projectAddressQuery);
                preparedStatement3.setString(1, projectNumber);
                ResultSet results3 = preparedStatement3.executeQuery();

                while (results3.next()) {
                    projectAddress = results3.getString("ProjectAddress");
                }

                PreparedStatement preparedStatement4 = connection.prepareStatement(erfQuery);
                preparedStatement4.setString(1, projectNumber);
                ResultSet results4 = preparedStatement4.executeQuery();

                while (results4.next()) {
                    erf = results4.getInt("ERF");
                }

                PreparedStatement preparedStatement5 = connection.prepareStatement(feeQuery);
                preparedStatement5.setString(1, projectNumber);
                ResultSet results5 = preparedStatement5.executeQuery();

                while (results5.next()) {
                    fee = results5.getFloat("Fee");
                }

                PreparedStatement preparedStatement6 = connection.prepareStatement(paidQuery);
                preparedStatement6.setString(1, projectNumber);
                ResultSet results6 = preparedStatement6.executeQuery();

                while (results6.next()) {
                    paid = results6.getFloat("Paid");
                }

                PreparedStatement preparedStatement7 = connection.prepareStatement(deadlineQuery);
                preparedStatement7.setString(1, projectNumber);
                ResultSet results7 = preparedStatement7.executeQuery();

                while (results7.next()) {
                    java.sql.Date deadlineSql = results7.getDate("Deadline");
                    deadline = deadlineSql.toLocalDate();

                }

                PreparedStatement preparedStatement8 = connection.prepareStatement(statusQuery);
                preparedStatement8.setString(1, projectNumber);
                ResultSet results8 = preparedStatement8.executeQuery();

                while (results8.next()) {
                    status = results8.getString("Status");
                }

                PreparedStatement preparedStatement9 = connection.prepareStatement(completionDateQuery);
                preparedStatement9.setString(1, projectNumber);
                ResultSet results9 = preparedStatement9.executeQuery();

                while (results9.next()) {
                    java.sql.Date completionDateSql = results9.getDate("CompletDate");

                    if (completionDateSql == null) {
                        completionDate = null;
                    } else {
                        completionDate = completionDateSql.toLocalDate();
                    }

                }

                //An array of type Person (object) of size 4 is created and variables are declared.

                person = new Person[4];

                String role = null;
                String s_name = null;
                String f_name = null;
                String tel = null;
                String email = null;
                String address = null;
                Float invoiceAmount = null;

                //A for loop to iterate from person[0] to person[3] is used to assign the below variables to each role.

                //Queries are stored as strings, and these strings are used in prepared statements which are then executed. The queries
                //find the values of the specified columns for specific rows (determined by the project number), and stores
                //these results as variables that will be used as parameters in the Person constructor in order to create
                //a person object.

                for (int x = 0; x < person.length; x++) {

                    if (x == 0) {
                        role = "Architect";
                    }
                    if (x == 1) {
                        role = "Project Manager";
                    }
                    if (x == 2) {
                        role = "Customer";
                    }
                    if (x == 3) {
                        role = "Structural Engineer";
                    }

                    //

                    String s_nameQuery = "select Sname from persons_project_1 where Role = ? and ProjectNo = ?";
                    String f_nameQuery = "select Fname from persons_project_1 where Role = ? and ProjectNo = ?";
                    String telQuery = "select Tel from persons_project_1 where Role = ? and ProjectNo = ?";
                    String emailQuery = "select Email from persons_project_1 where Role = ? and ProjectNo = ?";
                    String addressQuery = "select PhysAddress from persons_project_1 where Role = ? and ProjectNo = ?";
                    String invoiceQuery = "select Invoice_Amount from persons_project_1 where Role = ? and ProjectNo = ?";

                    PreparedStatement preparedStatement10 = connection.prepareStatement(s_nameQuery);
                    preparedStatement10.setString(1, role);
                    preparedStatement10.setInt(2, number);
                    ResultSet results10 = preparedStatement10.executeQuery();

                    while (results10.next()) {
                        s_name = results10.getString("Sname");
                    }

                    PreparedStatement preparedStatement11 = connection.prepareStatement(f_nameQuery);
                    preparedStatement11.setString(1, role);
                    preparedStatement11.setInt(2, number);
                    ResultSet results11 = preparedStatement11.executeQuery();

                    while (results11.next()) {
                        f_name = results11.getString("Fname");
                    }

                    PreparedStatement preparedStatement12 = connection.prepareStatement(telQuery);
                    preparedStatement12.setString(1, role);
                    preparedStatement12.setInt(2, number);
                    ResultSet results12 = preparedStatement12.executeQuery();

                    while (results12.next()) {
                        tel = results12.getString("Tel");
                    }

                    PreparedStatement preparedStatement13 = connection.prepareStatement(emailQuery);
                    preparedStatement13.setString(1, role);
                    preparedStatement13.setInt(2, number);
                    ResultSet results13 = preparedStatement13.executeQuery();

                    while (results13.next()) {
                        email = results13.getString("Email");
                    }

                    PreparedStatement preparedStatement14 = connection.prepareStatement(addressQuery);
                    preparedStatement14.setString(1, role);
                    preparedStatement14.setInt(2, number);
                    ResultSet results14 = preparedStatement14.executeQuery();

                    while (results14.next()) {
                        address = results14.getString("PhysAddress");
                    }

                    PreparedStatement preparedStatement15 = connection.prepareStatement(invoiceQuery);
                    preparedStatement15.setString(1, role);
                    preparedStatement15.setInt(2, number);
                    ResultSet results15 = preparedStatement15.executeQuery();

                    while (results15.next()) {
                        invoiceAmount = results15.getFloat("Invoice_Amount");
                    }

                    //New person object p is constructed.

                    Person p = new Person(role, s_name, f_name, tel, email, address, invoiceAmount);
                    person[x] = p;
                }


            //A new project is constructed using the previous variables, and this project is added to the projectList
            //array list.

                project = new Project(number, projectName, building, projectAddress, erf, fee, paid, deadline);

                project.person = person;

                projectList.add(project);
            }

            //Any SQL exceptions are caught and a message is printed to the screen.

        } catch (SQLException e) {
            System.out.println("SQL Exception.");
            e.printStackTrace();
        }
    }

    //The updateDetails method is created.
    //In a try block variables are declared, a scanner is constructed and and a connection object called connection
    //is created which connects to the database. The scanner is used to read the user's input based on a message
    //printed to screen multiple times, and the input values are stored in invariables. The user is asked
    //if they want to update the table using the project name or number as a primary key.


    public static void updateDetails() {

        try {
            String building;
            String projectAddress;
            int erf;
            float fee;
            float paid;
            java.sql.Date deadline;

            Scanner input = new Scanner(System.in);

            Connection connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/poisepms?useSSL=false", "otheruser",
                    "swordfish");

            System.out.println("If you'd like to update details by searching for the project number, enter \"1\". " +
                    "Press \"2\" if you'd like to search by project name: ");

            String search = input.nextLine();
            int selectNo = parseInt(search);

            System.out.println("Enter the existing project number. If there is no name, enter \"none\": ");
            int oldNumber = input.nextInt();

            input.nextLine();

            System.out.println("Enter the existing project name. If there is no name, enter \"none\": ");
            String oldName = input.nextLine();

            System.out.println("Enter the type of building being designed: ");
            building = input.nextLine();

            System.out.println("Enter the physical address of the project: ");
            projectAddress = input.nextLine();

            System.out.println("Enter the ERF number of the project: ");
            erf = input.nextInt();

            input.nextLine();

            System.out.println("Enter the total fee being charged for the project: ");

            //Convert the value captured as a string object to a float variable.

            String feeString = input.nextLine();
            fee = Float.parseFloat(feeString);

            System.out.println("Enter the total amount paid to date for the project: ");

            //Convert the value captured as a string object to a float variable.

            String paidString = input.nextLine();
            paid = Float.parseFloat(paidString);

            System.out.println("Enter the deadline for the project (yyyy/MM/dd): ");

            //The deadlineString string is parsed to a LocalDate object

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
            String deadlineString = input.nextLine();

            //convert String to LocalDate
            LocalDate date = LocalDate.parse(deadlineString, formatter);

            deadline = java.sql.Date.valueOf(date);

            System.out.println("Enter the current status of the project (finalised or not finalised): ");
            String status = input.nextLine();

            System.out.println("Enter the completion date of the project: ");

            String stringDate = input.nextLine();

            //convert String to LocalDate
            LocalDate parsedDate = LocalDate.parse(stringDate, formatter);

            java.sql.Date completionDate = java.sql.Date.valueOf(parsedDate);

            System.out.println("Enter the new project number: ");
            int newNumber = input.nextInt();

            input.nextLine();

            System.out.println("Enter the new project name: ");
            String newName = input.nextLine();

            //Two prepared statement objects are created with variables - one which uses the project number as a condition
            //and the other which uses the project name as a condition. These variables are populated with the
            //variables gotten above using an if statement to determine which of the two prepared statements to populate.
            //The statement is then executed, which sends the query to the database and updates the relevant row in the table.
            //The number of records updated is printed to the screen.

            PreparedStatement insertStatement1 = connection.prepareStatement(
                    "update projects set ProjectNo = ?, ProjectName = ?, Building = ?, ProjectAddress = ?, " +
                            "ERF = ?, Fee = ?, Paid = ?, Deadline = ?, Status = ?, CompletDate = ? " +
                            "where ProjectNo = ?");

            PreparedStatement insertStatement2 = connection.prepareStatement(
                    "update projects set ProjectNo = ?, ProjectName = ?, Building = ?, ProjectAddress = ?, " +
                            "ERF = ?, Fee = ?, Paid = ?, Deadline = ?, Status = ?, CompletDate = ? " +
                            "where ProjectName = ?");

            if (selectNo == 1) {

                insertStatement1.setInt(1, newNumber);
                insertStatement1.setString(2, newName);
                insertStatement1.setString(3, building);
                insertStatement1.setString(4, projectAddress);
                insertStatement1.setInt(5, erf);
                insertStatement1.setFloat(6, fee);
                insertStatement1.setFloat(7, paid);
                insertStatement1.setDate(8, deadline);
                insertStatement1.setString(9, status);
                insertStatement1.setDate(10, completionDate);
                insertStatement1.setInt(11, oldNumber);

                int queryResults1 = insertStatement1.executeUpdate();
                System.out.println("Query complete, " + queryResults1 + " records updated");

            } else if (selectNo == 2) {
                insertStatement2.setInt(1, newNumber);
                insertStatement2.setString(2, newName);
                insertStatement2.setString(3, building);
                insertStatement2.setString(4, projectAddress);
                insertStatement2.setInt(5, erf);
                insertStatement2.setFloat(6, fee);
                insertStatement2.setFloat(7, paid);
                insertStatement2.setDate(8, deadline);
                insertStatement2.setString(9, status);
                insertStatement2.setDate(10, completionDate);
                insertStatement2.setString(11, oldName);

                int queryResults2 = insertStatement2.executeUpdate();
                System.out.println("Query complete, " + queryResults2 + " records updated");
            }

            //Catch block to catch Input Mismatch Exceptions and print a message to screen if there is one.
        } catch (SQLException e) {
            System.out.println("SQL Exception");
            e.printStackTrace();
        }
    }

    //createInvoiceFile method is created, which uses an invoice list arrayList.
    //A new FileWriter object is created.
    //The writer takes the invoice objects from the invoiceList arrayList, and converts them to text using the toString
    //method and writes each invoice object on a new line in the file created by the FileWriter.

    public static void createInvoiceFile (ArrayList < Invoice > invoiceList) throws IOException {
        try {
            FileWriter myWriter = new FileWriter("completedProject.txt");

            for (Invoice invoice : invoiceList) {
                myWriter.write(invoice.toString() + System.lineSeparator());
            }

            myWriter.close();

        } catch (IOException e) {
            System.out.println("An IO error occurred.");
            e.printStackTrace();
        }
    }

    //captureProject method with no parameters.
    //A scanner is constructed to capture user input.
    //The project object is declared and its variables are declared and initiated.

    public static Project captureProject () throws SQLException {

        Scanner input = new Scanner(System.in);

        Project project;

        int number = 0;
        String projectName = null;
        String building = null;
        String projectAddress = null;
        int erf = 0;
        float fee = 0;
        float paid = 0;
        LocalDate deadline = null;

        //In a try block a connection object called connection is created which connects to the database.
        //The scanner is used to read the user's input based on a message printed to screen multiple times, and the
        //input values are stored in variables.

        try {
            Connection connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/poisepms?useSSL=false", "otheruser",
                    "swordfish");

            System.out.println("Enter the project number: ");
            number = input.nextInt();

            input.nextLine();

            System.out.println("Enter the project name. If there is no name, enter \"none\": ");
            projectName = input.nextLine();

            System.out.println("Enter the type of building being designed: ");
            building = input.nextLine();

            System.out.println("Enter the physical address of the project: ");
            projectAddress = input.nextLine();

            System.out.println("Enter the ERF number of the project: ");
            erf = input.nextInt();

            input.nextLine();

            System.out.println("Enter the total fee being charged for the project: ");

            //Convert the value captured as a string object to a float variable.

            String feeString = input.nextLine();
            fee = Float.parseFloat(feeString);

            System.out.println("Enter the total amount paid to date for the project: ");

            //Convert the value captured as a string object to a float variable.

            String paidString = input.nextLine();
            paid = Float.parseFloat(paidString);

            System.out.println("Enter the deadline for the project (yyyy/MM/dd): ");

            //Try block where the deadlineString string is parsed to a SimpleDateFormat object. If this fails, the catch
            //block is run and an error message is printed to screen.

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
            String deadlineString = input.nextLine();

            //convert String to LocalDate
            deadline = LocalDate.parse(deadlineString, formatter);

            //A prepared statement object is created with variables. These variables are populated with the
            //variables gotten above.
            //The statement is then executed, which sends the query to the database and populates a new row in the table
            //with the variable values.
            //The number of records updated is printed to the screen.

            PreparedStatement insertStatement = connection.prepareStatement("insert into projects values(?,?,?,?,?,?,?,?,?,?)");

            insertStatement.setInt(1, number);
            insertStatement.setString(2, projectName);
            insertStatement.setString(3, building);
            insertStatement.setString(4, projectAddress);
            insertStatement.setInt(5, erf);
            insertStatement.setFloat(6, fee);
            insertStatement.setFloat(7, paid);
            insertStatement.setObject(8, deadline);
            insertStatement.setString(9, "not finalised");
            insertStatement.setDate(10, null);

            int i = insertStatement.executeUpdate();
            System.out.println("Query complete, " + i + " records inserted");

        } catch (InputMismatchException | SQLException e) {
            e.printStackTrace();
        }

        //An array of type Person (object) of size 3 is created, and its variables/parameters are declared.

        Person[] person = new Person[4];

        String role;
        String s_name;
        String f_name;
        String tel;
        String email;
        String address;
        Float invoiceAmount = null;

        //A for loop that iterates from person[0] to person[3] is used to ask the user for input for each person
        //using a scanner that is constructed. A new connection for the method is created in the for loop.
        //The user's inputs are stored in the previously declared variables for each person.

        for (int x = 0; x < person.length; x++) {

            Connection connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/poisepms?useSSL=false", "otheruser",
                    "swordfish");

            Scanner input1 = new Scanner(System.in);

            System.out.print("Enter the role of the person in the following order: 1. architect, 2. customer, " +
                    "3. Project Manager, 4. Structural Engineer): ");
            role = input1.nextLine();

            System.out.print("Enter the surname of the person (e.g. John Smith): ");
            s_name = input1.nextLine();

            System.out.print("Enter the first name of the person (e.g. John Smith): ");
            f_name = input1.nextLine();

            System.out.print("Enter the telephone number of the person: ");
            tel = input1.nextLine();

            System.out.print("Enter the email address of the person: ");
            email = input1.nextLine();

            System.out.print("Enter the physical address of the person: ");
            address = input1.nextLine();

            //A prepared statement object is created with variables. These variables are populated with the
            //variables mentioned above.
            //The statement is then executed, which sends the query to the database and populates a new row in the table
            //with the variable values.
            //The number of records updated is printed to the screen.
            //Each person object is then constructed using the variables as parameters.

            PreparedStatement insertStatement2 = connection.prepareStatement("insert into persons_project_1 values(?,?,?,?,?,?,?,?,?)");

            insertStatement2.setInt(1, number);
            insertStatement2.setString(2, projectName);
            insertStatement2.setString(3, role);
            insertStatement2.setString(4, s_name);
            insertStatement2.setString(5, f_name);
            insertStatement2.setString(6, tel);
            insertStatement2.setString(7, email);
            insertStatement2.setString(8, address);
            insertStatement2.setObject(9, null);

            int i = insertStatement2.executeUpdate();
            System.out.println("Query complete, " + i + " records inserted");

            Person p = new Person(role, s_name, f_name, tel, email, address, invoiceAmount);
            person[x] = p;
        }

        //If statement used to determine if the project has a name, and if it doesn't (project name is "none"),
        //it uses the type of building and the customer's surname to name the project. The split string method is used
        //to split the first name from the last name, and the building type and surname are used to name the project.

        assert projectName != null;
        if (projectName.equals("none")) {
            String customerName = person[2].getS_name();
            String[] splitName = customerName.split(" ");
            projectName = building + " " + splitName[1];
        }

        //Constructor to create a new project using the variables above as parameters. The method returns a project
        //object.

        project = new Project(number, projectName, building, projectAddress, erf, fee, paid, deadline);

        project.person = person;

        return project;
    }

    //create viewIncomplete method.
    //In a try block a connection object called connection is created which connects to the database. A statement object
    //is also created, as well as a string query. The statement is executed, which sends the query to the database and
    //returns a result of type ResultSet. The maximum number of rows of the first column is then stored in a variable
    //that will be used to loop through all the rows of the database table.

    public static void viewIncomplete () {
        try {
            Connection connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/poisepms?useSSL=false", "otheruser",
                    "swordfish");

            Statement stmt = connection.createStatement();

            String query = "SELECT MAX(ProjectNo) FROM projects";

            ResultSet result = stmt.executeQuery(query);

            int max = 0;
            Float fee = null;
            Float paid = null;

            while (result.next()) {
                max = result.getInt(1);
            }

            //A for loop is created that loops through all the rows in the table (from 1 to the max/last row). The queries
            //are stored as strings, and the strings are used in a prepared statement which is then executed. The queries
            //find the values of the specified columns for specific rows (determined by the project number), and stores
            //these results as variables that are used to determine whether the amount paid matches the total fee or not,
            //and if it doesn't, the project number of this project is used in a prepared statement to query what the
            //name of this project is. A message is printed to the screen for this project which is incomplete.

            for (int i = 1; 1 <= i & i <= max; i++) {

                String projectNumber = Integer.toString(i);
                System.out.println(projectNumber);

                String feeQuery = "select Fee from projects where ProjectNo = ?";

                PreparedStatement preparedStatement1 = connection.prepareStatement(feeQuery);
                preparedStatement1.setString(1, projectNumber);
                ResultSet results1 = preparedStatement1.executeQuery();

                while (results1.next()) {
                    fee = results1.getFloat("Fee");
                }

                String paidQuery = "select Paid from projects where ProjectNo = ?";

                PreparedStatement preparedStatement2 = connection.prepareStatement(paidQuery);
                preparedStatement2.setString(1, projectNumber);
                ResultSet results2 = preparedStatement2.executeQuery();

                while (results2.next()) {
                    paid = results2.getFloat("Paid");
                }

                try {
                    assert fee != null;
                    if (!fee.equals(paid)) {

                        String projectNameQuery = "select  ProjectName from projects where ProjectNo = ?";

                        PreparedStatement preparedStatement3 = connection.prepareStatement(projectNameQuery);
                        preparedStatement3.setString(1, projectNumber);
                        ResultSet results3 = preparedStatement3.executeQuery();

                        while (results3.next()) {
                            System.out.println((results3.getString("ProjectName")) + " still needs to be completed");
                        }
                    }
                } catch (NullPointerException e) {
                    System.out.println("The date was entered in the incorrect format.");
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            System.out.println("SQL exception");
            e.printStackTrace();
        }
    }

    //overdue method with no parameters.
    //In a try block a connection object called connection is created which connects to the database. A statement object is also created,
    //as well as a string query. The statement is executed, which sends the query to the database and returns a result
    //of type ResultSet. The maximum number of rows of the first column is then stored in a variable that will be used
    //to loop through all the rows of the database table.

    public static void overdue() {
        try {
            Connection connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/poisepms?useSSL=false", "otheruser",
                    "swordfish");

            Statement stmt = connection.createStatement();

            String query = "SELECT MAX(ProjectNo) FROM projects";

            ResultSet result = stmt.executeQuery(query);

            int max = 0;
            Date deadline = null;

            while (result.next()) {
                max = result.getInt(1);
            }

            //A for loop is created that loops through all the rows in the table (from 1 to the max/last row). Queries
            //are stored as strings, and these strings are used in prepared statements which are then executed. The first query
            //finds the value of the deadline for a specific project number and stores it in a variable.
            //This is compared to the current day to see if the project has gone past its deadline, and if it has the name
            //of the project is queried from the database and the result printed to screen.

            for (int i = 1; 1 <= i & i <= max; i++) {

                String projectNumber = Integer.toString(i);

                String deadlineQuery = "select Deadline from projects where ProjectNo = ?";

                PreparedStatement preparedStatement1 = connection.prepareStatement(deadlineQuery);
                preparedStatement1.setString(1, projectNumber);
                ResultSet results1 = preparedStatement1.executeQuery();

                while (results1.next()) {
                    deadline = results1.getDate("Deadline");
                }

                long ms = System.currentTimeMillis();
                Date currentDate = new java.sql.Date(ms);

                try {
                    assert deadline != null;
                    if (deadline.compareTo(currentDate) < 0) {

                        String projectNameQuery = "select  ProjectName from projects where ProjectNo = ?";

                        PreparedStatement preparedStatement3 = connection.prepareStatement(projectNameQuery);
                        preparedStatement3.setString(1, projectNumber);
                        ResultSet results3 = preparedStatement3.executeQuery();

                        while (results3.next()) {
                            System.out.println((results3.getString("ProjectName")) + " has a deadline of " + deadline + " and is therefore past" +
                                    " its due date.");
                        }
                    }
                } catch (SQLException e) {
                    System.out.println("SQL exception");
                    e.printStackTrace();
                }
            }
        } catch (SQLException | NullPointerException e) {
            e.printStackTrace();
        }
    }

    //select method with no parameters.
    //In a try block a scanner is constructed to capture user input and a connection object called connection
    //is created which connects to the database.

    //The scanner is used to read the user's input based on a message printed to the screen, and the
    //input value is stored as a variable.

    public static void select() {
        try {
            Scanner sc = new Scanner(System.in);

            Connection connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/poisepms?useSSL=false", "otheruser",
                    "swordfish");

            System.out.println("If you'd like to search by project number, enter \"1\". Press \"2\" if you'd like to" +
                    " search by project name: ");

            String search = sc.nextLine();

            int selectNo = parseInt(search);
            int number;
            String projectName;

            //Two prepared statement objects are also created,as well as a string query.
            //An if-else statement is is used to perform two different tasks depending on what the user entered.
            //If the user selected 1, another query is executed and the result (the values of all the columns in
            //the row with the project number that the user entered) is printed to the screen.

            //The statement is executed, which sends the query to the database and returns a result of type ResultSet.
            //The maximum number of rows of the first column is then stored in a variable that will be used
            //to loop through all the rows of the database table.

            //A similar statement with a query is executed if the user enters 2, the same results are printed
            //to the screen, except the query is done with the project's name instead of project number.

            PreparedStatement selectStatement1 = connection.prepareStatement("select Building, ProjectAddress," +
                    " ERF, Fee, Paid, Deadline, Status, CompletDate from projects where ProjectNo=?");

            PreparedStatement selectStatement2 = connection.prepareStatement("select Building, ProjectAddress," +
                    " ERF, Fee, Paid, Deadline, Status, CompletDate from projects where ProjectName=?");

            if (selectNo == 1) {

                System.out.println("Enter the project number: ");
                number = sc.nextInt();

                selectStatement1.setInt(1, number);
                ResultSet results1 = selectStatement1.executeQuery();

                while (results1.next()) {
                    System.out.println(
                            "{Project number = " + number + "} " +
                                    "{Building = " + results1.getString(1) + "} " +
                                    "{Project Address = " + results1.getString(2) + "} " +
                                    "{ERF = " + results1.getString(3) + "} " +
                                    "{Fee = " + results1.getString(4) + "} " +
                                    "{Paid = " + results1.getString(5) + "} " +
                                    "{Deadline = " + results1.getString(6) + "} " +
                                    "{Status = " + results1.getString(7) + "} " +
                                    "{Completion Date = " + results1.getString(8) + "}"
                    );
                }

            } else if (selectNo == 2) {
                System.out.println("Enter the project name: ");
                projectName = sc.nextLine();

                selectStatement2.setString(1, projectName);
                ResultSet results2 = selectStatement2.executeQuery();

                while (results2.next()) {
                    System.out.println(
                            "{Project name = " + projectName + "} " +
                                    "{Building = " + results2.getString(1) + "} " +
                                    "{Project Address = " + results2.getString(2) + "} " +
                                    "{ERF = " + results2.getString(3) + "} " +
                                    "{Fee = " + results2.getString(4) + "} " +
                                    "{Paid = " + results2.getString(5) + "} " +
                                    "{Deadline = " + results2.getString(6) + "} " +
                                    "{Status = " + results2.getString(7) + "} " +
                                    "{Completion Date = " + results2.getString(8) + "}");
                }
            }
        } catch (SQLException e) {
            System.out.println("SQL Exception.");
            e.printStackTrace();
        }
    }
}