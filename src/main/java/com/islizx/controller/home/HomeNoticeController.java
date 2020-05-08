package com.islizx.controller.home;

import com.github.pagehelper.PageInfo;
import com.islizx.entity.Blog;
import com.islizx.model.enums.BlogStatusEnum;
import com.islizx.model.enums.CommentStatusEnum;
import com.islizx.model.enums.PostTypeEnum;
import com.islizx.service.BlogService;
import com.islizx.service.CommentService;
import com.islizx.service.IViewService;
import com.islizx.util.VerifyCodeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

/**
 * @author lizx
 * @date 2020-02-20 - 16:10
 */
@Controller
public class HomeNoticeController {

    @Autowired
    private BlogService blogService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private IViewService iViewService;

    /**
     * 公告首页
     * @param model
     * @return
     */
    @RequestMapping(value = "/notice", method = RequestMethod.GET)
    public String getPage(Model model){
        return getPage(-1, model);
    }

    /**
     * 渲染自定义页面
     *
     * @param id 公告id
     * @param model   model
     * @return 模板路径/themes/{theme}/post
     */
    @RequestMapping(value = "/notice/{id}", method = RequestMethod.GET)
    public String getPage(@PathVariable(value = "id") Integer id,
                          Model model) {
            HashMap<String, Object> postCriteria = new HashMap<>(1);
            postCriteria.put("sort", "updateTime");
            postCriteria.put("order", "desc");
            postCriteria.put("published", BlogStatusEnum.PUBLISHED.getCode());
            postCriteria.put("postType", PostTypeEnum.POST_TYPE_NOTICE.getCode());
            PageInfo<Blog> postPageInfo = blogService.pageBlog(1, 100, postCriteria);
            //侧边栏
            model.addAttribute("noticePageInfo", postPageInfo);

            if(id <= 0){
                List<Blog> tempList = postPageInfo.getList();
                for(Blog notice:tempList){
                    id = notice.getId();
                }
            }


            Blog post = blogService.getAndConvert(BlogStatusEnum.PUBLISHED.getCode(),PostTypeEnum.POST_TYPE_NOTICE.getCode(),id);
            if (null == post) {
                return "redirect:/error";
            }
            iViewService.viewed(post.getId());

            post.setViews(iViewService.getByPostId(post.getId()) + post.getViews());
            model.addAttribute("post", post);
            //文章访问量
            //blogService.updateBlogView(post.getId());

            //2、上一篇下一篇
            Blog afterBlog= blogService.getAfterBlog(BlogStatusEnum.PUBLISHED.getCode(), PostTypeEnum.POST_TYPE_NOTICE.getCode(),post.getId());
            Blog beforeBlog = blogService.getPreBlog(BlogStatusEnum.PUBLISHED.getCode(), PostTypeEnum.POST_TYPE_NOTICE.getCode(), post.getId());
            model.addAttribute("beforeBlog", beforeBlog);
            model.addAttribute("afterBlog", afterBlog);




            // 评论
            PageInfo pageInfo = commentService.getListCommentByBlogId(BlogStatusEnum.PUBLISHED.getCode(), CommentStatusEnum.PUBLISHED.getCode(),PostTypeEnum.POST_TYPE_NOTICE.getCode()
                    ,1, 10,post.getId());
            model.addAttribute("commentPageInfo", pageInfo);
        return "home/notice";
    }

    /**
     * 刷新验证码
     * @param request
     * @param response
     */
    @RequestMapping(value = "/notice/verifyCode", method = RequestMethod.GET)
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
}
