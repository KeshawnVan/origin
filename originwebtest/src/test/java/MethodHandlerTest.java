import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

public class MethodHandlerTest {

    static class StarPrinter {
        public void println(String s) {
            System.out.printf("StarPrinter: %s", s);
        }
    }

    public static MethodHandle getMethodHandler(Object receiver) {
        MethodType methodType = MethodType.methodType(void.class, String.class);
        try {
            return MethodHandles.lookup().findVirtual(receiver.getClass(), "println", methodType).bindTo(receiver);
        } catch (NoSuchMethodException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws Throwable {
        Object o = System.currentTimeMillis() % 2 == 0 ? System.out : new StarPrinter();
        getMethodHandler(o).invokeExact("invoke");
    }
}
