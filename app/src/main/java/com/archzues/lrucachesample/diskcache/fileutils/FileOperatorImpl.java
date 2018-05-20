package com.archzues.lrucachesample.diskcache.fileutils;

import android.text.TextUtils;

import java.io.File;

/**
 * Created by jansen on 2018/5/18.
 */

public class FileOperatorImpl implements IFileOperator {
    private IFileMappingGenerator mMappingGen;


    public FileOperatorImpl(String type) {
        mMappingGen = FileMappingFactory.getGenerator(type);
    }

    @Override
    public File get(String key) {
        if (TextUtils.isEmpty(key)) {
            throw new IllegalArgumentException("Key of file to get should not be empty");
        }
        File file = new File(mMappingGen.getLocalFileName(key));
        return file;
    }

    @Override
    public boolean put(String key, File file) {
        if (TextUtils.isEmpty(key) || file == null) {
            throw new IllegalArgumentException("Key or file to write should not be empty");
        }

        return false;
    }

    @Override
    public boolean clear() {
        return false;
    }

    @Override
    public boolean delete(String key) {
        return false;
    }
}
