package com.islizx.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.islizx.entity.*;
import com.islizx.mapper.*;
import com.islizx.model.dto.CountDTO;
import com.islizx.model.enums.BlogStatusEnum;
import com.islizx.model.enums.CommentStatusEnum;
import com.islizx.model.enums.PostTypeEnum;
import com.islizx.service.BlogService;
import com.islizx.util.MyUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @author lizx
 * @date 2020-01-31 - 18:50
 */
@Service
@Slf4j
@Transactional(propagation= Propagation.REQUIRED, rollbackFor=Exception.class)
public class BlogServiceImpl implements BlogService {

    @Autowired(required = false)
    private BlogMapper blogMapper;

    @Autowired(required = false)
    private BlogTagRefMapper blogTagRefMapper;

    @Autowired(required = false)
    private TypeMapper typeMapper;

    @Autowired(required = false)
    private TagMapper tagMapper;

    @Autowired(required = false)
    private CommentMapper commentMapper;

    @Autowired(required = false)
    private UserMapper userMapper;


    @Override
    public Integer countBlog(Integer published, Integer posttype) {
        Integer count = 0;
        try {
            count = blogMapper.countBlog(published, posttype);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("根据是否已发布统计文章数, published:{}, cause:{}", published, e);
        }
        return count;
    }

    @Override
    public Integer countBlogComment() {
        Integer count = 0;
        try {
            count = blogMapper.countBlogComment();
        } catch (Exception e) {
            e.printStackTrace();
            log.error("统计文章评论数失败, cause:{}", e);
        }
        return count;
    }
    @Override
    public Integer countBlogView() {
        Integer count = 0;
        try {
            count = blogMapper.countBlogView();
        } catch (Exception e) {
            e.printStackTrace();
            log.error("统计文章访问量失败, cause:{}", e);
        }
        return count;
    }

    @Override
    public Integer countBlogByTypeId(Integer typeId) {
        Integer count = 0;
        try {
            count = blogMapper.countBlogByTypeId(typeId);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("根据分类统计文章数量失败, typeId:{}, cause:{}", typeId, e);
        }
        return count;
    }

    @Override
    public Integer countBlogByTagId(Integer tagId) {
        return blogTagRefMapper.countBlogByTagId(tagId);
    }

    @Override
    public List<Blog> listBlog(HashMap<String, Object> criteria) {
        return blogMapper.findAll(criteria);
    }

    @Override
    public List<Blog> listRecentBlog(Integer limit) {
        return blogMapper.listBlogByLimit(limit);
    }

    @Override
    @CacheEvict(value= "blog", allEntries=true)
    @Transactional(rollbackFor = Exception.class)
    public void updateBlogDetail(Blog blog) {
        blog.setUpdateTime(new Date());
        blogMapper.update(blog);
        if (blog.getTags() != null) {
            //删除标签和文章关联
            blogTagRefMapper.deleteByBlogId(blog.getId());
            //添加标签和文章关联
            for (int i = 0; i < blog.getTags().size(); i++) {
                BlogTagRef blogTagRef = new BlogTagRef(blog.getId(), blog.getTags().get(i).getId());
                blogTagRefMapper.insert(blogTagRef);
            }
        }

    }

    @Override
    @CacheEvict(value= "blog", allEntries=true)
    public void updateBlog(Blog blog) {
        blogMapper.update(blog);
    }

    @Override
    @CacheEvict(value= "blog", allEntries=true)
    public void deleteBlogBatch(List<Integer> ids) {
        blogMapper.deleteBatch(ids);
    }

    @Override
    @CacheEvict(value= "blog", allEntries=true)
    public void deleteBlog(Integer id) {
        blogMapper.deleteById(id);
        blogTagRefMapper.deleteByBlogId(id);
    }

    @Override
    public PageInfo<Blog> pageBlog(Integer pageIndex, Integer pageSize, HashMap<String, Object> criteria) {
        PageHelper.startPage(pageIndex, pageSize);
        List<Blog> blogList = blogMapper.findAll(criteria);
        for (int i = 0; i < blogList.size(); i++) {
            //封装Type
            if(blogList.get(i).getTypeId() != null){
                Type type = typeMapper.getTypeById(blogList.get(i).getTypeId());
                blogList.get(i).setType(type);
            }
            if(blogList.get(i).getId() != null){
                List<Tag> tagList = blogTagRefMapper.listTagByBlogId(blogList.get(i).getId());
                blogList.get(i).setTags(tagList);
                blogList.get(i).init();
            }
            if(blogList.get(i).getUserId() != null){
                User user = userMapper.getUserById(blogList.get(i).getUserId());
                blogList.get(i).setUser(user);
            }
        }
        return new PageInfo<>(blogList, 5);
    }

    @Override
    public Blog getBlogByPublishedAndId(Integer published,Integer postType, Integer id) {
        System.out.println("===============getBlogByPublishedAndId执行一次============" + published + postType + id);
        Blog blog = blogMapper.getBlogByPublishedAndId(published,postType, id);
        if (blog != null) {
            if(blog.getTypeId() != null){
                Type type = typeMapper.getTypeById(blog.getTypeId());
                blog.setType(type);
            }
            if(blog.getId() != null){
                List<Tag> tagList = blogTagRefMapper.listTagByBlogId(blog.getId());
                blog.setTags(tagList);
                blog.init();
            }
            if(blog.getUserId() != null){
                User user = userMapper.getUserById(blog.getUserId());
                blog.setUser(user);
            }
        }
        return blog;
    }

