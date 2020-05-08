package com.islizx.controller.admin;

import com.github.pagehelper.PageInfo;
import com.islizx.config.annotation.SystemLog;
import com.islizx.entity.Blog;
import com.islizx.model.dto.Msg;
import com.islizx.entity.User;
import com.islizx.model.enums.BlogStatusEnum;
import com.islizx.model.enums.LogTypeEnum;
import com.islizx.model.enums.PostTypeEnum;
import com.islizx.service.BlogService;
import com.islizx.vo.BlogParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;

/**
 * @author lizx
 * @date 2020-02-15 - 21:33
 */
@Controller
@Slf4j
@RequestMapping("/admin")
public class PageController {

    @Autowired
    private BlogService blogService;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 页面管理页面
     *
     * @param model model
     * @return 模板路径admin/admin_page
     */
    @RequestMapping(value = "/pages", method = RequestMethod.GET)
    public String pages(@RequestParam(required = false, defaultValue = "1") Integer pageIndex,
                        @RequestParam(required = false, defaultValue = "15") Integer pageSize,
                        @RequestParam(required = false, defaultValue = "0") Integer published,
                        HttpSession session, Model model) {
        HashMap<String, Object> criteria = new HashMap<>(1);
        criteria.put("postType", PostTypeEnum.POST_TYPE_PAGE.getCode());

        PageInfo<Blog> blogPageInfo = blogService.pageBlog(pageIndex, pageSize, criteria);

        model.addAttribute("pageInfo", blogPageInfo);

        String msg = (String) session.getAttribute("msg");
        if(msg != null && !msg.equals("")){
            model.addAttribute("msg", msg);
            session.removeAttribute("msg");
        }
        return "admin/page/pages";
    }

    /**
     * 处理后台获取公告列表的请求
     * @param pageIndex
     * @param pageSize
     * @param published
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(value = "/pagesByJson")
    public String noticesByJson(@RequestParam(required = false, defaultValue = "1") Integer pageIndex,
                                @RequestParam(required = false, defaultValue = "15") Integer pageSize,
                                @RequestParam(required = false, defaultValue = "0") Integer published,
                                HttpSession session, Model model){

        HashMap<String, Object> criteria = new HashMap<>(1);
        criteria.put("postType", PostTypeEnum.POST_TYPE_PAGE.getCode());
        PageInfo<Blog> blogPageInfo = blogService.pageBlog(pageIndex, pageSize, criteria);

        model.addAttribute("pageInfo", blogPageInfo);

        return "admin/page/pages :: pageList";
    }

    /**
     *  后台添加页面显示
     * @param model
     * @return
     */
    @RequestMapping(value = "/pages/input", method = RequestMethod.GET)
    public String input(Model model){
        model.addAttribute("page", new Blog());
        return "admin/page/pages-input";
    }

    /**
     * 发布页面
     * @param blogParam
     * @param session
     * @return
     */
    @RequestMapping(value = "/pages", method = RequestMethod.POST)
    @SystemLog(description = "更新页面", type = LogTypeEnum.OPERATION)
    public String pushNotice(BlogParam blogParam, HttpSession session){
        Blog blog = new Blog();
        // 用户ID
        User user = (User) session.getAttribute("user");
        if (user != null) {
            blog.setUserId(user.getId());
        }
        // 页面链接
        blog.setUrl(blogParam.getUrl());
        // 允许评论
        blog.setCommentabled(blogParam.getCommentabled());
        // 标题
        blog.setTitle(blogParam.getTitle());
        // 文章内容
        blog.setContent(blogParam.getContent());
        // 发布状态
        blog.setPublished(BlogStatusEnum.PUBLISHED.getCode());
        blog.setPostType(PostTypeEnum.POST_TYPE_PAGE.getCode());
        blog.setAppreciation(false);
        blog.setShareStatement(false);
        blog.setRecommend(false);
        String msg = "";
        if(blogParam.getId() == null || blogParam.getId() == 0){
            blogService.insertBlog(blog);
            msg = "页面添加成功";
        }else{
            blog.setId(blogParam.getId());
            blog.setViews(blogService.getBlogByPublishedAndId(null ,PostTypeEnum.POST_TYPE_PAGE.getCode(), blogParam.getId()).getViews());
            blogService.updateBlogDetail(blog);
            msg = "页面更新成功";
        }
        redisTemplate.delete("pagePageInfo");
        session.setAttribute("msg", msg);
        return "redirect:/admin/pages";
    }

    /**
     * 检查该路径是否已经存在
     *
     * @param postUrl postUrl
     * @return JsonResult
     */
    @RequestMapping(value = "/pages/checkUrl")
    @ResponseBody
    public Msg checkUrlExists(@RequestParam("postUrl") String postUrl, @RequestParam("pageId") Integer id) {
        Blog blog = blogService.getBlogByUrl(postUrl, PostTypeEnum.POST_TYPE_PAGE.getCode());
        int iddb = blog.getId();
        if (null != blog) {
            if(iddb != id){
                return Msg.fail().add("msg", "该路径已存在");
            }
        }
        return Msg.success();
    }

    /**
     * 处理删除页面的请求
     *
     * @param id 公告编号
     * @return 重定向到/admin/post
     */
    @RequestMapping(value = "/pages/delete/{id}")
    @ResponseBody
    @SystemLog(description = "删除页面", type = LogTypeEnum.OPERATION)
    public Msg removePost(@PathVariable("id") Integer id) {
        Blog blogDB = blogService.getBlogByPublishedAndId(null,PostTypeEnum.POST_TYPE_PAGE.getCode(), id);
        if (blogDB == null) {
            return Msg.fail().add("msg", "页面不存在");
        }
        blogService.deleteBlog(id);

        redisTemplate.delete("pagePageInfo");
        return Msg.success().add("msg", "已删除页面");
    }

    /**
     *  后台修改页面显示
     * @param model
     * @return
     */
    @RequestMapping(value = "/pages/input/{id}", method = RequestMethod.GET)
    public String input(@PathVariable("id") Integer id, Model model){
        Blog blog = blogService.getBlogByPublishedAndId(null,PostTypeEnum.POST_TYPE_PAGE.getCode(), id);
        model.addAttribute("page",blog);

        redisTemplate.delete("pagePageInfo");
        return "admin/page/pages-input";
    }
}
