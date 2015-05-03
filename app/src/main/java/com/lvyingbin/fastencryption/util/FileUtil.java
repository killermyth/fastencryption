package com.lvyingbin.fastencryption.util;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by justin on 2015/2/25.
 */
public class FileUtil {
    private static void moveFile(String inputPath, String inputFile, String outputPath) {
        InputStream in = null;
        OutputStream out = null;
        try {
            //create output directory if it doesn't exist
            File dir = new File(outputPath);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            in = new FileInputStream(inputPath + inputFile);
            out = new FileOutputStream(outputPath + inputFile);

            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            in.close();
            in = null;
            // write the output file
            out.flush();
            out.close();
            out = null;
            // delete the original file
            new File(inputPath + inputFile).delete();
        } catch (FileNotFoundException fnfe1) {
            Log.e("tag", fnfe1.getMessage());
        } catch (Exception e) {
            Log.e("tag", e.getMessage());
        }
    }

    public static boolean isMounted()
    {
        return Environment.getExternalStorageState().equals("mounted");
    }

    public static String getSdDir()
    {
        if (!isMounted()) {
            return null;
        }
        return Environment.getExternalStorageDirectory().getAbsolutePath();
    }

    /**
     * 获取对应文件夹路径
     * @param type 文件夹类型，0主目录，1图片目录，2视频目录，文件目录
     * @return 绝对路径
     */
    public static String getDirPath(int type){
        String Path="";
        switch(type){
            case 0:
                Path = getSdDir() + FastEncryption.PRIVACY_PATH;
                break;
            case 1:
                Path = getSdDir() + FastEncryption.PRIVACY_PATH + FastEncryption.PRIVACY_IMG;
                break;
            case 2:
                Path = getSdDir() + FastEncryption.PRIVACY_PATH + FastEncryption.PRIVACY_VIDEO;
                break;
            case 3:
                Path = getSdDir() + FastEncryption.PRIVACY_PATH + FastEncryption.PRIVACY_FILE;
                break;
            default:
                Path = getSdDir() + FastEncryption.PRIVACY_PATH;
                break;
        }
        return Path;
    }

}
