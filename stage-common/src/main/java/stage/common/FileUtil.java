package stage.common;

import java.io.*;
import java.nio.charset.StandardCharsets;

import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.IOUtils;

/**
 * {@link FileUtil} provides some handy functions for working with files.
 *
 * @author Julian Drees
 * @author Tobias Fuchs
 * @author Yannick Kirschen
 * @author Cevin Steve Oehne
 * @author Tobias Tappert
 * @since 1.0.0
 */
@Log4j2
public final class FileUtil {
    private static final ClassLoader LOADER = FileUtil.class.getClassLoader();

    private FileUtil() {
    }

    /**
     * Reads a (text) file entirely into a string. The charset of the file must
     * be UTF-8.
     *
     * @param file The complete name of the file.
     * @return The file's content as a string.
     */
    public static String readFile(String file) {
        try (InputStream inputStream = LOADER.getResourceAsStream(file)) {
            assert inputStream != null;
            return IOUtils.toString(inputStream, StandardCharsets.UTF_8.name());
        } catch (IOException e) {
            log.error("File {} not found: {}", file, e.getMessage());
            throw new StartupException();
        }
    }
}
