package star.bean;

import star.annotation.Column;
import star.annotation.Table;

/**
 * @author keshawn
 * @date 2017/11/22
 */
@Table("USER")
public class User {
    private Long id;

    private String name;

    @Column("age")
    private int age;

    public User() {
    }

    private User(Builder builder) {
        setId(builder.id);
        setName(builder.name);
        setAge(builder.age);
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public static final class Builder {
        private Long id;
        private String name;
        private int age;

        private Builder() {
        }

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder age(int age) {
            this.age = age;
            return this;
        }

        public User build() {
            return new User(this);
        }
    }
}
