package star.bean;

import java.util.List;

public class DomElement {
    private String id;
    private String xpath;
    private String name;
    private Boolean isCollection;
    private List<DomElement> domElements;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getXpath() {
        return xpath;
    }

    public void setXpath(String xpath) {
        this.xpath = xpath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getCollection() {
        return isCollection;
    }

    public void setCollection(Boolean collection) {
        isCollection = collection;
    }

    public List<DomElement> getDomElements() {
        return domElements;
    }

    public void setDomElements(List<DomElement> domElements) {
        this.domElements = domElements;
    }
}
