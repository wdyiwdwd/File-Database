package controller;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import core.DB;
import core.View;
import core.Factory;
import model.FindModel;

public class BaseController {
	//得到DB实例
	protected DB db;
	//得到View实例
	protected View view;
	
	//判断是否刷新树
	protected boolean hasUpdate = true;
	
	protected void changeUpdateTrigger(boolean trigger){
		hasUpdate = trigger;
	}
	//构造函数
	protected BaseController() throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException {
		super();
		db = DB.getInstance();
		view = View.getInstance();
		hasUpdate = true;
		//注册当前时间
		Date datetime = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String applyDate =sdf.format(datetime);
		SimpleDateFormat stf = new SimpleDateFormat("HH:ss");
		String applyTime =stf.format(datetime);
		view.assign("currentTime", applyTime);
		view.assign("currentDate", applyDate);
		//保存根目录
		FindModel find = (FindModel) Factory.M("Find");
		Map root = find.findRoot();
		view.setSession("root", root);
	}

	//生成一个提示对话框
	protected void showMessages(String info, String url){
		if(info == null){
			view.show("<script>window.location.href="+url+"</script>");
		}
		else{
			view.show("<script>alert('"+info+"');window.location.href='"+url+"'</script>");
		}
	}
	
	//判断操作是否成功的对话框，第一个参数是成功与否
	protected void judgeSuccess(boolean successOrNot,String operation,String direction){
		if(successOrNot){
			this.showMessages(operation+"成功", direction);
		}
		else{
			this.showMessages(operation+"失败", direction);
		}
	}
	
	//把字符串中单引号和反斜杠规范化
	protected String DStr(String str){
		str = str.replaceAll("\\\\\\\\\\\\", "\\\\");
		str = str.replaceAll("\\\\\\\\", "\\\\");
		str = str.replaceAll("\\\\\'", "\'");
		str = str.replaceAll("\\\\", "\\\\\\\\");
		str = str.replaceAll("\'", "\\\\\'");
		return str;
	}
	
	protected Long DNum(String str){
		Long num = (long) 0;
		try {
			num = Long.valueOf(str);
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
		}
		return num;
	}
	
	//逆字符串转化
	
	protected String CDStr(String str){
		str = str.replaceAll("\\\\\\\\", "\\\\\\\\\\\\\\\\");
		return str;
	}
	
	//把时间换成Unix时间戳函数
	protected String Tstamp(String time){
		String utime = "unix_timestamp";
		utime = utime+"("+time+")";
		return utime;
	}
	
	//将输入的path中的斜杠都换成反斜杠，然后规范化
	protected String DPath(String path){
		while(path.endsWith("/") || path.endsWith("\\\\")){
			path = path.substring(0, path.length()-1);
		}
		path = path.replaceAll("/", "\\\\");
		path = DStr(path);
		return path;
	}
}
