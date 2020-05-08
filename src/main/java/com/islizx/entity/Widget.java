package com.islizx.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @author lizx
 * @date 2020-02-16 - 11:01
 */
@Data
public class Widget implements Serializable {

    private static final long serialVersionUID = -3390800332821558875L;
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
