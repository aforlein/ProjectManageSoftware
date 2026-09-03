package com.pm.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pm.common.PageResult;
import com.pm.dto.system.PasswordChangeDTO;
import com.pm.dto.system.UserDTO;
import com.pm.dto.system.UserPageQuery;
import com.pm.entity.SysRole;
import com.pm.entity.SysUser;
import com.pm.exception.BusinessException;
import com.pm.mapper.SysRoleMapper;
import com.pm.mapper.SysUserMapper;
import com.pm.service.UserService;
import com.pm.vo.system.UserVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final SysUserMapper sysUserMapper;
    private final SysRoleMapper sysRoleMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public PageResult<UserVO> page(UserPageQuery query) {
        Page<SysUser> page = new Page<>(query.getPage(), query.getSize());

        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        if (StrUtil.isNotBlank(query.getKeyword())) {
            wrapper.and(w -> w
                    .like(SysUser::getUsername, query.getKeyword())
                    .or()
                    .like(SysUser::getRealName, query.getKeyword())
                    .or()
                    .like(SysUser::getPhone, query.getKeyword()));
        }
        if (query.getRoleId() != null) {
            wrapper.eq(SysUser::getRoleId, query.getRoleId());
        }
        if (query.getStatus() != null) {
            wrapper.eq(SysUser::getStatus, query.getStatus());
        }
        wrapper.orderByDesc(SysUser::getCreateTime);

        Page<SysUser> userPage = sysUserMapper.selectPage(page, wrapper);

        // 批量获取角色名称
        List<Long> roleIds = userPage.getRecords().stream()
                .map(SysUser::getRoleId)
                .distinct()
                .collect(Collectors.toList());
        Map<Long, String> roleMap = sysRoleMapper.selectBatchIds(roleIds).stream()
                .collect(Collectors.toMap(SysRole::getId, SysRole::getRoleName));

        List<UserVO> voList = userPage.getRecords().stream().map(user -> {
            UserVO vo = new UserVO();
            BeanUtils.copyProperties(user, vo);
            vo.setRoleName(roleMap.getOrDefault(user.getRoleId(), "未知"));
            return vo;
        }).collect(Collectors.toList());

        return PageResult.of(userPage, voList);
    }

    @Override
    @Transactional
    public Long create(UserDTO dto) {
        // 校验用户名唯一
        Long count = sysUserMapper.selectCount(
                new LambdaQueryWrapper<SysUser>().eq(SysUser::getUsername, dto.getUsername()));
        if (count > 0) {
            throw new BusinessException("用户名已存在");
        }

        SysUser user = new SysUser();
        BeanUtils.copyProperties(dto, user);
        user.setPassword(passwordEncoder.encode(
                StrUtil.isNotBlank(dto.getPassword()) ? dto.getPassword() : "123456"));
        user.setStatus(1);
        sysUserMapper.insert(user);
        return user.getId();
    }

    @Override
    @Transactional
    public void update(Long id, UserDTO dto) {
        SysUser user = sysUserMapper.selectById(id);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }

        // 校验用户名唯一（排除自身）
        Long count = sysUserMapper.selectCount(
                new LambdaQueryWrapper<SysUser>()
                        .eq(SysUser::getUsername, dto.getUsername())
                        .ne(SysUser::getId, id));
        if (count > 0) {
            throw new BusinessException("用户名已存在");
        }

        SysUser updateUser = new SysUser();
        updateUser.setId(id);
        BeanUtils.copyProperties(dto, updateUser, "password");
        sysUserMapper.updateById(updateUser);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        sysUserMapper.deleteById(id); // 逻辑删除
    }

    @Override
    @Transactional
    public void toggleStatus(Long id) {
        SysUser user = sysUserMapper.selectById(id);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        SysUser updateUser = new SysUser();
        updateUser.setId(id);
        updateUser.setStatus(user.getStatus() == 1 ? 0 : 1);
        sysUserMapper.updateById(updateUser);
    }

    @Override
    @Transactional
    public void changePassword(Long id, PasswordChangeDTO dto) {
        SysUser user = sysUserMapper.selectById(id);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }

        if (!passwordEncoder.matches(dto.getOldPassword(), user.getPassword())) {
            throw new BusinessException("旧密码不正确");
        }

        SysUser updateUser = new SysUser();
        updateUser.setId(id);
        updateUser.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        sysUserMapper.updateById(updateUser);
    }
}
