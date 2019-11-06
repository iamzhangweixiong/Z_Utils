package com.zhangwx.z_utils.Z_DB.file;

import com.zhangwx.z_utils.MyApplication;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileLock;
import java.util.List;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.Okio;

public class FileCache {

    private static final int MAX_FILE_LEN = 1024;
    private static final byte[] RETURN = "\r\n".getBytes();
    private static final String FILE_PATH = "zCacheFile";
    private static final String FILE_NAME = "corpus";
    private static final String cachePath = MyApplication.getContext().getFilesDir() + File.separator
            + FILE_PATH + File.separator;

    // 写到内部存储
    public static boolean writeToFile(List<String> content) throws IOException {
        boolean bReturn = false;
        final File path = new File(cachePath);
        if (!path.exists()) {
            path.mkdirs();
        }
        final File file = new File(path.getPath() + File.separator + FILE_NAME);
        if (!file.exists()) {
            boolean created = file.createNewFile();
            if (!created) {
                return false;
            }
        }

        final FileOutputStream outStream = new FileOutputStream(file, true);
        final FileLock fl = outStream.getChannel().tryLock();
        if (fl != null) {
            for (String s : content) {
                outStream.write(s.getBytes());
                outStream.write(RETURN);
            }
            fl.release();
            bReturn = true;
        }
        outStream.close();
        return bReturn;
    }

    public static String readFile() throws IOException {
        final File file = new File(cachePath + FILE_NAME);
        if (file.exists()) {
            final FileInputStream inputStream = new FileInputStream(file);

            int len = inputStream.available();
            if (len > MAX_FILE_LEN) {
                len = MAX_FILE_LEN;
            }
//            用流读取会把换行符等读取出来
            byte[] bytes = new byte[len];
            final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            while (inputStream.read(bytes) != -1) {
                outputStream.write(bytes);
            }
            inputStream.close();
            return outputStream.toString();

//            final StringBuilder builder = new StringBuilder();
//            final BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
//            String line;
//            while ((line = reader.readLine()) != null) {
//                builder.append(line);// 需要自己加上换行符
//            }
//            return builder.toString();
        }
        return null;
    }

    public static boolean writeToFileOkio(List<String> content) throws IOException {
        final File path = new File(cachePath);
        if (!path.exists()) {
            path.mkdirs();
        }
        final File file = new File(path.getPath() + File.separator + FILE_NAME);
        if (!file.exists()) {
            boolean created = file.createNewFile();
            if (!created) {
                return false;
            }
        }

        final BufferedSink bufferedSink = Okio.buffer(Okio.appendingSink(file));
        for (String s : content) {
            bufferedSink.write(s.getBytes(), 0, s.length());
            bufferedSink.write(RETURN);
        }
        bufferedSink.flush();
        return true;
    }

    public static String readFileOkio() throws IOException {
        final File file = new File(cachePath + FILE_NAME);
        if (file.exists()) {
            final BufferedSource bufferedSource = Okio.buffer(Okio.source(file));
            return bufferedSource.readUtf8();
        }
        return null;
    }

}
