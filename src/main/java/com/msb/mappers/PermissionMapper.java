package com.msb.mappers;

import com.msb.base.BaseMapper;
import com.msb.vo.Permission;

import java.util.List;

public interface PermissionMapper extends BaseMapper<Permission,Integer> {

    /*根据roleId查询资源数量，*/
    Integer countPermissionByRoleId(Integer roleId);
    /*根据roleId删除当前的权限*/
    void deletePermissionByRoleId(Integer roleId);
    /*查询角色拥有的所有的资源ID的集合*/
    List<Integer> queryRoleHasAllMids(Integer roleId);



    /*通过用户ID查询对应的资源列表（资源权限码）*/
    List<String> queryUserHasRoleHasPermissionByUserId(Integer userId);
    /*通过资源ID查询权限记录*/
    Integer countPermissionByModuleId(Integer id);
    /*通过资源ID删除权限记录*/
    void deletePermissionByModuleId(Integer id);
}