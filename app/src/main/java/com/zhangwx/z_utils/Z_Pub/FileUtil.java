package com.zhangwx.z_utils.Z_Pub;

import android.annotation.TargetApi;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.webkit.MimeTypeMap;

import com.zhangwx.z_utils.MyApplication;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.channels.FileLock;
import java.util.ArrayList;
/**
 * Created by zhangwx on 2016/9/5.
 */
public class FileUtil {
    public static long getFileSize(String filePath){
        if (TextUtils.isEmpty(filePath))
            return 0L;
        File file = new File(filePath);
        return getFileSize(file);
    }

    public static long getFileSize(File file){
        if(file.exists()){
            try {
                return file.length();
            } catch (Exception ignore) {}
        }
        return 0L;
    }
    /**
     * 拷贝文件
     *
     */
    public static boolean copyFile(String fromPath, String toPath) {
        boolean result = false;
        FileInputStream from = null;
        FileOutputStream to = null;
        try {
            from = new FileInputStream(fromPath);
            to = new FileOutputStream(toPath);
            byte[] buffer = new byte[4096];
            int bytes_read;
            while ((bytes_read = from.read(buffer)) != -1)
                to.write(buffer, 0, bytes_read);
            result = true;
        } catch (IOException e) {
            e.printStackTrace();
            result = false;
        } finally {
            CloseUtil.close(from);
            CloseUtil.close(to);
        }
        return result;
    }
    /**
     * 判断文件是否存在
     */
    public static boolean isExist(String path) {
        if (TextUtils.isEmpty(path))
            return false;
        return new File(path).exists();
    }
    public static boolean copyDir(String source ,String des){
        File sourceDir = new File(source);
        File desDir = new File(des);
        desDir.mkdirs();
        if(sourceDir.exists() && sourceDir.isDirectory() && desDir.exists() && desDir.isDirectory()){
            File[] sources = sourceDir.listFiles();
            if (sources != null) {
                for(File tmp : sources){
                    copyFile(tmp.getAbsolutePath() , des + File.separator + tmp.getName());
                }
            }
            return true;
        }
        return false;
    }

    /**
     * 复制文件夹,递归复制文件夹下 所以文件，包括文件和文件夹
     * @author LOST
     * @param filePathSrc
     * @param filePathTo
     * @throws IOException
     */
    public static boolean copyFileDirectory(String filePathSrc, String filePathTo) {
        boolean opResult = false;
        if(!TextUtils.isEmpty(filePathTo)) {
            File desFile = new File(filePathTo);
            if (!desFile.exists()) {// 新建目标目录
                desFile.mkdir();
            }
        }

        if(!TextUtils.isEmpty(filePathSrc)) {
            File srcFile = new File(filePathSrc);
            if (srcFile != null) {
                // 获取源文件夹当前下的文件或目录
                File[] fileArr = srcFile.listFiles();
                for (int i = 0; i < fileArr.length; i++) {
                    if (fileArr[i].isFile()) {
                        File sourceFile = fileArr[i];
                        File targetFile = new File(new File(filePathTo).getAbsolutePath()
                                + File.separator + fileArr[i].getName());
//						opResult = copyOneFile(sourceFile.getPath(), targetFile.getPath());
                        opResult = copyFile(sourceFile.getPath(), targetFile.getPath());
                    } else if (fileArr[i].isDirectory()) {
                        String srcDir = filePathSrc + "/" + fileArr[i].getName();
                        String desDir = filePathTo + "/" + fileArr[i].getName();
                        opResult = copyFileDirectory(srcDir, desDir);
                    }
                }
            }
        }

        return opResult;
    }

    public static void saveFile(InputStream input, String filePath) throws IOException {
        File fileTemp = new File(filePath);
        File parentFile = fileTemp.getParentFile();
        if(parentFile == null || (!parentFile.exists() && !parentFile.mkdirs()))
            return;

        OutputStream fos = null;
        try {
            fos = new FileOutputStream(fileTemp);
        } catch (IOException e) {
            fos = null;
            if (Build.VERSION.SDK_INT >= 19) {
                // 使用media库的api重新尝试
                MediaFile mediaFile = new MediaFile(MyApplication.getContext().getContentResolver(), new File(filePath));
                fos = mediaFile.write();
            }
            if (fos == null) {
                throw e;
            }
        }
        int readed = 0;
        byte[] buffer = new byte[4096];
        try {
            while ((readed = input.read(buffer)) != -1) {
                fos.write(buffer, 0, readed);
            }
        } finally {
            CloseUtil.close(fos);
        }
    }

