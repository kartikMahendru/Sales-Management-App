package project.avishkar.salesmanagement.Graph;

public class GraphObject {

    private String date,name,profit;

    public GraphObject(){

    }

    public GraphObject(String name,String profit,String date)
    {
        this.date=date;
        this.name=name;
        this.profit=profit;
    }

    public String getDate() {
        return date;
    }

    public String getName() {
        return name;
    }

    public String getProfit() {
        return profit;
    }
}
