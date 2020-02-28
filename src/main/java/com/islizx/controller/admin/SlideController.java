package com.islizx.controller.admin;

import com.islizx.config.annotation.SystemLog;
import com.islizx.entity.Slide;
import com.islizx.model.dto.Msg;
import com.islizx.model.enums.LogTypeEnum;
import com.islizx.service.SlideService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author lizx
 * @date 2020-02-15 - 23:36
 */
@Controller
@Slf4j
@RequestMapping("/admin")
public class SlideController {

    @Autowired
    SlideService slideService;

    /**
     * 渲染幻灯片设置页面
     *
     * @return 模板路径/admin/admin_slide
     */
    @RequestMapping(value = "/slides", method = RequestMethod.GET)
    public String slides(Model model) {
        //前台主要幻灯片
        List<Slide> slides = slideService.findAll();
        model.addAttribute("slide", new Slide());
        model.addAttribute("slides", slides);
        return "/admin/slide/slides-input";
    }

    /**
     * 新增/修改幻灯片
     *
     * @param slide slide
     * @return 重定向到/admin/slide
     */
    @SystemLog(description = "更新幻灯片", type = LogTypeEnum.OPERATION)
    @RequestMapping(value = "/slides/save", method = RequestMethod.POST)
    public String saveSlide(@ModelAttribute Slide slide) {
        slideService.insertOrUpdate(slide);
        return "redirect:/admin/slides";
    }

    /**
     * 跳转到修改页面
     *
     * @param slideId 幻灯片编号
     * @param model   model
     * @return 模板路径/admin/admin_slide
     */
    @RequestMapping(value = "/slides/edit/{id}")
    public String updateSlide(@PathVariable("id") Integer slideId, Model model) {
        Slide slide = slideService.get(slideId);
        model.addAttribute("slide", slide);

        List<Slide> slides = slideService.findAll();
        model.addAttribute("slides", slides);
        return "/admin/slide/slides-input";
    }

    /**
     * 删除幻灯片
     *
     * @param slideId 幻灯片编号
     * @return 重定向到/admin/slide
     */
    @RequestMapping(value = "/slides/delete", method = RequestMethod.DELETE)
    @ResponseBody
    @SystemLog(description = "删除幻灯片", type = LogTypeEnum.OPERATION)
    public Msg removeSlide(@RequestParam("id") Integer slideId) {
        slideService.delete(slideId);

        return Msg.success().add("msg", "幻灯片删除成功!");
    }

    /**
     * 渲染幻灯片设置页面
     *
     * @return 模板路径/admin/admin_slide
     */
    @RequestMapping(value = "/slides/slideByJson", method = RequestMethod.POST)
    public String slideByJson(Model model) {
        //前台主要幻灯片
        List<Slide> slides = slideService.findAll();

        model.addAttribute("slides", slides);
        return "/admin/slide/slides-input :: slideList";
    }
}
