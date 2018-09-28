package myapplication.eye_found;

public class Jalan {
    public String idMediakit;
    public String namajalan;

    public Jalan(){

    }

    public Jalan(String idMediakit, String namajalan){
        this.idMediakit = idMediakit;
        this.namajalan = namajalan;
    }

    public String getIdMediakit() {
        return idMediakit;
    }

    public void setIdMediakit(String idMediakit) {
        this.idMediakit = idMediakit;
    }

    public String getNamajalan() {
        return namajalan;
    }

    public void setNamajalan(String namajalan) {
        this.namajalan = namajalan;
    }
}
