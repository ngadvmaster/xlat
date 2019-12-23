package com.egp.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.egp.web.service.HttpUtils;

@Controller
public class ContractController {

	@GetMapping("/contract")
	public String greeting(@RequestParam(name = "name", required = false, defaultValue = "World") String name, Model model) {
		String contract = name;

		// Call contract micro service to contract
		//String contractUrl = "http://localhost:8082/api?c=1";
		String contractUrl = "http://egp-contract-egp-test1.paas.xplat.fpt.com.vn/api?c=1";
		try {
			contract = HttpUtils.sendGet(contractUrl);
		} catch (Exception e) {
			e.printStackTrace();
			contract = e.getMessage();
		}

		model.addAttribute("name", contract);
		return "contract";
	}

}
