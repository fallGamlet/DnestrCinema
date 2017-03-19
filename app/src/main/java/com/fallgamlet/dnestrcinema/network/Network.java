package com.fallgamlet.dnestrcinema.network;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Base64;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;


/**
 * Created by fallgamlet on 11.01.16.
 */
public class Network {

    public static final String CONTENT_TYPE_TEXT = "text";
    public static final String CONTENT_TYPE_XML = "text/xml";
    public static final String CONTENT_TYPE_HTML = "text/html";
    public static final String CONTENT_TYPE_JSON = "application/json";
    public static final String CHARSET_UTF8 = "UTF-8";
    public static final String METHOD_GET = "GET";
    public static final String METHOD_POST = "POST";
    public static final String AUTH_TYPE_BASIC = "Basic";

    public static String getAccessToken(String login, String password) {
        String token = null;
        if (login==null) { login = ""; }
        if (password==null) { password = ""; }
        byte[] auth = (login +":"+ password).getBytes();
        return Base64.encodeToString(auth, Base64.NO_WRAP);
    }

    //region Base requests
    @NonNull
    public static RawResult requestDataSync(@NonNull String urlStr, @Nullable Options options, @Nullable byte[] data) throws IOException {
        if (options == null) { options = Options.Default(); }

        String charset = options.charset != null? options.charset: CHARSET_UTF8;
        String contentType = options.contentType != null? options.contentType: CONTENT_TYPE_TEXT;
        String method = options.method != null? options.method: METHOD_GET;
        String authStr = options.authorization;
        int dataLength = data==null? 0: data.length;
        int timeout = options.timeout;

        RawResult resultData = new RawResult();
        URL url = new URL(urlStr);
        HttpURLConnection httpCon;
        httpCon = (HttpURLConnection)url.openConnection();
        httpCon.setRequestProperty("Content-type", contentType);
        httpCon.setRequestProperty("Accept-Charset", charset);
        httpCon.setRequestMethod(method);
        httpCon.setDoInput(true);
        httpCon.setUseCaches (false);
        httpCon.setConnectTimeout(timeout);
        //httpCon.setRequestProperty("Accept-Encoding", "identity");

        if (authStr != null) {
            httpCon.setRequestProperty("Authorization", authStr);
        }

        if (METHOD_POST.equals(method)) {
            httpCon.setRequestProperty("Content-Length", String.valueOf(dataLength));
            httpCon.setDoOutput(true);
            if (data == null) { data = new byte[]{}; }
            OutputStream outputStream = httpCon.getOutputStream();
            BufferedOutputStream output = new BufferedOutputStream(outputStream);
            output.write(data);
            output.flush();
            output.close();
        }

        int respCode = httpCon.getResponseCode();
        resultData.status = respCode;
        InputStream inStream;

        if (respCode == HttpURLConnection.HTTP_OK) {
            inStream = httpCon.getInputStream();
        } else {
            inStream = httpCon.getErrorStream();
        }

        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[8*1024];
        int len;
        while ((len = inStream.read(buffer)) != -1) {
            byteStream.write(buffer, 0 ,len);
        }

        if (respCode == HttpURLConnection.HTTP_OK) {
            resultData.data = byteStream.toByteArray();
        } else {
            resultData.error = byteStream.toByteArray();
        }

        httpCon.disconnect();
        return resultData;
    }

    @NonNull
    public static StrResult requestDataSync(@NonNull String urlStr, @Nullable Options options, @Nullable String data) throws IOException {
        if (options == null) { options = Options.Default(); }
        String charset = options.charset!=null? options.charset: CHARSET_UTF8;
        byte[] dataBytes = data != null? data.getBytes(charset): null;
        RawResult rawResult = requestDataSync(urlStr, options, dataBytes);

        StrResult resultData = new StrResult();
        resultData.status = rawResult.status;
        if (rawResult.data != null) { resultData.data = new String(rawResult.data, charset); }
        if (rawResult.error != null) { resultData.error = new String(rawResult.error, charset); }
        return resultData;
    }

    @NonNull
    public static AsyncTask requestDataAsync(@NonNull RequestData requestData, final ResponseHandle handler) {
        AsyncTask<RequestData, Void, StrResult> asyncTask = new AsyncTask<RequestData, Void, StrResult>() {
            Exception exception;

            @Override
            protected StrResult doInBackground(RequestData... params) {
                if (params == null || params.length == 0) {
                    return null;
                }

                RequestData reqData = params[0];
                StrResult result = null;
                try {
                    result = requestDataSync(reqData.url, reqData.options, reqData.data);
                    exception = null;
                } catch (Exception e) {
                    exception = e;
                }
                return result;
            }

            @Override
            protected void onPostExecute(StrResult result) {
                if (handler != null) {
                    handler.finished(exception, result);
                }
            }
        };
        return asyncTask.execute(requestData);
    }
    //endregion

    //region Subclasses and interfaces
    public static class Options {
        public String charset;
        public String contentType;
        public String method;
        public String authorization;
        public int timeout;

        public Options() {
            clear();
        }

        public void clear() {
            charset = null;
            contentType = null;
            method = null;
            authorization = null;
            timeout = 0;
        }

        public static Options Default() {
            Options options = new Options();
            options.charset = CHARSET_UTF8;
            options.contentType = CONTENT_TYPE_TEXT;
            options.method = METHOD_GET;
            options.authorization = null;
            options.timeout = 5000;
            return options;
        }
    }

    public static class RequestData {
        public String url;
        public String data;
        public Options options;
    }

    public static class RawResult {
        public int status;
        public byte[] data;
        public byte[] error;
    }

    public static class StrResult {
        public int status;
        public String data;
        public String error;
    }

    public interface ResponseHandle {
        void finished(Exception exception, StrResult result);
    }

    // Class for Xml request
    public static class XmlRequest {
        public static String replaceLtAndRt(String src) {
            if (src == null) return null;
            return src.replace("&lt;", "<").replace("&gt;", ">");
        }
    }

    //endregion
}
