package myapplication.eye_found;

public class City {
    public String idCity;
    public String cityName;

    public City(){

    }

    public City(String idCity, String cityName){
        this.idCity = idCity;
        this.cityName = cityName;
    }

    public String getIdCity() {
        return idCity;
    }

    public void setIdCity(String idCity) {
        this.idCity = idCity;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }
}
