package project.avishkar.salesmanagement;

public class InventoryItem {
    private String itemName;
    private int total_available, sold, profit;

    public InventoryItem(){

    }

    public InventoryItem(String itemName, int total_available, int sold, int profit)
    {
        this.itemName=itemName;
        this.total_available=total_available;
        this.sold=sold;
        this.profit =profit;
    }


    public InventoryItem(String itemName,int total_available,int sold)
    {
        this.itemName=itemName;
        this.total_available=total_available;
        this.sold=sold;
        this.profit = (this.total_available-this.sold);
    }

    public String getItemName() { return itemName; }

    public int getTotal_available() {
        return total_available;
    }

    public int getSold() {
        return sold;
    }

    public int getProfit() {
        return profit;
    }
}

