package com.zhangwx.z_utils.Z_Widget.WebImageView.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.X509TrustManager;

public class HttpClient {

    private static final String TAG = "HttpClient";

    private static final int BLOCK_SIZE = 16 * 1024;

    public static final int TIMEOUT_CONNECT = 15 * 1000;
    public static final int TIMEOUT_READ = 60 * 1000;

    private static final String MULTIPART_BOUNDARY = "**BOUNDARY**FDBF587B**";
    private static final String CRLF = "\r\n";
    private static final String TWO_HYPHENS = "--";
    private static final String MULTIPART_END = TWO_HYPHENS + MULTIPART_BOUNDARY + TWO_HYPHENS + CRLF;

    public interface Cancelled {
        public abstract boolean isCancelled();
    }

    public interface Callback extends HttpClient.Cancelled {
        public abstract void onProgress(long progress, long total);
    }

    public static class StreamInfo {
        public long contentLength;
        public long lastModified; // in milliseconds
        public String mimeType;
        public String charset;
        public InputStream inputStream;
    }

    public static void init() {
        // Work around pre-Froyo bugs in HTTP connection reuse.
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.FROYO) {
            System.setProperty("http.keepAlive", "false");
        }
    }

//    public static String buildUserAgent(Context context, int versionCode) {
//        if (versionCode == 0) try {
//            versionCode = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
//        } catch (Throwable e) {
//        }
//        return String.format("QuickPic/%d/%s (Android %s; %s; %s; Build/%s)",
//                versionCode, RotApi.rot13(Utilities.getDeviceId(context)),
//                Build.VERSION.RELEASE, context.getResources().getConfiguration().locale.toString(), Build.MODEL, Build.ID);
//    }

    public static void closeSilently(Closeable close) {
        if (close == null) {
            return;
        }
        try {
            close.close();
        } catch (Exception e) {
            // do nothing
        }
    }

    public static void disconnectSilently(HttpURLConnection connection) {
        if (connection != null) try {
            connection.disconnect();
        } catch (Throwable e) {
            Log.e(TAG, "disconnect: ", e);
        }
    }

    public static int getNetworkType(Context context) {
        try {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            return activeNetwork.getType();
        } catch (Throwable e) {
            return -1/*ConnectivityManager.TYPE_NONE*/;
        }
    }

    public static void throwIfCancelled(Cancelled cancelled) throws CancelledException {
        if (cancelled != null && cancelled.isCancelled())
            throw new CancelledException();
    }

    /**
     * Create a connection.
     */
    public static HttpURLConnection makeConnection(String url, String method, Map<String, String> heads) throws IOException {
        final HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setConnectTimeout(TIMEOUT_CONNECT);
        connection.setDoInput(true);
        connection.setReadTimeout(TIMEOUT_READ);

        setConnectionRequestMethod(connection, method);

        if (heads != null) {
            for (Map.Entry<String, String> entry : heads.entrySet())
                connection.setRequestProperty(entry.getKey(), entry.getValue());
        }
        return connection;
    }

    public static HttpURLConnection makeConnection(String url, String method, Map<String, String> heads, int connectTimeout, int readTimeout) throws IOException {
        final HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setConnectTimeout(connectTimeout);
        connection.setDoInput(true);
        connection.setReadTimeout(readTimeout);

        setConnectionRequestMethod(connection, method);

        if (heads != null) {
            for (Map.Entry<String, String> entry : heads.entrySet())
                connection.setRequestProperty(entry.getKey(), entry.getValue());
        }
        return connection;
    }

    public static HttpURLConnection makeConnection(String url, String method) throws IOException {
        final HttpURLConnection connection = makeConnection(url, method, null);
        connection.setRequestProperty("Accept-Encoding", "gzip, identity");
        return connection;
    }

    public static void setConnectionRequestMethod(HttpURLConnection connection, String method) {
        // set unsupported method like: PROPFIND or MOVE
        try {
            connection.setRequestMethod(method);
        } catch (ProtocolException pe) {
            Class<?> cls = connection.getClass();
            try {
                // for Android 3.0+
                final Field field = cls.getDeclaredField("delegate");
                field.setAccessible(true);
                setConnectionRequestMethod((HttpURLConnection) field.get(connection), method);
            } catch (NoSuchFieldException e) {
                try {
                    // for Android 2.x
                    final Field field = cls.getDeclaredField("httpsEngine");
                    field.setAccessible(true);
                    setConnectionRequestMethod((HttpURLConnection) field.get(connection), method);
                } catch (Exception e1) {
                }
            } catch (IllegalArgumentException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
            while (cls != null) {
                try {
                    final Field field = cls.getDeclaredField("method");
                    field.setAccessible(true);
                    field.set(connection, method);
                    break;
                } catch (NoSuchFieldException e) {
                    cls = cls.getSuperclass();
                    continue;
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public static InputStream wrapInputStream(InputStream inStream, String encoding) throws Exception {
        if (inStream == null)
            return inStream;
        else if ("gzip".equals(encoding))
            return new GZIPInputStream(inStream, BLOCK_SIZE);
        else
            return inStream;
    }

    public static InputStream wrapInputStream(HttpURLConnection connection) throws Exception {
        return wrapInputStream(connection.getInputStream(), connection.getContentEncoding());
    }

    public static long copyStream(InputStream is, OutputStream os, long length, Cancelled cancelled) throws Exception {
        return copyStream(is, os, 0, length, BLOCK_SIZE, cancelled);
    }

    public static long copyStream(InputStream is, OutputStream os, long progress, long length, int blockSize, Cancelled cancelled) throws Exception {
        if (length <= 0)
            length = is.available();

        final Callback callback = (cancelled instanceof Callback && length > 0) ? (Callback) cancelled : null;
        final byte[] data = new byte[blockSize];
        int bytes = 0;
        while ((bytes = is.read(data)) >= 0) {
            if (bytes > 0) {
                os.write(data, 0, bytes);
                progress += bytes;
                if (callback != null)
                    callback.onProgress(progress, length);
            }
            if (progress >= length && length > 0)
                break;
            else
                throwIfCancelled(cancelled);
        }
        if (length > 0 && progress != length)
            Log.w(TAG, "copy stream error: " + progress + "/" + length);
        return progress;
    }

    public static String readStream(InputStream is, String charset, Cancelled cancelled) throws Exception {
        return readStream(is, charset, -1, cancelled);
    }

    public static String readStream(InputStream is, String charset, long length, Cancelled cancelled) throws Exception {
        if (is == null)
            return "";

        ByteArrayOutputStream bos = null;
        try {
            bos = new ByteArrayOutputStream(1024 * 256);
            copyStream(is, bos, length, cancelled);
            return new String(bos.toByteArray(), charset);
        } finally {
            HttpClient.closeSilently(bos);
        }
    }

    /**
     * Download a url content directly, cancelled can be null.
     */
    public static String downloadContent(HttpURLConnection connection, Cancelled cancelled) throws Exception {
        InputStream is = null;
        try {
            is = wrapInputStream(connection);
            throwIfCancelled(cancelled);

            final String charset = parseCharset(connection.getContentType());
            return readStream(is, charset, connection.getContentLength(), cancelled);
        } finally {
            closeSilently(is);
        }
    }

    /**
     * Download a url content directly, cancelled can be null.
     */
    public static String downloadErrorContent(HttpURLConnection connection, Cancelled cancelled) throws Exception {
        InputStream is = null;
        try {
            is = wrapInputStream(connection.getErrorStream(), connection.getContentEncoding());
            throwIfCancelled(cancelled);

            final String charset = parseCharset(connection.getContentType());
            return readStream(is, charset, connection.getContentLength(), cancelled);
        } finally {
            closeSilently(is);
        }
    }

    /**
     * Download a url content directly, cancelled can be null.
     */
    public static String downloadContent(String url, Map<String, String> heads, Cancelled cancelled) throws Exception {
        if (heads == null)
            heads = new HashMap<String, String>(1);
        if (!heads.containsKey("Accept-Encoding"))
            heads.put("Accept-Encoding", "gzip, identity"); // prefer gzip when download text

        String content = null;
        HttpURLConnection connection = null;
        try {
            connection = makeConnection(url, "GET", heads);
            content = downloadContent(connection, cancelled);
        } finally {
            disconnectSilently(connection);
        }
        return content;
    }

    public static String downloadContent(String url, Cancelled cancelled) throws Exception {
        return downloadContent(url, null, cancelled);
    }

    /**
     * Download a file directly, cancelled can be null.
     */
    public static void downloadFile(File file, HttpURLConnection connection, Cancelled cancelled) throws Exception {
        InputStream is = null;
        FileOutputStream fos = null;

        try {
            is = wrapInputStream(connection);
            throwIfCancelled(cancelled);

            fos = new FileOutputStream(file);
            copyStream(is, fos, connection.getContentLength(), cancelled);
        } finally {
            closeSilently(fos);
            closeSilently(is);
        }
    }

    /**
     * Download a file directly, cancelled can be null.
     */

    public static void downloadFile(final File file, String url, Map<String, String> heads, final Cancelled cancelled) throws Exception {
        if (heads == null)
            heads = new HashMap<String, String>(1);
        if (!heads.containsKey("Accept-Encoding"))
            heads.put("Accept-Encoding", "identity"); // prefer identity when download file

        final long position = file.length();
        if (position > 0)
            heads.put("range", "bytes=" + position + "-");

        doHttp(url, "GET", heads, new HttpHandler(cancelled, false) {
            @Override
            public void onDownload(int statusCode, String mimeType, String charset, long length, InputStream is) throws Exception {
                final FileOutputStream fos = new FileOutputStream(file, position > 0);
                try {
                    HttpClient.copyStream(is, fos, position, position + length, BLOCK_SIZE, cancelled);
                } finally {
                    HttpClient.closeSilently(fos);
                }
            }
        });
    }

    public static void downloadFile(File file, String url, Cancelled cancelled) throws Exception {
        downloadFile(file, url, null, cancelled);
    }

    /**
     * Returns the charset specified in the Content-Type of this header,
     * or the HTTP default (ISO-8859-1) if none can be found.
     */
    public static String parseCharset(String contentType) {
        if (contentType != null) {
            String[] params = contentType.split(";");
            for (int i = 1; i < params.length; i++) {
                String[] pair = params[i].trim().split("=");
                if (pair.length == 2 && pair[0].equals("charset")) {
                    return pair[1];
                }
            }
        }
        return "UTF_8";//HTTP.DEFAULT_CONTENT_CHARSET;
    }

    /**
     * Return the mimetype from Content-Type.
     */
    public static String parseMimetype(String contentType) {
        String parseMimetype = "";
        if (contentType != null) {
            final int paramPos = contentType.indexOf(";");
            parseMimetype = paramPos > 0 ? contentType.substring(0, paramPos) : contentType;
            parseMimetype = parseMimetype.toLowerCase().trim();
        }
        return (parseMimetype.length() == 0) ? "*/*" : parseMimetype;
    }

    public static void setContentLength(HttpURLConnection conn, long length) {
        // set the length of internal delegate
        final Class<?> cls = conn.getClass();
        try {
            // for Android 3.0+
            final Field field = cls.getDeclaredField("delegate");
            field.setAccessible(true);
            setContentLength((HttpURLConnection) field.get(conn), length);
        } catch (NoSuchFieldException e) {
            try {
                // for Android 2.x
                final Field field = cls.getDeclaredField("httpsEngine");
                field.setAccessible(true);
                setContentLength((HttpURLConnection) field.get(conn), length);
            } catch (Exception e1) {
            }
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        conn.setRequestProperty("Content-Length", Long.toString(length));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
            conn.setFixedLengthStreamingMode(length);
        else if (length <= Integer.MAX_VALUE)
            conn.setFixedLengthStreamingMode((int) length);
    }

    public static void uploadChunkStream(HttpURLConnection conn, String mimeType, int chunkSize, long progress, long contentLength, InputStream inStream, Cancelled cancelled) throws Exception {
        conn.setDoOutput(true);
        conn.setUseCaches(false);
        conn.setRequestProperty("Content-Type", mimeType);
        setContentLength(conn, chunkSize > inStream.available() ? inStream.available() : chunkSize);

        conn.connect();

        final OutputStream os = conn.getOutputStream();

        final Callback callback = (cancelled instanceof Callback && contentLength > 0) ? (Callback) cancelled : null;
        final byte[] data = new byte[BLOCK_SIZE];
        final int count = chunkSize / BLOCK_SIZE;
        int bytes = 0, i = 0;
        while (i < count && (bytes = inStream.read(data)) >= 0) {
            i++;
            if (bytes > 0) {
                os.write(data, 0, bytes);
                progress += bytes;
                if (callback != null)
                    callback.onProgress(progress, contentLength);
            }
            throwIfCancelled(cancelled);
        }

        os.flush();
        os.close();

//		if (progress < contentLength)
//			throw new RuntimeException("upload wrong bytes: " + progress + "/" + contentLength);
//		else if (progress != contentLength)
//			Log.w(TAG, "upload wrong bytes: " + progress + "/" + contentLength);
    }

    public static void uploadStream(HttpURLConnection conn, String mimeType, InputStream inStream, Cancelled cancelled) throws Exception {
        conn.setDoOutput(true);
        conn.setUseCaches(false);

        if (!TextUtils.isEmpty(mimeType)) {
            conn.setRequestProperty("Content-Type", mimeType);
        }

        final int contentLength = inStream.available();
        setContentLength(conn, contentLength);

        conn.connect();

        final OutputStream os = conn.getOutputStream();
        final long progress = copyStream(inStream, os, contentLength, cancelled);
        os.flush();
        os.close();

        if (progress < contentLength)
            throw new RuntimeException("upload wrong bytes: " + progress + "/" + contentLength);
        else if (progress != contentLength)
            Log.w(TAG, "upload wrong bytes: " + progress + "/" + contentLength);
    }

    public static void uploadString(HttpURLConnection conn, String mimeType, String content) throws Exception {
        byte[] data = content.getBytes("UTF-8");
        conn.setDoOutput(true);
        conn.setUseCaches(false);
        conn.setRequestProperty("Content-Type", mimeType);
        conn.setRequestProperty("Content-Length", String.valueOf(data.length));
        setContentLength(conn, data.length);

        final OutputStream os = conn.getOutputStream();
        os.write(data);
        os.flush();
        os.close();
    }

    /**
     * multi part upload support
     */
    public static class MultiPartsParam {
        public final long contentLength;
        public final Object content;
        public final byte[] headers;

        public MultiPartsParam(Object source, String... heads) throws Exception {
            if (source instanceof ByteBuffer) {
                content = source;
                contentLength = ((ByteBuffer) content).remaining();
            } else if (source instanceof InputStream) {
                content = source;
                contentLength = ((InputStream) content).available();
            } else {
                content = source.toString().getBytes("UTF-8");
                contentLength = ((byte[]) content).length;
            }

            final StringBuilder sb = new StringBuilder();
            sb.append(TWO_HYPHENS).append(MULTIPART_BOUNDARY).append(CRLF);
            for (int i = 0; i < heads.length; i = i + 2)
                sb.append(heads[i]).append(": ").append(heads[i + 1]).append(CRLF);
            sb.append(CRLF);
            headers = sb.toString().getBytes("UTF-8");
        }
    }

    public static void attachMultiParts(HttpURLConnection conn, String type, Cancelled cancelled, List<MultiPartsParam> params) throws Exception {
        final MultiPartsParam[] a = new MultiPartsParam[params.size()];
        attachMultiParts(conn, type, cancelled, params.toArray(a));
    }

    public static void attachMultiParts(HttpURLConnection conn, String type, Cancelled cancelled, MultiPartsParam... params) throws Exception {
        if (params == null || params.length == 0)
            throw new IllegalArgumentException();

        long contentLength = 0;
        for (MultiPartsParam param : params)
            contentLength += param.headers.length + param.contentLength + CRLF.length();
        contentLength = contentLength + MULTIPART_END.length();

        conn.setDoOutput(true);
        conn.setUseCaches(false);
        conn.setRequestProperty("Accept-Encoding", "identity"); // disable gzip since most data is media
        conn.setRequestProperty("Content-Type", "multipart/" + type + ";boundary=" + MULTIPART_BOUNDARY);
        setContentLength(conn, contentLength);

        conn.connect();

        final Callback callback = (cancelled instanceof Callback) ? (Callback) cancelled : null;
        final OutputStream os = conn.getOutputStream();
        long progress = 0;

        for (MultiPartsParam param : params) {
            os.write(param.headers);
            progress += param.headers.length;

            if (callback != null)
                callback.onProgress(progress, contentLength);
            throwIfCancelled(cancelled);

            // upload content
            final long savedProgress = progress;
            final Object content = param.content;
            if (content instanceof ByteBuffer) {
                // content as byte buffer
                final ByteBuffer buf = (ByteBuffer) content;
                final byte[] data = new byte[BLOCK_SIZE];
                final long size = buf.remaining();
                long blocks = size / BLOCK_SIZE;
                int padding = (int) (size - blocks * BLOCK_SIZE);
                for (long i = 0; i < blocks; i++) {
                    buf.get(data);
                    os.write(data);
                    progress += data.length;
                    if (callback != null)
                        callback.onProgress(progress, contentLength);
                    throwIfCancelled(cancelled);
                }
                if (padding > 0) {
                    buf.get(data, 0, padding);
                    os.write(data, 0, padding);
                    progress += padding;
                }
            } else if (content instanceof InputStream) {
                // content as input stream
                final InputStream is = (InputStream) content;
                final byte[] data = new byte[BLOCK_SIZE];
                int bytes = 0;
                while ((bytes = is.read(data)) >= 0) {
                    if (bytes > 0) {
                        os.write(data, 0, bytes);
                        progress += bytes;
                        if (callback != null)
                            callback.onProgress(progress, contentLength);
                    }
                    throwIfCancelled(cancelled);
                }
            } else {
                // content as byte array
                final byte[] data = (byte[]) content;
                os.write(data);
                progress += data.length;
            }
            if (progress - savedProgress < param.contentLength)
                throw new RuntimeException("upload wrong bytes: " + (progress - savedProgress) + "/" + param.contentLength);

            // end content wrapper
            os.write(CRLF.getBytes());
            progress += CRLF.length();
        }
        os.write(MULTIPART_END.getBytes());
        progress += MULTIPART_END.length();

        os.flush();
        os.close();

        if (callback != null)
            callback.onProgress(progress, contentLength);

        if (progress != contentLength)
            Log.w(TAG, "upload wrong bytes: " + progress + "/" + contentLength);
    }

    /*
     * Generic http handler.
     */
    public static class HttpHandler {

        protected final HttpClient.Cancelled mCancelled;
        protected final boolean mPreferGzip;

        public HttpHandler(HttpClient.Cancelled cancelled) {
            this(cancelled, true);
        }

        public HttpHandler(HttpClient.Cancelled cancelled, boolean preferGzip) {
            mCancelled = cancelled;
            mPreferGzip = preferGzip;
        }

        protected void throwIfCancelled() throws CancelledException {
            HttpClient.throwIfCancelled(mCancelled);
        }

        public void onCheckSSL(HttpsURLConnection conn) throws Exception {
        }

        public void onUpload(HttpURLConnection conn) throws Exception {
            Log.d(TAG, "default upload");
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
                // zero must be set in Android 2.3, can Override it
                conn.setRequestProperty("Content-Length", "0");
            }
        }

        public boolean onRedirect(HttpURLConnection conn, String url) throws Exception {
            return true;
        }

        public void onResponse(HttpURLConnection conn, int statusCode) throws Exception {
            if (statusCode >= 200 && statusCode < 300) {
                final String contentType = conn.getContentType();
                final InputStream is = wrapInputStream(conn);
                onDownload(statusCode, parseMimetype(contentType), parseCharset(contentType), conn.getContentLength(), is);
            } else if (statusCode >= 400 && statusCode < 500) {
                final String contentType = conn.getContentType();
                final InputStream is = wrapInputStream(conn.getErrorStream(), conn.getContentEncoding());
                on4xxError(statusCode, parseCharset(contentType), is);
            } else {
                Log.e(TAG, "onResponse throw UnknownHostException!");
                throw new UnknownHostException(Integer.toString(statusCode) + " " + conn.getResponseMessage()); // 5xx server error
            }
        }

        public void onDownload(int statusCode, String mimeType, String charset, long length, InputStream is) throws Exception {
            Log.d(TAG, "default download");
            try {
                final byte[] data = new byte[BLOCK_SIZE];
                while (is.read(data) >= 0)
                    ;
            } catch (Throwable e) {
                Log.e(TAG, "default download: ", e);
            } finally {
                closeSilently(is);
            }
        }

        public void on4xxError(int statusCode, String charset, InputStream is) throws Exception {
            String content = "";
            try {
                content = HttpClient.readStream(is, charset, mCancelled);
            } catch (Throwable e) {
                Log.e(TAG, "Read error stream: ", e);
            } finally {
                closeSilently(is);
            }
            if (statusCode == 404) {
                Log.e(TAG, "on4xxError statusCode == 404");
                throw new FileNotFoundException(content);
            } else {
                onGenericError(statusCode, content);
            }
        }

        /**
         * Generic 4xx error with error content (except 404), in most cases, you just need to override this
         * function instead of the on4xxError function.
         */
        public void onGenericError(int statusCode, String content) throws Exception {
            throw new ProtocolException(content);
        }

        public void onDisconnect(HttpURLConnection conn) {
            disconnectSilently(conn);
        }
    }

    public static void doHttp(String url, String method, Map<String, String> heads, HttpHandler handler) throws Exception {
        doHttpWithTimeout(url, method, heads, handler, TIMEOUT_CONNECT, TIMEOUT_READ);
    }

    public static void doHttpWithTimeout(String url, String method, Map<String, String> heads, HttpHandler handler, int connectTimeout, int readTimeout) throws Exception {
        int redirectTimes = 5;  // max redirection times
        String redirectUrl;
        String cookies = null;
        do {
            HttpURLConnection conn = null;
            try {
                if (heads == null)
                    heads = new HashMap<String, String>(1);
                if (!heads.containsKey("Accept-Encoding"))
                    heads.put("Accept-Encoding", handler.mPreferGzip ? "gzip, identity" : "identity");

                conn = makeConnection(url, method, heads, connectTimeout, readTimeout);
                conn.setInstanceFollowRedirects(false);  // Redirect bug before Android 2.3
                if (cookies != null) {
                    // from http redirect
                    final int i = cookies.indexOf(';');
                    conn.setRequestProperty("Cookie", i == -1 ? cookies : cookies.substring(0, i));
                }

                if (conn instanceof HttpsURLConnection) {
                    handler.onCheckSSL((HttpsURLConnection) conn);
                }

                if ("POST".equals(method) || "PUT".equals(method) || "DELETE".equals(method) || "MOVE".equals(method)) {
                    handler.onUpload(conn);
                }

                int statusCode = 0;
                try {
                    statusCode = conn.getResponseCode();
                } catch (IOException e) {
                    final String msg = e.getMessage();
                    if (msg != null && msg.contains("authentication challenge"))
                        statusCode = conn.getResponseCode();
                    else {
                        throw e;
                    }
                }
                if ((statusCode == HttpURLConnection.HTTP_MOVED_TEMP || statusCode == HttpURLConnection.HTTP_MOVED_PERM ||
                        statusCode == HttpURLConnection.HTTP_SEE_OTHER) && (redirectUrl = conn.getHeaderField("Location")) != null) {
                    // even getInstanceFollowRedirects() will be true by default, https to http or http to https will not be redirected.
                    url = redirectUrl;
                    cookies = conn.getHeaderField("Set-Cookie");
                    if (handler.onRedirect(conn, url))
                        continue;
                } else {
                    handler.onResponse(conn, statusCode);

                    if (!(statusCode >= 200 && statusCode < 300)) {
                    }
                }
                return;
            } finally {
                handler.onDisconnect(conn);
            }
        } while (--redirectTimes > 0);


        throw new UnknownHostException("HTTP error or redirect too many times");
    }

	/*
	 * HTTPS handler
	*/

    private static SSLContext mAlwaysTrustSSLContext;

    public static SSLContext getAlwaysTrustSSLClient() {
        synchronized (HttpClient.class) {
            if (mAlwaysTrustSSLContext == null) {
                final X509TrustManager tm = new X509TrustManager() {
                    public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                        //	Log.d(TAG, "checkClientTrusted:" + chain);
                    }

                    public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                        //	Log.d(TAG, "checkServerTrusted:" + chain);
                    }

                    public X509Certificate[] getAcceptedIssuers() {
                        //	Log.d(TAG, "getAcceptedIssuers:");
                        return new X509Certificate[0];
                    }
                };
                try {
                    final SSLContext context = SSLContext.getInstance("TLS");
                    context.init(null, new X509TrustManager[]{tm}, null);
                    mAlwaysTrustSSLContext = context;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return mAlwaysTrustSSLContext;
        }
    }

    public static void setInsecureSSLConnection(HttpsURLConnection connection) throws Exception {
        connection.setHostnameVerifier(new HostnameVerifier() {
            public boolean verify(String hostname, SSLSession session) {
                //	Log.d(TAG, "verify:" + hostname);
                return true;
            }
        });

        final SSLContext context = getAlwaysTrustSSLClient();
        connection.setSSLSocketFactory(context.getSocketFactory());
    }

}
