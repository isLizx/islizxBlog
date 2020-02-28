package com.islizx.entity;

import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author lizx
 * @date 2020-01-30 - 12:05
 */
@Data
public class Blog {
    private Integer id;

    private Boolean appreciation;

    private Boolean commentabled;

    private String content;

    private Date createTime;

    private String description;

    private String firstPicture;

    private String flag;

    private Integer published;

    private Boolean recommend;

    private Boolean shareStatement;

    private String title;

    private Date updateTime;

    private Integer views;

    private Integer typeId;

    private Integer userId;

    private Integer commentCount;

    private Integer likeCount;

    /**
     * 文章类型
     * post  文章
     * page  页面
     */
    private Integer postType;

    private String url;



    private Type type;

    private User user;

    private List<Tag> tags = new ArrayList<>();

    private List<Integer> tagIdList;

/*    private String tagIds;*/


    public void init() {
        this.tagIdList = tagsToIds(this.getTags());
    }

    //1,2,3
    private List<Integer> tagsToIds(List<Tag> tags) {
        if (!tags.isEmpty()) {
            List<Integer> taglist = new ArrayList<Integer>();
            for (Tag tag : tags) {
                taglist.add(tag.getId());
            }
            return taglist;
        } else {
            return tagIdList;
        }
    }
}
