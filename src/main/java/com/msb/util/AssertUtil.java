package com.msb.util;


import com.msb.exceptions.ParamsException;

/*判断方法相关*/
public class AssertUtil {


    public  static void isTrue(Boolean flag,String msg){
        if(flag){
            throw new ParamsException(msg);
        }
    }

}
