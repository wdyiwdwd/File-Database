<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<title>Document</title>
	<link rel="stylesheet" href="css\zTreeStyle\zTreeStyle.css" type="text/css">
<script type="text/javascript" src="js\jquery-1.9.1.min.js"></script>
<script type="text/javascript"  src="js\jquery.ztree.all-3.5.min.js"></script>

</head>
<body>
	<%@ page import = "java.util.Map" %>
	<script type = "text/javascript">
	function onClick(event,treeID,treeNode){
		var zNodes = $.fn.zTree.getZTreeObj("treeDemo");
		zNodes.expandNode(treeNode,true,false,true,true);
		if(treeNode.id%2==1){
			window.location.href="MyServlet?controller=Show&method=enterFolder&folder="+treeNode.id;
		}else{
			window.location.href="MyServlet?controller=Show&method=enterFolder&folder="+treeNode.pid;
		}
	}
	function beforeEditName(treeId,treeNode){
		var temp = treeNode.name;
		treeNode.name=treeNode.title;
		treeNode.title=temp;
	}
	function beforeRename(treeId, treeNode, newName, isCancel){
		if(!isCancel&&newName!=treeNode.name&&confirm("确认修改？"))
			window.location.href="MyServlet?controller=Update&method=updateName&oldName="+treeNode.name+"&UparentID="+treeNode.pid+"&newName="+newName;
		else{
			var temp = treeNode.title;
			treeNode.title=treeNode.name;
			treeNode.name=temp;
		}
			
	}
	function beforeRemove(treeID,treeNode){
		return confirm("确认删除?");
	}
	function onRemove(event,treeID,treeNode){
		window.location.href="MyServlet?controller=Update&method=deleteByID&deleteID="+treeNode.id;
	}
	function openNode(ID){
		var zNodes = $.fn.zTree.getZTreeObj("treeDemo");
		zNodes.expandNode(zNodes.getNodeByParam("id",ID,null),true,false,true,true);
	}

	
		var setting = {
			data: {
				simpleData: {
					enable: true,
					idKey:"id",
			        pIdKey:"pid",
			        rootPId:null
				},
				key:{
				    name:"name"
				}
			},
			edit:{
				enable:true,
				showRenameBtn:true,
				showRemoveBtn:true
			},
			callback:{
				onClick:onClick,
				beforeRemove:beforeRemove,
				onRemove:onRemove,
				beforeRename:beforeRename,
				beforeEditName:beforeEditName
			}

		};
		
		$(document).ready(function(){
			$.fn.zTree.init($("#treeDemo"), setting, zNodes);
			openNode(<%= ((Map)session.getAttribute("currentDir")).get("ID") %>);
		});
		var zNodes=<%= session.getAttribute("folders_json") %>
	</script>
	<div class="zTreeDemoBackground left" id="tree">
		<ul id="treeDemo" class="ztree" ></ul>
	</div>
</body>
</html>
