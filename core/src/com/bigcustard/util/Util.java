package com.bigcustard.util;

import com.badlogic.gdx.files.FileHandle;

import java.io.IOException;
import java.io.InputStream;
import java.util.function.Supplier;

public class Util {
    private Util() {}

    public static <T> T tryGet(Supplier<T> supplier, T fallback) {
        try {
            return supplier.get();
        } catch (Exception e) {
            return fallback;
        }
    }

    public static FileHandle importFile(InputStream dataStream, String url, FileHandle destination) {
        try {
            FileHandle mainImageFile = generateFileHandle(url, destination);
            mainImageFile.write(dataStream, false);
            dataStream.close();
            return mainImageFile;
        } catch (IOException e) {
            System.err.println("Error importing image: " + e);
            throw new RuntimeException(e);
        }
    }

    private static FileHandle generateFileHandle(String url, FileHandle folder) {
        String filename = url.substring(url.lastIndexOf("/") + 1);
        filename = filename.contains("?") ? filename.substring(0, filename.indexOf("?")) : filename;
        return findUniqueName(filename, folder);
    }

    private static FileHandle findUniqueName(String filename, FileHandle folder) {
        int dotIndex = filename.lastIndexOf('.');
        String filenameExcludingExtension = filename.substring(0, dotIndex);
        String extension = filename.substring(dotIndex);

        FileHandle candidate = folder.child(filenameExcludingExtension + extension);
        int suffix = 2;
        while (candidate.exists()) {
            candidate = folder.child(filenameExcludingExtension + suffix++ + extension);
        }
        return candidate;
    }

}
