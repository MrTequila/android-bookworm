package mrtequila.bookworm;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Michal on 2017-09-16.
 */

public class FileCopy {

    public void copyFile(File source, File destination) throws IOException {
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            inputStream = new FileInputStream(source);
            System.out.println("*********************************************************"+ inputStream.toString());
            System.out.println("***********************************************************"+ source.toString());
            outputStream = new FileOutputStream(destination);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }
        } finally {
            inputStream.close();
            outputStream.close();
        }
    }
}
