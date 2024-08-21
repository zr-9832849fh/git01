package com.msb.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.msb.base.BaseQuery;
import com.msb.base.BaseService;
import com.msb.mappers.ModuleMapper;
import com.msb.mappers.PermissionMapper;
import com.msb.mappers.RoleMapper;
import com.msb.query.RoleQuery;
import com.msb.util.AssertUtil;
import com.msb.vo.Permission;
import com.msb.vo.Role;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.util.*;

@Service
public class RoleService extends BaseService<Role,Integer> {
    @Resource
    private RoleMapper roleMapper;
    @Resource
    private PermissionMapper permissionMapper;
    @Resource
    private ModuleMapper moduleMapper;

    /**
     * 查询所有的角色
     * @return   Map<name,value>
     */
    public List<Map<String,Object>> queryAllRole(Integer userId){
        return roleMapper.queryAllRoles(userId);
    }

   // 多条件查询service已经写了就不用再写了

    /**
     * 角色管理-添加
     * 1.参数校验非空
     *     2.设置参数默认值
     *     3.执行增加方法
     * @param role
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void saveSoleMange(Role role){
        // 1.参数校验非空
        AssertUtil.isTrue(StringUtils.isEmpty(role.getRoleName()),"角色名不能为空");
        // 2.设置参数默认值
        role.setIsValid(1);
        role.setCreateDate(new Date());
        role.setUpdateDate(new Date());
        // 3.执行添加方法
        AssertUtil.isTrue(roleMapper.insertSelective(role) != 1,"添加失败");
    }

    /**
     * 角色管理-修改
     *      1.参数校验非空（角色名唯一、id是否存在）
     *     2.设置参数默认值
     *     3.执行增加方法
     * @param role
     * @param roleQuery
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateSoleMange(Role role,RoleQuery roleQuery){
        // 1.判断roleid是否存在
        AssertUtil.isTrue(StringUtils.isEmpty(role.getId()) || roleMapper.selectByPrimaryKey(role.getId()) ==null,"没有这条角色，更新失败");
        AssertUtil.isTrue(StringUtils.isEmpty(role.getRoleName()),"角色名不能为空");
         // 判断角色唯一（通过角色查对象）
        Role temp = roleMapper.queryRoleByRoleName(role.getRoleName());
        AssertUtil.isTrue(temp !=null && !(temp.getId().equals(role.getId())),"该角色已存在");
        // 2.设置默认参数
        role.setUpdateDate(new Date());
        // 3.执行修改方法
        AssertUtil.isTrue(roleMapper.updateByPrimaryKeySelective(role) !=1,"删除失败");
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteSoleMange(Integer roleId){
        Role temp = roleMapper.selectByPrimaryKey(roleId);
        AssertUtil.isTrue(null == roleId || null == temp,"待删除记录不存在");
        temp.setIsValid(0);
        AssertUtil.isTrue(roleMapper.updateByPrimaryKeySelective(temp) !=1,"角色记录删除失败");
    }

    /**
     * 角色管理-授权
     *      先将已有的权限记录删除，再将需要设置的权限记录添加
     *          1.通过角色id查询对应的权限记录
     *          2.如果权限记录存在，则删除对应的角色拥有的权限记录
     *          3.如果有权限记录，则添加权限记录（批量添加）
     * @param roleId
     * @param mIds
     */
    public void addGrant(Integer roleId, Integer[] mIds) {
        // 1.通过角色ID查询对应的权限记录
        Integer count = permissionMapper.countPermissionByRoleId(roleId);       // 后台删除
        // 如果权限记录存在，就删除当前角色所有的权限
        if (count>0){
            permissionMapper.deletePermissionByRoleId(roleId);
        }
        // 2.如果前台传过来有权限记录，则添加权限记录[如果没有就直接添加权限]  批量添加
        if (mIds != null && mIds.length > 0){                                    // 前台传过来直接添加
            // 定义permission集合
            List<Permission> permissionsList = new ArrayList<>();

            // 遍历资源ID数据
            for (Integer mId: mIds) {
                Permission permission = new Permission();
                permission.setModuleId(mId);
                permission.setRoleId(roleId);
                permission.setAclValue(moduleMapper.selectByPrimaryKey(mId).getOptValue());
                permission.setCreateDate(new Date());
                permission.setUpdateDate(new Date());

                permissionsList.add(permission);
            }
            // 添加授权
            AssertUtil.isTrue(permissionMapper.insertBatch(permissionsList) != permissionsList.size(),"授权失败");
        }

    }
}
