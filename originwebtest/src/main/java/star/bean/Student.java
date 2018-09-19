package star.bean;

import star.annotation.parser.ModelElement;
import star.annotation.parser.XPath;

@ModelElement
public class Student {

    @XPath("//@sn")
    private String sn;

    @XPath("//name")
    private String name;

    @XPath("//age")
    private String age;
}
