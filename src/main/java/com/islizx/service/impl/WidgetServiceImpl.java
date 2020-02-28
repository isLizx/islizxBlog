package com.islizx.service.impl;

import com.islizx.entity.Widget;
import com.islizx.mapper.WidgetMapper;
import com.islizx.service.WidgetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author lizx
 * @date 2020-02-16 - 11:08
 */
@Service
public class WidgetServiceImpl implements WidgetService {

    @Autowired(required = false)
    WidgetMapper widgetMapper;

    @Override
    public List<Widget> findWidgetByType(Integer widgetType) {
        return widgetMapper.findWidgetByType(widgetType);
    }

    @Override
    public Widget insertOrUpdate(Widget widget) {
        if(widget.getId() == null || widget.getId() == 0){
            widgetMapper.insert(widget);
        }else{
            widgetMapper.update(widget);
        }
        return widget;
    }

    @Override
    public Widget get(Integer id) {
        return widgetMapper.getWidgetById(id);
    }

    @Override
    public Integer delete(Integer id) {
        return widgetMapper.deleteWidget(id);
    }
}
