package com.ontimize.atomicHotelsApiRest.model.core.service;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.postgresql.xml.EmptyStringEntityResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.SQLWarningException;
import org.springframework.stereotype.Service;

import com.ontimize.atomicHotelsApiRest.api.core.exceptions.InvalidFieldsValuesException;
import com.ontimize.atomicHotelsApiRest.api.core.exceptions.MissingColumnsException;
import com.ontimize.atomicHotelsApiRest.api.core.exceptions.MissingFieldsException;
import com.ontimize.atomicHotelsApiRest.api.core.service.IHotelService;
import com.ontimize.atomicHotelsApiRest.model.core.dao.CustomerDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.HotelDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.ReceiptDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.RoomTypeDao;
import com.ontimize.atomicHotelsApiRest.model.core.tools.EntityResultWrong;
import com.ontimize.atomicHotelsApiRest.model.core.tools.ErrorMessage;
import com.ontimize.atomicHotelsApiRest.model.core.tools.ValidateFields;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.dto.EntityResultMapImpl;
import com.ontimize.jee.common.exceptions.OntimizeJEERuntimeException;
import com.ontimize.jee.common.tools.EntityResultTools;
import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;

@Service("HotelService")
@Lazy
public class HotelService implements IHotelService {

	@Autowired
	private HotelDao hotelDao;
	@Autowired
	private DefaultOntimizeDaoHelper daoHelper;

