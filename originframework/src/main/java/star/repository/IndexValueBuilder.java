package star.repository;

import star.utils.BeanUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * @author keshawn
 * @date 2018/2/26
 */
public final class IndexValueBuilder {

    private IndexValueBuilder() {
    }

    public static Map<Integer, Object> buildIndexValueMap(Object[] params, Map<String, Integer> fieldIndexMap) {
        Map<Integer, Object> indexValueMap = new HashMap<>(fieldIndexMap.size());

        if (params.length != 1) {
            throw new RuntimeException("CustomExecutor cannot parse params, because params is not one");
        }
        Object param = params[0];

        if (param instanceof Map) {
            mapParamParse(indexValueMap, fieldIndexMap, (Map<String, Object>) param);
        } else {
            mapParamParse(indexValueMap, fieldIndexMap, BeanUtil.toMap(param));
        }
        return indexValueMap;
    }


    private static void mapParamParse(Map<Integer, Object> indexValueMap, Map<String, Integer> fieldIndexMap, Map<String, Object> param) {
        Map<String, Object> paramMap = param;
        for (Map.Entry<String, Integer> entry : fieldIndexMap.entrySet()) {
            String paramName = entry.getKey();
            if (paramMap.containsKey(paramName)) {
                indexValueMap.put(entry.getValue(), paramMap.get(paramName));
            } else {
                throw new RuntimeException("buildIndexValueMap error, because cannot match param : " + paramName);
            }
        }
    }
}
