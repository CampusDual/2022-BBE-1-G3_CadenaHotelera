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
import com.ontimize.atomicHotelsApiRest.api.core.service.IFeatureService;
import com.ontimize.atomicHotelsApiRest.api.core.service.IRoomTypeFeatureService;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.dto.EntityResultMapImpl;
import com.ontimize.jee.common.exceptions.OntimizeJEERuntimeException;
import com.ontimize.jee.common.tools.EntityResultTools;
import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;
import com.ontimize.atomicHotelsApiRest.model.core.dao.CreditCardDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.FeatureDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.HotelDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.RoomTypeDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.RoomTypeFeatureDao;
import com.ontimize.atomicHotelsApiRest.model.core.tools.ControlFields;
import com.ontimize.atomicHotelsApiRest.model.core.tools.EntityResultWrong;
import com.ontimize.atomicHotelsApiRest.model.core.tools.ErrorMessage;
import com.ontimize.atomicHotelsApiRest.model.core.tools.ValidateFields;

@Service("RoomTypeFeatureService")
@Lazy
public class RoomTypeFeatureService implements IRoomTypeFeatureService{
	
	@Autowired
	private RoomTypeFeatureDao roomTypeFeatureDao;
	@Autowired
	private DefaultOntimizeDaoHelper daoHelper;
	
	@Override
	public EntityResult roomTypeFeatureQuery(Map<String, Object> keyMap, List<String> attrList)
			throws OntimizeJEERuntimeException {
		EntityResult resultado = new EntityResultWrong();

		try {

			// Control del filtro
			ControlFields cf = new ControlFields();
			cf.addBasics(RoomTypeFeatureDao.fields);
//			cf.setOptional(true);//El resto de los campos de fields serán aceptados, por defecto true			
			cf.validate(keyMap);

			cf.validate(attrList);// reutilizamos los mismos criterios para validar attrList

			resultado = this.daoHelper.query(this.roomTypeFeatureDao, keyMap, attrList);

		} catch (ValidateException e) {
			e.printStackTrace();
			resultado = new EntityResultWrong(e.getMessage());

		} catch (Exception e) {
			e.printStackTrace();
			resultado = new EntityResultWrong(ErrorMessage.ERROR);
		}
		return resultado;		
	}
	
	
	@Override
	public EntityResult roomTypeFeatureInsert(Map<String, Object> attrMap) throws OntimizeJEERuntimeException {
		
		EntityResult resultado = new EntityResultWrong();
		try {
			
			ControlFields cf = new ControlFields();
			List<String> required = new ArrayList<String>() {
				{
					add(RoomTypeFeatureDao.ATTR_ROOM_ID);
					add(RoomTypeFeatureDao.ATTR_FEATURE_ID);
				}
			};
/*			List<String> restricted = new ArrayList<String>() {
				{
					add(RoomTypeFeatureDao.ATTR_ROOM_ID);// No quiero que meta el id porque quiero el id autogenerado de la base de
					add(RoomTypeFeatureDao.ATTR_FEATURE_ID);	// datos
				}
			};
*/
			cf.addBasics(RoomTypeFeatureDao.fields);
			cf.setRequired(required);
//			cf.setRestricted(restricted);
//			cf.setOptional(true);//No existen más campos. No es neceario ponerlo
			cf.validate(attrMap);

			resultado = this.daoHelper.insert(this.roomTypeFeatureDao, attrMap);
			resultado.setMessage("Room type features registered");

		} catch (ValidateException e) {
			resultado = new EntityResultWrong(ErrorMessage.CREATION_ERROR + e.getMessage());
		} catch (DuplicateKeyException e) {
			resultado = new EntityResultWrong(ErrorMessage.CREATION_ERROR_DUPLICATED_FIELD);
		} catch (Exception e) {
			e.printStackTrace();
			resultado = new EntityResultWrong(ErrorMessage.CREATION_ERROR);
		}

/*			ValidateFields.required(attrMap, RoomTypeFeatureDao.ATTR_FEATURE_ID, RoomTypeFeatureDao.ATTR_ROOM_ID);	
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
	public EntityResult roomTypeFeatureDelete(Map<String, Object> keyMap) throws OntimizeJEERuntimeException {
		
		EntityResult resultado = new EntityResultWrong();

		try {
			List<String> required = new ArrayList<String>() {
				{
					add(RoomTypeFeatureDao.ATTR_ROOM_ID);
				}
			};
			ControlFields cf = new ControlFields();
			cf.addBasics(RoomTypeFeatureDao.fields);
			cf.setRequired(required);
			cf.setOptional(false);
			cf.validate(keyMap);

			Map<String, Object> consultaKeyMap = new HashMap<>() {
				{
					put(RoomTypeFeatureDao.ATTR_ROOM_ID, keyMap.get(RoomTypeFeatureDao.ATTR_ROOM_ID));
				}
			};

			EntityResult auxEntity = roomTypeFeatureQuery(consultaKeyMap, EntityResultTools.attributes(RoomTypeFeatureDao.ATTR_ROOM_ID));

			if (auxEntity.calculateRecordNumber() == 0) { // si no hay registros...
				resultado = new EntityResultWrong(ErrorMessage.DELETE_ERROR_MISSING_FIELD);
			} else {
				resultado = this.daoHelper.delete(this.roomTypeFeatureDao, keyMap);
				resultado.setMessage("Room type features deleted");
			}

		} catch (ValidateException e) {
			resultado = new EntityResultWrong(ErrorMessage.DELETE_ERROR + e.getMessage());
		}  catch (Exception e) {
			e.printStackTrace();
			resultado = new EntityResultWrong(ErrorMessage.DELETE_ERROR);
		}
		return resultado;
	}	
/*		EntityResult resultado = new EntityResultMapImpl();
		try {
			ValidateFields.required(keyMap, RoomTypeFeatureDao.ATTR_FEATURE_ID,RoomTypeFeatureDao.ATTR_ROOM_ID);

			EntityResult auxEntity = this.daoHelper.query(this.roomTypeFeatureDao,
					EntityResultTools.keysvalues(RoomTypeFeatureDao.ATTR_FEATURE_ID, keyMap.get(RoomTypeFeatureDao.ATTR_FEATURE_ID),RoomTypeFeatureDao.ATTR_ROOM_ID, keyMap.get(RoomTypeFeatureDao.ATTR_ROOM_ID)),
					EntityResultTools.attributes(RoomTypeFeatureDao.ATTR_FEATURE_ID,RoomTypeFeatureDao.ATTR_ROOM_ID));
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
