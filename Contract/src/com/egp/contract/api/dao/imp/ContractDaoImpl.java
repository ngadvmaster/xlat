package com.egp.contract.api.dao.imp;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.egp.common.pools.ConnectionPool;
import com.egp.contract.api.dao.ContractDao;

public class ContractDaoImpl implements ContractDao {

	@Override
	public List<String> GetAll() throws SQLException {
		List<String> res = new ArrayList<String>();
		Connection conn = ConnectionPool.getInstance().getConnection(ConnectionPool.USER_POOL);
		try {
			String sql = "SELECT name FROM contracs";
			final PreparedStatement stm = conn.prepareStatement(sql);
			ResultSet rs = stm.executeQuery();
			while (rs.next()) {
				res.add(rs.getString("name"));
			}
			rs.close();
			stm.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (conn != null) {
				conn.close();
			}
		}
		return res;
	}

	@Override
	public String Get(int id) throws SQLException {
		String res = "null";
		Connection conn = ConnectionPool.getInstance().getConnection(ConnectionPool.USER_POOL);
		try {
			String sql = "SELECT name FROM contracs WHERE id=?";
			PreparedStatement stm = (PreparedStatement) conn.prepareStatement(sql);
			stm.setInt(1, id);
			ResultSet rs = stm.executeQuery();
			if (rs.next()) {
				return rs.getString("name");
			}
			rs.close();
			stm.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (conn != null) {
				conn.close();
			}
		}
		return res;
	}
}
