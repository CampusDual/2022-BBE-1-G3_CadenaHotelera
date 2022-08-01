package com.ontimize.atomicHotelsApiRest.model.core.service;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.SQLWarningException;
import org.springframework.stereotype.Service;

import com.ontimize.atomicHotelsApiRest.api.core.exceptions.EntityResultRequiredException;
import com.ontimize.atomicHotelsApiRest.api.core.exceptions.InvalidFieldsValuesException;
import com.ontimize.atomicHotelsApiRest.api.core.exceptions.MissingFieldsException;
import com.ontimize.atomicHotelsApiRest.api.core.exceptions.ValidateException;
import com.ontimize.atomicHotelsApiRest.api.core.service.ICustomerService;
import com.ontimize.atomicHotelsApiRest.model.core.dao.BookingDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.BookingGuestDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.CustomerDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.FeatureDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.HotelDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.RoomDao;
import com.ontimize.atomicHotelsApiRest.model.core.tools.ControlFields;
import com.ontimize.atomicHotelsApiRest.model.core.tools.EntityResultExtraTools;
import com.ontimize.atomicHotelsApiRest.model.core.tools.EntityResultWrong;
import com.ontimize.atomicHotelsApiRest.model.core.tools.ErrorMessage;
import com.ontimize.atomicHotelsApiRest.model.core.tools.ValidateFields;
import com.ontimize.jee.common.db.SQLStatementBuilder;
import com.ontimize.jee.common.db.SQLStatementBuilder.BasicExpression;
import com.ontimize.jee.common.db.SQLStatementBuilder.BasicField;
import com.ontimize.jee.common.db.SQLStatementBuilder.BasicOperator;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.dto.EntityResultMapImpl;
import com.ontimize.jee.common.exceptions.OntimizeJEERuntimeException;
import com.ontimize.jee.common.tools.EntityResultTools;
import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;

@Service("CustomerService")
@Lazy
public class CustomerService implements ICustomerService {

	@Autowired
	private CustomerDao customerDao;
	@Autowired
	private DefaultOntimizeDaoHelper daoHelper;

	@Autowired
	ControlFields cf;

	@Override
	public EntityResult customerQuery(Map<String, Object> keyMap, List<String> attrList)
			throws OntimizeJEERuntimeException {
//TODO dividir esta consulta el bussiness y regular?
		EntityResult resultado = new EntityResultWrong();
		try {
			cf.reset();
			cf.addBasics(CustomerDao.fields);
			cf.validate(keyMap);
			cf.validate(attrList);

			resultado = this.daoHelper.query(this.customerDao, keyMap, attrList, "queryBasic");

		} catch (ValidateException e) {
			e.printStackTrace();
			resultado = new EntityResultWrong(e.getMessage());

		} catch (Exception e) {
			e.printStackTrace();
			resultado = new EntityResultWrong(ErrorMessage.UNKNOWN_ERROR);
		}
		return resultado;
	}
	@Override
	public EntityResult customerValidBookingHolder(Map<String, Object> keyMap, List<String> attrList)
			throws OntimizeJEERuntimeException {
//TODO dividir esta consulta el bussiness y regular?
		EntityResult resultado = new EntityResultWrong();
		try {
			cf.reset();
			cf.addBasics(CustomerDao.fields);
			cf.validate(keyMap);
			cf.validate(attrList);

			resultado = this.daoHelper.query(this.customerDao, keyMap, attrList, "queryBasic");

		} catch (ValidateException e) {
			e.printStackTrace();
			resultado = new EntityResultWrong(e.getMessage());

		} catch (Exception e) {
			e.printStackTrace();
			resultado = new EntityResultWrong(ErrorMessage.UNKNOWN_ERROR);
		}
		return resultado;
	}
	
