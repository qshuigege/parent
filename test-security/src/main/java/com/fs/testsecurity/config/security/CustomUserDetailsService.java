package com.fs.testsecurity.config.security;

import com.fs.testsecurity.dao.CrmSysUserDao;
import com.fs.testsecurity.po.CrmSysUserPo;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Component
public class CustomUserDetailsService implements UserDetailsService {

    @Resource
    private CrmSysUserDao userDao;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        CrmSysUserPo po = userDao.selectByUsername(username);
        if (po == null){
            throw new UsernameNotFoundException("用户"+username+"不存在！");
        }

        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        SimpleGrantedAuthority admin = new SimpleGrantedAuthority("ADMIN");
        grantedAuthorities.add(admin);

        return new User(username, po.getPassword(), grantedAuthorities);
    }
}
