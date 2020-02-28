package com.islizx.exception;

import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author lizx
 * @date 2020-02-21 - 12:37
 */
public class MyDispatcherServlet extends DispatcherServlet {
    @Override
    protected void noHandlerFound(HttpServletRequest request, HttpServletResponse response) throws Exception {

        request.getRequestDispatcher("/error").forward(request, response);
//        response.sendRedirect(request.getContextPath().concat("/error/404.html"));
    }
}
