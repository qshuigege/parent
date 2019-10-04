package com.fs.testsecurity.config.security;

import com.fs.diyutils.JsonResponse;
import com.fs.testsecurity.utils.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.web.firewall.StrictHttpFirewall;
import org.springframework.security.web.session.SessionInformationExpiredEvent;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration
//@EnableRedisHttpSession
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    private Logger log = LoggerFactory.getLogger(getClass());

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public HttpFirewall allowUrlSemicolonHttpFirewall() {
        StrictHttpFirewall firewall = new StrictHttpFirewall();
        firewall.setAllowSemicolon(true);
        return firewall;
    }

    /*@Bean
    public LettuceConnectionFactory connectionFactory() {
        return new LettuceConnectionFactory();
    }*/

    @Resource
    private UserDetailsService userDetailsService;

    @Resource
    private PasswordEncoder pwdEnc;

    AuthenticationEntryPoint authenticationEntryPoint;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers(
                        "/user/addUser",
                        "/test/**",
                        "/noauth/**"
                ).permitAll()
                .anyRequest().authenticated()
        .and().formLogin()
                .loginProcessingUrl("/user/login")
                .successHandler(new AuthenticationSuccessHandler() {
                    @Override
                    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
                        log.debug("登录成功！");
                        response.setContentType("application/json;charset=utf-8");
                        response.getWriter().print(JsonUtils.objectToJson(JsonResponse.success(authentication)));
                    }
                })
                .failureHandler(new AuthenticationFailureHandler() {
                    @Override
                    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
                        log.debug("登录失败！");
                        response.setContentType("application/json;charset=utf-8");
                        response.getWriter().print(JsonUtils.objectToJson(JsonResponse.fail(exception)));
                    }
                })
        .and().sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
                .invalidSessionUrl("/noauth/session/invalid")
                .maximumSessions(1)
                //.maxSessionsPreventsLogin(true)
                .expiredSessionStrategy(new SessionInformationExpiredStrategy() {
                    @Override
                    public void onExpiredSessionDetected(SessionInformationExpiredEvent event) throws IOException, ServletException {
                        HttpServletResponse response = event.getResponse();
                        response.setContentType("application/json;charset=utf-8");
                        response.getWriter().print(JsonUtils.objectToJson(JsonResponse.fail("你的账户已在另一地点登录！")));
                    }
                }).and()
        .and().csrf()
                .disable();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(pwdEnc);
    }
}
