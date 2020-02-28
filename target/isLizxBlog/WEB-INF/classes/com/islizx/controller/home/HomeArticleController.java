package com.islizx.controller.home;

import com.github.pagehelper.PageInfo;
import com.islizx.entity.Blog;
import com.islizx.model.dto.Msg;
import com.islizx.model.enums.BlogStatusEnum;
import com.islizx.model.enums.CommentStatusEnum;
import com.islizx.model.enums.PostTypeEnum;
import com.islizx.service.BlogService;
import com.islizx.service.CommentService;
import com.islizx.util.VerifyCodeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author lizx
 * @date 2020-02-18 - 15:56
 */
@Controller
@RequestMapping("/article")
public class HomeArticleController {

    @Autowired
    BlogService blogService;

    @Autowired
    CommentService commentService;

    /**
     * 渲染文章详情
     *
     * @param postId 文章ID
     * @param model   model
     * @return 模板路径/themes/{theme}/post
     */
    @RequestMapping(value = {"/{postId}.html", "{postId}"}, method = RequestMethod.GET)
    public String getPost(@PathVariable Integer postId,
                          @RequestParam(value = "page", defaultValue = "1") Integer pageNumber,
                          Model model) {
        //1、查询文章
        Blog blog = blogService.getAndConvert(BlogStatusEnum.PUBLISHED.getCode(),PostTypeEnum.POST_TYPE_POST.getCode(),postId);
        //文章不存在404
        if (null == blog) {
            return "error/404";
        }
        //文章存在但是未发布只有作者可以看
        if (!blog.getPublished().equals(BlogStatusEnum.PUBLISHED.getCode())) {
            return "error/404";
        }

        //2、上一篇下一篇
        Blog afterBlog = blogService.getAfterBlog(BlogStatusEnum.PUBLISHED.getCode(), PostTypeEnum.POST_TYPE_POST.getCode(),blog.getId());
        Blog beforeBlog = blogService.getPreBlog(BlogStatusEnum.PUBLISHED.getCode(), PostTypeEnum.POST_TYPE_POST.getCode(), blog.getId());
        model.addAttribute("beforeBlog", beforeBlog);
        model.addAttribute("afterBlog", afterBlog);


        //5.文章访问量
        blogService.updateBlogView(blog.getId());

        model.addAttribute("blog", blog);
        PageInfo pageInfo = commentService.getListCommentByBlogId(BlogStatusEnum.PUBLISHED.getCode(), CommentStatusEnum.PUBLISHED.getCode(),PostTypeEnum.POST_TYPE_POST.getCode(),1, 10,postId);
        model.addAttribute("pageInfo", pageInfo);

        return "home/blog";
    }

    /**
     * 刷新验证码
     * @param request
     * @param response
     */
    @RequestMapping(value = "/verifyCode", method = RequestMethod.GET)
    public void verifyCode(HttpServletRequest request , HttpServletResponse response){
        System.out.println("verifyCode执行=====================");
        response.setContentType("image/jpeg");
        //设置页面不缓存
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);

        VerifyCodeUtil vcUtil = VerifyCodeUtil.Instance();
        //将随机码设置在session中
        request.getSession().setAttribute("verifyCode", vcUtil.getResult()+"");
        try {
            ImageIO.write(vcUtil.getImage(), "jpeg", response.getOutputStream());

            response.getOutputStream().flush();
            response.getOutputStream().close();
            response.flushBuffer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 点赞
     *
     * @param postId
     * @return
     */
    @RequestMapping(value = "/like", method = RequestMethod.POST)
    @ResponseBody
    public Msg like(@RequestParam("id") Integer postId) {
        Blog blog = blogService.getBlogByPublishedAndId(BlogStatusEnum.PUBLISHED.getCode(),PostTypeEnum.POST_TYPE_POST.getCode(), postId);
        if (blog == null) {
            return Msg.fail().add("msg", "文章不存在");
        }
        blogService.incrBlogLikes(postId);
        return Msg.success().add("msg", "操作成功");
    }
}
