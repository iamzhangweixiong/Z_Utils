package com.zhangwx.z_utils.Z_DB.file;

import com.zhangwx.z_utils.MyApplication;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileLock;
import java.util.List;

public class FileCache {

    private static final int MAX_FILE_LEN = 1024;
    private static final byte[] RETURN = "\n".getBytes();
    private static final String FILE_PATH = "zCacheFile";
    private static final String FILE_NAME = "corpus.txt";
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

    public static synchronized String readFile() throws IOException {
        File file = new File(cachePath + FILE_NAME);
        if (file.exists()) {
            FileInputStream inputStream = new FileInputStream(file);
            int len = inputStream.available();
            if (len > MAX_FILE_LEN) {
                len = MAX_FILE_LEN;
            }

            byte[] bytes = new byte[len];
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            while (inputStream.read(bytes) != -1) {
                outputStream.write(bytes);
            }
            inputStream.close();
            return outputStream.toString();
        }
        return null;
    }

}
