package com.msb.Service;

import com.msb.base.BaseService;
import com.msb.mappers.ModuleMapper;
import com.msb.mappers.PermissionMapper;
import com.msb.model.TreeModule;
import com.msb.util.AssertUtil;
import com.msb.vo.Module;
import com.msb.vo.Permission;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.transaction.Transaction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ModuleService extends BaseService<Module,Integer> {
    @Resource
    private ModuleMapper moduleMapper;
    @Resource
    private PermissionMapper permissionMapper;
    /**
     * 授权-查询所有的资源
     * @return
     */
    public List<TreeModule> queryAllModules(Integer roleId){
        List<TreeModule> treeDtos = moduleMapper.queryAllModules();
        // 查询角色已分配的菜单id
        List<Integer> mids =permissionMapper.queryRoleHasAllMids(roleId);
        if(null !=mids && mids.size()>0){
            treeDtos.forEach(treeModule->{
                if(mids.contains(treeModule.getId())){
                    // 角色已分配该菜单
                    treeModule.setChecked(true);
                }
            });
        }
        return treeDtos;
    }

    /**
     * 查询资源数据 按照map集合的方式返回前端
     * @return
     */
    public Map<String,Object> queryModuleList(){
        Map<String,Object> map = new HashMap<>();

        // 调用mapper层方法
        List<Module> moduleList = moduleMapper.querymoduleList();
        map.put("code",0);
        map.put("msg","");
        map.put("count",moduleList.size());
        map.put("data",moduleList);

        return map;
    }

    /**
     * 1.参数校验
     *     模块名称 moduleName
     *         非空 同一层级 模块名唯一
     *     地址 url
     *        二级菜单(grade=1) 非空且同一层级下不可重复与
     *     父级菜单 parentId
     *        一级菜单  （目录 grade=0）  null
     *        二级|三级菜单 （菜单|按钮 grade=1或2）  parentId 非空 父级菜单记录必须存在
     *     层级  grade
     *       非空  0|1|2
     *     权限码  optValue
     *        非空  不可重复
     * 2.参数默认值设置
     *      是否有效：isValid  1
     *      createDate  updateDate
     * 3.执行添加 判断结果
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void saveModule(Module module){
        // 1.参数校验
        // 层级 grade 非空 0|1|2
        Integer grade =module.getGrade();
        AssertUtil.isTrue(null==grade || !(grade==0||grade==1||grade==2),"菜单层级不合法!");

        // 模块名称 moduleName  非空
        AssertUtil.isTrue(StringUtils.isBlank(module.getModuleName()),"模块名称不能为空");
        // 模块名称 moduleName 同一层级下模块名称唯一
        Module module1 = moduleMapper.queryModuleByGradeAndModuleName(grade,module.getModuleName());
        AssertUtil.isTrue(null !=module1,"该层级下菜单名已存在!");

        // 如果是二级菜单（grade=1）
        if(grade==1){
            // 地址 url  耳机菜单（grade=1） 非空
            AssertUtil.isTrue(StringUtils.isBlank(module.getUrl()),"url不能为空");
            // 地址 url   二级菜单 （grade=1），且同意一层级下不可重复
            module1=moduleMapper.queryModuleByGradeAndUrl(grade,module.getUrl());
            AssertUtil.isTrue(null !=module1,"二级菜单下url不可重复!");
        }

        // 父级菜单 parentId   一级菜单（目录 grade=0）  -1
        if(grade==0){
            module.setParentId(-1);
        }

        if(grade!=0){
            // 父级菜单必须存在（将父级菜单的ID作为主键，查询资源记录）
            AssertUtil.isTrue(null==module.getParentId() ||null== selectByPrimaryKey(module.getParentId()),"父级菜单不能为空");
        }
        // 权限码  optvalue  非空
        AssertUtil.isTrue(StringUtils.isBlank(module.getOptValue()),"请输入菜单权限码!");
        // 权限码 optvalue  不可重复
        module1 = moduleMapper.queryModuleByOptValue(module.getOptValue());
        AssertUtil.isTrue(null !=module1,"权限码重复!");


        // 2.设置参数默认值
        // 是否有效 isvalid  1
        module.setIsValid((byte)1);
        module.setCreateDate(new Date());
        module.setUpdateDate(new Date());
        AssertUtil.isTrue(insertSelective(module)<1,"菜单添加失败!");
    }


    /**
     * 修改资源
     *  1.参数校验
     *      id  非空，数据存在
     *      层级grade
     *          非空 0|1|2
     *      模块名称 moduleName
     *          非空，同一层级下模块名称唯一  （不包含当前修改记录本身）
     *      地址 url
     *          二级菜单（grade=1），非空且同一层级下不可重复（不包含当前修改记录本身）
     *      权限码 optvalue
     *          非空，不可重复（不包含当前修改记录本身）
     *  2.设置参数的默认值
     *      修改时间uodateDate  系统当前时间
     *  3.执行更新操作，判断受影响行数
     * @param module
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateModule(Module module){
        // 1.参数校验 id 非空，数据存在
        AssertUtil.isTrue(null == module.getId(),"待更新记录不存在!");
        // 通过id查询资源对象
        Module temp =moduleMapper.selectByPrimaryKey(module.getId());
        AssertUtil.isTrue(null ==temp,"待更新记录不存在!");

        AssertUtil.isTrue(StringUtils.isBlank(module.getModuleName()),"请输入菜单名!");  // 为什么要判断这个
        // 层级 grade  非空0.1.2
        Integer grade =module.getGrade();
        AssertUtil.isTrue(null==grade || !(grade==0||grade==1||grade==2),"菜单层级非法!");
        // 模块名称 moduleName  非空，同一层级下模块名称唯一  （不包含当前修改记录本身）
        temp  = moduleMapper.queryModuleByGradeAndModuleName(grade,module.getModuleName());
        AssertUtil.isTrue(null !=temp && !(temp.getId().equals(module.getId())),"该层级下菜单名已存在!");


        // 地址  url  二级菜单（grade=1），非空且同一层级下不可重复（不包含当前修改记录本身）
        if(grade==1){
            AssertUtil.isTrue(StringUtils.isBlank(module.getUrl()),"菜单url不能为空!");
            // 通过层级与菜单url查询资源对象
            temp=moduleMapper.queryModuleByGradeAndUrl(grade,module.getUrl());
            // 判断是否存在
            AssertUtil.isTrue(null !=temp && !(temp.getId().equals(module.getId())),"二级菜单下url不可重复!");
        }

        // 二级 三级菜单 必须指定上级菜单id
        if(grade!=0){
            AssertUtil.isTrue(null==module.getParentId() ||null== selectByPrimaryKey(module.getParentId()),"请指定上级菜单!");
        }
        // 权限码 optvalue  非空，不可重复（不包含当前修改记录本身）
        AssertUtil.isTrue(StringUtils.isBlank(module.getOptValue()),"权限码不能为空!");
        // 通过权限码查询资源对象
        temp = moduleMapper.queryModuleByOptValue(module.getOptValue());
        // 判断是否为空
        AssertUtil.isTrue(null !=temp && !(temp.getId().equals(module.getId())),"权限码重复!");
        // 2.设置参数默认值
        module.setUpdateDate(new Date());
        // 3.执行更新方法
        AssertUtil.isTrue(updateByPrimaryKeySelective(module)<1,"菜单记录更新失败!");
    }

    /**
     *  删除资源
     *      1.判断删除的记录是否存在
     *      2.如果当前资源存在自己录，则不可删除
     *      3.删除资源时，将对应的权限表的记录也删除（判断权限表中是否存在关联数据，如果存在，则删除）
     *      4.执行删除（更新）操作，判断受影响的行数
     * @param id
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteModule(Integer id) {
        // 1.判断id是否为空
        AssertUtil.isTrue(null == id,"待删除记录不存在！");
        // 通过id查询资源对象
        Module temp = moduleMapper.selectByPrimaryKey(id);
        // 判断资源对象是否为空
        AssertUtil.isTrue(null == temp,"待删除记录不存在哦");

        // 如果当前资源存在自己录（将id当作父id查询资源记录）
        Integer count = moduleMapper.queryModuleByParentId(id);
        // 如果存在子记录，则不可删除
        AssertUtil.isTrue(count>0,"该资源存在自己录，不可删除");

        // 通过资源id查询权限表中是否存在数据
        count = permissionMapper.countPermissionByModuleId(id);
        // 判断是否存在，存在则删除
        if (count>0){
            // 删除指定资源ID的权限记录
            permissionMapper.deletePermissionByModuleId(id);
        }

        // 2.参数设置（设置记录无效）
        temp.setIsValid((byte) 0);
        temp.setUpdateDate(new Date());

        // 执行更新
        AssertUtil.isTrue(moduleMapper.updateByPrimaryKeySelective(temp) < 1,"删除资源失败");
    }
}
