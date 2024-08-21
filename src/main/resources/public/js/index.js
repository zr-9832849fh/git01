layui.use(['form','jquery','jquery_cookie'], function () {
    var form = layui.form,
        layer = layui.layer,
        $ = layui.jquery,
        $ = layui.jquery_cookie($);
    // 对表单进行登录操作（lay-filter）
    form.on('submit(login)', function (data) {    //submit(*)  * 代表<登录>左边的属性：lay-filter
        data = data.field;   //data.field代表当前容器的全部表单字段，键值对形式：{name，value}
        if ( data.username =="undefined" || data.username =="" || data.username.trim()=="") {
            layer.msg('用户名不能为空');
            return false;
        }
        if ( data.password =="undefined" || data.password =="" || data.password.trim()=="")  {
            layer.msg('密码不能为空');
            return false;
        }
        $.ajax({        // 点击登录向后台请求
            type:"post",
            url:ctx+"/user/login",   // 请求路径
            data:{
                userName:data.username,    /*data.field  username 获得输入的用户名 、 密码*/
                userPwd:data.password
            },
            dataType:"json",
            success:function (data) {   //回调函数 ： data
                if(data.code==200){       // 是否登录成功的回调函数
                    layer.msg('登录成功', function () {    /*这里做两件事情：1.用户登录成功之后要跳转至登录页面
                                                                       2.将用户信息保存在cookie中*/
                        var result =data.result;
                        $.cookie("userIdStr",result.userIdStr);
                        $.cookie("userName",result.userName);
                        $.cookie("trueName",result.trueName);
                        // 如果点击记住我 设置cookie 过期时间7天
                        if($("input[type='checkbox']").is(':checked')){
                            // 写入cookie 7天
                            $.cookie("userIdStr",result.userIdStr, { expires: 7 });
                            $.cookie("userName",result.userName, { expires: 7 });
                            $.cookie("trueName",result.trueName, { expires: 7 });
                        }
                        window.location.href=ctx+"/main";  // 登录成功之后，跳转到首页
                    });
                }else{
                    layer.msg(data.msg);
                }
            }
        });
        return false;
    });
});