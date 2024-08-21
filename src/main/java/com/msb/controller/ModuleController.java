package com.msb.controller;

import com.msb.Service.ModuleService;
import com.msb.base.BaseController;
import com.msb.base.ResultInfo;
import com.msb.model.TreeModule;
import com.msb.vo.Module;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("module")
public class ModuleController extends BaseController {
    @Resource
    private ModuleService moduleService;


    /**
     * 授权-查询所有的资源
     * @return
     */
    @RequestMapping("queryAllMoudles")
    @ResponseBody
    public List<TreeModule> queryAllMoudles(Integer roleId){
        return moduleService.queryAllModules(roleId);
    }

    /**
     * 菜单-查到所有资源
     * @return
     */
    @RequestMapping("list")
    @ResponseBody    // @ResponseBody注解可以把控制单元返回值自动转换为json格式的数据（json对象）。
    public Map<String,Object> queryModuleList(){
        return moduleService.queryModuleList();
    }

    /**
     * 菜单-添加
     * @param module
     * @return
     */
    @RequestMapping("add")
    @ResponseBody
    public ResultInfo addModule(Module module){
        moduleService.saveModule(module);
        return success("添加成功");
    }

    /**
     * 菜单-修改
     * @param module
     * @return
     */
    @RequestMapping("update")
    @ResponseBody
    public ResultInfo updateModule(Module module){
        moduleService.updateModule(module);
        return success("修改成功");
    }

    /**
     * 菜单-删除
     * @param id
     * @return
     */
    @RequestMapping("delete")
    @ResponseBody
    public ResultInfo deleteModule(Integer id){
        moduleService.deleteModule(id);
        return success("删除成功");
    }




    /**
     * 菜单-添加-小窗口
     * @param grade
     * @param parentId
     * @return
     */
    @RequestMapping("addModulePage")
    public String  toAddModulePage(Integer grade, Integer parentId, HttpServletRequest request){
        // 将数据设置到请求域中
        request.setAttribute("grade",grade);
        request.setAttribute("parentId",parentId);
        return "module/add";
    }

    /**
     * 菜单-更新-小窗口
     * @param id
     * @param model
     * @return
     */
    @RequestMapping("updateModulePage")
    public String  toupdateModulePage(Integer id, Model model){
        // 将要修改的资源对象设置到请求域中
        model.addAttribute("module",moduleService.selectByPrimaryKey(id));
        return "module/update";
    }





    /**
     * 进入到菜单界面
     * @return
     */
    @RequestMapping("index")
    public String index(){
        return "module/module";
    }






}
