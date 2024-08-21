layui.use(['form', 'layer'], function () {
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery;

    /*关闭弹出层-取消按钮(点击取消的作用)*/
    $("#closebtn").click(function (){
        // 当你在iframe页面关闭自身时
        var index = parent.layer.getFrameIndex(window.name); // 先得到当前iframe层的索引
        parent.layer.close(index); // 再执行关闭
    })

    // lay-filter-addOrUpdateCusDevPlan 是确认按钮的属性  代表提交
    form.on('submit(addOrUpdateCusDevPlan)',function (data) {
        var index= top.layer.msg("数据提交中,请稍后...",{icon:16,time:false,shade:0.8});
        var url = ctx+"/cus_dev_plan/save";   //默认为查询
        if($("input[name='id']").val()){       // 如果不为空，则表示更新
            url=ctx+"/cus_dev_plan/update";
        }
        $.post(url,data.field,function (res) {
            if(res.code==200){
                top.layer.msg("操作成功");      // top ： 置于顶部
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