package com.archzues.lrucachesample.diskcache.fileutils;

/**
 * Created by jansen on 2018/5/18.
 */

public class FileMappingDateTime implements IFileMappingGenerator {
    @Override
    public String getLocalFileName(String key) {
        return key;
    }
}
