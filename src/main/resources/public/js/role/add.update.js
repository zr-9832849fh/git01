layui.use(['form', 'layer'], function () {
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery;



    formSelects.config('selectId',{
        type:"post",
        searchUrl:ctx + "/role/queryAllRoles",    // 请求路径
        // 自定义返回数据中name的key，默认 name
        keyName: 'roleName',
        // 自定义返回数据中value的key，默认value
        keyVal: 'id'
    },true);



    form.on('submit(addOrUpdateRole)',function (data) {
        var index= top.layer.msg("数据提交中,请稍后...",{icon:16,time:false,shade:0.8});
        var url = ctx+"/role/save";     // 后台添加方法路径
        if($("input[name='id']").val()){
            url=ctx+"/role/update";    // 后台更新方法路径
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