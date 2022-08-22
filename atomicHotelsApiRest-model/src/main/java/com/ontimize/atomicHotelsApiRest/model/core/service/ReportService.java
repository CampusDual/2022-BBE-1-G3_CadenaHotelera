package com.ontimize.atomicHotelsApiRest.model.core.service;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bouncycastle.util.Bytes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;


import com.ontimize.atomicHotelsApiRest.api.core.exceptions.ValidateException;
import com.ontimize.atomicHotelsApiRest.api.core.service.IReportService;
import com.ontimize.atomicHotelsApiRest.model.core.dao.BillDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.HotelDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.ReceiptDao;
import com.ontimize.atomicHotelsApiRest.model.core.tools.ControlFields;
import com.ontimize.atomicHotelsApiRest.model.core.tools.EntityResultUtils;
import com.ontimize.atomicHotelsApiRest.model.core.tools.EntityResultWrong;
import com.ontimize.atomicHotelsApiRest.model.core.tools.ErrorMessage;
import com.ontimize.atomicHotelsApiRest.model.core.tools.ReportsConfig;
import com.ontimize.atomicHotelsApiRest.model.core.tools.TypeCodes.type;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.dto.EntityResultMapImpl;
import com.ontimize.jee.common.exceptions.OntimizeJEERuntimeException;
import com.ontimize.jee.common.security.PermissionsProviderSecured;
import com.ontimize.jee.common.util.remote.BytesBlock;
import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.data.JRTableModelDataSource;
import net.sf.jasperreports.engine.type.OrientationEnum;

@Service("ReportService")
@Lazy
public class ReportService implements IReportService {

	@Autowired
	private HotelService hotelService;
	
	@Autowired
	private StatisticsService statisticsService;
	
	@Autowired
	private ReceiptService receiptService;

	@Autowired
	private DefaultOntimizeDaoHelper daoHelper;

	@Autowired
	ControlFields cf;
	
	private final String HOTEL_TEMPLATE_PATH = "..\\atomicHotelsApiRest-model\\src\\main\\resources\\reports\\plantilla.jrxml";
	private final String HOTEL_TEMPLATE_01_PATH = "..\\atomicHotelsApiRest-model\\src\\main\\resources\\reports\\Hotels_template.jrxml";
	private final String HOTEL_TEMPLATE_02_PATH = "..\\atomicHotelsApiRest-model\\src\\main\\resources\\reports\\Hotels_template2.jrxml";
	private final String HOTEL_TEMPLATE_03_PATH = "..\\atomicHotelsApiRest-model\\src\\main\\resources\\reports\\Hotels_template3.jrxml";
	private final String PRUEBA_PATH = "..\\atomicHotelsApiRest-model\\src\\main\\resources\\reports\\prueba.jrxml";
	private final String INCOME_VS_EXPENSES_CHART = "..\\atomicHotelsApiRest-model\\src\\main\\resources\\reports\\incomeVsExpensesChart.jrxml";
	private final String RECEIPT = "..\\atomicHotelsApiRest-model\\src\\main\\resources\\reports\\Receipt_template.jrxml";
	private final String EXTRA_SERVICES = "..\\atomicHotelsApiRest-model\\src\\main\\resources\\reports\\Extra_services_template.jrxml";

