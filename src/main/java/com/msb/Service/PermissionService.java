package com.msb.Service;

import com.msb.base.BaseService;
import com.msb.mappers.PermissionMapper;
import com.msb.vo.Permission;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class PermissionService extends BaseService<Permission,Integer> {
    @Resource
    private PermissionMapper permissionMapper;

    /**
     * 通过查询用户拥有的角色，角色拥有的资源，得到用户拥有的资源列表（资源权限码）
     * @param userId
     * @return
     */
    public List<String> queryUserHasRoleHasPermissionByUserId(Integer userId) {
        return permissionMapper.queryUserHasRoleHasPermissionByUserId(userId);
    }
}
