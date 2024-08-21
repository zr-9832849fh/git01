package com.msb.controller;

import com.msb.Service.RoleService;
import com.msb.base.BaseController;
import com.msb.base.ResultInfo;
import com.msb.query.RoleQuery;
import com.msb.vo.Role;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("role")
public class RoleController extends BaseController {
    @Resource
    private RoleService roleService;

    /**
     * 查询当前userId拥有的角色【用于用户管理时-添加add_update.js】
     * @return
     */
    @RequestMapping("queryAllRoles")
    @ResponseBody
    public List<Map<String,Object>> queryAllRole(Integer userId){
        return roleService.queryAllRole(userId);
    }

    /**
     * 角色管理模块-多条件查询（参数：角色名）
     */
    @RequestMapping("list")
    @ResponseBody
    public Map<String,Object> selectSole(RoleQuery roleQuery){   // 继承basequery：分页
        return roleService.queryByParamsForTable(roleQuery);
    }

    /**
     * 角色管理模块-添加
     * @param role
     * @return
     */
    @RequestMapping("save")
    @ResponseBody
    public ResultInfo saveSoleMange(Role role){
        roleService.saveSoleMange(role);
        return success("角色管理模块-添加成功"); // success返回的是ResultInfo对象
    }

    /**
     * 角色管理模块-更新
     * @param role
     * @return
     */
    @RequestMapping("update")
    @ResponseBody
    public ResultInfo updateSoleMange(Role role){
        roleService.saveSoleMange(role);
        return success("角色管理模块-修改成功"); // success返回的是ResultInfo对象
    }

    /**
     * 角色管理模块-删除
     * @param roleId
     * @return
     */
    @RequestMapping("delete")
    @ResponseBody
    public ResultInfo deleteSoleMange(Integer roleId){
        roleService.deleteSoleMange(roleId);
        return success("删除成功");
    }





    /**
     * 进入到角色管理模块
     * @return
     */
    @RequestMapping("index")
    public String soleMange(){
        return "role/role";
    }

    /**
     * 角色管理-小窗口
     * @return
     */
    @RequestMapping("addOrUpdateRolePage")
    public String addsoleMange(){
        return "role/add_update";
    }




    /**
     * 角色授权
     */
    @PostMapping("addGrant")
    @ResponseBody
    public ResultInfo addGrant(Integer roleId,Integer[] mIds){
        roleService.addGrant(roleId,mIds);
        return success("角色授权成功");
    }

    /**
     * 进入到授权界面
     * @param roleId
     * @return
     */
    @RequestMapping("toAddGrantPage")
    public String grant(Integer roleId, HttpServletRequest request){        // 从前台将roleId拿过来，用于对某个用户授权
        request.setAttribute("roleId",roleId);
        return "role/grant";
    }
}
