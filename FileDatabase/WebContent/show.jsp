<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import = "java.util.Map" %>
<%@ page import = "java.util.List" %>
<%@ page import = "java.math.BigDecimal" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>数据库文件系统</title>
<link rel="stylesheet"  type="text/css" href="css/bootstrap.min.css">
<link rel="stylesheet"  type="text/css" href="css/fileico.css">
<link rel="stylesheet"  type="text/css" href="css/waiting.css">
<link rel="stylesheet"  type="text/css" href="css/tree.css">
<script type="text/javascript" src="js/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="js/bootstrap.min.js"></script>
<script type="text/javascript" src="js/fileico.js"></script>
<link rel="stylesheet" href="css\zTreeStyle\zTreeStyle.css" type="text/css">
<script type="text/javascript"  src="js\jquery.ztree.all-3.5.min.js"></script>
<script type="text/javascript"  src="js\nodesinfo.js"></script>
</head>
<body>
	<%response.setHeader("Pragma","No-cache"); 
	response.setHeader("Cache-Control","no-cache"); 
	response.setDateHeader("Expires", 0); 
	response.flushBuffer();%>
	<div id="loading">
	<div id="loading-center">
	<div id="loading-center-absolute">
	<div class="object" id="first_object"></div>
	<div class="object" id="second_object"></div>
	<div class="object" id="third_object"></div>
	<div class="object" id="forth_object"></div>
	</div>
	</div>
	 
	</div>
	<%
	List<Map> files = (List<Map>)request.getAttribute("showFiles");
	List<Map> folders = (List<Map>)request.getAttribute("showFolders");
	%>
	<%@include file="topMenu.jsp"%>
	<div class =row>
	<div class="col-md-3">
		<%@include file="ListTree.jsp"%>
	</div>
	<div class="col-md-9">
		<%
		for(Map folder:folders) {
			String shortName = (String)folder.get("name");
			if(shortName.length()>16){
				shortName = shortName.substring(0, 16);
			}
		%>
			<div class="folder">
			<button class="btn btn-default btn-circle cirlcebutton" id="enter"
				onclick="window.location.href='MyServlet?controller=Show&method=enterFolder&folder=<%=folder.get("ID") %>'">
				<span class="glyphicon glyphicon-ok"></span>
			</button>
			<button class="btn btn-default btn-circle cirlcebutton" id="remove"
				onclick="confirm('是否要删除此文件夹？')?window.location.href='MyServlet?controller=Update&method=deleteByID&deleteID=<%=folder.get("ID") %>':pass">
				<span class="glyphicon glyphicon-remove"></span>
			</button>
			<button class="btn btn-default btn-circle cirlcebutton" id="info" data-toggle="modal" data-target="#<%=folder.get("ID") %>">
				<span class="glyphicon glyphicon-cog"></span>
			</button>
			<label class="filename_s"><%=shortName %></label>
			<label class="filename"><%=folder.get("name") %></label>
		</div>
		<%};%>
		<%
		for(Map file:files) {
			String shortName = (String)file.get("name");
			if(shortName.length()>16){
				shortName = shortName.substring(0, 16);
			}
		%>
		<div class="file">
			<button class="btn btn-default btn-circle cirlcebutton" id="enter"  data-toggle="modal" data-target="#<%=file.get("ID") %>">
				<span class="glyphicon glyphicon-ok"></span>
			</button>
			<button class="btn btn-default btn-circle cirlcebutton" id="remove"
				onclick="confirm('是否要删除此文件？')?window.location.href='MyServlet?controller=Update&method=deleteByID&deleteID=<%=file.get("ID") %>':pass">
				<span class="glyphicon glyphicon-remove"></span>
			</button>
			<button class="btn btn-default btn-circle cirlcebutton" id="info"   data-toggle="modal" data-target="#<%=file.get("ID") %>">
				<span class="glyphicon glyphicon-cog"></span>
			</button>
			<label class="filename_s"><%=shortName %></label>
			<label class="filename"><%=file.get("name") %></label>
		</div>
		<%};%>
		<%
		String rootPath = "\\\\";
		Map root = (Map)request.getSession().getAttribute("root");
		if(root!=null){
			rootPath = (String)root.get("path");
			rootPath = rootPath.replaceAll("\\\\", "\\\\\\\\");
		}
		if(path.contains(rootPath)) {%>
		<a class="newFolder" href="MyServlet?controller=Update&method=createFolder&createID=<%=((Map)session.getAttribute("currentDir")).get("ID") %>" ></a>
		<%} %>
	</div>
	
	
	
