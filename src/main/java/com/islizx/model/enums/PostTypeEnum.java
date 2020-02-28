package com.islizx.model.enums;

/**
 * @author lizx
 * @date 2020-02-12 - 22:29
 */
public enum PostTypeEnum {
    /**
     * 文章
     */
    POST_TYPE_POST(0),
    /**
     * 公告
     */
    POST_TYPE_NOTICE(1),

    /**
     * 页面
     */
    POST_TYPE_PAGE(2);

    private Integer code;

    PostTypeEnum(Integer code) {
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }
}
