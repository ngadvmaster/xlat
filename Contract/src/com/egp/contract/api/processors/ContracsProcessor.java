package com.egp.contract.api.processors;

import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;

import com.egp.common.common.cp.BaseProcessor;
import com.egp.common.common.cp.Param;
import com.egp.common.response.ContractResponse;
import com.egp.common.statics.ErrorCode;
import com.egp.contract.api.service.ContractService;
import com.egp.contract.api.service.imp.ContractServicempl;
import com.egp.contract.api.utils.Conts;

public class ContracsProcessor implements BaseProcessor<HttpServletRequest, String> {
	private static final Logger logger = Logger.getLogger(Conts.LOG_FILE_NAME);

	@Override
	public String execute(Param<HttpServletRequest> param) {
		HttpServletRequest request = (HttpServletRequest) param.get();
		String strId = request.getParameter("id");
		if ((strId != null) && (strId != "")) {
			int id = Integer.parseInt(request.getParameter("id"));
			String userAgent = request.getHeader("user-agent");
			logger.debug("ContracsProcessor - userAgent: " + userAgent);
			ContractResponse res = new ContractResponse(false, ErrorCode.ERR_1001);
			try {
				ContractService contractService = new ContractServicempl();
				res.setData(contractService.Get(id));
				res.setErrorCode(ErrorCode.SUCCESS);
				res.setSuccess(true);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error(e);
			}

			logger.debug("Response ContracsProcessor: " + res.toJson());
			return res.toJson();
		} else {
			return "MISSING PARAMETTER";
		}

	}
}