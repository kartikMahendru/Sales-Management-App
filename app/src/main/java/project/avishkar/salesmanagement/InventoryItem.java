package project.avishkar.salesmanagement;

public class InventoryItem {
    private String itemName;
    private int total_available, sold, notAlloted;

    public InventoryItem(){

    }

    public InventoryItem(String itemName,int total_available,int sold)
    {
        this.itemName=itemName;
        this.total_available=total_available;
        this.sold=sold;
        this.notAlloted = (this.total_available-this.sold);
    }

    public InventoryItem(String itemName,int total_available)
    {
        this.itemName=itemName;
        this.total_available=total_available;
        this.sold=0;
        this.notAlloted = this.total_available;
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

    public int getNotAlloted() {
        return notAlloted;
    }

    public void setNotAlloted(int notAlloted) {
        this.notAlloted = notAlloted;
    }
}

