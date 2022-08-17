package org.cloud.filter;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.exceptions.ValidateException;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONException;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTUtil;
import cn.hutool.jwt.JWTValidator;
import cn.hutool.jwt.signers.JWTSignerUtil;
import lombok.extern.slf4j.Slf4j;
import org.cloud.properties.JWTProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 每次处理请求时，从请求中获取token并token放入SecurityContext中。以便授权过滤器对token的校验
 */
@Component
@Slf4j
public class TokenFilter extends OncePerRequestFilter {

    private static final String FILTER_APPLIED = "__spring_security_demoFilter_filterApplied";

    @Autowired
    private JWTProperties jwtProperties;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("客户端访问 {}", request.getServletPath());

        String authentication = request.getHeader("Authorization");

        if (StrUtil.isNotEmpty(authentication) && !"/favicon.ico".equals(request.getServletPath())) {
            // 1. 获取请求头中的 Authentication 属性
            log.info("收到了请求里的 Authorization : " + authentication);

            // 2. 提取出Bearer后的token
            String token = StrUtil.removePrefix(authentication, "Bearer ");

            try {
                // 3. 校验token，如果正确就创建Authentication并放进SecurityContext；不正确就抛异常
                // 校验 jwt
                JWTValidator.of(token).validateAlgorithm(JWTSignerUtil.hs256(jwtProperties.getKeyBytes()));
                JWTValidator.of(token).validateDate(DateUtil.date());

                // 解析 jwt
                final JWT jwt = JWTUtil.parseToken(token);

                String username = (String) jwt.getPayload("username");

                // 封装认证信息
                // TODO: 2022/8/14 获取到权限列表，再把它放到 authentication
                UsernamePasswordAuthenticationToken authenticationToken
                        = new UsernamePasswordAuthenticationToken(username, jwt.getPayload("uid"), AuthorityUtils.commaSeparatedStringToAuthorityList((String) jwt.getPayload("authentication")));

                SecurityContextHolder.getContext().setAuthentication(authenticationToken);

                log.info("token 合法，将其放到上下文中");

            } catch (JSONException e) {
                log.info("接收的 jwt 不是合法的格式");
                throw e;
            } catch (ValidateException e) {
                log.info("jwt 过期");
                throw e;
            }

        }

        filterChain.doFilter(request, response);

    }

}
