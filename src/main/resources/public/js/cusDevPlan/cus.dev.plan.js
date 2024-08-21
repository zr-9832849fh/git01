layui.use(['table','layer'],function(){
    var layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        table = layui.table;
    //机会数据列表展示
    var  tableIns = table.render({
        elem: '#saleChanceList',         // is_valid代表的是有效状态,没有删除的
        url : ctx+'/sale_chance/list?state=1&flag=1',   // 设置flag参数，表示查询的是“客户开发计划”   state=1代表状态
        cellMinWidth : 95,
        page : true,
        height : "full-125",
        limits : [10,15,20,25],
        limit : 10,
        toolbar: "#toolbarDemo",    // 开启头部工具栏
        id : "saleChanceListTable",
        cols : [[
            {type: "checkbox", fixed:"center"},
            {field: "id", title:'编号',fixed:"true"},
            {field: 'chanceSource', title: '机会来源',align:"center"},
            {field: 'customerName', title: '客户名称',  align:'center'},
            {field: 'cgjl', title: '成功几率', align:'center'},
            {field: 'overview', title: '概要', align:'center'},
            {field: 'linkMan', title: '联系人',  align:'center'},
            {field: 'linkPhone', title: '联系电话', align:'center'},
            {field: 'description', title: '描述', align:'center'},
            {field: 'createMan', title: '创建人', align:'center'},
            {field: 'createDate', title: '创建时间', align:'center'},
            {field: 'updateDate', title: '修改时间', align:'center'},
            {field: 'devResult', title: '开发状态', align:'center',templet:function (d) {
                    return formatterDevResult(d.devResult);
                }},
            {title: '操作',fixed:"right",align:"center", minWidth:150,templet:"#op"} // "op"代表行工具栏
        ]]
    });

    function formatterDevResult(value){
        /**
         * 0-未开发
         * 1-开发中
         * 2-开发成功
         * 3-开发失败
         */
        if(value==0){
            return "<div style='color: yellow'>未开发</div>";
        }else if(value==1){
            return "<div style='color: #00FF00;'>开发中</div>";
        }else if(value==2){
            return "<div style='color: #00B83F'>开发成功</div>";
        }else if(value==3){
            return "<div style='color: red'>开发失败</div>";
        }else {
            return "<div style='color: #af0000'>未知</div>"
        }
    }


    // 多条件搜索
    $(".search_btn").on("click",function () {
        table.reload("saleChanceListTable",{
            page:{
                curr:1
            },
            where:{
                customerName:$("input[name='customerName']").val(),// 客户名
                createMan:$("input[name='createMan']").val(),// 创建人
                devResult:$("#devResult").val()    //开发状态
            }
        })
    });

    /*行工具栏的监听（开发、详情）   tool(saleChances） 对应"数据表格"的layfilter*/
    table.on("tool(saleChances)",function (obj) {
        var layEvent = obj.event;
        if(layEvent==="dev"){      // 开发
            // 打开计划项数据维护界面
            openCusDevPlanDialog("计划项数据维护",obj.data.id);
        }else if(layEvent ==="info"){   // 详情
            // 打开计划项数据详情界面
            openCusDevPlanDialog("计划项数据详情",obj.data.id);
        }
    });

    /**
     * 打开计划项数据维护界面
     * @param title
     * @param sid
     */
    function openCusDevPlanDialog(title,sid) {
        // iframe层
        layui.layer.open({
            title:title,
            type:2,
            area:["700px","500px"],
            maxmin:true,  // 可以最大最小化
            content:ctx+"/cus_dev_plan/toCusDevPlanDataPage?sid="+sid  // 请求到后台（通过sid去查），用于跳转到cus_Dev_plan_data.ftl页面
        })
    }





});
