package com.islizx.controller.admin;

import com.github.pagehelper.PageInfo;
import com.islizx.config.annotation.SystemLog;
import com.islizx.model.dto.Msg;
import com.islizx.model.enums.LogTypeEnum;
import com.islizx.service.BlogService;
import com.islizx.service.TypeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.islizx.entity.Type;

import javax.servlet.http.HttpSession;

/**
 * @author lizx
 * @date 2020-02-03 - 19:25
 */
@Controller
@RequestMapping("/admin")
@Slf4j
public class TypeController {

    @Autowired
    private TypeService typeService;

    @Autowired
    private BlogService blogService;

    /**
     * 后台分类列表显示
     *
     * @param model
     * @return
     */
/*    @RequestMapping(value = "/types", method = RequestMethod.GET)
    public String types(Model model, HttpSession session) {
        List<Type> typeList = typeService.listTypeWithCount();
        model.addAttribute("typelist",typeList);

        String msg = (String) session.getAttribute("msg");
        if(msg != null && !msg.equals("")){
            model.addAttribute("msg", msg);
            session.removeAttribute("msg");
        }
        return "Admin/types";
    }*/

    /**
     * 进入新增页面
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/types/input", method = RequestMethod.GET)
    @SystemLog(description = "新增分类", type = LogTypeEnum.OPERATION)
    public String input(@RequestParam(required = false, defaultValue = "1") Integer pageIndex,
                        @RequestParam(required = false, defaultValue = "15") Integer pageSize, Model model, HttpSession session) {
        PageInfo<Type> typePageInfo = typeService.pageType(pageIndex, pageSize);
        model.addAttribute("pageInfo",typePageInfo);

        String msg = (String) session.getAttribute("msg");
        if(msg != null && !msg.equals("")){
            model.addAttribute("msg", msg);
            session.removeAttribute("msg");
        }

        model.addAttribute("type", new Type());
        return "admin/article/types-input";
    }

    /**
     * 进入更新页面
     *
     * @param id
     * @param model
     * @return
     */
    @RequestMapping(value = "/types/input/{id}", method = RequestMethod.GET)
    public String editInput(@RequestParam(required = false, defaultValue = "1") Integer pageIndex,
                            @RequestParam(required = false, defaultValue = "15") Integer pageSize, @PathVariable Integer id, Model model, HttpSession session) {
        PageInfo<Type> typePageInfo = typeService.pageType(pageIndex, pageSize);
        model.addAttribute("pageInfo",typePageInfo);

        String msg = (String) session.getAttribute("msg");
        if(msg != null && !msg.equals("")){
            model.addAttribute("msg", msg);
            session.removeAttribute("msg");
        }

        model.addAttribute("type", typeService.getTypeById(id));
        return "admin/article/types-input";
    }

    /**
     * 分类更新提交
     * @param type
     * @param session
     * @return
     */
    @RequestMapping(value = "/types", method = RequestMethod.POST)
    @SystemLog(description = "更新分类", type = LogTypeEnum.OPERATION)
    public String post(Type type, HttpSession session){
        String msg = "";
        Type typeDB = typeService.getTypeByName(type.getName());
        if(type.getId() == 0 && typeDB == null){
            typeService.insertType(type);
            msg = "分类更新成功";
        }else if(type.getId() != 0){
            if((typeDB == null) || (typeDB.getId() == type.getId())){
                typeService.updateType(type);
                msg = "分类更新成功";
            }else{
                msg = "该分类已存在";
            }
        }else{
            msg = "该分类已存在";
        }
        session.setAttribute("msg", msg);
        return "redirect:/admin/types/input";
    }

    /**
     * 删除分类
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/types/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    @SystemLog(description = "删除分类", type = LogTypeEnum.OPERATION)
    public Msg delete(@PathVariable("id") Integer id)  {
        //禁止删除有文章的分类
        int count = blogService.countBlogByTypeId(id);

        if (count == 0) {
            typeService.deleteType(id);
            return Msg.success().add("msg", "分类删除成功");
        }
        return Msg.fail().add("msg", "不能删除包含文章的分类");
    }

    /**
     * 返回json格式的typelist
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/typesByJson")
    public String typesJson(@RequestParam(required = false, defaultValue = "1") Integer pageIndex,
                            @RequestParam(required = false, defaultValue = "15") Integer pageSize, Model model) {
        PageInfo<Type> typePageInfo = typeService.pageType(pageIndex, pageSize);
        model.addAttribute("pageInfo",typePageInfo);

        return "admin/article/types-input :: typeList";
    }

    @RequestMapping(value = "/checkTypeName")
    @ResponseBody
    public Msg typenameexist(String typename, Integer id){
        Type typeDB = typeService.getTypeByName(typename);
        if(typeDB != null){
            if(typeDB.getId() != id){
                return Msg.fail().add("msg", "该分类已存在");
            }
        }
        return Msg.success().add("msg", "该分类名有效");
    }

}
