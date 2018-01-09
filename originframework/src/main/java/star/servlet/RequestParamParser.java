package star.servlet;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import star.constant.ConfigConstant;
import star.utils.CodeUtil;
import star.utils.StreamUtil;
import star.utils.StringUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static star.constant.ServletConstant.*;

/**
 * @author keshawn
 * @date 2018/1/9
 */
public final class RequestParamParser {

    private RequestParamParser() {
    }

    public static Map<String, Object> buildParamMap(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding(UTF_8);
        Map<String, Object> paramMap = new HashMap<>(ConfigConstant.INITIAL_CAPACITY);

        fillServletObject(request, response, paramMap);

        fillByParameterNames(request, paramMap);

        fillByRequestBody(request, paramMap);

        return paramMap;
    }

    private static void fillByRequestBody(HttpServletRequest request, Map<String, Object> paramMap) throws IOException {
        //获得请求体
        String body = CodeUtil.decodeURL(StreamUtil.getString(request.getInputStream()));
        if (StringUtil.isNotEmpty(body)) {
            String contentType = request.getContentType();
            //JSON格式的
            if (APPLICATION_JSON.equals(contentType)) {
                paramMap.put(APPLICATION_JSON, body);
            } else {
                parseTextRequestBody(paramMap, body);

            }
        }
    }

    private static void parseTextRequestBody(Map<String, Object> paramMap, String body) {
        //其他的都按文本处理
        String[] params = StringUtils.split(body, SEPARATOR_CHARS);
        if (ArrayUtils.isNotEmpty(params)) {
            for (String param : params) {
                String[] paramArray = StringUtils.split(param, EQUAL);
                if (ArrayUtils.isNotEmpty(paramArray) && paramArray.length == 2) {
                    String paramName = paramArray[0];
                    String paramValue = paramArray[1];
                    putParamMap(paramMap, paramName, paramValue);
                }
            }
        }
    }

    private static void fillByParameterNames(HttpServletRequest request, Map<String, Object> paramMap) {
        Enumeration<String> paramNames = request.getParameterNames();
        while (paramNames.hasMoreElements()) {
            String paramName = paramNames.nextElement();
            String paramValue = request.getParameter(paramName);
            putParamMap(paramMap, paramName, paramValue);
        }
    }

    private static void fillServletObject(HttpServletRequest request, HttpServletResponse response, Map<String, Object> paramMap) {
        paramMap.put(HTTP_SERVLET_REQUEST, request);
        paramMap.put(HTTP_SERVLET_RESPONSE, response);
        paramMap.put(HTTP_SESSION, request.getSession());
        paramMap.put(SERVLET_CONTEXT, request.getSession().getServletContext());
    }

    private static void putParamMap(Map<String, Object> paramMap, String paramName, String paramValue) {
        //如果key相同，使用List去存储
        if (paramMap.containsKey(paramName)) {
            Object duplicateValue = paramMap.get(paramName);
            if (duplicateValue instanceof List) {
                ((List) duplicateValue).add(paramValue);
            } else {
                paramMap.put(paramName, Lists.newArrayList(duplicateValue, paramValue));
            }
        } else {
            paramMap.put(paramName, paramValue);
        }
    }
}
