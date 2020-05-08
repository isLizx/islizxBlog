package com.islizx.service;

import com.islizx.entity.Widget;

import java.util.List;

/**
 * @author lizx
 * @date 2020-02-16 - 11:06
 */
public interface WidgetService {
    /**
     * 根据类型查找工具
     * @return
     */
    List<Widget> findWidgetByType(Integer widgetType);


    /**
     * 添加或修改工具
     * @param widget
     * @return
     */
    Widget insertOrUpdate(Widget widget);

    /**
     * 根据id获得工具
     * @param id
     * @return
     */
    Widget get(Integer id);


    /**
     * 根据id删除工具
     * @param id
     * @return
     */
    Integer delete(Integer id);
}
