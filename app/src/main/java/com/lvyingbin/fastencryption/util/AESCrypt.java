package com.lvyingbin.fastencryption.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by justin on 2015/3/8.
 */
public class AESCrypt {
    private static String TYPE = "AES";
    private static int KeySizeAES128 = 16;
    private static int BUFFER_SIZE = 8192;

    public static Cipher getCipher(int mode, String key) {
        //mode =Cipher.DECRYPT_MODE or Cipher.ENCRYPT_MODE
        Cipher mCipher;
        byte[] keyPtr = new byte[KeySizeAES128];
        IvParameterSpec ivParam = new IvParameterSpec(keyPtr);
        byte[] passPtr = key.getBytes();
        try {
            mCipher = Cipher.getInstance(TYPE + "/CBC/PKCS5Padding");
            for (int i = 0; i < KeySizeAES128; i++) {
                if (i < passPtr.length) keyPtr[i] = passPtr[i];
                else keyPtr[i] = 0;
            }
            SecretKeySpec keySpec = new SecretKeySpec(keyPtr, TYPE);
            mCipher.init(mode, keySpec, ivParam);
            return mCipher;
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 解密文件
     *
     * @param srcFile
     * @param destFile
     * @param privateKey
     */
    public static void decrypt(String srcFile, String destFile, String privateKey) {

        byte[] readBuffer = new byte[BUFFER_SIZE];
        Cipher deCipher = getCipher(Cipher.DECRYPT_MODE, privateKey);
        if (deCipher == null) return; //init failed.

        CipherInputStream fis = null;
        BufferedOutputStream fos = null;
        int size;
        try {
            fis = new CipherInputStream(
                    new BufferedInputStream(
                            new FileInputStream(srcFile)), deCipher);
            fos = new BufferedOutputStream(
                    new FileOutputStream(parentDirMake(destFile)));
            while ((size = fis.read(readBuffer, 0, BUFFER_SIZE)) >= 0) {
                fos.write(readBuffer, 0, size);
            }
            fos.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                }
            }
            if (fos != null) {
                try {
                    fos.flush();
                } catch (IOException e) {
                }
                try {
                    fos.close();
                } catch (IOException e) {
                }
            }
        }
    }

    /**
     * 加密文件
     *
     * @param srcFile
     * @param destFile
     * @param privateKey
     */
    public static void crypt(String srcFile, String destFile, String privateKey) {

        byte[] readBuffer = new byte[BUFFER_SIZE];
        Cipher enCipher = getCipher(Cipher.ENCRYPT_MODE, privateKey);
        if (enCipher == null) return; //init failed.

        CipherOutputStream fos = null;
        BufferedInputStream fis = null;
        int size;
        try {
            fos = new CipherOutputStream(
                    new BufferedOutputStream(
                            new FileOutputStream(destFile)), enCipher);
            fis = new BufferedInputStream(
                    new FileInputStream(parentDirMake(srcFile)));
            while ((size = fis.read(readBuffer, 0, BUFFER_SIZE)) >= 0) {
                fos.write(readBuffer, 0, size);
            }
            fos.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                }
            }
            if (fos != null) {
                try {
                    fos.flush();
                } catch (IOException e) {
                }
                try {
                    fos.close();
                } catch (IOException e) {
                }
            }
        }
    }

    private static File parentDirMake(String filePath) throws IOException {
        File file = new File(filePath);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        file.createNewFile();

        return file;
    }

    public static void main(String[] args) {
        AESCrypt aes = new AESCrypt();
        aes.crypt("/Users/liaomin/Documents/计算机原理.pdf", "/Users/liaomin/Documents/加密_计算机原理.pdf", "111");
        aes.decrypt("/Users/liaomin/Documents/加密_计算机原理.pdf", "/Users/liaomin/Documents/解密_计算机原理.pdf", "111");
    }
}
