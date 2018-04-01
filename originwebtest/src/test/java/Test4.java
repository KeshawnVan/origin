import com.google.common.collect.Lists;
import org.junit.Test;
import star.bean.ClassInfo;
import star.bean.Node;
import star.bean.User;
import star.bean.UserDTO;
import star.core.LoadCore;
import star.factory.BeanFactory;
import star.factory.ClassFactory;
import star.proxy.TransactionProxy;
import star.utils.JsonUtil;
import star.utils.NumberUtil;
import star.utils.ReflectionUtil;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

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

    @Test
    public void testDate(){
        LocalDate start = LocalDate.of(2018, 3, 16);
        LocalDate end = LocalDate.of(2018, 6, 30);
        Long unAccountDays = ChronoUnit.DAYS.between(start, end) + 1L;
        Double avgFee = 9600D / unAccountDays;
        System.out.println(avgFee);
        System.out.println(unAccountDays);
        LocalDate now = LocalDate.now();
        LocalDate releaseStartDay = LocalDate.now().plusDays(1);
        Long releaseDays = ChronoUnit.DAYS.between(releaseStartDay, end) + 1L;
        Double sum = 11111305.00D + 95;
        Double amount = avgFee * releaseDays + sum;
        System.out.println(new BigDecimal(NumberUtil.scale2(amount) + ""));
        System.out.println();
        System.out.println(amount);
        System.out.println(releaseDays);
    }

    @Test
    public void buildTree(){
        Node 河南 = new Node(1L, "河南", null);
        Node 河北 = new Node(2L, "河北", null);
        Node 郑州 = new Node(11L, "郑州", 1L);
        Node 周口 = new Node(12L, "周口", 1L);
        Node 金水区 = new Node(111L, "金水区", 11L);
        Node 二七区 = new Node(112L, "二七区", 11L);
        ArrayList<Node> nodes = Lists.newArrayList(河南, 河北, 郑州, 周口, 金水区, 二七区);
        HashMap<Long, List<Node>> parentIdNodeMap = new HashMap<>();
        for (Node node : nodes) {
            //省没有parent id
            Long parentId = node.getParentId() == null ? 0L : node.getParentId();
            if (parentIdNodeMap.containsKey(parentId)){
                parentIdNodeMap.get(parentId).add(node);
            } else {
                parentIdNodeMap.put(parentId, Lists.newArrayList(node));
            }
        }
        System.out.println("xiao na na zhen ke ai");
        System.out.println("baobao e l ma?");
        for (Map.Entry<Long, List<Node>> entry : parentIdNodeMap.entrySet()) {
            System.out.println(JsonUtil.encodeJson(entry));
        }
        System.out.println(parentIdNodeMap);
    }

    @Test
    public void testGetMethod(){
        LoadCore.init();
        Object testController = BeanFactory.getBean("testController");
        Class<?> aClass = testController.getClass();
        System.out.println(aClass);
        Arrays.asList(aClass.getDeclaredMethods()).stream().map(Method::getName).forEach(System.out::println);
    }

    @Test
    public void testClassInfo(){
        LoadCore.init();
        ClassInfo classInfo = ClassFactory.getClassInfo(User.class);
        System.out.println(JsonUtil.encodeJson(classInfo));
    }

    @Test
    public void testGetFields() throws Exception{
        List<Field> fields = ReflectionUtil.getFields(TransactionProxy.class);
        System.out.println(fields);
        List<Field> fields1 = ReflectionUtil.getFields(UserDTO.class);
        System.out.println(fields1);
        Field createId = UserDTO.class.getDeclaredField("createId");
        System.out.println(createId);
    }
}
