package com.ontimize.atomicHotelsApiRest.model.core.service;

import static org.mockito.ArgumentMatchers.anyDouble;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.postgresql.xml.EmptyStringEntityResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.SQLWarningException;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import com.amadeus.Amadeus;
import com.amadeus.Location;
import com.amadeus.Params;
import com.amadeus.Response;
import com.amadeus.exceptions.NetworkException;
import com.amadeus.exceptions.ResponseException;
import com.amadeus.resources.ScoredLocation;
import com.ontimize.atomicHotelsApiRest.api.core.exceptions.EntityResultRequiredException;
import com.ontimize.atomicHotelsApiRest.api.core.exceptions.InvalidFieldsException;
import com.ontimize.atomicHotelsApiRest.api.core.exceptions.InvalidFieldsValuesException;
import com.ontimize.atomicHotelsApiRest.api.core.exceptions.LiadaPardaException;
import com.ontimize.atomicHotelsApiRest.api.core.exceptions.MissingColumnsException;
import com.ontimize.atomicHotelsApiRest.api.core.exceptions.MissingFieldsException;
import com.ontimize.atomicHotelsApiRest.api.core.exceptions.RestrictedFieldException;
import com.ontimize.atomicHotelsApiRest.api.core.exceptions.ValidateException;
import com.ontimize.atomicHotelsApiRest.api.core.service.IHotelService;
import com.ontimize.atomicHotelsApiRest.model.core.dao.BillDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.BookingDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.BookingServiceExtraDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.CustomerDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.DepartmentDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.HotelDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.ReceiptDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.RoomDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.RoomTypeDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.UserRoleDao;
import com.ontimize.atomicHotelsApiRest.model.core.tools.ControlFields;
import com.ontimize.atomicHotelsApiRest.model.core.tools.EntityResultExtraTools;
import com.ontimize.atomicHotelsApiRest.model.core.tools.EntityResultWrong;
import com.ontimize.atomicHotelsApiRest.model.core.tools.ErrorMessage;
import com.ontimize.atomicHotelsApiRest.model.core.tools.ValidateFields;
import com.ontimize.atomicHotelsApiRest.model.core.tools.TypeCodes.type;
import com.ontimize.jee.common.db.SQLStatementBuilder.BasicExpression;
import com.ontimize.jee.common.db.SQLStatementBuilder.BasicField;
import com.ontimize.jee.common.db.SQLStatementBuilder.BasicOperator;
import com.ontimize.jee.common.db.SQLStatementBuilder.SQLStatement;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.dto.EntityResultMapImpl;
import com.ontimize.jee.common.exceptions.OntimizeJEERuntimeException;
import com.ontimize.jee.common.security.PermissionsProviderSecured;
import com.ontimize.jee.common.tools.EntityResultTools;
import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;
import com.ontimize.jee.server.dao.IOntimizeDaoSupport;
import com.ontimize.jee.server.dao.ISQLQueryAdapter;
import com.sun.xml.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;

@Service("HotelService")
@Lazy
public class HotelService implements IHotelService {

	@Autowired
	private HotelDao dao;
	@Autowired
	private DefaultOntimizeDaoHelper daoHelper;

