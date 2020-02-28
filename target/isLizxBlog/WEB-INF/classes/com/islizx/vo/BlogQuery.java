package com.islizx.vo;

import lombok.Data;

/**
 * @author lizx
 * @date 2020-02-02 - 14:31
 */
@Data
public class BlogQuery {
    private Integer pageIndex;

    private String searchType;

    private String blogSource;

    private Integer published;

    private String sort;

    private String order;

    private String keywords;

}
