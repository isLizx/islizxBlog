package com.islizx.controller.admin;

import com.islizx.entity.Link;
import com.islizx.model.dto.Msg;
import com.islizx.service.LinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * @author lizx
 * @date 2020-02-19 - 21:29
 */
@Controller
@RequestMapping(value = "/admin")
public class LinkController {
    @Autowired
    private LinkService linkService;

    /**
     * 渲染小工具设置页面
     *
     * @return 模板路径/admin/link/links-input
     */
    @RequestMapping(value = "/links", method = RequestMethod.GET)
    public String links(Model model) {
        //前台主要小工具
        List<Link> linkList = linkService.getAll();
        model.addAttribute("linkList", linkList);

        model.addAttribute("link", new Link());

        return "/admin/link/links-input";
    }

    /**
     * 新增/修改友情链接
     *
     * @param link Link
     * @return 重定向到/admin/link
     */
    @RequestMapping(value = "/links/save", method = RequestMethod.POST)
    public String saveLink(@ModelAttribute Link link) {
        if(link.getId() == null || link.getId() <= 0){
            link.setLinkCreateTime(new Date());
            linkService.insert(link);
        }else{
            linkService.update(link);
        }
        return "redirect:/admin/links";
    }

    /**
     * 跳转到修改页面
     *
     * @param id 友情链接编号
     * @param model    model
     * @return 模板路径/admin/admin_link
     */
    @RequestMapping(value = "/links/edit/{id}", method = RequestMethod.GET)
    public String updateLink(@PathVariable("id") Integer id, Model model) {
        Link link = linkService.getById(id);
        model.addAttribute("link", link);

        //前台主要小工具
        List<Link> linkList = linkService.getAll();
        model.addAttribute("linkList", linkList);

        return "/admin/link/links-input";
    }

    /**
     * 删除友情链接
     *
     * @param widgetId 友情链接编号
     * @return 重定向到/admin/link
     */
    @RequestMapping(value = "/links/delete", method = RequestMethod.DELETE)
    @ResponseBody
    public Msg removeLink(@RequestParam("id") Integer widgetId) {
        linkService.delete(widgetId);

        return Msg.success().add("msg", "友情链接删除成功!");
    }

    /**
     * 渲染友情链接设置页面
     *
     * @return 模板路径/admin/links
     */
    @RequestMapping(value = "/links/linkByJson", method = RequestMethod.POST)
    public String widgetByJson(Model model) {
        List<Link> linkList = linkService.getAll();
        model.addAttribute("linkList", linkList);

        model.addAttribute("link", new Link());

        return "/admin/link/links-input :: linkList";
    }
}
