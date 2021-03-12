package core;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class DB {
	//�ڲ����ж��� ʵ��SingleTon
	private Connection _conn = null;
	//������Ϣ
	private static HashMap<String,String> _config = null;
	//SingleTon
	private static DB  _db = null;
	//static��ʼ��
	/*static{
		_config = new HashMap<String,String>();
		_config.put("dbhost", "localhost:3306");
		_config.put("dbname", "test");
		_config.put("username", "gong");
		_config.put("password", "");
	}*/
	//��ʼ������
	public static void init(HashMap<String,String> config){
		DB._config = config;
		if(!(DB._db instanceof DB)){
			DB._db = new DB(DB._config);
		}
	}
	public static void init(){
		if(!(DB._db instanceof DB)){
			DB._db = new DB(DB._config);
		}
	}
	//��ȡ˽�ж���
	public static DB getInstance(){
		if(!(DB._db instanceof DB)){
			DB.init(DB._config);
		}
		return DB._db;
	}
	//˽�й��캯��
	private DB(HashMap<String,String> config){
		if(config != null){
			//�ҵ�����
			try{
	            Class.forName("com.mysql.jdbc.Driver");
	        }catch(ClassNotFoundException e1){
	            System.out.println("�Ҳ���MySQL����!");
	            e1.printStackTrace();
	        }
			//�������ݿ�
			String connect = "jdbc:mysql://"+config.get("dbhost")+"/"+config.get("dbname")+"?useUnicode=true&characterEncoding=utf-8&useSSL=false";
			try {
				this._conn=DriverManager.getConnection(connect,config.get("username"),config.get("password"));
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				System.out.println("�޷��������ݿ�!");
				e.printStackTrace();
			}
		}
	}
	//��ResultSetת����List
	public List<Map> extractData(ResultSet rs) throws SQLException {
		ResultSetMetaData md = rs.getMetaData();
		int num = md.getColumnCount();
		List<Map> listOfRows = new ArrayList();
		while (rs.next()) {
		Map mapOfColValues = new HashMap(num);
		for (int i = 1; i <= num; i++) {
		mapOfColValues.put(md.getColumnName(i), rs.getObject(i));
		}
		listOfRows.add(mapOfColValues);
		}
		return listOfRows;
	}
	
	//��������
	@SuppressWarnings("unchecked")
	public boolean insert(String table,HashMap<String,String> data){
		StringBuffer addKey = new StringBuffer();
		StringBuffer addValue = new StringBuffer();
		Iterator<?> it = data.entrySet().iterator();
		while(it.hasNext()){
			Map.Entry<String, String> entry = (Entry<String, String>) it.next();
			addKey.append(entry.getKey()+",");
			addValue.append(entry.getValue()+"','");
		}
		String keys = addKey.toString().substring(0,addKey.length() - 1);
		String values = addValue.toString().substring(0,addValue.length() - 3);
		String insert = "INSERT INTO "+table+" ("+keys+") VALUES('"+values+"')";
		try {
			PreparedStatement stmt = this._conn.prepareStatement(insert);
			stmt.execute();
			stmt.close();
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println(insert+"\n�������ݿ�ʧ��!");
			e.printStackTrace();
		}
		return false;
	}
	
	//����where�ַ���
	private String AnalyzeWhereString(String[] where){
		String wholeWhere = "";
		if(where.length > 0){
			wholeWhere = "WHERE ";
		}
		for(int i = 0 ;i < where.length; i++){
			wholeWhere += "("+where[i]+") AND ";
		}
		wholeWhere = wholeWhere.substring(0, wholeWhere.length() - 4);
		return wholeWhere;
	}
	
	//��������
	@SuppressWarnings("unchecked")
	public boolean update(String table , String[] where , HashMap<String,String> data){
		String wholeWhere = this.AnalyzeWhereString(where);
		String set = "";
		Iterator<?> it = data.entrySet().iterator();
		while(it.hasNext()){
			Map.Entry<String, String> entry = (Entry<String, String>) it.next();
			set += entry.getKey() + " = '" + entry.getValue() + "',";
		}
		set = set.substring(0, set.length() - 1);
		
		String update = "UPDATE " + table + " SET " + set + " " + wholeWhere;
		try {
			PreparedStatement stmt = this._conn.prepareStatement(update);
			stmt.execute();
			stmt.close();
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println(update+"\n�������ݿ�ʧ��!");
			e.printStackTrace();
		}
		return false;
	}
	
	//��ȫ����
	//��������
	@SuppressWarnings("unchecked")
	public boolean update_safe(String table , String[] where , HashMap<String,String> data){
		String safe0 = "SET SQL_SAFE_UPDATES = 0";
		String safe1 = "SET SQL_SAFE_UPDATES = 1";
		String wholeWhere = this.AnalyzeWhereString(where);
		String set = "";
		Iterator<?> it = data.entrySet().iterator();
		while(it.hasNext()){
			Map.Entry<String, String> entry = (Entry<String, String>) it.next();
			set += entry.getKey() + " = '" + entry.getValue() + "',";
		}
		set = set.substring(0, set.length() - 1);
		
		String update = "UPDATE " + table + " SET " + set + " " + wholeWhere + " LIMIT 1";
		try {
			PreparedStatement stmt = this._conn.prepareStatement(safe1);
			stmt.execute();
			stmt = this._conn.prepareStatement(update);
			stmt.execute();
			stmt = this._conn.prepareStatement(safe0);
			stmt.execute();
			stmt.close();
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println(update+"\n�������ݿ�ʧ��!");
			e.printStackTrace();
		}
		return false;
	}
	
	//ɾ������
	public boolean delete(String table ,String[] where){
		String wholeWhere = this.AnalyzeWhereString(where);
		String delete = "DELETE FROM " + table + " " + wholeWhere;
		try {
			PreparedStatement stmt = this._conn.prepareStatement(delete);
			stmt.execute();
			stmt.close();
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println(delete+"\n�������ݿ�ʧ��!");
			e.printStackTrace();
		}
		return false;
	}
	
	//��ȫɾ��
	public boolean delete_safe(String table ,String[] where){
		String safe0 = "SET SQL_SAFE_UPDATES = 0";
		String safe1 = "SET SQL_SAFE_UPDATES = 1";
		String wholeWhere = this.AnalyzeWhereString(where);
		String delete = "DELETE FROM " + table + " " + wholeWhere + " LIMIT 1";
		try {
			PreparedStatement stmt = this._conn.prepareStatement(safe1);
			stmt.execute();
			stmt = this._conn.prepareStatement(delete);
			stmt.execute();
			stmt = this._conn.prepareStatement(safe0);
			stmt.execute();
			stmt.close();
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println(delete+"\n��ȫɾ�����ݿ�ʧ��!");
			e.printStackTrace();
		}
		return false;
	}
	
	//���ַ���������,�ָ
	private String implodeWithComma(String[] strings){
		String result = "";
		for(int i = 0; i < strings.length; i++){
			result += strings[i]+",";
		}
		result = result.substring(0, result.length()-1);
		return result;
	}
	
	//��ѯ����
	public List select(String[] fields, String[] tables, HashMap<String,String[]> param) throws SQLException{
		String fieldStr = this.implodeWithComma(fields);
		String tableStr = this.implodeWithComma(tables);
		String whereStr = "";
		String limitStr = "";
		String orderStr = "";
		String groupStr = "";
		if(param.containsKey("where") && param.containsKey("group")){
			throw new SQLException("�����group������having��������������");
		}
		if(param.containsKey("where")){
			whereStr = this.AnalyzeWhereString(param.get("where"));
		}
		if(param.containsKey("limit")){
			limitStr += "LIMIT ";
			limitStr += this.implodeWithComma(param.get("limit"));
		}
		if(param.containsKey("order")){
			orderStr += "ORDER BY ";
			orderStr += this.implodeWithComma(param.get("order"));
		}
		if(param.containsKey("group")){
			groupStr += "GROUP BY ";
			groupStr += param.get("group")[0];
			if(param.get("group").length == 2){
				groupStr += " HAVING ";
				groupStr += param.get("group")[1];
			}
		}
		String select = "SELECT "+fieldStr+" FROM "+tableStr+" "+groupStr+" "+whereStr+" "+orderStr+" "+limitStr;
		ResultSet result = null;
		try {
			
			PreparedStatement stmt = this._conn.prepareStatement(select);
			result = stmt.executeQuery();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println(select+"\n�������ݿ�ʧ��!");
			e.printStackTrace();
		}
		List list = this.extractData(result);
		result.close();
		return list;		
	}
	
	//������������
	public int count(String field, String table, String[] where){
		String wholeWhere = this.AnalyzeWhereString(where);
		String distinct = (field.equals("*"))? "":"DISTINCT";
		String count = "SELECT COUNT("+distinct+" "+field+") FROM "+table+" "+wholeWhere;
		try {
			PreparedStatement stmt = this._conn.prepareStatement(count);
			ResultSet result = stmt.executeQuery();
			result.first();
			int cnt=result.getInt(1);
			result.close();
			stmt.close();
			return cnt;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println(count+"\n��������ʧ��!");
			e.printStackTrace();
		}
		return -1;
	}
	
	//��ʼ������
	public void beginTransection(){
		try {
			this._conn.setAutoCommit(false);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("�޷�����������");
			e.printStackTrace();
		}
	}
	
	//�ύ����
	public void commit(){
		try {
			this._conn.commit();
			this._conn.setAutoCommit(true);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("�ύ����ʧ��");
			e.printStackTrace();
		}
	}
	
	//�ع�����
	public void rollback(){
		try {
			this._conn.rollback();
			this._conn.setAutoCommit(true);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("�ع�����ʧ��");
			e.printStackTrace();
		}
	}
	
	//����sql���
	public boolean excute(String sql){
		try {
			PreparedStatement stmt = this._conn.prepareStatement(sql);
			boolean success = stmt.execute();
			stmt.close();
			return success;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println(sql+"\nSql����ύʧ��!");
			e.printStackTrace();
		}
		return false;
	}
	
	//��������
	public void finalize(){
		try {
			this._conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
	
