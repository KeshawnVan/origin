import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.junit.Test;
import star.bean.Students;
import star.utils.ClassUtil;
import star.utils.JsonUtil;
import star.utils.XmlUtil;

import java.util.Iterator;

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
    }
}
