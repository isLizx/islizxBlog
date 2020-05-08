package com.islizx.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.islizx.entity.Attachment;
import com.islizx.entity.Type;
import com.islizx.mapper.BlogTagRefMapper;
import com.islizx.mapper.TypeMapper;
import com.islizx.service.TypeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.HashMap;
import java.util.List;

/**
 * @author lizx
 * @date 2020-02-01 - 13:14
 */
@Service
@Slf4j
@Transactional(propagation= Propagation.REQUIRED, rollbackFor=Exception.class)
public class TypeServiceImpl implements TypeService {

    @Autowired(required = false)
    private TypeMapper typeMapper;

    @Override
    public Integer countType() {
        Integer count = 0;
        try {
            count = typeMapper.countType();
        } catch (Exception e) {
            e.printStackTrace();
            log.error("统计分类失败, cause:{}", e);
        }
        return count;
    }

    @Override
    public List<Type> listType() {
        List<Type> typeList = null;
        try {
            typeList = typeMapper.listType();
        } catch (Exception e) {
            e.printStackTrace();
            log.error("根据文章获得分类列表失败, cause:{}", e);
        }
        return typeList;
    }

    @Override
    public void deleteType(Integer id) {
        try {
            typeMapper.deleteType(id);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("删除分类失败, id:{}, cause:{}", id, e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
    }

    @Override
    public Type getTypeById(Integer id) {
        Type type = null;
        try {
            type = typeMapper.getTypeById(id);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("根据分类ID获得分类, id:{}, cause:{}", id, e);
        }
        return type;
    }

    @Override
    public Type insertType(Type type) {
        try {
            typeMapper.insert(type);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("创建分类失败, category:{}, cause:{}", type, e);
        }
        return type;
    }

    @Override
    public void updateType(Type type) {
        try {
            typeMapper.update(type);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("更新分类失败, category:{}, cause:{}", type, e);
        }
    }

    @Override
    public Type getTypeByName(String name) {
        Type type = null;
        try {
            type = typeMapper.getTypeByName(name);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("更新分类失败, category:{}, cause:{}", type, e);
        }
        return type;
    }

    @Override
    public List<Type> listTypeWithCount() {
        List<Type> typeList = null;
        try {
            typeList = typeMapper.listType();
            for (int i = 0; i < typeList.size(); i++) {
                Integer count = typeMapper.getBlogCountWithTypeId(typeList.get(i).getId());
                typeList.get(i).setBlogCount(count);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("根据分类统计文章总数失败, cause:{}", e);
        }
        return typeList;
    }

    @Override
    public List<Type> listTypeWithCountAtHome() {
        List<Type> typeList = null;
        try {
            typeList = typeMapper.listType();
            for (int i = 0; i < typeList.size(); i++) {
                Integer count = typeMapper.getBlogCountWithTypeIdAtHome(typeList.get(i).getId());
                typeList.get(i).setBlogCount(count);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("根据分类统计文章总数失败, cause:{}", e);
        }
        return typeList;
    }

    @Override
    public PageInfo<Type> pageType(Integer pageIndex, Integer pageSize) {
        PageHelper.startPage(pageIndex, pageSize);
        List<Type> typeList = typeMapper.listType();
        for (int i = 0; i < typeList.size(); i++) {
            Integer count = typeMapper.getBlogCountWithTypeId(typeList.get(i).getId());
            typeList.get(i).setBlogCount(count);
        }
        return new PageInfo<>(typeList, 5);
    }
}
