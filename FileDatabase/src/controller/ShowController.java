package controller;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;

import core.Factory;
import model.FindModel;


public class ShowController extends BaseController{

	private List<Map> list;//查询结果
	private JSONArray nodeArray = null;
	
	
	public ShowController() throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public void showByRoot() throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException{
		FindModel find = (FindModel) Factory.M("Find");
		
		Map root = find.findRoot();
		List<Map> files = new ArrayList();
		List<Map> folders = new ArrayList();
		if(root != null){
			view.setSession("currentDir", root);
			view.setSession("root", root);
			this.view.assign("path", DStr((String) root.get("path")));
			files = find.findSubfileByID(Long.valueOf((Long)root.get("ID")));
			folders = find.findSubfolderByID(Long.valueOf((Long)root.get("ID")));
		}		
		this.assignNode();
		this.view.assign("showFiles", files);
		this.view.assign("showFolders", folders);
		this.view.display("show.jsp");
	}

	public void showBySession() throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException{
		FindModel find = (FindModel) Factory.M("Find");
		//changeUpdateTrigger(false);
		Map<String,Object> info = (Map<String, Object>) this.view.getSession("currentDir");
		if(info == null){
			info = (Map<String, Object>) (view.getSession("root"));
			if(info == null){
				this.showByRoot();
				return;
			}
		}
		Long id = (Long)info.get("ID");
		//System.out.println("SessionParentID "+id);
		List<Map> files = find.findSubfileByID(Long.valueOf(id));
		List<Map> folders = find.findSubfolderByID(Long.valueOf(id));
		//注册该有内容
		this.assignNode();
		this.view.assign("path", DStr((String) info.get("path")));
		this.view.assign("showFiles", files);
		this.view.assign("showFolders", folders);
		this.view.display("show.jsp");
	}
	
	public void showByPath() throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException{
		String[] post = this.view.getPost("path");
		changeUpdateTrigger(false);
		//System.out.println(post[0]);
		String path;
		if(post != null){
			path = post[0];
		}
		else{
			//path = (String)view.getSession("???");
			path = (String) ((Map)(view.getSession("root"))).get("path");
		}
		FindModel find = (FindModel) Factory.M("Find");
		List<Map> folder = find.findFolderByPath(DPath(path));
		if(folder == null || folder.size()==0){
			path = (String) ((Map)(view.getSession("root"))).get("path");
			folder = find.findFolderByPath(DStr(path));
		}
		this.view.setSession("currentDir", folder.get(0));
		List<Map> files = find.findSubfileByPath(DPath(path));
		List<Map> folders = find.findSubfolderByPath(DPath(path));
		//注册该有内容
		this.assignNode();
		this.view.assign("path", DStr(path));
		this.view.assign("showFiles", files);
		this.view.assign("showFolders", folders);
		this.view.display("show.jsp");
	}
	
	public void enterFolder() throws NumberFormatException, SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException{
		String[] get = this.view.getGet("folder");
		changeUpdateTrigger(false);
		//System.out.println(post[0]);
		FindModel find = (FindModel) Factory.M("Find");
		String id = null;
		if(get != null){
			id = get[0];
		}
		else{
			return;
		}
		List<Map> folder = find.findByID(Long.valueOf(id));
		List<Map> files = find.findSubfileByID(Long.valueOf(id));
		List<Map> folders = find.findSubfolderByID(Long.valueOf(id));
		//注册该有内容
		this.assignNode();
		this.view.setSession("currentDir", folder.get(0));
		this.view.assign("path", DStr((String) folder.get(0).get("path")));
		this.view.assign("showFiles", files);
		this.view.assign("showFolders", folders);
		this.view.display("show.jsp");
	}
	
	public void backToParent() throws ClassNotFoundException, InstantiationException, IllegalAccessException, NumberFormatException, SQLException{
		//System.out.println(post[0]);
		FindModel find = (FindModel) Factory.M("Find");
		changeUpdateTrigger(false);
		Map<String,Object> info = (Map<String, Object>) this.view.getSession("currentDir");
		if(info == null){
			info = (Map) (view.getSession("root"));
		}
		Long id = (Long)info.get("ID");
		List<Map> folder = find.findByID(id);
		Long parent_id = (Long) folder.get(0).get("parent_id");
		if(parent_id==null || parent_id == 0){
			parent_id = id;
		}
		//注册该有内容
		List<Map> parent = find.findByID(parent_id);
		List<Map> files = find.findSubfileByID(parent_id);
		List<Map> folders = find.findSubfolderByID(parent_id);
		this.view.setSession("currentDir", parent.get(0));
		this.assignNode();
		this.view.assign("path", DStr((String) parent.get(0).get("path")));
		this.view.assign("showFiles", files);
		this.view.assign("showFolders", folders);
		this.view.display("show.jsp");
	}
	
	
	public List<Map> getFiles(){
		new Runnable(){
			@Override
			public void run() {
				try {
					 //list= model.findFileByNeeded(null);
					 FindModel find;
					try {
						 find = (FindModel) Factory.M("Find");
						 list.addAll(find.findFileByNeeded(null));
					} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} catch (SQLException e) {
					
				}
			}
			
		}.run();
		return list;
	}
	@SuppressWarnings("rawtypes")
	public List<Map> getFolders(){
		new Runnable(){
			@Override
			public void run() {
				try {
					FindModel find;
					try {
						find = (FindModel) Factory.M("Find");
						list.addAll(find.findFolderByNeeded(null));
					} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					 //list= model.findFolderByNeeded(null);
				} catch (SQLException e) {
					
				}
			}
			
		}.run();
		return list;
	}

	
	public JSONArray parseJSON(List<Map> list){
		Iterator<Map> it = list.iterator();
		JSONArray array = new JSONArray();
		String[] get = this.view.getGet("folder");
		String currentID = "0";
		String name,title;
		if(get!=null && get[0]!=null)
			currentID = get[0];
		while(it.hasNext()){
			Map<String,String> map = new HashMap<>();// = it.next();
			Map<String,Object> each =(Map<String,Object>) it.next();
			map.put("id",String.valueOf(each.get("ID")) );
			map.put("pid", String.valueOf(each.get("parent_id")));
			title=(String)each.get("name");
			int i=title.lastIndexOf('.');
			name = title.substring(0,i>0?i:title.length());
			map.put("name",name.length()>15?name.substring(0,15)+"..":name);//显示值
			map.put("title",title);//真值 
			map.put("isParent", each.get("sha256")==null?"true":"false");
			array.put(map);  //zTree副属性名改
		}
		return array;
	}



	
	private JSONArray updateArray(){
		nodeArray = new JSONArray();//为了清空。。
		if(list != null )
			list.clear();
		else
			list=new ArrayList<Map>();
		getFolders();
		getFiles();	
		nodeArray = parseJSON(list);
		return nodeArray;
	}

	
	public void assignNode(){	//在enterFolder等操作中设置hasUpdate为false  更新操作设置为true	
		if(hasUpdate)
			view.setSession("folders_json", updateArray());
	}
}
