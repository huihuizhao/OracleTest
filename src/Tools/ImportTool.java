package Tools;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ImportTool {

	public static void main(String[] args) {
		System.out.println("Hello World!");
		// testOracle();
		writeData();
	}

	/**
	 * 一个非常标准的连接Oracle数据库的示例代码
	 */
	public static void testOracle() {
		Connection con = null;// 创建一个数据库连接
		PreparedStatement pre = null;// 创建预编译语句对象，一般都是用这个而不用Statement
		ResultSet result = null;// 创建一个结果集对象
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");// 加载Oracle驱动程序
			System.out.println("开始尝试连接数据库！");
			String url = "jdbc:oracle:" + "thin:@192.168.1.4:1521:ORACLEHH";// 127.0.0.1是本机地址，XE是精简版Oracle的默认数据库名
			// String user = "sys";// 用户名,系统默认的账户名
			String user = "sys as sysdba";// 用户名,系统默认的账户名

			String password = "1";// 你安装时选设置的密码
			con = DriverManager.getConnection(url, user, password);// 获取连接
			System.out.println("连接成功！");
			String sql = "select * from HANGCI where name=?";// 预编译语句，“？”代表参数
			pre = con.prepareStatement(sql);// 实例化预编译语句
			pre.setString(1, "201606热液与冷泉综合调查航次-1");// 设置参数，前面的1表示参数的索引，而不是表中列名的索引
			result = pre.executeQuery();// 执行查询，注意括号中不需要再加参数
			while (result.next())
				// 当结果集不为空时
				System.out.println("航次名称:" + result.getString("name") + "；   海域：" + result.getString("haiyu"));
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				// 逐一将上面的几个对象关闭，因为不关闭的话会影响性能、并且占用资源
				// 注意关闭的顺序，最后使用的最先关闭
				if (result != null)
					result.close();
				if (pre != null)
					pre.close();
				if (con != null)
					con.close();
				System.out.println("数据库连接已关闭！");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	// public static void writeData() {
	//
	// Connection con = null;// 创建一个数据库连接
	// PreparedStatement pre = null;// 创建预编译语句对象，一般都是用这个而不用Statement
	// int result = 0;// 创建一个结果集对象
	// try {
	// Class.forName("oracle.jdbc.driver.OracleDriver");// 加载Oracle驱动程序
	// System.out.println("开始尝试连接数据库！");
	// String url = "jdbc:oracle:" + "thin:@192.168.1.4:1521:ORACLEHH";//
	// 127.0.0.1是本机地址，XE是精简版Oracle的默认数据库名
	// // String user = "sys";// 用户名,系统默认的账户名
	// String user = "sys as sysdba";// 用户名,系统默认的账户名
	//
	// String password = "1";// 你安装时选设置的密码
	// con = DriverManager.getConnection(url, user, password);// 获取连接
	// System.out.println("连接成功！");
	// String sql = "UPDATE HANGCI SET NAME='201606热液与冷泉综合调查航次-1' WHERE ID=?";//
	// 预编译语句，“？”代表参数
	// pre = con.prepareStatement(sql);// 实例化预编译语句
	// pre.setString(1, "");// 设置参数，前面的1表示参数的索引，而不是表中列名的索引
	// result = pre.executeUpdate();// 执行查询，注意括号中不需要再加参数
	// // while (result.next())
	// // // 当结果集不为空时
	// // System.out.println("航次名称:" + result.getString("name") + "； 海域：" +
	// // result.getString("haiyu"));
	// } catch (Exception e) {
	// e.printStackTrace();
	// } finally {
	// try {
	// // 逐一将上面的几个对象关闭，因为不关闭的话会影响性能、并且占用资源
	// // 注意关闭的顺序，最后使用的最先关闭
	//// if (result != null)
	//// result.close();
	// if (pre != null)
	// pre.close();
	// if (con != null)
	// con.close();
	// System.out.println("数据库连接已关闭！");
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }
	//
	// }

	public static void writeData() {
		// 批量添加20000条数据用时8秒。
		try {
			String url = "jdbc:oracle:" + "thin:@192.168.1.4:1521:ORACLEHH"; //
			// orcl为数据库的SID
			String user = "sys as sysdba";
			String password = "1";
			StringBuffer sql = new StringBuffer();
			sql.append("insert into GUIJI (HANGCI) values (?)");
			// sql.append("UPDATE HANGCI SET NAME='201606热液与冷泉综合调查航次-1' WHERE
			// ID=''");
			Class.forName("oracle.jdbc.driver.OracleDriver");
			Connection con = (Connection) DriverManager.getConnection(url, user, password);
			// 关闭事务自动提交
			con.setAutoCommit(false);

			Long startTime = System.currentTimeMillis();
			PreparedStatement pst = (PreparedStatement) con.prepareStatement(sql.toString());
			// for (int i = 0; i < list.size(); i++) {
			// ExLog exLog = (ExLog) list.get(i);
			pst.setString(1, "201606热液与冷泉综合调查航次-1");
			// pst.setString(2, exLog.getExLogDate());
			// 把一个SQL命令加入命令列表
			pst.addBatch();
			// }
			// 执行批量更新
			pst.executeBatch();
			// 语句执行完毕，提交本事务
			con.commit();
			Long endTime = System.currentTimeMillis();
			System.out.println("用时：" + (endTime - startTime));
			pst.close();
			con.close();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

}
