<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	String Cdate = (String)request.getAttribute("currentDate");
	String Ctime = (String)request.getAttribute("currentTime");
	String path = (String)request.getAttribute("path");
%>




<nav class="navbar navbar-default" role="navigation">
	<div class="container-fluid">
	<div class = "row">
	<div class="col-md-2">
	    <header class="navbar-header">
	        <a class="navbar-brand" data-toggle="modal" data-target="#insert" style="cursor:pointer;">数据库文件系统</a>
	    </header>
	 </div>
	 <div class="col-md-1">
	    <button type="button" class="btn btn-default" style="
	    	width:70%;
	    	height:90%;
	    	margin-top:14%"
			onclick="window.location.href='MyServlet?controller=Show&method=backToParent'">
	        <span class="glyphicon glyphicon-share-alt"></span>
	   	</button>
	 </div>
	 <div class="col-md-6">
	    <form class="navbar-form" method="post" action="MyServlet?controller=Show&method=showByPath">
	    <div class="form-group">
	        <input name="path" type="text" class="form-control" style="width:320%;float:left" value="<%=path %>">
	    </div>
	    <button type="submit" class="btn btn-default" style="margin-top:0.8%;float:right;border:none;">
	       	<span class="glyphicon glyphicon-arrow-right"></span>
	    </button>
	    </form>
	</div>
	<div class="col-md-3">
	    <form class="navbar-form navbar-left"  method="post" action="MyServlet?controller=Find&method=simplyFind">
	        <div class="form-group">
	            <input name="fuzzyName" type="text" class="form-control" placeholder="Search" style="width:90%">
	        </div>
	       	<input name="searchPath" type="hidden" value="<%=path %>">
	        <button type="submit" class="btn btn-default" style="margin-top:1.2%;float:right">
	        	<span class="glyphicon glyphicon-search"></span>
	        </button>
	    </form>
	   	<button type="button" class="btn btn-default" style="margin-top:3.5%" data-toggle="modal" data-target="#search" >
	        <span class="glyphicon glyphicon-th-list"></span>
	   	</button>
	</div>
</nav>


