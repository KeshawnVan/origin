import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class TestQueue {
    static LoadingCache<String, List<String>> queueCache = CacheBuilder.newBuilder()
            .expireAfterWrite(1, TimeUnit.MINUTES)
            .initialCapacity(16)
            .build(new CacheLoader<String, List<String>>() {
                @Override
                public List<String> load(String s) throws Exception {
                    return Lists.newArrayList("q1", "q2", "q3", "q4");
                }
            });

    public static void main(String[] args) {
        List<String> strings = queueCache.getUnchecked("prefix");
        System.out.println(strings);
        strings.remove(1);
        System.out.println(queueCache.getUnchecked("prefix"));
        try {
            Thread.sleep(60 * 1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(queueCache.getUnchecked("prefix"));
    }
}
