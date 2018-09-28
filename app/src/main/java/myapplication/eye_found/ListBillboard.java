package myapplication.eye_found;

import java.util.ArrayList;
import java.util.List;

public class ListBillboard {
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
    public String np1m;
    public String np3m;
    public String np6m;
    public String np12m;


    public ListBillboard(String idmediakit, String sidecode, String location, String city, String viewvalue,
                         String mediatype, String type_light,  String filepdf, String photo, String orientation, String sizewidth, String sizeheight, String np1m, String np3m, String np6m, String np12m){
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
        this.np1m = np1m;
        this.np3m = np3m;
        this.np6m = np6m;
        this.np12m = np12m;
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

    public String getType_light() {
        return type_light;
    }

    public String getFilepdf() {
        return filepdf;
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

    public String getNp1m() {
        return np1m;
    }

    public String getNp3m() {
        return np3m;
    }

    public String getNp6m() {
        return np6m;
    }

    public String getNp12m() {
        return np12m;
    }
}





