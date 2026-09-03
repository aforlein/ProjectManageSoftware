package com.pm.security;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.pm.entity.SysUser;
import com.pm.mapper.SysUserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final SysUserMapper sysUserMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SysUser sysUser = sysUserMapper.selectOne(
                new LambdaQueryWrapper<SysUser>()
                        .eq(SysUser::getUsername, username)
                        .eq(SysUser::getStatus, 1)
        );

        if (sysUser == null) {
            throw new UsernameNotFoundException("用户不存在或已被禁用: " + username);
        }

        return new User(
                sysUser.getUsername(),
                sysUser.getPassword(),
                Collections.emptyList()
        );
    }
}
