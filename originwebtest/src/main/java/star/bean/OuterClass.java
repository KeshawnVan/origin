package star.bean;

public class OuterClass {
    private class InterClass {
        public InterClass() {
            System.out.println("interClass create");
        }
    }

    public OuterClass() {
        InterClass interClass = new InterClass();
        System.out.println("OuterClass create");
    }

    public static void main(String[] args) {
        OuterClass outerClass = new OuterClass();
    }
}
