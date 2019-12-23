package com.egp.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.egp.web.service.HttpUtils;

@Controller
public class BiddingController {

	@GetMapping("/bidding")
	public String greeting(@RequestParam(name="name", required=false, defaultValue="Bidding controller") String name, Model model) {
		String biddings= name;	
		
		// Call bidding micro service to biddings
		//String biddingUrl = "http://localhost:8081/api?c=1";
		String biddingUrl = "http://egp-bidding-egp-test1.paas.xplat.fpt.com.vn/api?c=1";		
		try {
			biddings = HttpUtils.sendGet(biddingUrl);
		} catch (Exception e) {			
			e.printStackTrace();
			biddings = e.getMessage();
		}		
		
		model.addAttribute("name", biddings);
		return "bidding";
	}
}
