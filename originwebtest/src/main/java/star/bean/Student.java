package star.bean;

import star.annotation.parser.ModelElement;
import star.annotation.parser.XPath;

import java.util.List;

@ModelElement
public class Student {

    @XPath("//@sn")
    private String sn;

    @XPath("//name")
    private String name;

    @XPath("//age")
    private String age;

    @XPath("//addr")
    private List<String> address;

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public List<String> getAddress() {
        return address;
    }

    public void setAddress(List<String> address) {
        this.address = address;
    }
}
