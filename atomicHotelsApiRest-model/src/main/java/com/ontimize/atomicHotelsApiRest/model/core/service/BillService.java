package com.ontimize.atomicHotelsApiRest.model.core.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import com.ontimize.atomicHotelsApiRest.api.core.exceptions.EntityResultRequiredException;
import com.ontimize.atomicHotelsApiRest.api.core.exceptions.ValidateException;
import com.ontimize.atomicHotelsApiRest.api.core.service.IBillService;
import com.ontimize.atomicHotelsApiRest.model.core.dao.BillDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.BookingDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.BookingGuestDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.CustomerDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.DepartmentDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.HotelDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.RoomDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.RoomTypeDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.UserRoleDao;
import com.ontimize.atomicHotelsApiRest.model.core.tools.ControlFields;
import com.ontimize.atomicHotelsApiRest.model.core.tools.EntityResultWrong;
import com.ontimize.atomicHotelsApiRest.model.core.tools.ErrorMessage;
import com.ontimize.atomicHotelsApiRest.model.core.tools.TypeCodes.type;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.dto.EntityResultMapImpl;
import com.ontimize.jee.common.exceptions.OntimizeJEERuntimeException;
import com.ontimize.jee.common.security.PermissionsProviderSecured;
import com.ontimize.jee.common.tools.EntityResultTools;
import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;

@Service("BillService")
@Lazy
public class BillService implements IBillService {

	@Autowired
	private BillDao dao;

	@Autowired
	private DefaultOntimizeDaoHelper daoHelper;

	@Autowired
	private DepartmentService departmentService;

	@Autowired
	private HotelService hotelService;

	@Autowired
	ControlFields cf;

	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult billQuery(Map<String, Object> keyMap, List<String> attrList)
			throws OntimizeJEERuntimeException {
		EntityResult resultado = new EntityResultWrong();
		try {

			cf.reset();

			cf.setCPHtlColum(dao.ATTR_ID_HTL);
			cf.setCPRoleUsersRestrictions(UserRoleDao.ROLE_MANAGER);

			cf.addBasics(dao.fields);
			cf.validate(keyMap);

			cf.validate(attrList);

			resultado = this.daoHelper.query(this.dao, keyMap, attrList);

		} catch (ValidateException e) {
			resultado = e.getEntityResult();
		} catch (Exception e) {
			resultado = new EntityResultWrong(ErrorMessage.ERROR);
		}
		return resultado;
	}

	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult billInsert(Map<String, Object> attrMap) throws OntimizeJEERuntimeException {

		EntityResult resultado = new EntityResultWrong();
		try {

			cf.reset();

			cf.setCPHtlColum(dao.ATTR_ID_HTL);
			cf.setCPRoleUsersRestrictions(UserRoleDao.ROLE_MANAGER);

			List<String> required = new ArrayList<String>() {
				{
					add(dao.ATTR_ID_HTL);
					add(dao.ATTR_ID_DPT);
					add(dao.ATTR_CONCEPT);
					add(dao.ATTR_DATE);
					add(dao.ATTR_AMOUNT);
				}
			};
			List<String> restricted = new ArrayList<String>() {
				{
					add(dao.ATTR_ID);
				}
			};

			cf.addBasics(dao.fields);
			cf.setRequired(required);
			cf.setRestricted(restricted);
			cf.validate(attrMap);

			resultado = this.daoHelper.insert(this.dao, attrMap);
			resultado.setMessage("Bill registered.");

		} catch (ValidateException e) {
			resultado = e.getEntityResult();
		} catch (DuplicateKeyException e) {
			resultado = new EntityResultWrong(ErrorMessage.CREATION_ERROR_DUPLICATED_FIELD);
		} catch (DataIntegrityViolationException e) {
			resultado = new EntityResultWrong(ErrorMessage.CREATION_ERROR_MISSING_FK);
		} catch (Exception e) {
			resultado = new EntityResultWrong(ErrorMessage.UNKNOWN_ERROR);
		}
		return resultado;
	}

	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult billUpdate(Map<String, Object> attrMap, Map<String, Object> keyMap)
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
//			cf.setOptional(true); //No es necesario ponerlo
			cf.validate(attrMap);

