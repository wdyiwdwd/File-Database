package model;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.security.MessageDigest;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class VisitModel extends BaseModel {

	public static boolean over;
	public static long NUM;
	public static String path;
	public static int maxDe;
	public static int deepCount;

	/*********内部类**********/

	class MyFileVisitor3 extends SimpleFileVisitor<Path>
	// 统计文件数量
	{
		// 访问文件时触发该方法
		public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
			VisitModel.NUM++;
			return FileVisitResult.CONTINUE;
		}

		// 开始访问目录时触发该方法
		public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
			VisitModel.NUM++;
			return FileVisitResult.CONTINUE;
		}

		public FileVisitResult visitFileFailed(Path file, IOException exc) {
			System.out.println(exc);
			System.out.println(file);
			return FileVisitResult.CONTINUE;
		}
	}

	class MyFileVisitor4 extends SimpleFileVisitor<Path>
	// 插入数据库
	{
		private long begin;
		private long end;
		private long count;
		
		private String filename = file;

		public MyFileVisitor4(long b, long e) {
			super();
			// TODO Auto-generated constructor stub
			begin = b;
			end = e;
			count = 0;
		}

		// 访问文件时触发该方法
		public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
			count++;
			if (count > begin && count <= end) {
				String name = file.getFileName().toString();
				String path = file.toString();
				int deep=file.getNameCount();
				long size = attrs.size();
				long space = (long) (Math.ceil(size / 1024.0));
				SimpleDateFormat fmtDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date cre_t = new Date(attrs.creationTime().toMillis());
				Date lac_t = new Date(attrs.lastAccessTime().toMillis());
				Date lmo_t = new Date(attrs.lastModifiedTime().toMillis());
				String sha256 = VisitModel.getFingerSHA256(file.toFile());
				String parent=file.getParent().toString();
				long parent_id=0;
				while(true){
					List<Map> result;
					String[] fields = { "ID" };
					String[] tables = { folder };
					HashMap<String, String[]> param = new HashMap<String, String[]>();
					String[] where = { "path='" + DStr(parent) + "'" };
					param.put("where", where);
					try {
						result = db.select(fields, tables, param);
						if(result.size()!=0)
							parent_id=(long) result.get(0).get("ID");
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					if(parent_id!=0){
						break;
					}else{
						//爸爸在其他线程中还没插进来，等一会
						try {
							Thread.sleep(100);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("name", DStr(name));
				map.put("path", DStr(path));
				map.put("deep", String.valueOf(deep));
				map.put("parent_id", String.valueOf(parent_id));
				map.put("size", String.valueOf(size));
				map.put("space", String.valueOf(space));
				map.put("creationTime", fmtDate.format(cre_t));
				map.put("lastAccessTime", fmtDate.format(lac_t));
				map.put("lastModifiedTime", fmtDate.format(lmo_t));
				map.put("sha256", sha256);
				synchronized (db) {
					String setsql = "set @@auto_increment_increment=2;";
					db.excute(setsql);
					setsql = "set @@auto_increment_offset=2;";
					db.excute(setsql);
					db.insert(filename, map);
				}
			}
			return FileVisitResult.CONTINUE;
		}

		// 开始访问目录时触发该方法
		public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
			count++;
			if (count > begin && count <= end) {
				String name;
				if (dir.getNameCount() > 0) {
					name = dir.getName(dir.getNameCount() - 1).toString();
				} else {
					name = dir.toString();//某盘根目录
				}
				String path = dir.toString();
				int deep=dir.getNameCount();
				long size = attrs.size();
				long space = (long) (Math.ceil(size / 1024.0));
				SimpleDateFormat fmtDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date cre_t = new Date(attrs.creationTime().toMillis());
				Date lac_t = new Date(attrs.lastAccessTime().toMillis());
				Date lmo_t = new Date(attrs.lastModifiedTime().toMillis());
				int subdir_num = 0;
				int subfile_num = 0;
				File[] files = dir.toFile().listFiles();
				for (File file : files) {
					if (file.isDirectory())
						subdir_num++;
					else
						subfile_num++;
				}
				String parent;
				long parent_id=0;
				while(count!=1){
					parent=dir.getParent().toString();
					
					List<Map> result;
					String[] fields = { "ID" };
					String[] tables = { folder };
					HashMap<String, String[]> param = new HashMap<String, String[]>();
					String[] where = { "path='" + DStr(parent) + "'" };
					param.put("where", where);
					try {
						result = db.select(fields, tables, param);
						if(result.size()!=0)
							parent_id=(long) result.get(0).get("ID");
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					if(parent_id!=0){
						break;
					}else{
						//爸爸在其他线程中还没插进来，等一会
						try {
							System.out.println("sleep");
							Thread.sleep(100);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("name", DStr(name));
				map.put("path", DStr(path));
				map.put("deep", String.valueOf(deep));
				map.put("parent_id",String.valueOf(parent_id));
				map.put("size", String.valueOf(size));
				map.put("space", String.valueOf(space));
				map.put("creationTime", fmtDate.format(cre_t));
				map.put("lastAccessTime", fmtDate.format(lac_t));
				map.put("lastModifiedTime", fmtDate.format(lmo_t));
				map.put("subdir_num", String.valueOf(subdir_num));
				map.put("subfile_num", String.valueOf(subfile_num));
				synchronized (db) {
					String setsql = "set @@auto_increment_increment=2;";
					db.excute(setsql);
					setsql = "set @@auto_increment_offset=1;";
					db.excute(setsql);
					db.insert(folder, map);
				}
			}
			return FileVisitResult.CONTINUE;
		}

		public FileVisitResult visitFileFailed(Path file, IOException exc) {
			System.out.println(exc);
			System.out.println(file);
			return FileVisitResult.CONTINUE;
		}
	}

	class Insert_thd0 extends Thread {
		private long begin;
		private long end;

		public Insert_thd0(long b, long e) {
			super();
			// TODO Auto-generated constructor stub
			begin = b;
			end = e;
		}

		public void run() {
			long startMili=System.currentTimeMillis();
			System.out.println("插入开始   "+begin+"   "+end);
			MyFileVisitor4 myFV4 = new MyFileVisitor4(begin, end);
			if (VisitModel.maxDe == -1) {
				try {
					Files.walkFileTree(Paths.get(VisitModel.path), myFV4);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				try {
					Files.walkFileTree(Paths.get(VisitModel.path), null, VisitModel.maxDe, myFV4);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			long endMili=System.currentTimeMillis();
			System.out.println("插入结束  " + begin + " " + end+"   耗时"+(endMili-startMili)+"毫秒");
		}
	}



	/*******内部类结束
	 * @throws SQLException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * @throws ClassNotFoundException **********/


	public VisitModel() {
		super();
		// TODO Auto-generated constructor stub
		NUM = 0;
		over = false;
	}

	public static String getFingerSHA256(File aFile) {
		if (!aFile.isFile()) {
			return null;
		}

		MessageDigest digest = null;
		FileInputStream in = null;
		byte buffer[] = new byte[1024];
		int len;
		try {
			digest = MessageDigest.getInstance("SHA-256");
			in = new FileInputStream(aFile);
			while ((len = in.read(buffer, 0, 1024)) != -1) {
				digest.update(buffer, 0, len);
			}
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		BigInteger bigInt = new BigInteger(1, digest.digest());
		return bigInt.toString(16);
	}

	public void filetodb(String pa, int de) {
		VisitModel fv = new VisitModel();
		VisitModel.path = pa;
		VisitModel.maxDe = de;
		MyFileVisitor3 myFV3 = new MyFileVisitor3();
		if (VisitModel.maxDe == -1) {
			try {
				Files.walkFileTree(Paths.get(VisitModel.path), myFV3);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			try {
				Files.walkFileTree(Paths.get(VisitModel.path), null, VisitModel.maxDe, myFV3);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println(NUM);
		long block = VisitModel.NUM / 4;
		Insert_thd0 insert_thd1 = new Insert_thd0(0, block);
		insert_thd1.start();
		Insert_thd0 insert_thd2 = new Insert_thd0(block, 2 * block);
		insert_thd2.start();
		Insert_thd0 insert_thd3 = new Insert_thd0(2 * block, 3 * block);
		insert_thd3.start();
		Insert_thd0 insert_thd4 = new Insert_thd0(3 * block, NUM);
		insert_thd4.start();
		while(true){
			if((!insert_thd1.isAlive())&&(!insert_thd2.isAlive())&&(!insert_thd3.isAlive())&&(!insert_thd4.isAlive())){
				over=true;
				break;
			}
		}		
	}
	
	void deep(long id,boolean first)
	//求指定id的东西的层数，调用时first写true
	{
		if(first)
			VisitModel.deepCount=0;
		else
			++VisitModel.deepCount;
		List<Map> result;
		long parent_id;
		String[] fields = { "parent_id" };
		String[] tables;
		if(id%2==0){
			 tables=new String[] { "file" };
		}
		else{
			tables=new String[] { "folder" };
		}
		HashMap<String, String[]> param = new HashMap<String, String[]>();
		String[] where = { "ID='" + id + "'" };
		param.put("where", where);
		try {
			result = this.db.select(fields, tables, param);
			parent_id=(long) result.get(0).get("parent_id");
			if(parent_id!=0)
				deep(parent_id,false);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	List<Map> limit_deep(long id, int limit,boolean forfile)
	//返回id目录下层数>limit的文件（forfile=true)或文件夹(forfile=false)
	{
		List<Map> result;
		List<Map> back = new ArrayList();
		String[] fields = { "ID" };
		String[] tables;
		HashMap<String, String[]> param = new HashMap<String, String[]>();
		String[] where;
		if(forfile){
			 tables=new String[] { "file" };
		}
		else{
			tables=new String[] { "folder" };
		}
		param.clear();
		where=new String[]{"parent_id='"+id+"'"};
		param.put("where", where);
		try {
			result = this.db.select(fields, tables, param);
			for(int i=0;i<result.size();++i)
			{
				deep((long) result.get(i).get("ID"),true);
				if(VisitModel.deepCount>limit)
				{
					System.out.println(result.get(i).get("ID"));
					back.add(result.get(i));
				}
			}
			result.clear();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		tables=new String[] { "folder" };
		try {
			result = this.db.select(fields, tables, param);
			for(int i=0;i<result.size();++i)
			{
				limit_deep((long) result.get(i).get("ID"),limit,forfile);
			}
			result.clear();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return back;
	}
}

