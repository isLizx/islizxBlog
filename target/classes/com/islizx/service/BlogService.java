package com.islizx.service;

import com.github.pagehelper.PageInfo;
import com.islizx.entity.Blog;
import com.islizx.entity.Comment;
import com.islizx.model.dto.CountDTO;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lizx
 * @date 2020-01-31 - 18:28
 */
public interface BlogService {

    /**
     * 获取文章总数
     *
     * @param published 状态
     * @return 数量
     */
    Integer countBlog(Integer published, Integer postType);

    /**
     * 获取评论总数
     *
     * @return 数量
     */
    Integer countBlogComment();

    /**
     * 获得浏览量总数
     *
     * @return 数量
     */
    Integer countBlogView();

    /**
     * 统计有这个分类的文章数
     *
     * @param id 分类ID
     * @return 数量
     */
    Integer countBlogByTypeId(Integer id);

    /**
     * 统计有这个标签的文章数
     *
     * @param tagId 标签ID
     * @return 数量
     */
    Integer countBlogByTagId(Integer tagId);


    /**
     * 获得所有文章不分页
     *
     * @param criteria 查询条件
     * @return 列表
     */
    List<Blog> listBlog(HashMap<String, Object> criteria);

    /**
     * 获得最新文章
     *
     * @param limit 查询数量
     * @return 列表
     */
    List<Blog> listRecentBlog(Integer limit);


    /**
     * 修改文章详细信息
     *
     * @param blog 文章
     */
    void updateBlogDetail(Blog blog);

    /**
     * 修改文章简单信息
     *
     * @param blog 文章
     */
    void updateBlog(Blog blog);

    /**
     * 批量删除文章
     *
     * @param ids 文章ID
     */
    void deleteBlogBatch(List<Integer> ids);

    /**
     * 删除文章
     *
     * @param id 文章ID
     */
    void deleteBlog(Integer id);

    /**
     * 分页显示
     *
     * @param pageIndex 第几页开始
     * @param pageSize  一页显示多少
     * @param criteria  查询条件
     * @return 文章列表
     */
    PageInfo<Blog> pageBlog(Integer pageIndex,
                               Integer pageSize,
                               HashMap<String, Object> criteria);

    /**
     * 文章详情页面显示
     *
     * @param published 状态
     * @param id     文章ID
     * @return 文章
     */
    Blog getBlogByPublishedAndId(Integer published,Integer postType, Integer id);

    /**
     * 获取访问量较多的文章
     *
     * @param limit 查询数量
     * @return 列表
     */
    List<Blog> listBlogByViewCount(Integer limit);

    /**
     * 获得上一篇文章
     *
     * @param id 文章ID
     * @return 文章
     */
    Blog getAfterBlog(Integer published, Integer postType, Integer id);

    /**
     * 获得下一篇文章
     *
     * @param id 文章ID
     * @return 文章
     */
    Blog getPreBlog(Integer published, Integer postType, Integer id);

    /**
     * 获得随机文章
     *
     * @param limit 查询数量
     * @return 列表
     */
    List<Blog> listRandomBlog(Integer limit);

    /**
     * 获得评论数较多的文章
     *
     * @param limit 查询数量
     * @return 列表
     */
    List<Blog> listBlogByCommentCount(Integer limit);

    /**
     * 添加文章
     *
     * @param blog 文章
     */
    void insertBlog(Blog blog);


    /**
     * 更新文章的评论数
     *
     * @param blogId 文章ID
     */
    void updateCommentCount(Integer blogId);

    /**
     * 获得最后更新记录
     *
     * @return 文章
     */
    Blog getLastUpdateBlog();

    /**
     * 获得相关文章
     *
     * @param typeId 分类ID
     * @param limit  查询数量
     * @return 列表
     */
    List<Blog> listBlogByTypeId(Integer typeId, Integer limit);

    /**
     * 获得相关文章
     *
     * @param cateIds 分类ID集合
     * @param limit   数量
     * @return 列表
     */
//    List<Blog> listBlogByCategoryIds(List<Integer> cateIds, Integer limit);


    /**
     * 根据文章ID获得分类ID列表
     *
     * @param blogId 文章Id
     * @return 列表
     */
//    List<Integer> listTypeIdByBlogId(Integer blogId);

    /**
     * 获得所有的文章
     *
     * @return 列表
     */
    List<Blog> listAllNotWithContent();

    /**
     * 更新文章评论数
     * @param id
     */
    void resetCommentSize(Integer id);


    /**
     * 批量查询
     * @param ids
     * @return
     */
    List<Blog> findByBatchIds(List<Integer> ids);

    /**
     * 根据url查找页面
     * @return
     */
    Blog getBlogByUrl(String url, Integer postType);


    CountDTO getAllCount();

    /**
     * 获取并转为html
     * @return
     */
    Blog getAndConvert(Integer published, Integer postType, Integer id);

    /**
     * 修改文章浏览
     * @param id
     */
    void updateBlogView(Integer id);


    /**
     * 修改点赞数
     * @param id
     */
    void incrBlogLikes(Integer id);

    /**
     * 总结文章
     *
     * @return
     */
    LinkedHashMap<String, List<Blog>> archiveBlog();
}
