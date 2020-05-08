package com.islizx.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author lizx
 * @date 2020-02-21 - 14:34
 */
@Data
public class Log implements Serializable {

    private static final long serialVersionUID = -7506956161988702794L;
    private Integer id;

    /**
     * 方法操作名称
     */
    private String name;

    /**
     * 日志类型
     */
    private String logType;

    /**
     * 请求路径
     */
    private String requestUrl;

    /**
     * 请求类型
     */
    private String requestType;

    /**
     * 请求参数
     */
    private String requestParam;

    /**
     * 请求用户
     */
    private String username;

    /**
     * ip
     */
    private String ip;

    /**
     * ip信息
     */
    private String ipInfo;

    /**
     * 花费时间
     */
    private Integer costTime;

    /**
     * 创建时间
     */
    private Date createTime;
}
