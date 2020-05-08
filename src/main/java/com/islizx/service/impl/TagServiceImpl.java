package com.islizx.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.islizx.entity.Tag;
import com.islizx.entity.Type;
import com.islizx.mapper.BlogTagRefMapper;
import com.islizx.mapper.TagMapper;
import com.islizx.service.TagService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.List;

/**
 * @author lizx
 * @date 2020-01-31 - 19:28
 */
@Service
@Slf4j
public class TagServiceImpl implements TagService {

    @Autowired(required = false)
    private TagMapper tagMapper;

    @Autowired(required = false)
    private BlogTagRefMapper blogTagRefMapper;

    @Override
    public Integer countTag() {
        return tagMapper.countTag();
    }

    @Override
    public List<Tag> listTag() {
        List<Tag> tagList = null;
        try {
            tagList = tagMapper.listTag(null);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("获得所有标签失败, cause:{}", e);
        }
        return tagList;
    }

    @Override
    public List<Tag> listTagWithCount(Integer limit) {
        List<Tag> tagList = null;
        try {
            tagList = tagMapper.listTag(limit);
            for (int i = 0; i < tagList.size(); i++) {
                Integer count = blogTagRefMapper.countBlogByTagId(tagList.get(i).getId());
                tagList.get(i).setBlogCount(count);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("获得所有标签失败, cause:{}", e);
        }
        return tagList;
    }

    @Override
    public List<Tag> listTagWithCountAtHome(Integer limit) {
        List<Tag> tagList = null;
        try {
            tagList = tagMapper.listTag(limit);
            for (int i = 0; i < tagList.size(); i++) {
                Integer count = blogTagRefMapper.countBlogByTagIdByHome(tagList.get(i).getId());
                tagList.get(i).setBlogCount(count);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("获得所有标签失败, cause:{}", e);
        }
        return tagList;
    }


    @Override
    public Tag getTagById(Integer id) {
        Tag tag = null;
        try {
            tag = tagMapper.getTagById(id);
        } catch (Exception e) {            e.printStackTrace();
            log.error("根据ID获得标签失败, id:{}, cause:{}", id, e);
        }
        return tag;
    }

    @Override
    public Tag insertTag(Tag tag) {
        try {
            tagMapper.insert(tag);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("添加标签失败, tag:{}, cause:{}", tag, e);
        }
        return tag;
    }

    @Override
    public void updateTag(Tag tag) {
        try {
            tagMapper.update(tag);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("更新标签失败, tag:{}, cause:{}", tag, e);
        }
    }

    @Override
    public void deleteTag(Integer id) {
        try {
            tagMapper.deleteById(id);
            blogTagRefMapper.deleteByTagId(id);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("删除标签失败, id:{}, cause:{}", id, e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
    }

    @Override
    public Tag getTagByName(String name) {
        Tag tag = null;
        try {
            tag = tagMapper.getTagByName(name);
        } catch (Exception e) {            e.printStackTrace();
            log.error("根据名称获得标签, name:{}, cause:{}", name, e);
        }
        return tag;
    }

    @Override
    public List<Tag> listTagByBlogId(Integer blogId) {
        List<Tag> tagList = null;
        try {
            tagList = blogTagRefMapper.listTagByBlogId(blogId);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("根据文章ID获得标签失败，articleId:{}, cause:{}", blogId, e);
        }
        return tagList;
    }

    @Override
    public PageInfo<Tag> pageTag(Integer pageIndex, Integer pageSize) {
        PageHelper.startPage(pageIndex, pageSize);
        List<Tag> tagList = tagMapper.listTag(null);
        for (int i = 0; i < tagList.size(); i++) {
            Integer count = blogTagRefMapper.countBlogByTagId(tagList.get(i).getId());
            tagList.get(i).setBlogCount(count);
        }
        return new PageInfo<>(tagList, 5);
    }
}
