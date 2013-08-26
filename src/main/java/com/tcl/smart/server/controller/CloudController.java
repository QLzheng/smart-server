package com.tcl.smart.server.controller;

import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tcl.smart.server.cloud.bean.VideoList;


import ch.qos.logback.classic.Logger;


@RequestMapping("/v2/*")
@Controller
public class CloudController {
	private Logger logger = (Logger) LoggerFactory.getLogger(CloudController.class);
	
	/*test deploy*/
	@RequestMapping(value="info",method = RequestMethod.GET)
	public @ResponseBody String test() {
		StringBuffer sb = new StringBuffer();
		sb.append("<h1><b>Deploy Success. Version 1.0.5.</b></h1><br />");
		sb.append("<p><font color=\"red\">method_1:</font>train  <font color=\"red\">param:</font>delay_hours <font color=\"red\">descrip:</font>train immediately without delay_hours,or delay integer value hours.</p>");
		sb.append("<p><font color=\"red\">method_2:</font>train1 <font color=\"red\">descrip:</font>train single time. </p>");
		sb.append("<p><font color=\"red\">method_3:</font>ftptrain <font color=\"red\">param:</font>delay_hours </p>");
		return sb.toString();
	}
	
	@RequestMapping(value="report/video",method = RequestMethod.POST)
	public @ResponseBody String getVideo(@RequestBody VideoList video){
		logger.debug(video.toString());
		return "success";
	}
	
}
