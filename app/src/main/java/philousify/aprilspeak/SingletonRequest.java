package philousify.aprilspeak;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.LruCache;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

/**
 * Created by Philip on 11/9/2016.
 */

public class SingletonRequest {
    private static SingletonRequest mInstance;
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;
    private static Context mContext;

    //Constructor
    private SingletonRequest(Context context){
        mContext = context;
        mRequestQueue = getRequestQueue();

        mImageLoader = new ImageLoader(mRequestQueue,
                new ImageLoader.ImageCache() {
                    private final LruCache<String,Bitmap> cache = new LruCache<>(20);
                    @Override
                    public Bitmap getBitmap(String url) {
                        return cache.get(url);
                    }

                    @Override
                    public void putBitmap(String url, Bitmap bitmap) {
                        cache.put(url,bitmap);
                    }
                });
    }

    //Check member Instance for instantiation
    public static synchronized SingletonRequest getInstance(Context context){
        if(mInstance == null){
            mInstance = new SingletonRequest(context);
        }
        return mInstance;
    }
    public RequestQueue getRequestQueue(){
        if(mRequestQueue == null){
            mRequestQueue = Volley.newRequestQueue(mContext.getApplicationContext());
        }
        return mRequestQueue;
    }
    //Usually String requests
    public <T> void addToRequestQueue(Request<T> req){
        getRequestQueue().add(req);

    }
    public ImageLoader getImageLoader(){
        return mImageLoader;
    }

}
