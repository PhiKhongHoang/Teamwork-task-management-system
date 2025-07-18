package com.ktn3.TTMS.service;

import com.ktn3.TTMS.dto.request.user.ReqCreateUser;
import com.ktn3.TTMS.dto.response.ResCommonApi;

public interface UserService {
    ResCommonApi<?> create(ReqCreateUser req);
    ResCommonApi<?> findById(Long id);
}
