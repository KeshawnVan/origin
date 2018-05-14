import com.google.common.collect.Lists;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.*;

public class TestThread {
    public static void main(String[] args) throws Exception {

        List<String> list = Lists.newArrayList();
        for (int i = 0; i < 20; i++) {
            list.add("queue" + i);
        }

        ThreadPoolExecutor boom = new ThreadPoolExecutor(5, 5, 0, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(), new ThreadPoolExecutor.DiscardPolicy());

        while (true) {
            List<Future<String>> futures = new LinkedList<>();
            for (String queue : list) {
                Future<String> future = boom.submit(run(queue));
                futures.add(future);
            }
            for (Future<String> future : futures) {
                System.out.println(future.get());
            }
        }
    }

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
}
