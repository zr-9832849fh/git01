package com.msb.mappers;

import com.msb.base.BaseMapper;
import com.msb.base.BaseQuery;
import com.msb.query.RoleQuery;
import com.msb.vo.Role;

import java.util.List;
import java.util.Map;

public interface RoleMapper extends BaseMapper<Role,Integer> {
    // 查询所有的角色
    List<Map<String,Object>> queryAllRoles(Integer userId);

    Role queryRoleByRoleName(String roleName);
}
