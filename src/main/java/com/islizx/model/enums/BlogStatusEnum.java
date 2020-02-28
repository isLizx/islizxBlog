package com.islizx.model.enums;

/**
 * @author lizx
 * @date 2020-02-06 - 22:00
 */
public enum BlogStatusEnum {

    /**
     * 已发布
     */
    PUBLISHED(0),

    /**
     * 草稿
     */
    DRAFT(1),

    /**
     * 回收站
     */
    TRASH(2);


    private Integer code;

    BlogStatusEnum(Integer code) {
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }
}
