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

import com.ontimize.atomicHotelsApiRest.api.core.exceptions.ValidateException;
import com.ontimize.atomicHotelsApiRest.api.core.service.IRoomTypeService;
import com.ontimize.atomicHotelsApiRest.model.core.dao.BedComboDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.FeatureDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.RoomTypeDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.RoomTypeFeatureDao;
import com.ontimize.atomicHotelsApiRest.model.core.tools.ControlFields;
import com.ontimize.atomicHotelsApiRest.model.core.tools.EntityResultWrong;
import com.ontimize.atomicHotelsApiRest.model.core.tools.ErrorMessage;

import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.dto.EntityResultMapImpl;
import com.ontimize.jee.common.exceptions.OntimizeJEERuntimeException;
import com.ontimize.jee.common.security.PermissionsProviderSecured;
import com.ontimize.jee.common.tools.EntityResultTools;
import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;

@Service("RoomTypeService")
@Lazy
public class RoomTypeService implements IRoomTypeService {

	@Autowired
	private RoomTypeDao roomTypeDao;
	@Autowired
	private DefaultOntimizeDaoHelper daoHelper;

	@Autowired
	ControlFields cf;

	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult roomTypeQuery(Map<String, Object> keyMap, List<String> attrList)
			throws OntimizeJEERuntimeException {
		EntityResult resultado = new EntityResultWrong();
		try {
			cf.reset();
			cf.addBasics(RoomTypeDao.fields);
			cf.validate(keyMap);
			cf.validate(attrList);
			return this.daoHelper.query(this.roomTypeDao, keyMap, attrList);
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
	public EntityResult roomTypeInsert(Map<String, Object> attrMap) throws OntimizeJEERuntimeException {

		EntityResult resultado = new EntityResultWrong();

		try {
			cf.reset();
			cf.addBasics(RoomTypeDao.fields);
			List<String> required = new ArrayList<>() {
				{
					add(RoomTypeDao.ATTR_NAME);
					add(RoomTypeDao.ATTR_PRICE);
					add(RoomTypeDao.ATTR_BEDS_COMBO);
				}
			};
			cf.setRequired(required);
			List<String> restricted = new ArrayList<>() {
				{
					add(RoomTypeDao.ATTR_ID);
				}
			};
			cf.setRestricted(restricted);
			cf.validate(attrMap);

			resultado = this.daoHelper.insert(this.roomTypeDao, attrMap);

			resultado.setMessage("RoomType registrada");

		} catch (DuplicateKeyException e) {
			resultado = new EntityResultWrong(ErrorMessage.CREATION_ERROR_DUPLICATED_FIELD);
		} catch (DataIntegrityViolationException e) {
			resultado = new EntityResultWrong(ErrorMessage.CREATION_ERROR_MISSING_FK);
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
	public EntityResult roomTypeUpdate(Map<String, Object> attrMap, Map<String, Object> keyMap)
			throws OntimizeJEERuntimeException {

		EntityResult resultado = new EntityResultMapImpl();
		try {
			cf.reset();
			cf.addBasics(RoomTypeDao.fields);
			List<String> required = new ArrayList<>() {
				{
					add(RoomTypeDao.ATTR_ID);
				}
			};
			cf.setRequired(required);
			cf.setOptional(false);
			cf.validate(keyMap);

			cf.reset();
			cf.addBasics(RoomTypeDao.fields);
			List<String> restricted = new ArrayList<>() {
				{
					add(RoomTypeDao.ATTR_ID);
				}
			};
			cf.setRestricted(restricted);
			cf.validate(attrMap);

			resultado = this.daoHelper.update(this.roomTypeDao, attrMap, keyMap);
			if (resultado.getCode() == EntityResult.OPERATION_SUCCESSFUL_SHOW_MESSAGE) {
				resultado = new EntityResultWrong(ErrorMessage.UPDATE_ERROR_MISSING_FIELD);
			} else {
				resultado.setMessage("RoomType actualizada");
			}
		} catch (ValidateException e) {
			resultado = e.getEntityResult();
		} catch (DuplicateKeyException e) {
			resultado = new EntityResultWrong(ErrorMessage.UPDATE_ERROR_DUPLICATED_FIELD);
		} catch (DataIntegrityViolationException e) {
			resultado = new EntityResultWrong(ErrorMessage.UPDATE_ERROR_REQUIRED_FIELDS);
		} catch (Exception e) {
			e.printStackTrace();
			resultado = new EntityResultWrong(ErrorMessage.UNKNOWN_ERROR);
		}
		return resultado;
	}

	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult roomTypeDelete(Map<String, Object> keyMap) throws OntimizeJEERuntimeException {

		EntityResult resultado = new EntityResultWrong();
		try {
			cf.reset();
			cf.addBasics(RoomTypeDao.fields);
			List<String> required = new ArrayList() {
				{
					add(RoomTypeDao.ATTR_ID);
				}
			};
			cf.setRequired(required);
			cf.setOptional(false);
			cf.validate(keyMap);
			Map<String, Object> consultaKeyMap = new HashMap<>() {
				{
					put(RoomTypeDao.ATTR_ID, keyMap.get(RoomTypeDao.ATTR_ID));
				}
			};

			EntityResult auxEntity = roomTypeQuery(consultaKeyMap, EntityResultTools.attributes(RoomTypeDao.ATTR_ID));
			if (auxEntity.calculateRecordNumber() == 0) { // si no hay registros...
				resultado = new EntityResultWrong(ErrorMessage.DELETE_ERROR_MISSING_FIELD);
			} else {
				resultado = this.daoHelper.delete(this.roomTypeDao, keyMap);
				resultado.setMessage("RoomType eliminado");
			}
		} catch (ValidateException e) {
			resultado = e.getEntityResult();
		} catch (DataIntegrityViolationException e) {
			e.getStackTrace();
			resultado = new EntityResultWrong(ErrorMessage.DELETE_ERROR_FOREING_KEY);
		} catch (Exception e) {
			e.printStackTrace();
			resultado = new EntityResultWrong(ErrorMessage.ERROR);
		}
		return resultado;

	}

	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult infoQuery(Map<String, Object> keysValues, List<String> attrList) {

		EntityResult resultado = new EntityResultWrong();
		try {
			cf.reset();
			cf.addBasics(RoomTypeDao.fields);
			cf.addBasics(BedComboDao.fields);
			cf.validate(keysValues);
			cf.validate(attrList);

			resultado = this.daoHelper.query(this.roomTypeDao, keysValues, attrList, "queryRoomTypes");

		} catch (ValidateException e) {
			resultado = e.getEntityResult();
		} catch (Exception e) {
			e.printStackTrace();
			resultado = new EntityResultWrong(ErrorMessage.UNKNOWN_ERROR);
		}

		return resultado;
	}

//	@Override
//	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult infoRoomFeaturesQuery(Map<String, Object> keysValues, List<String> attrList) {

		EntityResult resultado = new EntityResultWrong();
		try {
			cf.reset();
			cf.addBasics(RoomTypeDao.fields);
			cf.addBasics(FeatureDao.fields);
			cf.addBasics(RoomTypeFeatureDao.fields);
			cf.addBasics(BedComboDao.fields);

			cf.validate(keysValues);
			cf.validate(attrList);

			resultado = this.daoHelper.query(this.roomTypeDao, keysValues, attrList, "queryRoomFeaturesTypes");

		} catch (ValidateException e) {
			resultado = e.getEntityResult();
		} catch (Exception e) {
			e.printStackTrace();
			resultado = new EntityResultWrong(ErrorMessage.UNKNOWN_ERROR);
		}

		return resultado;
	}
}
