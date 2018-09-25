import com.google.common.collect.Lists;
import org.junit.Test;
import star.bean.DomElement;
import star.utils.ClassUtil;
import star.utils.DomUtil;
import star.utils.JsonUtil;

import java.util.Map;

public class TestDomUtil {

    @Test
    public void buildMap() {
        DomElement students = new DomElement();
        DomElement text = buildText();
        DomElement flags = buildFlag();
        DomElement student = buildStudent();

        students.setDomElements(Lists.newArrayList(text, flags, student));

        Map<String, Object> map = DomUtil.decode(students, ClassUtil.getClassLoader().getResourceAsStream("students.xml"), "urn:hl7-org:v3");
        System.out.println(JsonUtil.encodeJson(map));
    }

    private DomElement buildStudent() {
        DomElement student = new DomElement();
        DomElement sn = buildSn();
        DomElement address = buildAddress();
        student.setName("student");
        student.setCollection(true);
        student.setXpath("//students/class/student");
        student.setDomElements(Lists.newArrayList(sn, address));
        return student;
    }

    private DomElement buildAddress() {
        DomElement address = new DomElement();
        address.setXpath("//addr");
        address.setCollection(true);
        address.setName("address");
        return address;
    }

    private DomElement buildSn() {
        DomElement sn = new DomElement();
        sn.setCollection(false);
        sn.setName("ssn");
        sn.setXpath("//@sn");
        return sn;
    }

    private DomElement buildFlag() {
        DomElement flags = new DomElement();
        flags.setName("flag");
        flags.setCollection(true);
        flags.setXpath("//students/flag");
        return flags;
    }

    private DomElement buildText() {
        DomElement text = new DomElement();
        text.setCollection(false);
        text.setId("123");
        text.setName("txt");
        text.setXpath("//students/text");
        return text;
    }
}
