package com.msb.mappers;

import com.msb.base.BaseMapper;
import com.msb.model.TreeModule;
import com.msb.vo.Module;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ModuleMapper extends BaseMapper<Module,Integer> {
    // 授权-查询所有的资源列表，泛型中的类型得和ztree树的格式一致，所以得新建一个模块类
    public List<TreeModule> queryAllModules();




    /*菜单管理-查询所有资源数据列表*/
    public List<Module> querymoduleList();


    /*通过层级与模块名查询资源对象*/
    Module queryModuleByGradeAndModuleName(@Param("grade") Integer grade, @Param("moduleName")String moduleName);
    /*通过层级与url查询资源对象*/
    Module queryModuleByGradeAndUrl(@Param("grade")Integer grade, @Param("url")String url);
    /*通过权限码查询资源对象*/
    Module queryModuleByOptValue(@Param("optValue")String optValue);

    Integer queryModuleByParentId(Integer id);
}