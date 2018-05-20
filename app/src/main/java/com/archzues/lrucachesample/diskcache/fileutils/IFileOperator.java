package com.archzues.lrucachesample.diskcache.fileutils;

import java.io.File;

/**
 * Created by jansen on 2018/5/18.
 */

public interface IFileOperator {


    File get(String key);

    boolean put(String key, File file);

    boolean clear();

    boolean delete(String key);

}
