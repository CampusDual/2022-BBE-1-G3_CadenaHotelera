package com.ontimize.atomicHotelsApiRest.model.core.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import com.ontimize.atomicHotelsApiRest.api.core.exceptions.LiadaPardaException;
import com.ontimize.atomicHotelsApiRest.api.core.exceptions.MissingFieldsException;
import com.ontimize.atomicHotelsApiRest.api.core.exceptions.ValidateException;
import com.ontimize.atomicHotelsApiRest.api.core.service.IFeatureService;
import com.ontimize.atomicHotelsApiRest.api.core.service.IRoomTypeFeatureService;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.dto.EntityResultMapImpl;
import com.ontimize.jee.common.exceptions.OntimizeJEERuntimeException;
import com.ontimize.jee.common.security.PermissionsProviderSecured;
import com.ontimize.jee.common.tools.EntityResultTools;
import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;
import com.ontimize.atomicHotelsApiRest.model.core.dao.CreditCardDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.FeatureDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.HotelDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.RoomDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.RoomTypeDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.RoomTypeFeatureDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.UserRoleDao;
import com.ontimize.atomicHotelsApiRest.model.core.tools.ControlFields;
import com.ontimize.atomicHotelsApiRest.model.core.tools.EntityResultWrong;
import com.ontimize.atomicHotelsApiRest.model.core.tools.ErrorMessage;
import com.ontimize.atomicHotelsApiRest.model.core.tools.ValidateFields;

@Service("RoomTypeFeatureService")
@Lazy
public class RoomTypeFeatureService implements IRoomTypeFeatureService{
	
	@Autowired
	private RoomTypeFeatureDao dao;
	@Autowired
	private DefaultOntimizeDaoHelper daoHelper;

