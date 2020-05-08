package com.islizx.entity;

import com.sun.org.apache.xpath.internal.operations.Bool;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author lizx
 * @date 2020-01-30 - 11:49
 */
@Data
public class User implements Serializable {
    private static final long serialVersionUID = -7813104768329168886L;
    private Integer id;

    private String avatar;

    private String email;

    private String nickname;

    private String password;

    private Boolean type;

    private String username;

    private String url;

    private String lastLoginIp;

    private Date registerTime;

    private Date updateTime;

    private Date lastLoginTime;

    private Boolean status;

    private String description;

    /**
     * 文章数量(不是数据库字段)
     */
    private Integer blogCount;


    public Boolean getType() {
        if(type == null){
            return false;
        }
        return type;
    }

    public Boolean getStatus() {
        if(status == null){
            return false;
        }
        return status;
    }
}
