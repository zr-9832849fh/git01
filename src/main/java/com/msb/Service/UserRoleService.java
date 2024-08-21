package com.msb.Service;

import com.msb.base.BaseService;
import com.msb.mappers.UserRoleMapper;
import com.msb.vo.UserRole;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserRoleService extends BaseService<UserRole,Integer> {
    @Resource
    private UserRoleMapper userRoleMapper;
}
