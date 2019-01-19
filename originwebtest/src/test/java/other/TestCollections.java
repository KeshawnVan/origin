package other;

import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.impl.factory.Lists;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class TestCollections {

    @Test
    public void helloEclipse() {
        ImmutableList<Integer> integerImmutableList = Lists.immutable.of(1, 2, 4, 5, 6, 7);
        List<Integer> integers = integerImmutableList.select(i -> i.equals(1)).castToList();
        System.out.println(integers);
        ArrayList<Integer> integers1 = com.google.common.collect.Lists.newArrayList(123, 122, 344);

    }
}
