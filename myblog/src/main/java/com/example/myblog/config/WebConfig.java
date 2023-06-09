package com.example.myblog.config;

import jakarta.servlet.Servlet;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRegistration;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

//WebApplicationInitializer就是将spring mvc部署到servlet容器里面
public class WebConfig implements WebApplicationInitializer {
    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        AnnotationConfigWebApplicationContext ctx=new AnnotationConfigWebApplicationContext();
        ctx.register(WebMvcConfig.class);//注册SpringMvc的配置类WebMvcConfig
        ctx.setServletContext(servletContext);//和当前ServletContext关联
        /**
         * 注册SpringMvc的DispatcherServlet
         */
        ServletRegistration.Dynamic servlet=servletContext.addServlet("dispatcher", (Servlet) new DispatcherServlet(ctx));
        servlet.addMapping("/");
        servlet.setLoadOnStartup(1);
    }
}
