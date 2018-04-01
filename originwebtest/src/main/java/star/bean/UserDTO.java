package star.bean;

import star.annotation.bean.Component;
import star.annotation.bean.Inject;
import star.annotation.repository.Table;
import star.service.TestService;

import java.time.Instant;

/**
 * @author keshawn
 * @date 2017/11/22
 */
@Table("USER")
@Component
public class UserDTO extends BaseDTO{

    private Long id;

    private String name;

    private Integer age;

    private Instant birthday;

    private Status status;

    private Long createId;

    public UserDTO() {
    }

    public UserDTO(Long id, String name, int age, Instant birthday, Status status) {
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

    @Override
    public Long getCreateId() {
        return createId;
    }

    @Override
    public void setCreateId(Long createId) {
        this.createId = createId;
    }
}
