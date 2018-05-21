package com.gsafety.entity;

import javax.persistence.*;
import java.util.UUID;

/**
 *
 * @author Liugan
 * @date 2017/2/15
 * Model 用户
 */
@Entity
@Table(name = "tbl_user")
public class User {
    @Id
    private String userId = UUID.randomUUID().toString().replaceAll("-", "");

    private String userEmail;

    private String userName;

    private String userPass;

    /** 0 表示未激活*/
    private int state = 0 ;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPass() {
        return userPass;
    }

    public void setUserPass(String userPass) {
        this.userPass = userPass;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
