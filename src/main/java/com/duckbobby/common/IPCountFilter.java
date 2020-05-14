package com.duckbobby.common;

import com.duckbobby.service.IPCountService;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import java.io.IOException;
import javax.servlet.*;

public class IPCountFilter implements Filter {
    private IPCountService ipCountService;

    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        //过滤器层面进行统计访问量
        ServletContext servletContext = req.getServletContext();
        ApplicationContext applicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
        ipCountService = applicationContext.getBean(IPCountService.class);
        ipCountService.addIPCountOther(req);
        chain.doFilter(req, res);
    }

    public void init(FilterConfig filterConfig) {
    }

    public void destroy() {
    }

}