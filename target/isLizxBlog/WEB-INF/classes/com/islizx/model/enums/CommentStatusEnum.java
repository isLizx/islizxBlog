package com.islizx.model.enums;

/**
 * @author lizx
 * @date 2020-02-14 - 15:30
 */
public enum CommentStatusEnum {
    /**
     * 待审核
     */
    CHECKING(0),
    /**
     * 已发布
     */
    PUBLISHED(1),

    /**
     * 回收站
     */
    RECYCLE(2);

    private Integer code;

    CommentStatusEnum(Integer code) {
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }
}
