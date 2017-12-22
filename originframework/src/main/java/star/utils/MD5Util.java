package star.utils;

import java.security.NoSuchAlgorithmException;

/**
 * @author keshawn
 * @date 2017/11/8
 */

public final class MD5Util {

    private static final String MD5 = "MD5";

    private MD5Util() {
    }

    /**
     * MD5 摘要计算(String).
     *
     * @param src String
     * @return String
     */
    public static String md5Digest(String src) {
        try {
            return StringUtil.toHex(md5Digest(src.getBytes()));
        } catch (NoSuchAlgorithmException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * MD5 摘要计算(byte[]).
     *
     * @param src byte[]
     * @return byte[] 16 bit digest
     * @throws NoSuchAlgorithmException 异常
     */
    public static byte[] md5Digest(byte[] src) throws NoSuchAlgorithmException {
        java.security.MessageDigest alg = java.security.MessageDigest.getInstance(MD5);
        return alg.digest(src);
    }
}
