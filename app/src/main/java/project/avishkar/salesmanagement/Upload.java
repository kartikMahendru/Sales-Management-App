package project.avishkar.salesmanagement;

public class Upload {

    private String email, url;

    public Upload(){

    }
    public Upload(String email, String url){
        this.email = email;
        this.url = url;
    }

    public String getEmail()
    {
        return email;
    }

    public String getUrl(){
        return url;
    }
}
