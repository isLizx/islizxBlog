package com.islizx.entity;

import lombok.Data;

import java.util.Date;

/**
 * @author lizx
 * @date 2020-02-07 - 15:27
 */
@Data
public class Attachment {

    private Integer id;
    /**
     * 附件名
     */
    private String attachName;

    /**
     * 附件路径
     */
    private String attachPath;

    /**
     * 附件缩略图路径
     */
    private String attachSmallPath;

    /**
     * 附件类型
     */
    private String attachType;

    /**
     * 附件后缀
     */
    private String attachSuffix;

    /**
     * 附件大小
     */
    private String attachSize;

    /**
     * 附件长宽
     */
    private String attachWh;

    /**
     * 附件来源，0：上传，1：外部链接
     */
    private Integer attachOrigin = 0;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 原始大小，即不进行换算
     *
     */
    private Long rawSize;


}
