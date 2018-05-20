package com.archzues.lrucachesample.diskcache.fileutils;

import android.text.TextUtils;

/**
 * Created by jansen on 2018/5/18.
 */

public class FileMappingFactory {

    public static final String TYPE_MD5 = "MD5";
    public static final String TYPE_DATE_TIME = "DATE_TIME";

    public static IFileMappingGenerator getGenerator(String type) {
        if (TextUtils.isEmpty(type)) {
            throw new IllegalArgumentException("Filename mapping type could not be empty!");
        }
        switch (type.toUpperCase()) {
            case TYPE_MD5:
                return new FileMappingMD5();
            case TYPE_DATE_TIME:
                return new FileMappingDateTime();
            default:
                return new FileMappingMD5();
        }
    }

}
