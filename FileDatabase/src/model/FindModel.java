package model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FindModel extends BaseModel {

	public FindModel() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	//通过ID找到文件或者文件夹，单数是文件，偶数是文件夹
	public List<Map> findByID(Long ID) throws SQLException{
		HashMap<String,String[]> param = new HashMap<>();
		String[] where = {
			"ID = "+String.valueOf(ID)
		};
		param.put("where", where);
		String[] fields = {"*"};
		String[] tables = new String[1];
		tables[0] = (ID%2==0)? this.file:this.folder;
		return this.db.select(fields, tables, param);
	}
	
	//通过文件名精确查找文件
	public List<Map> findFileByExactNameInPath(String name, String path) throws SQLException{
		HashMap<String,String[]> param = new HashMap<>();
		String[] where = {
			"name = '"+name+"'",
			"path like '"+CDStr(path)+"\\\\\\\\%'" 
		};
		param.put("where", where);
		String[] fields = {"*"};
		String[] tables = {this.file};
		String[] order = {"name","lastModifiedTime"};
		param.put("order", order);
		return this.db.select(fields, tables, param);
	}
	
	//通过文件夹名模糊查找文件夹 like
	public List<Map> findFileByFussyNameInPath(String name, String path) throws SQLException{
		HashMap<String,String[]> param = new HashMap<>();
		/*
		System.out.println("model "+path);
		System.out.println("model "+name);
		*/
		String[] where = {
			"name like '%"+name+"%'",
			"path like '"+CDStr(path)+"\\\\\\\\%'" 
		};
		param.put("where", where);
		String[] fields = {"*"};
		String[] tables = {this.file};
		String[] order = {"name","lastModifiedTime"};
		param.put("order", order);
		return this.db.select(fields, tables, param);
	}
	
	//通过文件夹名精确查找文件夹
	public List<Map> findFolderByExactNameInpath(String name, String path) throws SQLException{
		HashMap<String,String[]> param = new HashMap<>();
		String[] where = {
			"name = '"+name+"'",
			"path like '"+CDStr(path)+"\\\\\\\\%'" 
		};
		param.put("where", where);
		String[] fields = {"*"};
		String[] tables = {this.folder};
		String[] order = {"name","lastModifiedTime"};
		param.put("order", order);
		return this.db.select(fields, tables, param);
	}
	
	//通过文件夹名模糊查找文件
	public List<Map> findFolderByFussyNameInPath(String name, String path) throws SQLException{
		HashMap<String,String[]> param = new HashMap<>();
		String[] where = {
			"name like '%"+name+"%'",
			"path like '"+CDStr(path)+"\\\\\\\\%'" 
		};
		param.put("where", where);
		String[] fields = {"*"};
		String[] tables = {this.folder};
		String[] order = {"name","lastModifiedTime"};
		param.put("order", order);
		return this.db.select(fields, tables, param);
	}
	
	//在Controller中调用这个函数，传入where数组，找到所需要的文件信息
	public List<Map> findFileByNeeded(String[] where) throws SQLException{
		HashMap<String,String[]> param = new HashMap<>();
		if(where != null && !(where.length==1&&where[0]=="")){
			param.put("where", where);
		}
		String[] fields = {"*"};
		String[] tables = {this.file};
		String[] order = {"name","lastModifiedTime"};
		param.put("order", order);
		return this.db.select(fields, tables, param);
	}
	
	//在Controller中调用这个函数，传入where数组，找到所需要的文件夹信息
	public List<Map> findFolderByNeeded(String[] where) throws SQLException{
		HashMap<String,String[]> param = new HashMap<>();
		if(where != null && !(where.length==1&&where[0]=="")){
			param.put("where", where);
		}
		String[] fields = {"*"};
		String[] tables = {this.folder};
		String[] order = {"name","lastModifiedTime"};
		param.put("order", order);
		return this.db.select(fields, tables, param);
	}
	
	//找到目标ID的所有子文件夹
	public List<Map> findSubfolderByID(long ID) throws SQLException{
		HashMap<String,String[]> param = new HashMap<>();
		String[] where = {
			"parent_id = "+String.valueOf(ID)
		};
		param.put("where", where);
		String[] fields = {"*"};
		String[] tables = {this.folder};
		return this.db.select(fields, tables, param);
	}
	
	//找到目标ID的所有子文件
	public List<Map> findSubfileByID(long ID) throws SQLException{
		HashMap<String,String[]> param = new HashMap<>();
		String[] where = {
			"parent_id = "+String.valueOf(ID)
		};
		param.put("where", where);
		String[] fields = {"*"};
		String[] tables = {this.file};
		return this.db.select(fields, tables, param);
	}
	
	//通过path找到该目录下的子文件
	public List<Map> findSubfileByPath(String path) throws SQLException{
		HashMap<String,String[]> param = new HashMap<>();
		String[] where = {
			"path = '"+path+"'"
		};
		param.put("where", where);
		String[] fields = {"*"};
		String[] tables = {this.folder};
		String[] order = {"name","lastModifiedTime"};
		param.put("order", order);
		List<Map> result = this.db.select(fields, tables, param);
		if(result!=null)
			return this.findSubfileByID((long)(result.get(0)).get("ID"));
		return null;
	}
	
	//通过path找到该目录下的子文件夹
	public List<Map> findSubfolderByPath(String path) throws SQLException{
		HashMap<String,String[]> param = new HashMap<>();
		String[] where = {
			"path = '"+path+"'"
		};
		param.put("where", where);
		String[] fields = {"*"};
		String[] tables = {this.folder};
		String[] order = {"name","lastModifiedTime"};
		param.put("order", order);
		List<Map> result = this.db.select(fields, tables, param);
		if(result!=null)
			return this.findSubfolderByID((long)(result.get(0)).get("ID"));
		return null;
	}
	
	//通过path找到一个文件
	public List<Map> findFileByPath(String path) throws SQLException{
		HashMap<String,String[]> param = new HashMap<>();
		String[] where = {
			"path = '"+path+"'"
		};
		param.put("where", where);
		String[] fields = {"*"};
		String[] tables = {this.file};
		String[] order = {"name","lastModifiedTime"};
		param.put("order", order);
		return this.db.select(fields, tables, param);
	}
	
	//通过path找到一个文件夹
	public List<Map> findFolderByPath(String path) throws SQLException{
		HashMap<String,String[]> param = new HashMap<>();
		String[] where = {
			"path = '"+path+"'"
		};
		param.put("where", where);
		String[] fields = {"*"};
		String[] tables = {this.folder};
		String[] order = {"name","lastModifiedTime"};
		param.put("order", order);
		return this.db.select(fields, tables, param);
	}
	
	public Map findRoot() throws SQLException{
		HashMap<String,String[]> param = new HashMap<>();
		String[] where = {
			"parent_id is null or parent_id = 0"
		};
		param.put("where", where);
		String[] fields = {"*"};
		String[] tables = {this.folder};
		List<Map> root =  this.db.select(fields, tables, param);
		if(root.size()>0){
			return root.get(0);
		}
		return null;
	}
	
	//判断一个路径是不是文件夹
	public Boolean isFolderPath(String path) throws SQLException{
		if(this.findFolderByPath(path)!=null){
			return Boolean.TRUE;
		}
		else if(this.findFileByPath(path)!=null){
			return Boolean.FALSE;
		}
		return null;
	}
}
