package com.msb.query;

import com.msb.base.BaseQuery;

public class RoleQuery extends BaseQuery {
    private String roleName;   // 角色名

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}
