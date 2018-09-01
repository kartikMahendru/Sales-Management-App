package project.avishkar.salesmanagement;

/**
 * Created by user on 9/1/18.
 */

public class SalesManager {
    String name, number, email, password;
    SalesManager(String name, String email, String number, String password){
        this.email = email;
        this.name = name;
        this.number = number;
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

    public String getPassword() {
        return password;
    }
}
