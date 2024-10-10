package itstep.learning.services.storage;

import com.google.inject.Singleton;
import org.apache.commons.fileupload.FileItem;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

@Singleton
public class LocalStorageSerivce implements StorageService {
    private final static String storagePath = "C:/storage/Java213";

    @Override
    public File getFile(String fileName) {
        return null;
    }

    @Override
    public String saveFile(FileItem fileItem, String baseName) throws IOException {
        if (fileItem == null) {
            throw new IOException("FileItem is null");


        }
        if (fileItem.getSize() == 0) {
            throw new IOException("FileItem is empty");


        }

        String fileName = fileItem.getName();
        if (fileName == null) {
            throw new IOException( "FileItem has no name" );

        }
        // виділяємо розширення з початкового імені файлу
        int dotIndex = fileName.lastIndexOf( '.' );
        if( dotIndex == -1 ) {
            throw new IOException("FileItem has no extension");
        }
            String extension = fileName. substring( dotIndex );
        if(".".equals(extension)) {
            throw new IOException("FileItem has empty extension");
        }
        // генеруємо нове ім'я файлу, перевіряємо, що такого немає у сховищі
        String savedName;
        File file;
        return "";
    }
}