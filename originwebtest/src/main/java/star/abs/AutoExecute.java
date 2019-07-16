package star.abs;

public enum AutoExecute {

    ONE(0, "one"),
    TWO(1, "two"),
    THREE(2, "three"),
    FOUR(3, "four");

    private int value;

    private String name;

    AutoExecute(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

}
