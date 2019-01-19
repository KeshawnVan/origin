package other;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.collections.CollectionUtils;
import org.jetbrains.annotations.NotNull;
import star.utils.Tuple;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TestResultMap {

    public static void main(String[] args) {

        List<Map<String, Object>> mapList = getTestData();

        String pId = "a=id";
        Tuple<String, String> pTableAndColumn = getTableAndColumn(pId);
        List<Map<String, Object>> results = new ArrayList<>();

        //按照PId的值分组
        Map<Object, List<Map<String, Object>>> pId2RowsMap = mapList.stream().collect(Collectors.groupingBy(map -> map.get(pId)));
        for (Map.Entry<Object, List<Map<String, Object>>> id2RowsEntry : pId2RowsMap.entrySet()) {
            List<Map<String, Object>> rows = id2RowsEntry.getValue();
            //主表的值都一样，取第一个就行
            Map<String, Object> pValue = rows.get(0);
            Map<String, Object> pMap = buildPMap(pTableAndColumn, pValue);
            Map<String, List<Map<String, Object>>> table2DatumMap = buildTable2DatumMap(pTableAndColumn, rows);
            for (Map.Entry<String, List<Map<String, Object>>> table2DatumEntry : table2DatumMap.entrySet()) {
                pMap.put(table2DatumEntry.getKey(), table2DatumEntry.getValue());
            }
            results.add(pMap);
        }
        System.out.println(results);
    }

    private static Map<String, List<Map<String, Object>>> buildTable2DatumMap(Tuple<String, String> pTableAndColumn, List<Map<String, Object>> rows) {
        Map<String, List<Map<String, Object>>> table2DatumMap = new HashMap<>();
        //遍历每一列
        for (Map<String, Object> row : rows) {
            //每列的值按照table分组
            Map<String, List<Map.Entry<String, Object>>> table2ColumnsMap = row.entrySet().stream().collect(Collectors.groupingBy(entry -> getTableAndColumn(entry.getKey())._1));
            for (Map.Entry<String, List<Map.Entry<String, Object>>> table2ColumnsEntry : table2ColumnsMap.entrySet()) {
                String tableName = table2ColumnsEntry.getKey();
                //如果是主表，直接跳过
                if (tableName.equals(pTableAndColumn._1)) {
                    continue;
                }
                Map<String, Object> column2ValueMap = table2ColumnsEntry.getValue().stream().collect(Collectors.toMap(entry -> getTableAndColumn(entry.getKey())._2, Map.Entry::getValue));
                List<Map<String, Object>> tableData = table2DatumMap.get(tableName);
                if (CollectionUtils.isNotEmpty(tableData)) {
                    tableData.add(column2ValueMap);
                } else {
                    table2DatumMap.put(tableName, Lists.newArrayList(column2ValueMap));
                }
            }
        }
        return table2DatumMap;
    }

    @NotNull
    private static List<Map<String, Object>> getTestData() {
        Map<String, Object> map1 = Maps.newHashMap();

        map1.put("a=id", "1111");
        map1.put("a=age", 12);
        map1.put("b=id", "aaaa");
        map1.put("b=addr", "test");
        map1.put("b=f_id", "1111");
        map1.put("c=id", "1a1a");
        map1.put("c=telephone", "12345678901");
        map1.put("c=f_id", "aaaa");

        Map<String, Object> map2 = Maps.newHashMap();

        map2.put("a=id", "1111");
        map2.put("a=age", 12);
        map2.put("b=id", "bbbb");
        map2.put("b=addr", "test");
        map2.put("b=f_id", "1111");
        map2.put("c=id", "2a2a");
        map2.put("c=telephone", "12345678901");
        map2.put("c=f_id", "bbbb");

        return Lists.newArrayList(map1, map2);
    }

    private static Map<String, Object> buildPMap(Tuple<String, String> pTableAndColumn, Map<String, Object> pValue) {
        HashMap<String, Object> pMap = new HashMap<>();
        for (Map.Entry<String, Object> entry : pValue.entrySet()) {
            Tuple<String, String> tableAndColumn = getTableAndColumn(entry.getKey());
            //如果是主表中的数据
            if (tableAndColumn._1.equals(pTableAndColumn._1)) {
                pMap.put(tableAndColumn._2, entry.getValue());
            }
        }
        return pMap;
    }

    private static Tuple<String, String> getTableAndColumn(String tableAndColumnName) {
        String[] splits = tableAndColumnName.split("=");
        String tableName = splits[0];
        String columnName = splits[1];
        return new Tuple<>(tableName, columnName);
    }
}
