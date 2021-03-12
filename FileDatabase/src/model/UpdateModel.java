package model;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;

import core.Factory;

public class UpdateModel extends BaseModel{

	public UpdateModel() {
		// TODO Auto-generated constructor stub
		super();
	}
	
	//�����ݿ��е�����ȫ��ɾ��
	public boolean deleteAll(){
		String[] where ={
			"1 = 1"
		};
		boolean bool1 = this.db.delete(this.folder, where);
		boolean bool2 = this.db.delete(this.file, where);
		return bool1&&bool2;
	}
	
	//ͨ��IDɾ���ļ���
	public boolean deleteFolderByID(Long ID){
		if(ID%2==0){
			return true;
		}
		String[] where ={
			"ID ="+String.valueOf(ID)
		};
		return this.db.delete(this.folder, where);
	}
	
	/*//ͨ��IDɾ���ļ�
	public boolean deleteFileByID(Long ID){
		String[] where ={
			"ID ="+String.valueOf(ID)
		};
		return this.db.delete(this.file, where);
	}*/
	
	//ͨ��pathɾ���ļ������ļ���
	public boolean deleteByPath(String path) throws IOException{
		File targetFile = new File(path);
		if (targetFile.isDirectory()) {
			FileUtils.deleteDirectory(targetFile);
			return true;
		} 
		else if (targetFile.isFile()) {
			targetFile.delete();
			return true;
		}
		return false;
	}
	
	/*public boolean updateNameByID(Long ID,String name){
		String table = "";
		if(ID%2!=0){
			table = this.folder;
		}
		else{
			table = this.file;
		}
		String[] where={
			"ID = "+String.valueOf(ID)
		};
		HashMap<String,String> data = new HashMap<>();
		data.put("name", name);
		return db.update(table, where, data);
	}*/
	
	public boolean updateNameByParentID(Long parent,String oldName,String newName) throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException{
		FindModel find = (FindModel)Factory.M("Find");
		List<Map> parents = find.findByID(parent);
		if(parents==null){
			return false;
		}
		String path = (String) parents.get(0).get("path");
		//System.out.println("rename "+path);
        File oldfile=new File(path+"\\\\"+oldName); 
        File newfile=new File(path+"\\\\"+newName); 
        if(!oldfile.exists()){
            return false;//�������ļ�������
        }
        if(newfile.exists()){//���ڸ�Ŀ¼���Ѿ���һ���ļ������ļ�����ͬ�������������� 
            System.out.println(newName+"�Ѿ����ڣ�"); 
        	return false;
        }
        else{ 
            oldfile.renameTo(newfile); 
        } 
        return true;
   }
	
	public boolean createFolder(Long parent) throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException{
		FindModel find = (FindModel)Factory.M("Find");
		List<Map> parents = find.findByID(parent);
		if(parents==null){
			return false;
		}
		String path = (String) parents.get(0).get("path");
		int i = 1;
		String newName = "new folder ";
		while(true){
			File newfile=new File(path+"\\\\"+newName+String.valueOf(i)); 
			if(!newfile.exists()){
				return newfile.mkdir();
			}
			i++;
		}
	}
	
}
