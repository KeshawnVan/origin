package star.thread;

import org.springframework.util.CollectionUtils;
import star.utils.NumberUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RandomList<T> {

    private List<T> list;

    public RandomList() {
        list = new ArrayList<>();
    }

    public RandomList(List<T> list) {
        this.list = list;
    }

    public synchronized Optional<T> randomGet() {
        return CollectionUtils.isEmpty(list) ? Optional.empty() : Optional.ofNullable(list.get(NumberUtil.getRandomNum(list.size())));
    }

    public synchronized void remove(T t) {
        list.remove(t);
    }

    @Override
    public String toString() {
        return "RandomList{" +
                "list=" + list +
                '}';
    }
}
