package controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import core.Factory;
import model.FindModel;
import model.UpdateModel;
import model.VisitModel;

public class UpdateController extends BaseController{

	public UpdateController() throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException{
		// TODO Auto-generated constructor stub
		super();
	}
	
	public void deleteByID() throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException{
		String[] postID = view.getGet("deleteID");
		changeUpdateTrigger(true);
		if(postID!=null){
			Long id = Long.valueOf(postID[0]);
			//System.out.println("C "+path);
			FindModel find = (FindModel)Factory.M("Find");
			List<Map> object = find.findByID(id);
			String path = "";
			if(object!=null){
				path = (String) object.get(0).get("path");
			}
			UpdateModel delete = (UpdateModel)Factory.M("Update");
			//针对Apache的BUG
			boolean sOn = delete.deleteFolderByID(id);
			boolean successOrnot = delete.deleteByPath(DStr(path));
			this.judgeSuccess(successOrnot, "删除", "MyServlet?controller=Show&method=showBySession");
		}
	}
	
	/*//私有 删除所有子文件以及子文件夹
	private boolean deleteAllSubfolderByID(Long ID) throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException{
		boolean successOrNot = true;
		FindModel find = (FindModel)Factory.M("Find");
		List<Map> subfiles = find.findSubfileByID(ID);
		if(subfiles!=null){
			for(int i = 0; i<subfiles.size(); i++){
				if(CdeleteFileByID((Long) subfiles.get(i).get("ID"))==false){
					successOrNot = false;
				}
			}
		}
		List<Map> subfolders = find.findSubfolderByID(ID);
		if(subfolders == null){
			return successOrNot;
		}
		else{
			for(int i = 0; i<subfolders.size();i++){
				if(deleteAllSubfolderByID((Long) subfolders.get(i).get("ID")) == false){
					successOrNot = false;
				}
			}
		}
		if(CdeleteFolderByID(ID) == false){
			successOrNot = false;
		}
		return successOrNot;
	}
	
	//私有 删除通过一个文件夹
	private boolean CdeleteFolderByID(Long ID) throws ClassNotFoundException, InstantiationException, IllegalAccessException{
		UpdateModel delete = (UpdateModel)Factory.M("Update");
		boolean successOrnot = delete.deleteFolderByID(Long.valueOf(ID));
		return successOrnot;
	}
	
	//接口 删除文件夹
	public void deleteFolder() throws ClassNotFoundException, InstantiationException, IllegalAccessException, NumberFormatException, SQLException{
		String[] get = this.view.getGet("delete");
		String id = null;
		if(get!=null){
			id = get[0];
		}
		this.db.beginTransection();
		boolean successOrnot = this.deleteAllSubfolderByID(Long.valueOf(id));
		if(successOrnot == true){
			this.db.commit();
		}
		else{
			this.db.rollback();
		}
		this.judgeSuccess(successOrnot, "删除", "MyServlet?controller=Show&method=showBySession");
	}
	
	//私有 通过ID删除一个文件
	private boolean CdeleteFileByID(Long ID) throws ClassNotFoundException, InstantiationException, IllegalAccessException{
		UpdateModel delete = (UpdateModel)Factory.M("Update");
		boolean successOrnot = delete.deleteFileByID(Long.valueOf(ID));
		return successOrnot;
	}
	
	//接口 删除一个文件
	public void deleteFile() throws ClassNotFoundException, InstantiationException, IllegalAccessException{
		String[] get = this.view.getGet("delete");
		String id = null;
		if(get!=null){
			id = get[0];
		}
		boolean successOrnot = CdeleteFileByID(Long.valueOf(id));
		this.judgeSuccess(successOrnot, "删除", "MyServlet?controller=Show&method=showBySession");
	}*/

	//接口重命名文件
	public void updateName() throws ClassNotFoundException, InstantiationException, IllegalAccessException, NumberFormatException, SQLException{
		String[] postOldName = view.getPost("oldName");
		String[] postNewName = view.getPost("newName");
		String[] postUpdateID = view.getPost("UparentID");
		changeUpdateTrigger(true);
		if(postOldName == null){
			postOldName = view.getGet("oldName");
			postNewName = view.getGet("newName");
			postUpdateID = view.getGet("UparentID");
			System.out.println("GET " + postOldName[0] +" "+ postNewName[0] +" "+ postUpdateID[0]);
		}
		System.out.println("POST " + postOldName[0]  +" "+ postNewName[0]  +" "+ postUpdateID[0]);
		if(postNewName==null || postUpdateID==null){
			this.judgeSuccess(false, "更新", "MyServlet?controller=Show&method=showBySession");
		}
		else{
			UpdateModel update = (UpdateModel)Factory.M("Update");
			boolean successOrnot = update.updateNameByParentID(Long.valueOf(postUpdateID[0]),DStr(postOldName[0]),DStr(postNewName[0]));
			this.judgeSuccess(successOrnot, "更新", "MyServlet?controller=Show&method=showBySession");
		}
	}
	
	//插入文件
	public void insertDB() throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException, InterruptedException{
		//view.display("waiting.jsp");
		String[] postPath = view.getPost("insertPath");
		if(postPath!=null){
			UpdateModel update = (UpdateModel)Factory.M("Update");
			boolean successOrNot = update.deleteAll();
			VisitModel visit = (VisitModel)Factory.M("Visit");
			visit.filetodb(DStr(postPath[0]), -1);	
			FindModel find = (FindModel) Factory.M("Find");
			Map root = find.findRoot();
			if(root != null){
				view.setSession("currentDir", root);
				view.setSession("root", root);
			}	
			this.judgeSuccess(successOrNot, "插入", "MyServlet?controller=Show&method=showByRoot");
		}
	}
	
	//新建文件夹
	public void createFolder() throws NumberFormatException, ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException{
		String[] postCreateID = view.getPost("createID");
		if(postCreateID==null){
			this.judgeSuccess(false, "更新", "MyServlet?controller=Show&method=showBySession");
		}
		else{
			UpdateModel update = (UpdateModel)Factory.M("Update");
			boolean successOrnot = update.createFolder(Long.valueOf(postCreateID[0]));
			this.judgeSuccess(successOrnot, "创建", "MyServlet?controller=Show&method=showBySession");
		}
	}
}
