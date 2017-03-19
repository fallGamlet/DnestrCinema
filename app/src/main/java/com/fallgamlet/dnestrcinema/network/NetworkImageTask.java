package com.fallgamlet.dnestrcinema.network;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import org.json.JSONArray;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * Created by fallgamlet on 27.05.16.
 */
public class NetworkImageTask {

    //region sub Classes and Interfaces
    public interface NetworkImageCallback {
        void onImageLoaded(UrlImage urlImg);
    }

    public static class UrlImage {
        public String url;
        public Bitmap img;

        public UrlImage() {
            url = null;
            img = null;
        }

        public UrlImage(String urlStr, Bitmap bitmap) {
            url = urlStr;
            img = bitmap;
        }
    }

    public interface TaskCallback {
        void onTaskFinished(NetworkImageTask task);
    }

    public static class Task extends AsyncTask<UrlImage, UrlImage, Long> {

        private NetworkImageCallback mCallback;

        public Task(NetworkImageCallback callback) {
            mCallback = callback;
        }

        @Override
        protected Long doInBackground(UrlImage... params) {
            for (UrlImage urlImg : params) {
                urlImg.img = loadImage(urlImg.url);
                publishProgress(urlImg);
            }
            return (long) 0;
        }

        @Override
        protected void onProgressUpdate(UrlImage... params) {
            for (UrlImage urlImg : params) {
                if (urlImg == null) { return; }

                if (urlImg.url != null && urlImg.img != null) {
                    NetworkImageTask.cachedImages.put(urlImg.url, urlImg.img);
                }

                if (mCallback != null) {
                    mCallback.onImageLoaded(urlImg);
                }
            }
        }

        @Override
        protected void onPostExecute(Long res) {
        }

        /**
         * Получить картинку по указанному URL
         *
         * @param urlStr значение адреса, по каторому должна быть получена картинка
         * @return картинка
         */
        public static Bitmap loadImage(String urlStr) {
            if (urlStr == null || urlStr.isEmpty())
                return null;

            Bitmap bitmap;
            HttpURLConnection httpCon = null;
            try {
                URL url = new URL(urlStr);
                httpCon = (HttpURLConnection) url.openConnection();
                httpCon.setConnectTimeout(5000);
                httpCon.setUseCaches(true);
                httpCon.setDefaultUseCaches(true);
                InputStream stream = httpCon.getInputStream();
                bitmap = BitmapFactory.decodeStream(stream);
            } catch (Exception e) {
                Log.e("loadImage", "error:" + e.getLocalizedMessage());
                bitmap = null;
            } finally {
                if (httpCon != null) httpCon.disconnect();
            }
            //
            return bitmap;
        }

    }

    public static class ImageRequest implements NetworkImageCallback {
        //region Fields
        private String mUrl;
        private Bitmap mImage;
        private Task mTask;
        private ArrayList<NetworkImageCallback> mCallbackList;
        //endregion

        //region Constructors
        public ImageRequest(String urlStr) {
            mUrl = urlStr;
            mImage = null;
            mCallbackList = new ArrayList<>();
        }
        //endregion

        //region Methods
        public void request(@NonNull NetworkImageCallback callback) {
            if (mImage != null) {
                UrlImage urlImg = new UrlImage(mUrl, mImage);
                callback.onImageLoaded(urlImg);
                return;
            }

            addCallback(callback);
            if (mTask == null) { mTask = new Task(this); }
            AsyncTask.Status status = mTask.getStatus();
            if (status != AsyncTask.Status.RUNNING) {
                if (status == AsyncTask.Status.FINISHED) { mTask = new Task(this); }
                UrlImage urlImg = new UrlImage(mUrl, mImage);
                mTask.execute(urlImg);
            }
        }

        public boolean equalUrl(String url) {
            return (mUrl == null && url == null)
                    || (mUrl != null && mUrl.equalsIgnoreCase(url));
        }

        public String getUrl() {
            return mUrl;
        }

        public Bitmap getImage() {
            return mImage;
        }

        public void SetImage(Bitmap bitmap) {
            this.mImage = bitmap;
        }

        protected ArrayList<NetworkImageCallback> getCallbacks() {
            if (mCallbackList == null) {
                mCallbackList = new ArrayList<>();
            }
            return mCallbackList;
        }

        public boolean addCallback(@NonNull NetworkImageCallback callback) {
            ArrayList<NetworkImageCallback> callbacks = getCallbacks();
            int pos = callbacks.indexOf(callback);
            if (pos < 0) {
                callbacks.add(callback);
            }
            return true;
        }

