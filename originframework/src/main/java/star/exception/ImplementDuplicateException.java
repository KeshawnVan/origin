package star.exception;

/**
 * @author keshawn
 * @date 2017/11/10
 */
public class ImplementDuplicateException extends RuntimeException {
    private String message;

    public ImplementDuplicateException() {
    }

    public ImplementDuplicateException(String message) {
        this.message = message;
    }
}
