package myapplication.eye_found;

public class Media {
    public String idMedia;
    public String mediaName;

    public Media(){

    }
    public Media(String idMedia, String mediaName){
        this.idMedia = idMedia;
        this.mediaName = mediaName;
    }

    public String getIdMedia() {
        return idMedia;
    }

    public void setIdMedia(String idMedia) {
        this.idMedia = idMedia;
    }

    public String getMediaName() {
        return mediaName;
    }

    public void setMediaName(String mediaName) {
        this.mediaName = mediaName;
    }
}
