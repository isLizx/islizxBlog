package com.islizx.controller.admin;

import cn.hutool.core.lang.Validator;
import cn.hutool.extra.servlet.ServletUtil;
import com.github.pagehelper.PageInfo;
import com.islizx.config.annotation.SystemLog;
import com.islizx.entity.*;
import com.islizx.model.dto.LizxConst;
import com.islizx.model.dto.Msg;
import com.islizx.model.enums.*;
import com.islizx.service.BlogService;
import com.islizx.service.CommentService;
import com.islizx.service.MailService;
import com.islizx.vo.CommentQuery;
import com.sun.org.apache.xpath.internal.operations.Bool;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * @author lizx
 * @date 2020-02-14 - 15:26
 */
@Controller
@Slf4j
@RequestMapping("/admin")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private BlogService blogService;

    @Autowired
    private MailService mailService;

    /**
     * 评论人相关信息
     */
    public static final String COMMENT_AUTHOR_IP = "ip";
    public static final String COMMENT_AUTHOR = "nickname";
    public static final String COMMENT_EMAIL = "email";
    public static final String COMMENT_CONTENT = "content";

    @RequestMapping("/comments/getpublishedsize")
    @ResponseBody
    public Msg getpublishedsize() {
        // 三种评论类型个数
        Integer publish = commentService.countCommentByPass(CommentStatusEnum.PUBLISHED.getCode());
        Integer draft = commentService.countCommentByPass(CommentStatusEnum.CHECKING.getCode());
        Integer trash = commentService.countCommentByPass(CommentStatusEnum.RECYCLE.getCode());

        return Msg.success().add("publish", publish).add("draft", draft).add("trash", trash);
    }

    /**
     * 后台评论列表显示
     *
     * @return modelAndView
     */
    @RequestMapping(value = "/comments", method = RequestMethod.GET)
    public String index(@RequestParam(required = false, defaultValue = "1") Integer pageIndex,
                        @RequestParam(required = false, defaultValue = "15") Integer pageSize,
                        @RequestParam(required = false, defaultValue = "1") Integer pass,
                        HttpSession session, Model model) {
        // 三种文章类型个数
        Integer publish = commentService.countCommentByPass(CommentStatusEnum.PUBLISHED.getCode());
        Integer draft = commentService.countCommentByPass(CommentStatusEnum.CHECKING.getCode());
        Integer trash = commentService.countCommentByPass(CommentStatusEnum.RECYCLE.getCode());

        HashMap<String, Object> criteria = new HashMap<>(1);
        criteria.put("pass", pass);
        PageInfo<Comment> commentPageInfo = commentService.pageComment(pageIndex, pageSize, criteria);

        model.addAttribute("pageInfo", commentPageInfo);

        model.addAttribute("publish", publish);
        model.addAttribute("draft", draft);
        model.addAttribute("trash", trash);

        model.addAttribute("pass", pass);

        String msg = (String) session.getAttribute("msg");
        if (msg != null && !msg.equals("")) {
            model.addAttribute("msg", msg);
            session.removeAttribute("msg");
        }
        return "admin/comment/comments";
    }

    /**
     * 分页条件查询
     *
     * @param pageIndex    页数
     * @param pageSize
     * @param commentQuery
     * @param model
     * @return
     */
    @RequestMapping("/comments/search")
    public String search(@RequestParam(required = false, defaultValue = "1") Integer pageIndex,
                         @RequestParam(required = false, defaultValue = "15") Integer pageSize, CommentQuery commentQuery, Model model) {
        HashMap<String, Object> criteria = new HashMap<>();

        String keywords = commentQuery.getKeywords();
        Integer pass = commentQuery.getPass();
        String sort = commentQuery.getSort();
        String order = commentQuery.getOrder();
        String searchType = commentQuery.getSearchType();
        if (!StringUtils.isBlank(keywords)) {
            if (COMMENT_CONTENT.equals(searchType)) {
                criteria.put("content", keywords);
            } else if (COMMENT_AUTHOR.equals(searchType)) {
                criteria.put("nickname", keywords);
            } else if (COMMENT_EMAIL.equals(searchType)) {
                criteria.put("email", keywords);
            } else if (COMMENT_AUTHOR_IP.equals(searchType)) {
                criteria.put("ip", keywords);
            }
        }
        if (pass != null) {
            criteria.put("pass", pass);
        } else {
            criteria.put("pass", 1);
        }
        if (sort != null && !StringUtils.isBlank(sort)) criteria.put("sort", sort);
        if (order != null && !StringUtils.isBlank(order)) criteria.put("order", order);
        PageInfo<Comment> commentPageInfo = commentService.pageComment(pageIndex, pageSize, criteria);
        model.addAttribute("pageInfo", commentPageInfo);

        return "admin/comment/comments :: commentList";
    }

    /**
     * 将评论改变为发布状态
     * 评论状态有两种：待审核1，回收站2
     * 对待审核转发布的，发邮件
     *
     * @param commentId 评论编号
     * @return 重定向到/admin/comment
     */
    @ResponseBody
    @RequestMapping(value = "/comments/revert", method = RequestMethod.PUT)
    @SystemLog(description = "回滚评论", type = LogTypeEnum.OPERATION)
    public Msg moveToPublish(@RequestParam("id") Integer commentId, HttpSession session) {
        User loginUser = (User) session.getAttribute("user");
        //评论
        Comment comment = commentService.getById(null,null,null,commentId);

        Blog blog = blogService.getBlogByPublishedAndId(null, null,comment.getBlogId());
        Comment result = commentService.updateCommentPass(commentId, CommentStatusEnum.PUBLISHED.getCode());
        // 判断是不是子评论，如果是，也对被回复人发邮件
        if(result.getParentCommentId() != null && result.getParentCommentId() > 0){
            //被回复的评论
            Comment lastComment = commentService.getById(null,null, null,comment.getParentCommentId());
            //邮件通知
            new EmailToAuthor(result, lastComment, blog).start();
        }
        //判断是否启用邮件服务
        new NoticeToAuthor(result, blog, result.getPass()).start();

        return Msg.success().add("msg", "评论已发布!");
    }
    /**
     * 删除评论
     *
     * @param commentId commentId
     * @return string 重定向到/admin/comment
     */
    @RequestMapping(value = "/comments/delete", method = RequestMethod.DELETE)
    @ResponseBody
    @SystemLog(description = "删除评论", type = LogTypeEnum.OPERATION)
    public Msg moveToAway(@RequestParam("id") Integer commentId) {
        //评论
        Comment comment = commentService.getById(null,null, null, commentId);

        if (Objects.equals(comment.getPass(), CommentStatusEnum.RECYCLE.getCode())) {
            commentService.deleteComment(commentId);
            return Msg.success().add("msg", "评论已彻底删除");
        } else {
            commentService.updateCommentPass(commentId, CommentStatusEnum.RECYCLE.getCode());
            return Msg.success().add("msg", "评论仍入回收站");
        }
    }

    /**
     * 管理员回复评论，并通过评论
     *
     * @param commentId      被回复的评论
     * @param commentContent 回复的内容
     * @return 重定向到/admin/comment
     */
    @RequestMapping(value = "/comments/reply")
    @ResponseBody
    @SystemLog(description = "回复评论", type = LogTypeEnum.OPERATION)
    public Msg replyComment(@RequestParam("id") Integer commentId,
                                   @RequestParam("commentContent") String commentContent,
                                   HttpServletRequest request, HttpSession session) {
        //博主信息
        User loginUser = (User) session.getAttribute("user");
        //被回复的评论
        Comment lastComment = commentService.getById(null,null, null,commentId);
        if (lastComment == null) {
            return Msg.fail().add("msg", "回复失败,评论不存在");
        }

        Blog blog = blogService.getBlogByPublishedAndId(null, null, lastComment.getBlogId());
        if (blog == null) {
            return Msg.fail().add("msg", "回复失败,文章不存在");
        }
        //修改被回复的评论的状态
        if (Objects.equals(lastComment.getPass(), CommentStatusEnum.CHECKING.getCode())) {
            /*lastComment.setPass(CommentStatusEnum.PUBLISHED.getCode());
            commentService.insertOrUpdate(lastComment);*/
            commentService.updateCommentPass(lastComment.getId(), CommentStatusEnum.PUBLISHED.getCode());
        }

        //保存评论
        Comment comment = new Comment();
        comment.setBlogId(lastComment.getBlogId());
        comment.setNickname(loginUser.getNickname());
        comment.setEmail(loginUser.getEmail());
        comment.setIp(ServletUtil.getClientIP(request));
        comment.setAvatar(loginUser.getAvatar());
        comment.setContent(commentContent);
        comment.setParentCommentId(commentId);
        comment.setPass(CommentStatusEnum.PUBLISHED.getCode());
        comment.setAdminComment(true);
        comment.setCreateTime(new Date());
        commentService.insertComment(comment);
        //邮件通知
        new EmailToAuthor(comment, lastComment, blog).start();

        return Msg.success().add("msg", "回复成功!");

    }

    /**
     * 批量删除
     *
     * @param del_idstr 评论ID列表
     * @return
     */
    @RequestMapping(value = "/comments/batchDelete")
    @ResponseBody
    @SystemLog(description = "批量删除评论", type = LogTypeEnum.OPERATION)
    public Msg batchDelete(@RequestParam("del_idstr") String del_idstr) {
        List<Integer> ids = new ArrayList<>();
        if(del_idstr.contains("-")){
            String[] str_ids = del_idstr.split("-");
            for(String string:str_ids){
                ids.add(Integer.parseInt(string));
            }
        }else{
            // 单个删除
            Integer id = Integer.parseInt(del_idstr);
            ids.add(id);
        }

        //Integer loginUserId = ((User)session.getAttribute("user")).getId();
        //批量操作
        //1、防止恶意操作
        if (ids == null || ids.size() == 0 || ids.size() >= 100) {
            return Msg.fail().add("msg", "参数不合法!");
        }
        //2、检查用户权限
        //文章作者才可以删除
        List<Comment> commentList = commentService.findByBatchIds(ids);
        /*for (Comment comment : commentList) {
            if (!Objects.equals(comment.getUserId(), loginUserId) && !Objects.equals(comment.getAcceptUserId(), loginUserId)) {
                return new JsonResult(ResultCodeEnum.FAIL.getCode(), localeMessageUtil.getMessage("code.admin.common.permission-denied"));
            }
        }*/
        //3、如果当前状态为回收站，则删除；否则，移到回收站
        for (Comment comment : commentList) {
            if (Objects.equals(comment.getPass(), CommentStatusEnum.RECYCLE.getCode())) {
                commentService.deleteComment(comment.getId());
            } else {
                commentService.updateCommentPass(comment.getId(), CommentStatusEnum.RECYCLE.getCode());
            }
        }
        return Msg.success().add("msg", "删除成功!");
    }

    /**
     * 异步发送邮件回复给评论者
     */
    class EmailToAuthor extends Thread {

        private Comment comment;
        private Comment lastComment;
        private Blog blog;

        private EmailToAuthor(Comment comment, Comment lastComment, Blog blog) {
            this.comment = comment;
            this.lastComment = lastComment;
            this.blog = blog;
        }

        @Override
        public void run() {
            if (StringUtils.equals(LizxConst.OPTIONS.get(BlogPropertiesEnum.SMTP_EMAIL_ENABLE.getProp()), TrueFalseEnum.TRUE.getValue()) && StringUtils.equals(LizxConst.OPTIONS.get(BlogPropertiesEnum.COMMENT_REPLY_NOTICE.getProp()), TrueFalseEnum.TRUE.getValue())) {
                if (Validator.isEmail(lastComment.getEmail())) {
                    Map<String, Object> map = new HashMap<>(8);
                    if (blog.getPostType()==PostTypeEnum.POST_TYPE_POST.getCode()) {
                        map.put("pageUrl", LizxConst.OPTIONS.get(BlogPropertiesEnum.BLOG_URL.getProp()) + "/article/" + blog.getId() + ".html");
                    } else if (blog.getPostType()==PostTypeEnum.POST_TYPE_NOTICE.getCode()) {
                        map.put("pageUrl", LizxConst.OPTIONS.get(BlogPropertiesEnum.BLOG_URL.getProp()) + "/notice/" + blog.getId());
                    } else {
                        map.put("pageUrl", LizxConst.OPTIONS.get(BlogPropertiesEnum.BLOG_URL.getProp()) + "/p/" + blog.getUrl());
                    }
                    map.put("comment", comment);
                    map.put("lastComment", lastComment);
                    map.put("blog", blog);

                    // 页面名
                    map.put("pageTitle", LizxConst.OPTIONS.get(BlogPropertiesEnum.BLOG_TITLE.getProp()) + " (评论回复通知)");

                    map.put("webUrl", LizxConst.OPTIONS.get(BlogPropertiesEnum.BLOG_URL.getProp()));
                    // 本站名
                    map.put("webTitle", LizxConst.OPTIONS.get(BlogPropertiesEnum.BLOG_TITLE.getProp()));

                    mailService.sendTemplateMail(
                            lastComment.getEmail(), LizxConst.OPTIONS.get(BlogPropertiesEnum.BLOG_TITLE.getProp()) + "(评论回复通知)", map, "common/mail_template/mail_reply.html");
                }
            }
        }
    }

    /**
     * 异步通知评论者审核通过
     */
    class NoticeToAuthor extends Thread {

        private Comment comment;
        private Blog blog;
        private Integer status;

        private NoticeToAuthor(Comment comment, Blog blog, Integer status) {
            this.comment = comment;
            this.blog = blog;
            this.status = status;
        }

        @Override
        public void run() {
            if (StringUtils.equals(LizxConst.OPTIONS.get(BlogPropertiesEnum.SMTP_EMAIL_ENABLE.getProp()), TrueFalseEnum.TRUE.getValue())
                    && StringUtils.equals(LizxConst.OPTIONS.get(BlogPropertiesEnum.COMMENT_REPLY_NOTICE.getProp()), TrueFalseEnum.TRUE.getValue())) {
                try {
                    //待审核的评论变成已通过，发邮件
                    if (status == 1 && Validator.isEmail(comment.getEmail())) {
                        Map<String, Object> map = new HashMap<>(6);
                        if (blog.getPostType()==PostTypeEnum.POST_TYPE_POST.getCode()) {
                            map.put("pageUrl", LizxConst.OPTIONS.get(BlogPropertiesEnum.BLOG_URL.getProp()) + "/article/" + blog.getId() + ".html");
                        } else if (blog.getPostType()==PostTypeEnum.POST_TYPE_NOTICE.getCode()) {
                            map.put("pageUrl", LizxConst.OPTIONS.get(BlogPropertiesEnum.BLOG_URL.getProp()) + "/notice/" + blog.getId());
                        } else {
                            map.put("pageUrl", LizxConst.OPTIONS.get(BlogPropertiesEnum.BLOG_URL.getProp()) + "/p/" + blog.getUrl());
                        }
                        map.put("comment", comment);
                        map.put("blog", blog);
                        // 页面名
                        map.put("pageTitle", LizxConst.OPTIONS.get(BlogPropertiesEnum.BLOG_TITLE.getProp()) + " (评论审核通知)");
                        // 本站网址
                        map.put("webUrl", LizxConst.OPTIONS.get(BlogPropertiesEnum.BLOG_URL.getProp()));
                        // 本站名
                        map.put("webTitle", LizxConst.OPTIONS.get(BlogPropertiesEnum.BLOG_TITLE.getProp()));
                        mailService.sendTemplateMail(
                                comment.getEmail(),
                                  LizxConst.OPTIONS.get(BlogPropertiesEnum.BLOG_TITLE.getProp()) + " (评论审核通知)", map, "common/mail_template/mail_passed.html");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
