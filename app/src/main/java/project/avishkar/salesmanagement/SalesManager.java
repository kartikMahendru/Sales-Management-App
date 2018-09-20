package project.avishkar.salesmanagement;

import java.util.ArrayList;

/**
 * Created by user on 9/1/18.
 */

public class SalesManager {
    private String name, number, password, email, orgName;
    private ArrayList<InventoryItem> inventoryItems;

    public SalesManager(){}
    public SalesManager(String name, String number, String password, String email, String orgName){
        this.name = name;
        this.number = number;
        this.password = password;
        this.email = email;
        this.orgName = orgName;
        this.inventoryItems  = new ArrayList<>();

    }

    public String getEmail() {
        return email;
    }

    public String getOrgName() {
        return orgName;
    }

    public String getName() {
        return name;
    }

    public String getNumber() {
        return number;
    }

    public String getPassword() {
        return password;
    }
}
