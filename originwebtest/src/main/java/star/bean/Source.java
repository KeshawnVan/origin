package star.bean;

import star.annotation.bean.Transfer;

/**
 * @author keshawn
 * @date 2018/3/8
 */
public class Source {

    private Long id;

    private String name;

    private String lists;

    @Transfer("dd")
    private Integer ss;

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

    public String getLists() {
        return lists;
    }

    public void setLists(String lists) {
        this.lists = lists;
    }

    public Integer getSs() {
        return ss;
    }

    public void setSs(Integer ss) {
        this.ss = ss;
    }
}
