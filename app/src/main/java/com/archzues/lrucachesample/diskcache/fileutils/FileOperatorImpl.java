package com.archzues.lrucachesample.diskcache.fileutils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.text.TextUtils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import static android.os.Environment.MEDIA_MOUNTED;

/**
 * Created by jansen on 2018/5/18.
 */

public class FileOperatorImpl implements IFileOperator {

    public static final int DEFAULT_BUFFER_SIZE = 32 * 1024; // 32 Kb

    private static final String EXTERNAL_STORAGE_PERMISSION = "android.permission.WRITE_EXTERNAL_STORAGE";
    private static final String TEMP_IMAGE_POSTFIX = ".tmp";
    public static final Bitmap.CompressFormat DEFAULT_COMPRESS_FORMAT = Bitmap.CompressFormat.PNG;
    public static final int DEFAULT_COMPRESS_QUALITY = 100;
    private IFileMappingGenerator mMappingGen;
    private File mCacheDir;


    public FileOperatorImpl(Context context, String type) {
        mMappingGen = FileMappingFactory.getGenerator(type);
        mCacheDir = getExternalCacheDir(context);
    }

    @Override
    public File get(String key) {
        if (TextUtils.isEmpty(key)) {
            throw new IllegalArgumentException("Key of file to get should not be empty");
        }
        String fileName = mMappingGen.getLocalFileName(key);
        if (!mCacheDir.exists() && !mCacheDir.mkdir()) {
            return null;
        }
        return new File(mCacheDir, fileName);
    }

    @Override
    public boolean save(String key, Bitmap bitmap) throws IOException {
        if (TextUtils.isEmpty(key) || bitmap == null) {
            throw new IllegalArgumentException("Key or bitmap to write should not be empty");
        }
        File imageFile = get(key);
        File tmpFile = new File(imageFile.getAbsolutePath() + TEMP_IMAGE_POSTFIX);
        OutputStream os = new BufferedOutputStream(new FileOutputStream(tmpFile), DEFAULT_BUFFER_SIZE);
        boolean savedSuccessfully = false;
        try {
            savedSuccessfully = bitmap.compress(DEFAULT_COMPRESS_FORMAT, DEFAULT_COMPRESS_QUALITY, os);
        } finally {
            os.close();
            if (savedSuccessfully && !tmpFile.renameTo(imageFile)) {
                savedSuccessfully = false;
            }
            if (!savedSuccessfully) {
                tmpFile.delete();
            }
        }
        bitmap.recycle();
        return savedSuccessfully;
    }

    @Override
    public boolean clear() {
        return false;
    }

    @Override
    public boolean delete(String key) {
        return false;
    }

    public Bitmap getBitmap(String Key) {
        Bitmap bitmap = BitmapFactory.decodeFile(get(Key).getAbsolutePath());
        return bitmap;
    }

    public static File getCacheDirectory(Context context, boolean preferExternal) {
        File appCacheDir = null;
        String externalStorageState;
        try {
            externalStorageState = Environment.getExternalStorageState();
        } catch (NullPointerException e) {
            externalStorageState = "";
        } catch (IncompatibleClassChangeError e) {
            externalStorageState = "";
        }
        if (preferExternal && MEDIA_MOUNTED.equals(externalStorageState) && hasExternalStoragePermission(context)) {
            appCacheDir = getExternalCacheDir(context);
        }
        if (appCacheDir == null) {
            appCacheDir = context.getCacheDir();
        }
        if (appCacheDir == null) {
            String cacheDirPath = "/data/data/" + context.getPackageName() + "/cache/";
            appCacheDir = new File(cacheDirPath);
        }
        return appCacheDir;
    }


    private static File getExternalCacheDir(Context context) {
        File dataDir = new File(new File(Environment.getExternalStorageDirectory(), "Android"), "data");
        File appCacheDir = new File(new File(dataDir, context.getPackageName()), "cache");
        if (!appCacheDir.exists()) {
            if (!appCacheDir.mkdirs()) {
                return null;
            }
            try {
                new File(appCacheDir, ".nomedia").createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return appCacheDir;
    }

    private static boolean hasExternalStoragePermission(Context context) {
        int perm = context.checkCallingOrSelfPermission(EXTERNAL_STORAGE_PERMISSION);
        return perm == PackageManager.PERMISSION_GRANTED;
    }

}
