package com.zhangwx.z_utils.Z_Utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.os.Environment;
import android.os.StatFs;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import static android.os.Environment.MEDIA_MOUNTED;
import static android.os.Environment.isExternalStorageEmulated;

/**
 * Created by zhangwx on 2016/8/24.
 */
public class FileUtils {
    // 解压并校验解压后的entry的md5值
    public static boolean unzip(String srcPath, String dstPath, String md5) {
        boolean succeeded = false;
        ZipFile zipFile = null;
        InputStream is = null;
        FileOutputStream os = null;

        try {
            zipFile = new ZipFile(srcPath);
            @SuppressWarnings("rawtypes")
            Enumeration entries = zipFile.entries();
            if (!entries.hasMoreElements()) {
                return false;
            }

            ZipEntry entry = (ZipEntry) entries.nextElement();
            is = zipFile.getInputStream(entry);

            MessageDigest md5Digest = null;
            if (!TextUtils.isEmpty(md5)) {
                md5Digest = MessageDigest.getInstance("MD5");
                is = new DigestInputStream(is, md5Digest);
            }

            final int BUF_SIZE = 4096;
            byte[] buffer = new byte[BUF_SIZE];

            os = new FileOutputStream(dstPath);

            int bytes = 0;
            do {
                bytes = is.read(buffer);
                if (bytes > 0) {
                    os.write(buffer, 0, bytes);
                } else {
                    break;
                }
            } while (true);

            os.flush();

            if (md5Digest != null) {
                String md5String = MD5Utils.encodeHex(md5Digest.digest());
                if (md5String.compareToIgnoreCase(md5) != 0) {
                    return false;
                }
            }

            succeeded = true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (os != null) {
                    os.close();
                }

                if (is != null) {
                    is.close();
                }

                if (zipFile != null) {
                    zipFile.close();
                }

                if (!succeeded) {
                    (new File(dstPath)).delete();
                }
            } catch (Exception e) {
            }
        }
        return succeeded;
    }

    // 删除dstPath文件 并将srcPath文件重命名为dstPath
    public static boolean replaceFile(String srcPath, String dstPath) {
        File dstFile = new File(dstPath);
        if (dstFile.exists()) {
            int retry = 3;
            do {
                if (dstFile.delete()) {
                    return (new File(srcPath)).renameTo(dstFile);
                }

                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                }
            } while (--retry > 0);
            return false;
        }

        return (new File(srcPath)).renameTo(dstFile);
    }

    // 外部存储设备是否有效
    public static boolean isValidExternalStorage() {
        String state = Environment.getExternalStorageState();
        return state.equals(Environment.MEDIA_MOUNTED);
    }

    // 内部存储设备可用大小
    public static long getAvailableInternalStorageSize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());

        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();

        return (availableBlocks * blockSize);
    }

    // 添加斜杠
    public static String addSlash(final String path) {
        if (TextUtils.isEmpty(path)) {
            return File.separator;
        }

        if (path.charAt(path.length() - 1) != File.separatorChar) {
            return path + File.separatorChar;
        }

        return path;
    }

    // 去掉斜杠
    public static String removeSlash(String path) {
        if (TextUtils.isEmpty(path) || path.length() == 1) {
            return path;
        }

        if (path.charAt(path.length() - 1) == File.separatorChar) {
            return path.substring(0, path.length() - 1);
        }

        return path;
    }

    // 最后的斜杠换成0，如果没有斜杠则补0
    public static String replaceEndSlashBy0(String path) {

        if (TextUtils.isEmpty(path)) {
            return "0";
        }

        if (path.charAt(path.length() - 1) != File.separatorChar) {
            return path + "0";
        }

        return path.substring(0, path.length() - 1) + "0";
    }

    public static File checkPath(String path) {
        if (TextUtils.isEmpty(path)) {
            return null;
        }

        File file = new File(path);
        if (file == null || !file.exists()) {
            return null;
        }
        return file;
    }

    public static File checkDir(String path) {
        if (TextUtils.isEmpty(path))
            return null;
        File dir = new File(path);
        if (!dir.exists() || !dir.isDirectory()) {
            return null;
        }
        return dir;
    }

    public static File checkDir(File dir) {
        if (dir == null || !dir.exists() || !dir.isDirectory()) {
            return null;
        }
        return dir;
    }

    public static File checkFile(String path) {
        if (TextUtils.isEmpty(path))
            return null;
        File file = new File(path);
        if (!file.exists() || !file.isFile()) {
            return null;
        }
        return file;
    }

    public static File checkFile(File file) {
        if (file == null || !file.exists() || !file.isFile()) {
            return null;
        }
        return file;
    }

    public static File checkDirAndCreate(String path) {
        if (TextUtils.isEmpty(path))
            return null;
        File dir = new File(path);
        if (!dir.exists() || !dir.isDirectory()) {
            dir.delete();
            dir.mkdirs();
        }
        return dir;
    }

    public static File checkDirCreate(String path) {
        if (TextUtils.isEmpty(path))
            return null;
        File dir = new File(path);
        if (!dir.exists() || !dir.isDirectory()) {
            dir.delete();
            dir.mkdir();
        }
        return dir;
    }

    public static boolean checkFileAndDelete(String path) {
        if (TextUtils.isEmpty(path))
            return false;
        File file = new File(path);
        if (file.exists()) {
            file.delete();
        }
        return true;
    }

    public static InputStream checkAssetsFile(Context context, String path) {
        InputStream is = null;
        if(path == null){
            return null;
        }
        try {
            AssetManager manager = context.getAssets();
            is = manager.open(path);

        } catch (Exception e) {
        }

        return is;
    }

    public static boolean checkAssetsDir(Context context, String rootPath, String path) {

        boolean exist = false;

        try {
            context.getAssets().open(path);
            exist = true;
        } catch (Exception e) {
        }
        return exist;
    }

    /**
     * 复制单个文件
     * @param oldPath String 原文件路径 如：c:/fqf.txt
     * @param newPath String 复制后路径 如：f:/fqf.txt
     * @return int 0:success -1:throw exception  -2: the oldFile not exist
     * @author renjie
     * @date 2014.02.27
     */
    public static int copyFile(String oldPath, String newPath) {
        if(oldPath != null && newPath!= null && oldPath.equals(newPath)){
            return -1;
        }
        InputStream inStream = null;
        FileOutputStream fs = null;
        try {
            int bytesum = 0;
            int byteread = 0;
            File oldfile = new File(oldPath);
            if (oldfile.exists()) { // 文件存在时
                inStream = new FileInputStream(oldPath); // 读入原文件
                fs = new FileOutputStream(newPath);
                byte[] buffer = new byte[1444];
                while ((byteread = inStream.read(buffer)) != -1) {
                    bytesum += byteread; // 字节数 文件大小
                    // System.out.println(bytesum);
                    fs.write(buffer, 0, byteread);
                }
                fs.flush();
                fs.getFD().sync();
                return 0;
            } else {
                return -2;
            }
        } catch (Exception e) {
            return -1;
        } finally {
            try{
                if(inStream != null){
                    inStream.close();
                }
            } catch(Exception e){

            }

            try{
                if(fs != null){
                    fs.close();
                }
            } catch(Exception e){

            }
        }
    }

    private static int OFFSET_LENGTH = 8;

    public static boolean verifyOriginalFileWithCopiedFile(String originalPath, String copiedPath) {
        File originalFile = new File(originalPath);
        InputStream originalinStream = null; // 读入原文件
        InputStream copiedInputStream = null; // 读入原文件
        try {
            originalinStream = new FileInputStream(originalPath); // 读入原文件
            copiedInputStream = new FileInputStream(copiedPath); // 读入原文件
            long fileSize = originalFile.length();
            originalinStream.skip(fileSize - OFFSET_LENGTH);
            copiedInputStream.skip(fileSize - OFFSET_LENGTH);
            byte[] originabuffer = new byte[OFFSET_LENGTH];
            byte[] copiedbuffer = new byte[OFFSET_LENGTH];
            byte[] tmpbuffer = new byte[OFFSET_LENGTH];
            int byteread = 0;
            int bytesum = 0;
            while ((byteread = originalinStream.read(tmpbuffer)) != -1) {
                if (bytesum + byteread >= 8) {
                    byteread = 8 - bytesum;
                }
                System.arraycopy(tmpbuffer, 0, originabuffer, bytesum, byteread);
                bytesum += byteread; // 字节数 文件大小
                if (bytesum >= 8) {
                    break;
                }
                // System.out.println(bytesum);
            }

            byteread = 0;
            bytesum = 0;
            while ((byteread = copiedInputStream.read(tmpbuffer)) != -1) {
                // System.out.println(bytesum);
                if (bytesum + byteread >= 8) {
                    byteread = 8 - bytesum;
                }
                System.arraycopy(tmpbuffer, 0, copiedbuffer, bytesum, byteread);
                bytesum += byteread; // 字节数 文件大小
                if (bytesum >= 8) {
                    break;
                }
            }

            for (int i = 0; i < originabuffer.length; i++) {
                if (originabuffer[i] != copiedbuffer[i]) {
                    return false;
                }
            }
            return true;

        } catch (Exception e) {
            return false;
        } finally {
            try {
                if (originalinStream != null) {
                    originalinStream.close();
                }
                if (copiedInputStream != null) {
                    copiedInputStream.close();
                }
            } catch (Exception e) {

            }
        }
    }


    /**
     * 复制整个文件夹内容
     * @param oldPath String 原文件路径 如：c:/fqf
     * @param newPath String 复制后路径 如：f:/fqf/ff
     * @return boolean
     * @author renjie
     * @date 2014.02.27
     */
    public static void copyFolder(String oldPath, String newPath) {

        try {
            (new File(newPath)).mkdirs(); //如果文件夹不存在 则建立新文件夹
            File a=new File(oldPath);
            String[] file=a.list();
            File temp=null;
            for (int i = 0; i < file.length; i++) {
                if(oldPath.endsWith(File.separator)){
                    temp=new File(oldPath+file[i]);
                }
                else{
                    temp=new File(oldPath+ File.separator+file[i]);
                }

                if(temp.isFile()){
                    FileInputStream input = new FileInputStream(temp);
                    FileOutputStream output = new FileOutputStream(newPath + "/" +
                            (temp.getName()).toString());
                    byte[] b = new byte[1024 * 5];
                    int len;
                    while ( (len = input.read(b)) != -1) {
                        output.write(b, 0, len);
                    }
                    output.flush();
                    output.close();
                    input.close();
                }
                if(temp.isDirectory()){//如果是子文件夹
                    copyFolder(oldPath+"/"+file[i],newPath+"/"+file[i]);
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();

        }
    }

    public static void copyAssetToFiles(Context context, String assetsPath, String datapath) {
        if (TextUtils.isEmpty(assetsPath) || TextUtils.isEmpty(datapath))
            return;

        File datafile = new File(datapath);

        if (datafile.exists() && datafile.isFile()) {

//            Log.d("show", String.format("data: %s exist", datapath));

        } else {
            InputStream is = null;
            try {

                AssetManager asm = context.getAssets();
                is = asm.open(assetsPath);
                FileUtils.copyToFile(is, datafile);

//                Log.d("show", String.format("copyAssetToFiles: %s -> %s", assetsPath, datafile));

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public static boolean copyToFile(InputStream inputStream, File destFile) {
        try {
            if (destFile.exists()) {
                destFile.delete();
            }
            FileOutputStream out = new FileOutputStream(destFile);
            try {
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) >= 0) {
                    out.write(buffer, 0, bytesRead);
                }
            } finally {
                out.flush();
                try {
                    out.getFD().sync();
                } catch (IOException e) {
                }
                out.close();
            }
            return true;
        } catch (IOException e) {
            return false;
        }
    }

//    /**
//     * 删除文件
//     * @param fileName
//     * @return true:成功 false:失败
//     */
//    public static boolean deleteFile(String fileName){
//        File file = new File(fileName);
//        if(file.isFile() && file.exists()){
//            file.delete();
//            return true;
//        }else{
//            return false;
//        }
//    }

    /**
     * 删除文件夹
     * @param folderPath
     */
    public static void delFolder(String folderPath) {
        try {
            delAllFile(folderPath);
            String filePath = folderPath;
            filePath = filePath.toString();
            File myFilePath = new File(filePath);
            myFilePath.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除文件夹中的所有文件
     * @param path
     * @return true:成功 false:失败
     */
    public static boolean delAllFile(String path) {
        if(path == null || path.length() <= 0) {
            return false;
        }

        boolean bReturn = false;
        File file = new File(path);
        if (!file.exists()) {
            return bReturn;
        }
        if (!file.isDirectory()) {
            return bReturn;
        }
        String[] tempList = file.list();
        if ( tempList != null ){
            File temp = null;
            for (int i = 0; i < tempList.length; i++) {
                if (path.endsWith(File.separator)) {
                    temp = new File(path + tempList[i]);
                } else {
                    temp = new File(path + File.separator + tempList[i]);
                }
                if (temp.isFile()) {
                    temp.delete();
                }
                if (temp.isDirectory()) {
                    delAllFile(path + File.separatorChar + tempList[i]);
                    delFolder(path + File.separatorChar + tempList[i]);
                    bReturn = true;
                }
            }
        }

        return bReturn;
    }

    public static int pathFileCount(String path){
        if(null == path || path.length() <= 0){
            return 0;
        }

        File pathFile = new File(path);
        if(!pathFile.exists() || !pathFile.isDirectory()){
            return 0;
        }

        String[] pathFileNames = pathFile.list();
        if(null == pathFileNames){
            return 0;
        }

        return pathFileNames.length;
    }

    public static boolean isCacheDirAvail(Context context) {
        if(null == context) {
            return false;
        }
        return null != context.getCacheDir();
    }

    public static String makePath(String path1, String path2) {
        if (path1.endsWith(File.separator))
            return path1 + path2;

        return path1 + File.separator + path2;
    }


    public static File checkFilesFile(Context context, String path) {
        if (TextUtils.isEmpty(path))
            return null;
        File dir = getFilesDir(context);
        if (dir == null)
            return null;
        String filepath = FileUtils.addSlash(dir.getAbsolutePath()) + path;
        File file = FileUtils.checkFile(filepath);
        return file;
    }

    public static File checkFilesDirAndCreate(Context context, String path) {
        if (TextUtils.isEmpty(path))
            return null;
        File basedir = getFilesDir(context);
        if (basedir == null)
            return null;
        String dirpath = FileUtils.addSlash(basedir.getAbsolutePath()) + path;
        File dir = FileUtils.checkDirAndCreate(dirpath);
        return dir;
    }

    /**
     * 封装对Context.getFilesDir()的调用
     */
    public static File getFilesDir(Context ctx) {

        if (null == ctx) {
            return null;
        }

        File result = null;
        for (int i = 0; i < 3; ++i) {
            // 因为有时候getFilesDir()在无法创建目录时会返回失败，所以我们在此等待并于半秒内尝试三次。
            result = ctx.getFilesDir();
            if (null != result) {
                break;
            } else {
                try {
                    Thread.sleep(166);
                } catch (InterruptedException e) {
                }
            }
        }

        return result;
    }


    public final static String FILE_EXTENSION_SEPARATOR = ".";

    private FileUtils() {
        throw new AssertionError();
    }

    /**
     * read file
     *
     * @param filePath
     * @param charsetName The name of a supported {@link java.nio.charset.Charset </code>charset<code>}
     * @return if file not exist, return null, else return content of file
     * @throws RuntimeException if an error occurs while operator BufferedReader
     */
    public static StringBuilder readFile(String filePath, String charsetName) {
        File file = new File(filePath);
        StringBuilder fileContent = new StringBuilder("");
        if (file == null || !file.isFile()) {
            return null;
        }

        BufferedReader reader = null;
        try {
            InputStreamReader is = new InputStreamReader(new FileInputStream(file), charsetName);
            reader = new BufferedReader(is);
            String line = null;
            while ((line = reader.readLine()) != null) {
                if (!fileContent.toString().equals("")) {
                    fileContent.append("\r\n");
                }
                fileContent.append(line);
            }
            reader.close();
            return fileContent;
        } catch (IOException e) {
            throw new RuntimeException("IOException occurred. ", e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    throw new RuntimeException("IOException occurred. ", e);
                }
            }
        }
    }

    /**
     * write file
     *
     * @param filePath
     * @param content
     * @param append is append, if true, write to the end of file, else clear content of file and write into it
     * @return return false if content is empty, true otherwise
     * @throws RuntimeException if an error occurs while operator FileWriter
     */
    public static boolean writeFile(String filePath, String content, boolean append) {
        if (StringUtils.isEmpty(content)) {
            return false;
        }

        FileWriter fileWriter = null;
        try {
            makeDirs(filePath);
            fileWriter = new FileWriter(filePath, append);
            fileWriter.write(content);
            fileWriter.close();
            return true;
        } catch (IOException e) {
            throw new RuntimeException("IOException occurred. ", e);
        } finally {
            if (fileWriter != null) {
                try {
                    fileWriter.close();
                } catch (IOException e) {
                    throw new RuntimeException("IOException occurred. ", e);
                }
            }
        }
    }

    /**
     * write file
     *
     * @param filePath
     * @param contentList
     * @param append is append, if true, write to the end of file, else clear content of file and write into it
     * @return return false if contentList is empty, true otherwise
     * @throws RuntimeException if an error occurs while operator FileWriter
     */
    public static boolean writeFile(String filePath, List<String> contentList, boolean append) {
        // if (ListUtils.isEmpty(contentList)) {
        //  return false;
        // }

        FileWriter fileWriter = null;
        try {
            makeDirs(filePath);
            fileWriter = new FileWriter(filePath, append);
            int i = 0;
            for (String line : contentList) {
                if (i++ > 0) {
                    fileWriter.write("\r\n");
                }
                fileWriter.write(line);
            }
            fileWriter.close();
            return true;
        } catch (IOException e) {
            throw new RuntimeException("IOException occurred. ", e);
        } finally {
            if (fileWriter != null) {
                try {
                    fileWriter.close();
                } catch (IOException e) {
                    throw new RuntimeException("IOException occurred. ", e);
                }
            }
        }
    }

    /**
     * write file, the string will be written to the begin of the file
     *
     * @param filePath
     * @param content
     * @return
     */
    public static boolean writeFile(String filePath, String content) {
        return writeFile(filePath, content, false);
    }

    /**
     * write file, the string list will be written to the begin of the file
     *
     * @param filePath
     * @param contentList
     * @return
     */
    public static boolean writeFile(String filePath, List<String> contentList) {
        return writeFile(filePath, contentList, false);
    }

    /**
     * write file, the bytes will be written to the begin of the file
     *
     * @param filePath
     * @param stream
     * @return
     * @see {@link #writeFile(String, InputStream, boolean)}
     */
    public static boolean writeFile(String filePath, InputStream stream) {
        return writeFile(filePath, stream, false);
    }

    /**
     * write file
     *
     * @param filePath the file to be opened for writing.
     * @param stream the input stream
     * @param append if <code>true</code>, then bytes will be written to the end of the file rather than the beginning
     * @return return true
     * @throws RuntimeException if an error occurs while operator FileOutputStream
     */
    public static boolean writeFile(String filePath, InputStream stream, boolean append) {
        return writeFile(filePath != null ? new File(filePath) : null, stream, append);
    }

    /**
     * write file, the bytes will be written to the begin of the file
     *
     * @param file
     * @param stream
     * @return
     * @see {@link #writeFile(File, InputStream, boolean)}
     */
    public static boolean writeFile(File file, InputStream stream) {
        return writeFile(file, stream, false);
    }

    /**
     * write file
     *
     * @param file the file to be opened for writing.
     * @param stream the input stream
     * @param append if <code>true</code>, then bytes will be written to the end of the file rather than the beginning
     * @return return true
     * @throws RuntimeException if an error occurs while operator FileOutputStream
     */
    public static boolean writeFile(File file, InputStream stream, boolean append) {
        OutputStream o = null;

        if (file == null)
            return false;

        try {
            makeDirs(file.getAbsolutePath());
            o = new FileOutputStream(file, append);
            byte data[] = new byte[1024];
            int length = -1;
            while ((length = stream.read(data)) != -1) {
                o.write(data, 0, length);
            }
            o.flush();
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            // throw new RuntimeException("FileNotFoundException occurred. ", e);
        } catch (IOException e) {
            e.printStackTrace();
            // throw new RuntimeException("IOException occurred. ", e);
        } finally {
            if (o != null) {
                try {
                    o.close();
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    //throw new RuntimeException("IOException occurred. ", e);
                }
            }
        }
        return false;
    }

    /**
     * read file to string list, a element of list is a line
     *
     * @param filePath
     * @param charsetName The name of a supported {@link java.nio.charset.Charset </code>charset<code>}
     * @return if file not exist, return null, else return content of file
     * @throws RuntimeException if an error occurs while operator BufferedReader
     */
    public static List<String> readFileToList(String filePath, String charsetName) {
        File file = new File(filePath);
        List<String> fileContent = new ArrayList<String>();
        if (file == null || !file.isFile()) {
            return null;
        }

        BufferedReader reader = null;
        try {
            InputStreamReader is = new InputStreamReader(new FileInputStream(file), charsetName);
            reader = new BufferedReader(is);
            String line = null;
            while ((line = reader.readLine()) != null) {
                fileContent.add(line);
            }
            reader.close();
            return fileContent;
        } catch (IOException e) {
            throw new RuntimeException("IOException occurred. ", e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    throw new RuntimeException("IOException occurred. ", e);
                }
            }
        }
    }

    /**
     * get file name from path, not include suffix
     *
     * <pre>
     *      getFileNameWithoutExtension(null)               =   null
     *      getFileNameWithoutExtension("")                 =   ""
     *      getFileNameWithoutExtension("   ")              =   "   "
     *      getFileNameWithoutExtension("abc")              =   "abc"
     *      getFileNameWithoutExtension("a.mp3")            =   "a"
     *      getFileNameWithoutExtension("a.b.rmvb")         =   "a.b"
     *      getFileNameWithoutExtension("c:\\")              =   ""
     *      getFileNameWithoutExtension("c:\\a")             =   "a"
     *      getFileNameWithoutExtension("c:\\a.b")           =   "a"
     *      getFileNameWithoutExtension("c:a.txt\\a")        =   "a"
     *      getFileNameWithoutExtension("/home/admin")      =   "admin"
     *      getFileNameWithoutExtension("/home/admin/a.txt/b.mp3")  =   "b"
     * </pre>
     *
     * @param filePath
     * @return file name from path, not include suffix
     * @see
     */
    public static String getFileNameWithoutExtension(String filePath) {
        if (StringUtils.isEmpty(filePath)) {
            return filePath;
        }

        int extenPosi = filePath.lastIndexOf(FILE_EXTENSION_SEPARATOR);
        int filePosi = filePath.lastIndexOf(File.separator);
        if (filePosi == -1) {
            return (extenPosi == -1 ? filePath : filePath.substring(0, extenPosi));
        }
        if (extenPosi == -1) {
            return filePath.substring(filePosi + 1);
        }
        return (filePosi < extenPosi ? filePath.substring(filePosi + 1, extenPosi) : filePath.substring(filePosi + 1));
    }

    /**
     * get file name from path, include suffix
     *
     * <pre>
     *      getFileName(null)               =   null
     *      getFileName("")                 =   ""
     *      getFileName("   ")              =   "   "
     *      getFileName("a.mp3")            =   "a.mp3"
     *      getFileName("a.b.rmvb")         =   "a.b.rmvb"
     *      getFileName("abc")              =   "abc"
     *      getFileName("c:\\")              =   ""
     *      getFileName("c:\\a")             =   "a"
     *      getFileName("c:\\a.b")           =   "a.b"
     *      getFileName("c:a.txt\\a")        =   "a"
     *      getFileName("/home/admin")      =   "admin"
     *      getFileName("/home/admin/a.txt/b.mp3")  =   "b.mp3"
     * </pre>
     *
     * @param filePath
     * @return file name from path, include suffix
     */
    public static String getFileName(String filePath) {
        if (StringUtils.isEmpty(filePath)) {
            return filePath;
        }

        int filePosi = filePath.lastIndexOf(File.separator);
        return (filePosi == -1) ? filePath : filePath.substring(filePosi + 1);
    }

    /**
     * get folder name from path
     *
     * <pre>
     *      getFolderName(null)               =   null
     *      getFolderName("")                 =   ""
     *      getFolderName("   ")              =   ""
     *      getFolderName("a.mp3")            =   ""
     *      getFolderName("a.b.rmvb")         =   ""
     *      getFolderName("abc")              =   ""
     *      getFolderName("c:\\")              =   "c:"
     *      getFolderName("c:\\a")             =   "c:"
     *      getFolderName("c:\\a.b")           =   "c:"
     *      getFolderName("c:a.txt\\a")        =   "c:a.txt"
     *      getFolderName("c:a\\b\\c\\d.txt")    =   "c:a\\b\\c"
     *      getFolderName("/home/admin")      =   "/home"
     *      getFolderName("/home/admin/a.txt/b.mp3")  =   "/home/admin/a.txt"
     * </pre>
     *
     * @param filePath
     * @return
     */
    public static String getFolderName(String filePath) {

        if (StringUtils.isEmpty(filePath)) {
            return filePath;
        }

        int filePosi = filePath.lastIndexOf(File.separator);
        return (filePosi == -1) ? "" : filePath.substring(0, filePosi);
    }

    /**
     * get suffix of file from path
     *
     * <pre>
     *      getFileExtension(null)               =   ""
     *      getFileExtension("")                 =   ""
     *      getFileExtension("   ")              =   "   "
     *      getFileExtension("a.mp3")            =   "mp3"
     *      getFileExtension("a.b.rmvb")         =   "rmvb"
     *      getFileExtension("abc")              =   ""
     *      getFileExtension("c:\\")              =   ""
     *      getFileExtension("c:\\a")             =   ""
     *      getFileExtension("c:\\a.b")           =   "b"
     *      getFileExtension("c:a.txt\\a")        =   ""
     *      getFileExtension("/home/admin")      =   ""
     *      getFileExtension("/home/admin/a.txt/b")  =   ""
     *      getFileExtension("/home/admin/a.txt/b.mp3")  =   "mp3"
     * </pre>
     *
     * @param filePath
     * @return
     */
    public static String getFileExtension(String filePath) {
        if (StringUtils.isBlank(filePath)) {
            return filePath;
        }

        int extenPosi = filePath.lastIndexOf(FILE_EXTENSION_SEPARATOR);
        int filePosi = filePath.lastIndexOf(File.separator);
        if (extenPosi == -1) {
            return "";
        }
        return (filePosi >= extenPosi) ? "" : filePath.substring(extenPosi + 1);
    }

    /**
     * Creates the directory named by the trailing filename of this file, including the complete directory path required
     * to create this directory. <br/>
     * <br/>
     * <ul>
     * <strong>Attentions:</strong>
     * <li>makeDirs("C:\\Users\\Trinea") can only create users folder</li>
     * <li>makeFolder("C:\\Users\\Trinea\\") can create Trinea folder</li>
     * </ul>
     *
     * @param filePath
     * @return true if the necessary directories have been created or the target directory already exists, false one of
     *         the directories can not be created.
     *         <ul>
     *         <li>if {@link FileUtils#getFolderName(String)} return null, return false</li>
     *         <li>if target directory already exists, return true</li>
     *         <li>return boolean</li>
     *         </ul>
     */
    public static boolean makeDirs(String filePath) {
        String folderName = getFolderName(filePath);
        if (StringUtils.isEmpty(folderName)) {
            return false;
        }

        File folder = new File(folderName);
        return (folder.exists() && folder.isDirectory()) ? true : folder.mkdirs();
    }

    /**
     * @param filePath
     * @return
     * @see #makeDirs(String)
     */
    public static boolean makeFolders(String filePath) {
        return makeDirs(filePath);
    }

    /**
     * Indicates if this file represents a file on the underlying file system.
     *
     * @param filePath
     * @return
     */
    public static boolean isFileExist(String filePath) {
        if (StringUtils.isBlank(filePath)) {
            return false;
        }

        File file = new File(filePath);
        return (file.exists() && file.isFile());
    }

    /**
     * Indicates if this file represents a directory on the underlying file system.
     *
     * @param directoryPath
     * @return
     */
    public static boolean isFolderExist(String directoryPath) {
        if (StringUtils.isBlank(directoryPath)) {
            return false;
        }

        File dire = new File(directoryPath);
        return (dire.exists() && dire.isDirectory());
    }

    /**
     * delete file or directory
     * <ul>
     * <li>if path is null or empty, return true</li>
     * <li>if path not exist, return true</li>
     * <li>if path exist, delete recursion. return true</li>
     * <ul>
     *
     * @param path
     * @return
     */
    public static boolean deleteFile(String path) {
        if (StringUtils.isBlank(path)) {
            return true;
        }

        File file = new File(path);
        if (!file.exists()) {
            return true;
        }
        if (file.isFile()) {
            return file.delete();
        }
        if (!file.isDirectory()) {
            return false;
        }
        File[] files = file.listFiles();
        if (files != null && files.length > 0) {
            for (File f : files) {
                if (f.isFile()) {
                    f.delete();
                } else if (f.isDirectory()) {
                    deleteFile(f.getAbsolutePath());
                }
            }
        }
        return file.delete();
    }

    /**
     * get file size
     * <ul>
     * <li>if path is null or empty, return -1</li>
     * <li>if path exist and it is a file, return file size, else return -1</li>
     * <ul>
     *
     * @param path
     * @return returns the length of this file in bytes. returns -1 if the file does not exist.
     */
    public static long getFileSize(String path) {
        if (StringUtils.isBlank(path)) {
            return -1;
        }

        File file = new File(path);
        return (file.exists() && file.isFile() ? file.length() : -1);
    }

    public static String getFilesDirPath(Context context)
    {
        String strPath = null;
        File file = getFilesDirObj(context, 3);
        if (null != file)
        {
            strPath = file.getPath();
        }
        return strPath;
    }
    private static void checkApplicationDataSubDir(Context context, String strSubDir)
    {
        if (null != context)
        {
            ApplicationInfo app = context.getApplicationInfo();
            if (null != app && !TextUtils.isEmpty(app.dataDir))
            {
                File dDataDir = new File(app.dataDir + "/" + strSubDir);
                if (dDataDir != null && dDataDir.isFile()) {
                    dDataDir.delete();
                }
                dDataDir.mkdirs();
            }
        }
    }

    private static File getFilesDirObj(Context context, int nRetry)
    {
        checkApplicationDataSubDir(context, "files");

        File dir = null;
        if (null != context)
        {
            if (nRetry <= 0)
            {
                dir = context.getFilesDir();
            }
            else
            {
                for (int nIdx = 0; nIdx < nRetry; ++nIdx)
                {
                    dir = context.getFilesDir();
                    if (null != dir)
                    {
                        break;
                    }

                    SystemClock.sleep(10);
                }
            }
        }

        return dir;
    }

    public static void copy(File src, File dst) throws IOException {
        InputStream in = new FileInputStream(src);
        OutputStream out = new FileOutputStream(dst);

        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        in.close();
        out.close();
    }

    /**
     * 保存Bitmap到目标路径
     *
     * @param bitmap
     * @param path
     * @return
     */
    public static boolean saveBitmap(Bitmap bitmap, String path) {
        if (bitmap == null || path == null)
            return false;
        OutputStream out = null;
        try {
            out = new FileOutputStream(path);
            Bitmap.CompressFormat format = null;
            String ext = FileUtils.pathFindExtension(path);
            if (ext != null) {
                if (ext.equalsIgnoreCase("jpg") || ext.equalsIgnoreCase("jpeg")) {
                    format = Bitmap.CompressFormat.JPEG;
                }
            }
            if (format == null) {
                format = Bitmap.CompressFormat.PNG;
            }
            Log.e("CircleHead", "HeadBitmap Format: " + format.name());
            return bitmap.compress(format, 80, out);
        } catch (FileNotFoundException e) {
            if (e != null){
                Log.e("CircleHead", "saveHeadBitmap fail: " + e.getMessage());
            }
        } catch (Throwable t) {
            if (t != null){
                Log.e("CircleHead", "saveHeadBitmap fail: " + t.getMessage());
            }
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (Exception e) {
                    if (e != null){
                        Log.e("CircleHead", "saveHeadBitmap fail: " + e.getMessage());
                    }
                }
            }
        }
        return false;
    }

    /**
     * 获取文件后缀
     * @param path
     * @return
     */
    public static String pathFindExtension(String path) {
        if (path == null)
            return null;
        int index = path.lastIndexOf(".");
        if (index >= 0) {
            return path.substring(index + 1, path.length());
        }
        return null;
    }

    /**
     * "/data/data/[app_package_name]/files/type"
     * @param context
     * @param type
     * @return
     */
    public static File getReserveDiskCacheDir(Context context, String type) {
        File cacheDir = getCacheDirectory(context, type, false);
        File individualDir = new File(cacheDir, "uil-images");
        if (individualDir.exists() || individualDir.mkdir()) {
            cacheDir = individualDir;
        }
        return cacheDir;
    }

    /**
     * "/Android/data/[app_package_name]/files/type"
     * @param context
     * @param type
     * @return
     */
    public static File getCacheDirectory(Context context, String type) {
        return getCacheDirectory(context, type, true);
    }

    public static File getCacheDirectory(Context context, String type, boolean preferExternal) {
        File appCacheDir = null;
        String externalStorageState;
        try {
            externalStorageState = Environment.getExternalStorageState();
        } catch (NullPointerException e) { // (sh)it happens (Issue #660)
            externalStorageState = "";
        } catch (IncompatibleClassChangeError e) { // (sh)it happens too (Issue #989)
            externalStorageState = "";
        }
        if (preferExternal && MEDIA_MOUNTED.equals(externalStorageState) && isExternalStorageEmulated()) {
            appCacheDir = getExternalCacheDir(context, type);
        }
        if (appCacheDir == null) {
            appCacheDir = new File(context.getFilesDir(), type);
        }
        if (appCacheDir == null) {
            String cacheDirPath = "/data/data/" + context.getPackageName() + "/file/";
            Log.e("FileUtils", "Can't define system cache directory" + cacheDirPath);
            appCacheDir = new File(cacheDirPath, type);
        }
        return appCacheDir;
    }

    private static File getExternalCacheDir(Context context, String type) {
        File dataDir = new File(new File(Environment.getExternalStorageDirectory(), "Android"), "data");
        File appCacheDir = new File(new File(dataDir, context.getPackageName()), "files");
        File cacheDir = new File(appCacheDir, type);
        if (!cacheDir.exists()) {
            if (!cacheDir.mkdirs()) {
                Log.e("FileUtils", "Unable to create external cache directory");
                return null;
            }
            try {
                new File(cacheDir, ".nomedia").createNewFile();
            } catch (IOException e) {
                Log.e("FileUtils", "Can't create \".nomedia\" file in application external cache directory");
            }
        }
        return cacheDir;
    }

    /**
     * 复制整个文件夹内容
     * @param oldPath String 原文件路径 如：c:/fqf
     * @param newPath String 复制后路径 如：f:/fqf/ff
     * @return boolean
     */
    public static void copyFolder(String oldPath, String newPath, boolean copyDirectory) {
        try {
            (new File(newPath)).mkdirs();
            File a = new File(oldPath);
            String[] file = a.list();
            File temp = null;
            for (int i = 0; i < file.length; i++) {

                if (oldPath.endsWith(File.separator)) {
                    temp = new File(oldPath + file[i]);
                } else{
                    temp = new File(oldPath + File.separator + file[i]);
                }

                if(temp.isFile()){
                    FileInputStream input = new FileInputStream(temp);
                    FileOutputStream output = new FileOutputStream(newPath + File.separator + temp.getName());
                    byte[] b = new byte[1024 * 5];
                    int len;
                    while ( (len = input.read(b)) != -1) {
                        output.write(b, 0, len);
                    }
                    output.flush();
                    output.close();
                    input.close();

                } else if (copyDirectory && temp.isDirectory()) {
                    copyFolder(oldPath + File.separator + file[i], newPath + File.separator + file[i], copyDirectory);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