<%for(Map folder:folders) {%>
<!--modal 文件夹模态框 -->
<div class="modal fade" id="<%=folder.get("ID") %>" tabindex="-1" role="dialog" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button><!--右上角的 X 关闭-->
                <h4 class="modal-title" id="myModalLabel">详细信息</h4>
            </div>
            <div class="modal-body">
        <!--in body-->
				<form id="searchForm" role="form" method="post" class="form-horizontal" action="MyServlet?controller=Update&method=updateName" onsubmit = "return confirm('确认重命名吗？')">
					<div class="form-group">
						<div class="col-sm-offset-1 col-sm-10">
							<label class="col-md-3 control-label">类别:</label>
							<div class="col-md-9">
								<input type="text" name="class" class="form-control" value="文件夹" readonly="readonly">
							</div>
						</div>
					</div>
					<div class="form-group">
						<div class="col-sm-offset-1 col-sm-10">
							<label for="name" class="col-md-3 control-label">名称:</label>
							<div class="col-md-9">
								<input type="text" name="newName" class="form-control" value="<%=folder.get("name") %>">
							</div>
						</div>
					</div>
					<div class="form-group">
						<div class="col-sm-offset-1 col-sm-10">
							<label for="name" class="col-md-3 control-label">路径:</label>
							<div class="col-md-9">
								<input type="text" name="folderPath" class="form-control" value="<%=folder.get("path") %>" readonly="readonly">
							</div>
						</div>
					</div>
					<div class="form-group">
						<div class="col-sm-offset-1 col-sm-10">
							<label for="name" class="col-md-3 control-label">深度:</label>
							<div class="col-md-9">
								<input type="text" name="folderDeep" class="form-control" value="<%=folder.get("deep") %> 层" readonly="readonly">
							</div>
						</div>
					</div>
					<div class="form-group">
						<div class="col-sm-offset-1 col-sm-10">
							<label class="col-md-3 control-label">大小:</label>
							<div class="col-md-9">
								<input type="text" name="size" class="form-control" value="<%=folder.get("size") %>B   <%=((BigDecimal)folder.get("size")).divide(BigDecimal.valueOf(1024),3, BigDecimal.ROUND_HALF_DOWN) %>KB   <%=((BigDecimal)folder.get("size")).divide(BigDecimal.valueOf(1024*1024),3, BigDecimal.ROUND_HALF_DOWN) %>MB   <%=((BigDecimal)folder.get("size")).divide(BigDecimal.valueOf(1024*1024*1024),3, BigDecimal.ROUND_HALF_DOWN) %>G" readonly="readonly">
							</div>
						</div>
					</div>
					<div class="form-group">
						<div class="col-sm-offset-1 col-sm-10">
							<label class="col-md-3 control-label">创建时间:</label>
							<div class="col-md-9">
								<input type="text" name="creationTime" class="form-control" value="<%=folder.get("creationTime")%>" readonly="readonly">
							</div>
						</div>
					</div>
					<div class="form-group">
						<div class="col-sm-offset-1 col-sm-10">
							<label class="col-md-3 control-label">最近访问:</label>
							<div class="col-md-9">
								<input type="text" name="lastAccessTime" class="form-control" value="<%=folder.get("lastAccessTime")%>" readonly="readonly">
							</div>
						</div>
					</div>
					<div class="form-group">
						<div class="col-sm-offset-1 col-sm-10">
							<label class="col-md-3 control-label">最近修改:</label>
							<div class="col-md-9">
								<input type="text" name="lastModifiedTime" class="form-control" value="<%=folder.get("lastModifiedTime")%>" readonly="readonly">
							</div>
						</div>
					</div>
					<div class="form-group">
						<div class="col-sm-offset-1 col-sm-10">
							<label class="col-md-3 control-label">子文件数:</label>
							<div class="col-md-9">
								<input type="text" name="subfile_num" class="form-control" value="<%=folder.get("subfile_num")%>" readonly="readonly">
							</div>
						</div>
					</div>
					<div class="form-group">
						<div class="col-sm-offset-1 col-sm-10">
							<label class="col-md-3 control-label">子文件夹数:</label>
							<div class="col-md-9">
								<input type="text" name="subdir_num" class="form-control" value="<%=folder.get("subdir_num")%>" readonly="readonly">
							</div>
						</div>
					</div>
					
					<input type="hidden" name="oldName" value="<%=folder.get("name") %>">
					<input type="hidden" name="UparentID" value="<%=folder.get("parent_id") %>">
            </div><!--modal body-->
            <div class="modal-footer"> 
            	<button type="submit" name="search" class="btn btn-primary">确定</button>
    			</form> 
		        <button type="button" class="btn btn-danger" data-dismiss="modal">关闭</button>
            </div>      
        </div><!-- /.modal-content -->
    </div><!-- /.modal-dialog -->
</div><!--modal-->
<%};%>
	
	
	
