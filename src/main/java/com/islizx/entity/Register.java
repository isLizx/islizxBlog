package com.islizx.entity;

import java.io.Serializable;

/**
 * @author lizx
 * @date 2020-03-06 - 16:17
 */
public class Register implements Serializable {
    private static final long serialVersionUID = 8639794612968093235L;
    private Long id;

    private String code;

    private Byte state;

    private Integer userId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code == null ? null : code.trim();
    }

    public Byte getState() {
        return state;
    }

    public void setState(Byte state) {
        this.state = state;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
