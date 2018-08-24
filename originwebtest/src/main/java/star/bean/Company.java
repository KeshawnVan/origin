package star.bean;

import com.alibaba.fastjson.annotation.JSONField;

public class Company extends BaseDTO {

    private Long id;

    @JSONField(name = "cName")
    private String name;

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
}
