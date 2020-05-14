package com.duckbobby.service.Impl;

import com.duckbobby.dao.IPcountDao;
import com.duckbobby.model.IPCount;
import com.duckbobby.service.IPCountService;
import com.duckbobby.utils.IPUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Service
public class IPCountServiceImpl implements IPCountService {
    @Autowired
    IPcountDao IPcountDao;
    @Override
    public void addIPCountOther(ServletRequest req) {
        String requestURI = ((HttpServletRequest) req).getRequestURI();
        if (requestURI.endsWith(".css") ||
                requestURI.endsWith(".js") ||
                requestURI.endsWith(".png") ||
                requestURI.endsWith(".woff2") ||
                requestURI.endsWith(".ico") ||
                requestURI.endsWith(".woff") ||
                requestURI.endsWith(".gif") ||
                requestURI.endsWith(".css") ||
                requestURI.endsWith(".jpg") ||
                requestURI.endsWith("/404")) {
            return;
        }
        IPCount ipCount = new IPCount();
        ipCount.setUrl(requestURI);
        ipCount.setAccessTime(new Date());
        String realIP = IPUtils.getRemoteIp((HttpServletRequest) req);
        ipCount.setIPAddress(realIP);
        ipCount.setFilterType(2);
        IPcountDao.addIPCount(ipCount);
    }
}
