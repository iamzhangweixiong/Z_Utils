
package com.zhangwx.z_utils.Z_Pub;

import android.text.TextUtils;
import android.util.Base64;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AesUtil {
    public static final String DEFAULT_KEY = "202CBba7b7ab7a7a7bB07152D234B70";
    public static final String DEFAULT_IV = "D1D99Cac8ac8a8c8aca8CCA4B635DBF1";

    public static boolean decryptFile(InputStream sourceInputStream,
                                      String targetFilePath, String key, String iv) {
        return AESCipher(Cipher.DECRYPT_MODE, sourceInputStream, targetFilePath, key, iv);
    }

    public static boolean encryptFile(InputStream inputStream, String targetFilePath, String key, String iv) {
        return AESCipher(Cipher.ENCRYPT_MODE, inputStream, targetFilePath, key, iv);
    }

    public static byte[] encryptAES(byte[] content, byte[] key, byte[] iv) {
        try {
            String aesKey = (key == null) ? DEFAULT_KEY : String.valueOf(key);
            String aesIv = (iv == null) ? DEFAULT_IV : String.valueOf(iv);
            Cipher cipher = initCipher(Cipher.ENCRYPT_MODE, aesKey, aesIv);
            if(cipher != null) {
                byte[] encrypted = cipher.doFinal(content);
                return encrypted;
            }
        } catch (Exception ex) {
        }
        return null;
    }

    public static byte[] decryptAES(byte[] content, byte[] key, byte[] iv) {
        try {
            String aesKey = (key == null) ? DEFAULT_KEY : String.valueOf(key);
            String aesIv = (iv == null) ? DEFAULT_IV : String.valueOf(iv);
            Cipher cipher = initCipher(Cipher.DECRYPT_MODE, aesKey, aesIv);
            byte[] decrypted = cipher.doFinal(content);
            return decrypted;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }


    private static boolean AESCipher(int cipherMode, InputStream sourceInputStream,
                                     String targetFilePath, String key, String iv) {
        boolean result = false;
        FileOutputStream targetOutputStream = null;
        try {
            if (cipherMode != Cipher.ENCRYPT_MODE
                    && cipherMode != Cipher.DECRYPT_MODE) {
                return false;
            }
            Cipher mCipher = initCipher(cipherMode, key, iv);

            File targetFile = new File(targetFilePath);
            if (!targetFile.getParentFile().exists()) {
                targetFile.getParentFile().mkdirs();
            }
            targetOutputStream = new FileOutputStream(targetFile);

            InputStream cipherInputStream = new CipherInputStream(
                sourceInputStream, mCipher);
            byte[] buffer = new byte[2048];
            int read = cipherInputStream.read(buffer);
            while(read >= 0){
                targetOutputStream.write(buffer,0,read);
                read = cipherInputStream.read(buffer);
            }

            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (sourceInputStream != null) {
                    sourceInputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (targetOutputStream != null) {
                    targetOutputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    private static byte[] hexStringToByteArray(String s){
        int len = s.length();
        byte[] data = new byte[len/2];

        for (int i = 0; i < len; i+=2) {
            data[i/2] = (byte)((Character.digit(s.charAt(i), 16) << 4)
                                + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }

    private static Cipher initCipher(int cipherMode, String key, String iv) {
        Cipher cipher = null;
        try {
            final byte[] secretKey = hexStringToByteArray(key);
            final byte[] initVector = hexStringToByteArray(iv);

            cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(cipherMode, 
                new SecretKeySpec(secretKey, "AES"),
                new IvParameterSpec(initVector, 0, cipher.getBlockSize()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cipher;
    }



    public static Cipher initCipher(int cipherMode, SecretKeySpec key, AlgorithmParameterSpec iv) {
        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(cipherMode, key,iv);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cipher;
    }


    public static String encrypt(final String plainText, final String seed, final byte[] iv) throws Exception {
        Cipher cipher = initCipher(Cipher.ENCRYPT_MODE, getKey(seed), getIV(iv));
        byte[] encrypted = cipher.doFinal(plainText.getBytes("UTF-8"));
        String encryptedText = new String(Base64.encode(encrypted,
                Base64.DEFAULT), "UTF-8");
        return encryptedText;
    }

    public static String decrypt(final String cryptedText, final String seed, final byte[] iv) throws Exception {
        Cipher cipher = initCipher(Cipher.DECRYPT_MODE, getKey(seed), getIV(iv));
        byte[] bytes = Base64.decode(cryptedText, Base64.DEFAULT);
        byte[] decrypted = cipher.doFinal(bytes);
        String decryptedText = new String(decrypted, "UTF-8");
        return decryptedText;
    }


    public static SecretKeySpec getKey(final String seed) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        digest.update(seed.getBytes("UTF-8"));
        byte[] keyBytes = new byte[32];
        System.arraycopy(digest.digest(), 0, keyBytes, 0, keyBytes.length);
        return new SecretKeySpec(keyBytes, "AES");
    }

    public static AlgorithmParameterSpec getIV(byte[] iv) {
        IvParameterSpec ivParameterSpec;
        ivParameterSpec = new IvParameterSpec(iv);
        return ivParameterSpec;
    }

    public static String encodeAid(String aid) {
        String str = "";
        try {
            if (TextUtils.isEmpty(aid)) {
                return "";
            }
            String encodeAid = Base64.encodeToString(decodeHex(aid.toCharArray()), Base64.NO_WRAP);
            str = encodeAid.replace('=', '-');
        } catch (Exception e) {}
        return str;
    }

    private static byte[] decodeHex(final char[] data) {
        final int len = data.length;

        if ((len & 0x01) != 0) {
            throw new NumberFormatException("decodeHex (len & 0x01) != 0");
        }

        final byte[] out = new byte[len >> 1];

        // two characters form the hex value.
        for (int i = 0, j = 0; j < len; i++) {
            int f = toDigit(data[j], j) << 4;
            j++;
            f = f | toDigit(data[j], j);
            j++;
            out[i] = (byte) (f & 0xFF);
        }

        return out;
    }

    private static int toDigit(final char ch, final int index) {
        final int digit = Character.digit(ch, 16);
        if (digit == -1) {
            throw new NumberFormatException("Illegal hexadecimal character " + ch + " at index " + index);
        }
        return digit;
    }

    private static final byte[] IV = {
            (byte) 0x81, (byte) 0x29, (byte) 0x9C, (byte) 0xA9,
            (byte) 0x87, (byte) 0xCC, (byte) 0x07, (byte) 0x08,
            (byte) 0xA8, (byte) 0xDE, (byte) 0xCC, (byte) 0xA4,
            (byte) 0xC6, (byte) 0xA5, (byte) 0xDB, (byte) 0xF1};

    public static String getEncryptCorpus(String content) {
        try {
            return AesUtil.encrypt(content, "SSSSSSSS", IV);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }


    public static String getDecryptCorpus(String content) {
        try {
            return AesUtil.decrypt(content, "SSSSSSSS", IV);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
