package com.msb.mappers;

import com.msb.base.BaseMapper;
import com.msb.vo.UserRole;

public interface UserRoleMapper extends BaseMapper<UserRole,Integer> {

    /*根据当前userId查询到所有的角色*/
    Integer countUserRoleUserId(Integer userId);

    /* 将当前userId查到的所有角色记录全部删除 */
    Integer deleteUserRoleByUserId(Integer userId);
}