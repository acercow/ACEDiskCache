package com.archzues.lrucachesample.diskcache.fileutils;

/**
 * Created by jansen on 2018/5/18.
 */

public class FileMappingMD5 implements IFileMappingGenerator {

    @Override
    public String getLocalFileName(String key) {

        return key;
    }
}
