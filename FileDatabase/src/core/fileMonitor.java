package core;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;
import org.apache.commons.io.monitor.FileAlterationListener;

public class fileMonitor {
	public static DB db = DB.getInstance();
	public static long interval;
	public static FileAlterationObserver observer;
	public static FileAlterationMonitor monitor;
	public static boolean start = false;
	public fileMonitor() {
		super();
		// TODO Auto-generated constructor stub
		db=DB.getInstance();
	}
	static{
		start = false;
		LocalFileListener.init();
		interval = 10l;
		if(!(monitor instanceof FileAlterationMonitor)){
			System.out.println("create monitor");
			monitor = new FileAlterationMonitor(interval);
		}
	}

	public static void fileMonite(String path) throws Exception {
		// TODO Auto-generated method stub
		// monitoring directory
		String rootDir = path;

		// loop millisecond Â 
		if(!(observer instanceof FileAlterationObserver)){
			observer = new FileAlterationObserver(rootDir, null, null);
			System.out.println("create observer");
		}
		LocalFileListener listener = LocalFileListener.getInstance();

		System.out.println("FSW: begin to monitor the directory (" + rootDir + ")");
		// start to monitor
		if(start == false){	
			observer.addListener(listener);
			monitor.addObserver(observer);
			monitor.start();
			System.out.println("monitor start");
			start = true;
		}
	}
	
	public static void stopMonitor() throws Exception{
		monitor.stop();
		System.out.println("»ØÊÕ");
	}

}
