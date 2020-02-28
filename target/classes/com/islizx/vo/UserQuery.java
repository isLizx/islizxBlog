package com.islizx.vo;

import lombok.Data;

/**
 * @author lizx
 * @date 2020-02-05 - 23:35
 */
@Data
public class UserQuery {
    private Integer id;

    private String username;

    private String nickname;

    private String email;

    private String avatar;

    private String url;

    private String description;

    private Boolean status;

}
