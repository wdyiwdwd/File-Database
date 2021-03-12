package model;

import core.DB;
import core.View;

public class BaseModel {
	protected String file; //存放文件的表名
	protected String folder; //存放文件夹的表名
	protected DB db;
	protected View view;
	
	//构造函数
	protected BaseModel() {
		super();
		file = "myfile1";
		folder = "mydir1";
		db=DB.getInstance();
		view=View.getInstance();
	}

	//去掉名字中的单引号和反斜杠
	protected String DStr(String str){
		str = str.replaceAll("\\\\\\\\\\\\", "\\\\");
		str = str.replaceAll("\\\\\\\\", "\\\\");
		str = str.replaceAll("\\\\\'", "\'");
		str = str.replaceAll("\\\\", "\\\\\\\\");
		str = str.replaceAll("\'", "\\\\\'");
		return str;
	}
	
	protected String CDStr(String str){
		str = str.replaceAll("\\\\\\\\", "\\\\\\\\\\\\\\\\");
		return str;
	}
	
	//将时间字符串变为Unix时间戳的函数字符串 传入到SQL中可以自动变成数字
	/*protected String Tstamp(String time){
		String utime = "unix_timestamp";
		utime = utime+"("+time+")";
		return utime;
	}*/
}
