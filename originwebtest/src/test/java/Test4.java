import org.junit.Test;

import java.util.Arrays;

/**
 * @author keshawn
 * @date 2018/3/20
 */
public class Test4 {

    @Test
    public void printThread() {
        System.out.println(Thread.currentThread().hashCode());
        System.out.println(Thread.currentThread().getId());
        System.out.println(Thread.currentThread().getName());
    }

    @Test
    public void test() {
        int k = 2;
        Integer[] array = {2, 1, 4, 5, 9};
        for (int i = 0; i < array.length; i++) {
            for (int j = i + 1; j <= k; j++) {
                if (array[i] > array[j]) {
                    int temp = array[i];
                    array[i] = array[j];
                    array[j] = temp;
                }
            }
            if (i == k) {
                System.out.println(array[k - 1]);
            }
        }
        System.out.println(Arrays.toString(array));
    }
}
