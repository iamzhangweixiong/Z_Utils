package com.zhangwx.z_utils.Z_Pub;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

/**
 * Created by zhangwx on 2016/9/5.
 */
public class ZipHelper {
    static public boolean CreateZipFiles(File fs[], File zipFileName) {
        boolean result = false;

        try {
            int buff_len = 1024;
            byte buff[] = new byte[buff_len];

            ZipOutputStream zo = new ZipOutputStream(new FileOutputStream(zipFileName));
            for (int i = 0; i < fs.length; i++) {
                if (fs[i].exists() == false)
                    continue;

                FileInputStream fi = new FileInputStream(fs[i]);
                ZipEntry ze = new ZipEntry(fs[i].getName());

                zo.putNextEntry(ze);
                while (true) {
                    int count = fi.read(buff, 0, buff_len);
                    if (count <= 0) {
                        break;
                    }

                    zo.write(buff, 0, count);
                }

                fi.close();
            }

            zo.flush();
            zo.close();

            result = true;
        } catch (FileNotFoundException e) {

        } catch (IOException e) {

        }

        return result;
    }

    /**
     * 压缩文件,文件夹
     *
     * @param srcFilePath 要压缩的文件/文件夹名字
     * @param zipFilePath 指定压缩的目的和名字
     * @throws Exception
     */
    public static void zipFolder(String srcFilePath, String zipFilePath) throws Exception {
        if (null == srcFilePath || null == zipFilePath) {
            return;
        }
        ZipOutputStream outZip = new ZipOutputStream(new FileOutputStream(zipFilePath));

        File file = new File(srcFilePath);
        zipFiles(file.getParent() + File.separator, file.getName(), outZip);

        outZip.flush();
        outZip.close();

    }

    /**
     * 压缩文件
     *
     * @param folderPath
     * @param fileName
     * @param zipOut
     * @throws Exception
     */
    private static void zipFiles(String folderPath, String fileName, ZipOutputStream zipOut) throws Exception {
        if (zipOut == null) {
            return;
        }

        File file = new File(folderPath + fileName);

        // 判断是不是文件
        if (file.isFile()) {
            ZipEntry zipEntry = new ZipEntry(fileName);
            FileInputStream inputStream = new FileInputStream(file);
            zipOut.putNextEntry(zipEntry);

            int len;
            byte[] buffer = new byte[1024];

            while ((len = inputStream.read(buffer, 0, 1024)) != -1) {
                zipOut.write(buffer, 0, len);
            }

            inputStream.close();
        } else {
            // 文件夹的方式,获取文件夹下的子文件
            String fileList[] = file.list();

            // 如果没有子文件, 则添加进去即可
            if (fileList.length <= 0) {
                ZipEntry zipEntry = new ZipEntry(fileName + File.separator);
                zipOut.putNextEntry(zipEntry);
            }

            // 如果有子文件, 遍历子文件
            for (int i = 0; i < fileList.length; i++) {
                zipFiles(folderPath, fileName + File.separator + fileList[i], zipOut);
            }

        }

    }

    /**
     * 解压缩功能.
     * 将ZIP_FILENAME文件解压到ZIP_DIR目录下.
     *
     * @throws Exception
     */
    public static int upZipFile(File zipFile, String folderPath) throws ZipException, IOException {
        ZipFile zfile = new ZipFile(zipFile);
        Enumeration zList = zfile.entries();
        ZipEntry ze = null;
        byte[] buf = new byte[1024];
        while (zList.hasMoreElements()) {
            ze = (ZipEntry) zList.nextElement();
            if (ze.isDirectory()) {
                Log.d("Jason", "upZipFile -- ze.getName() = " + ze.getName() + "  folderPath:" + folderPath);
                String dirstr = folderPath + File.separator + ze.getName();
                //dirstr.trim();
                Log.d("Jason", "upZipFile -- str 1111= " + dirstr);
                File f = new File(dirstr);
                f.mkdir();
                continue;
            }
            Log.d("Jason", "upZipFile -- ze.getName() = " + ze.getName());
            OutputStream os = new BufferedOutputStream(new FileOutputStream(getRealFileName(folderPath, ze.getName())));
            InputStream is = new BufferedInputStream(zfile.getInputStream(ze));
            int readLen = 0;
            while ((readLen = is.read(buf, 0, 1024)) != -1) {
                os.write(buf, 0, readLen);
            }
            if (is != null) {
                is.close();
            }
            if (os != null) {
                os.close();
            }
        }
        if (zfile != null) {
            zfile.close();
        }
        return 0;
    }

    /**
     * 解压缩功能.
     * 将ZIP_FILENAME文件解压到ZIP_DIR目录下.
     *
     * @throws Exception
     */
    public int upZipFile1(File zipFile, String folderPath) throws ZipException, IOException {
        //public static void upZipFile() throws Exception{
        ZipFile zfile = new ZipFile(zipFile);
        Enumeration zList = zfile.entries();
        ZipEntry ze = null;
        byte[] buf = new byte[1024];
        while (zList.hasMoreElements()) {
            ze = (ZipEntry) zList.nextElement();
            if (ze.isDirectory()) {
                Log.d("upZipFile", "ze.getName() = " + ze.getName());
                String dirstr = folderPath + ze.getName();
                //dirstr.trim();
                dirstr = new String(dirstr.getBytes("8859_1"), "GB2312");
                Log.d("upZipFile", "str = " + dirstr);
                File f = new File(dirstr);
                f.mkdir();
                continue;
            }
            Log.d("upZipFile", "ze.getName() = " + ze.getName());
            OutputStream os = new BufferedOutputStream(new FileOutputStream(getRealFileName(folderPath, ze.getName())));
            InputStream is = new BufferedInputStream(zfile.getInputStream(ze));
            int readLen = 0;
            while ((readLen = is.read(buf, 0, 1024)) != -1) {
                os.write(buf, 0, readLen);
            }
            is.close();
            os.close();
        }
        zfile.close();
        return 0;
    }

    /**
     * 给定根目录，返回一个相对路径所对应的实际文件名.
     *
     * @param baseDir     指定根目录
     * @param absFileName 相对路径名，来自于ZipEntry中的name
     * @return java.io.File 实际的文件
     */
    public static File getRealFileName(String baseDir, String absFileName) {
        String[] dirs = absFileName.split("/");
        File ret = new File(baseDir);
        String substr = null;
        if (dirs.length > 1) {
            for (int i = 0; i < dirs.length - 1; i++) {
                substr = dirs[i];
                try {
                    //substr.trim();
                    substr = new String(substr.getBytes("8859_1"), "GB2312");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                ret = new File(ret, substr);
            }
            Log.d("upZipFile", "1ret = " + ret);
            if (!ret.exists())
                ret.mkdirs();
            substr = dirs[dirs.length - 1];
            try {
                //substr.trim();
                substr = new String(substr.getBytes("8859_1"), "GB2312");
                Log.d("upZipFile", "substr = " + substr);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            ret = new File(ret, substr);
            Log.d("upZipFile", "2ret = " + ret);
            return ret;
        }

        return ret;
    }
}
