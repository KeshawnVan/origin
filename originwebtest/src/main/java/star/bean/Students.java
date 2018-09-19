package star.bean;

import star.annotation.parser.ModelElement;
import star.annotation.parser.XPath;

import java.util.List;

@ModelElement
public class Students {

    @XPath("//students/student")
    List<Student> students;

    @XPath("//students/text")
    String text;

    @XPath("//students/@klass")
    String klass;

    @XPath("//students/flag")
    List<Long> flags;
}
