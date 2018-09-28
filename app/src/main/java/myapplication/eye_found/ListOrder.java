package myapplication.eye_found;

public class ListOrder {
    public String idmediakit;
    public String sidecode;
    public String location;
    public String city;
    public String viewvalue;
    public String mediatype;
    public String filepdf;
    public String type_light;
    public String photo;
    public String orientation;
    public String sizewidth;
    public String sizeheight;
    public String idpesan;

    public ListOrder(String idmediakit, String sidecode, String location, String city, String viewvalue,
                     String mediatype, String type_light,  String filepdf, String photo, String orientation, String sizewidth, String sizeheight, String idpesan){
        this.idmediakit = idmediakit;
        this.sidecode = sidecode;
        this.location = location;
        this.city = city;
        this.viewvalue = viewvalue;
        this.mediatype = mediatype;
        this.type_light = type_light;
        this.filepdf = filepdf;
        this.photo = photo;
        this.orientation = orientation;
        this.sizewidth = sizewidth;
        this.sizeheight = sizeheight;
        this.idpesan = idpesan;
    }

    public String getIdmediakit() {
        return idmediakit;
    }

    public String getSidecode() {
        return sidecode;
    }

    public String getLocation() {
        return location;
    }

    public String getCity() {
        return city;
    }

    public String getViewvalue() {
        return viewvalue;
    }

    public String getMediatype() {
        return mediatype;
    }

    public String getFilepdf() {
        return filepdf;
    }

    public String getType_light() {
        return type_light;
    }

    public String getPhoto() {
        return photo;
    }

    public String getOrientation() {
        return orientation;
    }

    public String getSizewidth() {
        return sizewidth;
    }

    public String getSizeheight() {
        return sizeheight;
    }

    public String getIdpesan() {
        return idpesan;
    }
}
