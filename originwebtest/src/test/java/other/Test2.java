package other;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.junit.Test;
import star.bean.User;
import star.utils.CastUtil;
import star.utils.UserGenerater;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author keshawn
 * @date 2018/1/9
 */
public class Test2 {

    @Test
    public void testLongAndInt(){
        int a = 2;
        long b = 2L;
        System.out.println(a == CastUtil.castInt(b));
    }

    @Test
    public void testList(){
//        ArrayList<String> l1 = Lists.newArrayList(1, "b", "c", "d");
//        ArrayList<String> l2 = Lists.newArrayList("a", "b", "c", "d", "e");
        ArrayList<Integer> integers = Lists.newArrayList(1, 2, 3, 4);
        ArrayList<Integer> integers2 = Lists.newArrayList(1, 2, 3, 4, 5);
//        integers2.removeAll(integers);
//        System.out.println(integers2);
        integers2.retainAll(integers);
        System.out.println(integers2);
    }

    @Test
    public void testCha(){
        Stream<Integer> integerStream = Stream.generate(() -> 1).limit(10000000);
        long s = System.currentTimeMillis();
        System.out.println(sum(integerStream));
        long e = System.currentTimeMillis();
        System.out.println(e - s);
    }

    public Integer sum(Stream<Integer> stream){

        return stream.mapToInt(Integer::intValue).sum();
    }

    public List<String> getNameAndAge(List<User> users){
        return users.stream()
                .map(user -> user.getName() + "-" + user.getAge())
                .collect(Collectors.toList());
    }

    @Test
    public void testGetNameAndAge(){
        List<User> users = UserGenerater.generateUserList();
        System.out.println(getNameAndAge(users));
    }

    @Test
    public void testInt(){
        getMembers("asdsdaAAADDDLSsdasd");
        ArrayList<String> strings = Lists.newArrayList("a", "b","c","d");
        List<List<String>> partition = Lists.partition(strings, 2);
        System.out.println(partition);

    }

    public int getMembers(String s){

        ArrayList<char[]> chars = Lists.newArrayList(s.toCharArray());
        ImmutableList<Character> characters = Lists.charactersOf(s);
        characters.stream().forEach(System.out::println);
        return 0;
    }

    @Test
    public void testStringList(){
        //Stream<String> stringStream = UserGenerater.generateUserList().stream().map(User::getName);
        Stream<String> stringStream = Stream.of("Aasd","ssd","as");
        Optional<String> max = stringStream
                .peek(System.out::println)
                .max(Comparator.comparing(name -> getCount(name)));
        System.out.println(max.get());
    }

    private long getCount(String name) {
        System.out.println(name);
        long count = Lists.charactersOf(name).stream().filter(it -> it <= 'z' && it >= 'a').count();
        System.out.println(count);
        return count;
    }
}