			Map<String, Object> subConsultaKeyMap = new HashMap<>() {
				{
					putAll(keyMap);
				}
			};

			EntityResult auxEntity = billQuery(subConsultaKeyMap, EntityResultTools.attributes(dao.ATTR_ID)); // aquí

			if (auxEntity.calculateRecordNumber() == 0) { // si no hay registros...
				resultado = new EntityResultWrong(ErrorMessage.UPDATE_ERROR_MISSING_FIELD);
			} else {

				resultado = this.daoHelper.update(this.dao, attrMap, keyMap);
			}
			if (resultado.getCode() == EntityResult.OPERATION_SUCCESSFUL_SHOW_MESSAGE) {
				resultado = new EntityResultWrong(ErrorMessage.UPDATE_ERROR_MISSING_FIELD);
			} else {
				resultado.setMessage("Bill updated");
			}
		} catch (ValidateException e) {
			resultado = e.getEntityResult();
		} catch (DuplicateKeyException e) {
			resultado = new EntityResultWrong(ErrorMessage.UPDATE_ERROR_DUPLICATED_FIELD);
		} catch (DataIntegrityViolationException e) {// Puede ser que se meta una FK que no exista o se le ponga null al
			// precio cuando no se debería permitir
			resultado = new EntityResultWrong(
					ErrorMessage.UPDATE_ERROR_MISSING_FK + " / " + ErrorMessage.UPDATE_ERROR_REQUIRED_FIELDS);
		} catch (Exception e) {
			resultado = new EntityResultWrong(ErrorMessage.UNKNOWN_ERROR);
		}
		return resultado;
	}

	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult billDelete(Map<String, Object> keyMap) throws OntimizeJEERuntimeException {

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

			Map<String, Object> subconsultaKeyMap = new HashMap<>() {
				{
					put(dao.ATTR_ID, keyMap.get(dao.ATTR_ID));
				}
			};

			EntityResult auxEntity = billQuery(subconsultaKeyMap, EntityResultTools.attributes(dao.ATTR_ID));

			if (auxEntity.calculateRecordNumber() == 0) { // si no hay registros...
				resultado = new EntityResultWrong(ErrorMessage.DELETE_ERROR_MISSING_FIELD);
			} else {
				resultado = this.daoHelper.delete(this.dao, keyMap);
				resultado.setMessage("Bill deleted.");
			}

		} catch (ValidateException e) {
			resultado = e.getEntityResult();
		} catch (DataIntegrityViolationException e) {
			resultado = new EntityResultWrong(ErrorMessage.DELETE_ERROR_FOREING_KEY);
		} catch (Exception e) {
			resultado = new EntityResultWrong(ErrorMessage.UNKNOWN_ERROR);
		}
		return resultado;
	}

	/**
	 * Dado un identificador de hotel y otro de departamento, devuelve los datos de
	 * las facturas pertenecientes a dicho hotel anidado por departamento
	 * 
	 * @param keyMap   DepartmentDao.ATTR_ID, HotelDao.ATTR_ID
	 * @param attrList (anyList())
	 * @return EntityResult (HotelDao.ATTR_NAME,
	 *         HotelDao.ATTR_CITY,DepartmentDao.ATTR_ID,"gastos departamento",)
	 * @throws OntimizeJEERuntimeException
	 */
	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult gastosDepartamentoQuery(Map<String, Object> keyMap, List<String> attrList)
			throws OntimizeJEERuntimeException {

		EntityResult resultadoFinal = new EntityResultMapImpl(); // Para que no salga el 1 en el json de respuesta

		EntityResult resultado = new EntityResultWrong();
		try {

			List<String> required = new ArrayList<String>() {
				{
					add(DepartmentDao.ATTR_ID);
					add(HotelDao.ATTR_ID);
				}
			};

			cf.reset();
			cf.setCPHtlColum(dao.ATTR_ID_HTL);
			cf.setCPRoleUsersRestrictions(UserRoleDao.ROLE_MANAGER);
			
			cf.addBasics(DepartmentDao.fields);
			cf.addBasics(HotelDao.fields);
			cf.setRequired(required);
			cf.setOptional(false);
			cf.validate(keyMap);

			// Devuelve
			cf.reset();// Controla la vuelta
			cf.addBasics(DepartmentDao.fields);
			cf.validate(attrList);

			// creamos nuevo keymap, para filtrar por id departamento
			Map<String, Object> idDepart = new HashMap<>() {
				{
					put(DepartmentDao.ATTR_ID, keyMap.get(DepartmentDao.ATTR_ID));

				}
			};

			// devuelve los datos del departamento //keymap
			resultado = departmentService.departmentQuery(idDepart, attrList);

			List<String> listaGastos = new ArrayList<String>() {
				{
					add(dao.ATTR_ID);
					add(dao.ATTR_CONCEPT);
					add(dao.ATTR_DATE);
					add(dao.ATTR_AMOUNT);
				}
			};

			// nuevo keyMap
			Map<String, Object> idHotelDepart = new HashMap<>() {
				{
					put(dao.ATTR_ID_HTL, keyMap.get(HotelDao.ATTR_ID)); // Queremos q el keyMap del principio nos saque
																		// el attrID
					put(dao.ATTR_ID_DPT, keyMap.get(DepartmentDao.ATTR_ID));
				}
			};

			// anido los gastos //id_dpt listasGatos
			EntityResult resultGastos = this.billQuery(idHotelDepart, listaGastos);

			Map<String, Object> mapFinal = new HashMap<String, Object>();

			List<Object> gastosDep = new ArrayList<Object>();
			for (int i = 0; i < resultGastos.calculateRecordNumber(); i++) {
				Object h = resultGastos.getRecordValues(i);
				gastosDep.add(h);
			}

			mapFinal.putAll(resultado.getRecordValues(0));
			mapFinal.put("gastos departamento", gastosDep);

			resultadoFinal.addRecord(mapFinal);

		} catch (ValidateException e) {
			resultado = e.getEntityResult();
		} catch (Exception e) {
			e.printStackTrace();
			return resultado = new EntityResultWrong(ErrorMessage.UNKNOWN_ERROR);
		}
		return resultadoFinal;

	}

	/**
	 * 
	 * 
	 */
	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult gastosDepartamentoHotelQuery(Map<String, Object> keyMap, List<String> attrList)
			throws OntimizeJEERuntimeException {

		EntityResult resultadoFinal = new EntityResultMapImpl(); // Para que no salga el 1 en el json de respuesta

		EntityResult resultado = new EntityResultWrong();
		try {

			List<String> required = new ArrayList<String>() {
				{
					add(HotelDao.ATTR_ID);
				}
			};

			// validamos key
			cf.reset();
			cf.setCPHtlColum(dao.ATTR_ID_HTL);
			cf.setCPRoleUsersRestrictions(UserRoleDao.ROLE_MANAGER);
			
			cf.addBasics(HotelDao.fields);
			cf.setRequired(required);
			cf.setOptional(false);
			cf.validate(keyMap);

			// validamos attr
			cf.reset();
			cf.addBasics(HotelDao.fields);
			cf.validate(attrList);

			resultado = hotelService.hotelQuery(keyMap, attrList);

			// id hotel + id dep
			EntityResult departamento = this.gastosDepartamentoQuery(keyMap, attrList); // hacerlo tantas veces como
																						// departamentos existan en
																						// general

		} catch (ValidateException e) {
			resultado = e.getEntityResult();
		} catch (Exception e) {
			e.printStackTrace();
			return resultado = new EntityResultWrong(ErrorMessage.UNKNOWN_ERROR);
		}
		return resultadoFinal;

	}

	/**
	 * OK Dado un identificador de bill(bll_id), devuelve los datos de las facturas
	 * pertenecientes a dicho hotel htl_name, htl_city, dpt_name, bll_id,
	 * bll_concept, bll_date, bll_amount
	 * 
	 * @param keyMap   (dao.ATTR_ID)
	 * @param attrList (anyList())
	 * @return EntityResult (HotelDao.ATTR_NAME, HotelDao.ATTR_CITY)
	 * @throws OntimizeJEERuntimeException
	 */
	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult billsByHotelDepartmentQuery(Map<String, Object> keyMap, List<String> attrList)
			throws OntimizeJEERuntimeException {

		EntityResult resultado = new EntityResultWrong();
		try {
			/*
			 * List<String> required = new ArrayList<String>() { { add(dao.ATTR_ID_DPT);
			 * add(dao.ATTR_ID_HTL);
			 * 
			 * } };
			 */
			cf.reset();
			cf.setCPHtlColum(dao.ATTR_ID_HTL);
			cf.setCPRoleUsersRestrictions(UserRoleDao.ROLE_MANAGER);
			
			cf.setNoEmptyList(false);
			
			cf.addBasics(HotelDao.fields);
			cf.addBasics(dao.fields);
			cf.addBasics(DepartmentDao.fields);
//			cf.setRequired(required);
//			cf.setOptional(false);
			cf.validate(keyMap);
			cf.validate(attrList);

			/*
			 * //Si no quisiéramos aceptar columnas:
			 * 
			 * cf.reset(); cf.setNoEmptyList(false); cf.validate(attrList);
			 */
			resultado = this.daoHelper.query(this.dao, keyMap, attrList, "billsByHotelDep");

		} catch (ValidateException e) {
			resultado = e.getEntityResult();
		} catch (Exception e) {
			e.printStackTrace();
			resultado = new EntityResultWrong(ErrorMessage.UNKNOWN_ERROR);
		}
		return resultado;
	}
