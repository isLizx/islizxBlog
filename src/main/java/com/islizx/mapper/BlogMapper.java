package com.islizx.mapper;

import com.islizx.entity.Blog;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;

/**
 * @author lizx
 * @date 2020-01-30 - 15:48
 */
@Mapper
public interface BlogMapper {
    /**
     * 根据ID删除
     *
     * @param id 文章ID
     * @return 影响函数
     */
    Integer deleteById(Integer id);

    /**
     * 添加文章
     *
     * @param blog 文章
     * @return 文章
     */
    Integer insert(Blog blog);

    /**
     * 更新文章
     *
     * @param blog 文章
     * @return 影响行数
     */
    Integer update(Blog blog);

    /**
     * 获得所有的文章
     *
     * @param criteria 查询条件
     * @return 文章列表
     */
    List<Blog> findAll(HashMap<String, Object> criteria);

    /**
     * 文章归档
     * @return
     */
    List<Blog> listAllNotWithContent();

    /**
     * 获取文章总数
     *
     * @return 数量
     */
    Integer countBlog(@Param(value = "published") Integer published,@Param(value = "postType") Integer postType);

    /**
     * 获得留言总数
     *
     * @return 数量
     */
    Integer countBlogComment();

    /**
     * 获得浏览量总数
     *
     * @return 文章数量
     */
    Integer countBlogView();

    /**
     * 获得所有文章(文章归档)
     *
     * @return 文章列表
     */
    List<Blog> listBlog();

    /**
     * 根据id查询用户信息
     *
     * @param published 状态
     * @param id 文章ID
     * @return 文章
     */
    Blog getBlogByPublishedAndId(@Param(value = "published") Integer published,@Param(value = "postType") Integer postType, @Param(value = "id") Integer id);

    /**
     * 分页操作
     *
     * @param published    状态
     * @param pageIndex 从第几页开始
     * @param pageSize  数量
     * @return 文章列表
     */
    @Deprecated
    List<Blog> pageBlog(@Param(value = "published") Boolean published,
                              @Param(value = "pageIndex") Integer pageIndex,
                              @Param(value = "pageSize") Integer pageSize);


    /**
     * 获得访问最多的文章(猜你喜欢)
     *
     * @param limit 查询数量
     * @return 文章列表
     */
    List<Blog> listBlogByViewCount(@Param(value = "limit") Integer limit);

    /**
     * 获得下一篇文章
     *
     * @param id 文章ID
     * @return 文章
     */
    Blog getAfterBlog( @Param(value = "published") Integer published, @Param(value = "postType") Integer postType, @Param(value = "id") Integer id);

    /**
     * 获得上一篇文章
     *
     * @param id 文章ID
     * @return 文章
     */
    Blog getPreBlog( @Param(value = "published") Integer published, @Param(value = "postType") Integer postType, @Param(value = "id") Integer id);

    /**
     * 获得随机文章
     *
     * @param limit 查询数量
     * @return 文章列表
     */
    List<Blog> listRandomBlog(@Param(value = "limit") Integer limit);

    /**
     * 热评文章
     *
     * @param limit  查询数量
     * @return 文章列表
     */
    List<Blog> listBlogByCommentCount(@Param(value = "limit") Integer limit);



    /**
     * 更新文章的评论数
     *
     * @param id 文章ID
     */
    void updateCommentCount(@Param(value = "id") Integer id);

    /**
     * 获得最后更新的记录
     *
     * @return 文章
     */
    Blog getLastUpdateBlog();

    /**
     * 用户的文章数
     *
     * @param id 用户ID
     * @return 数量
     */
    Integer countBlogByUser(@Param(value = "id") Integer id);

    /**
     * 根据分类ID
     *
     * @param id 分类ID
     * @param limit      查询数量
     * @return 文章列表
     */
    List<Blog> findBlogByTypeId(@Param("id") Integer id,
                                          @Param("limit") Integer limit);

    /**
     * 根据分类ID
     *
     * @param typeIds 分类ID集合
     * @param limit       查询数量
     * @return 文章列表
     */
//    List<Blog> findBlogByTypeIds(@Param("typeIds") List<Integer> typeIds,
//                                           @Param("limit") Integer limit);

    /**
     * 获得最新文章
     *
     * @param limit 查询数量
     * @return 列表
     */
    List<Blog> listBlogByLimit(Integer limit);

    /**
     * 批量删除文章
     *
     * @param ids 文章Id列表
     * @return 影响行数
     */
    Integer deleteBatch(@Param("ids") List<Integer> ids);

    /**
     * 根据typeid获取文章数
     *
     * @param id 分类id
     * @return
     */
    Integer countBlogByTypeId(Integer id);


    /**
     * 批量查询
     * @param ids
     * @return
     */
    List<Blog> selectByIds(@Param("ids") List<Integer> ids);

    /**
     * 根据url查找页面
     * @param url
     * @return
     */
    Blog getBlogByUrl(@Param("url") String url,@Param("postType") Integer postType);


    /**
     * 修改浏览数
     * @param id
     */
    void incrBlogViews(@Param("id")Integer id,@Param("views") Integer views);


    /**
     * 修改点赞数
     * @param id
     */
    void incrBlogLikes(Integer id);

    List<String> findGroupYear();

    List<Blog> findByYear(String year);
}
