package utils;

import java.io.File;

public class FileUtils {

    public static String getFileNameWithoutExtension(File file) {
        return file.getName().substring(0, file.getName().lastIndexOf("."));
    }
}
