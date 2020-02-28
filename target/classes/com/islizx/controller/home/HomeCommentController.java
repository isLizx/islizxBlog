package com.islizx.controller.home;

import cn.hutool.extra.servlet.ServletUtil;
import cn.hutool.http.HtmlUtil;
import com.github.pagehelper.PageInfo;
import com.islizx.entity.Blog;
import com.islizx.entity.Comment;
import com.islizx.entity.User;
import com.islizx.model.dto.LizxConst;
import com.islizx.model.enums.*;
import com.islizx.service.BlogService;
import com.islizx.service.CommentService;
import com.islizx.service.MailService;
import com.islizx.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author lizx
 * @date 2020-02-18 - 16:19
 */
@Controller
@RequestMapping("/comments")
public class HomeCommentController {
    @Autowired
    private CommentService commentService;

    @Autowired
    private UserService userService;

    @Autowired
    private MailService mailService;

    @Value("/images/otherUserAvatar.jpg")
    private String avatar;

    @Autowired
    private BlogService blogService;


    @RequestMapping(value = "/{blogId}", method = RequestMethod.GET)
    public String comments(@PathVariable Integer blogId, Model model, HttpSession session,
                           @RequestParam(value = "size", defaultValue = "10") Integer pageSize,
                           @RequestParam(value = "page", defaultValue = "1") Integer pageIndex) {
        PageInfo pageInfo = commentService.getListCommentByBlogId(BlogStatusEnum.PUBLISHED.getCode(),CommentStatusEnum.PUBLISHED.getCode(),null, pageIndex, pageSize,blogId);
        model.addAttribute("pageInfo", pageInfo);
        Blog blog = blogService.getBlogByPublishedAndId(BlogStatusEnum.PUBLISHED.getCode(),null, blogId);
        model.addAttribute("blog", blog);
        String msg = (String) session.getAttribute("msg");
        if (msg != null && !msg.equals("")) {
            model.addAttribute("msg", msg);
            session.removeAttribute("msg");
        }
        return "home/blog :: commentDiv";
    }


    @RequestMapping(method = RequestMethod.POST)
    public String post(Comment comment, HttpSession session, HttpServletRequest request, Model model,@RequestParam(value = "verifyCode")String verifyCode) {
        if(verifyCode==null || verifyCode.equals("")){
            session.setAttribute("msg", "请输入验证码");
            return "redirect:/comments/" + comment.getBlogId();
        } else if(!verifyCode.equals(request.getSession().getAttribute("verifyCode"))){
            session.setAttribute("msg", "验证码错误");
            return "redirect:/comments/" + comment.getBlogId();
        }
        boolean isAdminToAdmin = true;
        //1.判断字数，应该小于1000字
        if (comment != null && comment.getContent() != null && comment.getContent().length() > 1000) {
            session.setAttribute("msg", "评论字数太长，请删减或者分条发送!");
            return "redirect:/comments/" + comment.getBlogId();
        }

        //2.检查文章是否存在
        Comment lastComment = null;
        Blog blog = blogService.getBlogByPublishedAndId(BlogStatusEnum.PUBLISHED.getCode(),null, comment.getBlogId());
        if (blog == null) {
            session.setAttribute("msg", "文章不存在!");
            return "redirect:/comments/" + comment.getBlogId();
        }

        //3.判断是评论还是回复
        //回复评论
        if (comment.getParentCommentId() > 0) {
            lastComment = commentService.getById(BlogStatusEnum.PUBLISHED.getCode(), CommentStatusEnum.PUBLISHED.getCode(),null, comment.getParentCommentId());
            if (lastComment == null) {
                session.setAttribute("msg", "回复的评论不存在!");
                return "redirect:/comments/" + comment.getBlogId();
            }
        }
        //将评论内容的字符专为安全字符
        comment.setContent(HtmlUtil.escape(comment.getContent()));

        //4.判断是否登录
        //如果已登录
        User loginUser = (User) session.getAttribute("user");
        if (loginUser != null) {

            comment.setAvatar(loginUser.getAvatar());
            comment.setAdminComment(true);
            comment.setEmail(loginUser.getEmail());
            comment.setNickname(loginUser.getNickname());

                isAdminToAdmin = false;
        }
        //匿名评论
        else {
            comment.setEmail(HtmlUtil.escape(comment.getEmail()).toLowerCase());
            comment.setNickname(HtmlUtil.escape(comment.getNickname()));
            comment.setAdminComment(false);
            comment.setAvatar(avatar);
        }

        //5.保存分类信息
        if (comment.isAdminComment()) {
                comment.setPass(CommentStatusEnum.PUBLISHED.getCode());
            } else {
                comment.setPass(CommentStatusEnum.CHECKING.getCode());
            }
        String ip = ServletUtil.getClientIP(request);
        comment.setIp(ip);

        commentService.insertComment(comment);
        if(comment.isAdminComment()){
            session.setAttribute("msg", "评论成功.");
        }else{
            session.setAttribute("msg", "你的评论已经提交，待管理员审核之后可显示.");
        }
        blog.setUser(userService.getUserById(blog.getUserId()));
        if(isAdminToAdmin){
            // 通知管理员有新的回复,如果是管理员评论，则不发邮件
            new EmailToAdmin(comment, lastComment, blog).start();
        }

        return "redirect:/comments/" + comment.getBlogId();
    }




