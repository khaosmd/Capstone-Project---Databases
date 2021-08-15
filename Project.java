//imports the LocalDate class
import java.time.LocalDate;

//Create the Project class
public class Project {

    //Attributes of the constructor
    private int number;
    private String projectName;
    private String building;
    private String projectAddress;
    private int erf;
    private float fee;
    private float paid;
    private LocalDate deadline;
    private String status;
    private LocalDate completionDate;

    //Create an array of type Person (object) with a size of 3
    Person[] person = new Person[3];

    // Constructor
    public Project(int number, String projectName, String building, String projectAddress, int erf, float fee, float paid,
                   LocalDate deadline) {

        //declare and initialise object variables
        this.number = number;
        this.projectName = projectName;
        this.building = building;
        this.projectAddress = projectAddress;
        this.erf = erf;
        this.fee = fee;
        this.paid = paid;
        this.deadline = deadline;
    }

    //toString method
    public String toString() {
        String output = "Number: " + number;
        output += "\nName:" + projectName;
        output += "\nBuilding:" + building;
        output += "\nAddress:" + projectAddress;
        output += "\nERF:" + erf;
        output += "\nFee:" + fee;
        output += "\nPaid:" + paid;
        output += "\nDeadline:" + deadline.toString();

        //for loop to iterate through all the person objects and call the toString for each person
        for (int x = 0; x < person.length; x++) {
            output += person[x].toString();
        }
        return output;
    }

    //setters and getters for variables
    public void setDeadline(LocalDate date) {
        deadline = date;
    }

    public void setPaid(float paid) {
        this.paid = paid;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public void setProjectAddress(String projectAddress) {
        this.projectAddress = projectAddress;
    }

    public void setErf(int erf) {
        this.erf = erf;
    }

    public void setFee(float fee) {
        this.fee = fee;
    }

    public void setPerson(Person[] person) {
        this.person = person;
    }

    public int getNumber() {
        return number;
    }

    public String getProjectName() {
        return projectName;
    }

    public String getBuilding() {
        return building;
    }

    public String getProjectAddress() {
        return projectAddress;
    }

    public int getErf() {
        return erf;
    }

    public float getFee() {
        return fee;
    }

    public float getPaid() {
        return paid;
    }

    public LocalDate getDeadline() {
        return deadline;
    }

    public Person[] getPerson() {
        return person;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDate getCompletionDate() {
        return completionDate;
    }

    public void setCompletionDate(LocalDate completionDate) {
        this.completionDate = completionDate;
    }
}


