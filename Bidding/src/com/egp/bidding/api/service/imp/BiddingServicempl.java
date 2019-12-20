package com.egp.bidding.api.service.imp;

import java.sql.SQLException;
import java.util.List;

import com.egp.bidding.api.dao.BiddingDao;
import com.egp.bidding.api.dao.imp.BiddingDaoImpl;
import com.egp.bidding.api.service.BiddingService;

public class BiddingServicempl implements BiddingService {

	@Override
	public List<String> GetAll() throws SQLException {		
		BiddingDao dao = new BiddingDaoImpl();		
		return dao.GetAll();
	}

	@Override
	public String Get(int id) throws SQLException {
		BiddingDao dao = new BiddingDaoImpl();		
		return dao.Get(id);
	}	
}