    /**
     * 发送邮件通知管理员
     */
    class EmailToAdmin extends Thread {
        private Comment comment;
        private Comment lastComment;
        private Blog blog;

        private EmailToAdmin(Comment comment, Comment lastComment, Blog blog) {
            this.comment = comment;
            this.lastComment = lastComment;
            this.blog = blog;
        }

        @Override
        public void run() {
            //发送通知给管理员
            if (StringUtils.equals(LizxConst.OPTIONS.get(BlogPropertiesEnum.SMTP_EMAIL_ENABLE.getProp()), TrueFalseEnum.TRUE.getValue()) && StringUtils.equals(LizxConst.OPTIONS.get(BlogPropertiesEnum.NEW_COMMENT_NOTICE.getProp()), TrueFalseEnum.TRUE.getValue())) {

                    Map<String, Object> map = new HashMap<>(8);
                    if (blog.getPostType()==PostTypeEnum.POST_TYPE_POST.getCode()) {
                        map.put("pageUrl", LizxConst.OPTIONS.get(BlogPropertiesEnum.BLOG_URL.getProp()) + "/article/" + blog.getId() + ".html");
                    } else if (blog.getPostType()==PostTypeEnum.POST_TYPE_NOTICE.getCode()) {
                        map.put("pageUrl", LizxConst.OPTIONS.get(BlogPropertiesEnum.BLOG_URL.getProp()) + "/notice/" + blog.getId());
                    } else {
                        map.put("pageUrl", LizxConst.OPTIONS.get(BlogPropertiesEnum.BLOG_URL.getProp()) + "/p/" + blog.getUrl());
                    }


                    // 页面名
                    map.put("pageTitle", LizxConst.OPTIONS.get(BlogPropertiesEnum.BLOG_TITLE.getProp()) + " (新评论通知)");
                    // 本站网址
                    map.put("webUrl", LizxConst.OPTIONS.get(BlogPropertiesEnum.BLOG_URL.getProp()));
                    // 本站名
                    map.put("webTitle", LizxConst.OPTIONS.get(BlogPropertiesEnum.BLOG_TITLE.getProp()));
                    // 新评论
                    map.put("comment", comment);
                    // 旧评论
                    map.put("lastComment", lastComment);
                    // 被评文章
                    map.put("blog", blog);
                    mailService.sendTemplateMail(
                            blog.getUser().getEmail(), LizxConst.OPTIONS.get(BlogPropertiesEnum.BLOG_TITLE.getProp()) + "(新评论通知)", map, "common/mail_template/mail_new.html");
                }

        }
    }
}
