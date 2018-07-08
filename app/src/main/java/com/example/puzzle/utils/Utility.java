package com.example.puzzle.utils;

import android.content.Context;
import android.graphics.Bitmap;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.ResponseBody;

public class Utility {
    static public int REQUEST_PERMISSION_CODE = 1;
    static public int REQUEST_CHOOSE_PICTURE_CODE = 2;

    static public String saveBitmap(Context context, String name, Bitmap bitmap) {
        String path = getCachePath(context) + name;
        File file = new File(path);
        FileOutputStream out;
        try {
            out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
            return path;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    static public String savePicture(Context context, String name, ResponseBody responseBody) {
        String filePath = getCachePath(context) + name;
        try {
            File file = new File(filePath);
            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {
                byte[] fileReader = new byte[4096];
                long fileSizeDownloaded = 0;
                long fileSize = responseBody.contentLength();
                inputStream = responseBody.byteStream();
                outputStream = new FileOutputStream(file);

                while (true) {
                    int read = inputStream.read(fileReader);
                    if (read == -1) {
                        break;
                    }

                    outputStream.write(fileReader, 0, read);
                    fileSizeDownloaded += read;
                }

                outputStream.flush();
                return filePath;
            } catch (IOException e) {
                return null;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            return null;
        }
    }

    private static String getCachePath(Context context) {
        return context.getCacheDir().getAbsolutePath() + File.separator;
    }
}
