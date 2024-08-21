$(function () {
    loadModuleInfo(); 
});

// 定义树结构对象
var zTreeObj;

/**
 * 利用ajax到后台拿数据
 */
function loadModuleInfo() {
    $.ajax({
        type:"post",      // queryAllModules：查询当前角色的所有资源
        url:ctx+"/module/queryAllModules?roleId="+$("input[name='roleId']").val(),
        dataType:"json",
        success:function (data) {

            // zTree 的参数配置，深入使用请参考 API 文档（setting 配置详解）
            var setting = {
                check: {   // 复选框
                    enable: true   // true-选中
                },
                // 使用简单的json数据
                data: {
                    simpleData: {
                        enable: true
                    }
                },
                // 绑定函数
                callback: {
                    /*当checkbox 被选中或取消选中时的触发函数*/
                    onCheck: zTreeOnCheck
                }
            };
            // 将所有查询到的资源显示在ztree上
            zTreeObj = $.fn.zTree.init($("#test1"), setting, data);   // 第一个参数代表ztree放置位置，
        }
    })
}

 // event：事件   treeNode：树节点
function zTreeOnCheck(event, treeId, treeNode) {
    //alert(treeNode.tId + ", " + treeNode.name + "," + treeNode.checked);
    // getCheckedNodes：获取所有被勾选的节点集合，如果checked=true。表示获取勾选的节点；如果checked=false，表示未获取勾选的节点
    var nodes= zTreeObj.getCheckedNodes(true); // nodes：代表选中的资源
    var mids="mids=";       // mids = 1&mids=2&mids  以这个形式传到后台
    // 判断并遍历选中的资源集合
    if (nodes > 0){
        for(var i=0;i<nodes.length;i++){
            if(i<nodes.length-1){
                mids=mids+nodes[i].id+"&mids=";    // nodes[i].id:代表每一个  资源id
            }else{
                mids=mids+nodes[i].id;
            }
        }
    }


    $.ajax({
        type:"post",
        url:ctx+"/role/addGrant",
        /*将角色id 和 资源id全部传达后台*/
        data:mids+"&roleId="+$("input[name='roleId']").val(),    // 获取需要授权的 角色ID 的值（隐藏域）
        dataType:"json",
        success:function (data) {       // 后台把信息传到这里
            console.log(data);
        }
    })

}