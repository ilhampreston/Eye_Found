package myapplication.eye_found.app;

import android.app.Application;
import android.text.TextUtils;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by Moe on 3/22/2018.
 */

public class AppController extends Application{

    public static final String TAG = AppController.class.getSimpleName();
    private RequestQueue requestQueue;
    private static AppController Instance;

    @Override
    public void onCreate() {
        super.onCreate();
        Instance = this;
    }
    public static synchronized AppController getInstance(){
        return Instance;
    }
    public RequestQueue getRequestQueue(){
        if (requestQueue == null){
            requestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        return requestQueue;
    }
    public <T> void  addToRequestQueue(Request<T> request, String tag){
        request.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        request.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        getRequestQueue().add(request);
    }
    public <T> void  addToRequestQueue(Request <T> request){
        request.setTag(TAG);
        request.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        getRequestQueue().add(request);
    }
    public void cancelPendingRequest (Object tag){
        if (requestQueue != null){
            requestQueue.cancelAll(tag);
        }
    }


}
