package star.convert;

public class ExcelValue<R> {
    private R value;
    private String type;

    public R getValue() {
        return value;
    }

    public void setValue(R value) {
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
