package com.islizx.service;

import com.github.pagehelper.PageInfo;
import com.islizx.entity.Comment;

import java.util.HashMap;
import java.util.List;

/**
 * @author lizx
 * @date 2020-02-14 - 13:11
 */
public interface CommentService {

    // 对指定字段查询
    PageInfo<Comment> pageComment(Integer pageIndex, Integer pageSize, HashMap<String, Object> criteria);

    // 修改评论状态
    Comment updateCommentPass(Integer id, Integer pass);

    // 添加评论
    Comment insertComment(Comment comment);

    // 删除评论
    void deleteComment(Integer commentId);

    // 查找当前博客下的所有评论
    PageInfo<Comment> getListCommentByBlogId(Integer blogPublished,Integer commentPublished, Integer postType,Integer pageIndex, Integer pageSize,Integer blogId);

    // 根据id获取评论
    Comment getById(Integer blogPublished,Integer commentPublished, Integer postType, Integer id);

    // 查找每种状态下的评论数
    Integer countCommentByPass(Integer pass);

    // 批量查询
    List<Comment> findByBatchIds(List<Integer> ids);

    // 查询最新评论
    List<Comment> getCommentLimit(Integer limit);
}
