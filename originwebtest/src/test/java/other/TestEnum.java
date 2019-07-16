package other;

import org.junit.Test;
import star.abs.*;

import java.util.*;
import java.util.stream.Collectors;

public class TestEnum {

    @Test
    public void exec() {

        // 倒序排序
        List<AutoExecute> autoExecutes = Arrays.stream(AutoExecute.values()).sorted(Comparator.comparing(AutoExecute::getValue).reversed()).collect(Collectors.toList());

        // 构建枚举和实现类的关系
        Map<AutoExecute, EnumExecuteManager> mapping = buildMapping();

        selectAndExecute(autoExecutes, mapping, 0);
    }

    public void selectAndExecute(List<AutoExecute> autoExecutes, Map<AutoExecute, EnumExecuteManager> mapping, int index) {
        // 超出长度终止递归
        if (index > autoExecutes.size() - 1) {
            throw new RuntimeException("都没匹配到");
        }
        AutoExecute autoExecute = autoExecutes.get(index);
        EnumExecuteManager enumExecuteManager = mapping.get(autoExecute);
        int executeResult = enumExecuteManager.execute(autoExecute);
        // 如果结果为一， 结束, 否则匹配下一个实现类
        if (executeResult == 1) {
            System.out.println("匹配到" + autoExecute.getName());
        } else {
            selectAndExecute(autoExecutes, mapping, index + 1);
        }

    }

    private Map<AutoExecute, EnumExecuteManager> buildMapping() {
        Map<AutoExecute, EnumExecuteManager> mapping = new HashMap<>();
        mapping.put(AutoExecute.ONE, new OneExecuteManager());
        mapping.put(AutoExecute.TWO, new TwoExecuteManager());
        mapping.put(AutoExecute.THREE, new ThreeExecuteManager());
        mapping.put(AutoExecute.FOUR, new FourExecuteManager());
        return mapping;
    }
}
