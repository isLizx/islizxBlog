package com.islizx.service;

import com.github.pagehelper.PageInfo;
import com.islizx.entity.Type;

import java.util.HashMap;
import java.util.List;

/**
 * @author lizx
 * @date 2020-02-01 - 13:12
 */
public interface TypeService {
    /**
     * 获得分类总数
     *
     * @return
     */
    Integer countType();


    /**
     * 获得分类列表
     *
     * @return 分类列表
     */
    List<Type> listType();


    /**
     * 删除分类
     *
     * @param id ID
     */

    void deleteType(Integer id);

    /**
     * 根据id查询分类信息
     *
     * @param id     ID
     * @return 分类
     */
    Type getTypeById(Integer id);

    /**
     * 添加分类
     *
     * @param type 分类
     * @return 分类
     */
    Type insertType(Type type);

    /**
     * 更新分类
     *
     * @param type 分类
     */
    void updateType(Type type);

    /**
     * 根据分类名获取分类
     *
     * @param name 名称
     * @return 分类
     */
    Type getTypeByName(String name);

    /**
     * 获得每个分类下文章的总数
     *
     * @return 分类列表
     */
    List<Type> listTypeWithCount();

    /**
     * 获得每个分类下文章的总数 前台
     *
     * @return 分类列表
     */
    List<Type> listTypeWithCountAtHome();

    /**
     * 分页获取分类
     * @param pageIndex
     * @param pageSize
     * @return
     */
    PageInfo<Type> pageType(Integer pageIndex,Integer pageSize);

}
