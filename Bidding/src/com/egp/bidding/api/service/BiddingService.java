package com.egp.bidding.api.service;

import java.sql.SQLException;
import java.util.List;

public abstract interface BiddingService {

	public abstract List<String> GetAll() throws SQLException;

	public abstract String Get(int id) throws SQLException;
}
