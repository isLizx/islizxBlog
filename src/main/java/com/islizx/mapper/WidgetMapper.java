package com.islizx.mapper;

import com.islizx.entity.Widget;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author lizx
 * @date 2020-02-16 - 11:09
 */
@Mapper
public interface WidgetMapper {

    /**
     * 根据类型查找工具
     * @return
     */
    List<Widget> findWidgetByType(Integer widgetType);


    /**
     * 添加工具
     * @param widget
     * @return
     */
    Integer insert(Widget widget);

    /**
     * 修改工具
     * @param widget
     * @return
     */
    Integer update(Widget widget);

    /**
     * 根据id查找工具
     * @param id
     * @return
     */
    Widget getWidgetById(Integer id);

    /**
     * 根据id删除工具
     * @param id
     * @return
     */
    Integer deleteWidget(Integer id);
}
