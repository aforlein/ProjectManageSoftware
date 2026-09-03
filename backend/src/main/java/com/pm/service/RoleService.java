package com.pm.service;

import com.pm.dto.system.RoleDTO;
import com.pm.vo.system.RoleVO;

import java.util.List;

public interface RoleService {

    List<RoleVO> listAll();

    List<RoleVO> page();

    Long create(RoleDTO dto);

    void update(Long id, RoleDTO dto);

    void delete(Long id);
}