	@Autowired
	ControlFields cf;

	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult hotelQuery(Map<String, Object> keyMap, List<String> attrList)
			throws OntimizeJEERuntimeException {

		EntityResult resultado = new EntityResultWrong();

		try {
			cf.reset();

			cf.setCPHtlColum(dao.ATTR_ID);
			cf.setCPRoleUsersRestrictions(UserRoleDao.ROLE_MANAGER, UserRoleDao.ROLE_STAFF);

			cf.addBasics(dao.fields);
//			cf.setOptional(true);
			cf.validate(keyMap);
			cf.validate(attrList);

			resultado = this.daoHelper.query(this.dao, keyMap, attrList);

		} catch (ValidateException e) {
			resultado = e.getEntityResult();
		} catch (Exception e) {
			e.printStackTrace();
			resultado = new EntityResultWrong(ErrorMessage.UNKNOWN_ERROR);
		}
		return resultado;
	}

	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult hotelInsert(Map<String, Object> attrMap) throws OntimizeJEERuntimeException {

		EntityResult resultado = new EntityResultWrong();
		try {

			List<String> required = new ArrayList<String>() {
				{
					add(dao.ATTR_NAME);
					add(dao.ATTR_STREET);
					add(dao.ATTR_CITY);
					add(dao.ATTR_CP);
					add(dao.ATTR_STATE);
					add(dao.ATTR_COUNTRY);
				}
			};
			List<String> restricted = new ArrayList<String>() {
				{
					add(dao.ATTR_ID);// No quiero que meta el id porque quiero el id autogenerado de la base de
										// datos
				}
			};

			cf.reset();
			cf.addBasics(dao.fields);
			cf.setRequired(required);
			cf.setRestricted(restricted);
			cf.validate(attrMap);

			resultado = this.daoHelper.insert(this.dao, attrMap);
			resultado.setMessage("Hotel registrado");

		} catch (ValidateException e) {
			resultado = e.getEntityResult();
		} catch (DuplicateKeyException e) {
			resultado = new EntityResultWrong(ErrorMessage.CREATION_ERROR_DUPLICATED_FIELD);
		} catch (Exception e) {
			e.printStackTrace();
			resultado = new EntityResultWrong(ErrorMessage.UNKNOWN_ERROR);
		}

		// OPCION A (comprobando si el registro ya existe)
//		if (attrMap.containsKey(dao.ATTR_NAME)) {
//			Map<String, Object> auxKeyMap = new HashMap<String, Object>();
//			List<String> auxAttrList = new ArrayList<String>();
//			auxKeyMap.put(dao.ATTR_NAME, attrMap.get(dao.ATTR_NAME));
//			auxAttrList.add(dao.ATTR_NAME);
//			EntityResult auxEntity = hotelQuery(auxKeyMap, auxAttrList);
//			// System.out.println("coincidencias:" + auxEntity.calculateRecordNumber());//
//			// TODO eliminar
//			if (auxEntity.calculateRecordNumber() == 0) { // si no hay registros...
//				resultado = this.daoHelper.insert(this.hotelDao, attrMap);
//			} else {
//				resultado.setCode(EntityResult.OPERATION_WRONG);
//				resultado.setMessage("Error al crear Hotel - El registro ya existe");
//			}
//		}

		// OPCION B (capturando excepción duplicateKey)
//		try {
//			resultado = this.daoHelper.insert(this.hotelDao, attrMap);
//			if (resultado != null && resultado.getCode() == EntityResult.OPERATION_WRONG) {
//				resultado.setMessage("Error al insertar datos");
//			} else {
//				resultado.setMessage("mensaje cambiado2 desde insert");
//			}
//		} catch (DuplicateKeyException e) {
//			resultado.setCode(EntityResult.OPERATION_WRONG);
//			resultado.setMessage("Error al crear Hotel - El registro ya existe");
//		}

		// TODO limpiar pruebas de setMessage

//		// OPCION C (comprobando si el registro ya existe)
//		if (attrMap.containsKey(dao.ATTR_NAME)) {
//			EntityResult auxEntity = this.daoHelper.query(this.hotelDao,
//					EntityResultTools.keysvalues(dao.ATTR_NAME, attrMap.get(dao.ATTR_NAME)),
//					EntityResultTools.attributes(dao.ATTR_NAME));
//			if (auxEntity.calculateRecordNumber() == 0) { // si no hay registros...
//				resultado = this.daoHelper.insert(this.hotelDao, attrMap);
//			} else {				
//				resultado = new EntityResultWrong("Error al crear Hotel - El registro ya existe");
//			}
//		}
		return resultado;
	}

	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult hotelUpdate(Map<String, Object> attrMap, Map<String, Object> keyMap)
			throws OntimizeJEERuntimeException {
		EntityResult resultado = new EntityResultWrong();
		try {

			// ControlFields del filtro
			List<String> requiredFilter = new ArrayList<String>() {
				{
					add(dao.ATTR_ID);
				}
			};
			cf.reset();
			cf.addBasics(dao.fields);
			cf.setRequired(requiredFilter);
			cf.setOptional(false);// No será aceptado ningún campo que no esté en required
			cf.validate(keyMap);

			// ControlFields de los nuevos datos
			List<String> restrictedData = new ArrayList<String>() {
				{
					add(dao.ATTR_ID);// El id no se puede actualizar
				}
			};
			cf.reset();
			cf.addBasics(dao.fields);
			cf.setRestricted(restrictedData);
			cf.validate(attrMap);

			Map<String, Object> subConsultaKeyMap = new HashMap<>() {
				{
					putAll(keyMap);
				}
			};

			EntityResult auxEntity = hotelQuery(subConsultaKeyMap, EntityResultTools.attributes(dao.ATTR_ID)); // aquí
																												// validamos
																												// la
																												// resctricción
																												// por
																												// permisos
			if (auxEntity.calculateRecordNumber() == 0) { // si no hay registros...
				resultado = new EntityResultWrong(ErrorMessage.UPDATE_ERROR_MISSING_FIELD);
			} else {

				resultado = this.daoHelper.update(this.dao, attrMap, keyMap);

				if (resultado.getCode() == EntityResult.OPERATION_SUCCESSFUL_SHOW_MESSAGE) {
					resultado = new EntityResultWrong(ErrorMessage.UPDATE_ERROR_MISSING_FIELD);
				} else {
					resultado = new EntityResultMapImpl();
					resultado.setMessage("Hotel actualizado");
				}
			}
		} catch (ValidateException e) {
			resultado = e.getEntityResult();
		} catch (DuplicateKeyException e) {
			e.printStackTrace();
			resultado = new EntityResultWrong(ErrorMessage.UPDATE_ERROR_DUPLICATED_FIELD);
		} catch (DataIntegrityViolationException e) {
			e.printStackTrace();
			resultado = new EntityResultWrong(ErrorMessage.UPDATE_ERROR_REQUIRED_FIELDS);
		} catch (Exception e) {
			e.printStackTrace();
			resultado = new EntityResultWrong(ErrorMessage.UNKNOWN_ERROR);
		}
		return resultado;
	}

	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult hotelDelete(Map<String, Object> keyMap) throws OntimizeJEERuntimeException {

		EntityResult resultado = new EntityResultWrong();
		try {
			List<String> required = new ArrayList<String>() {
				{
					add(dao.ATTR_ID);
				}
			};
			cf.reset();
			cf.addBasics(dao.fields);
			cf.setRequired(required);
			cf.setOptional(false);
			cf.validate(keyMap);

			Map<String, Object> subConsultaKeyMap = new HashMap<>() {
				{
					put(dao.ATTR_ID, keyMap.get(dao.ATTR_ID));
				}
			};

			EntityResult auxEntity = hotelQuery(subConsultaKeyMap, EntityResultTools.attributes(dao.ATTR_ID));

			if (auxEntity.calculateRecordNumber() == 0) { // si no hay registros...
				resultado = new EntityResultWrong(ErrorMessage.DELETE_ERROR_MISSING_FIELD);
			} else {
				resultado = this.daoHelper.delete(this.dao, keyMap);
				resultado.setMessage("Hotel eliminado");
			}

		} catch (ValidateException e) {
			resultado = e.getEntityResult();
		} catch (DataIntegrityViolationException e) {
			resultado = new EntityResultWrong(ErrorMessage.DELETE_ERROR_FOREING_KEY);
		} catch (Exception e) {
			e.printStackTrace();
			resultado = new EntityResultWrong(ErrorMessage.UNKNOWN_ERROR);
		}
		return resultado;
	}

//	@Override
//	public EntityResult hotelInfoQuery(Map<String, Object> keysValues, List<String> attrList)
//			throws OntimizeJEERuntimeException {
//// el InfoQuery lo utilizamos para obtener una query mas detallada con joins de
//// otras tablas.
//		EntityResult queryRes = new EntityResultWrong();
//		try {
//			ControlFields cf = new ControlFields();
//			cf.addBasics(dao.fields);
//			cf.validate(keysValues);
//			cf.validate(attrList);
//
//			queryRes = this.daoHelper.query(this.hotelDao, keysValues, attrList, "queryHotel");
//		} catch (ValidateException e) {
//			queryRes = new EntityResultWrong(e.getMessage());
//		} catch (Exception e) {
//			queryRes = new EntityResultWrong(ErrorMessage.UNKNOWN_ERROR);
//		}
//
//		return queryRes;
////		return null;
//	}

