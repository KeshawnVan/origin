package star.bean;

public class Key {
    private String table;

    private String column;

    private String type;

    public Key() {
    }

    public Key(String table, String column, String type) {
        this.table = table;
        this.column = column;
        this.type = type;
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
}
