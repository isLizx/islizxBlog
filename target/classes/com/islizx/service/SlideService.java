package com.islizx.service;


import com.islizx.entity.Slide;

import java.util.List;

/**
 * @author lizx
 * @date 2020-02-15 - 23:37
 */
public interface SlideService {

    /**
     * 查找所有幻灯片
     * @return
     */
    List<Slide> findAll();


    /**
     * 添加或修改幻灯片
     * @param slide
     * @return
     */
    Slide insertOrUpdate(Slide slide);

    /**
     * 根据id获得幻灯片
     * @param id
     * @return
     */
    Slide get(Integer id);


    /**
     * 根据id删除幻灯片
     * @param id
     * @return
     */
    Integer delete(Integer id);
}
