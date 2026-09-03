package com.pm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.pm.dto.auth.LoginRequest;
import com.pm.entity.SysRole;
import com.pm.entity.SysUser;
import com.pm.exception.BusinessException;
import com.pm.mapper.SysRoleMapper;
import com.pm.mapper.SysUserMapper;
import com.pm.security.JwtTokenProvider;
import com.pm.service.AuthService;
import com.pm.vo.auth.LoginVO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final SysUserMapper sysUserMapper;
    private final SysRoleMapper sysRoleMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public LoginVO login(LoginRequest request) {
        // 查询用户
        SysUser user = sysUserMapper.selectOne(
                new LambdaQueryWrapper<SysUser>()
                        .eq(SysUser::getUsername, request.getUsername())
        );

        if (user == null) {
            throw new BusinessException(401, "用户名或密码错误");
        }

        if (user.getStatus() != null && user.getStatus() == 0) {
            throw new BusinessException(401, "账户已被禁用");
        }

        // 校验密码
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BusinessException(401, "用户名或密码错误");
        }

        // 查询角色
        SysRole role = sysRoleMapper.selectById(user.getRoleId());
        String roleCode = role != null ? role.getRoleCode() : "VIEWER";

        // 生成JWT
        String token = jwtTokenProvider.generateToken(user.getId(), user.getUsername(), roleCode);

        return LoginVO.builder()
                .token(token)
                .username(user.getUsername())
                .realName(user.getRealName())
                .roleCode(roleCode)
                .build();
    }
}
