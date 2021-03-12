package core;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import model.*;
import controller.*;
import view.*;

public class Factory {
	
	//生成一个model类对象
	public static Object M(String name) throws ClassNotFoundException, InstantiationException, IllegalAccessException{
		//model类名
		String className = "model."+name+"Model";
		//model类
		Class model = null;
		//model对象
		Object modelObject = null;
		model = Class.forName(className);
		modelObject = model.newInstance();
		return modelObject;
	}
	
	//生成一个Controller类对象
	public static void C(String name,String methodName) throws ClassNotFoundException, NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException{
		
		String className = "controller."+name+"Controller";

		//controller类
		Class controller = null;
		//controller对象
		Object controllerObjcet = null;
		//方法名
		Method method = null;
		controller = Class.forName(className);
		controllerObjcet = controller.newInstance();
		method = controller.getMethod(methodName, new Class[0]);
		//执行函数
		method.invoke(controllerObjcet, new Object[0]);

	}
	
	//生成一个View类对象
	public static Object V(String name) throws ClassNotFoundException, InstantiationException, IllegalAccessException{
		//view类名
		String className = "view."+name+"View";
		//view类
		Class view = null;
		//view对象
		Object viewObject = null;
		view = Class.forName(className);
		viewObject = view.newInstance();
		return viewObject;
	}
	
	//去除非法字符
	public static String ridOfIllegalChar(String str){
		str.replaceAll("\\\'", "\'");
		str.replaceAll("\\\"", "\"");
		str.replaceAll("\'", "\\\'");
		str.replaceAll("\"", "\\\"");
		return str;
	}
}
