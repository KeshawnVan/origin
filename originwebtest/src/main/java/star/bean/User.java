package star.bean;

import star.annotation.Column;
import star.annotation.Id;
import star.annotation.Table;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author keshawn
 * @date 2017/11/22
 */
@Table("USER")
public class User {
    @Id
    private Long id;

    private String name;

    @Column("age")
    private int age;

    private Instant birthday;

    private Status status;

    public User() {
    }

    public User(Long id, String name, int age, Instant birthday, Status status) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.birthday = birthday;
        this.status = status;
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
