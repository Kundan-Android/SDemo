package com.caliber.shwaasdemo.Utils;

import android.content.Context;
import android.util.Log;

import java.io.File;

/**
 * Created by Caliber on 19-01-2018.
 */

public class ExternalStoragePath {

    private ExternalStoragePath(){
        throw new IllegalStateException("Utility Class. Cannot instantiate");
    }

    public static File getExternalCardPath(Context context) {

        File[] listOfInternalAndExternalStorage = context.getExternalFilesDirs(null);
        File externalCardPath = null;
        if(  /*listOfInternalAndExternalStorage != null &&*/ listOfInternalAndExternalStorage.length >= 1 ) {
            if (listOfInternalAndExternalStorage[0] != null)
                externalCardPath = listOfInternalAndExternalStorage[0];
            else
                externalCardPath = listOfInternalAndExternalStorage[0];
            Log.d("DataStreaming", externalCardPath.getAbsolutePath());
        }

        return externalCardPath;
    }
}

