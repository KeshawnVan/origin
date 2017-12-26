package star.bean;

import star.utils.CastUtil;

import java.util.Map;

/**
 * @author keshawn
 * @date 2017/11/17
 * 请求参数对象
 */
public final class Param {
    private Map<String, Object> paramMap;

    public Param(Map<String, Object> paramMap) {
        this.paramMap = paramMap;
    }
    public long getLong(String name){
        return CastUtil.castLong(paramMap.get(name));
    }

    public Map<String, Object> getMap() {
        return paramMap;
    }
}
