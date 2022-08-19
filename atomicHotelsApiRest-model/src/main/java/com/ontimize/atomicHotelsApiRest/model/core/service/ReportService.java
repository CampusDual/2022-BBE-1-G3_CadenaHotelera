package com.ontimize.atomicHotelsApiRest.model.core.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.ontimize.atomicHotelsApiRest.api.core.exceptions.ValidateException;
import com.ontimize.atomicHotelsApiRest.api.core.service.IReportService;
import com.ontimize.atomicHotelsApiRest.model.core.dao.BillDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.HotelDao;
import com.ontimize.atomicHotelsApiRest.model.core.tools.ControlFields;
import com.ontimize.atomicHotelsApiRest.model.core.tools.EntityResultWrong;
import com.ontimize.atomicHotelsApiRest.model.core.tools.ErrorMessage;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.dto.EntityResultMapImpl;
import com.ontimize.jee.common.exceptions.OntimizeJEERuntimeException;
import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Service("ReportService")
@Lazy
public class ReportService implements IReportService{
	
	@Autowired
	private HotelService hotelService;

	@Autowired
	private DefaultOntimizeDaoHelper daoHelper;

	@Autowired
	ControlFields cf;

	@Override
	public EntityResult reportPruebaQuery(Map<String, Object> keyMap, List<String> attrList)
			throws OntimizeJEERuntimeException {
		EntityResult resultado = new EntityResultMapImpl();
		
		try {
			
		cf.reset();
		cf.addBasics(HotelDao.fields);
		cf.validate(keyMap);
		
		
		List<String> required = Arrays.asList(HotelDao.ATTR_ID,HotelDao.ATTR_NAME,HotelDao.ATTR_CITY);
		cf.reset();
		cf.addBasics(HotelDao.fields);
		cf.setRequired(required);
		cf.setOptional(false);
		cf.validate(attrList);
		
		resultado = hotelService.hotelQuery(keyMap, attrList);
		
		List<PruebaHoteles> a = new ArrayList<PruebaHoteles>();
		
		for(int i=0; i<resultado.calculateRecordNumber();i++) {
			Integer id = (Integer)resultado.getRecordValues(i).get(HotelDao.ATTR_ID);
			String name= (String)resultado.getRecordValues(i).get(HotelDao.ATTR_NAME);
			String city = (String)resultado.getRecordValues(i).get(HotelDao.ATTR_CITY);
			
			PruebaHoteles h = new PruebaHoteles(id,name,city);
			a.add(h);
		}		
		
		String filePath="..\\atomicHotelsApiRest-model\\src\\main\\resources\\reports\\Hotels_template.jrxml";
		 
		Map<String,Object> parameters = new HashMap<String,Object>(){{
			 put("hotels_title","HOTELES ATÓMICOS");
			 put("hotels_subtitle","Grupo Cadena de Hoteles Atómicos");
		 }};
		 
//		 JRDataSource data = new JRDataSource().getFieldValue(resultado);
		 
		 JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(a);
		
//		JRDataSource dataSource = new JRDataSource(new EntityResultDataSource(resultado).getFields());
		 
//		 JRField jrField = res
//				 
//		 JRDataSource dataSource = new JRDataSource(jrField);
//		
//		JRMapArrayDataSource dataSource = new JRMapArrayDataSource(
//                new Object[] { fuelSalesReportInputMO.getDataSources() });
	
		
		JasperReport jasperReport = JasperCompileManager.compileReport(filePath);

        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters,dataSource);

        JasperExportManager.exportReportToPdfFile(jasperPrint,"..\\atomicHotelsApiRest-model\\src\\main\\resources\\reports\\Hotels_report.pdf");
		
		}catch(ValidateException e) {
			e.printStackTrace();
			resultado= new EntityResultWrong(e.getMessage());
		}catch(Exception e) {
			e.printStackTrace();
			resultado= new EntityResultWrong(ErrorMessage.UNKNOWN_ERROR);
		}
		
		return resultado;
	}
}
