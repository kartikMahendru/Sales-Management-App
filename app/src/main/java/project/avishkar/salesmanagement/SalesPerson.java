package project.avishkar.salesmanagement;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by user on 9/1/18.
 */

public class SalesPerson {
    private String name, number, managerName, password, emailId;
    private ArrayList<InventoryItem> inventoryItems;

    public SalesPerson(){}
    public SalesPerson(String name, String number, String password, String managerName, String Email) {
        this.name = name;
        this.number = number;
        this.password = password;
        this.managerName = managerName;
        this.emailId = Email;

        this.inventoryItems = new ArrayList<>();

    }

    public String getEmailId() {
        return emailId;
    }
    public String getName() {
        return name;
    }

    public String getNumber() {
        return number;
    }

    public String getManagerName() {
        return managerName;
    }

    public ArrayList<InventoryItem> getInventoryItems() {
        return inventoryItems;
    }

    public String getPassword() {
        return password;
    }


}
