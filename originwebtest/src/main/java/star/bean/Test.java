package star.bean;

public class Test {
    static User user;


    public static User getUser() {
        return user;
    }

    public static void setUser(User user) {
        Test.user = user;
    }

    public static void main(String[] args) {
        Test test = new Test();
        User user = new User();
        user.setName("123");
        test.setUser(user);
        System.out.println(new Test().getUser());
    }
}
