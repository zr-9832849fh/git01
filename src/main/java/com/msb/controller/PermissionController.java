package com.msb.controller;

import com.msb.Service.PermissionService;
import com.msb.base.BaseController;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;

@Controller
public class PermissionController extends BaseController {
    @Resource
    private PermissionService permissionService;
}
