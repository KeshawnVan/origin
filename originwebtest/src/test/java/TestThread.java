import com.google.common.collect.Lists;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;
import star.bean.User;
import star.thread.Holder;
import star.thread.NamedThreadFactory;
import star.utils.CastUtil;
import star.utils.ClassUtil;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.function.Function;

public class TestThread {
//    public static void main(String[] args) throws Exception {
//
//        List<String> list = Lists.newArrayList();
//        for (int i = 0; i < 20; i++) {
//            list.add("queue" + i);
//        }
//
//        ThreadPoolExecutor boom = new ThreadPoolExecutor(5, 5, 0, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(), new ThreadPoolExecutor.DiscardPolicy());
//
//        while (true) {
//            List<Future<String>> futures = new LinkedList<>();
//            for (String queue : list) {
//                Future<String> future = boom.submit(run(queue));
//                futures.add(future);
//            }
//            for (Future<String> future : futures) {
//                System.out.println(future.get());
//            }
//        }
//    }

    @NotNull
    private static Callable<String> run(String queue) {
        return () -> {
            System.out.println(String.format("execute %s begin", queue));
            try {
                Thread.sleep(5000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //System.out.println(String.format("execute %s end", queue));
            return String.format("execute %s end", queue);
        };
    }

    @Test
    public void testRenameThreadName() {
        ThreadFactory threadFactory = new NamedThreadFactory("listener");
        Runnable fkx = () -> System.out.println("thread name is : " + Thread.currentThread().getName());
        ExecutorService executorService = Executors.newSingleThreadExecutor(threadFactory);
        executorService.execute(fkx);
        try {
            Thread.sleep(10000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testFunctionName() {
        Function function = test -> test;
        Class<? extends Function> aClass = function.getClass();
        System.out.println(aClass.getName());
    }

    @Test
    public void testThreadFactory() {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
    }

    public static void main(String[] args) {

        Lists.newArrayList(1, 2, 3, 4, 5, 6, 7, 8, 98, 0).parallelStream().forEach(i -> System.out.println(Holder.getString()));
        System.out.println("autote130-pmsPartition-subscriberEquipmentInstructionGenerate-C1011000.fifo".length());
    }

    @Test
    public void testShutDown() {
        Runnable runnable = () -> {
            System.out.println("thread start");
            while (true) {
                System.out.println("======11111======");
                try {
                    Thread.sleep(1000L);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        };
        Runnable runnable2 = () -> {
            System.out.println("thread start");
            while (true) {
                System.out.println("======22222======");
                try {
                    Thread.sleep(1000L);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        };
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(runnable);
        try {
            Thread.sleep(10000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        executorService.shutdownNow();

        System.out.println("thread 1 shut down");

        ExecutorService executorService2 = Executors.newSingleThreadExecutor();
        executorService2.execute(runnable2);
        try {
            Thread.sleep(10000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        executorService2.shutdownNow();

        System.out.println("thread 2 shut down");


        try {
            Thread.sleep(1000000000000000000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void testJStack() {
        Runnable runnable = () -> {
            System.out.println("thread start");
            while (true) {
                System.out.println("======11111======");
                try {
                    Thread.sleep(1000L);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        };
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(runnable);
        long l = System.currentTimeMillis();
        Map<Thread, StackTraceElement[]> allStackTraces = Thread.getAllStackTraces();
        for (Map.Entry<Thread, StackTraceElement[]> threadEntry : allStackTraces.entrySet()) {
            Thread thread = threadEntry.getKey();
            StackTraceElement[] stackTraceElements = threadEntry.getValue();
            System.out.println(thread + "stack is : ");
            for (StackTraceElement stackTraceElement : stackTraceElements) {
                System.out.println("\t" + stackTraceElement + "\n");
            }
        }
        System.out.println(System.currentTimeMillis() - l);
        OperatingSystemMXBean operatingSystemMXBean = ManagementFactory.getOperatingSystemMXBean();
        System.out.println(operatingSystemMXBean.getSystemLoadAverage());
    }

    @Test
    public void test() {
        LocalDate now = LocalDate.now();
        int year = now.getYear();
        int day = now.getDayOfMonth();
        System.out.println(CastUtil.castString(year * day));
    }

    @Test
    public void testLists() {
        List<String> heroList = Lists.newArrayList("疾风剑豪", "放逐之刃", "无双剑姬");
        List<String> strings = Arrays.asList("疾风剑豪", "放逐之刃", "无双剑姬");
    }

    @Test
    public void testThreadSaveAndLoad() {
        User user = new User();
        new Thread(() -> user.setId(1000L)).start();
        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        user.setId(10001L);
        System.out.println(user.getId());
        new Thread(() -> System.out.println(user.getId())).start();
    }

    @Test
    public void testFieldEqui() {
        Field field = ClassUtil.getClassInfo(User.class).getFieldMap().get("id");
        Long l = 100L;
        System.out.println(field.getType().equals(l.getClass()));
    }
}