	public EntityResult customerBloquedQuery(Object customerId) throws OntimizeJEERuntimeException {
		EntityResult resultado = new EntityResultWrong();

		try {
			Map<String, Object> keyMap = new HashMap<>();
			/*
			  select bkg_cst_id, bgs_cst_id from bookings left join bookings_guests on
			  bgs_bkg_id = bkg_id left join customers on bkg_cst_id = cst_id where bkg_end
			  >= now() and bkg_checkout is null and bkg_canceled is null
			 * 
			 */
			BasicField customer = new BasicField(BookingDao.ATTR_CUSTOMER_ID);
			BasicField guest = new BasicField(BookingGuestDao.ATTR_CST_ID);
			BasicExpression exp01 = new BasicExpression(customer, BasicOperator.EQUAL_OP, customerId);
			BasicExpression exp02 = new BasicExpression(guest, BasicOperator.EQUAL_OP, customerId);
			BasicExpression finaExp = new BasicExpression(exp01, BasicOperator.OR_OP, exp02);
			EntityResultExtraTools.putBasicExpression(keyMap, finaExp);
			resultado = this.daoHelper.query(this.customerDao, keyMap,
					EntityResultTools.attributes(BookingDao.ATTR_CUSTOMER_ID, BookingGuestDao.ATTR_CST_ID),
					"queryBloquedCustomer");

		} catch (Exception e) {
			e.printStackTrace();
			resultado = new EntityResultWrong(ErrorMessage.UNKNOWN_ERROR);
		}
		return resultado;
	}


	@Override
	public EntityResult businessCustomerInsert(Map<String, Object> attrMap) throws OntimizeJEERuntimeException {
		EntityResult resultado = new EntityResultWrong();
		try {

			List<String> required = new ArrayList<String>() {
				{
					add(CustomerDao.ATTR_NAME);
					add(CustomerDao.ATTR_PHONE);
					add(CustomerDao.ATTR_COUNTRY);

					add(CustomerDao.ATTR_VAT_NUMBER);
				}
			};
			List<String> restricted = new ArrayList<String>() {
				{
					add(CustomerDao.ATTR_ID);
					add(CustomerDao.ATTR_SURNAME);
					add(CustomerDao.ATTR_BIRTH_DATE);
					add(CustomerDao.ATTR_IDEN_DOC);
				}
			};
			cf.reset();
			cf.addBasics(CustomerDao.fields);
			cf.setRequired(required);
			cf.setRestricted(restricted);
			cf.validate(attrMap);

			Map<String, Object> subConsultaKeyMap = new HashMap<>() {
				{
					put(CustomerDao.ATTR_VAT_NUMBER, attrMap.get(CustomerDao.ATTR_VAT_NUMBER));
				}
			};

			EntityResult auxEntity = customerQuery(subConsultaKeyMap,
					EntityResultTools.attributes(CustomerDao.ATTR_VAT_NUMBER));
			if (auxEntity.calculateRecordNumber() == 0) { // si no hay registros, insertamos
				resultado = this.daoHelper.insert(this.customerDao, attrMap);

				resultado.setMessage("Business Customer registrado");
			} else {
				resultado = new EntityResultWrong(ErrorMessage.CREATION_ERROR_DUPLICATED_FIELD);
			}

		} catch (ValidateException e) {
			resultado = new EntityResultWrong(e.getMessage());
		} catch (DuplicateKeyException e) {
			resultado = new EntityResultWrong(ErrorMessage.CREATION_ERROR_DUPLICATED_FIELD);
		} catch (Exception e) {
			e.printStackTrace();
			resultado = new EntityResultWrong(ErrorMessage.UNKNOWN_ERROR);
		}
		return resultado;
	}

