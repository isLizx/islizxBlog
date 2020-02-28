package com.islizx.controller.admin;

import com.github.pagehelper.PageInfo;
import com.islizx.config.annotation.SystemLog;
import com.islizx.model.dto.Msg;
import com.islizx.entity.Tag;
import com.islizx.model.enums.LogTypeEnum;
import com.islizx.service.BlogService;
import com.islizx.service.TagService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;


/**
 * @author lizx
 * @date 2020-02-03 - 21:43
 */
@Controller
@Slf4j
@RequestMapping("/admin")
public class TagController {

    @Autowired
    private TagService tagService;

    @Autowired
    private BlogService blogService;

    /**
     * 标签列表
     *
     * @param model
     * @return
     */
/*    @RequestMapping(value = "/tags", method = RequestMethod.GET)
    public String tags(Model model, HttpSession session) {
        List<Tag> tagList = tagService.listTagWithCount();
        model.addAttribute("taglist", tagList);

        String msg = (String) session.getAttribute("msg");
        if (msg != null && !msg.equals("")) {
            model.addAttribute("msg", msg);
            session.removeAttribute("msg");
        }
        return "Admin/tags";
    }*/

    /**
     * 进入新增页面
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/tags/input", method = RequestMethod.GET)
    public String input(@RequestParam(required = false, defaultValue = "1") Integer pageIndex,
                        @RequestParam(required = false, defaultValue = "15") Integer pageSize, Model model, HttpSession session) {
        PageInfo<Tag> tagPageInfo = tagService.pageTag(pageIndex, pageSize);
        model.addAttribute("pageInfo", tagPageInfo);

        String msg = (String) session.getAttribute("msg");
        if (msg != null && !msg.equals("")) {
            model.addAttribute("msg", msg);
            session.removeAttribute("msg");
        }

        model.addAttribute("tag", new Tag());
        return "admin/article/tags-input";
    }

    /**
     * 进入更新页面
     *
     * @param id
     * @param model
     * @return
     */
    @RequestMapping(value = "/tags/input/{id}", method = RequestMethod.GET)
    public String editInput(@RequestParam(required = false, defaultValue = "1") Integer pageIndex,
                            @RequestParam(required = false, defaultValue = "15") Integer pageSize, @PathVariable Integer id, Model model, HttpSession session) {
        PageInfo<Tag> tagPageInfo = tagService.pageTag(pageIndex, pageSize);
        model.addAttribute("pageInfo", tagPageInfo);

        String msg = (String) session.getAttribute("msg");
        if (msg != null && !msg.equals("")) {
            model.addAttribute("msg", msg);
            session.removeAttribute("msg");
        }

        model.addAttribute("tag", tagService.getTagById(id));
        return "admin/article/tags-input";
    }

    /**
     * 标签更新提交
     *
     * @param tag
     * @param session
     * @return
     */
    @RequestMapping(value = "/tags", method = RequestMethod.POST)
    @SystemLog(description = "更新标签", type = LogTypeEnum.OPERATION)
    public String post(Tag tag, HttpSession session) {
        String msg = "";
        Tag tagDB = tagService.getTagByName(tag.getName());
        if(tag.getId() == 0 && tagDB == null){
            tagService.insertTag(tag);
            msg = "标签更新成功";
        }else if(tag.getId() != 0){
            if((tagDB == null || (tagDB.getId() == tag.getId()))){
                tagService.updateTag(tag);
                msg = "标签更新成功";
            }else{
                msg = "该标签已存在";
            }
        }else{
            msg = "该标签已存在";
        }

        session.setAttribute("msg", msg);
        return "redirect:/admin/tags/input";
    }

    /**
     * 删除标签
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/tags/{id}")
    @ResponseBody
    @SystemLog(description = "删除标签", type = LogTypeEnum.OPERATION)
    public Msg delete(@PathVariable("id") Integer id, HttpSession session) {
        //禁止删除有文章的分类
        int count = blogService.countBlogByTagId(id);

        if (count == 0) {
            tagService.deleteTag(id);
            return Msg.success().add("msg", "标签删除成功");
        }
        return Msg.fail().add("msg", "不能删除包含文章的分类");
    }

    /**
     * 返回json格式的taglist
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/tagsByJson")
    public String typesJson(@RequestParam(required = false, defaultValue = "1") Integer pageIndex,
                            @RequestParam(required = false, defaultValue = "15") Integer pageSize,Model model, HttpSession session) {
        PageInfo<Tag> tagPageInfo = tagService.pageTag(pageIndex, pageSize);
        model.addAttribute("pageInfo", tagPageInfo);

        return "admin/article/tags-input :: tagList";
    }

    @RequestMapping(value = "/checkTagName")
    @ResponseBody
    public Msg tagnameexist(String tagname, Integer id){
        Tag tagDB = tagService.getTagByName(tagname);
        if(tagDB != null){
            if(tagDB.getId() != id){
               return Msg.fail().add("msg", "该标签已存在");
            }
        }
        return Msg.success().add("msg", "该标签名有效");
    }


}
