package star.bean;

import star.annotation.parser.ModelElement;
import star.annotation.parser.XPath;

import java.util.List;

@ModelElement
public class Students {

    @XPath("//students/class/student")
    List<Student> students;

    @XPath("//students/text")
    String text;

    @XPath("//students/@klass")
    String klass;

    @XPath("//students/flag")
    List<Long> flags;

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getKlass() {
        return klass;
    }

    public void setKlass(String klass) {
        this.klass = klass;
    }

    public List<Long> getFlags() {
        return flags;
    }

    public void setFlags(List<Long> flags) {
        this.flags = flags;
    }
}
