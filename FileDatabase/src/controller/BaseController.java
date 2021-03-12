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
	//�õ�DBʵ��
	protected DB db;
	//�õ�Viewʵ��
	protected View view;
	
	//�ж��Ƿ�ˢ����
	protected boolean hasUpdate = true;
	
	protected void changeUpdateTrigger(boolean trigger){
		hasUpdate = trigger;
	}
	//���캯��
	protected BaseController() throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException {
		super();
		db = DB.getInstance();
		view = View.getInstance();
		hasUpdate = true;
		//ע�ᵱǰʱ��
		Date datetime = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String applyDate =sdf.format(datetime);
		SimpleDateFormat stf = new SimpleDateFormat("HH:ss");
		String applyTime =stf.format(datetime);
		view.assign("currentTime", applyTime);
		view.assign("currentDate", applyDate);
		//�����Ŀ¼
		FindModel find = (FindModel) Factory.M("Find");
		Map root = find.findRoot();
		view.setSession("root", root);
	}

	//����һ����ʾ�Ի���
	protected void showMessages(String info, String url){
		if(info == null){
			view.show("<script>window.location.href="+url+"</script>");
		}
		else{
			view.show("<script>alert('"+info+"');window.location.href='"+url+"'</script>");
		}
	}
	
	//�жϲ����Ƿ�ɹ��ĶԻ��򣬵�һ�������ǳɹ����
	protected void judgeSuccess(boolean successOrNot,String operation,String direction){
		if(successOrNot){
			this.showMessages(operation+"�ɹ�", direction);
		}
		else{
			this.showMessages(operation+"ʧ��", direction);
		}
	}
	
	//���ַ����е����źͷ�б�ܹ淶��
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
	
	//���ַ���ת��
	
	protected String CDStr(String str){
		str = str.replaceAll("\\\\\\\\", "\\\\\\\\\\\\\\\\");
		return str;
	}
	
	//��ʱ�任��Unixʱ�������
	protected String Tstamp(String time){
		String utime = "unix_timestamp";
		utime = utime+"("+time+")";
		return utime;
	}
	
	//�������path�е�б�ܶ����ɷ�б�ܣ�Ȼ��淶��
	protected String DPath(String path){
		while(path.endsWith("/") || path.endsWith("\\\\")){
			path = path.substring(0, path.length()-1);
		}
		path = path.replaceAll("/", "\\\\");
		path = DStr(path);
		return path;
	}
}