	@Override
	public EntityResult hotelQuery(Map<String, Object> keyMap, List<String> attrList)
			throws OntimizeJEERuntimeException {
		
		EntityResult resultado = new EntityResultMapImpl();

		try {
		ValidateFields.atLeastOneRequired(keyMap, HotelDao.ATTR_ID,HotelDao.ATTR_NAME,HotelDao.ATTR_STREET,HotelDao.ATTR_CITY,HotelDao.ATTR_CP,HotelDao.ATTR_STATE,HotelDao.ATTR_COUNTRY,HotelDao.ATTR_PHONE,HotelDao.ATTR_EMAIL,HotelDao.ATTR_DESCRIPTION,HotelDao.ATTR_IS_OPEN);
		ValidateFields.onlyThis(keyMap, HotelDao.ATTR_ID,HotelDao.ATTR_NAME,HotelDao.ATTR_STREET,HotelDao.ATTR_CITY,HotelDao.ATTR_CP,HotelDao.ATTR_STATE,HotelDao.ATTR_COUNTRY,HotelDao.ATTR_PHONE,HotelDao.ATTR_EMAIL,HotelDao.ATTR_DESCRIPTION,HotelDao.ATTR_IS_OPEN);
		ValidateFields.isInt(keyMap,HotelDao.ATTR_ID,HotelDao.ATTR_IS_OPEN);
		//TODO ver qué hacer si se quiere buscar por nules ya que con esto no se aceptan!!
		ValidateFields.isString(keyMap,HotelDao.ATTR_NAME,HotelDao.ATTR_STREET,HotelDao.ATTR_CITY,HotelDao.ATTR_CP,HotelDao.ATTR_STATE,HotelDao.ATTR_COUNTRY,HotelDao.ATTR_PHONE,HotelDao.ATTR_EMAIL,HotelDao.ATTR_DESCRIPTION);

		
		ValidateFields.atLeastOneRequired(attrList, HotelDao.ATTR_ID,HotelDao.ATTR_NAME,HotelDao.ATTR_STREET,HotelDao.ATTR_CITY,HotelDao.ATTR_CP,HotelDao.ATTR_STATE,HotelDao.ATTR_COUNTRY,HotelDao.ATTR_PHONE,HotelDao.ATTR_EMAIL,HotelDao.ATTR_DESCRIPTION,HotelDao.ATTR_IS_OPEN);
		ValidateFields.onlyThis(attrList, HotelDao.ATTR_ID,HotelDao.ATTR_NAME,HotelDao.ATTR_STREET,HotelDao.ATTR_CITY,HotelDao.ATTR_CP,HotelDao.ATTR_STATE,HotelDao.ATTR_COUNTRY,HotelDao.ATTR_PHONE,HotelDao.ATTR_EMAIL,HotelDao.ATTR_DESCRIPTION,HotelDao.ATTR_IS_OPEN);
		
		resultado = this.daoHelper.query(this.hotelDao, keyMap, attrList);
		
		}catch(MissingFieldsException e) {
			e.printStackTrace();
			resultado = new EntityResultWrong(ErrorMessage.REQUIRED_FIELDS);
		}catch(MissingColumnsException e) {
			e.printStackTrace();
			resultado = new EntityResultWrong(ErrorMessage.REQUIRED_COLUMNS);
		}catch(InvalidFieldsValuesException e) {
			e.printStackTrace();
			resultado = new EntityResultWrong(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			resultado = new EntityResultWrong(e.getMessage());
		}
		return resultado;
	}

	@Override
	public EntityResult hotelInsert(Map<String, Object> attrMap) throws OntimizeJEERuntimeException{
		
		EntityResult resultado = new EntityResultMapImpl();
		try {

			ValidateFields.required(attrMap, HotelDao.ATTR_IS_OPEN);
			ValidateFields.emptyFields(attrMap, HotelDao.ATTR_NAME, HotelDao.ATTR_STREET, HotelDao.ATTR_CITY,
					HotelDao.ATTR_CP, HotelDao.ATTR_STATE, HotelDao.ATTR_COUNTRY);
			ValidateFields.onlyThis(attrMap, HotelDao.ATTR_NAME, HotelDao.ATTR_STREET, HotelDao.ATTR_CITY,
					HotelDao.ATTR_CP, HotelDao.ATTR_STATE, HotelDao.ATTR_COUNTRY,HotelDao.ATTR_IS_OPEN,HotelDao.ATTR_PHONE,HotelDao.ATTR_EMAIL);
			ValidateFields.checkMail((String) attrMap.get(HotelDao.ATTR_EMAIL));// Para obtener el valor introducido, attrMap.get, sino, valida simplemente el nombre que le damos al campo, no el valor 
	
			resultado = this.daoHelper.insert(this.hotelDao, attrMap);
			resultado.setMessage("Hotel registrado");

		} catch (MissingFieldsException e) {
			resultado = new EntityResultWrong(ErrorMessage.CREATION_ERROR + e.getMessage());
			
		}catch(InvalidFieldsValuesException e) {
			resultado = new EntityResultWrong(ErrorMessage.CREATION_ERROR + e.getMessage());	
			
		} catch (DuplicateKeyException e) {
			resultado = new EntityResultWrong(ErrorMessage.CREATION_ERROR_DUPLICATED_FIELD);

		} 
//		catch (Exception e) {
//			resultado = new EntityResultWrong(ErrorMessage.CREATION_ERROR);
//		}

		// OPCION A (comprobando si el registro ya existe)
//		if (attrMap.containsKey(HotelDao.ATTR_NAME)) {
//			Map<String, Object> auxKeyMap = new HashMap<String, Object>();
//			List<String> auxAttrList = new ArrayList<String>();
//			auxKeyMap.put(HotelDao.ATTR_NAME, attrMap.get(HotelDao.ATTR_NAME));
//			auxAttrList.add(HotelDao.ATTR_NAME);
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
//		if (attrMap.containsKey(HotelDao.ATTR_NAME)) {
//			EntityResult auxEntity = this.daoHelper.query(this.hotelDao,
//					EntityResultTools.keysvalues(HotelDao.ATTR_NAME, attrMap.get(HotelDao.ATTR_NAME)),
//					EntityResultTools.attributes(HotelDao.ATTR_NAME));
//			if (auxEntity.calculateRecordNumber() == 0) { // si no hay registros...
//				resultado = this.daoHelper.insert(this.hotelDao, attrMap);
//			} else {				
//				resultado = new EntityResultWrong("Error al crear Hotel - El registro ya existe");
//			}
//		}
		return resultado;
	}

	@Override
	public EntityResult hotelUpdate(Map<String, Object> attrMap, Map<String, Object> keyMap)
			throws OntimizeJEERuntimeException {
		EntityResult resultado = new EntityResultMapImpl();
		try {
			//TODO pendiente comprobar que funcione bien
			ValidateFields.required(keyMap, HotelDao.ATTR_ID);
			ValidateFields.onlyThis(keyMap, HotelDao.ATTR_ID);
			ValidateFields.isInt(keyMap, HotelDao.ATTR_ID);
			
			ValidateFields.atLeastOneRequired(attrMap, HotelDao.ATTR_NAME, HotelDao.ATTR_STREET, HotelDao.ATTR_CITY,
					HotelDao.ATTR_CP, HotelDao.ATTR_STATE, HotelDao.ATTR_COUNTRY,HotelDao.ATTR_PHONE,HotelDao.ATTR_EMAIL,HotelDao.ATTR_IS_OPEN,HotelDao.ATTR_DESCRIPTION);
			ValidateFields.onlyThis(attrMap, HotelDao.ATTR_NAME, HotelDao.ATTR_STREET, HotelDao.ATTR_CITY,
					HotelDao.ATTR_CP, HotelDao.ATTR_STATE, HotelDao.ATTR_COUNTRY,HotelDao.ATTR_PHONE,HotelDao.ATTR_EMAIL,HotelDao.ATTR_IS_OPEN,HotelDao.ATTR_DESCRIPTION);
			ValidateFields.isInt(attrMap, HotelDao.ATTR_IS_OPEN);
			
////		TODO	if(mail está en la lista entonces hacer el check mail..)
//			ValidateFields.checkMail((String) attrMap.get(HotelDao.ATTR_EMAIL));
			//TODO qué pasa con no dejar que se metan cadenas vacías cuando todos los campos no son requeridos??
			
			resultado = this.daoHelper.update(this.hotelDao, attrMap, keyMap);
			if (resultado.getCode() == EntityResult.OPERATION_SUCCESSFUL_SHOW_MESSAGE) {
				resultado = new EntityResultWrong(ErrorMessage.UPDATE_ERROR_MISSING_FIELD);
			} else {
				resultado.setMessage("Hotel actualizado");
			}
		} catch (MissingFieldsException e) {
			resultado = new EntityResultWrong(ErrorMessage.UPDATE_ERROR +" - "+ e.getMessage());		
		}catch(InvalidFieldsValuesException e) {
			resultado = new EntityResultWrong(ErrorMessage.UPDATE_ERROR +" - "+ e.getMessage());
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
	public EntityResult hotelDelete(Map<String, Object> keyMap) throws OntimizeJEERuntimeException {

		EntityResult resultado = new EntityResultMapImpl();
		try {
			ValidateFields.required(keyMap, HotelDao.ATTR_ID);
			ValidateFields.onlyThis(keyMap, HotelDao.ATTR_ID);
			ValidateFields.isInt(keyMap, HotelDao.ATTR_ID);

			EntityResult auxEntity = this.daoHelper.query(this.hotelDao,
					EntityResultTools.keysvalues(HotelDao.ATTR_ID, keyMap.get(HotelDao.ATTR_ID)),
					EntityResultTools.attributes(HotelDao.ATTR_ID));
			if (auxEntity.calculateRecordNumber() == 0) { // si no hay registros...
				resultado = new EntityResultWrong(ErrorMessage.DELETE_ERROR_MISSING_FIELD);
			} else {
				resultado = this.daoHelper.delete(this.hotelDao, keyMap);
				resultado.setMessage("Hotel eliminado");
			}
		} catch (MissingFieldsException e) {
			resultado = new EntityResultWrong(ErrorMessage.DELETE_ERROR + e.getMessage());
		}catch(InvalidFieldsValuesException e) {
			resultado = new EntityResultWrong(ErrorMessage.DELETE_ERROR +" - "+ e.getMessage());
		} catch (DataIntegrityViolationException e) {
			resultado = new EntityResultWrong(ErrorMessage.DELETE_ERROR_FOREING_KEY);
		} catch (Exception e) {
			resultado = new EntityResultWrong(ErrorMessage.DELETE_ERROR);
		}
		return resultado;
	}

	public EntityResult hotelDataQuery(Map<String, Object> keysValues, List<String> attrList) {
		EntityResult queryRes = this.daoHelper.query(this.hotelDao,
				EntityResultTools.keysvalues(HotelDao.ATTR_ID, keysValues.get(HotelDao.ATTR_ID)),
				EntityResultTools.attributes(HotelDao.ATTR_ID, HotelDao.ATTR_NAME, HotelDao.ATTR_STREET,
						HotelDao.ATTR_CITY, HotelDao.ATTR_CP, HotelDao.ATTR_STATE, HotelDao.ATTR_COUNTRY,
						HotelDao.ATTR_PHONE, HotelDao.ATTR_EMAIL, HotelDao.ATTR_DESCRIPTION, HotelDao.ATTR_IS_OPEN),
				"queryHotel");
		return queryRes;
	}

}