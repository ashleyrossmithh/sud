package sud.core.rest.file;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@RestController
@RequestMapping("/api/file/download")
@Log4j2
public class DownloadFileResource {
    private static final String TEMP_DIR = System.getProperty("java.io.tmpdir");

    private static final int BUFFER_SIZE = 1024 * 1024 * 4;


    @GetMapping(value = "/{id}/{part}")
    public ResponseEntity<byte[]> getFile(@PathVariable(value = "id") String fileName, @PathVariable(value = "part") Integer part) throws IOException {
        return getFile("temp", fileName, part);
    }

    @GetMapping(value = "/{storage}/{id}/{part}")
    public ResponseEntity<byte[]> getFile(@PathVariable String storage, @PathVariable(value = "id") String fileName, @PathVariable(value = "part") Integer part) throws IOException {
        File file = new File(TEMP_DIR + "/" + fileName); //NOSONAR
        if (!file.exists() || !file.isFile()) {
            return ResponseEntity.notFound().build();
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-File-Size", String.valueOf(file.length()));
        headers.add("X-Buff-Size", String.valueOf(BUFFER_SIZE));
        if (part < 0) {
            return ResponseEntity.status(201).headers(headers).build();
        }
        if (part > ((file.length() / BUFFER_SIZE) + 1)) {
            return ResponseEntity.badRequest().build();
        }
        try (FileInputStream fis = new FileInputStream(file)) {
            fis.skip((long) BUFFER_SIZE * part); //NOSONAR
            long newPart = file.length() - (long) BUFFER_SIZE * part;
            final int currentPart = newPart > BUFFER_SIZE ? BUFFER_SIZE : (int) newPart;
            final byte[] buff = new byte[currentPart];
            fis.read(buff, 0, currentPart);
            boolean isLastPart = (long) BUFFER_SIZE * part + currentPart == file.length();
            return ResponseEntity.status(isLastPart ? 200 : 201)
                    .headers(headers)
                    .contentLength(currentPart)
                    .contentType(MediaType.parseMediaType("application/octet-stream"))
                    .body(buff);
        }
    }
}
