package com.islizx.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author lizx
 * @date 2020-01-30 - 12:32
 */
@Data
public class Comment implements Serializable {

    private static final long serialVersionUID = -1160612910766266139L;
    private Integer id;

    private String nickname;

    private String email;

    private String content;

    private String avatar;

    private Date createTime;

    private Integer blogId;

    private Integer parentCommentId;

    private boolean adminComment;

    private String ip;

    private Integer pass;



    private Blog blog;

    private Comment parentComment;

    private List<Comment> replyComments = new ArrayList<>();
}
