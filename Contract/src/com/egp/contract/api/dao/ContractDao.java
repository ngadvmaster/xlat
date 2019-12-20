package com.egp.contract.api.dao;

import java.sql.SQLException;
import java.util.List;

public abstract interface ContractDao {

	public abstract List<String> GetAll() throws SQLException;

	public abstract String Get(int id) throws SQLException;
}
