package com.pm.service;

import com.pm.dto.auth.LoginRequest;
import com.pm.vo.auth.LoginVO;

public interface AuthService {

    LoginVO login(LoginRequest request);
}
