package star.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Optional;

/**
 * @author keshawn
 * @date 2017/11/17
 */
public final class StreamUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(StreamUtil.class);

    private StreamUtil() {
    }

    public static String getString(InputStream inputStream) {
        StringBuilder stringBuilder = new StringBuilder();
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }
        } catch (IOException e) {
            LOGGER.error("get string failure", e);
            throw new RuntimeException(e);
        }
        return stringBuilder.toString();
    }

    public static byte[] getByte(InputStream is) throws Exception {
        byte[] buffer = new byte[1024];
        int len;
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            while ((len = is.read(buffer)) != -1) {
                bos.write(buffer, 0, len);
            }
            bos.flush();
            return Optional.ofNullable(bos.toByteArray()).orElseGet(() -> new byte[0]);
        }
    }
}
