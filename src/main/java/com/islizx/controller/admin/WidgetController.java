package com.islizx.controller.admin;

import com.islizx.config.annotation.SystemLog;
import com.islizx.entity.Widget;
import com.islizx.model.dto.Msg;
import com.islizx.model.enums.LogTypeEnum;
import com.islizx.model.enums.WidgetTypeEnum;
import com.islizx.service.WidgetService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author lizx
 * @date 2020-02-16 - 11:18
 */
@Slf4j
@Controller
@RequestMapping(value = "/admin")
public class WidgetController {

    @Autowired
    WidgetService widgetService;

    /**
     * 渲染小工具设置页面
     *
     * @return 模板路径/admin/admin_widget
     */
    @RequestMapping(value = "/widgets", method = RequestMethod.GET)
    public String widgets(Model model) {
        //前台主要小工具
        List<Widget> footerWidgets = widgetService.findWidgetByType(WidgetTypeEnum.FOOTER_WIDGET.getCode());
        List<Widget> postDetailSidebarWidgets = widgetService.findWidgetByType(WidgetTypeEnum.POST_DETAIL_SIDEBAR_WIDGET.getCode());
        model.addAttribute("footerWidgets", footerWidgets);
        model.addAttribute("postDetailSidebarWidgets", postDetailSidebarWidgets);

        model.addAttribute("widget", new Widget());

        return "/admin/widget/widgets-input";
    }

    /**
     * 新增/修改小工具
     *
     * @param widget widget
     * @return 重定向到/admin/widget
     */
    @RequestMapping(value = "/widgets/save", method = RequestMethod.POST)
    @SystemLog(description = "更新小工具", type = LogTypeEnum.OPERATION)
    public String saveWidget(@ModelAttribute Widget widget) {
        widgetService.insertOrUpdate(widget);
        return "redirect:/admin/widgets";
    }

    /**
     * 跳转到修改页面
     *
     * @param widgetId 小工具编号
     * @param model    model
     * @return 模板路径/admin/admin_widget
     */
    @RequestMapping(value = "/widgets/edit/{id}", method = RequestMethod.GET)
    public String updateWidget(@PathVariable("id") Integer widgetId, Model model) {
        Widget widget = widgetService.get(widgetId);
        model.addAttribute("widget", widget);

        //前台主要小工具
        List<Widget> postDetailSidebarWidgets = widgetService.findWidgetByType(WidgetTypeEnum.POST_DETAIL_SIDEBAR_WIDGET.getCode());
        List<Widget> footerWidgets = widgetService.findWidgetByType(WidgetTypeEnum.FOOTER_WIDGET.getCode());

        model.addAttribute("footerWidgets", footerWidgets);
        model.addAttribute("postDetailSidebarWidgets", postDetailSidebarWidgets);

        return "/admin/widget/widgets-input";
    }

    /**
     * 删除小工具
     *
     * @param widgetId 小工具编号
     * @return 重定向到/admin/widget
     */
    @RequestMapping(value = "/widgets/delete", method = RequestMethod.DELETE)
    @ResponseBody
    @SystemLog(description = "删除小工具", type = LogTypeEnum.OPERATION)
    public Msg removeWidget(@RequestParam("id") Integer widgetId) {
        widgetService.delete(widgetId);

        return Msg.success().add("msg", "小工具删除成功!");
    }

    /**
     * 渲染小工具设置页面
     *
     * @return 模板路径/admin/widgets
     */
    @RequestMapping(value = "/widgets/widgetByJson", method = RequestMethod.POST)
    public String widgetByJson(Model model) {
        List<Widget> footerWidgets = widgetService.findWidgetByType(WidgetTypeEnum.FOOTER_WIDGET.getCode());
        List<Widget> postDetailSidebarWidgets = widgetService.findWidgetByType(WidgetTypeEnum.POST_DETAIL_SIDEBAR_WIDGET.getCode());
        model.addAttribute("footerWidgets", footerWidgets);
        model.addAttribute("postDetailSidebarWidgets", postDetailSidebarWidgets);

        model.addAttribute("widget", new Widget());

        return "/admin/widget/widgets-input :: widgetList";
    }
}
