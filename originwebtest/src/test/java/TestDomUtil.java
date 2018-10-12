import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
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
        DomElement classId = buildClassId();
        student.setName("student");
        student.setCollection(true);
        student.setXpath("//students/class/student");
        student.setDomElements(Lists.newArrayList(sn, address, classId));
        return student;
    }

    private DomElement buildClassId() {
        DomElement classId = new DomElement();
        classId.setName("classId");
        classId.setCollection(false);
        classId.setXpath("//students/class/id");
        classId.setJoin(true);
        return classId;
    }

    private DomElement buildAddress() {
        DomElement address = new DomElement();
        address.setXpath("//students/class/student/addr");
        address.setCollection(true);
        address.setName("address");
        return address;
    }

    private DomElement buildSn() {
        DomElement sn = new DomElement();
        sn.setCollection(false);
        sn.setName("ssn");
        sn.setXpath("//students/class/student/@sn");
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

    @Test
    public void testA() {
        DomElement patient = new DomElement();
        DomElement id = getId();
        DomElement statusCode = getStatusCode();
        DomElement effectiveTime = getEffectiveTime();
        patient.setDomElements(Lists.newArrayList(id, statusCode, effectiveTime));
        Map<String, Object> map = DomUtil.decode(patient, ClassUtil.getClassLoader().getResourceAsStream("subject.xml"), "urn:hl7-org:v3");
        System.out.println(map);
    }

    private DomElement getEffectiveTime() {
        DomElement effectiveTime = new DomElement();
        effectiveTime.setCollection(false);
        effectiveTime.setName("effectiveTime");
        effectiveTime.setXpath("//controlActProcess/subject/registrationRequest/subject1/patient/effectiveTime/any/@value");
        return effectiveTime;
    }

    private DomElement getStatusCode() {
        DomElement statusCode = new DomElement();
        statusCode.setName("statusCode");
        statusCode.setXpath("//controlActProcess/subject/registrationRequest/subject1/patient/statusCode/@code");
        statusCode.setCollection(false);
        return statusCode;
    }

    private DomElement getId() {
        DomElement id = new DomElement();
        id.setName("id");
        id.setCollection(false);
        id.setXpath("//controlActProcess/subject/registrationRequest/subject1/patient/id/item/@root");
        return id;
    }

    @Test
    public void test() {
        String path = "//controlActProcess/subject/registrationRequest/subject1/patient/statusCode/@code";
        String prefix = path.substring(0, 3);
        String formatPrefix = prefix.equals("//@") ? prefix : prefix.substring(0, 1) + "/ns:" + prefix.substring(2, 3);
        String content = path.substring(3, path.length());
        int index = content.indexOf("/@");
        String formatContent = index > 0
                ? StringUtils.replaceAll(content.substring(0, index), "/", "/ns:") + content.substring(index, content.length())
                : StringUtils.replaceAll(content, "/", "/ns:");
        String nsXpath = formatPrefix + formatContent;
        System.out.println(nsXpath);
    }
}