    @Override
    @Cacheable(value = "blog", key = "'listBlogByViewCount_limit_' + #limit")
    public List<Blog> listBlogByViewCount(Integer limit) {
        return blogMapper.listBlogByViewCount(limit);
    }

    @Override
    @Cacheable(value = "blog", key = "'afterBlog_published_' + #published + '_postType_' + #postType + '_id_' + #id")
    public Blog getAfterBlog(Integer published, Integer postType, Integer id) {
        return blogMapper.getAfterBlog(published,postType,id);
    }

    @Override
    @Cacheable(value = "blog", key = "'preBlog_published_' + #published + '_postType_' + #postType + '_id_' + #id")
    public Blog getPreBlog(Integer published, Integer postType, Integer id) {
        return blogMapper.getPreBlog(published,postType,id);
    }

    @Override
    public List<Blog> listRandomBlog(Integer limit) {
        return blogMapper.listRandomBlog(limit);
    }

    @Override
    public List<Blog> listBlogByCommentCount(Integer limit) {
        return blogMapper.listBlogByCommentCount(limit);
    }

    @Override
    @CacheEvict(value= "blog", allEntries=true)
    public void insertBlog(Blog blog) {
        //添加文章
        blog.setCreateTime(new Date());
        blog.setUpdateTime(new Date());
        blog.setViews(0);
        blog.setLikeCount(0);
        blog.setCommentCount(0);
        blogMapper.insert(blog);
        //添加标签和文章关联
        for (int i = 0; i < blog.getTags().size(); i++) {
            BlogTagRef blogTagRef = new BlogTagRef(blog.getId(), blog.getTags().get(i).getId());
            blogTagRefMapper.insert(blogTagRef);
        }
    }

    @Override
    @CacheEvict(value= "blog", allEntries=true)
    public void updateCommentCount(Integer blogId) {
        blogMapper.updateCommentCount(blogId);
    }

    @Override
    @Cacheable(value = "blog")
    public Blog getLastUpdateBlog() {
        return blogMapper.getLastUpdateBlog();
    }

    @Override
    @Cacheable(value = "blog", key = "'blog_typeId_' + #typeId + '_limit_' + #limit")
    public List<Blog> listBlogByTypeId(Integer typeId, Integer limit) {
        return blogMapper.findBlogByTypeId(typeId, limit);
    }

    @Override
    @Cacheable(value = "blog")
    public List<Blog> listAllNotWithContent() {
        return blogMapper.listAllNotWithContent();
    }

    @Override
    @CacheEvict(value= "blog", allEntries=true)
    public void resetCommentSize(Integer id) {
        blogMapper.updateCommentCount(id);
    }

    @Override
    public List<Blog> findByBatchIds(List<Integer> ids) {
        return blogMapper.selectByIds(ids);
    }

    @Override
    @Cacheable(value = "blog", key = "'blog_url_' + #url + '_postType_' + #postType")
    public Blog getBlogByUrl(String url, Integer postType) {
        Blog blog =  blogMapper.getBlogByUrl(url, postType);
        if (blog != null) {
            if(blog.getTypeId() != null){
                Type type = typeMapper.getTypeById(blog.getTypeId());
                blog.setType(type);
            }
            if(blog.getId() != null){
                List<Tag> tagList = blogTagRefMapper.listTagByBlogId(blog.getId());
                blog.setTags(tagList);
                blog.init();
            }
            if(blog.getUserId() != null){
                User user = userMapper.getUserById(blog.getUserId());
                blog.setUser(user);
            }
        }
        return blog;
    }

    @Override
    public CountDTO getAllCount() {
        CountDTO countDTO = new CountDTO();
        countDTO.setBlogCount(blogMapper.countBlog(BlogStatusEnum.PUBLISHED.getCode(), PostTypeEnum.POST_TYPE_POST.getCode()));
        countDTO.setTypeCount(typeMapper.countType());
        countDTO.setTagCount(tagMapper.countTag());
        countDTO.setCommentCount(commentMapper.countComment(CommentStatusEnum.PUBLISHED.getCode()));
        countDTO.setViewCount(blogMapper.countBlogView());
        return countDTO;
    }

    @Override
    public Blog getAndConvert(Integer published, Integer postType, Integer id) {
        Blog blog = this.getBlogByPublishedAndId(published,postType, id);
        if (blog == null) {
            return null;
        }
        Blog b = new Blog();
        BeanUtils.copyProperties(blog,b);

        String content = b.getContent();
        b.setContent(MyUtils.markdownToHtmlExtensions(content));

        return b;
    }

    @Override
    @CacheEvict(value= "blog", allEntries=true)
    public void updateBlogView(Integer id, Integer views) {
        blogMapper.incrBlogViews(id, views);
    }

    @Override
    @CacheEvict(value= "blog", allEntries=true)
    public void incrBlogLikes(Integer id) {
        blogMapper.incrBlogLikes(id);
    }

    @Override
    @Cacheable(value = "blog")
    public LinkedHashMap<String, List<Blog>> archiveBlog() {
        List<String> years = blogMapper.findGroupYear();
        LinkedHashMap<String, List<Blog>> map = new LinkedHashMap<>();
        for (String year : years) {
            map.put(year, blogMapper.findByYear(year));
        }
        return map;
    }
}