	@Override
	public EntityResult regularCustomerInsert(Map<String, Object> attrMap) throws OntimizeJEERuntimeException {
		EntityResult resultado = new EntityResultWrong();
		try {

			List<String> required = new ArrayList<String>() {
				{
					add(CustomerDao.ATTR_NAME);
					add(CustomerDao.ATTR_PHONE);
					add(CustomerDao.ATTR_COUNTRY);

					add(CustomerDao.ATTR_SURNAME);
					add(CustomerDao.ATTR_IDEN_DOC);
				}
			};
			List<String> restricted = new ArrayList<String>() {
				{
					add(CustomerDao.ATTR_ID);
					add(CustomerDao.ATTR_VAT_NUMBER);
				}
			};
			cf.reset();
			cf.addBasics(CustomerDao.fields);
			cf.setRequired(required);
			cf.setRestricted(restricted);
			cf.validate(attrMap);

			Map<String, Object> subConsultaKeyMap = new HashMap<>() {
				{
					put(CustomerDao.ATTR_IDEN_DOC, attrMap.get(CustomerDao.ATTR_IDEN_DOC));
				}
			};
//			
			EntityResult auxEntity = customerQuery(subConsultaKeyMap,
					EntityResultTools.attributes(CustomerDao.ATTR_IDEN_DOC));
			if (auxEntity.calculateRecordNumber() == 0) { // si no hay registros, insertamos
				resultado = this.daoHelper.insert(this.customerDao, attrMap);
				resultado.setMessage("Regular Customer registrado");
			} else {
				resultado = new EntityResultWrong(ErrorMessage.CREATION_ERROR_DUPLICATED_FIELD);
			}

		} catch (ValidateException e) {
			resultado = new EntityResultWrong(e.getMessage());

		} catch (DuplicateKeyException e) {
			resultado = new EntityResultWrong(ErrorMessage.CREATION_ERROR_DUPLICATED_FIELD);

		} catch (Exception e) {
			e.printStackTrace();
			resultado = new EntityResultWrong(ErrorMessage.UNKNOWN_ERROR);
		}
		return resultado;
	}

//	@Override
//	public EntityResult customerInsert(Map<String, Object> attrMap) throws OntimizeJEERuntimeException {
//
//		EntityResult resultado = new EntityResultMapImpl();
//
//		try {
//
//			// ValidateFields.emptyFields(attrMap, CustomerDao.ATTR_NAME,
//			// CustomerDao.ATTR_SURNAMES, CustomerDao.ATTR_DNI,
//			// CustomerDao.ATTR_NATIONALITY,CustomerDao.ATTR_PHONE,CustomerDao.ATTR_CREDITCARD,
//			// CustomerDao.ATTR_VALID_DATE);
//
//			ValidateFields.emptyFields(attrMap, CustomerDao.ATTR_NAME, CustomerDao.ATTR_SURNAMES, CustomerDao.ATTR_DNI,
//					CustomerDao.ATTR_NATIONALITY, CustomerDao.ATTR_PHONE, CustomerDao.ATTR_CREDITCARD,
//					CustomerDao.ATTR_VALID_DATE);
//
//			// ValidateFields.checkMail(CustomerDao.ATTR_EMAIL);
//
//			resultado = this.daoHelper.insert(this.customerDao, attrMap);
//
//			resultado.setMessage("Customer registrado");
//
//		} catch (MissingFieldsException e) {
//			resultado = new EntityResultWrong(ErrorMessage.CREATION_ERROR + e.getMessage());
//
//		} catch (DuplicateKeyException e) {
//			resultado = new EntityResultWrong(ErrorMessage.CREATION_ERROR_DUPLICATED_FIELD);
//
//		} catch (Exception e) {
//			resultado = new EntityResultWrong(ErrorMessage.CREATION_ERROR);
//		}
//
//		return resultado;
//
//	}

