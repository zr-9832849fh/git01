package com.msb.mappers;

import com.msb.base.BaseMapper;
import com.msb.vo.User;

import java.util.List;
import java.util.Map;

public interface UserMapper extends BaseMapper<User,Integer> {
   // 通过用户名查询用户对象
   public  User queryUserByName(String userName);



   /*查询所有的销售人员（第二板块）*/
   List<Map<String,Object>> queryAllSales();
}