package com.archzues.lrucachesample.diskcache.fileutils;

import android.graphics.Bitmap;

import java.io.File;
import java.io.IOException;

/**
 * Created by jansen on 2018/5/18.
 */

public interface IFileOperator {


    File get(String key);

    boolean save(String key, Bitmap bitmap) throws IOException;

    boolean clear();

    boolean delete(String key);

}
