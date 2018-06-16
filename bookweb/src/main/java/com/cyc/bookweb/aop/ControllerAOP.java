package com.cyc.bookweb.aop;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class ControllerAOP {
    private static Logger log = Logger.getLogger(ControllerAOP.class);
    public Object aroundMethod(ProceedingJoinPoint joinpoint) {// ProceedingJoinPoint为通知
        Object obj = null;
        try {
            long start = System.currentTimeMillis();
            log.info("+++++controller方法开始...");
            obj = joinpoint.proceed();// 执行通知的方法
            long end = System.currentTimeMillis();
            long actionTime = end - start;
            log.info("++++++controller方法结束,执行时间为:" + (actionTime));
            /**
             * 记录日志
             */
            RequestAttributes ra = RequestContextHolder.getRequestAttributes();
            ServletRequestAttributes sra = (ServletRequestAttributes) ra;
            HttpServletRequest req = sra.getRequest();
            Object target = joinpoint.getTarget();
            String methodName = joinpoint.getSignature().getName();
            // String className =
            // h.getBean().getClass().getSuperclass().getSimpleName();
            String className = target.getClass().getSimpleName();
            // String methodName = h.getMethod().getName();
            String requestURI = req.getRequestURI();
            // log.info("+++++className:" + className);
            // log.info("+++++methodName:" + methodName);
            // log.info("+++++requestURI:" + requestURI);
            /**
             * 保存日志
             */
            String remoteHost = req.getRemoteHost();
            // log.info("+++++remoteHost:" + remoteHost);
            String localAddr = req.getLocalAddr();
            // log.info("+++++localAddr:" + localAddr);
            String remoteAddr = req.getRemoteAddr();
            // log.info("+++++remoteAddr:" + remoteAddr);
            String remoteUser = req.getRemoteUser();
            // log.info("+++++remoteUser:" + remoteUser);
            String localName = req.getLocalName();
            // log.info("+++++localName:" + localName);
            String serverName = req.getServerName();
            // log.info("+++++serverName:" + serverName);
            String userName = "游客用户";
            // HttpSession session = req.getSession();
            // String userName = (session != null && (User)
            // session.getAttribute("userLogin") != null)
            // ? ((User) session.getAttribute("userLogin")).getLoginName() :
            // "游客用户";
            String userNickName = "未设置";
            return obj;
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return obj;

    }
}