	@Autowired
	ControlFields cf;
	
	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult roomTypeFeatureQuery(Map<String, Object> keyMap, List<String> attrList)
			throws OntimizeJEERuntimeException {
		EntityResult resultado = new EntityResultWrong();

		try {

			// Control del filtro
			cf.reset();
						
			cf.addBasics(dao.fields);
//			cf.setOptional(true);//El resto de los campos de fields serán aceptados, por defecto true			
			cf.validate(keyMap);

			cf.validate(attrList);// reutilizamos los mismos criterios para validar attrList

			resultado = this.daoHelper.query(this.dao, keyMap, attrList);

		} catch (ValidateException e) {
			resultado = e.getEntityResult();
		} catch (Exception e) {
			e.printStackTrace();
			resultado = new EntityResultWrong(ErrorMessage.ERROR);
		}
		return resultado;		
	}
	
	
	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult roomTypeFeatureInsert(Map<String, Object> attrMap) throws OntimizeJEERuntimeException {
		
		EntityResult resultado = new EntityResultWrong();
		try {
			
			cf.reset();
			List<String> required = new ArrayList<String>() {
				{
					add(dao.ATTR_ROOM_ID);
					add(dao.ATTR_FEATURE_ID);
				}
			};
/*			List<String> restricted = new ArrayList<String>() {
				{
					add(dao.ATTR_ROOM_ID);// No quiero que meta el id porque quiero el id autogenerado de la base de
					add(dao.ATTR_FEATURE_ID);	// datos
				}
			};
*/
			cf.addBasics(dao.fields);
			cf.setRequired(required);
//			cf.setRestricted(restricted);
//			cf.setOptional(true);//No existen más campos. No es neceario ponerlo
			cf.validate(attrMap);

			resultado = this.daoHelper.insert(this.dao, attrMap);
			resultado.setMessage("Room type features registered");

		} catch (ValidateException e) {
			resultado = e.getEntityResult();
		} catch (DuplicateKeyException e) {
			resultado = new EntityResultWrong(ErrorMessage.CREATION_ERROR_DUPLICATED_FIELD);
		} catch (DataIntegrityViolationException e) {
			resultado = new EntityResultWrong(ErrorMessage.CREATION_ERROR_MISSING_FK);
		} catch (Exception e) {
			e.printStackTrace();
			resultado = new EntityResultWrong(ErrorMessage.UNKNOWN_ERROR);
		}

/*			ValidateFields.required(attrMap, dao.ATTR_FEATURE_ID, dao.ATTR_ROOM_ID);	
			resultado = this.daoHelper.insert(this.roomTypeFeatureDao, attrMap);	
			resultado.setMessage("RoomTypeFeature registrada");

		} catch (MissingFieldsException e) {
			resultado = new EntityResultWrong(ErrorMessage.CREATION_ERROR + e.getMessage());			
		} catch (DuplicateKeyException e) {
			resultado = new EntityResultWrong(ErrorMessage.CREATION_ERROR_DUPLICATED_FIELD);
		}catch (DataIntegrityViolationException e) {
			resultado = new EntityResultWrong(ErrorMessage.UPDATE_ERROR_MISSING_FK);		
		} catch (Exception e) {
			resultado = new EntityResultWrong(ErrorMessage.CREATION_ERROR);
		}
*/		
		return resultado;
	}
	
	
	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult roomTypeFeatureDelete(Map<String, Object> keyMap) throws OntimizeJEERuntimeException {
		
		EntityResult resultado = new EntityResultWrong();

		try {
			List<String> required = new ArrayList<String>() {
				{
					add(dao.ATTR_ROOM_ID);
					add(dao.ATTR_FEATURE_ID);
				}
			};
			cf.reset();
			cf.addBasics(dao.fields);
			cf.setRequired(required);
			cf.setOptional(false);
			cf.validate(keyMap);

			Map<String, Object> consultaKeyMap = new HashMap<>() {
				{
					put(dao.ATTR_ROOM_ID, keyMap.get(dao.ATTR_ROOM_ID));
					put(dao.ATTR_FEATURE_ID, keyMap.get(dao.ATTR_FEATURE_ID));
				}
			};

			EntityResult auxEntity = roomTypeFeatureQuery(consultaKeyMap, EntityResultTools.attributes(dao.ATTR_ROOM_ID, dao.ATTR_FEATURE_ID));

			if (auxEntity.calculateRecordNumber() == 0) { // si no hay registros...
				resultado = new EntityResultWrong(ErrorMessage.DELETE_ERROR_MISSING_FIELD);
			} else {
				resultado = this.daoHelper.delete(this.dao, keyMap);
				resultado.setMessage("Room type features deleted");
			}

		} catch (ValidateException e) {
			resultado = e.getEntityResult();
		} catch (Exception e) {
			e.printStackTrace();
			resultado = new EntityResultWrong(ErrorMessage.UNKNOWN_ERROR);
		}
		return resultado;
	}	
/*		EntityResult resultado = new EntityResultMapImpl();
		try {
			ValidateFields.required(keyMap, dao.ATTR_FEATURE_ID,dao.ATTR_ROOM_ID);

			EntityResult auxEntity = this.daoHelper.query(this.roomTypeFeatureDao,
					EntityResultTools.keysvalues(dao.ATTR_FEATURE_ID, keyMap.get(dao.ATTR_FEATURE_ID),dao.ATTR_ROOM_ID, keyMap.get(dao.ATTR_ROOM_ID)),
					EntityResultTools.attributes(dao.ATTR_FEATURE_ID,dao.ATTR_ROOM_ID));
			if (auxEntity.calculateRecordNumber() == 0) { // si no hay registros...
				resultado = new EntityResultWrong(ErrorMessage.DELETE_ERROR_MISSING_FIELD);
			} else {
				resultado = this.daoHelper.delete(this.roomTypeFeatureDao, keyMap);
				resultado.setMessage("RoomTypeFeature eliminada");
			}
		} catch (MissingFieldsException e) {
			resultado = new EntityResultWrong(ErrorMessage.DELETE_ERROR + e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			resultado = new EntityResultWrong(ErrorMessage.DELETE_ERROR);
		}
		return resultado;
	}
*/
}
