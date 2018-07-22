package weekl.weatherdemo.bean;

public class City {
    public String location;
    public String parentCity;
    public String adminArea;

    public City(){
        super();
    }

    public City(String location, String parentCity, String adminArea){
        this.location = location;
        this.parentCity = parentCity;
        this.adminArea = adminArea;
    }
}
