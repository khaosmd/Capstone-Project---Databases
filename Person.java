//imports the scanner class to accept user input
//Create the Person class
public class Person {

    //Attributes for constructor
    private String role;
    private String s_name;
    private String f_name;
    private String tel;
    private String email;
    private String address;
    private Float invoiceAmount;

    // Constructor
    public Person(String role, String s_name, String f_name, String tel, String email, String address, Float invoiceAmount) {
        this.role = role;
        this.s_name = s_name;
        this.f_name = f_name;
        this.tel = tel;
        this.email = email;
        this.address = address;
        this.invoiceAmount = invoiceAmount;
    }

    //toString method

    public String toString() {
            String output = "\nRole: " + role;
        output += "\nSurname:" + s_name;
            output += "\nFirst Name:" + f_name;
            output += "\nTelephone number:" + tel;
            output += "\nEmail Address:" + email;
            output += "\nAddress:" + address;

            return output;
    }

    //setters and getters
    public void setRole(String role) {
        this.role = role;
    }

    public void setS_name(String s_name) {
        this.s_name = s_name;
    }

    public void setF_name(String f_name) {
        this.f_name = f_name;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setInvoiceAmount(Float invoiceAmount) {
        this.invoiceAmount = invoiceAmount;
    }

    public String getRole() {
        return role;
    }

    public String getS_name() {
        return s_name;
    }

    public String getF_name() {
        return f_name;
    }

    public String getTel() {
        return tel;
    }

    public String getEmail() {
        return email;
    }

    public String getAddress() {
        return address;
    }

    public Float invoiceAmount() {
        return invoiceAmount;
    }
}



