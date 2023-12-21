package com.fullstackproject.ecommerce.Config;

import com.fullstackproject.ecommerce.Service.TokenService;
import org.bson.types.ObjectId;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtFilter extends GenericFilterBean {
    private final TokenService tokenService;
    public JwtFilter(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Override
    public void doFilter(ServletRequest servletRequest,
                         ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;

        String token = httpServletRequest.getHeader("Authorization");
        if("OPTIONS".equalsIgnoreCase(httpServletRequest.getMethod())){
            httpServletResponse.sendError(HttpServletResponse.SC_OK, "success");
            return;
        }
        //specific APIs without token
        if (allowRequestWithoutToken(httpServletRequest)){
            httpServletResponse.setStatus(HttpServletResponse.SC_OK);
            filterChain.doFilter(servletRequest, servletResponse);
        }else{
            ObjectId userId = new ObjectId(tokenService.verifyTokenAndDecrypt(token));
            httpServletRequest.setAttribute("userId", userId);
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }

    public boolean allowRequestWithoutToken(HttpServletRequest httpServletRequest){
        System.out.println(httpServletRequest.getRequestURI());
        return httpServletRequest.getRequestURI().contains("/users");
    }
}