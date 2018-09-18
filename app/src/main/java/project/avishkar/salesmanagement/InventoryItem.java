package project.avishkar.salesmanagement;

public class InventoryItem {
    private String itemName;
    private int total_available,sold;

    public InventoryItem(String itemName,int total_available,int sold)
    {
        this.itemName=itemName;
        this.total_available=total_available;
        this.sold=sold;
    }

    public InventoryItem(String itemName,int total_available)
    {
        this.itemName=itemName;
        this.total_available=total_available;
        this.sold=0;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getTotal_available() {
        return total_available;
    }

    public void setTotal_available(int total_available) {
        this.total_available = total_available;
    }

    public int getSold() {
        return sold;
    }

    public void setSold(int sold) {
        this.sold = sold;
    }
}

