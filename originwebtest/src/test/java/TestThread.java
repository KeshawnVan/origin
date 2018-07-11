import com.google.common.collect.Lists;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;
import star.thread.Holder;

import java.time.Instant;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.*;
import java.util.function.Function;
import java.util.stream.Stream;

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
        Runnable fkx = () -> {
            Thread.currentThread().setName("fkx123");
            System.out.println("thread name is : " + Thread.currentThread().getName());
        };
        ExecutorService executorService = Executors.newSingleThreadExecutor();
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
        Lists.newArrayList(1,2,3,4,5,6,7,8,98,0).parallelStream().forEach(i -> System.out.println(Holder.getString()));
    }
}