	@SuppressWarnings("static-access")
	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult poiQuery(Map<String, Object> keyMap, List<String> attrList) throws OntimizeJEERuntimeException {
		EntityResult resultado = new EntityResultWrong();
		EntityResult pois = new EntityResultMapImpl();

//		String category = (keyMap.containsKey("category")) ? (String) keyMap.get("category") : "";
//		String radius = (keyMap.containsKey("radius")) ? (String) keyMap.get("radius") : "1";

		try {
//			if (keyMap.containsKey("category")) {
//				keyMap.remove("category");
//			}
//			if (keyMap.containsKey("radius")) {
//				keyMap.remove("radius");
//			}
			List<String> required = Arrays.asList(dao.ATTR_ID, dao.ATTR_CATEGORY, dao.ATTR_RADIUS);
			cf.reset();
			cf.addBasics(dao.fields);
			cf.setRequired(required);
			cf.validate(keyMap);
			cf.setNoEmptyList(false);
			cf.validate(attrList);
			Map<String, Object> keyMapDireccion = new HashMap<>();
			if (keyMap.get(dao.ATTR_ID) != null) {
				keyMapDireccion.put(dao.ATTR_ID, keyMap.get(dao.ATTR_ID));
			}
			
			List<String> attrList1 = Arrays.asList(dao.ATTR_ID,dao.ATTR_STREET,dao.ATTR_CITY);
			resultado = hotelQuery(keyMapDireccion, attrList1);
		

		
			String street = (String) resultado.getRecordValues(0).get(dao.ATTR_STREET);
			String city = (String) resultado.getRecordValues(0).get(dao.ATTR_CITY);
			String urlEnpoint = "https://nominatim.openstreetmap.org/search?street="
					+ URLEncoder.encode(street, StandardCharsets.UTF_8) + "&city="
					+ URLEncoder.encode(city, StandardCharsets.UTF_8) + "&format=json";
			URL url = new URL(urlEnpoint);

			HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			con.connect();
			int responseCode = con.getResponseCode();
			if (responseCode != 200) {
				throw new RuntimeException("Ocurrio un error " + responseCode);
			} else {
				StringBuilder infor = new StringBuilder();
				Scanner sc = new Scanner(url.openStream());
				while (sc.hasNext()) {
					infor.append(sc.nextLine());
				}
				sc.close();
				JSONArray jsonarray = new JSONArray(infor.toString());
				JSONObject jsonObject = jsonarray.getJSONObject(0);
				String latitude = jsonObject.getString("lat");
				String longitude = jsonObject.getString("lon");

				Map<String, Object> attrMapco = new HashMap<>() {
					{
						put(dao.ATTR_LAT, latitude);
						put(dao.ATTR_LON, longitude);
					}
				};

				EntityResult coorUpdate = hotelUpdate(attrMapco, keyMap);

				String urlEndpoint2 = "https://test.api.amadeus.com/v1/security/oauth2/token";
				String urlParams = "grant_type=client_credentials&client_id=OiD6eJmJHbF5pS9d9cFMjOJBQQAJcWnm&client_secret=kxp59MWMcdFrcADB";
				String urlPoi = "https://test.api.amadeus.com/v1/reference-data/locations/pois?latitude=" + latitude
						+ "&longitude=" + longitude + "&radius=" + keyMap.get(dao.ATTR_RADIUS) +"&page%5Blimit%5D=10&page%5Boffset%5D=0"+ "&categories="
						+ keyMap.get(dao.ATTR_CATEGORY);

				URL url2 = new URL(urlEndpoint2);
				HttpsURLConnection con2 = (HttpsURLConnection) url2.openConnection();
				con2.setDoOutput(true);
				con2.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
				byte[] dataUrlEndPoint2 = urlParams.getBytes(StandardCharsets.UTF_8);
				int dataLenght = dataUrlEndPoint2.length;
				con2.setRequestProperty("Content-Lenght", Integer.toString(dataLenght)); // content lenght va en bytes
				con2.setRequestMethod("POST");
				con2.setUseCaches(false);
	
					DataOutputStream wr = new DataOutputStream(con2.getOutputStream());
					wr.write(dataUrlEndPoint2); // escirbe en servidor
					BufferedReader br = new BufferedReader(new InputStreamReader((con2.getInputStream()))); // leemos
																											// respuesta
					StringBuilder sb = new StringBuilder();
					String output;
					while ((output = br.readLine()) != null) {
						sb.append(output);
					}
					JSONArray jsonarray2 = new JSONArray("[" + sb.toString() + "]");
					JSONObject jsonObject2 = jsonarray2.getJSONObject(0);
					URL url3 = new URL(urlPoi);
					HttpsURLConnection con3 = (HttpsURLConnection) url3.openConnection();
					con3.setRequestProperty("Authorization", "Bearer " + jsonObject2.get("access_token"));
					con3.setRequestProperty("Content-Type", "application/json");
					con3.setRequestMethod("GET");
					con3.connect();
					int responseCod = con3.getResponseCode();
					if(responseCod==200) {
						
					
					BufferedReader in2 = new BufferedReader(new InputStreamReader(con3.getInputStream()));

					StringBuffer response2 = new StringBuffer();
					while ((output = in2.readLine()) != null) {
						response2.append(output);
					}

					in2.close();
					// printing result from response
					System.out.println(response2.toString());
					JSONObject jsonarray3 = new JSONObject(response2.toString());
					JSONArray jsarray = (JSONArray) jsonarray3.get("data");

					int u = 0;
					System.out.println(jsarray.length());
					StringBuilder cc = new StringBuilder();
					while (u < jsarray.length()) {
						JSONObject coor = (JSONObject) jsarray.get(u);
						cc.append(coor.get("geoCode"));
						u++;
					}
					System.err.println(cc.toString());

					@SuppressWarnings("unchecked")
					Map<String, Object> aux = resultado.getRecordValues(0); // obtenemos el HashMap 0 de resultado.
					List<Object> auxList = new ArrayList<>(); // creamos una lista auxiliar
					for (int i = 0; i < jsarray.length(); i++) {
						Map<String, Object> poisMp = new HashMap<>(); // creamos un HashMap dentro del for
						JSONObject nuevo = (JSONObject) jsarray.get(i);
						// System.out.println(nuevo.get("name"));
						poisMp.put("Poins", nuevo.get("name")); // creamos POINS en el hashmap POISMP

						JSONObject Ogeo = nuevo.getJSONObject("geoCode");
						Map<String, Object> coorM = new HashMap<>();
						coorM.put("latitude", Ogeo.get("latitude"));
						coorM.put("longitude", Ogeo.get("longitude"));
						poisMp.put("geoCode", coorM);

						poisMp.put("Category", nuevo.get(dao.ATTR_CATEGORY));
						auxList.add(poisMp); //
					}
					aux.put("POINS", auxList);
					pois.addRecord(aux);
					}
					else {
						pois = new EntityResultWrong("Error de conexión con la api amadeus");
					}
			}
		
		} catch (NullPointerException e) {
			pois = new EntityResultWrong("Este hotel no se encuentra.");
		} catch (JSONException e) {
			pois = new EntityResultWrong("La dirección o ciudad del hotel no se encuentran");
		} catch (ValidateException e) {
			pois = e.getEntityResult();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			pois = new EntityResultWrong(e1.getMessage());
		} 

		return pois;
	}

}