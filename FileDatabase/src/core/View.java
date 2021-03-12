package core;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.util.Map.Entry;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.catalina.servlet4preview.RequestDispatcher;

public class View {
	//�ڲ����ж��� ʵ��SingleTon
	private MyServlet _servlet = null;
	//SingleTon
	private static View  _view = null;
	//��ʼ������
	public static void init(MyServlet servlet){
		if(!(_view instanceof View)){
			View._view = new View(servlet);
		}
	}
	//��ȡ˽�ж���
	public static View getInstance(){
		if(!(View._view instanceof View)){
			throw new NullPointerException();
		}
		return View._view;
	}
	//˽�й��캯��
	private View(MyServlet servlet){
		_servlet = servlet;
	}
	//�õ�Postֵ
	public String[] getPost(String key){
		Map<String,String[]> map = _servlet.getParam();
		if(map.containsKey(key)){
			return map.get(key);
		}
		return null;
	}
	//�õ�getֵ
	public String[] getGet(String key){
		Map<String,String[]> map = _servlet.getParam();
		if(map.containsKey(key)){
			return map.get(key);
		}
		return null;
	}
	
	public void show(String url){
		try {
			this._servlet.getResponse().getWriter().println(url);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//��תҳ��
	public void display(String url){
		HttpServletRequest request = this._servlet.getRequest();
		javax.servlet.RequestDispatcher rd = request.getRequestDispatcher(url);
		try {
			rd.forward(request, this._servlet.getResponse());
		} catch (ServletException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	//����ע��
	public void assign(String key , Object value){
		HttpServletRequest request = this._servlet.getRequest();
		request.setAttribute(key, value);
	}
	//����ע��
	public void assign(Map<String,Object> map){
		Iterator<?> it = map.entrySet().iterator();
		while(it.hasNext()){
			Map.Entry<String, Object> entry = (Entry<String, Object>) it.next();
			this.assign(entry.getKey(), entry.getValue());
		}
	}
	//�õ��Ự
	public Object getSession(String key){
		HttpSession session = this.getServlet().getRequest().getSession();
		return session.getAttribute(key);
	}
	//����Session
	public void setSession(String key, Object value){
		HttpSession session = this.getServlet().getRequest().getSession();
		session.setAttribute(key, value);
	}
	
	//����servlet����
	public MyServlet getServlet(){
		return this._servlet;
	}
	
	
}
