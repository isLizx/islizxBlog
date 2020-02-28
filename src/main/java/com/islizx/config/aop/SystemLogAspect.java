package com.islizx.config.aop;


import com.islizx.config.annotation.SystemLog;
import com.islizx.entity.Log;
import com.islizx.entity.User;
import com.islizx.model.enums.LogTypeEnum;
import com.islizx.service.LogService;
import com.islizx.util.MyUtils;
import com.islizx.util.ObjectUtil;
import com.islizx.util.ThreadPoolUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.NamedThreadLocal;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Spring AOP实现日志管理
 *
 * @author lizx
 * @date 2020-02-21 - 14:33
 */
@Aspect
@Component
@Slf4j
public class SystemLogAspect {
    private static final ThreadLocal<Date> beginTimeThreadLocal = new NamedThreadLocal<Date>("ThreadLocal beginTime");

    @Autowired
    private LogService logService;

    @Autowired(required = false)
    private HttpServletRequest request;


    /**
     * Controller层切点,注解方式
     */
    @Pointcut("@annotation(com.islizx.config.annotation.SystemLog)")
    public void controllerAspect() {

    }

    /**
     * 前置通知 (在方法执行之前返回)用于拦截Controller层记录用户的操作的开始时间
     *
     * @param joinPoint 切点
     * @throws InterruptedException
     */
    @Before("controllerAspect()")
    public void doBefore(JoinPoint joinPoint) throws InterruptedException {

        //线程绑定变量（该数据只有当前请求的线程可见）
        Date beginTime = new Date();
        beginTimeThreadLocal.set(beginTime);
    }


    /**
     * 后置通知(在方法执行之后并返回数据) 用于拦截Controller层无异常的操作
     *
     * @param joinPoint 切点
     */
    @AfterReturning("controllerAspect()")
    public void after(JoinPoint joinPoint) {
        try {
            String username = "";
            String description = getControllerMethodInfo(joinPoint).get("description").toString();
            Map<String, String[]> logParams = request.getParameterMap();
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            HttpSession session = request.getSession();
            if (session.getAttribute("user") != null) {
                User user = (User) session.getAttribute("user");
                username = user.getUsername();
            }

            Log log = new Log();
            //请求用户
            log.setUsername(username);
            //日志标题
            log.setName(description);
            //日志类型
            String logType = String.valueOf(getControllerMethodInfo(joinPoint).get("type"));
            log.setLogType(logType);
            //日志请求url
            log.setRequestUrl(request.getRequestURI());
            //请求方式
            log.setRequestType(request.getMethod());
            // 请求时间
            log.setCreateTime(new Date());
            //请求参数
            if(Objects.equals(logType, LogTypeEnum.OPERATION.getValue())) {
                log.setRequestParam("忽略");
            } else {
                log.setRequestParam(ObjectUtil.mapToString(logParams));
            }
            //请求IP
            log.setIp(MyUtils.getIpAddr(request));
            //IP地址
            //log.setIpInfo(ipInfoUtil.getIpCity(ipInfoUtil.getIpAddr(request)));
            //请求开始时间
            long beginTime = beginTimeThreadLocal.get().getTime();
            long endTime = System.currentTimeMillis();
            //请求耗时
            Long logElapsedTime = endTime - beginTime;
            log.setCostTime(logElapsedTime.intValue());
            //调用线程保存至数据库
            ThreadPoolUtil.getPool().execute(new SaveSystemLogThread(log, logService));

        } catch (Exception e) {
            log.error("AOP后置通知异常", e);
        }
    }


    /**
     * 保存日志至数据库
     */
    private static class SaveSystemLogThread implements Runnable {

        private Log log;
        private LogService logService;

        public SaveSystemLogThread(Log esLog, LogService logService) {
            this.log = esLog;
            this.logService = logService;
        }

        @Override
        public void run() {
            logService.insertOrUpdate(log);
        }
    }

    /**
     * 获取注解中对方法的描述信息 用于Controller层注解
     *
     * @param joinPoint 切点
     * @return 方法描述
     * @throws Exception
     */
    public static Map<String, Object> getControllerMethodInfo(JoinPoint joinPoint) throws Exception {

        Map<String, Object> map = new HashMap<String, Object>(16);
        //获取目标类名
        String targetName = joinPoint.getTarget().getClass().getName();
        //获取方法名
        String methodName = joinPoint.getSignature().getName();
        //获取相关参数
        Object[] arguments = joinPoint.getArgs();
        //生成类对象
        Class targetClass = Class.forName(targetName);
        //获取该类中的方法
        Method[] methods = targetClass.getMethods();

        String description = "";
        String type = "";

        for (Method method : methods) {
            if (!method.getName().equals(methodName)) {
                continue;
            }
            Class[] clazzs = method.getParameterTypes();
            if (clazzs.length != arguments.length) {
                //比较方法中参数个数与从切点中获取的参数个数是否相同，原因是方法可以重载哦
                continue;
            }
            description = method.getAnnotation(SystemLog.class).description();
            type = method.getAnnotation(SystemLog.class).type().getValue();
            map.put("description", description);
            map.put("type", type);
        }
        return map;
    }

}
