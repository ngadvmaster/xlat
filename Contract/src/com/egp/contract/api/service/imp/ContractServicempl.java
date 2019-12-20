package com.egp.contract.api.service.imp;

import java.sql.SQLException;
import java.util.List;

import com.egp.contract.api.dao.ContractDao;
import com.egp.contract.api.dao.imp.ContractDaoImpl;
import com.egp.contract.api.service.ContractService;

public class ContractServicempl implements ContractService {

	@Override
	public List<String> GetAll() throws SQLException {
		ContractDao dao = new ContractDaoImpl();
		return dao.GetAll();
	}

	@Override
	public String Get(int id) throws SQLException {
		ContractDao dao = new ContractDaoImpl();
		return dao.Get(id);
	}
}
