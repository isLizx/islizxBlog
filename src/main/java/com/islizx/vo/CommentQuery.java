package com.islizx.vo;

import lombok.Data;

/**
 * @author lizx
 * @date 2020-02-14 - 15:41
 */
@Data
public class CommentQuery {
    private Integer pass;

    private String keywords;

    private String searchType;

    private String sort;

    private String order;
}
