package com.fs.myerp.config.security;

import com.fs.myerp.dao.SysuserDao;
import com.fs.myerp.po.SysuserPo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.access.intercept.DefaultFilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Component
public class CustomUserDetailsService implements UserDetailsService {

    public static void main(String[] args) {
        AntPathMatcher antPathMatcher = new AntPathMatcher();
        boolean match = antPathMatcher.match("/employee/aa", "/employee/basic/hello");
        if (match){
            System.out.println("yes");
        } else {
            System.out.println("no");
        }
    }

    private Logger log = LoggerFactory.getLogger(getClass());

    @Resource
    private SysuserDao userDao;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SysuserPo po = userDao.selectByUsername(username);
        if (po == null){
            throw new UsernameNotFoundException("用户"+username+"不存在！");
        }

        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        SimpleGrantedAuthority admin = new SimpleGrantedAuthority("ADMIN");
        grantedAuthorities.add(admin);

        return new User(username, po.getPassword(), grantedAuthorities);
    }
}
