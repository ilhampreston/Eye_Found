package myapplication.eye_found;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class DownloadPDF {
    private static final int MEGABYTE = 1024*1024;

    public static void downloadFile(String fileURL, File directory){
        try {
            URL url = new URL(fileURL);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.connect();

            InputStream inputStream = httpURLConnection.getInputStream();
            FileOutputStream fileOutputStream = new FileOutputStream(directory);
            int totalSize = httpURLConnection.getContentLength();

            byte[] buffer = new byte[MEGABYTE];
            int bufferlength = 0;
            while((bufferlength = inputStream.read(buffer))>0){
                fileOutputStream.write(buffer, 0, bufferlength);
            }
            fileOutputStream.close();

        }
        catch (FileNotFoundException e){

        }
        catch (MalformedURLException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
