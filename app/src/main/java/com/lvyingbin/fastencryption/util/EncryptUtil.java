package com.lvyingbin.fastencryption.util;

/**
 * Created by justin on 2015/3/7.
 */
public class EncryptUtil {
    //xor encrypt
    public static byte[] byteEncrypt(byte bytes[])
    {
        int byteLength = bytes.length;
        for (int i = 0; i < byteLength; i++)
        {
            bytes[i] = (byte)(0x6b ^ bytes[i]);
        }
        return bytes;
    }
}
