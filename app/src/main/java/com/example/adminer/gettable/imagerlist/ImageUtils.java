package com.example.adminer.gettable.imagerlist;

import android.graphics.Bitmap;
import android.util.LruCache;
import android.widget.ImageView;
import android.widget.ListView;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.example.adminer.gettable.R;
import com.example.adminer.gettable.volley.VolleyManager;

/**
 * Created by Administrator on 2016/1/6.
 * 真正请求
 */
public class ImageUtils{
    public static void loadImage(final String url, final ListView listView) {
        ImageLoader imageLoader = VolleyManager.getImageLoader();
        ImageLoader.ImageListener listener = new ImageLoader.ImageListener() {
            ImageView imageView = (ImageView) listView.findViewWithTag(url);

            @Override
            public void onResponse(ImageLoader.ImageContainer imageContainer, boolean b) {
                if (imageContainer != null) {
                    ImageView imageView = (ImageView) listView.findViewWithTag(url);
                    if (imageView != null) {
                        if (imageContainer.getBitmap() != null) {
                            imageView.setImageBitmap(imageContainer.getBitmap());
                        } else {
                            imageView.setImageResource(R.drawable.empty_photo);
                        }
                    }
                }
            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                //如果出错，则说明都不显示（简单处理），最好准备一张出错图片
                imageView.setImageResource(R.drawable.empty_photo);
            }
        };
        imageLoader.get(url, listener);

    }

    /**
     * 取消图片请求
     */
    public static void cancelAllImageRequests() {
        ImageLoader imageLoader = VolleyManager.getImageLoader();
        RequestQueue requestQueue = VolleyManager.getRequestQueue();
        if (imageLoader != null && requestQueue != null) {
            int num = requestQueue.getSequenceNumber();
//            imageLoader.drain(num);
//            requestQueue.stop();
        }
    }

    private class BitmapCache implements ImageLoader.ImageCache {

        //使用安卓提供的缓存机制
        private LruCache<String , Bitmap> mCache;

        //重写构造方法
        private BitmapCache() {
            int maxSize = 10*1024*1024;  //缓存大小为10兆
            mCache = new LruCache<String ,Bitmap>(maxSize);

        }
        @Override
        public Bitmap getBitmap(String s) {
            return mCache.get(s);
        }

        @Override
        public void putBitmap(String s, Bitmap bitmap) {
            mCache.put(s,bitmap);
        }
    }
}
