package com.islizx.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.ui.Model;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.ValidationException;

/**
 * @author lizx
 * @date 2020-02-20 - 21:50
 */
@ControllerAdvice
@Slf4j
public class ControllerExceptionHandler {
    /**
     * 400 - Bad Request
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ModelAndView handleMissingServletRequestParameterException(MissingServletRequestParameterException e, Model model) {
        //log.error("缺少请求参数", e);
        String message = "【缺少请求参数】" + e.getMessage();
        ModelAndView modelAndView = new ModelAndView("error/error");
        modelAndView.addObject("message", message);
        modelAndView.addObject("code", 400);
        modelAndView.addObject("exception", e);
        return modelAndView;
    }

    /**
     * 400 - Bad Request
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ModelAndView handleHttpMessageNotReadableException(HttpMessageNotReadableException e, Model model) {
        //log.error("参数解析失败", e);
        String message = "【参数解析失败】" + e.getMessage();
        ModelAndView modelAndView = new ModelAndView("error/error");
        modelAndView.addObject("message", message);
        modelAndView.addObject("code", 400);
        modelAndView.addObject("exception", e);
        return modelAndView;
    }

    /**
     * 400 - Bad Request
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ModelAndView handleMethodArgumentNotValidException(MethodArgumentNotValidException e, Model model) {
        //log.error("参数验证失败", e);
        BindingResult result = e.getBindingResult();
        FieldError error = result.getFieldError();
        String field = error.getField();
        String code = error.getDefaultMessage();
        String message = "【参数验证失败】" + String.format("%s:%s", field, code);
        ModelAndView modelAndView = new ModelAndView("error/error");
        modelAndView.addObject("message", message);
        modelAndView.addObject("code", 400);
        modelAndView.addObject("exception", e);
        return modelAndView;
    }

    /**
     * 400 - Bad Request
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BindException.class)
    public ModelAndView handleBindException(BindException e) {
        //log.error("参数绑定失败", e);
        BindingResult result = e.getBindingResult();
        FieldError error = result.getFieldError();
        String field = error.getField();
        String code = error.getDefaultMessage();
        String message = "【参数绑定失败】" + String.format("%s:%s", field, code);

        ModelAndView modelAndView = new ModelAndView("error/error");
        modelAndView.addObject("message", message);
        modelAndView.addObject("code", 400);
        modelAndView.addObject("exception", e);
        return modelAndView;
    }



    /**
     * 400 - Bad Request
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ValidationException.class)
    public ModelAndView handleValidationException(ValidationException e) {
        //log.error("参数验证失败", e);
        String message = "【参数验证失败】" + e.getMessage();
        ModelAndView modelAndView = new ModelAndView("error/error");
        modelAndView.addObject("message", message);
        modelAndView.addObject("code", 400);
        modelAndView.addObject("exception", e);
        return modelAndView;
    }

    /**
     * 405 - Method Not Allowed
     */
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ModelAndView handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        //log.error("不支持当前请求方法", e);
        String message = "【不支持当前请求方法】" + e.getMessage();
        ModelAndView modelAndView = new ModelAndView("error/error");
        modelAndView.addObject("message", message);
        modelAndView.addObject("code", 405);
        modelAndView.addObject("exception", e);
        return modelAndView;
    }

    /**
     * 415 - Unsupported Media Type
     */
    @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ModelAndView handleHttpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException e) {
        //log.error("不支持当前媒体类型", e);
        //其他异常

        String message = "【不支持当前媒体类型】" + e.getMessage();
        ModelAndView modelAndView = new ModelAndView("error/error");
        modelAndView.addObject("message", message);
        modelAndView.addObject("code", 415);
        modelAndView.addObject("exception", e);
        return modelAndView;
    }

    @ExceptionHandler(value = Exception.class)
    public ModelAndView handlerError(Exception e) {
        //其他异常
        ModelAndView modelAndView = new ModelAndView("error/error");
        String message = e.getMessage();
        modelAndView.addObject("code", 500);
        modelAndView.addObject("message", message);
        modelAndView.addObject("exception", e);
        return modelAndView;
    }
}
