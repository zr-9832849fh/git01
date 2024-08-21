layui.use(['form', 'layer','formSelects'], function () {   // 加formselects模块
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery;

    // 引入formselects模块
    var  formSelects = layui.formSelects;




    /**
     * 加载角色下拉框
     */
    // 接收隐藏域的userId
    var userId=$("input[name='id']").val();
    formSelects.config('selectId',{
        type:"post",      // /role/queryAllRoles： 在roleconreoller层中获取所有的角色
        searchUrl:ctx+"/role/queryAllRoles?userId="+userId,     // 上面有隐藏域存用户userId
        //自定义返回数据中name的key, 默认 name
        keyName: 'rolename',            // 这个name要和后台sql返回的字段名一致
        //自定义返回数据中value的key, 默认 value
        keyVal: 'id'                // // 这个name要和后台sql返回的字段名一致
    },true);


    form.on('submit(addOrUpdateUser)',function (data) {
        var index= top.layer.msg("数据提交中,请稍后...",{icon:16,time:false,shade:0.8});
        var url = ctx+"/user/save";
        if($("input[name='id']").val()){
            url=ctx+"/user/update";
        }
        $.post(url,data.field,function (res) {
            if(res.code==200){
                top.layer.msg("操作成功");
                top.layer.close(index);
                layer.closeAll("iframe");
                // 刷新父页面
                parent.location.reload();
            }else{
                layer.msg(res.msg);
            }
        });
        return false;
    });


});