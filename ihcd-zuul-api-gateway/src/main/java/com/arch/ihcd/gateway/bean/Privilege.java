package com.arch.ihcd.gateway.bean;

public class Privilege {
    private Integer id;
    private String privilege;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPrivilege() {
        return privilege;
    }

    public void setPrivilege(String privilege) {
        this.privilege = privilege;
    }

    @Override
    public String toString() {
        return "{" +
                "id:" + id +
                ", privilege:'" + privilege + "'}";
    }
}
