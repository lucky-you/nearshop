package com.baishan.nearshopclient.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Administrator on 2017/1/10 0010.
 */
@Entity
public class Account {
    @Id(autoincrement = true)
    private Long id;
    public String Phone;
    public String Password;
    public String LoginTime;

    @Generated(hash = 441066441)
    public Account(Long id, String Phone, String Password, String LoginTime) {
        this.id = id;
        this.Phone = Phone;
        this.Password = Password;
        this.LoginTime = LoginTime;
    }

    public Account(String phone, String password, String loginTime) {
        Phone = phone;
        Password = password;
        LoginTime = loginTime;
    }

    @Generated(hash = 882125521)
    public Account() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPhone() {
        return this.Phone;
    }

    public void setPhone(String Phone) {
        this.Phone = Phone;
    }

    public String getPassword() {
        return this.Password;
    }

    public void setPassword(String Password) {
        this.Password = Password;
    }

    public String getLoginTime() {
        return this.LoginTime;
    }

    public void setLoginTime(String LoginTime) {
        this.LoginTime = LoginTime;
    }
}
