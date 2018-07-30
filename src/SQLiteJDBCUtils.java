import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * author: cc
 * 
 * date: 2018年7月11日 下午7:02:50
 *
 * desc：
 **/

public class SQLiteJDBCUtils {

	private static String path = "/Users/cc/Documents/code/GitHub/LoginputDB/user.db";

	 static Connection c;
	 static Statement stmt;

	public static void main(String[] args) {
		//count();
		readTxt();
		//insertTxt();
		//deleteTxt();
		//deleteStr();
	}

	public static void before() {
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:" + path);
			System.out.println("Opened database successfully");
			stmt = c.createStatement();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void after() {
		try {
			stmt.close();
			c.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 读取txt 写入db
	 */
	public static void count() {
		try {
			if (stmt == null)
				before();
			Map<String, String> datamap = new HashMap<>();
			ResultSet rs = stmt.executeQuery("select * from 自然码;");
			if (rs != null) {
				while (rs.next()) {
					String Word = rs.getString("Word");
					datamap.put(Word, "0");
				}
			}
			System.out.println("datamap.size():" + datamap.size());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 读取txt 写入db
	 */
	public static void readTxt() {
		try {
			if (stmt == null)
				before();
			String dir = "/Users/cc/Documents/搜狗词库to落格/"; // 绝对路径
			// String fileArr []=
			// {"搜狗词库_自然码.txt","通讯录_自然码.txt","日常用语大词库_自然码.txt","成语_自然码.txt"};
			// String fileArr []= {"搜狗词库_自然码.txt","通讯录_自然码.txt"};
			 String fileArr[] = { "通讯录_自然码.txt" };
			//String fileArr[] = { "最常用的10000个英文单词.txt" };
			int index = 0;
			for (String fileName : fileArr) {
				if (fileName == null || fileName.isEmpty())
					continue;
				File filename = new File(dir + fileName);
				InputStreamReader reader = new InputStreamReader(new FileInputStream(filename), "UTF-8");
				@SuppressWarnings("resource")
				BufferedReader br = new BufferedReader(reader);
				String line = "";
				line = br.readLine();
				// 已有数据
				Map<String, String> datamap = new HashMap<>();
				ResultSet rs = stmt.executeQuery("select * from 自然码;");
				if (rs != null) {
					while (rs.next()) {
						String Word = rs.getString("Word");
						datamap.put(Word, "0");
					}
				}
				System.out.println(fileName + ",datamap.size():" + datamap.size());

				StringBuffer buffer = new StringBuffer();
				c.setAutoCommit(false);
				while (line != null) {
					line = br.readLine(); // 一行数据
					if (line != null) {
						String arr[] = line.split("	");
						if (datamap.get(arr[0]) == null && arr[1] != null) {
							buffer.setLength(0);
							buffer.append("insert into 自然码 (Py,Word,Weight,New) values ('" + arr[1].toLowerCase()
									+ "','" + arr[0] + "',0,0);");
							stmt.executeUpdate(buffer.toString());
							if (index % 10000 == 0) {
								c.commit();
								System.out.println(dateToStr(null, null) + fileName + ",插入数量:" + index);
							}
							index++;
						} else {
							// System.out.println(dateToStr(null, null) +
							// ",插入数据已存在:" + arr[0]);
						}
					}
				}
			}
			c.commit();
			System.out.println(dateToStr(null, null) + ",完成数量:" + index);
			after();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 读取txt 删除db
	 */
	public static void deleteTxt() {
		try {
			if (stmt == null)
				before();
			String pathname = "E:/OneDrive/文档/双拼/搜狗词库to落格/通讯录_自然码.txt"; // 绝对路径
			File filename = new File(pathname);
			InputStreamReader reader = new InputStreamReader(new FileInputStream(filename), "UTF-8");
			@SuppressWarnings("resource")
			BufferedReader br = new BufferedReader(reader);
			String line = "";
			line = br.readLine();
			// 已有数据
			Map<String, String> datamap = new HashMap<>();
			ResultSet rs = stmt.executeQuery("select * from 自然码;");
			if (rs != null) {
				while (rs.next()) {
					String Word = rs.getString("Word");
					datamap.put(Word, "0");
				}
			}
			System.out.println("datamap.size():" + datamap.size());
			int index = 0;
			StringBuffer buffer = new StringBuffer();
			c.setAutoCommit(false);
			while (line != null) {
				line = br.readLine(); // 一行数据
				if (line != null) {
					String arr[] = line.split("	");
					if (datamap.get(arr[0]) != null) {
						buffer.setLength(0);
						buffer.append("delete from 自然码  where Word='" + arr[0] + "';");
						stmt.executeUpdate(buffer.toString());
						c.commit();
						System.out.println(dateToStr(null, null) + ",name:" + arr[0] + ",删除数量:" + index);
						index++;
					}
				}
			}
			c.commit();
			System.out.println(dateToStr(null, null) + ",完成数量:" + index);
			after();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 读取String 删除db
	 */
	public static void deleteStr() {
		try {
			if (stmt == null)
				before();
			// 已有数据
			Map<String, String> datamap = new HashMap<>();
			ResultSet rs = stmt.executeQuery("select * from 自然码;");
			if (rs != null) {
				while (rs.next()) {
					String Word = rs.getString("Word");
					datamap.put(Word, "0");
				}
			}
			System.out.println("datamap.size():" + datamap.size());
			String strArr[] = { "ko", "intitle:", "quan", "Shadowrocket" };
			int index = 0;
			StringBuffer buffer = new StringBuffer();
			c.setAutoCommit(false);
			for (String str : strArr) {
				if (str == null || str.isEmpty())
					continue;
				if (datamap.get(str) != null) {
					buffer.setLength(0);
					buffer.append("delete from 自然码  where Word='" + str + "' or Py='" + str + "';");
					stmt.executeUpdate(buffer.toString());
					c.commit();
					System.out.println(dateToStr(null, null) + ",name:" + str + ",删除数量:" + index);
					index++;
				}
			}
			c.commit();
			System.out.println(dateToStr(null, null) + ",完成数量:" + index);
			after();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 写入txt
	 */
	public static void insertTxt() {
		try {
			if (stmt == null)
				before();
			File writename = new File("E:/OneDrive/文档/双拼/落格输入法导出词库.txt"); // 相对路径，如果没有则要建立一个新的output。txt文件
			writename.createNewFile(); // 创建新文件
			BufferedWriter out = new BufferedWriter(new FileWriter(writename));
			// out.write("我会写入文件啦\r\n"); // \r\n即为换行
			ResultSet rs = stmt.executeQuery("select * from 自然码 order by Weight desc;");
			int index = 0;
			System.out.println(dateToStr(null, null) + "写入开始");
			if (rs != null) {
				StringBuffer buffer = new StringBuffer();
				while (rs.next()) {
					String Word = rs.getString("Word");
					String Py = rs.getString("Py");
					String Weight = rs.getString("Weight");
					buffer.setLength(0);
					buffer.append(Word + "	" + Py + "	" + Weight);
					out.write(buffer.toString() + "\r\n");
					if (index % 10000 == 0)
						System.out.println(dateToStr(null, null) + ",写入数量:" + index);
					index++;
				}
			}
			System.out.println(dateToStr(null, null) + "写入完成数量index:" + index);
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 时间转字符串
	 */
	public static String dateToStr(Object date, String pattern) {
		if (date == null)
			date = new Date();
		if (pattern == null)
			pattern = "yyyy-MM-dd HH:mm:ss:SSS";
		SimpleDateFormat sf = new SimpleDateFormat(pattern);
		try {
			return sf.format(date);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
