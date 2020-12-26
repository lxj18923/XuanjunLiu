package com.qigetech.mark.user.entity.user;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author liuxuanjun
 * @since 2018-11-14
 */
@Data
@EqualsAndHashCode
@Accessors(chain = true)
@TableName("qige_user")
public class User{

    private static final long serialVersionUID = 1L;

    @TableId
    private long id;

    private String name;

    private String password;
    private String salt;
    private String language;
    private String remark;
    private String stuName;//姓名
    private long stuId;//学号
    private String major;//专业








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
