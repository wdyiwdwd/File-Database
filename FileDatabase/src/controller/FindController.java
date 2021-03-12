package controller;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import core.Factory;
import model.BaseModel;
import model.FindModel;

public class FindController extends BaseController {

	public FindController() throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException{
		// TODO Auto-generated constructor stub
		super();
	}

	//简单通过模糊查找文件以及文件夹名
	public void simplyFind() throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException{
		String[] postName = this.view.getPost("fuzzyName");
		String[] postPath = this.view.getPost("searchPath");
		changeUpdateTrigger(false);
		String name = null;
		String path = null;
		if(postName != null){
			name = postName[0];
		}
		if(postPath != null){
			String rootPath = (String) ((Map)(view.getSession("root"))).get("path");
			path = postPath[0];
			if(!path.contains(DStr(rootPath)))
				path = rootPath;
		}
		FindModel find = (FindModel) Factory.M("Find");
		List<Map> folders = find.findFolderByFussyNameInPath(DStr(name), DPath(path));
		System.out.println(DStr(path));
		List<Map> files = find.findFileByFussyNameInPath(DStr(name), DStr(path));
		this.view.assign("path", "%"+DStr(name)+"%");
		this.view.assign("showFiles", files);
		this.view.assign("showFolders", folders);
		this.view.display("show.jsp");
	}
	
	//通过弹窗详细查找符合条件的文件以及文件夹
	public void complexlyFind() throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException{
		//get All Post
		String[] postClass = view.getPost("theClass");
		String[] postName = view.getPost("theName");
		String[] postKind = view.getPost("theKind");
		String[] postSmallSize = view.getPost("smallSize");
		String[] postBigSize = view.getPost("bigSize");
		String[] postUnit = view.getPost("unit");
		String[] postCStartDate = view.getPost("CStartDate");
		String[] postCStartTime = view.getPost("CStartTime");
		String[] postCEndDate = view.getPost("CEndDate");
		String[] postCEndTime = view.getPost("CEndTime");
		String[] postMStartDate = view.getPost("MStartDate");
		String[] postMStartTime = view.getPost("MStartTime");
		String[] postMEndDate = view.getPost("MEndDate");
		String[] postMEndTime = view.getPost("MEndTime");
		String[] postAStartDate = view.getPost("AStartDate");
		String[] postAStartTime = view.getPost("AStartTime");
		String[] postAEndDate = view.getPost("AEndDate");
		String[] postAEndTime = view.getPost("AEndTime");
		String[] postPath = view.getPost("CsearchPath");
		String[] postSHA = view.getPost("findSHA256");
		String[] postSDeep = view.getPost("smallDeep");
		String[] postBDeep = view.getPost("bigDeep");
		changeUpdateTrigger(false);
		//生成提示信息显示在path上
		String path = "";
		//生成where (String[])
		ArrayList<String> arrayWhere = new ArrayList<>();
		//根据名字查找
		if(postName!=null && postKind!=null){
			if(postKind[0].equals("0")){
				arrayWhere.add("name like '%"+DStr(postName[0])+"%'");
				path += "name like '%"+DStr(postName[0])+"%'\t";
			}
			else{
				arrayWhere.add("name = '"+DStr(postName[0])+"'");
				path += "name = '"+DStr(postName[0])+"'\t";
			}
		}
		//根据大小查找
		if(postSmallSize!=null && postBigSize!=null && !postSmallSize[0].equals("") && !postBigSize[0].equals("")){
			long smallSize = DNum(postSmallSize[0]);
			long bigSize =  DNum(postBigSize[0]);
			Long unit =  DNum(postUnit[0]);
			smallSize = (long) (smallSize * Math.pow(1024, unit));
			bigSize = (long) (bigSize * Math.pow(1024, unit));
			arrayWhere.add("size between "+String.valueOf(smallSize)+" and "+String.valueOf(bigSize));
			path += String.valueOf(smallSize)+" < size < "+String.valueOf(bigSize)+"\t";
		}
		//根据创建时间
		if(postCStartDate!=null && postCEndDate!=null && postCStartTime!=null && postCEndTime!=null){
			String startTime = postCStartDate[0] + " " + postCStartTime[0];
			String endTime = postCEndDate[0] + " " + postCEndTime[0];
			arrayWhere.add("creationTime between '"+startTime+"' and '"+endTime+"'");
			System.out.println("CTime "+startTime+"-"+endTime);
		}
		//根据最近修改时间
		if(postMStartDate!=null && postMEndDate!=null && postMStartTime!=null && postMEndTime!=null){
			String startTime = postMStartDate[0] + " " + postMStartTime[0];
			String endTime = postMEndDate[0] + " " + postMEndTime[0];
			arrayWhere.add("creationTime between '"+startTime+"' and '"+endTime+"'");
			System.out.println("MTime "+startTime+"-"+endTime);
		}
		//根据最近访问时间
		if(postAStartDate!=null && postAEndDate!=null && postAStartTime!=null && postAEndTime!=null){
			String startTime = postAStartDate[0] + " " + postAStartTime[0];
			String endTime = postAEndDate[0] + " " + postAEndTime[0];
			arrayWhere.add("creationTime between '"+startTime+"' and '"+endTime+"'");
			System.out.println("ATime "+startTime+"-"+endTime);
		}
		//根据SHA256访问
		if(postSHA!=null && !postSHA[0].equals("")){
			String SHA256 = postSHA[0];
			arrayWhere.add("SHA256 = '"+DStr(SHA256)+"'");
			System.out.println("SHA256 = "+DStr(SHA256));
		}
		//根据最小层数访问
		if(postSDeep!=null && !postSDeep[0].equals("") && postBDeep!=null && !postBDeep[0].equals("")){
			String sdeep = postSDeep[0];
			String bdeep = postBDeep[0];
			arrayWhere.add("deep >= "+ DNum(sdeep)+"");
			arrayWhere.add("deep <= "+ DNum(bdeep)+"");
			path += sdeep+"<= deep <= "+bdeep+"\t";
		}
		//从哪一个路径开始搜索
		if(postPath != null){
			String rootPath = (String) ((Map)(view.getSession("root"))).get("path");
			String thePath = postPath[0];
			if(!thePath.contains(DStr(rootPath)))
				thePath = rootPath;
			arrayWhere.add("path like '"+CDStr(DStr(thePath))+"\\\\\\\\%'");
			//path += DStr(thePath)+"\t";
			System.out.println(CDStr(DStr(thePath)));
		}
		//生成数组
		List<Map> folders = new ArrayList<Map>();
		List<Map> files = new ArrayList<Map>();
		String[] where = (String[])arrayWhere.toArray(new String[0]);
		FindModel find = (FindModel) Factory.M("Find");
		if(postClass[0].equals("0")){
			folders = find.findFolderByNeeded(where);
			files = find.findFileByNeeded(where);
			path+=" File And Folder";
		}
		else if(postClass[0].equals("1")){
			folders = find.findFolderByNeeded(where);
			path+=" Folder";
		}
		else if(postClass[0].equals("2")){
			files = find.findFileByNeeded(where);
			path+=" File";
		}
		this.view.assign("path", DStr(path));
		this.view.assign("showFiles", files);
		this.view.assign("showFolders", folders);
		this.view.display("show.jsp");
	}
}