        public boolean removeCallback(@NonNull NetworkImageCallback callback) {
            boolean result = false;
            if (mCallbackList != null) {
                result = mCallbackList.remove(callback);
                if (mCallbackList.isEmpty() && mTask != null) { mTask.cancel(true); }
            }
            return result;
        }

        public void clearCallbacks() {
            if (mCallbackList != null) {
                mCallbackList.clear();
            }
        }

        public int executeCallbacks() {
            int result = 0;
            if (mCallbackList != null) {
                for (NetworkImageCallback callback : mCallbackList) {
                    UrlImage urlImg = new UrlImage(mUrl, mImage);
                    if (callback != null) {
                        callback.onImageLoaded(urlImg);
                        result++;
                    }
                }
            }
            return result;
        }

        @Override
        public void onImageLoaded(UrlImage urlImg) {
            mImage = urlImg != null ? urlImg.img : null;
            ArrayList<NetworkImageCallback> callbacks = new ArrayList<NetworkImageCallback>(getCallbacks());
            clearCallbacks();
            for (NetworkImageCallback callback : callbacks) {
                if (callback != null) {
                    callback.onImageLoaded(urlImg);
                }
            }
        }
        //endregion
    }
    //endregion

    //region Fields
    public static final Map<String, Bitmap> cachedImages = new HashMap<>();
    protected LinkedList<ImageRequest> mImageRequests;
    //endregion

    //region Constructors
    public NetworkImageTask() {
        getRequests();
    }
    //endregion

    //region Methods
    protected LinkedList<ImageRequest> getRequests() {
        if (mImageRequests == null) { mImageRequests = new LinkedList<>(); }
        return mImageRequests;
    }

    protected ImageRequest getRequest(@NonNull String url) {
        LinkedList<ImageRequest> requests = getRequests();
        ImageRequest result = null;
        for (ImageRequest request : requests) {
            if (request.equalUrl(url)) {
                result = request;
                break;
            }
        }
        return result;
    }

    protected ImageRequest getRequest(@NonNull NetworkImageCallback callback) {
        LinkedList<ImageRequest> requests = getRequests();
        ImageRequest result = null;
        for (ImageRequest request : requests) {
            int pos = request.getCallbacks().indexOf(callback);
            if (pos >= 0) {
                result = request;
                break;
            }
        }
        return result;
    }

    public void requestImage(@NonNull String url, @NonNull NetworkImageCallback callback) {
        Bitmap img = NetworkImageTask.cachedImages.get(url);

        if (img != null) {
            callback.onImageLoaded(new UrlImage(url, img));
            return;
        }



        ImageRequest request = getRequest(url);
        if (request == null) {
            request = new ImageRequest(url);
            getRequests().add(request);
        }
        request.request(callback);
    }

    public void removeCallback(NetworkImageCallback callback) {
        if (callback == null) { return; }
        ImageRequest request = getRequest(callback);
        request.removeCallback(callback);
    }

    public void clearFinishedTasks() {
        LinkedList<ImageRequest> requests = getRequests();
        LinkedList<ImageRequest> finishedList = new LinkedList<>();
        for (ImageRequest request : requests) {
            if (request.mTask.getStatus() == AsyncTask.Status.FINISHED) {
                finishedList.add(request);
            }
        }
        if (finishedList.isEmpty()) { requests.removeAll(finishedList); }
    }

    public void dispose() {
        LinkedList<ImageRequest> requests = getRequests();
        if (requests.isEmpty()) {
            return;
        }
        for (ImageRequest request : requests) {
            request.clearCallbacks();
            request.mTask.cancel(true);
            request.mTask = null;
        }
        requests.clear();
    }

    private void writeToCatch(Context context, Bitmap bitmap, String key) {
        if (context == null || bitmap==null || key == null  || key.isEmpty()) {
            return;
        }

        FileOutputStream outStream;
        try {
            outStream = context.openFileOutput(key, Context.MODE_PRIVATE);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Bitmap readCatch(Context context, String key) {
        if (context == null || key == null || key.isEmpty()) {
            return null;
        }

        FileInputStream inStream;
        Bitmap bitmap;
        try {
            inStream = context.openFileInput(key);
            bitmap = BitmapFactory.decodeStream(inStream);
        } catch (Exception e) {
            e.printStackTrace();
            bitmap = null;
        }
        return bitmap;
    }

    private String md5(final String s) {
        final String MD5 = "MD5";
        String hash;
        try {
            // Create MD5 Hash
            MessageDigest digest = MessageDigest.getInstance(MD5);
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            hash = hexString.toString();

        } catch (Exception e) {
            e.printStackTrace();
            hash = null;
        }
        return hash;
    }
    //endregion
}
