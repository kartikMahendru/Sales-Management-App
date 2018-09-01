package project.avishkar.salesmanagement;

/**
 * Created by user on 9/1/18.
 */

public class SalesPerson {
    String name, number, email, invite_code, password;

    public SalesPerson(String name, String email, String number, String password) {
        this.name = name;
        this.number = number;
        this.email = email;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public String getNumber() {
        return number;
    }

    public String getEmail() {
        return email;
    }

    public String getInvite_code() {
        return invite_code;
    }

    public String getPassword() {
        return password;
    }
}