<%for(Map file:files) {%>
<!--modal 文件夹模态框 -->
<div class="modal fade" id="<%=file.get("ID") %>" tabindex="-1" role="dialog" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true" id="insertDB3">&times;</button><!--右上角的 X 关闭-->
                <h4 class="modal-title" id="myModalLabel">详细信息</h4>
            </div>
            <div class="modal-body">
        <!--in body-->
				<form id="searchForm" role="form" method="post" class="form-horizontal" action="MyServlet?controller=Update&method=updateName"  onsubmit = "return confirm('确认重命名吗？')">
					<div class="form-group">
						<div class="col-sm-offset-1 col-sm-10">
							<label class="col-md-3 control-label">类别:</label>
							<div class="col-md-9">
								<input type="text" name="class" class="form-control" value="文件" readonly="readonly">
							</div>
						</div>
					</div>
					<div class="form-group">
						<div class="col-sm-offset-1 col-sm-10">
							<label for="name" class="col-md-3 control-label">名称:</label>
							<div class="col-md-9">
								<input type="text" name="newName" class="form-control" value="<%=file.get("name") %>">
							</div>
						</div>
					</div>
					<div class="form-group">
						<div class="col-sm-offset-1 col-sm-10">
							<label for="name" class="col-md-3 control-label">路径:</label>
							<div class="col-md-9">
								<input type="text" name="filePath" class="form-control" value="<%=file.get("path") %>" readonly="readonly">
							</div>
						</div>
					</div>
					<div class="form-group">
						<div class="col-sm-offset-1 col-sm-10">
							<label for="name" class="col-md-3 control-label">深度:</label>
							<div class="col-md-9">
								<input type="text" name="fileDeep" class="form-control" value="<%=file.get("deep") %> 层" readonly="readonly">
							</div>
						</div>
					</div>
					<div class="form-group">
						<div class="col-sm-offset-1 col-sm-10">
							<label class="col-md-3 control-label">大小:</label>
							<div class="col-md-9">
								<input type="text" name="size" class="form-control" value="<%=file.get("size") %>B   <%=((BigDecimal)file.get("size")).divide(BigDecimal.valueOf(1024),3, BigDecimal.ROUND_HALF_DOWN) %>KB   <%=((BigDecimal)file.get("size")).divide(BigDecimal.valueOf(1024*1024),3, BigDecimal.ROUND_HALF_DOWN) %>MB   <%=((BigDecimal)file.get("size")).divide(BigDecimal.valueOf(1024*1024*1024),3, BigDecimal.ROUND_HALF_DOWN) %>G" readonly="readonly">
							</div>
						</div>
					</div>
					<div class="form-group">
						<div class="col-sm-offset-1 col-sm-10">
							<label class="col-md-3 control-label">创建时间:</label>
							<div class="col-md-9">
								<input type="text" name="creationTime" class="form-control" value="<%=file.get("creationTime")%>" readonly="readonly">
							</div>
						</div>
					</div>
					<div class="form-group">
						<div class="col-sm-offset-1 col-sm-10">
							<label class="col-md-3 control-label">最近访问:</label>
							<div class="col-md-9">
								<input type="text" name="lastAccessTime" class="form-control" value="<%=file.get("lastAccessTime")%>" readonly="readonly">
							</div>
						</div>
					</div>
					<div class="form-group">
						<div class="col-sm-offset-1 col-sm-10">
							<label class="col-md-3 control-label">最近修改:</label>
							<div class="col-md-9">
								<input type="text" name="lastModifiedTime" class="form-control" value="<%=file.get("lastModifiedTime")%>" readonly="readonly">
							</div>
						</div>
					</div>
					<div class="form-group">
						<div class="col-sm-offset-1 col-sm-10">
							<label class="col-md-3 control-label">所占块数:</label>
							<div class="col-md-9">
								<input type="text" name="space" class="form-control" value="<%=file.get("space")%>" readonly="readonly">
							</div>
						</div>
					</div>
					<div class="form-group">
						<div class="col-sm-offset-1 col-sm-10">
							<label class="col-md-3 control-label">SHA256:</label>
							<div class="col-md-9">
								<input type="text" name="sha256" class="form-control" value="<%=file.get("sha256")%>" readonly="readonly">
							</div>
						</div>
					</div>
					
					<input type="hidden" name="oldName" value="<%=file.get("name") %>">
					<input type="hidden" name="UparentID" value="<%=file.get("parent_id") %>">
            </div><!--modal body-->
            <div class="modal-footer"> 
            	<button type="submit" name="search" class="btn btn-primary">确定</button>
    			</form> 
		        <button type="button" class="btn btn-danger" data-dismiss="modal">关闭</button>
            </div>      
        </div><!-- /.modal-content -->
    </div><!-- /.modal-dialog -->
</div><!--modal-->
<%};%>

	
</body>
</html>