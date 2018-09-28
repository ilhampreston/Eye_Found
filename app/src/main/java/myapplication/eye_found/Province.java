package myapplication.eye_found;

public class Province {
    public String idProvince;
    public String provinceName;

    public Province(){

    }

    public Province(String idProvince, String provinceName){
        this.idProvince = idProvince;
        this.provinceName = provinceName;
    }

    public String getIdProvince() {
        return idProvince;
    }

    public void setIdProvince(String idProvince) {
        this.idProvince = idProvince;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }
}
