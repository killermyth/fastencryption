package com.lvyingbin.fastencryption.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by justin on 2015/3/7.
 */
public class BitmapUtil {
    public static Bitmap getBitmapFromPath(String bitmapPath) {
        File bitmapFile = new File(bitmapPath);
        if (bitmapFile.exists()) {
            Bitmap bitmap;
            try {
                bitmap = BitmapFactory.decodeStream(new FileInputStream(bitmapFile));
            } catch (FileNotFoundException filenotfoundexception) {
                return null;
            }
            return bitmap;
        } else {
            return null;
        }
    }

    public static void saveBitmapToPath(String s, Bitmap bitmap)
    {
        File file;
        if (s != null && bitmap != null)
        {
            if (!(file = new File(s)).exists())
            {
                try
                {
                    FileOutputStream fileoutputstream = new FileOutputStream(file);
                    BufferedOutputStream bufferedoutputstream = new BufferedOutputStream(fileoutputstream);
                    bitmap.compress(android.graphics.Bitmap.CompressFormat.JPEG, 100, bufferedoutputstream);
                    fileoutputstream.close();
                    bufferedoutputstream.flush();
                    bufferedoutputstream.close();
                    return;
                }
                catch (FileNotFoundException filenotfoundexception)
                {
                    filenotfoundexception.printStackTrace();
                    return;
                }
                catch (IOException ioexception)
                {
                    ioexception.printStackTrace();
                }
                return;
            }
        }
    }
}
