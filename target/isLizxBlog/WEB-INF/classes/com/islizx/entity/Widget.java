package com.islizx.entity;

import lombok.Data;

/**
 * @author lizx
 * @date 2020-02-16 - 11:01
 */
@Data
public class Widget {

    private Integer id;
    /**
     * 小工具标题
     */
    private String widgetTitle;

    /**
     * 小工具内容
     */
    private String widgetContent;

    /**
     * 是否显示(1是，0否)
     */
    private Integer isDisplay = 1;

    /**
     * 位置
     */
    private Integer widgetType;
}
