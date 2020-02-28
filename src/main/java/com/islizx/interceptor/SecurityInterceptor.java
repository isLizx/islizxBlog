package com.islizx.interceptor;

import com.islizx.entity.User;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author lizx
 * @date 2020-01-31 - 11:06
 */
public class SecurityInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
        //这里可以根据session的用户来判断角色的权限，根据权限来转发不同的页面
        System.out.println("SecurityInterceptor执行===============");
        User loginUser = (User) request.getSession().getAttribute("user");
        if(loginUser == null) {
            request.getSession().setAttribute("msg", "请先进行登录操作!");
            response.sendRedirect(request.getContextPath() + "/login");
            return false;
        }else if(loginUser.getStatus()){
            request.getSession().setAttribute("msg", "该账号已被冻结,请联系管理员!");
            response.sendRedirect(request.getContextPath() + "/login");
            return false;
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
