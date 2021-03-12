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
	
	//ͨ��ID�ҵ��ļ������ļ��У��������ļ���ż�����ļ���
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
	
	//ͨ���ļ�����ȷ�����ļ�
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
	
	//ͨ���ļ�����ģ�������ļ��� like
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
	
	//ͨ���ļ�������ȷ�����ļ���
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
	
	//ͨ���ļ�����ģ�������ļ�
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
	
	//��Controller�е����������������where���飬�ҵ�����Ҫ���ļ���Ϣ
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
	
	//��Controller�е����������������where���飬�ҵ�����Ҫ���ļ�����Ϣ
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
	
	//�ҵ�Ŀ��ID���������ļ���
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
	
	//�ҵ�Ŀ��ID���������ļ�
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
	
	//ͨ��path�ҵ���Ŀ¼�µ����ļ�
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
	
	//ͨ��path�ҵ���Ŀ¼�µ����ļ���
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
	
	//ͨ��path�ҵ�һ���ļ�
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
	
	//ͨ��path�ҵ�һ���ļ���
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
	
	//�ж�һ��·���ǲ����ļ���
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
