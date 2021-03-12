package model;

import core.DB;
import core.View;

public class BaseModel {
	protected String file; //����ļ��ı���
	protected String folder; //����ļ��еı���
	protected DB db;
	protected View view;
	
	//���캯��
	protected BaseModel() {
		super();
		file = "myfile1";
		folder = "mydir1";
		db=DB.getInstance();
		view=View.getInstance();
	}

	//ȥ�������еĵ����źͷ�б��
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
	
	//��ʱ���ַ�����ΪUnixʱ����ĺ����ַ��� ���뵽SQL�п����Զ��������
	/*protected String Tstamp(String time){
		String utime = "unix_timestamp";
		utime = utime+"("+time+")";
		return utime;
	}*/
}
