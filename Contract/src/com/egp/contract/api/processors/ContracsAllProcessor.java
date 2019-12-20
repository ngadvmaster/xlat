package com.egp.contract.api.processors;

import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;

import com.egp.common.common.cp.BaseProcessor;
import com.egp.common.common.cp.Param;
import com.egp.common.response.ContractAllResponse;
import com.egp.common.statics.ErrorCode;
import com.egp.contract.api.service.ContractService;
import com.egp.contract.api.service.imp.ContractServicempl;
import com.egp.contract.api.utils.Conts;

public class ContracsAllProcessor implements BaseProcessor<HttpServletRequest, String> {

	private static final Logger logger = Logger.getLogger(Conts.LOG_FILE_NAME);

	@Override
	public String execute(Param<HttpServletRequest> param) {
		HttpServletRequest request = (HttpServletRequest) param.get();
		String userAgent = request.getHeader("user-agent");
		logger.debug("ContracsAllProcessor - userAgent: " + userAgent);
		ContractAllResponse res = new ContractAllResponse(false, ErrorCode.ERR_1001);
		try {
			ContractService contractService = new ContractServicempl();
			res.setDatas(contractService.GetAll());
			res.setErrorCode(ErrorCode.SUCCESS);
			res.setSuccess(true);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
		}

		logger.debug("Response ContractAllResponse: " + res.toJson());
		return res.toJson();
	}
}