    /**
     * 保存数据到文件
     */
    public static boolean saveFile(String filePath, byte[] content) throws IOException {
        boolean bReturn = false;
        FileOutputStream outStream = null;
        FileLock fl = null;
        try {
            File file = new File(filePath);
            if (file.exists())
                file.delete();
            File parentFile = file.getParentFile();
            if (parentFile != null && !parentFile.exists()) {
                parentFile.mkdirs();
            }
            outStream = new FileOutputStream(file);
            fl = outStream.getChannel().tryLock();
            if (fl != null) {
                outStream.write(content);
                bReturn = true;
            }
        } catch (Exception ignore) {}
        finally {
            if (fl != null) {
                try {
                    fl.release();
                } catch (Exception ignore) {}
            }
            CloseUtil.close(outStream);
        }
        return bReturn;
    }

    /**
     * 将输入流写入文件中
     * @param inputStream 输入流，不负责关闭
     * @param file 待写入文件
     */
    public static boolean inputStreamToFileWithException(InputStream inputStream, File file) throws IOException {
        if (file.exists()) {
            file.delete();
        }
        OutputStream os = null;
        try {
            os = new FileOutputStream(file);
            int bytesRead = 0;
            byte[] buffer = new byte[8192];
            while ((bytesRead = inputStream.read(buffer, 0, 8192)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            inputStream.reset();
        } finally {
            CloseUtil.close(os);
        }

        return true;

    }

    public static String getMimeType(String url) {
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getMimeTypeFromExtension(extension);
    }

    /**
     * 删除文件
     *
     * @return true:成功 false:失败
     */
    public static boolean deleteFile(String fileName) {
        File file = new File(fileName);
        if (file.isFile() && file.exists()) {
            boolean deleteSuccess = file.delete();
            // 如果删除不成功是由于4.4外置sd卡权限问题，则通过和压缩一样的方法来尝试删除图片
            if (!deleteSuccess) {
                if (Build.VERSION.SDK_INT >= 19 && MyApplication.getContext() != null) {
                    deleteEXSdcardFile(MyApplication.getContext(), file);
                    deleteSuccess = !file.exists();
                }
            }
            return deleteSuccess;
        } else {
            return false;
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private static void deleteEXSdcardFile(Context mContext, File file) {
        Uri filesUri = MediaStore.Files.getContentUri("external");
        Uri imagesUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        String where = MediaStore.MediaColumns.DATA + "=?";

        String[] selectionArgs = new String[] { file.getAbsolutePath() };
        try {
            mContext.getContentResolver().delete(filesUri, where, selectionArgs);
            if (file.exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Files.FileColumns.DATA, file.getAbsolutePath());
                mContext.getContentResolver().insert(imagesUri, values);
                mContext.getContentResolver().delete(filesUri, where, selectionArgs);
            }
            if (file.exists()) {
                Uri uri = getMediaUri(mContext, file.getPath());
                if (uri != null) {
                    try {
                        ContentValues values = new ContentValues();
                        values.put(MediaStore.Files.FileColumns.MEDIA_TYPE, MediaStore.Files.FileColumns.MEDIA_TYPE_PLAYLIST);
                        mContext.getContentResolver().update(uri, values, null, null);
                        mContext.getContentResolver().delete(uri, null, null);
                    } catch (Exception e) {
                    }
                }
            }
            if (file.exists()) {
                deleteByMediaTypePlaylist(mContext, file.getPath());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除文件夹 该类
     * @param file
     *            必须是一个文件夹
     * @return
     */
    public static   boolean deleteFileDirectory(File file) {
        boolean delFileResult = true;
        if (!file.exists() || !file.isDirectory()) {
            return false;
        }
        File[] fileArrs = file.listFiles();
        for (int i = 0; i < fileArrs.length; i++) {
            File fileTemp = fileArrs[i];
            if (fileTemp.isFile()) {
                delFileResult = deleteFile(fileTemp);
                if (!delFileResult) {
                    break;
                }
            } else {
                delFileResult = deleteFileDirectory(fileTemp);
                if (!delFileResult) {
                    break;
                }
            }
        }
        if (!delFileResult) {
            return false;
        }
        if (file.delete()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 删除单个文件
     *
     * @param file
     * @return 成功删除返回true ，否则 fasle
     */
    public static boolean deleteFile(File file) {
        boolean deleteResult = false;
        if (file.exists()) {
            if (file.delete()) {
                deleteResult = true;
            }
        }
        return deleteResult;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private static Uri getMediaUri(Context context, String path) {
        Uri result = null;
        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(MediaStore.Files.getContentUri("external"), new String[]{MediaStore.Files.FileColumns._ID}, "_data=?", new String[]{path}, null);
            if (cursor != null && cursor.getCount() == 1) { // for files need to be overridden
                cursor.moveToFirst();
                long id = cursor.getLong(cursor.getColumnIndex(MediaStore.Files.FileColumns._ID));
                result = MediaStore.Files.getContentUri("external", id);
            } else { // for files need to be added
                ContentValues values = new ContentValues();
                values.put(MediaStore.Files.FileColumns.DATA, path);
                result = context.getContentResolver().insert(MediaStore.Files.getContentUri("external"), values);
            }
        } catch (Exception ignore) {
        } finally {
            CloseUtil.close(cursor);
        }

        return result;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private static void deleteByMediaTypePlaylist(Context mContext, String path) {
        Uri uri = getMediaUri(mContext, path);
        if (uri != null) {
            try {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Files.FileColumns.MEDIA_TYPE, MediaStore.Files.FileColumns.MEDIA_TYPE_PLAYLIST);
                mContext.getContentResolver().update(uri, values, null, null);
                mContext.getContentResolver().delete(uri, null, null);
            } catch (Exception e) {
            }
        }
    }

    public static final int ERROR_CODE_RENAME_FILE_SUCCEED = 1;
    public static final int ERROR_CODE_INVALID_ARGUMENT = -1;
    public static final int ERROR_CODE_FILE_ALREADY_EXIST = -2;
    public static final int ERROR_CODE_RENAME_FILE_FAILED = -3;
    /**
     * 重命名文件
     * @param srcfile 源文件
     * @param newFilePath 新路径
     * @return 修改成功, 返回true
     */
    public static int renameFile(String srcfile, String newFilePath) {
        File file = new File(srcfile);
        if (!file.exists() || file.getAbsolutePath().equals(newFilePath)) {
            return ERROR_CODE_INVALID_ARGUMENT;
        }
        File newPath = new File(newFilePath);
        if (newPath.exists()) {
            return ERROR_CODE_FILE_ALREADY_EXIST;
        }

        boolean isSucess = file.renameTo(newPath);
        if (!isSucess) { // java 重命名失败则自己拷贝
            try {
                if(copyFile(srcfile, newFilePath)) {
                    isSucess = FileUtil.deleteFile(srcfile); // 删除源文件
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return isSucess ? ERROR_CODE_RENAME_FILE_SUCCEED : ERROR_CODE_RENAME_FILE_FAILED;
    }

    public static File checkDirAndCreate(String path) {
        if (TextUtils.isEmpty(path)) {
            return null;
        }

        File dir = new File(path);
        if (!dir.exists() || !dir.isDirectory()) {
            dir.delete();
            dir.mkdirs();
        }
        return dir;
    }

    /**
     * 把字符串数组按行逐个写入文件
     * @param fileDir
     * @param fileName
     * @param contents
     */
    public static final void writeFileString(String fileDir, String fileName, String[] contents) {
        if (null == fileDir || null == fileName) {
            return;
        }
        BufferedWriter bw = null;
        FileOutputStream fos = null;
        try {
            File dirFile = new File(fileDir);
            if(!dirFile.exists()) {
                dirFile.mkdirs();
            }
            File file = new File(dirFile,fileName);
            if (!file.exists()) {
                file.createNewFile();
            }
            fos = new FileOutputStream(file);
            bw = new BufferedWriter(new OutputStreamWriter(fos));
            if(null == contents || contents.length <= 0) {
                bw.write("");
            } else{
                int len = contents.length;
                for(int i = 0; i< len; i++){
                    if(null == contents[i]){
                        continue;
                    }
                    bw.write(contents[i]);
                    if(i != len - 1){
                        bw.write("\n");
                    }
                }
            }
        } catch (IOException e) {
        } finally{
            if(null != bw){
                try {
                    bw.close();
                } catch (Exception e2) {
                }
            }
        }
    }

    /**
     * 从文件中按行读取字符串数组（文件必须是小文件）
     * @param filePath
     * @return
     */
    public static final String[] readFileString(String filePath){
        String[] contents = null;
        if(null == filePath){
            return null;
        }
        FileInputStream fis = null;
        BufferedReader br = null;
        ArrayList<String> contentList = null;
        try {
            File file = new File(filePath);
            if(file.exists()){
                fis = new FileInputStream(file);
                br = new BufferedReader(new InputStreamReader(fis));
                contentList = new ArrayList<String>(2);
                for (String line = br.readLine(); line != null; line = br.readLine()) {
                    contentList.add(line);
                }
                contents = new String[contentList.size()];
                contentList.toArray(contents);
            }
        } catch (Exception e) {
        } finally {
            if(null != br){
                try {
                    br.close();
                } catch (Exception e2) {
                }
            }
        }
        return contents;
    }
}
