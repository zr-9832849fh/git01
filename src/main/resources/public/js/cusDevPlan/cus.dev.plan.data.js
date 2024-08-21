/*计划项数据维护的那个小界面*/
layui.use(['table','layer'],function(){
    var layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        table = layui.table;
    //计划项数据展示
    var  tableIns = table.render({
        elem: '#cusDevPlanList',
        /*访问数据的url（后台的数据接口）*/
        url : ctx+'/cus_dev_plan/list?sid='+$("input[name='id']").val(),    /*利用隐藏域传入后台拿值*/
        cellMinWidth : 95,
        page : true,
        height : "full-125",
        limits : [10,15,20,25],
        limit : 10,
        toolbar: "#toolbarDemo",         /*头部工具栏*/
        id : "cusDevPlanListTable",
        cols : [[
            {type: "checkbox", fixed:"center"},
            {field: "id", title:'编号',fixed:"true"},
            {field: 'planItem', title: '计划项',align:"center"},
            {field: 'exeAffect', title: '执行效果',align:"center"},
            {field: 'planDate', title: '执行时间',align:"center"},
            {field: 'createDate', title: '创建时间',align:"center"},
            {field: 'updateDate', title: '更新时间',align:"center"},
            {title: '操作',fixed:"right",align:"center", minWidth:150,templet:"#cusDevPlanListBar"}
        ]]
    });

    /**
     * 这里是客户开发计划中：“开发”中的添加计划项、开发成功、开发失败
     */
    table.on("toolbar(cusDevPlans)",function (obj) {
        switch (obj.event) {
            case "add" :  // 添加计划项
                openAddOrUpdateCusDevPlanDialog();
                break;
            case "success":   // 开发成功
                updateSaleChanceDevResult($("input[name='id']").val(),2);
                break;
            case "failed":   // 开发失败
                updateSaleChanceDevResult($("input[name='id']").val(),3);
                break;
        }
    });

    /**
     * 行工具栏监听（编辑和删除）
     */
    table.on("tool(cusDevPlans)",function (obj) {
        var layEvent = obj.event;
        if(layEvent === "edit"){
            openAddOrUpdateCusDevPlanDialog(obj.data.id);
        }else if(layEvent === "del"){
            layer.confirm("确认删除当前记录?",{icon: 3, title: "客户开发计划管理"},function (index) {
                $.post(ctx+"/cus_dev_plan/delete",{id:obj.data.id},function (data) {
                    if(data.code==200){
                        layer.msg("删除成功");
                        tableIns.reload();
                    }else{
                        layer.msg(data.msg);
                    }
                })
            })
        }
    });

    function openAddOrUpdateCusDevPlanDialog(id) {
        var title="计划项管理管理-添加计划项";
        var url=ctx+"/cus_dev_plan/addOrUpdateCusDevPlanPage?sid="+$("input[name='id']").val();   // 这里将sid传到第三个小界面中做添加处理
        if(id){
            title="计划项管理管理-更新计划项";
            url=url+"&id="+id;
        }
        layui.layer.open({
            title:title,
            type:2,
            area:["700px","500px"],
            maxmin:true,
            content:url
        })
    }



    function updateSaleChanceDevResult(sid,devResult) {
        layer.confirm("确认更新机会数据状态?",{icon: 3, title: "客户开发计划管理"},function (index) {
            $.post(ctx+"/sale_chance/updateSaleChanceDevResult",{
                id:sid,
                devResult:devResult    // 开发状态
            },function (data) {
                if(data.code==200){
                    layer.msg("机会数据更新成功",{icon:6});
                    layer.closeAll("iframe");  // 关闭弹出层
                    // 刷新父页面
                    parent.location.reload();
                }else{
                    layer.msg(data.msg,{icon : 5});
                }
            })
        })
    }





});