<!--modal 模态框 -->
<div class="modal fade" id="search" tabindex="-1" role="dialog" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button><!--右上角的 X 关闭-->
                <h4 class="modal-title" id="myModalLabel">详细搜索</h4>
            </div>
            <div class="modal-body">
        <!--in body-->
				<form id="searchForm" role="form" method="post" class="form-horizontal" action="MyServlet?controller=Find&method=complexlyFind" >
					<div class="form-group" id="classDiv">
						<div class="col-sm-offset-1 col-sm-10">
							<label class="col-md-3 control-label">类别:</label>
							<div class="col-md-9">
								<select name="theClass" class="form-control">
										<option value="0">全部</option>
										<option value="1">文件夹</option>
										<option value="2">文件</option>
								</select>
							</div>
						</div>
					</div>
					<div class="form-group">
						<div class="col-sm-offset-1 col-sm-10">
							<label for="name" class="col-md-3 control-label">名称:</label>
							<div class="col-md-9">
								<input type="text" name="theName" class="form-control" placeholder="请输入文件（夹）名">
							</div>
						</div>
					</div>
					<div class="form-group">
						<div class="col-sm-offset-1 col-sm-10">
							<label class="col-md-3 control-label">-</label>
							<div class="col-md-4">
								<label class="checkbox-inline">
								<input type="radio" name="theKind" value="1">精确查找名称
								</label>
							</div>
							<div class="col-md-1"></div>
							<div class="col-md-4">
								<label class="checkbox-inline">
								<input type="radio" name="theKind" value="0"  checked>模糊查找名称
								 </label>
							</div>
						</div>
					</div>
					<div class="form-group">
						<div class="col-sm-offset-1 col-sm-10">
							<label for="name" class="col-md-3 control-label">层数:</label>
							<div class="col-md-4">
								<input type="text" name="smallDeep" class="form-control" value="0">
							</div>
							<label class="control-label col-md-1">-</label>
							<div class="col-md-4">
								<input type="text" name="bigDeep" class="form-control" value="99">
							</div>
						</div>
					</div>
					<div class="form-group">
						<div class="col-sm-offset-1 col-sm-10">
							<label class="col-md-3 control-label">大小:</label>
							<div class="col-md-2">
								<input type="text" name="smallSize" class="form-control" value="0" >
							</div>
							<label class="control-label col-md-1">-</label>
							<div class="col-md-3">
								<input type="text" name="bigSize" class="form-control" value="999999">
							</div>
							<div class="col-md-3">
								<select name="unit" class="form-control">
										<option value="0">B</option>
										<option value="1">KB</option>
										<option value="2">MB</option>
										<option value="3">G</option>
								</select>
							</div>
						</div>
					</div>
					<div class="form-group">
						<div class="col-sm-offset-1 col-sm-10">
							<label class="col-md-3 control-label">创建时间:</label>
							<div class="col-md-5">
								<input type="date" name="CStartDate" class="form-control" value="1970-01-01">
							</div>
							<div class="col-md-4">
								<input type="time" name="CStartTime" class="form-control" value="00:00">
							</div>
						</div>
					</div>
					<div class="form-group">
						<div class="col-sm-offset-1 col-sm-10">
							<label class="col-md-3 control-label">-</label>
							<div class="col-md-5">
								<input type="date" name="CEndDate" class="form-control" value="<%=Cdate%>">
							</div>
							<div class="col-md-4">
								<input type="time" name="CEndTime" class="form-control" value="<%=Ctime%>">
							</div>
						</div>
					</div>
					<div class="form-group">
						<div class="col-sm-offset-1 col-sm-10">
							<label class="col-md-3 control-label">最近访问:</label>
							<div class="col-md-5">
								<input type="date" name="AStartDate" class="form-control" value="1970-01-01">
							</div>
							<div class="col-md-4">
								<input type="time" name="AStartTime" class="form-control" value="00:00">
							</div>
						</div>
					</div>
					<div class="form-group">
						<div class="col-sm-offset-1 col-sm-10">
							<label class="col-md-3 control-label">-</label>
							<div class="col-md-5">
								<input type="date" name="AEndDate" class="form-control" value="<%=Cdate%>">
							</div>
							<div class="col-md-4">
								<input type="time" name="AEndTime" class="form-control" value="<%=Ctime%>">
							</div>
						</div>
					</div>
					<div class="form-group">
						<div class="col-sm-offset-1 col-sm-10">
							<label class="col-md-3 control-label">最近修改:</label>
							<div class="col-md-5">
								<input type="date" name="MStartDate" class="form-control" value="1970-01-01">
							</div>
							<div class="col-md-4">
								<input type="time" name="MStartTime" class="form-control" value="00:00">
							</div>
						</div>
					</div>
					<div class="form-group">
						<div class="col-sm-offset-1 col-sm-10">
							<label class="col-md-3 control-label">-</label>
							<div class="col-md-5">
								<input type="date" name="MEndDate" class="form-control" value="<%=Cdate%>">
							</div>
							<div class="col-md-4">
								<input type="time" name="MEndTime" class="form-control" value="<%=Ctime%>">
							</div>
						</div>
					</div>
					<div class="form-group" id="SHA256Div">
						<div class="col-sm-offset-1 col-sm-10">
							<label for="name" class="col-md-3 control-label">SHA256:</label>
							<div class="col-md-9">
								<input type="text" name="findSHA256" class="form-control" placeholder="请输入SHA256">
							</div>
						</div>
					</div>
					
					<input type="hidden" name="CsearchPath" value="<%=path %>">
            </div><!--modal body-->
            <div class="modal-footer"> 
            	<button type="submit" name="search" class="btn btn-primary">搜索</button>
    			</form> 
		        <button type="button" class="btn btn-danger" data-dismiss="modal">关闭</button>
            </div>      
        </div><!-- /.modal-content -->
    </div><!-- /.modal-dialog -->
</div><!--modal-->



	

<!--modal 插入数据库时的模态框 -->
<div class="modal fade" id="insert" tabindex="-1" role="dialog" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button><!--右上角的 X 关闭-->
                <h4 class="modal-title" id="myModalLabel">插入数据库</h4>
            </div>
            <div class="modal-body">
        <!--in body-->
				<form id="searchForm" role="form" method="post" class="form-horizontal" action="MyServlet?controller=Update&method=insertDB"  onsubmit = "return insertSubmit()">
					<div class="form-group">
						<div class="col-sm-offset-1 col-sm-10">
							<label class="col-md-3 control-label">路径:</label>
							<div class="col-md-9">
								<input type="text" name="insertPath" class="form-control" placeholder="请输入路径" >
							</div>
						</div>
					</div>
            </div><!--modal body-->
            <div class="modal-footer"> 
            	<button type="submit"  class="btn btn-primary" id="insertDB">插入</button>
    			</form> 
		        <button type="button" class="btn btn-danger" data-dismiss="modal" id="insertDB2">关闭</button>
            </div>      
        </div><!-- /.modal-content -->
    </div><!-- /.modal-dialog -->
</div><!--modal-->


