package net.xgs.query.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.jfinal.plugin.activerecord.ICallback;

public class SqlExecute implements ICallback {

	private String sql;
	//FIXME 修正带参数的sql语句
	@SuppressWarnings("unused")
	private Object[] params;
	public  Object result = null;
	@Override
	public Object call(Connection conn) throws SQLException {
		PreparedStatement pst = null;
		try {
			pst = conn.prepareStatement(sql);
			pst.execute();
			return null;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(pst != null){
				pst.close();
			}
		}
		return null;
	}
	public SqlExecute(String sql){
		this.sql = sql;
	}

	public SqlExecute(String sql,Object...params){
		this.params = params;
		this.sql = sql;
	}
}