	@Override
	public ResponseEntity test(Map<String, Object> keyMap, List<String> attrList) {
		EntityResult consulta = new EntityResultMapImpl();
		ResponseEntity resultado;
		try {

			cf.reset();
			cf.addBasics(HotelDao.fields);
			cf.validate(keyMap);

			List<String> required = Arrays.asList(HotelDao.ATTR_ID, HotelDao.ATTR_NAME, HotelDao.ATTR_CITY);
			cf.reset();
			cf.addBasics(HotelDao.fields);
//			cf.setRequired(required);
//			cf.setOptional(false);
			cf.validate(attrList);

			consulta = hotelService.hotelQuery(keyMap, attrList);

			List<PruebaHoteles> a = new ArrayList<PruebaHoteles>();

//			for (int i = 0; i < consulta.calculateRecordNumber(); i++) {
//				Integer id = (Integer) consulta.getRecordValues(i).get(HotelDao.ATTR_ID);
//				String name = (String) consulta.getRecordValues(i).get(HotelDao.ATTR_NAME);
//				String city = (String) consulta.getRecordValues(i).get(HotelDao.ATTR_CITY);
//
//				PruebaHoteles h = new PruebaHoteles(id, name, city);
//				a.add(h);
//			}

			String fotoPath = "..\\atomicHotelsApiRest-model\\src\\main\\resources\\reports\\images\\atom.png";

			Files.readAllBytes(Paths.get(fotoPath));
//			
			Map<String, Object> parameters = new HashMap<String, Object>() {
				{
					put("hotels_title", "HOTELES ATÓMICOS");
					put("hotels_subtitle", "Grupo Cadena de Hoteles Atómicos");
					put("foto",new ByteArrayInputStream(Files.readAllBytes(Paths.get(fotoPath))));
					put("foto2",Paths.get(fotoPath));
//					put("foto",Files.readAllBytes(Paths.get(fotoPath)));
				}
			};
//			
//			JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(a);
            
			JRTableModelDataSource dataSource = new JRTableModelDataSource(EntityResultUtils.createTableModel(consulta));			
			JasperReport jasperReport = JasperCompileManager.compileReport(HOTEL_TEMPLATE_01_PATH);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

			resultado = returnFile(JasperExportManager.exportReportToPdf(jasperPrint));

		} catch (ValidateException e) {
			e.printStackTrace();
			resultado = null;
		} catch (Exception e) {
			e.printStackTrace();
			resultado = null;
		}
		return resultado;

	}
	
