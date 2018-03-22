import org.junit.Test;

/**
 * @author keshawn
 * @date 2018/3/20
 */
public class Test4 {

    @Test
    public void printThread(){
        System.out.println(Thread.currentThread().hashCode());
        System.out.println(Thread.currentThread().getId());
        System.out.println(Thread.currentThread().getName());
    }
}