	@Override
	public EntityResult customerUpdate(Map<String, Object> attrMap, Map<String, Object> keyMap)
			throws OntimizeJEERuntimeException {

		EntityResult resultado = new EntityResultMapImpl();
		try {
			ValidateFields.required(keyMap, CustomerDao.ATTR_ID);
			resultado = this.daoHelper.update(this.customerDao, attrMap, keyMap);
			if (resultado.getCode() == EntityResult.OPERATION_SUCCESSFUL_SHOW_MESSAGE) {
				resultado = new EntityResultWrong(ErrorMessage.UPDATE_ERROR_MISSING_FIELD);
			} else {
				resultado.setMessage("Customer actualizado");
			}
		} catch (ValidateException e) {
			resultado = new EntityResultWrong(ErrorMessage.UPDATE_ERROR + e.getMessage());
		} catch (DuplicateKeyException e) {
			e.printStackTrace();
			resultado = new EntityResultWrong(ErrorMessage.UPDATE_ERROR_DUPLICATED_FIELD);
		} catch (DataIntegrityViolationException e) {
			e.printStackTrace();
			resultado = new EntityResultWrong(ErrorMessage.UPDATE_ERROR_REQUIRED_FIELDS);
		} catch (Exception e) {
			e.printStackTrace();
			resultado = new EntityResultWrong(ErrorMessage.UPDATE_ERROR);
		}
		return resultado;
	}

	@Override
	public EntityResult customerDelete(Map<String, Object> keyMap) throws OntimizeJEERuntimeException {

		EntityResult resultado = new EntityResultWrong("Operación no disponible");
//		try {
//			ValidateFields.required(keyMap, CustomerDao.ATTR_ID);
//
//			EntityResult auxEntity = this.daoHelper.query(this.customerDao,
//					EntityResultTools.keysvalues(CustomerDao.ATTR_ID, keyMap.get(CustomerDao.ATTR_ID)),
//					EntityResultTools.attributes(CustomerDao.ATTR_ID));
//			if (auxEntity.calculateRecordNumber() == 0) { // si no hay registros...
//				resultado = new EntityResultWrong(ErrorMessage.DELETE_ERROR_MISSING_FIELD);
//			} else {
//				resultado = this.daoHelper.delete(this.customerDao, keyMap);
//				resultado.setMessage("Customer eliminado");
//			}
//		} catch (MissingFieldsException e) {
//			resultado = new EntityResultWrong(ErrorMessage.DELETE_ERROR + e.getMessage());
//		} catch (DataIntegrityViolationException e) {
//			resultado = new EntityResultWrong(ErrorMessage.DELETE_ERROR_FOREING_KEY);
//		} catch (Exception e) {
//			e.printStackTrace();
//			resultado = new EntityResultWrong(ErrorMessage.DELETE_ERROR);
//		}
		return resultado;

	}

