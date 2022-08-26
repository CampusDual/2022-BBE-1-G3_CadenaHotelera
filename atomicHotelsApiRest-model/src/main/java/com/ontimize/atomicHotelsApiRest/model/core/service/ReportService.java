package com.ontimize.atomicHotelsApiRest.model.core.service;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

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
import com.ontimize.atomicHotelsApiRest.model.core.dao.BookingDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.CustomerDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.EmployeeDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.HotelDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.ReceiptDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.RoomTypeDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.UserRoleDao;
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
import net.sf.jasperreports.engine.JREmptyDataSource;
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
	private BookingService bookingService;

	@Autowired
	private ReceiptService receiptService;

	@Autowired
	private DefaultOntimizeDaoHelper daoHelper;

	@Autowired
	ControlFields cf;

	private final String HOTEL_TEMPLATE_PATH = "..\\atomicHotelsApiRest-model\\src\\main\\resources\\reports\\plantilla.jrxml";
	private final String HOTEL_TEMPLATE_03_PATH = "..\\atomicHotelsApiRest-model\\src\\main\\resources\\reports\\Hotels_template3.jrxml";
	private final String INCOME_VS_EXPENSES_CHART = "..\\atomicHotelsApiRest-model\\src\\main\\resources\\reports\\incomeVsExpensesChart.jrxml";
	private final String RECEIPT_BD = "..\\atomicHotelsApiRest-model\\src\\main\\resources\\reports\\Receipt_template_BD.jrxml";
	private final String OCCUPANCY_CHART = "..\\atomicHotelsApiRest-model\\src\\main\\resources\\reports\\occupancyChart.jrxml";
	private final String OCCUPANCY_BY_NATIONALITY_CHART = "..\\atomicHotelsApiRest-model\\src\\main\\resources\\reports\\occupancyByNationalityChart.jrxml";
	private final String LISTALLEMPLOYEE = "..\\atomicHotelsApiRest-model\\src\\main\\resources\\reports\\EmpleadosAllReporte.jasper";
	private final String EMPLOYEE_BY_HOTEL = "..\\atomicHotelsApiRest-model\\src\\main\\resources\\reports\\EmpleadosPorHotelReporte2.jrxml";
	private final String DEPARTMENT_EXPENSES_CHART = "..\\atomicHotelsApiRest-model\\src\\main\\resources\\reports\\departmentExpensesByHotelChart.jrxml";
	private final String LIST_HOTELS = "..\\atomicHotelsApiRest-model\\src\\main\\resources\\reports\\PosicionHotelReporte.jrxml";
	private final String EMPLOYEE_DEPARTMENT_COST = "..\\atomicHotelsApiRest-model\\src\\main\\resources\\reports\\EmployeePieDepartamentHotel.jrxml";

	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public ResponseEntity hotels() throws OntimizeJEERuntimeException {

		ResponseEntity resultado;
		try {
//			List<String> required = new ArrayList<String>() {
//				{
//					add(HotelDao.ATTR_ID);
//				}
//			};
//			cf.reset();
//			cf.addBasics(HotelDao.fields);
//			cf.setRequired(required);
//			cf.setNoEmptyList(false);
//			cf.validate(keyMap);
			JasperReport jasperReport = JasperCompileManager.compileReport(LIST_HOTELS);
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, ReportsConfig.getBasicParameters());
			resultado = returnFile(JasperExportManager.exportReportToPdf(jasperPrint));
//		} catch (ValidateException e) {			
//			resultado = ResponseEntity.ok(e.getEntityResult());
		} catch (Exception e) {
			e.printStackTrace();
			resultado = ResponseEntity.ok(new EntityResultWrong(ErrorMessage.UNKNOWN_ERROR));
		}
		return resultado;

	}

	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public ResponseEntity employeePieCostByDepartament(Map<String, Object> keyMap, List<String> attrList)
			throws OntimizeJEERuntimeException {

		ResponseEntity resultado;
		try {
			List<String> required = new ArrayList<String>() {
				{
					add(HotelDao.ATTR_ID);
				}
			};

			/// Ver si existe el hotel

			cf.reset();
			cf.addBasics(HotelDao.fields);
			cf.setCPHtlColum(HotelDao.ATTR_ID);
			cf.setCPRoleUsersRestrictions(UserRoleDao.ROLE_MANAGER);

			cf.setRequired(required);
			cf.setOptional(false);
			cf.validate(keyMap);

			EntityResult existe = hotelService.hotelQuery(keyMap, required);
			if (existe.calculateRecordNumber() == 1) {
				JasperReport jasperReport = JasperCompileManager.compileReport(EMPLOYEE_DEPARTMENT_COST);
				JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport,
						ReportsConfig.getBasicParametersPutAll(keyMap));
				resultado = returnFile(JasperExportManager.exportReportToPdf(jasperPrint));
			} else {
				resultado = ResponseEntity.ok(new EntityResultWrong(ErrorMessage.INVALID_HOTEL_ID));
			}
		} catch (ValidateException e) {
			resultado = ResponseEntity.ok(e.getEntityResult());
		} catch (Exception e) {
			e.printStackTrace();
			resultado = ResponseEntity.ok(new EntityResultWrong(ErrorMessage.UNKNOWN_ERROR));
		}
		return resultado;

	}

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

			JRTableModelDataSource dataSource = new JRTableModelDataSource(
					EntityResultUtils.createTableModel(consulta));
			JasperReport jasperReport = JasperCompileManager.compileReport(HOTEL_TEMPLATE_PATH);
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, ReportsConfig.getBasicParameters(),
					dataSource);

			resultado = returnFile(JasperExportManager.exportReportToPdf(jasperPrint));

		} catch (ValidateException e) {
			resultado = ResponseEntity.ok(e.getEntityResult());
		} catch (Exception e) {
			e.printStackTrace();
			resultado = ResponseEntity.ok(new EntityResultWrong(ErrorMessage.UNKNOWN_ERROR));
		}
		return resultado;

	}

	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public ResponseEntity incomeVsExpensesChart(Map<String, Object> keyMap, List<String> attrList)
			throws OntimizeJEERuntimeException {
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

			EntityResult consultaCategorizada = new EntityResultMapImpl();
			for (int i = 0; i < consulta.calculateRecordNumber(); i++) {
				HashMap<String, Object> auxMap = new HashMap<>();
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
			JRTableModelDataSource dataSource = new JRTableModelDataSource(
					EntityResultUtils.createTableModel(consultaCategorizada));
			JasperReport jasperReport = JasperCompileManager.compileReport(INCOME_VS_EXPENSES_CHART);
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, ReportsConfig.getBasicParameters(),
					dataSource);
			jasperPrint.setOrientation(OrientationEnum.LANDSCAPE);
			resultado = returnFile(JasperExportManager.exportReportToPdf(jasperPrint));

		} catch (ValidateException e) {
			resultado = ResponseEntity.ok(e.getEntityResult());
		} catch (Exception e) {
			e.printStackTrace();
			resultado = ResponseEntity.ok(new EntityResultWrong(ErrorMessage.UNKNOWN_ERROR));
		}
		return resultado;
	}

	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public ResponseEntity receipt(Map<String, Object> keyMap, List<String> attrList)
			throws OntimizeJEERuntimeException {

		ResponseEntity resultado;
		try {

			List<String> required = new ArrayList<String>() {
				{
					add(ReceiptDao.ATTR_ID);
				}
			};
//todo hacer consulta por recibos
			cf.reset();
			cf.addBasics(ReceiptDao.fields);
			cf.setRequired(required);
			cf.setOptional(false);
			cf.validate(keyMap);
			
			List<String> receiptList = Arrays.asList(ReceiptDao.ATTR_ID);
			EntityResult consulta = receiptService.receiptQuery(keyMap, receiptList);
			
			if(consulta.calculateRecordNumber()!=0) {
				JasperReport jasperReport = JasperCompileManager.compileReport(RECEIPT_BD);
				JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport,
						ReportsConfig.getBasicParametersPutAll(keyMap));
				jasperPrint.setOrientation(OrientationEnum.LANDSCAPE);

				resultado = returnFile(JasperExportManager.exportReportToPdf(jasperPrint));
				
			}else {
				resultado = ResponseEntity.ok("El recibo no existe");
			}	

		} catch (ValidateException e) {
			resultado = ResponseEntity.ok(e.getEntityResult());

		} catch (Exception e) {
			e.printStackTrace();
			resultado = ResponseEntity.ok(new EntityResultWrong(ErrorMessage.UNKNOWN_ERROR));
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

	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public ResponseEntity occupancyChart(Map<String, Object> keyMap, List<String> attrList)
			throws OntimizeJEERuntimeException {
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

			consulta = statisticsService.hotelOccupancyPercentageQuery(keyMap, new ArrayList<String>());

			EntityResult consultaCategorizada = new EntityResultMapImpl();
			for (int i = 0; i < consulta.calculateRecordNumber(); i++) {
				HashMap<String, Object> auxMap = new HashMap<>();
				auxMap.put("htl_id", consulta.getRecordValues(i).get("htl_id"));
				auxMap.put("htl_name", consulta.getRecordValues(i).get("htl_name"));

				auxMap.put("serie", "capacity_in_date_range");
				auxMap.put("value", consulta.getRecordValues(i).get("capacity_in_date_range"));

				consultaCategorizada.addRecord(auxMap);

				auxMap.put("serie", "occupancy_in_date_range");
				auxMap.put("value", consulta.getRecordValues(i).get("occupancy_in_date_range"));

				consultaCategorizada.addRecord(auxMap);

			}

			Map<String, Object> fechas = new HashMap<String, Object>() {
				{
					put("from", keyMap.get("from"));
					put("to", keyMap.get("to"));
				}
			};

			JRTableModelDataSource dataSource = new JRTableModelDataSource(
					EntityResultUtils.createTableModel(consultaCategorizada));
			JasperReport jasperReport = JasperCompileManager.compileReport(OCCUPANCY_CHART);
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport,
					ReportsConfig.getBasicParametersPutAll(fechas), dataSource);
			jasperPrint.setOrientation(OrientationEnum.LANDSCAPE);
			resultado = returnFile(JasperExportManager.exportReportToPdf(jasperPrint));

		} catch (ValidateException e) {
			resultado = ResponseEntity.ok(e.getEntityResult());
		} catch (Exception e) {
			e.printStackTrace();
			resultado = ResponseEntity.ok(new EntityResultWrong(ErrorMessage.UNKNOWN_ERROR));
		}
		return resultado;
	}

	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public ResponseEntity occupancyByNationalityChart(Map<String, Object> keyMap, List<String> attrList)
			throws OntimizeJEERuntimeException {
		EntityResult consulta = new EntityResultMapImpl();
		EntityResult subConsulta = new EntityResultMapImpl();
		ResponseEntity resultado;
		try {

			List<String> required = new ArrayList<String>() {
				{
					add(HotelDao.ATTR_FROM);
					add(HotelDao.ATTR_TO);
					add(HotelDao.ATTR_ID);
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
			cf.setCPHtlColum(HotelDao.ATTR_ID);
			cf.setCPRoleUsersRestrictions(UserRoleDao.ROLE_MANAGER, UserRoleDao.ROLE_STAFF);

			cf.setRequired(required);
			cf.validate(keyMap);

			consulta = statisticsService.hotelOccupancyByNationalityQuery(keyMap, new ArrayList<String>());

			List<String> hotelList = Arrays.asList(HotelDao.ATTR_NAME, HotelDao.ATTR_CITY);
			Map<String, Object> idHotel = new HashMap<String, Object>() {
				{
					put(HotelDao.ATTR_ID, keyMap.get(HotelDao.ATTR_ID));
				}
			};

			subConsulta = hotelService.hotelQuery(idHotel, hotelList);

//			Map<String,Object> hotleFechas = new HashMap<String,Object>(){{
//				put(HotelDao.ATTR_NAME,subConsulta.getRecordValues(0).get(HotelDao.ATTR_NAME));
//				put(HotelDao.ATTR_FROM,keyMap.get(HotelDao.ATTR_FROM));
//				put(HotelDao.ATTR_TO,keyMap.get(HotelDao.ATTR_TO));
//			}};

			keyMap.put(HotelDao.ATTR_NAME, subConsulta.getRecordValues(0).get(HotelDao.ATTR_NAME));

			JRTableModelDataSource dataSource = new JRTableModelDataSource(
					EntityResultUtils.createTableModel(consulta));
			JasperReport jasperReport = JasperCompileManager.compileReport(OCCUPANCY_BY_NATIONALITY_CHART);
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport,
					ReportsConfig.getBasicParametersPutAll(keyMap), dataSource);
			jasperPrint.setOrientation(OrientationEnum.LANDSCAPE);
			resultado = returnFile(JasperExportManager.exportReportToPdf(jasperPrint));

		} catch (NullPointerException e) {
			resultado = ResponseEntity.ok("No hay datos");
		} catch (ValidateException e) {
			resultado = ResponseEntity.ok(e.getEntityResult());
		} catch (Exception e) {
			e.printStackTrace();
			resultado = ResponseEntity.ok(new EntityResultWrong(ErrorMessage.UNKNOWN_ERROR));
		}
		return resultado;
	}

	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public ResponseEntity departmentExpensesByHotelChart(Map<String, Object> keyMap, List<String> attrList)
			throws OntimizeJEERuntimeException {
		EntityResult consulta = new EntityResultMapImpl();
		EntityResult subConsulta = new EntityResultMapImpl();
		ResponseEntity resultado;
		try {

			List<String> required = new ArrayList<String>() {
				{
					add(HotelDao.ATTR_FROM);
					add(HotelDao.ATTR_TO);
					add(HotelDao.ATTR_ID);
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
			cf.setCPHtlColum(HotelDao.ATTR_ID);
			cf.setCPRoleUsersRestrictions(UserRoleDao.ROLE_MANAGER, UserRoleDao.ROLE_STAFF);

			cf.addBasics(fields);
			cf.setRequired(required);
			cf.validate(keyMap);

			consulta = statisticsService.departmentExpensesByHotelQuery(keyMap, new ArrayList<String>());

			List<String> hotelList = Arrays.asList(HotelDao.ATTR_NAME, HotelDao.ATTR_CITY);
			Map<String, Object> idHotel = new HashMap<String, Object>() {
				{
					put(HotelDao.ATTR_ID, keyMap.get(HotelDao.ATTR_ID));
				}
			};

			subConsulta = hotelService.hotelQuery(idHotel, hotelList);

//			Map<String,Object> hotleFechas = new HashMap<String,Object>(){{
//				put(HotelDao.ATTR_NAME,subConsulta.getRecordValues(0).get(HotelDao.ATTR_NAME));
//				put(HotelDao.ATTR_FROM,keyMap.get(HotelDao.ATTR_FROM));
//				put(HotelDao.ATTR_TO,keyMap.get(HotelDao.ATTR_TO));
//			}};

			keyMap.put(HotelDao.ATTR_NAME, subConsulta.getRecordValues(0).get(HotelDao.ATTR_NAME));

			JRTableModelDataSource dataSource = new JRTableModelDataSource(
					EntityResultUtils.createTableModel(consulta));
			JasperReport jasperReport = JasperCompileManager.compileReport(DEPARTMENT_EXPENSES_CHART);
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport,
					ReportsConfig.getBasicParametersPutAll(keyMap), dataSource);
			jasperPrint.setOrientation(OrientationEnum.LANDSCAPE);
			resultado = returnFile(JasperExportManager.exportReportToPdf(jasperPrint));

		} catch (NullPointerException e) {
			resultado = ResponseEntity.ok("No hay datos");
		} catch (ValidateException e) {
			resultado = ResponseEntity.ok(e.getEntityResult());
		} catch (Exception e) {
			e.printStackTrace();
			resultado = ResponseEntity.ok(new EntityResultWrong(ErrorMessage.UNKNOWN_ERROR));
		}
		return resultado;
	}

	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public ResponseEntity listAllEmployeeReport(Map<String, Object> keyMap, List<String> attrList) {
		EntityResult consulta = new EntityResultMapImpl();
		ResponseEntity resultado;
		try {

			JasperPrint reporteLleno = JasperFillManager.fillReport(LISTALLEMPLOYEE, new HashMap<>(),
					getMyPostgresConnection());

			resultado = returnFile(JasperExportManager.exportReportToPdf(reporteLleno));

		} catch (Exception e) {
			e.printStackTrace();
			resultado = ResponseEntity.ok(new EntityResultWrong(ErrorMessage.UNKNOWN_ERROR));
		}
		return resultado;

	}

	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public ResponseEntity employeesByHotel(Map<String, Object> keyMap, List<String> attrList)
			throws OntimizeJEERuntimeException {

		ResponseEntity resultado;
		try {

			List<String> required = new ArrayList<String>() {
				{
					add(HotelDao.ATTR_ID);
				}
			};
///Ver si existe el hotel

			cf.reset();
			cf.addBasics(HotelDao.fields);
			cf.setCPHtlColum(HotelDao.ATTR_ID);
			cf.setCPRoleUsersRestrictions(UserRoleDao.ROLE_MANAGER, UserRoleDao.ROLE_STAFF);

			cf.setRequired(required);
			cf.setOptional(false);
			cf.validate(keyMap);

			EntityResult existe = hotelService.hotelQuery(keyMap, required);
			if (existe.calculateRecordNumber() == 1) {
				JasperReport jasperReport = JasperCompileManager.compileReport(EMPLOYEE_BY_HOTEL);
				JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport,
						ReportsConfig.getBasicParametersPutAll(keyMap));

				resultado = returnFile(JasperExportManager.exportReportToPdf(jasperPrint));
			} else {
				resultado = ResponseEntity.ok(new EntityResultWrong(ErrorMessage.INVALID_HOTEL_ID));
			}

		} catch (ValidateException e) {
			resultado = ResponseEntity.ok(e.getEntityResult());
		} catch (Exception e) {
			e.printStackTrace();
			resultado = ResponseEntity.ok(new EntityResultWrong(ErrorMessage.UNKNOWN_ERROR));
		}
		return resultado;

	}

	private static Connection getMyPostgresConnection() {
		try {
			Class.forName("org.postgresql.Driver");
			String connectionURL = "jdbc:postgresql://45.84.210.174:65432/Backend_2022_G3";
			Connection conn = DriverManager.getConnection(connectionURL, "Backend_2022_G3", "quei1Okai3eeGieboo");
			return conn;
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return null;
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
//						resultado = ResponseEntity.ok(e.getEntityResult());

//		} catch (Exception e) {
//			e.printStackTrace();
//			resultado = new EntityResultWrong(ErrorMessage.UNKNOWN_ERROR);
//		}
//
//		return resultado;
//	}

//	@Override 
//	public ResponseEntity receipt(Map<String, Object> keyMap, List<String> attrList)
//			throws OntimizeJEERuntimeException {
//
//		EntityResult consultaRecibo = new EntityResultMapImpl();
//		EntityResult consultaReserva = new EntityResultMapImpl();
//		EntityResult consultaHabitacion = new EntityResultMapImpl();
//
//		ResponseEntity resultado;
//		try {
//
//			List<String> required = new ArrayList<String>() {
//				{
//					add(ReceiptDao.ATTR_ID);
//				}
//			};
//
//			cf.reset();
//			cf.addBasics(ReceiptDao.fields);
//			cf.setRequired(required);
//			cf.setOptional(false);
//			cf.validate(keyMap);
//
//			consultaRecibo = receiptService.completeReceiptQuery(keyMap, new ArrayList<String>());
//
//			EntityResult consultaReciboFinal = consultaRecibo;
//
//			Map<String, Object> idBooking = new HashMap<String, Object>() {
//				{
//					put(BookingDao.ATTR_ID, consultaReciboFinal.getRecordValues(0).get(ReceiptDao.ATTR_BOOKING_ID));
//				}
//			};
//
//			consultaReserva = bookingService.bookingCompleteInfoQuery(idBooking, new ArrayList<String>());
//
//			consultaHabitacion = bookingService.bookingDaysUnitaryRoomPriceQuery(idBooking, new ArrayList<String>());
//
//			EntityResult consultaReservaFinal = consultaReserva;
//			EntityResult consultaHabitacionFinal = consultaHabitacion;
//
//			EntityResult servciosExtra = new EntityResultMapImpl();
//			List<Object> lista = (List<Object>) consultaRecibo.getRecordValues(0).get("extra_services");
//			for (Object a : lista) {
//				servciosExtra.addRecord((HashMap<String, Object>) a);
//			}
//
//			Map<String, Object> parameters = new HashMap<String, Object>() {
//				{
//
//					put(ReceiptDao.ATTR_DATE, consultaReciboFinal.getRecordValues(0).get(ReceiptDao.ATTR_DATE));
//					put(ReceiptDao.ATTR_BOOKING_ID,
//							consultaReciboFinal.getRecordValues(0).get(ReceiptDao.ATTR_BOOKING_ID));
//					put(ReceiptDao.ATTR_DIAS, consultaReciboFinal.getRecordValues(0).get(ReceiptDao.ATTR_DIAS));
//					put(ReceiptDao.ATTR_TOTAL_SERVICES,
//							consultaReciboFinal.getRecordValues(0).get(ReceiptDao.ATTR_TOTAL_SERVICES));
//					put(ReceiptDao.ATTR_TOTAL_ROOM,
//							consultaReciboFinal.getRecordValues(0).get(ReceiptDao.ATTR_TOTAL_ROOM));
//					put(ReceiptDao.ATTR_TOTAL, consultaReciboFinal.getRecordValues(0).get(ReceiptDao.ATTR_TOTAL));
//					put(ReceiptDao.ATTR_ID, consultaReciboFinal.getRecordValues(0).get(ReceiptDao.ATTR_ID));
//
//					put(CustomerDao.ATTR_NAME, consultaReservaFinal.getRecordValues(0).get(CustomerDao.ATTR_NAME));
//					put(CustomerDao.ATTR_SURNAME,
//							consultaReservaFinal.getRecordValues(0).get(CustomerDao.ATTR_SURNAME));
//
//					put(BookingDao.ATTR_CHECKIN,
//							consultaHabitacionFinal.getRecordValues(0).get(BookingDao.ATTR_CHECKIN));
//					put(BookingDao.ATTR_CHECKOUT,
//							consultaHabitacionFinal.getRecordValues(0).get(BookingDao.ATTR_CHECKOUT));
//					put(RoomTypeDao.ATTR_PRICE, consultaHabitacionFinal.getRecordValues(0).get(RoomTypeDao.ATTR_PRICE));
//				}
//			};
//			
//			
//
////			Map<String, Object> sinServcios = new HashMap<String, Object>() {
////				{
////					put("bsx_units", null);
////					put("bsx_precio", null);
////					put("sxt_description", null);
////					put("sxt_name", null);
////					put("bsx_date", null);
////				}
////			};
////
////			if (servciosExtra.isEmpty()) {
////				servciosExtra.addRecord(sinServcios);
////			}
//			
//			
//
//			JRTableModelDataSource dataSource = new JRTableModelDataSource(
//					EntityResultUtils.createTableModel(servciosExtra));
//			JasperReport jasperReport = JasperCompileManager.compileReport(RECEIPT);
//			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport,
//					ReportsConfig.getBasicParametersPutAll(parameters), dataSource);
//			jasperPrint.setOrientation(OrientationEnum.LANDSCAPE);
//
//			resultado = returnFile(JasperExportManager.exportReportToPdf(jasperPrint));
//
//		}  catch (ValidateException e) {			
//						resultado = ResponseEntity.ok(e.getEntityResult());

//		} catch (Exception e) {
//			e.printStackTrace();
//			resultado = ResponseEntity.ok(new EntityResultWrong(ErrorMessage.UNKNOWN_ERROR));
//		}
//		return resultado;
//	}

}