	@Override
	public ResponseEntity plantilla(Map<String, Object> keyMap, List<String> attrList) {
		EntityResult consulta = new EntityResultMapImpl();
		ResponseEntity resultado;
		try {
			
			cf.reset();
			cf.addBasics(HotelDao.fields);
			cf.validate(keyMap);
			
			List<String> required = Arrays.asList(HotelDao.ATTR_ID, HotelDao.ATTR_NAME, HotelDao.ATTR_CITY);
			cf.reset();
			cf.addBasics(HotelDao.fields);
//			cf.setRequired(required);
//			cf.setOptional(false);
			cf.validate(attrList);
			
			consulta = hotelService.hotelQuery(keyMap, attrList);
			
			List<PruebaHoteles> a = new ArrayList<PruebaHoteles>();
			
			for (int i = 0; i < consulta.calculateRecordNumber(); i++) {
				Integer id = (Integer) consulta.getRecordValues(i).get(HotelDao.ATTR_ID);
				String name = (String) consulta.getRecordValues(i).get(HotelDao.ATTR_NAME);
				String city = (String) consulta.getRecordValues(i).get(HotelDao.ATTR_CITY);
				
				PruebaHoteles h = new PruebaHoteles(id, name, city);
				a.add(h);
			}
			
			
			JRTableModelDataSource dataSource = new JRTableModelDataSource(EntityResultUtils.createTableModel(consulta));			
			JasperReport jasperReport = JasperCompileManager.compileReport(HOTEL_TEMPLATE_PATH);
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, ReportsConfig.getBasicParameters(), dataSource);
			
			resultado = returnFile(JasperExportManager.exportReportToPdf(jasperPrint));
			
		} catch (ValidateException e) {
			e.printStackTrace();
			resultado = null;
		} catch (Exception e) {
			e.printStackTrace();
			resultado = null;
		}
		return resultado;
		
	}
	
	@Override
	public ResponseEntity incomeVsExpensesChart(Map<String, Object> keyMap, List<String> attrList) throws OntimizeJEERuntimeException {
		EntityResult consulta = new EntityResultMapImpl();
		ResponseEntity resultado;
		try {
			
			List<String> required = new ArrayList<String>() {
				{
					add(HotelDao.ATTR_FROM);
					add(HotelDao.ATTR_TO);
				}
			};

			Map<String, type> fields = new HashMap<String, type>() {
				{
					put(HotelDao.ATTR_ID, type.INTEGER);
					put(HotelDao.ATTR_FROM, type.DATE);
					put(HotelDao.ATTR_TO, type.DATE);
				}
			};

			cf.reset();
			cf.addBasics(fields);
			cf.setRequired(required);
			cf.validate(keyMap);
	
			consulta = statisticsService.incomeVsExpensesByHotelQuery(keyMap, new ArrayList<String>());

//			List<BenefitsBean> a = new ArrayList<BenefitsBean>();

//			for (int i = 0; i < consulta.calculateRecordNumber(); i++) {
//				BigDecimal expenses = (BigDecimal) consulta.getRecordValues(i).get("total_expenses");
//				BigDecimal income = (BigDecimal) consulta.getRecordValues(i).get("total_income");
//				String hotel = (String) consulta.getRecordValues(i).get(HotelDao.ATTR_NAME);
//
//				BenefitsBean h = new BenefitsBean(hotel, expenses,income);
//				a.add(h);
//			}

			//			JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(a);
			EntityResult consultaCategorizada = new EntityResultMapImpl();
				for(int i = 0; i <consulta.calculateRecordNumber();i++) {
				HashMap<String,Object> auxMap = new HashMap<>();
				auxMap.put("htl_id", consulta.getRecordValues(i).get("htl_id"));
				auxMap.put("htl_name", consulta.getRecordValues(i).get("htl_name"));
				
				auxMap.put("serie", "total_income");
				auxMap.put("value", consulta.getRecordValues(i).get("total_income"));
				
				consultaCategorizada.addRecord(auxMap);
				
				auxMap.put("serie", "total_expenses");
				auxMap.put("value", consulta.getRecordValues(i).get("total_expenses"));
				
				consultaCategorizada.addRecord(auxMap);
			
			}
			
			System.err.println(consultaCategorizada);
			JRTableModelDataSource dataSource = new JRTableModelDataSource(EntityResultUtils.createTableModel(consultaCategorizada));
			JasperReport jasperReport = JasperCompileManager.compileReport(INCOME_VS_EXPENSES_CHART);
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, ReportsConfig.getBasicParameters(), dataSource);
			jasperPrint.setOrientation(OrientationEnum.LANDSCAPE);
			resultado = returnFile(JasperExportManager.exportReportToPdf(jasperPrint));

		} catch (ValidateException e) {
			e.printStackTrace();
			resultado = null;
		} catch (Exception e) {
			e.printStackTrace();
			resultado = null;
		}
		return resultado;
	}
	
	@Override //TODO hacer recibos
	public ResponseEntity receipt(Map<String, Object> keyMap, List<String> attrList) throws OntimizeJEERuntimeException {
		EntityResult consulta = new EntityResultMapImpl();
		ResponseEntity resultado;
		try {
			
			List<String> required = new ArrayList<String>() {
				{
					add(ReceiptDao.ATTR_ID);
				}
			};

			cf.reset();
			cf.addBasics(ReceiptDao.fields);
			cf.setRequired(required);
			cf.setOptional(false);
			cf.validate(keyMap);
	
			consulta = receiptService.completeReceiptQuery(keyMap, new ArrayList<String>());

//			EntityResult consultaCategorizada = new EntityResultMapImpl();
//				for(int i = 0; i <consulta.calculateRecordNumber();i++) {
//				HashMap<String,Object> auxMap = new HashMap<>();
//				auxMap.put("htl_id", consulta.getRecordValues(i).get("htl_id"));
//				auxMap.put("htl_name", consulta.getRecordValues(i).get("htl_name"));
//				
//				auxMap.put("serie", "total_income");
//				auxMap.put("value", consulta.getRecordValues(i).get("total_income"));
//				
//				consultaCategorizada.addRecord(auxMap);
//				
//				auxMap.put("serie", "total_expenses");
//				auxMap.put("value", consulta.getRecordValues(i).get("total_expenses"));
//				
//				consultaCategorizada.addRecord(auxMap);
//			
//			}
			
//			Map<String,Object> consultaServicios= new HashMap<String,Object>();
			
			EntityResult servciosExtra = new EntityResultMapImpl();
			List<Object> lista = (List<Object>)consulta.getRecordValues(0).get("extra_services");
			for(Object a:lista) {
				servciosExtra.addRecord((HashMap<String,Object>)a);
			}
			
//			List<Object> listaServciosExtra = new ArrayList<Object>();
//			for (int i = 0; i < ((List<Object>)consulta.getRecordValues(0).get("extra_services")).size(); i++) {
//				Object h = ((List<Object>)consulta.getRecordValues(0).get("extra_services")).get(i);
//				listaServciosExtra.add(h);
//			}
			
//			consultaServicios.put("extra_services", listaServciosExtra);
//			
//			
//			servciosExtra.addRecord(consultaServicios);
			
			JRTableModelDataSource dataSourceServices = new JRTableModelDataSource(EntityResultUtils.createTableModel(servciosExtra));
			JasperReport jasperReportServices = JasperCompileManager.compileReport(EXTRA_SERVICES);
     		JasperPrint jasperPrintServices= JasperFillManager.fillReport(jasperReportServices, new HashMap<String,Object>(), dataSourceServices);
//			
			Map<String,Object> subReport = new HashMap<String,Object>(){{
				put("subReport",jasperPrintServices);
			}};
			
			System.err.println(consulta);
			JRTableModelDataSource dataSource = new JRTableModelDataSource(EntityResultUtils.createTableModel(consulta));
			JasperReport jasperReport = JasperCompileManager.compileReport(RECEIPT);
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, subReport, dataSource);
			jasperPrint.setOrientation(OrientationEnum.LANDSCAPE);
			
			resultado = returnFile(JasperExportManager.exportReportToPdf(jasperPrint));

		} catch (ValidateException e) {
			e.printStackTrace();
			resultado = null;
		} catch (Exception e) {
			e.printStackTrace();
			resultado = null;
		}
		return resultado;
	}

	public ResponseEntity returnFile(byte[] bytesPdf) {
		HttpHeaders header = new HttpHeaders();
		header.setContentType(MediaType.APPLICATION_PDF);
		String pdfName = "report.pdf";
		header.setContentDispositionFormData(pdfName, pdfName);
		header.setCacheControl("must-revalidate, post-check=0, pre-check=0");
		return new ResponseEntity(bytesPdf, header, HttpStatus.OK);
	}
	
