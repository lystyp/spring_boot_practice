package com.example.spring_boot_pracrice.config;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.net.HttpCookie;
import java.util.Iterator;

@Slf4j
public class RequestInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        log.info("preHandle request :" + request.toString());
        HttpSession session = request.getSession();
        Iterator<String> i = session.getAttributeNames().asIterator();
        while (i.hasNext()) {
            String s = i.next();
            log.info(s + " : " + session.getAttribute(s).toString());
        }

        Cookie[] cookie = request.getCookies();

        for (Cookie c : cookie) {
            log.info(c.getName() + " : " + c.getAttribute(c.getName() ));
        }

        return true;
    }

    /**
     * 目标方法执行完成以后
     * @param request
     * @param response
     * @param handler
     * @param modelAndView
     * @throws Exception
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        log.info("postHandle request :" + request.toString());
        HttpSession session = request.getSession();
        Iterator<String> i = session.getAttributeNames().asIterator();
        while (i.hasNext()) {
            String s = i.next();
            log.info(s + " : " + session.getAttribute(s).toString());
        }

        Cookie[] cookie = request.getCookies();

        for (Cookie c : cookie) {
            log.info(c.getName() + " : " + c.getAttribute(c.getName() ));
        }
    }

    /**
     * 页面渲染以后
     * @param request
     * @param response
     * @param handler
     * @param ex
     * @throws Exception
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        log.info("afterCompletion request :" + request.toString());
    }
}
