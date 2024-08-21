package com.msb.controller;

import com.msb.Service.UserRoleService;
import com.msb.base.BaseController;
import com.msb.vo.UserRole;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;

@Controller

public class UserRoleController extends BaseController {
    @Resource
    private UserRoleService userRoleService;
}
