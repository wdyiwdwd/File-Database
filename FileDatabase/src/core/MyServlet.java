package core;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class MyServlet
 */
@WebServlet("/MyServlet")
public class MyServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	private HttpServletRequest request;
	
	private HttpServletResponse response;
	
	private boolean listenerStart;
    /**
     * @throws Exception 
     * @see HttpServlet#HttpServlet()
     */
    public MyServlet() throws Exception {
        super();
        // TODO Auto-generated constructor stub
        //MySQLEncapsulation.init();
        
        listenerStart = false;
		DB.init(Config.dbconfig);
		/*HashMap<String,String[]> param = new HashMap<String,String[]>();
		String[] where = {
			"parent_id = 0 or parent_id is null"	
		};
		param.put("where", where);
		String[] tables = {"mydir1"};
		String[] fields = {"*"};
		List<Map> root = DB.getInstance().select(fields,tables , param);
		String path = null;
		if(root!=null){
			path = (String) root.get(0).get("path");
		}
		System.out.println(path);
		if(path!=null){
			fileMonitor.fileMonite(path);
		}*/
    }
    
    private String DStr(String str){
		str = str.replaceAll("\\\\\\\\\\\\", "\\\\");
		str = str.replaceAll("\\\\\\\\", "\\\\");
		str = str.replaceAll("\\\\\'", "\'");
		str = str.replaceAll("\\\\", "\\\\\\\\");
		str = str.replaceAll("\'", "\\\\\'");
		return str;
	}

    public void PdoGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
    	this.doGet(request, response);
    }
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setContentType("text/html; charset=UTF-8");
		View.init(this);
		this.request = request;
		this.response = response;
		this.request.setCharacterEncoding("UTF-8");//传值编码
		this.response.setContentType("text/html;charset=UTF-8");//设置传输编码
		if(request.getParameter("controller") != null){
			String controller = request.getParameter("controller");
			String method = request.getParameter("method");
			try {
				Factory.C(controller, method);
				String path = DStr((String) ((Map)View.getInstance().getSession("root")).get("path"));
				if(fileMonitor.start == false && this.listenerStart == false){
					fileMonitor.fileMonite(path);
					this.listenerStart = true;
				}
			} catch (ClassNotFoundException | NoSuchMethodException | SecurityException | IllegalAccessException
					| IllegalArgumentException | InvocationTargetException | InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	
	public Map<String,String[]> getParam(){
		return request.getParameterMap();
	}
	
	public HttpServletResponse getResponse(){
		return this.response;
	}
	public HttpServletRequest getRequest(){
		return this.request;
	}
	
	public void finalize() throws Exception{
		fileMonitor.stopMonitor();
	}
}
