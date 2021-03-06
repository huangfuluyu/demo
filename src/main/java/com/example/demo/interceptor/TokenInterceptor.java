package com.example.demo.interceptor;

import com.example.demo.config.JwtConfig;
import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.AsyncHandlerInterceptor;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.SignatureException;

/**
 * @author : HuangFu
 * @Description : TokenInterceptor 拦截器
 * @date : 2022-05-24 10:49
 **/
@Component
public class TokenInterceptor implements HandlerInterceptor {
    @Resource
    private JwtConfig jwtConfig;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 地址过滤
        String uri = request.getRequestURI() ;
        if (uri.contains("/login")){
            return true ;
        }

        //Token 验证
        String token = request.getHeader(jwtConfig.getHeader());
        if(!StringUtils.hasLength(token)){
            token = request.getParameter(jwtConfig.getHeader());
        }
        if(!StringUtils.hasLength(token)){
            throw new SignatureException(jwtConfig.getHeader()+ "不能为空");
        }

        Claims claims = null;
        try{
            claims = jwtConfig.getTokenClaim(token);
            if(claims == null || jwtConfig.isTokenExpired(claims.getExpiration())){
                throw new SignatureException(jwtConfig.getHeader() + "失效，请重新登录。");
            }
        }catch (Exception e){
            throw new SignatureException(jwtConfig.getHeader() + "失效，请重新登录。");
        }
        // 设置 identityId 用户身份ID
        request.setAttribute("identityId", claims.getSubject());
        return true;
    }

}
