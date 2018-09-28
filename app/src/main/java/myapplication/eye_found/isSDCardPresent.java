package myapplication.eye_found;

import android.os.Environment;

public class isSDCardPresent {
    public boolean checkForSDCard(){
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            return true;
        }
        return false;
    }
}