//////
	/*
	 * @Override public EntityResult gastosDepartamentoHotelQuery(Map<String,
	 * Object> keyMap, List<String> attrList) throws OntimizeJEERuntimeException {
	 * 
	 * EntityResult resultadoFinal = new EntityResultMapImpl(); //Para que no salga
	 * el 1 en el json de respuesta//////////// EntityResult resultado = new
	 * EntityResultWrong(); try {
	 * 
	 * cf.reset(); cf.addBasics(HotelDao.fields); cf.addBasics(dao.fields);
	 * cf.addBasics(DepartmentDao.fields); cf.validate(keyMap);
	 * 
	 * List<String> lista = new ArrayList<String>() { { add(HotelDao.ATTR_CITY);
	 * add(HotelDao.ATTR_NAME); } };
	 * 
	 * resultado = this.daoHelper.query(this.billDao, keyMap, lista,
	 * "billsByHotelDep");
	 * 
	 * List<String> listaGastos = new ArrayList<String>() {{ add(dao.ATTR_ID);
	 * add(dao.ATTR_CONCEPT); add(dao.ATTR_DATE); add(dao.ATTR_AMOUNT); } };
	 * 
	 * //anido los gastos //id_dpt listasGastos EntityResult resultGastos =
	 * this.billQuery(resultado.getRecordValues(0), listaGastos);
	 * 
	 * Map<String, Object> mapFinal = new HashMap<String, Object>();
	 * 
	 * List<Object> gastosDep = new ArrayList<Object>(); for (int i = 0; i <
	 * resultado.calculateRecordNumber(); i++) { Object h =
	 * resultado.getRecordValues(i);
	 * 
	 * if(resultado.get(HotelDao.ATTR_CITY) == resultado.get(HotelDao.ATTR_CITY)){
	 * gastosDep.add(h); } // Object h = resultado.get(HotelDao.ATTR_CITY); //
	 * Object g = resultado.get(HotelDao.ATTR_NAME);
	 * 
	 * 
	 * // gastosDep.add(g); }
	 * 
	 * mapFinal.putAll(resultado.getRecordValues(0));
	 * mapFinal.put("gastos departamento", gastosDep);
	 * 
	 * resultadoFinal.addRecord(mapFinal);
	 * 
	 * } catch (ValidateException e) { e.printStackTrace(); return resultadoFinal =
	 * new EntityResultWrong(e.getMessage());
	 * 
	 * } catch (Exception e) { e.printStackTrace(); return resultadoFinal = new
	 * EntityResultWrong(ErrorMessage.UNKNOWN_ERROR); } return resultadoFinal; }
	 */
}
