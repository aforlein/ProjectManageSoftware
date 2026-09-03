package com.pm.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pm.common.PageResult;
import com.pm.dto.system.PasswordChangeDTO;
import com.pm.dto.system.UserDTO;
import com.pm.dto.system.UserPageQuery;
import com.pm.vo.system.UserVO;

public interface UserService {

    PageResult<UserVO> page(UserPageQuery query);

    Long create(UserDTO dto);

    void update(Long id, UserDTO dto);

    void delete(Long id);

    void toggleStatus(Long id);

    void changePassword(Long id, PasswordChangeDTO dto);
}
