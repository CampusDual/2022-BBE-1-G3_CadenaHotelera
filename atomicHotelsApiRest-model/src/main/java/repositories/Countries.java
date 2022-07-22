package repositories;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ontimize.atomicHotelsApiRest.api.core.service.ICountryService;
import com.ontimize.atomicHotelsApiRest.model.core.dao.CountryDao;
import com.ontimize.jee.common.dto.EntityResult;


public class Countries {
	
	@Autowired
	ICountryService countryService;
	
	private Map<String, String> map= null;
	
	private static Countries instance = null;

	private Countries() {
		List<String> attrList = new ArrayList<>() {{
			add(CountryDao.ATTR_ISO);
			add(CountryDao.ATTR_NAME);
		}};
		EntityResult er = countryService.countryQuery(new HashMap<>(), attrList);
		map = new HashMap<>();				
		
		for(int i = 0 ; i < er.calculateRecordNumber();i++) {
			System.out.println(er.getRecordValues(i));
			map.put((String) er.getRecordValues(i).get(CountryDao.ATTR_ISO),(String) er.getRecordValues(i).get(CountryDao.ATTR_NAME));		
		}				
		System.out.println();
		System.out.println("****************");
	}

	public static Countries instance() {
		if (instance == null) {
			instance = new Countries();			
		}
		return instance;
	}
	
	public Map<String, String> getMap(){
		return map;
	}
	

}
