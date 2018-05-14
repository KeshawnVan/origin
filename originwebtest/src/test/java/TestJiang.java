import org.junit.Test;

import java.util.Arrays;
import java.util.Random;

public class TestJiang {

    @Test
    public void odd() {
        // 1.生成一个随机数组
        Integer[] randomArray = getRandomArray(10);
        // 2.传入一个数组，将奇数放到左侧，偶数放到右侧
        Integer[] sortedArray = getSortedArray(randomArray);
        System.out.println(Arrays.toString(sortedArray));

    }

    /**
     * 生成一个长度为size的随机整数数组
     *
     * @param size
     * @return
     */
    public Integer[] getRandomArray(Integer size) {
        Random random = new Random();
        Integer[] randomArray = new Integer[size];

        for (int i = 0; i < size; i++) {
            randomArray[i] = random.nextInt(100);
        }
        return randomArray;
    }

    /**
     * 传入一个数组，将奇数放到左侧，偶数放到右侧
     */
    public Integer[] getSortedArray(Integer[] randomArray) {
        Integer[] sortedArray = new Integer[randomArray.length];

        for (Integer integer : randomArray) {
            if (integer % 2 == 0) {
                insertByRight(sortedArray, integer);
            } else {
                insertByLeft(sortedArray, integer);
            }
        }
        return sortedArray;
    }

    public void insertByLeft(Integer[] array, Integer num) {
        for (int i = 0; i < array.length; i++) {
            Integer integer = array[i];
            if (integer == null) {
                array[i] = num;
                break;
            }
        }
    }

    public void insertByRight(Integer[] array, Integer num) {
        for (int i = array.length - 1; i >= 0; i--) {
            Integer integer = array[i];
            if (integer == null) {
                array[i] = num;
                break;
            }
        }
    }
}
