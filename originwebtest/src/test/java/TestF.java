import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.ArrayUtils;
import org.junit.Test;
import star.annotation.Inject;
import star.bean.Handler;
import star.bean.PartnerLevel;
import star.bean.User;
import star.bean.YamlBean;
import star.constant.RequestMethod;
import star.controller.TestController;
import star.exception.ImplementDuplicateException;
import star.factory.BeanFactory;
import star.factory.ControllerFactory;
import star.proxy.DynamicProxy;
import star.service.TestService;
import star.service.impl.TestServiceImpl;
import star.utils.*;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;

import static star.utils.StringUtil.checkTimeValid;

/**
 * @author keshawn
 * @date 2017/11/8
 */
public class TestF {

    public static final String UTF_8 = "UTF-8";
    public static final String UNICODE = "UNICODE";

    @Test
    public void test() {
        System.out.println(StringUtil.isNotEmpty("   "));
    }

    @Test
    public void testIoc() {
        //获取所有的Bean类和Bean实例之间的映射关系
        Map<Class<?>, Object> beanMap = BeanFactory.getBeanMap();
        //获取所有带有@Service注解的类的接口与自身的映射关系
        Map<Class<?>, Class<?>> serviceMappingMap = BeanFactory.getServiceMappingMap();
        System.out.println(serviceMappingMap);
        if (CollectionUtil.isNotEmpty(beanMap)) {
            for (Map.Entry<Class<?>, Object> beanEntry : beanMap.entrySet()) {
                Class<?> beanClass = beanEntry.getKey();
                Object beanInstance = beanEntry.getValue();
                Field[] beanFields = beanClass.getDeclaredFields();
                if (ArrayUtils.isNotEmpty(beanFields)) {
                    //循环进行依赖注入
                    for (Field beanField : beanFields) {
                        if (beanField.isAnnotationPresent(Inject.class)) {
                            Class<?> beanFieldClass = beanField.getType();
                            System.out.println(beanFieldClass.getSimpleName());
                            //如果是接口，注入实现类，否则注入类自身
                            if (beanFieldClass.isInterface()) {
                                Inject inject = beanField.getAnnotation(Inject.class);
                                String injectValue = inject.value();
                                //如果使用@Inject注解未指定实现类，则从serviceMappingMap中寻找实现类进行注入
                                if (StringUtil.isEmpty(injectValue)) {
                                    Class<?> implementClass = serviceMappingMap.get(beanFieldClass);
                                    //如果实现类为ImplementDuplicateException，则代表一个接口有多个实现类，而使用者又未指明使用那个实现类
                                    if (implementClass.equals(ImplementDuplicateException.class)) {
                                        //打印日志
                                        throw new ImplementDuplicateException("cannot inject User , this interface has more than one implement");
                                    } else {
                                        Object beanFieldInstance = BeanFactory.getBean(implementClass);
                                        if (beanFieldInstance != null) {
                                            //通过反射初始化BeanField的值
                                            ReflectionUtil.setField(beanInstance, beanField, beanFieldInstance);
                                        }
                                    }
                                } else {
                                    //@Inject注解未指定实现类 User id
                                    Object beanFieldInstance = BeanFactory.getBean(injectValue);
                                    if (beanFieldInstance != null) {
                                        //通过反射初始化BeanField的值
                                        ReflectionUtil.setField(beanInstance, beanField, beanFieldInstance);
                                    }
                                }
                            } else {
                                Object beanFieldInstance = BeanFactory.getBean(beanFieldClass);
                                if (beanFieldInstance != null) {
                                    //通过反射初始化BeanField的值
                                    ReflectionUtil.setField(beanInstance, beanField, beanFieldInstance);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Test
    public void testDI() {
//        IocCore iocCore = new IocCore();
//        TestController testController = (TestController)BeanFactory.getBean("testController");
//        System.out.println(testController.hashCode());
//        testController.hello();
//        System.out.println("----------------");
//        testController = (TestController)BeanFactory.getBean("testController");
//        System.out.println(testController.hashCode());
//        testController.hello();
    }

    @Test
    public void testYaml() {
        YamlBean yamlBean = YamlUtil.getYamlBean("origin.yml", YamlBean.class);
        System.out.println(yamlBean);
    }

    @Test
    public void testControllerFactory() {
        Handler handler = ControllerFactory.getHandler(RequestMethod.GET, "/2");
        System.out.println(handler);
    }

    @Test
    public void testJsonUtil() {
        YamlBean yamlBean = YamlUtil.getYamlBean("origin.yml", YamlBean.class);
        String json = JsonUtil.encodeJson(yamlBean);
        System.out.println(json);
        YamlBean bean = JsonUtil.decodeJson(json, YamlBean.class);
        System.out.println(bean);
        String name = "fkx";
        System.out.println(JsonUtil.encodeJson(name));
    }

    @Test
    public void testLists() {
        List<String> list = Lists.newArrayList("a", "b", "c", "d", "e", "f", "g");
        list.forEach(a -> System.out.println(a));
        Lists.partition(list, 2).forEach(a -> a.forEach(b -> System.out.println(b)));
    }

    @Test
    public void testW() {
        String placeholder = ",?";
        StringBuilder stringBuilder = new StringBuilder("(?");
        int size = 1000;
        for (int i = 0; i < size; i++) {
            stringBuilder.append(placeholder);
        }
        stringBuilder.append(")");
    }

    @Test
    public void buildJson() {
        Map<String, Object> jsonMap = new HashMap<>();
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("skuId", "2961041");
        paramMap.put("phoneNum", "15225261060");
        jsonMap.put("methodName", "checkCode");
        jsonMap.put("jsonParams", paramMap);
        System.out.println(JsonUtil.encodeJson(jsonMap));
    }

    @Test
    public void testReg() {
        System.out.println(checkTimeValid("2099-08-10 01:17:57"));
    }

    @Test
    public void testUrlEncode() throws Exception {
        System.out.println(URLEncoder.encode("test", "UNICODE"));
        System.out.println(URLEncoder.encode("范开翔#", "UTF-8"));
    }

    @Test
    public void testDecode() {
        String json = "{methodName:checkCode,jsonParams:{skuId:2961041,phoneNum:15225261060}}";
        Map map = JsonUtil.decodeJson(json, Map.class);
        System.out.println(map);
    }

    private String scale2digit(BigDecimal bigDecimal) {
        if (bigDecimal == null) {
            return "0.00";
        }
        return bigDecimal.setScale(2, RoundingMode.HALF_UP).toString();
    }

    @Test
    public void testBig() {
        System.out.println(scale2digit(new BigDecimal(12)));
        System.out.println(scale2digit(null));
        String s = scale2digit(new BigDecimal(12));
        String ss = "10.01";
        String[] array = ss.split("\\.");
        System.out.println(array[0]);
        System.out.println(array[1]);

    }

    @Test
    public void testInstant() {
        System.out.println(JsonUtil.encodeJson(Instant.now()));
        Instant instant = JsonUtil.decodeJson(JsonUtil.encodeJson(Instant.now()), Instant.class);
        System.out.println(instant);
    }

    @Test
    public void jackson() {
        String s = "{\"receiveInstantDate\":\"2017-12-06 13:17:22\",\"sender\":\"+111\",\"transId\":\"3\",\"type\":0,\"messageEquipmentId\":\"10000\",\"content\":\"test\",\"shortCode\":\"test\"}";
        Map<String, Object> map = JsonUtil.decodeJson(s, Map.class);
        System.out.println(map);
    }

    @Test
    public void testEm() {
        System.out.println(PartnerLevel.SUP_DEALER.toString());
        System.out.println(PartnerLevel.values());
        String level = "DEALER";
        int num = 0;
        PartnerLevel[] values = PartnerLevel.values();
        for (PartnerLevel value : values) {
            if (value.toString().equals(level)) {
                num = value.getCode();
            }
        }
        System.out.println(num);
    }

    @Test
    public void TestBuilder() {
        User user = GenericLambdaBuilder
                .build(User.class)
                .with(User::setAge, 10)
                .with(User::setName, "asd")
                .get();
        System.out.println(JsonUtil.encodeJson(user));
    }


    public static void hello() {
        System.out.println("Hello World");
    }

    @Test
    public void testBuilder(){
        User fkx = User.newBuilder().age(10).name("fkx").build();
        String json = JSON.toJSONStringWithDateFormat(fkx,"yyyy-MM-dd HH:mm:ss", SerializerFeature.DisableCircularReferenceDetect);
        System.out.println(json);
    }

    @Test
    public void testProper(){
        Properties properties = PropertiesUtil.loadProperties("log4j.properties");
        Set<String> keys = PropertiesUtil.getAllKey(properties);
        System.out.println(keys);
    }

    @Test
    public void testTimeZone(){
        LocalDateTime localDateTime = LocalDateTime.now();
        Instant instant = localDateTime.atZone(ZoneOffset.ofHours(2).normalized()).toInstant();
        LocalDateTime dateTime = LocalDateTime.ofInstant(instant, ZoneOffset.ofHours(2));
        System.out.println(dateTime);
    }

    @Test
    public void testProxy(){
        TestServiceImpl testService = new TestServiceImpl();
        DynamicProxy proxy = new DynamicProxy(testService);
        TestService proxyService = proxy.getProxy();
        proxy.setBeforeSupplier(()->User.newBuilder().age(10).name("fkx").build());
//        proxy.setAfterConsumer(p -> {
//            User user = (User) p;
//            System.out.println(user.getName());
//        });
        proxyService.hello();
    }

    @Test
    public void testCode()throws Exception{
        String s = "E卡商品特殊标识";
        String code = convert(s);
        System.out.println(code);
        String ss = "\\u0045\\u5361\\u5546\\u54c1\\u7279\\u6b8a\\u6807\\u8bc6";
        System.out.println(URLDecoder.decode(ss,UNICODE));
    }
    public String convert(String str)
    {
        str = (str == null ? "" : str);
        String tmp;
        StringBuffer sb = new StringBuffer(1000);
        char c;
        int i, j;
        sb.setLength(0);
        for (i = 0; i < str.length(); i++)
        {
            c = str.charAt(i);
            sb.append("\\u");
            j = (c >>>8); //取出高8位
            tmp = Integer.toHexString(j);
            if (tmp.length() == 1)
                sb.append("0");
            sb.append(tmp);
            j = (c & 0xFF); //取出低8位
            tmp = Integer.toHexString(j);
            if (tmp.length() == 1)
                sb.append("0");
            sb.append(tmp);

        }
        return (new String(sb));
    }
}
