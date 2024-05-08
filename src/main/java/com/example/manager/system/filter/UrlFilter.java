package com.example.manager.system.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.IOException;

public class UrlFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest)request;
        HttpServletResponseWrapper httpResponse = new HttpServletResponseWrapper((HttpServletResponse) response);
        System.out.println(httpRequest.getRequestURI());
        String path=httpRequest.getRequestURI();
        if(path.indexOf("userId")<0 && path.indexOf("accountName")<0 && path.indexOf("role")<0){
            path=path+"?" + "userId" + "=" + ((HttpServletRequest) request).getHeader("userId") +
                    "&" + "accountName" + "=" + ((HttpServletRequest) request).getHeader("accountName") +
                    "&" + "role" + "=" + ((HttpServletRequest) request).getHeader("role");
            System.out.println(path);
            httpRequest.getRequestDispatcher(path).forward(request,response);
        }
        else {
            chain.doFilter(request,response);
        }
        return;
    }
}
