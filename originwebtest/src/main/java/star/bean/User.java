package star.bean;

import star.annotation.repository.Id;
import star.annotation.repository.Table;

import java.time.Instant;

/**
 * @author keshawn
 * @date 2017/11/22
 */
@Table("USER")
public class User extends BaseDTO{
    @Id
    private Long id;

    private String name;

    private Integer age;

    private Instant birthday;

    private Status status;

    public User() {
    }

    public User(Long id, String name, Integer age, Instant birthday, Status status) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.birthday = birthday;
        this.status = status;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", birthday=" + birthday +
                ", status=" + status +
                '}';
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

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }


    public Instant getBirthday() {
        return birthday;
    }

    public void setBirthday(Instant birthday) {
        this.birthday = birthday;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

}
