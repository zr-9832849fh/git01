<!DOCTYPE html>
<html>
<head>
	<title>营销机会管理</title>
	<#include "../common.ftl">
</head>
<body class="childrenBody">

<form class="layui-form" >

	<blockquote class="layui-elem-quote quoteBox">
				<#if premission?seq_contains("101001")>      <#--营销管理-查询-->
				<form class="layui-form">
					<div class="layui-inline">
						<div class="layui-input-inline">
							<input type="text" name="customerName"
								   class="layui-input
							searchVal" placeholder="客户名" />
						</div>
						<div class="layui-input-inline">
							<input type="text" name="createMan" class="layui-input
							searchVal" placeholder="创建人" />
						</div>
						<div class="layui-input-inline">
							<select name="state"  id="state">
								<option value="" >分配状态</option>
								<option value="0">未分配</option>
								<option value="1" >已分配</option>
							</select>
						</div>
						<a class="layui-btn search_btn" data-type="reload"><i
									class="layui-icon">&#xe615;</i> 搜索</a>
					</div>
				</form>
				</#if>


	</blockquote>
	<#--表格工具栏-->
	<table id="saleChanceList" class="layui-table"  lay-filter="saleChances"></table>

		<script type="text/html" id="toolbarDemo">
				<div class="layui-btn-container">
						<#if premission?seq_contains("101002")>
						<a class="layui-btn layui-btn-normal addNews_btn" lay-event="add">
							<i class="layui-icon">&#xe608;</i>
							添加
						</a>
						</#if>
						<#if premission?seq_contains("101003")>
						<a class="layui-btn layui-btn-normal delNews_btn" lay-event="del">
							<i class="layui-icon">&#xe608;</i>
							删除
						</a>
						</#if>
				</div>
		</script>



	<!--操作-->
	<script id="saleChanceListBar" type="text/html">
				<#if premission?seq_contains("101004")>
				<a class="layui-btn layui-btn-xs" id="edit" lay-event="edit">编辑</a>
																<#--lay-event="edit 被绑定到了sale-chance.js中-->
				</#if>

				<#if premission?seq_contains("101003")>
				<a class="layui-btn layui-btn-xs layui-btn-danger" lay-event="del">删除</a>
				</#if>
	</script>

</form>
<script type="text/javascript" src="${ctx}/js/saleChance/sale.chance.js"></script>

</body>
</html>