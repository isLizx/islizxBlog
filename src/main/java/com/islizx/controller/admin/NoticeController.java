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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;

/**
 * @author lizx
 * @date 2020-02-12 - 22:54
 */
@Controller
@RequestMapping("/admin")
@Slf4j
public class NoticeController {
    @Autowired
    private BlogService blogService;


    @RequestMapping("/notices/getpublishedsize")
    @ResponseBody
    public Msg getpublishedsize(){
        // 三种文章类型个数
        Integer publish = blogService.countBlog(BlogStatusEnum.PUBLISHED.getCode(), PostTypeEnum.POST_TYPE_NOTICE.getCode());
        Integer draft = blogService.countBlog(BlogStatusEnum.DRAFT.getCode(), PostTypeEnum.POST_TYPE_NOTICE.getCode());
        Integer trash =blogService.countBlog(BlogStatusEnum.TRASH.getCode(), PostTypeEnum.POST_TYPE_NOTICE.getCode());

        return Msg.success().add("publish", publish).add("draft", draft).add("trash", trash);
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
    @RequestMapping(value = "/notices", method = RequestMethod.GET)
    public String notices(@RequestParam(required = false, defaultValue = "1") Integer pageIndex,
                          @RequestParam(required = false, defaultValue = "15") Integer pageSize,
                          @RequestParam(required = false, defaultValue = "0") Integer published,
                          HttpSession session, Model model){
        // 三种文章类型个数
        Integer publish = blogService.countBlog(BlogStatusEnum.PUBLISHED.getCode(), PostTypeEnum.POST_TYPE_NOTICE.getCode());
        Integer draft = blogService.countBlog(BlogStatusEnum.DRAFT.getCode(), PostTypeEnum.POST_TYPE_NOTICE.getCode());
        Integer trash =blogService.countBlog(BlogStatusEnum.TRASH.getCode(), PostTypeEnum.POST_TYPE_NOTICE.getCode());

        HashMap<String, Object> criteria = new HashMap<>(1);
        criteria.put("postType", PostTypeEnum.POST_TYPE_NOTICE.getCode());
        criteria.put("published", published);
        PageInfo<Blog> blogPageInfo = blogService.pageBlog(pageIndex, pageSize, criteria);

        model.addAttribute("pageInfo", blogPageInfo);

        model.addAttribute("publish", publish);
        model.addAttribute("draft", draft);
        model.addAttribute("trash", trash);

        model.addAttribute("published", published);

        String msg = (String) session.getAttribute("msg");
        if(msg != null && !msg.equals("")){
            model.addAttribute("msg", msg);
            session.removeAttribute("msg");
        }
        return "admin/notice/notices";
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
    @RequestMapping(value = "/noticesByJson")
    public String noticesByJson(@RequestParam(required = false, defaultValue = "1") Integer pageIndex,
                          @RequestParam(required = false, defaultValue = "15") Integer pageSize,
                          @RequestParam(required = false, defaultValue = "0") Integer published,
                          HttpSession session, Model model){

        HashMap<String, Object> criteria = new HashMap<>(1);
        criteria.put("postType", PostTypeEnum.POST_TYPE_NOTICE.getCode());
        criteria.put("published", published);
        PageInfo<Blog> blogPageInfo = blogService.pageBlog(pageIndex, pageSize, criteria);

        model.addAttribute("pageInfo", blogPageInfo);

        return "admin/notice/notices :: noticeList";
    }

    /**
     *  后台添加页面显示
     * @param model
     * @return
     */
    @RequestMapping(value = "/notices/input", method = RequestMethod.GET)
    public String input(Model model){
        model.addAttribute("notice", new Blog());
        return "admin/notice/notices-input";
    }

    /**
     * 发表公告
     * @param blogParam
     * @param session
     * @return
     */
    @RequestMapping(value = "/notices", method = RequestMethod.POST)
    @SystemLog(description = "保存公告", type = LogTypeEnum.OPERATION)
    public String pushNotice(BlogParam blogParam, HttpSession session){
        Blog blog = new Blog();
        // 用户ID
        User user = (User) session.getAttribute("user");
        if (user != null) {
            blog.setUserId(user.getId());
        }
        // 允许评论
        blog.setCommentabled(blogParam.getCommentabled());
        // 标题
        blog.setTitle(blogParam.getTitle());
        // 文章内容
        blog.setContent(blogParam.getContent());
        // 发布状态
        blog.setPublished(blogParam.getPublished());
        blog.setPostType(PostTypeEnum.POST_TYPE_NOTICE.getCode());
        blog.setAppreciation(false);
        blog.setShareStatement(false);
        blog.setRecommend(false);
        String msg = "";
        if(blogParam.getId() == null){
            blogService.insertBlog(blog);
            msg = "公告添加成功";
        }else{
            blog.setId(blogParam.getId());
            blog.setViews(blogService.getBlogByPublishedAndId(null , PostTypeEnum.POST_TYPE_NOTICE.getCode(),blogParam.getId()).getViews());
            blogService.updateBlogDetail(blog);
            msg = "公告更新成功";
        }
        session.setAttribute("msg", msg);
        return "redirect:/admin/notices";
    }

    /**
     *  后台修改页面显示
     * @param model
     * @return
     */
    @RequestMapping(value = "/notices/input/{id}", method = RequestMethod.GET)
    public String input(@PathVariable("id") Integer id, Model model){
        Blog blog = blogService.getBlogByPublishedAndId(null, PostTypeEnum.POST_TYPE_NOTICE.getCode(), id);
        model.addAttribute("notice",blog);

        return "admin/notice/notices-input";
    }

    /**
     * 处理移至回收站的请求
     *
     * @param id 公告编号
     * @return
     */
    @RequestMapping(value = "/notices/throw/{id}")
    @ResponseBody
    @SystemLog(description = "将公告移到回收站", type = LogTypeEnum.OPERATION)
    public Msg moveToTrash(@PathVariable("id") Integer id) {
        Blog blogDB = blogService.getBlogByPublishedAndId(null, PostTypeEnum.POST_TYPE_NOTICE.getCode(), id);
        if (blogDB == null) {
            return Msg.fail().add("msg", "公告不存在");
        }
        blogDB.setPublished(BlogStatusEnum.TRASH.getCode());
        blogService.updateBlog(blogDB);

        return Msg.success().add("msg", "已将公告丢入回收站");
    }

    /**
     * 处理公告为发布的状态
     *
     * @param id 公告编号
     * @return 重定向到/admin/post
     */
    @RequestMapping(value = "/notices/revert/{id}")
    @ResponseBody
    @SystemLog(description = "将公告改为发布的状态", type = LogTypeEnum.OPERATION)
    public Msg moveToPublish(@PathVariable("id") Integer id) {
        Blog blogDB = blogService.getBlogByPublishedAndId(null,PostTypeEnum.POST_TYPE_NOTICE.getCode(), id);
        if (blogDB == null) {
            return Msg.fail().add("msg", "公告不存在");
        }
        blogDB.setPublished(BlogStatusEnum.PUBLISHED.getCode());
        blogService.updateBlog(blogDB);

        return Msg.success().add("msg", "已发布公告");
    }

    /**
     * 处理删除公告的请求
     *
     * @param id 公告编号
     * @return 重定向到/admin/post
     */
    @RequestMapping(value = "/notices/delete/{id}")
    @ResponseBody
    @SystemLog(description = "删除公告", type = LogTypeEnum.OPERATION)
    public Msg removePost(@PathVariable("id") Integer id) {
        Blog blogDB = blogService.getBlogByPublishedAndId(null,PostTypeEnum.POST_TYPE_NOTICE.getCode(), id);
        if (blogDB == null) {
            return Msg.fail().add("msg", "公告不存在");
        }
        blogService.deleteBlog(id);

        return Msg.success().add("msg", "已删除公告");
    }
}
