package com.egp.bidding.api.processors;

import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;

import com.egp.bidding.api.service.BiddingService;
import com.egp.bidding.api.service.imp.BiddingServicempl;
import com.egp.bidding.api.utils.Conts;
import com.egp.common.common.cp.BaseProcessor;
import com.egp.common.common.cp.Param;
import com.egp.common.response.BiddingAllResponse;
import com.egp.common.statics.ErrorCode;

public class BiddingAllProcessor implements BaseProcessor<HttpServletRequest, String> {

	private static final Logger logger = Logger.getLogger(Conts.LOG_FILE_NAME);

	@Override
	public String execute(Param<HttpServletRequest> param) {
		HttpServletRequest request = (HttpServletRequest) param.get();
		String userAgent = request.getHeader("user-agent");
		logger.debug("BiddingAllProcessor - userAgent: " +userAgent);
		BiddingAllResponse res = new BiddingAllResponse(false, ErrorCode.ERR_1001);
		try {
			BiddingService biddingService = new BiddingServicempl();
			res.setDatas(biddingService.GetAll());
			res.setErrorCode(ErrorCode.SUCCESS);
			res.setSuccess(true);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
		}

		logger.debug("Response BiddingAllProcessor: " + res.toJson());
		return res.toJson();
	}
}