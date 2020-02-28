package com.islizx.mapper;

import com.islizx.entity.BlogTagRef;
import com.islizx.entity.Tag;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author lizx
 * @date 2020-01-31 - 19:36
 */
@Mapper
public interface BlogTagRefMapper {
    /**
     * 添加文章和标签关联记录
     * @param record 关联对象
     * @return 影响行数
     */
    int insert(BlogTagRef record);

    /**
     * 根据标签ID删除记录
     * @param tagId 标签ID
     * @return 影响行数
     */
    int deleteByTagId(Integer tagId);

    /**
     * 根据文章ID删除记录
     * @param BlogId 文章ID
     * @return 影响行数
     */
    int deleteByBlogId(Integer BlogId);

    /**
     * 根据标签ID统计文章数
     * @param tagId 标签ID
     * @return 文章数量
     */
    int countBlogByTagId(Integer tagId);

    /**
     * 根据文章获得标签列表
     *
     * @param blogId 文章ID
     * @return 标签列表
     */
    List<Tag> listTagByBlogId(Integer blogId);


    /**
     * 根据标签ID统计文章数 前台
     * @param tagId 标签ID
     * @return 文章数量
     */
    int countBlogByTagIdByHome(Integer tagId);
}
