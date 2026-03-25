package sud.utils;

import org.apache.commons.io.FileUtils;
import sud.CurrentContext;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;

public class FileService {

    public static String asBase64(String path) throws IOException {
        byte[] fileContent = Files.readAllBytes(Path.of(path));
        byte[] encoded = Base64.getEncoder().encode(fileContent);
        return new String(encoded,  StandardCharsets.UTF_8);
    }

    public static void main(String arg[]) throws IOException {
        String contains = asBase64(CurrentContext.qrFilePathTmp);
        System.out.println(contains);
    }

    public static void clearTmpDir() {
        String tmpDir = System.getProperty("java.io.tmpdir");
        File tmpFile = new File(tmpDir);
        try {
            FileUtils.cleanDirectory(tmpFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
