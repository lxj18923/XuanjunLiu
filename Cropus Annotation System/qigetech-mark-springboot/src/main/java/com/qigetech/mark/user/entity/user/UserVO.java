package com.qigetech.mark.user.entity.user;

import lombok.Data;

import java.util.Date;

@Data
public class UserVO {

    private long id;

    private String name;

    private String stuName;
    private long stuId;
    private String major;

    private String remark;


    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getStuName() {
        return stuName;
    }

    public void setStuName(String stuName) {
        this.stuName = stuName;
    }

    public long getStuId() {
        return stuId;
    }

    public void setStuId(long stuId) {
        this.stuId = stuId;
    }



    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 创建用户
     */
    private String createUser;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 更新用户
     */
    private String updateUser;

}