	@Override
	public EntityResult customerCancelUpdate(Map<String, Object> attrMap, Map<String, Object> keyMap)
			throws OntimizeJEERuntimeException {
//TODO no cancelar usuarios en uso  en booking si no finalizado la fecha.
		EntityResult resultado = new EntityResultWrong();
		try {
			cf.reset();
			cf.addBasics(CustomerDao.fields);
			cf.setRequired(new ArrayList<>() {
				{
					add(CustomerDao.ATTR_ID);
				}
			});
			cf.setOptional(false);
			cf.validate(keyMap);

			cf.reset();
			cf.addBasics(CustomerDao.fields);
			cf.setRequired(new ArrayList<>() {
				{
					add(CustomerDao.NON_ATTR_ACTION);
				}
			});
			cf.setOptional(false);
			cf.validate(attrMap);

			Map<String, Object> subConsultaKeyMap = new HashMap<>() {
				{
					put(CustomerDao.ATTR_ID, keyMap.get(CustomerDao.ATTR_ID));
				}
			};

			EntityResult auxEntity = customerQuery(subConsultaKeyMap,
					EntityResultTools.attributes(CustomerDao.ATTR_ID));
			if (auxEntity.calculateRecordNumber() == 0) { // si no hay registros...
				resultado = new EntityResultWrong(ErrorMessage.UPDATE_ERROR_MISSING_FIELD);

			} else {
				auxEntity = customerBloquedQuery(keyMap.get(CustomerDao.ATTR_ID));
				if (auxEntity.getCode() != EntityResult.OPERATION_WRONG && auxEntity.calculateRecordNumber() == 0) { // si
																														// no
																														// está
																														// siendo
																														// usado
																														// el
																														// cliente

					Map<String, Object> finalAttrMap = new HashMap<>() {
						{
							put(CustomerDao.ATTR_CANCELED, new Date());
						}
					};
					resultado = this.daoHelper.update(this.customerDao, finalAttrMap, keyMap);

					if (resultado.getCode() == EntityResult.OPERATION_SUCCESSFUL_SHOW_MESSAGE) {
						resultado = new EntityResultWrong(ErrorMessage.UPDATE_ERROR_MISSING_FIELD);
					} else {
						resultado = new EntityResultMapImpl();
						resultado.setMessage("Customer canceled");
					}
				} else {
					resultado = new EntityResultWrong(ErrorMessage.BLOCKED_CUSTOMER);
				}
			}
		} catch (ValidateException e) {
			resultado = new EntityResultWrong(ErrorMessage.UPDATE_ERROR + e.getMessage());
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

	/**
	 * Creamos dos métodos para leer, mostrar y restringir mails de customers. En el
	 * primero llamamos al método alojado en la Interfaz y lo sobreescribimos, para
	 * posteriormente realizar lo dispuesto en el xml. En el segundo, validamos con
	 * el restricted de la clase ValidateFields, y creamos una basicExpression en la
	 * que indicamos que el campo mailAgreement, debe ser true
	 */
	@Override
	public EntityResult mailAgreementQuery(Map<String, Object> keyMap, List<String> attrList)
			throws OntimizeJEERuntimeException {
		EntityResult resultado = this.daoHelper.query(this.customerDao, keyMap, attrList, "queryAgreementEmails");
		return resultado;
	}

//	@Override
//	public EntityResult mailAgreementBasicExpressionQuery(Map<String, Object> keyMap, List<String> attrList)
//			throws OntimizeJEERuntimeException {
//		ValidateFields.restricted(attrList, CustomerDao.ATTR_CREDITCARD, CustomerDao.ATTR_PHONE);
//
//		BasicField mailAgreement = new BasicField(CustomerDao.ATTR_MAIL_AGREEMENT);
//		BasicExpression expresion = new BasicExpression(mailAgreement, BasicOperator.EQUAL_OP, true);
//		EntityResultExtraTools.putBasicExpression(keyMap, expresion);
//		return this.daoHelper.query(this.customerDao, keyMap, attrList);
//	}

	/**
	 * Método para comprobar que los datos del atributo país, concuerden con los de
	 * la tabla countries
	 * 
	 */
	/*
	 * @Override public EntityResult checkCountryQuery(Map<String, Object> keyMap,
	 * List<String> attrList) throws OntimizeJEERuntimeException { EntityResult
	 * resultado = new EntityResultMapImpl(); try{ List<String> attrlist =
	 * Arrays.asList(CustomerDao.ATTR_COUNTRY); Map<String, Object> col = new
	 * HashMap<>(); col.put(CustomerDao.ATTR_COUNTRY,
	 * attrMap.get(CustomerDao.ATTR_COUNTRY)); EntityResult country=
	 * this.daoHelper.query(this.customerDao, col, attrlist);
	 * 
	 * if(country!= )
	 * 
	 * 
	 * }
	 * 
	 * // EntityResult resultado = this.daoHelper.query(this.customerDao, keyMap,
	 * attrList, "queryCheckCountry"); // return resultado;
	 * 
	 * 
	 * //mirar atributo country cuando se cambie customers //
	 * ValidateFields.emptyField(attrList, CustomerDao.ATTR_COUNTRY);
	 * 
	 * }
	 */

}
