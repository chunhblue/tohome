package com.nri.esAPI;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.nri.esAPI.Service.RealTimeInventoryService;

@ResponseBody
@RestController
public class MyController {

	@Autowired
	private RealTimeInventoryService realTimeInventoryService;
	
	@RequestMapping("/GetRelTimeInventory/{storeCd}/{articleId}/{topDepartment}/{department}/{category}/{subCategory}/{inEsTime}/{page}/{size}")
	public String GetRelTimeInventory(
			@PathVariable("storeCd") String storeCd,
			@PathVariable("articleId") String articleId,
			@PathVariable("topDepartment") String topDepartment,
			@PathVariable("department") String department,
			@PathVariable("category") String category,
			@PathVariable("subCategory") String subCategory,
			@PathVariable("inEsTime")String inEsTime,
			@PathVariable("page")String page,
			@PathVariable("size")String size,
			String articleIdListJson) throws IOException, ParseException{
		return realTimeInventoryService.GetRelTimeInventory(storeCd, articleId,topDepartment,department,category,subCategory,inEsTime,page,size,articleIdListJson);
	}
	
	@RequestMapping("/SaveRelTimeInventory")
	public String SaveRelTimeInventory(String realTimeJson) throws IOException, ParseException {
	    return realTimeInventoryService.SaveRelTimeInventory(realTimeJson);
    }

    @Configuration
	@EnableScheduling
	public class Task {
		/**
		 *每10分钟执行一次
		 */
		@Scheduled(cron = "0 0/10 * * * ?")
		public void run() throws IOException, ParseException, SQLException {
			Date date =new Date(System.currentTimeMillis());
			System.out.println("task定时任务" + date);
			realTimeInventoryService.SaveRelTimeInventoryByFaildata();
		}
	}
}
