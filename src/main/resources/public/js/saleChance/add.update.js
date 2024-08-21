layui.use(['form', 'layer'], function () {
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery;

    /*关闭弹出层*/
    $("#closebtn").click(function (){
        // 当你在iframe页面关闭自身时
        var index = parent.layer.getFrameIndex(window.name); // 先得到当前iframe层的索引
        parent.layer.close(index); // 再执行关闭
    })


    /**
     * 加人指派的下拉框（后台查询的的销售人员）
     */
    $.post(ctx+"/user/queryAllSales",function (res) {     // queryAllSales 后台的销售人员的值装入下拉框中
        for(var i=0;i<res.length;i++){
            if($("input[name='man']").val() == res[i].id){
                $("#assignMan").append("<option value=\""+res[i].id+"\"  selected='selected' >"+res[i].uname+"</option>");
            }else{
                $("#assignMan").append("<option value=\""+res[i].id+"\"   >"+res[i].uname+"</option>");
            }
        }
        // 重新渲染下拉框内容
        layui.form.render("select");
    });

    /**
     *前台营销机会-添加、修改
     */
    form.on('submit(addOrUpdateSaleChance)',function (data) {   // 是按钮lay-filter的值
        var index= top.layer.msg("数据提交中,请稍后...",{icon:16,time:false,shade:0.8});
        // icon 图标；time:false不关闭 ；shade:0.8设置遮罩透明度
        var url = ctx+"/sale_chance/save";
        if($("input[name='id']").val()){
            url=ctx+"/sale_chance/add";    // 这里就是往后台请求的地方(路径)
        }

        // 通过营销机会的ID来判断当前需要执行的是添加操作还是修改操作
        // 如果营销机会ID为空，则表示执行添加操作；ID不为空，则执行修改操作
        // 通过获取隐藏域中的ID
        var saleChanceId = $("[name='id']").val();
        // 判断ID是否为空
        if (saleChanceId !=null && saleChanceId !=''){
            // 更新操作
            url = ctx + "/sate_chance/update";
        }
        $.post(url,data.field,function (res) {   // 发送ajax请求，为了拿到数据之后关闭页面
            if(res.code==200){
                top.layer.msg("操作成功",{icon: 6});
                // 关闭加载层
                top.layer.close(index);
                layer.closeAll("iframe");
                // 刷新父页面
                parent.location.reload();
            }else{
                layer.msg(res.msg,{icon: 5});  // icon:5 代表哭脸
            }
        });
        return false;
    });

});