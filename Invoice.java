//Create the Invoice class
public class Invoice {

    //Attributes for constructor
    private String customer;
    private String telephone;
    private String emailAdd;
    private String physAdd;
    private float paidAmount;
    private float total;

    // Constructor
    public Invoice(String customer, String telephone, String emailAdd, String physAdd, float paidAmount, float total) {
        this.customer = customer;
        this.telephone = telephone;
        this.emailAdd = emailAdd;
        this.physAdd = physAdd;
        this.paidAmount = paidAmount;
        this.total = total;
    }

    public Invoice() {
    }

    //setters and getters
    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public void setEmailAdd(String emailAdd) {
        this.emailAdd = emailAdd;
    }

    public void setPhysAdd(String physAdd) {
        this.physAdd = physAdd;
    }

    public void setTotal(float fee) {
        this.total = paidAmount - fee;
    }

    public void setPaidAmount(float paidAmount) {
        this.paidAmount = paidAmount;
    }

    public String getCustomer() {
        return customer;
    }

    public String getTelephone() {
        return telephone;
    }

    public String getEmailAdd() {
        return emailAdd;
    }

    public String getPhysAdd() {
        return physAdd;
    }

    public float getTotal() {
        return total;
    }

    public float getPaidAmount() {
        return paidAmount;
    }

    //override the toString() method
    @Override
    public String toString() {
        return "Invoice{" +
                "customer='" + customer + '\'' +
                ", telephone='" + telephone + '\'' +
                ", emailAdd='" + emailAdd + '\'' +
                ", physAdd='" + physAdd + '\'' +
                ", paidAmount=" + paidAmount +
                ", total=" + total +
                '}';
    }
}
