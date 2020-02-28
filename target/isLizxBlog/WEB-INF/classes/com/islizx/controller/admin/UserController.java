package com.islizx.controller.admin;

import com.islizx.config.annotation.SystemLog;
import com.islizx.model.dto.Msg;
import com.islizx.entity.User;
import com.islizx.model.enums.LogTypeEnum;
import com.islizx.service.UserService;
import com.islizx.util.MyUtils;
import com.islizx.vo.UserQuery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;

/**
 * @author lizx
 * @date 2020-02-03 - 22:48
 */
@Controller
@Slf4j
@RequestMapping("/admin")
public class UserController {

    @Autowired
    private UserService userService;


    final String emailPattern = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";
    /**
     * 后台用户列表显示
     *
     * @return
     */
    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public ModelAndView userList(HttpSession session)  {
        ModelAndView modelandview = new ModelAndView();

        List<User> userList = userService.listUser();
        modelandview.addObject("userList",userList);
        String msg = (String) session.getAttribute("msg");
        if(msg != null && !msg.equals("")){
            modelandview.addObject("msg", msg);
            session.removeAttribute("msg");
        }
        modelandview.setViewName("admin/users/users");
        return modelandview;
    }
    /**
     * 后台添加用户页面显示
     *
     * @return
     */
    @RequestMapping(value = "/users/input")
    public String insertUserView()  {
        return "admin/users/users-add";
    }

    /**
     * 编辑用户页面显示
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/users/input/{id}")
    public ModelAndView editUserView(@PathVariable("id") Integer id)  {
        ModelAndView modelAndView = new ModelAndView();

        User user =  userService.getUserById(id);
        modelAndView.addObject("user",user);

        modelAndView.setViewName("admin/users/users-edit");
        return modelAndView;
    }

    /**
     * 检查用户名是否存在
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/users/checkUserName",method = RequestMethod.POST)
    @ResponseBody
    public Msg checkUserName(HttpServletRequest request)  {
        String username = request.getParameter("username");
        User user = userService.getUserByName(username);
        int id = Integer.valueOf(request.getParameter("id"));
        //用户名已存在,但不是当前用户(编辑用户的时候，不提示)
        if(user!=null) {
            if(user.getId()!=id) {
                return Msg.fail().add("msg", "用户名已存在");
            }
        }
        return Msg.success();
    }

    /**
     * 检查Email是否存在
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/users/checkUserEmail",method = RequestMethod.POST)
    @ResponseBody
    public Msg checkUserEmail(HttpServletRequest request)  {
        String email = request.getParameter("email");
        User user = userService.getUserByEmail(email);
        int id = Integer.valueOf(request.getParameter("id"));
        //用户名已存在,但不是当前用户(编辑用户的时候，不提示)
        if(user!=null) {
            if(user.getId()!=id) {
                return Msg.fail().add("msg", "邮箱已被注册");
            }
        }
        return Msg.success();
    }

    /**
     * 修改登录用户的资料
     * @param user
     * @return
     */
    @RequestMapping(value = "/profile", method = RequestMethod.PUT)
    @ResponseBody
    @SystemLog(description = "修改个人资料", type = LogTypeEnum.OPERATION)
    public Msg updateUser(User user){
        if(user.getId() != null){
            String username = user.getUsername();
            String email = user.getEmail();
            String nickname = user.getNickname();
            if(username == null || username.equals("")){
                return Msg.fail().add("msg","请输入用户名");
            }
            if(nickname == null || nickname.equals("")){
                return Msg.fail().add("msg","请输入昵称");
            }
            if (email == null || email.equals("")){
                return Msg.fail().add("msg","请输入email");
            } else if(!email.matches(emailPattern)){
                return Msg.fail().add("msg","邮箱格式不正确");
            }
            // 修改
            if(user.getAvatar() == null) user.setAvatar("");
            if(user.getDescription() == null) user.setDescription("");
            if(user.getUrl() == null) user.setUrl("");
            userService.updateUser(user);

            return Msg.success();
        }else{
            return Msg.fail().add("msg", "数据有误");
        }
    }