//	@Override
//	public EntityResult reportPruebaQuery(Map<String, Object> keyMap, List<String> attrList)
//			throws OntimizeJEERuntimeException {
//		EntityResult resultado = new EntityResultMapImpl();
//
//		try {
//
//			cf.reset();
//			cf.addBasics(HotelDao.fields);
//			cf.validate(keyMap);
//
//			List<String> required = Arrays.asList(HotelDao.ATTR_ID, HotelDao.ATTR_NAME, HotelDao.ATTR_CITY);
//			cf.reset();
//			cf.addBasics(HotelDao.fields);
//			cf.setRequired(required);
//			cf.setOptional(false);
//			cf.validate(attrList);
//
//			resultado = hotelService.hotelQuery(keyMap, attrList);
//
//			List<PruebaHoteles> a = new ArrayList<PruebaHoteles>();
//
//			for (int i = 0; i < resultado.calculateRecordNumber(); i++) {
//				Integer id = (Integer) resultado.getRecordValues(i).get(HotelDao.ATTR_ID);
//				String name = (String) resultado.getRecordValues(i).get(HotelDao.ATTR_NAME);
//				String city = (String) resultado.getRecordValues(i).get(HotelDao.ATTR_CITY);
//
//				PruebaHoteles h = new PruebaHoteles(id, name, city);
//				a.add(h);
//			}
//
//			String filePath = "..\\atomicHotelsApiRest-model\\src\\main\\resources\\reports\\Hotels_template.jrxml";
//
//			Map<String, Object> parameters = new HashMap<String, Object>() {
//				{
//					put("hotels_title", "HOTELES ATÓMICOS");
//					put("hotels_subtitle", "Grupo Cadena de Hoteles Atómicos");
//				}
//			};
//
////		 JRDataSource data = new JRDataSource().getFieldValue(resultado);
//
//			JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(a);
//
////		JRDataSource dataSource = new JRDataSource(new EntityResultDataSource(resultado).getFields());
//
////		 JRField jrField = res
////				 
////		 JRDataSource dataSource = new JRDataSource(jrField);
////		
////		JRMapArrayDataSource dataSource = new JRMapArrayDataSource(
////                new Object[] { fuelSalesReportInputMO.getDataSources() });
//
//			JasperReport jasperReport = JasperCompileManager.compileReport(filePath);
//
//			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
//
//			JasperExportManager.exportReportToPdfFile(jasperPrint,
//					"..\\atomicHotelsApiRest-model\\src\\main\\resources\\reports\\Hotels_report.pdf");
//
//			byte[] reportPdf = JasperExportManager.exportReportToPdf(jasperPrint);
//
//		} catch (ValidateException e) {
//			e.printStackTrace();
//			resultado = new EntityResultWrong(e.getMessage());
//		} catch (Exception e) {
//			e.printStackTrace();
//			resultado = new EntityResultWrong(ErrorMessage.UNKNOWN_ERROR);
//		}
//
//		return resultado;
//	}
}
