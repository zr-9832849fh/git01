package com.msb.Service;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.msb.base.BaseService;
import com.msb.mappers.UserMapper;
import com.msb.mappers.UserRoleMapper;
import com.msb.model.UserModel;
import com.msb.query.UserQuery;
import com.msb.util.AssertUtil;
import com.msb.util.Md5Util;
import com.msb.util.PhoneUtil;
import com.msb.util.UserIDBase64;
import com.msb.vo.User;
import com.msb.vo.UserRole;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.asm.IRelationship;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class UserService extends BaseService<User,Integer> {
    @Resource
    private UserMapper userMapper;
    @Resource
    private UserRoleMapper userRoleMapper;

    /**
     * 1登录模块-登录
     * @param userName
     * @param userPwd
     * @return
     */
    public UserModel userLogin(String userName,String userPwd){
        // 1.判断用户名密码是否为空AsserUtil.istrue（Boolean flag,String msg）
        checkLoginParams(userName,userPwd);
        // 2.调用Usermapper层方法（返回user对象）
        User user = userMapper.queryUserByName(userName);
        // 3.判断user是否为空
        AssertUtil.isTrue(user == null,"用户不存在或已经注销");  // user == null 如果true 则返回msg
        // 4.对象不为空，则校验密码和数据库中是否一致，密码不正确方法结束
        checkLoginPwd(userPwd,user.getUserPwd());

        // 5..这里将保存的数据（id、username、truename）返回到前端中
        return buildUserInfo(user);
    }

    private UserModel buildUserInfo(User user) {
        UserModel userModel = new UserModel();    // 有加密后id、用户名和真实名
//        userModel.setUserId(user.getId());      这里因为传进入的getId在浏览器要显示加密后的样子。所以不能用int
        userModel.setUserIdStr(UserIDBase64.encoderUserID(user.getId()));  /*加密*/
        userModel.setUserName(user.getUserName());
        userModel.setUserName(user.getTrueName());
        return userModel;
    }

    /**
     * 2登录模块-修改密码（四个参数 用户id、原始密码、新密码、确认密码） ： 增删改都要用事务
     * @param userId
     * @param oldPwd          更新的思想就是/；将原先的密码从数据库拿出来，然后用新密码放入
     * @param newPwd
     * @param repeatPwd
     */
    public void updataPassword(Integer userId,String oldPwd,String newPwd,String repeatPwd){
        // 拿userId去获取对象
        User user = userMapper.selectByPrimaryKey(userId);
        // 判断user是否为空
        AssertUtil.isTrue(null == user,"当前用户不存在");
        // 判断密码的情况
        checkPasswordParams(user,oldPwd,newPwd,repeatPwd);
        // 将新密码放入user对象中
        user.setUserPwd(newPwd);
        // 执行更新方法
        AssertUtil.isTrue(userMapper.updateByPrimaryKeySelective(user) < 1,"更新失败");
    }


    private void checkLoginPwd(String userPwd, String uPwd) {
        // 首先加密前台传过来的密码

        AssertUtil.isTrue(!userPwd.equals(uPwd),"密码错误");
    }

    private void checkLoginParams(String userName, String userPwd) {
        // 判断用户名
        AssertUtil.isTrue(StringUtils.isBlank(userName),"用户名不能为空");
        // 判断密码
        AssertUtil.isTrue(StringUtils.isBlank(userPwd),"密码不能为空");
    }




    private void checkPasswordParams(User user, String oldPwd, String newPwd, String repeatPwd) {
        // 旧密码非空  旧密码是否和数据库中密码一致
        AssertUtil.isTrue(oldPwd == null && oldPwd == "","旧密码不能为空");
        AssertUtil.isTrue(oldPwd.equals(user.getUserPwd()),"旧密码和数据库中密码不能相同噢");
        // 新密码不能和旧密码一致  ，不能非空
        AssertUtil.isTrue(newPwd == null && newPwd == "","新密码不能为空");
        AssertUtil.isTrue(oldPwd.equals(newPwd),"旧密码和新密码不能一致");
        // 新密码不能确认密码一致，不能为空
        AssertUtil.isTrue(repeatPwd == null,"确认密码不能为空");
        AssertUtil.isTrue(repeatPwd.equals(newPwd),"确认密码和新密码不能一致");

    }

    /**
     * 查询所有的销售人员
     * @return
     */
    public List<Map<String,Object>> queryAllSales(){
        return userMapper.queryAllSales();
    }








/*------------------------------------------------------------------------------------------------------*/





    /**
     * 用户管理-添加操作
     *      1.判断非空：用户名（唯一）、真实姓名、邮箱、手机号和手机号格式
     *      2.设置参数默认值（）
     *              isValid 1  有效参数
     *              creteDate  创建时间
     *              updateDate 修改时间
     *              userPwd 123456 -> md5加密
     *      3.执行添加操作(没有id)
     *
     *      4. 将角色进行处理（用户角色关联）
     * @param user
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void add_UserManage(User user){
        // 1.判断非空
        checkParams(user);
        // 2. 设置参数默认值
        user.setIsValid(1);
        user.setCreateDate(new Date());
        user.setUpdateDate(new Date());
        user.setUserPwd("123456");
        // 加密
        Md5Util.encode(user.getUserPwd());
        // 3.进行添加操作
        AssertUtil.isTrue(userMapper.insertSelective(user) !=1,"执行添加操作失败");

        // 4.用户角色关联（用户名和角色名【前台传过来的数组】）
        relationUserRole(user.getId(),user.getRoleIds());
    }

    /**
     * 用户管理-更新操作（为什么要再次判定非空，因为在保证满足的条件下才能更改成功）
     *         1.判断非空：id、用户名（唯一）、email、手机号（格式合法）
     *         2.设置默认参数值(updateDate)
     *         3.执行更新，判断结果
     * @param user
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void update_UserMange(User user){
        // 1.通过id查到当前用户对象,判断数据库中是否存在
        User temp = userMapper.selectByPrimaryKey(user.getId());
        AssertUtil.isTrue(temp == null,"待更新记录不存在");
        // 参数校验
        checkParams(user.getUserName(),user.getEmail(),user.getPhone(),user.getId());
        // 2.设置默认参数值(updateDate)
        temp.setUpdateDate(new Date());
        // 3.执行更新，判断结果
        AssertUtil.isTrue(userMapper.updateByPrimaryKeySelective(user) != 1,"用户管理-更新失败");

        // 4.用户角色关联（用户名和角色名【前台传过来的数组】）  **更新**
        relationUserRole(user.getId(),user.getRoleIds());
    }

    /**
     * 批量删除[加上删除角色]
     * @param ids
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void delete_UserMange(Integer[] ids){    // 用户管理中有多个用户
        // 判断id是否为空
        AssertUtil.isTrue(ids == null,"id不能为空");
        // 调用mapper层方法，判断受影响行数 与  需要删除行数作比较
        AssertUtil.isTrue(!userMapper.deleteBatch(ids).equals(ids.length),"未删除成功,请检查");

        // 遍历ids
        for (Integer userId: ids) {
            // 拿到每一个userId对象的  角色数量
            Integer count = userRoleMapper.countUserRoleUserId(userId);
            // 判断如果count > 0 ,那就进行删除，并且判断数据库中查到的角色数量  与 count是否一致
            if (count > 0){
                AssertUtil.isTrue(userRoleMapper.deleteUserRoleByUserId(userId) != count,"角色未删除成功，请检查");
            }
        }
    }




    /*用户角色关联*/
    private void relationUserRole(Integer userId, String roleIds) {
        // 1.利用当前的用户ID查询当前的角色记录的数量（就是看当前用户有几个角色）
        Integer count = userRoleMapper.countUserRoleUserId(userId);   // 因为要去用户-角色表查才能查到数量
        // 判断count查到的数据量
        if (count > 0){    // 【添加本来角色数量为0，就不会走这个】
            // 代表该用户有角色:[如果有角色，就应该删除这些角色]
            AssertUtil.isTrue(userRoleMapper.deleteUserRoleByUserId(userId) != count,"用户角色关联失败");
        }
        // 2.利用传过来的roleIds【因为之前做了查询所有的 “角色”】 ，判断角色ID是否存在，如果存在，则添加该用户的对应角色
        // 注：如果在数据库中添加了对应的角色，那么在前台也是会修改的
        if (!StringUtils.isBlank(roleIds)){   // roleIds不为空才往下走
            // 如果roleId存在
            // 就可以将所有的用户角色数据设置到集合中，再执行批量增加
            List<UserRole> userRoleList = new ArrayList<>();
            // 将roleId字符串转化为数组
            String[] roleIdArray = roleIds.split(",");  // [1,2,3,4,5]
            // 遍历数组，得到的用户角色对象，放入集合中

            for (String roleId : roleIdArray) {
                UserRole userRole = new UserRole();
                userRole.setRoleId(Integer.parseInt(roleId)); // 将字符串的roleId依次放入userRole中
                userRole.setUserId(userId);
                userRole.setCreateDate(new Date());
                userRole.setUpdateDate(new Date());
                // 设置到集合中
                userRoleList.add(userRole);
            }
            // 批量添加用户角色记录
            AssertUtil.isTrue(userRoleMapper.insertBatch(userRoleList) != userRoleList.size(),"用户角色关联失败");


        }

    }
















    // 增加-参数校验
    private void checkParams(User user) {
        AssertUtil.isTrue(StringUtils.isBlank(user.getUserName()),"客户名异常，请输入客户名!");
        AssertUtil.isTrue(StringUtils.isBlank(user.getEmail()),"邮箱异常，请输入邮箱!");
        AssertUtil.isTrue(StringUtils.isBlank(user.getPhone()),"手机号异常，请输入手机号!");
        AssertUtil.isTrue(!PhoneUtil.isMobile(user.getPhone()),"手机号格式不合法");
    }
    // 修改-参数校验
    private void checkParams(String userName, String email, String phone, Integer userId) {
        // 用户名除了得判断 非空 还要判断和数据库其他用户名是否一致
        User temp = userMapper.queryUserByName(userName);  // 通过用户名查询用户对象
        // 如果数据库中ID（要修改的id） 和 当前用户id 一致，才可以修改  取反不能修改
        AssertUtil.isTrue(null != temp && !(temp.getId().equals(userId)), "该用户已存在");
        AssertUtil.isTrue(StringUtils.isBlank(userName),"客户名异常，请输入客户名!");
        AssertUtil.isTrue(StringUtils.isBlank(email),"邮箱异常，请输入邮箱!");
        AssertUtil.isTrue(StringUtils.isBlank(phone),"手机号异常，请输入手机号!");
        AssertUtil.isTrue(!PhoneUtil.isMobile(phone),"手机号格式不合法");
    }
}
