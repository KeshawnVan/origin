import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.junit.Test;
import star.bean.Students;
import star.utils.ClassUtil;
import star.utils.JsonUtil;
import star.utils.ReflectionUtil;
import star.utils.XmlUtil;

import java.util.*;
import java.util.stream.Collectors;

public class TestDom4j {

    @Test
    public void testParse() throws Exception {
        SAXReader saxReader = new SAXReader();
        Document document = saxReader.read(ClassUtil.getClassLoader().getResourceAsStream("students.xml"));
        Element rootElement = document.getRootElement();
        for (Iterator<Element> it = rootElement.elementIterator(); it.hasNext(); ) {
            Element element = it.next();
            parseElement(element);
        }
    }

    private void parseElement(Element element) {
        System.out.printf("< element %s start \r\n", element.getName());
        System.out.printf("< element %s value is %s ", element.getName(), element.getText().trim());
        for (Iterator<Attribute> ia = element.attributeIterator(); ia.hasNext(); ) {
            Attribute attribute = ia.next();
            System.out.printf("attribute is %s value is %s ", attribute.getName(), attribute.getValue());
        }
        for (Iterator<Element> ie = element.elementIterator(); ie.hasNext(); ) {
            parseElement(ie.next());
        }
        System.out.printf(" element %s end >", element.getName());
        System.out.println("");
    }

    @Test
    public void testXpath() throws Exception {
        Students students = XmlUtil.decode(ClassUtil.getClassLoader().getResourceAsStream("students.xml"), Students.class);
        System.out.println(JsonUtil.encodeJson(students));

        Map<String, String> xpathMap = new HashMap<>();
        xpathMap.put("star.bean.Students.text", "//students/text");
        Students students2 = XmlUtil.decode(ClassUtil.getClassLoader().getResourceAsStream("students.xml"), Students.class, xpathMap);
        System.out.println(JsonUtil.encodeJson(students2));
    }

    @Test
    public void getAllKey() {
        List<String> keys = Arrays.stream(Students.class.getDeclaredFields()).map(field -> Students.class.getName() + "." + field.getName()).collect(Collectors.toList());
        System.out.println(keys);
    }

    @Test
    public void getClassSet() {
//        String packageName = "star.bean";
//        Map<String, List<String>> clazzFiledMap = ClassUtil.getClassSet(packageName).stream()
//                .collect(Collectors.toMap(Class::getSimpleName, clazz -> Arrays.stream(clazz.getDeclaredFields())
//                        .map(field -> clazz.getName() + "." + field.getName()).collect(Collectors.toList())));
//
//        System.out.println(JsonUtil.encodeJson(clazzFiledMap));
        String packageName = "org.apache.commons.io.output";
        Set<Class<?>> classSet = ClassUtil.getClassSet(packageName);
        System.out.println(classSet);

    }
}
