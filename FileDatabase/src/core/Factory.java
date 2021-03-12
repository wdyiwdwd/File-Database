package core;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import model.*;
import controller.*;
import view.*;

public class Factory {
	
	//����һ��model�����
	public static Object M(String name) throws ClassNotFoundException, InstantiationException, IllegalAccessException{
		//model����
		String className = "model."+name+"Model";
		//model��
		Class model = null;
		//model����
		Object modelObject = null;
		model = Class.forName(className);
		modelObject = model.newInstance();
		return modelObject;
	}
	
	//����һ��Controller�����
	public static void C(String name,String methodName) throws ClassNotFoundException, NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException{
		
		String className = "controller."+name+"Controller";

		//controller��
		Class controller = null;
		//controller����
		Object controllerObjcet = null;
		//������
		Method method = null;
		controller = Class.forName(className);
		controllerObjcet = controller.newInstance();
		method = controller.getMethod(methodName, new Class[0]);
		//ִ�к���
		method.invoke(controllerObjcet, new Object[0]);

	}
	
	//����һ��View�����
	public static Object V(String name) throws ClassNotFoundException, InstantiationException, IllegalAccessException{
		//view����
		String className = "view."+name+"View";
		//view��
		Class view = null;
		//view����
		Object viewObject = null;
		view = Class.forName(className);
		viewObject = view.newInstance();
		return viewObject;
	}
	
	//ȥ���Ƿ��ַ�
	public static String ridOfIllegalChar(String str){
		str.replaceAll("\\\'", "\'");
		str.replaceAll("\\\"", "\"");
		str.replaceAll("\'", "\\\'");
		str.replaceAll("\"", "\\\"");
		return str;
	}
}
