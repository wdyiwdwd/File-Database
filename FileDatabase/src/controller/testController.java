package controller;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import model.*;
import core.Factory;
import core.View;

public class testController extends BaseController{
	protected testController()
			throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException {
		super();
		// TODO Auto-generated constructor stub
	}

	public void test() throws IOException{
		System.out.print("incontroller");
		View.getInstance().getServlet().getResponse().getWriter().print("incontroller");
		//View.getInstance().display("NewFile.jsp");
		String str = View.getInstance().getGet("method")[0];
		Date datetime = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String applyDate =sdf.format(datetime);
		SimpleDateFormat stf = new SimpleDateFormat("HH:ss");
		String applyTime =stf.format(datetime);
		view.assign("currentTime", applyTime);
		view.assign("currentDate", applyDate);
		View.getInstance().display("test.jsp");
		//System.out.print(str);
	}
	
	public void test2(){
		if(View.getInstance().getPost("name")!=null){
			testModel tm = null;
			try {
				tm = (testModel)Factory.M("test");
				Integer i = tm.getCnt(View.getInstance().getPost("name")[0]);
				View.getInstance().assign("filename", View.getInstance().getPost("name")[0]);
				View.getInstance().assign("cnt", i);
				View.getInstance().display("test.jsp");
			} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	public void test3(){
		this.showMessages("test3", "MyServlet?controller=test&method=test");
	}
}
