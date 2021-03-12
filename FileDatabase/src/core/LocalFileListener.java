package core;



import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;
import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.InvocationTargetException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.security.MessageDigest;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//以下是用sql封装类的
public class LocalFileListener extends FileAlterationListenerAdaptor {

	private static View view = View.getInstance();
	
	private String folder = "mydir1";
	private String file = "myfile1";
	
	private static LocalFileListener listener;
	
	static public LocalFileListener getInstance(){
		if(!(listener instanceof LocalFileListener)){
			System.out.println("create listener");
			listener = new LocalFileListener();
		}
		return listener;
	}
	
	static public void init(){
		view = View.getInstance();
	}
	
	
	 private String DStr(String str){
		str = str.replaceAll("\\\\\\\\\\\\", "\\\\");
		str = str.replaceAll("\\\\\\\\", "\\\\");
		str = str.replaceAll("\\\\\'", "\'");
		str = str.replaceAll("\\\\", "\\\\\\\\");
		str = str.replaceAll("\'", "\\\\\'");
		return str;
	}
	
	
	private LocalFileListener() {
		super();
		// TODO Auto-generated constructor stub
	}

	private String getFingerSHA256(File aFile) {
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
	
	@Override
	public void onFileCreate(File file) {
		System.out.println("FSW: File Create " + file.getAbsolutePath());
		Path filepath = file.toPath();
		String name = filepath.getFileName().toString();
		String path = filepath.toString();
		int deep=filepath.getNameCount();
		BasicFileAttributeView basicView = Files.getFileAttributeView(filepath, BasicFileAttributeView.class);
		BasicFileAttributes attrs;
		try {
			attrs = basicView.readAttributes();
			long size = attrs.size();
			long space = (long) (Math.ceil(size / 1024.0));
			SimpleDateFormat fmtDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date cre_t = new Date(attrs.creationTime().toMillis());
			Date lac_t = new Date(attrs.lastAccessTime().toMillis());
			Date lmo_t = new Date(attrs.lastModifiedTime().toMillis());
			String sha256 = this.getFingerSHA256(file);
			String parent = filepath.getParent().toString();
			long parent_id = 0;
			List<Map> result;
			String[] fields = { "ID" };
			String[] tables = { this.folder };
			HashMap<String, String[]> param = new HashMap<String, String[]>();
			String[] where = { "path='" + DStr(parent) + "'" };
			param.put("where", where);
			result = fileMonitor.db.select(fields, tables, param);
			if(result.size()!=0)
				parent_id=(long) result.get(0).get("ID");
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
			synchronized (fileMonitor.db) {
				String setsql = "set @@auto_increment_increment=2;";
				fileMonitor.db.excute(setsql);
				setsql = "set @@auto_increment_offset=2;";
				fileMonitor.db.excute(setsql);
				fileMonitor.db.insert(this.file, map);
			}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			System.out.println(e1);
			e1.printStackTrace();
		}
	}

	@Override
	public void onFileChange(File file) {
		System.out.println("FSW: File Modify " + file.getAbsolutePath());
		Path filepath = file.toPath();
		String path = filepath.toString();
		BasicFileAttributeView basicView = Files.getFileAttributeView(filepath, BasicFileAttributeView.class);
		BasicFileAttributes attrs;
		try {
			attrs = basicView.readAttributes();
			long size = attrs.size();
			long space = (long) (Math.ceil(size / 1024.0));
			SimpleDateFormat fmtDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date cre_t = new Date(attrs.creationTime().toMillis());
			Date lac_t = new Date(attrs.lastAccessTime().toMillis());
			Date lmo_t = new Date(attrs.lastModifiedTime().toMillis());
			String sha256 = this.getFingerSHA256(file);
			HashMap<String, String> update = new HashMap<String, String>();
			update.put("size", String.valueOf(size));
			update.put("space", String.valueOf(space));
			update.put("creationTime", fmtDate.format(cre_t));
			update.put("lastAccessTime", fmtDate.format(lac_t));
			update.put("lastModifiedTime", fmtDate.format(lmo_t));
			update.put("sha256", sha256);
			String[] updateWhere = { "path='" + DStr(path) + "'" };
			fileMonitor.db.update_safe(this.file, updateWhere, update);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			//System.out.println(e1);
			//e1.printStackTrace();
		}
	}

	@Override
	public void onFileDelete(File file) {
		System.out.println("FSW: File Delete " + file.getAbsolutePath());
		Path filepath = file.toPath();
		String path = filepath.toString();
		try {
			String[] deleteWhere = { "path='" + DStr(path) + "'" };
			fileMonitor.db.delete(this.file, deleteWhere);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println(e);
			e.printStackTrace();
		}
	}

	@Override
	public void onDirectoryCreate(File dir) {
		System.out.println("FSW: Dir Create " + dir.getAbsolutePath());
		Path dirpath = dir.toPath();
		String name;
		if (dirpath.getNameCount() > 0) {
			name = dirpath.getName(dirpath.getNameCount() - 1).toString();
		} else {
			name = dirpath.toString();// 某盘根目录
		}
		String path = dirpath.toString();
		int deep=dirpath.getNameCount();
		BasicFileAttributeView basicView = Files.getFileAttributeView(dirpath, BasicFileAttributeView.class);
		BasicFileAttributes attrs;
		try {
			attrs = basicView.readAttributes();
			long size = attrs.size();
			long space = (long) (Math.ceil(size / 1024.0));
			SimpleDateFormat fmtDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date cre_t = new Date(attrs.creationTime().toMillis());
			Date lac_t = new Date(attrs.lastAccessTime().toMillis());
			Date lmo_t = new Date(attrs.lastModifiedTime().toMillis());
			int subdir_num = 0;
			int subfile_num = 0;
			File[] files = dir.listFiles();
			for (File f : files) {
				if (f.isDirectory())
					subdir_num++;
				else
					subfile_num++;
			}
			String parent = dirpath.getParent().toString();
			long parent_id = 0;
			List<Map> result;
			String[] fields = { "ID" };
			String[] tables = { this.folder };
			HashMap<String, String[]> param = new HashMap<String, String[]>();
			String[] where = { "path='" + DStr(parent) + "'" };
			param.put("where", where);
			result = fileMonitor.db.select(fields, tables, param);
			if(result.size()!=0)
				parent_id=(long) result.get(0).get("ID");
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
			map.put("subdir_num", String.valueOf(subdir_num));
			map.put("subfile_num", String.valueOf(subfile_num));
			synchronized (fileMonitor.db) {
				String setsql = "set @@auto_increment_increment=2;";
				fileMonitor.db.excute(setsql);
				setsql = "set @@auto_increment_offset=1;";
				fileMonitor.db.excute(setsql);
				fileMonitor.db.insert(this.folder, map);
			}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			System.out.println(e1);
			e1.printStackTrace();
		}
	}

	@Override
	public void onDirectoryChange(File dir) {
		System.out.println("FSW: Dir Modify " + dir.getAbsolutePath());
		Path dirpath = dir.toPath();
		String path = dirpath.toString();
		BasicFileAttributeView basicView = Files.getFileAttributeView(dirpath, BasicFileAttributeView.class);
		BasicFileAttributes attrs;
		try {
			attrs = basicView.readAttributes();
			long size = attrs.size();
			long space = (long) (Math.ceil(size / 1024.0));
			SimpleDateFormat fmtDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date cre_t = new Date(attrs.creationTime().toMillis());
			Date lac_t = new Date(attrs.lastAccessTime().toMillis());
			Date lmo_t = new Date(attrs.lastModifiedTime().toMillis());
			int subdir_num = 0;
			int subfile_num = 0;
			File[] files = dir.listFiles();
			for (File f : files) {
				if (f.isDirectory())
					subdir_num++;
				else
					subfile_num++;
			}
			HashMap<String, String> update = new HashMap<String, String>();
			update.put("size", String.valueOf(size));
			update.put("space", String.valueOf(space));
			update.put("creationTime", fmtDate.format(cre_t));
			update.put("lastAccessTime", fmtDate.format(lac_t));
			update.put("lastModifiedTime", fmtDate.format(lmo_t));
			update.put("subdir_num", String.valueOf(subdir_num));
			update.put("subfile_num", String.valueOf(subfile_num));
			String[] updateWhere = { "path='" + DStr(path) + "'" };
			fileMonitor.db.update_safe(this.folder, updateWhere, update);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			System.out.println(e1);
			e1.printStackTrace();
		}
	}

	@Override
	public void onDirectoryDelete(File dir) {
		System.out.println("FSW: Dir Delete " +dir.getAbsolutePath());
		Path dirpath = dir.toPath();
		String path = dirpath.toString();
		try {
			String[] deleteWhere = { "path='" + DStr(path) + "'" };
			fileMonitor.db.delete(this.folder, deleteWhere);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println(e);
			e.printStackTrace();
		}
	}
}