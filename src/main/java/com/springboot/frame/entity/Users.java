package com.springboot.frame.entity;

/**
 * @Author: Administrator
 * @Date: 2021/3/28 10:39
 */
public class Users {

    /**
     * 用户主键id
     */
    private int id;

    /**
     * 用户姓名
     */
    private String name;

    /**
     * 用户描述信息
     */
    private String describe;

    public Users() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    @Override
    public String toString() {
        return "Users{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", describe='" + describe + '\'' +
                '}';
    }
}
