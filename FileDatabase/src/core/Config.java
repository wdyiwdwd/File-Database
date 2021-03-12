package core;

import java.util.HashMap;

public class Config {
	public static HashMap<String,String> dbconfig;
	
	static {
		dbconfig = new HashMap<String,String>();
		dbconfig.put("dbhost", "localhost:3306");
		dbconfig.put("dbname", "file");
		dbconfig.put("username", "gong");
		dbconfig.put("password", "");
	}
	
}
