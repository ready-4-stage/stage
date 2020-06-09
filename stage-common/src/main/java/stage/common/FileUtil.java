package stage.common;

import java.io.*;
import java.nio.charset.StandardCharsets;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.IOUtils;

@Log4j2
public final class FileUtil {
    private FileUtil() {
    }

    public static String readFile(String file) {
        try {
            InputStream inputStream = FileUtil.class.getClassLoader().getResourceAsStream(
                file);
            assert inputStream != null;
            return IOUtils.toString(inputStream, StandardCharsets.UTF_8.name());
        } catch (IOException e) {
            log.error("File {} not found: {}", file, e.getMessage());
            throw new StartupException();
        }
    }
}
