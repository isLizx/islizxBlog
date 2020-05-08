package com.islizx.controller.admin;

import com.github.pagehelper.PageInfo;
import com.islizx.config.annotation.SystemLog;
import com.islizx.entity.Comment;
import com.islizx.entity.Log;
import com.islizx.model.dto.Msg;
import com.islizx.entity.User;
import com.islizx.model.enums.BlogStatusEnum;
import com.islizx.model.enums.CommentStatusEnum;
import com.islizx.model.enums.LogTypeEnum;
import com.islizx.model.enums.PostTypeEnum;
import com.islizx.service.*;
import com.islizx.util.MyUtils;
import com.islizx.util.VerifyCodeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.imageio.ImageIO;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.islizx.util.MyUtils.getIpAddr;
import static com.islizx.util.MyUtils.code;
/**
 * @author lizx
 * @date 2020-01-30 - 11:17
 */
@Controller
@Slf4j
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private BlogService blogService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private AttachmentService attachmentService;

    @Autowired
    private LogService logService;


    @RequestMapping("/error")
    public String error(){
        return "error/404";
    }

    /**
     * 登录页面显示
     * @return
     */
    @RequestMapping("/login")
    public String loginPage(HttpServletRequest request,HttpSession session, Map<String, Object> map) {
        String username = "";
        String password = "";
        //获取当前站点的所有Cookie
        Cookie[] cookies = request.getCookies();
        for (int i = 0; i < cookies.length; i++) {//对cookies中的数据进行遍历，找到用户名、密码的数据
            if ("username".equals(cookies[i].getName())) {
                username = cookies[i].getValue();
            } else if ("password".equals(cookies[i].getName())) {
                password = cookies[i].getValue();
            }
        }
        // 判断是否为通过拦截器重定向到这
        String msg = (String) session.getAttribute("msg");
        if(msg != null && !msg.equals("")){
            map.put("msg", msg);
            session.removeAttribute("msg");
        }
        map.put("username", username);
        map.put("password", password);
        return "admin/login";
    }

    /**
     * 后台首页
     * @return
     */
    @RequestMapping("/admin")
    public String indexPage(Model model, HttpSession session) throws Exception{
        // 三种文章类型个数,文章数
        Integer articlePublish = blogService.countBlog(BlogStatusEnum.PUBLISHED.getCode(), PostTypeEnum.POST_TYPE_POST.getCode());
        Integer articleDraft = blogService.countBlog(BlogStatusEnum.DRAFT.getCode(), PostTypeEnum.POST_TYPE_POST.getCode());
        Integer articleTrash =blogService.countBlog(BlogStatusEnum.TRASH.getCode(), PostTypeEnum.POST_TYPE_POST.getCode());
        model.addAttribute("articlePublish", articlePublish);
        model.addAttribute("articleDraft", articleDraft);
        model.addAttribute("articleTrash", articleTrash);
        model.addAttribute("articleSum", articlePublish+articleDraft+articleTrash);

        // 三种评论类型个数
        Integer commentPublish = commentService.countCommentByPass(CommentStatusEnum.PUBLISHED.getCode());
        Integer commentDraft = commentService.countCommentByPass(CommentStatusEnum.CHECKING.getCode());
        Integer commentTrash = commentService.countCommentByPass(CommentStatusEnum.RECYCLE.getCode());
        model.addAttribute("commentPublish", commentPublish);
        model.addAttribute("commentDraft", commentDraft);
        model.addAttribute("commentTrash", commentTrash);
        model.addAttribute("commentSum", commentPublish+commentDraft+commentTrash);

        // 获取附件总数
        Integer attachementSum = attachmentService.countAttachment();
        String attachSizeSum = attachmentService.sumAttachmentSize();
        model.addAttribute("attachementSum", attachementSum);
        model.addAttribute("attachSizeSum", attachSizeSum);

        // 获取登录用户
        SimpleDateFormat formatter =  new SimpleDateFormat( "yyyy-MM-dd");
        User loginUser = (User) session.getAttribute("user");
        String startTime = formatter.format(loginUser.getRegisterTime());
        String endTime = formatter.format(new Date());
        model.addAttribute("joinTime", MyUtils.caculateTotalTime(startTime, endTime));

        // 获取所有用户
        List<User> userList = userService.listUser();
        model.addAttribute("userList", userList);

        // 获取最新评论
        List<Comment> commentList = commentService.getCommentLimit(6);
        model.addAttribute("commentLit", commentList);

        // 获取最新文章
        //model.addAttribute("article",blogService.listRecentBlog(6));

        return "admin/index";
    }

    /**
     * 登录验证
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/loginVerify",method = RequestMethod.POST)
    @ResponseBody
    @SystemLog(description = "用户登录", type = LogTypeEnum.LOGIN)
    public Msg loginVerify(HttpServletRequest request, HttpServletResponse response, HttpSession session)  {
        log.info("hello================");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String rememberme = request.getParameter("rememberme");
        String verifyCode = request.getParameter("verifyCode");
        if(verifyCode==null || verifyCode.equals("")){
            return Msg.fail().add("msg","请输入验证码").add("code",101);
        } else if(!verifyCode.equals(request.getSession().getAttribute("verifyCode"))){
            return Msg.fail().add("msg","验证码错误").add("code", 101);
        } else if(username==null || username.equals("")){
            return Msg.fail().add("msg","请输入用户名").add("code", 103);
        } else if(password==null || password.equals("")){
            return Msg.fail().add("msg","请输入密码").add("code", 102);
        }
        User user = userService.getUserByNameOrEmail(username);
        if(user==null) {
            return Msg.fail().add("msg","用户名不存在！").add("code", 103);
        } else if(!user.getType()){
            return Msg.fail().add("msg","该用户不是管理员！").add("code", 103);
        } else if(!code(password).equals(user.getPassword())) {
            return Msg.fail().add("msg","密码错误！").add("code", 102);
        }
        String contextPath = request.getContextPath();
        //添加session
        session.setAttribute("user", user);
        //添加cookie
        if(rememberme!=null) {
            //创建两个Cookie对象
            Cookie nameCookie = new Cookie("username", username);
            //设置Cookie的有效期为3天
            nameCookie.setMaxAge(60 * 60 * 24 * 3);
            nameCookie.setPath(contextPath + "/");
            Cookie pwdCookie = new Cookie("password", password);
            pwdCookie.setMaxAge(60 * 60 * 24 * 3);
            pwdCookie.setPath(contextPath + "/");
            response.addCookie(nameCookie);
            response.addCookie(pwdCookie);
        }else{
            Cookie[] cookies = request.getCookies();
            for (int i = 0; i < cookies.length; i++) {//对cookies中的数据进行遍历，找到用户名、密码的数据
                if ("username".equals(cookies[i].getName()) || "password".equals(cookies[i].getName())) {
                    Cookie cookie = new Cookie(cookies[i].getName(), null);
                    cookie.setMaxAge(0);
                    cookie.setPath(contextPath + "/");
                    response.addCookie(cookie);
                }
            }
        }
        user.setLastLoginTime(new Date());
        user.setLastLoginIp(getIpAddr(request));
        userService.updateUser(user);

        //登录成功
        return Msg.success();
    }

    /**
     * 刷新验证码
     * @param request
     * @param response
     */
    @RequestMapping(value = "/verifyCode", method = RequestMethod.GET)
    public void verifyCode(HttpServletRequest request ,HttpServletResponse response){
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
     * 退出登录
     *
     * @param session
     * @return
     */
    @RequestMapping(value = "/admin/logout")
    public String logout(HttpSession session)  {
        session.removeAttribute("user");
        session.invalidate();
        return "redirect:/login";
    }

}
