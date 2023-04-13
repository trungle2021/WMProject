package wm.clientmvc.utils;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class ClientUtilFunction {
    public static List<String> AddMultipleFilesEncrypted(MultipartFile[] files) {
        List<String> encodedFiles = new ArrayList<>();
        for (MultipartFile file : files
        ) {
            try {
                String encodedFile = Base64.getEncoder().encodeToString(file.getBytes());
                encodedFiles.add(encodedFile);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return encodedFiles;
    }

    public static String AddFileEncrypted(MultipartFile file) throws IOException {
        String encodedFile = Base64.getEncoder().encodeToString(file.getBytes());
        return encodedFile;
    }

}
