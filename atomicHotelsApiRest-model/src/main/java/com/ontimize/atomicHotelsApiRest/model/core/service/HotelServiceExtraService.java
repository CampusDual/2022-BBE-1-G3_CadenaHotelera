package com.ontimize.atomicHotelsApiRest.model.core.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import com.ontimize.atomicHotelsApiRest.api.core.exceptions.MissingFieldsException;
import com.ontimize.atomicHotelsApiRest.api.core.exceptions.ValidateException;
import com.ontimize.atomicHotelsApiRest.api.core.service.IHotelServiceExtraService;
import com.ontimize.atomicHotelsApiRest.model.core.dao.HotelDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.HotelServiceExtraDao;
import com.ontimize.atomicHotelsApiRest.model.core.tools.ControlFields;
import com.ontimize.atomicHotelsApiRest.model.core.tools.EntityResultWrong;
import com.ontimize.atomicHotelsApiRest.model.core.tools.ErrorMessage;
import com.ontimize.atomicHotelsApiRest.model.core.tools.ValidateFields;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.dto.EntityResultMapImpl;
import com.ontimize.jee.common.exceptions.OntimizeJEERuntimeException;
import com.ontimize.jee.common.tools.EntityResultTools;
import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;

@Service("HotelServiceExtraService")
@Lazy
public class HotelServiceExtraService implements IHotelServiceExtraService {

	@Autowired
	private HotelServiceExtraDao hotelServiceExtraDao;

	@Autowired
	private DefaultOntimizeDaoHelper daoHelper;
	
	@Autowired
	ControlFields cf;

	public EntityResult hotelServiceExtraQuery(Map<String, Object> keyMap, List<String> attrList)
			throws OntimizeJEERuntimeException {
		EntityResult resultado = new EntityResultWrong();
		try {

			cf.reset();
			cf.addBasics(HotelServiceExtraDao.fields);
			cf.validate(keyMap);

			cf.validate(attrList);

			resultado = this.daoHelper.query(this.hotelServiceExtraDao, keyMap, attrList);

		} catch (ValidateException e) {
			resultado = new EntityResultWrong(e.getMessage());
		} catch (Exception e) {
			resultado = new EntityResultWrong(ErrorMessage.ERROR);
		}
		return resultado;
	}

	@Override
	public EntityResult hotelServiceExtraInsert(Map<String, Object> attrMap)
			throws OntimizeJEERuntimeException, MissingFieldsException {

		EntityResult resultado = new EntityResultWrong();
		try {

			cf.reset();
			List<String> required = new ArrayList<String>() {
				{
					add(HotelServiceExtraDao.ATTR_ID_HTL);
					add(HotelServiceExtraDao.ATTR_ID_SXT);
					add(HotelServiceExtraDao.ATTR_PRECIO);
				}
			};
			List<String> restricted = new ArrayList<String>() {
				{
					add(HotelServiceExtraDao.ATTR_ID);
				}
			};

			cf.addBasics(HotelServiceExtraDao.fields);
			cf.setRequired(required);
			cf.setRestricted(restricted);
			cf.validate(attrMap);

			resultado = this.daoHelper.insert(this.hotelServiceExtraDao, attrMap);
			resultado.setMessage("HotelServiceExtra registrado");

		} catch (ValidateException e) {
			resultado = new EntityResultWrong(e.getMessage());
		} catch (DuplicateKeyException e) {
			resultado = new EntityResultWrong(ErrorMessage.CREATION_ERROR_DUPLICATED_FIELD);
		} catch (DataIntegrityViolationException e) {
			resultado = new EntityResultWrong(ErrorMessage.CREATION_ERROR_MISSING_FK);
		} catch (Exception e) {
			resultado = new EntityResultWrong(ErrorMessage.CREATION_ERROR + e.getMessage());
		}
		return resultado;
	}

	@Override
	public EntityResult hotelServiceExtraUpdate(Map<String, Object> attrMap, Map<String, Object> keyMap)
			throws OntimizeJEERuntimeException {
		EntityResult resultado = new EntityResultWrong();
		try {
			
			//ControlFields del filtro
			List<String> requiredFilter = new ArrayList<String>() {{
				add(HotelServiceExtraDao.ATTR_ID);
			}};	
			cf.reset();	
			cf.addBasics(HotelServiceExtraDao.fields);
			cf.setRequired(requiredFilter);
			cf.setOptional(false);//No será aceptado ningún campo que no esté en required
			cf.validate(keyMap);	
			
			
			
			//ControlFields de los nuevos datos
			List<String> restrictedData = new ArrayList<String>() {{
				add(HotelServiceExtraDao.ATTR_ID);//El id no se puede actualizar
			}};
			cf.reset();
			cf.addBasics(HotelServiceExtraDao.fields);
			cf.setRestricted(restrictedData);
//			cf.setOptional(true); //No es necesario ponerlo
			cf.validate(attrMap);
			
			resultado = this.daoHelper.update(this.hotelServiceExtraDao, attrMap, keyMap);
			
			if (resultado.getCode() == EntityResult.OPERATION_SUCCESSFUL_SHOW_MESSAGE) {
				resultado = new EntityResultWrong(ErrorMessage.UPDATE_ERROR_MISSING_FIELD);
			} else {
				resultado.setMessage("Service Extra actualizado");
			}
		} catch (ValidateException e) {
			resultado = new EntityResultWrong(e.getMessage());
		} catch (DuplicateKeyException e) {
			resultado = new EntityResultWrong(ErrorMessage.UPDATE_ERROR_DUPLICATED_FIELD);
		} catch (DataIntegrityViolationException e) {// Puede ser que se meta una FK que no exista o se le ponga null al
														// precio cuando no se debería permitir
			resultado = new EntityResultWrong(
					ErrorMessage.UPDATE_ERROR_MISSING_FK + " / " + ErrorMessage.UPDATE_ERROR_REQUIRED_FIELDS);
		} catch (Exception e) {
			resultado = new EntityResultWrong(ErrorMessage.UPDATE_ERROR);
		}
		return resultado;
	}

	@Override
	public EntityResult hotelServiceExtraDelete(Map<String, Object> keyMap) throws OntimizeJEERuntimeException {

		EntityResult resultado = new EntityResultWrong();
		try {
			
			List<String> required = new ArrayList<String>() {{
				add(HotelServiceExtraDao.ATTR_ID);
			}};
			cf.reset();
			cf.addBasics(HotelServiceExtraDao.fields);
			cf.setRequired(required);
			cf.setOptional(false);
			cf.validate(keyMap);

			
			Map<String, Object> consultaKeyMap = new HashMap<>() { {
				put(HotelServiceExtraDao.ATTR_ID, keyMap.get(HotelServiceExtraDao.ATTR_ID));
				}
			};
			
			EntityResult auxEntity = hotelServiceExtraQuery(consultaKeyMap, 
					EntityResultTools.attributes(HotelServiceExtraDao.ATTR_ID));
			
			if (auxEntity.calculateRecordNumber() == 0) { // si no hay registros...
				resultado = new EntityResultWrong(ErrorMessage.DELETE_ERROR_MISSING_FIELD);
			} else {
				resultado = this.daoHelper.delete(this.hotelServiceExtraDao, keyMap);
				resultado.setMessage("HotelServiceExtra eliminado");
			}
			
		} catch (ValidateException e) {
			resultado = new EntityResultWrong(e.getMessage());
		} catch (Exception e) {
			resultado = new EntityResultWrong(ErrorMessage.DELETE_ERROR);
		}
		return resultado;
	}

}
