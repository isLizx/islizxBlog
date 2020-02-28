package com.islizx.mapper;

import com.islizx.entity.Comment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;

/**
 * @author lizx
 * @date 2020-02-13 - 22:10
 */
@Mapper
public interface CommentMapper {

    /**
     * 获得所有的评论
     * @param criteria
     * @return
     */
    List<Comment> findAll(HashMap<String, Object> criteria);

    /**
     * 根据id删除
     * @param id
     * @return
     */
    Integer deleteById(Integer id);

    /**
     * 获得评论总数
     * @param pass
     * @return
     */
    Integer countComment(@Param(value = "pass") Integer pass);

    /**
     * 添加评论
     * @param comment
     * @return
     */
    Integer insertComment(Comment comment);

    /**
     * 查询前limit条评论
     *
     * @param limit 查询数量
     * @return 评论列表
     */
    List<Comment> findLatestCommentByLimit(Integer limit);

    /**
     * 查找一条博客下面所有的顶级评论
      * @param blogId
     * @return
     */
    List<Comment> findByBlogIdAndParentCommentNull(Integer blogId);

    /**
     * 查找一条博客下面所有的评论
     * @param blogId
     * @return
     */
    List<Comment> findByBlogId(Integer blogId);

    /**
     * 更新评论状态
     *
     * @param id 评论Id
     * @param pass    状态
     * @return 影响行数
     */
    Integer updateCommentPass(@Param("id") Integer id,
                                @Param("pass") Integer pass);

    /**
     * 根据id获得
     * @param id
     * @return
     */
    Comment getById(@Param("commentPublished") Integer commentPublished, @Param("id") Integer id);

    /**
     * 获得子评论Id列表
     *
     * @param id 评论pathTrace封装
     * @return 评论Id列表
     */
    List<Integer> selectChildCommentIds(Integer id);

    /**
     * 批量查询
     * @param ids
     * @return
     */
    List<Comment> selectByIds(@Param("ids") List<Integer> ids);
}