    /**
     * 后台更新用户页面提交
     *
     * @param user
     * @return
     */
    @RequestMapping(value = "/users",method = RequestMethod.POST)
    @ResponseBody
    @SystemLog(description = "添加用户", type = LogTypeEnum.OPERATION)
    public Msg insertUserSubmit(User user, HttpSession session)  {
        String username = user.getUsername();
        String email = user.getEmail();
        if(username == null || username.equals("")){
            return Msg.fail().add("msg","请输入用户名").add("code", 101);
        }
        if (email == null || email.equals("")){
            return Msg.fail().add("msg","请输入email").add("code", 102);
        } else if(!email.matches(emailPattern)){
            return Msg.fail().add("msg","邮箱格式不正确").add("code", 102);
        }
        String password = user.getPassword();
        if(password == null || password.equals("")){
            return Msg.fail().add("msg","请输入密码").add("code", 103);
        }


            // 添加
            User user2 = userService.getUserByName(user.getUsername());
            User user3 = userService.getUserByEmail(user.getEmail());

            if(user2==null&&user3==null) {
                user.setRegisterTime(new Date());
                user.setPassword(MyUtils.code(user.getPassword()));
                userService.insertUser(user);
            }
            session.setAttribute("msg", "用户添加成功!");
            return Msg.success();

    }

    /**
     * 删除用户
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/users/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    @SystemLog(description = "删除用户", type = LogTypeEnum.OPERATION)
    public Msg deleteUser(@PathVariable("id") Integer id)  {
        userService.deleteUser(id);

        return Msg.success().add("msg", "用户删除成功!");
    }

    /**
     * 基本信息页面显示
     *
     * @return
     */
    @RequestMapping(value = "/profile", method = RequestMethod.GET)
    public ModelAndView userProfileView(HttpSession session)  {

        ModelAndView modelAndView = new ModelAndView();
        User sessionUser = (User) session.getAttribute("user");
        User user =  userService.getUserById(sessionUser.getId());
        modelAndView.addObject("user",user);

        modelAndView.setViewName("admin/users/profile");
        return modelAndView;
    }

    /**
     * 后台用户列表json显示
     *
     * @return
     */
    @RequestMapping(value = "/usersByJson")
    public String userList(Model model)  {

        List<User> userList = userService.listUser();
        model.addAttribute("userList",userList);

        return "admin/users/users :: userList";
    }

    /**
     * 登录用户密码修改
     * @param request
     * @param session
     * @return
     */
    @RequestMapping(value = "/security", method = RequestMethod.PUT)
    @ResponseBody
    @SystemLog(description = "修改个人密码", type = LogTypeEnum.OPERATION)
    public Msg security(HttpServletRequest request, HttpSession session){
        String oldps = request.getParameter("oldps");
        String newps = request.getParameter("newps");
        User loginUser = (User) session.getAttribute("user");
        String oldpsDB = loginUser.getPassword();
        if(oldps == null || oldps.equals("")){
            return Msg.fail().add("msg", "旧密码不能为空！");
        }
        oldps = MyUtils.code(oldps);
        if(!oldpsDB.equals(oldps)){
            return Msg.fail().add("msg", "旧密码错误！");
        }

        if(newps == null || newps.equals("")){
            return Msg.fail().add("msg", "新密码不能为空！");
        }

        User user = new User();
        user.setId(loginUser.getId());
        user.setUpdateTime(new Date());
        user.setPassword(MyUtils.code(newps));

        userService.updateUser(user);


        return Msg.success();
    }

    @RequestMapping(value = "/users/edit", method = RequestMethod.PUT)
    @ResponseBody
    @SystemLog(description = "修改用户资料", type = LogTypeEnum.OPERATION)
    public Msg updateUser(UserQuery userQuery){
        User userDB = userService.getUserById(userQuery.getId());
        if(userDB == null){
            return Msg.fail().add("msg", "用户不存在");
        }

        userDB.setType(true);
        userDB.setUsername(userQuery.getUsername());
        userDB.setNickname(userQuery.getNickname());
        userDB.setEmail(userQuery.getEmail());
        userDB.setAvatar(userQuery.getAvatar());
        userDB.setDescription(userQuery.getDescription());
        userDB.setUrl(userQuery.getUrl());
        userDB.setStatus(userQuery.getStatus());

        userService.updateUser(userDB);

        return Msg.success().add("msg", "用户资料修改成功");
    }

    /**
     * 用户密码修改
     * @param request
     * @return
     */
    @RequestMapping(value = "/users/security", method = RequestMethod.PUT)
    @ResponseBody
    @SystemLog(description = "修改用户密码", type = LogTypeEnum.OPERATION)
    public Msg updateUserPassword(HttpServletRequest request){
        String newps = request.getParameter("newps");
        String id = request.getParameter("id");
        if(newps == null || newps.equals("")){
            return Msg.fail().add("msg", "新密码不能为空！");
        }

        User user = new User();
        user.setId(Integer.valueOf(id));
        user.setUpdateTime(new Date());
        user.setPassword(MyUtils.code(newps));

        userService.updateUser(user);


        return Msg.success();
    }

}
