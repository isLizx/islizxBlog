package com.islizx.model.enums;

/**
 * @author lizx
 * @date 2020-02-16 - 11:20
 */
public enum WidgetTypeEnum {
    /**
     * 正文侧边栏小工具
     */
    POST_DETAIL_SIDEBAR_WIDGET(0),

    /**
     * 页脚小工具
     */
    FOOTER_WIDGET(1);

    private Integer code;

    WidgetTypeEnum(Integer code) {
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}
