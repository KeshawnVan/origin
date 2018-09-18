package star.utils;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.List;

public final class FileUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileUtil.class);

    private FileUtil() {
    }

    public static File createFile(String filePath) {
        File file;
        try {
            file = new File(filePath);
            File parentFile = file.getParentFile();
            if (!parentFile.exists()) {
                FileUtils.forceMkdir(parentFile);
            }
        } catch (Exception e) {
            LOGGER.error("create file failure", e);
            throw new RuntimeException(e);
        }
        return file;
    }

    public static void mkdir(String path) {
        mkdir(createFile(path));
    }

    public static void mkdir(File path) {
        try {
            FileUtils.forceMkdir(path);
        } catch (Exception e) {
            LOGGER.error(String.format("mkdir %s error", path), e);
        }
    }

    public static void moveFile(File srcFile, File destFile) {
        try {
            FileUtils.moveFile(srcFile, destFile);
        } catch (Exception e) {
            LOGGER.error("moveFile error", e);
            throw new RuntimeException(e);
        }
    }

    public static void moveFile(String srcFile, String destFile) {
        moveFile(createFile(srcFile), createFile(destFile));
    }

    public static void moveFileToDirectory(File srcFile, File destDir) {
        try {
            FileUtils.moveFileToDirectory(srcFile, destDir, true);
        } catch (IOException e) {
            LOGGER.error("moveFileToDirectory error", e);
        }
    }

    public static void moveFileToDirectory(String srcFile, String destDir) {
        moveFileToDirectory(createFile(srcFile), createFile(destDir));
    }

    public static List<String> readLines(File file) {
        try {
            return FileUtils.readLines(file);
        } catch (IOException e) {
            LOGGER.error("readLines error", e);
            throw new RuntimeException(e);
        }
    }

    public static List<String> readLines(String file) {
        return readLines(createFile(file));
    }

    public static String readFileToString(String file) {
        try {
            return FileUtils.readFileToString(createFile(file));
        } catch (IOException e) {
            LOGGER.error("readFileToString error ", e);
            throw new RuntimeException(e);
        }
    }

    public static byte[] readFileToByteArray(String file) {
        try {
            return FileUtils.readFileToByteArray(createFile(file));
        } catch (IOException e) {
            LOGGER.error("readFileToByteArray error ", e);
            throw new RuntimeException(e);
        }
    }

    public static void copyFileToDirectory(File srcFile, File destDir) {
        try {
            FileUtils.copyFileToDirectory(srcFile, destDir, true);
        } catch (IOException e) {
            LOGGER.error("copyFileToDirectory error", e);
        }
    }

    public static void copyFileToDirectory(String srcFile, String destDir) {
        copyFileToDirectory(createFile(srcFile), createFile(destDir));
    }

    public static boolean isExist(String filePath) {
        Boolean flag = false;
        try {
            File file = new File(filePath);
            if (file.exists() && file.isFile()) {
                flag = true;
            }
        } catch (Exception e) {
            LOGGER.info("path:" + filePath + "error", e);
        }
        return flag;
    }

    public static boolean isDirExist(String path) {
        Boolean flag = false;
        try {
            File file = new File(path);
            if (file.exists() && file.isDirectory()) {
                flag = true;
            }
        } catch (Exception e) {
            LOGGER.info("path:" + path + "error", e);
        }
        return flag;
    }

    public static void writeString(String content, String path, String encoding) {
        try {
            FileUtils.writeStringToFile(createFile(path), content, encoding);
        } catch (IOException e) {
            LOGGER.error("writeString error", e);
            throw new RuntimeException(e);
        }
    }

    public static void writeString(String content, String path) {
        try {
            FileUtils.writeStringToFile(createFile(path), content);
        } catch (IOException e) {
            LOGGER.error("writeString error", e);
            throw new RuntimeException(e);
        }
    }

    public static void remove(String filePath) {
        try {
            FileUtils.forceDelete(createFile(filePath));
        } catch (IOException e) {
            LOGGER.error("remove error", e);
            throw new RuntimeException(e);
        }
    }
}
