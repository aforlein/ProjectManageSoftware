package com.pm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.pm.dto.system.RoleDTO;
import com.pm.entity.SysRole;
import com.pm.entity.SysUser;
import com.pm.exception.BusinessException;
import com.pm.mapper.SysRoleMapper;
import com.pm.mapper.SysUserMapper;
import com.pm.service.RoleService;
import com.pm.vo.system.RoleVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final SysRoleMapper sysRoleMapper;
    private final SysUserMapper sysUserMapper;

    @Override
    public List<RoleVO> listAll() {
        List<SysRole> roles = sysRoleMapper.selectList(null);
        return roles.stream().map(role -> {
            RoleVO vo = new RoleVO();
            BeanUtils.copyProperties(role, vo);
            return vo;
        }).collect(Collectors.toList());
    }

    @Override
    public List<RoleVO> page() {
        return listAll();
    }

    @Override
    @Transactional
    public Long create(RoleDTO dto) {
        Long count = sysRoleMapper.selectCount(
                new LambdaQueryWrapper<SysRole>().eq(SysRole::getRoleCode, dto.getRoleCode()));
        if (count > 0) {
            throw new BusinessException("角色编码已存在");
        }
        SysRole role = new SysRole();
        BeanUtils.copyProperties(dto, role);
        sysRoleMapper.insert(role);
        return role.getId();
    }

    @Override
    @Transactional
    public void update(Long id, RoleDTO dto) {
        SysRole role = sysRoleMapper.selectById(id);
        if (role == null) {
            throw new BusinessException("角色不存在");
        }

        Long count = sysRoleMapper.selectCount(
                new LambdaQueryWrapper<SysRole>()
                        .eq(SysRole::getRoleCode, dto.getRoleCode())
                        .ne(SysRole::getId, id));
        if (count > 0) {
            throw new BusinessException("角色编码已存在");
        }

        SysRole updateRole = new SysRole();
        updateRole.setId(id);
        BeanUtils.copyProperties(dto, updateRole);
        sysRoleMapper.updateById(updateRole);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        // 检查是否有用户关联此角色
        Long count = sysUserMapper.selectCount(
                new LambdaQueryWrapper<SysUser>().eq(SysUser::getRoleId, id));
        if (count > 0) {
            throw new BusinessException("该角色下还有用户，无法删除");
        }
        sysRoleMapper.deleteById(id);
    }
}
