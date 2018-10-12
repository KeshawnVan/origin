package star.bean;

import java.util.List;

public class DomElement {
    private String id;
    private String xpath;
    private String table;
    private String column;
    private String type;
    private Boolean isCollection;
    private Boolean isJoin = Boolean.FALSE;
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

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public Boolean getJoin() {
        return isJoin;
    }

    public void setJoin(Boolean join) {
        isJoin = join;
    }
}
