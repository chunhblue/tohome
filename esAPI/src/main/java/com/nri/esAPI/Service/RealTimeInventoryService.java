package com.nri.esAPI.Service;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.text.ParseException;

import org.springframework.stereotype.Service;

@Service
public interface RealTimeInventoryService {
	/**
	    * inventory
	 * @param barcode 
	 * @param size 
	 * @param page 
	 * @param articleIdListJson 
	    * @param Store,articleId
	    * @return
	    */
	String GetRelTimeInventory(String StoreCd,String articleId,String topDepartment,
			String department,String category,String subCategory,String inEsTime, String page, String size, String articleIdListJson)  throws IOException, ParseException ;

    String SaveRelTimeInventory(String realTimeJson) throws IOException, ParseException;

    void SaveRelTimeInventoryByFaildata() throws IOException, ParseException, SQLException;
}
