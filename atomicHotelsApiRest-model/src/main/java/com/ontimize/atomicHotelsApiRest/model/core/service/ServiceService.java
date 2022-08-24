package com.ontimize.atomicHotelsApiRest.model.core.service;

import static org.mockito.ArgumentMatchers.anyList;

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
import com.ontimize.atomicHotelsApiRest.api.core.service.IServiceService;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.dto.EntityResultMapImpl;
import com.ontimize.jee.common.exceptions.OntimizeJEERuntimeException;
import com.ontimize.jee.common.security.PermissionsProviderSecured;
import com.ontimize.jee.common.tools.EntityResultTools;
import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;
import com.ontimize.atomicHotelsApiRest.model.core.dao.BedComboDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.FeatureDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.HotelDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.ServiceDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.ServicesXtraDao;
import com.ontimize.atomicHotelsApiRest.model.core.tools.ControlFields;
import com.ontimize.atomicHotelsApiRest.model.core.tools.EntityResultWrong;
import com.ontimize.atomicHotelsApiRest.model.core.tools.ErrorMessage;
import com.ontimize.atomicHotelsApiRest.model.core.tools.ValidateFields;

@Service("ServiceService")
@Lazy
public class ServiceService implements IServiceService {

	@Autowired
	private ServiceDao serviceDao;
	@Autowired
	private DefaultOntimizeDaoHelper daoHelper;

	@Autowired
	ControlFields cf;

	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult serviceQuery(Map<String, Object> keyMap, List<String> attrList)
			throws OntimizeJEERuntimeException {

		EntityResult resultado = new EntityResultWrong();
		try {
			cf.reset();
			cf.addBasics(ServiceDao.fields);
			cf.validate(keyMap);
			cf.validate(attrList);
			return this.daoHelper.query(this.serviceDao, keyMap, attrList);
		} catch (ValidateException e) {
			resultado = e.getEntityResult();
		} catch (LiadaPardaException e) {
			resultado = new EntityResultWrong(e.getMessage());
		} catch (Exception e) {
			resultado = new EntityResultWrong(ErrorMessage.ERROR);
		}
		return resultado;
	}

	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult serviceInsert(Map<String, Object> attrMap) throws OntimizeJEERuntimeException {

		EntityResult resultado = new EntityResultWrong();
		try {

			cf.reset();
			List<String> requeridos = new ArrayList<String>() {
				{
					add(ServiceDao.ATTR_NAME);
				}
			};
			List<String> restricted = new ArrayList<String>() {
				{
					add(ServiceDao.ATTR_ID);// No quiero que meta el id porque quiero el id autogenerado de la base de
											// datos
				}
			};

			cf.addBasics(ServiceDao.fields);
			cf.setRequired(requeridos);
			cf.setRestricted(restricted);
			cf.setOptional(true);// El resto de los campos de fields serán aceptados
			cf.validate(attrMap);

			resultado = this.daoHelper.insert(this.serviceDao, attrMap);
			resultado.setMessage("Service registered");

		} catch (ValidateException e) {
			resultado = e.getEntityResult();
		} catch (LiadaPardaException e) {
			resultado = new EntityResultWrong(e.getMessage());
			e.printStackTrace();
		} catch (DuplicateKeyException e) {
			resultado = new EntityResultWrong(ErrorMessage.CREATION_ERROR_DUPLICATED_FIELD);
		} catch (DataIntegrityViolationException e) {
			resultado = new EntityResultWrong(ErrorMessage.CREATION_ERROR_MISSING_FK);
		} catch (Exception e) {
			e.printStackTrace();
			resultado = new EntityResultWrong(ErrorMessage.CREATION_ERROR);
		}
		return resultado;
	}

	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult serviceUpdate(Map<String, Object> attrMap, Map<String, Object> keyMap)
			throws OntimizeJEERuntimeException {

		EntityResult resultado = new EntityResultWrong();

		try {

			// ControlFields del filtro
			cf.reset();
			cf.addBasics(ServiceDao.fields);
			List<String> requiredFilter = new ArrayList<String>() {
				{
//				add(ServicesXtraDao.ATTR_NAME);
					add(ServiceDao.ATTR_ID);
				}
			};
			cf.setRequired(requiredFilter);
			cf.setOptional(false);// No será aceptado ningún campo que no esté en required
			cf.validate(keyMap);

			// ControlFields de los nuevos datos
			cf.reset();
			cf.addBasics(ServiceDao.fields);
			List<String> restrictedData = new ArrayList<String>() {
				{
					add(ServiceDao.ATTR_ID);// El id no se puede actualizar
				}
			};
			cf.setRestricted(restrictedData);
			// cd.setOptional(true); //No es necesario ponerlo
			cf.validate(attrMap);

			resultado = this.daoHelper.update(this.serviceDao, attrMap, keyMap);

			if (resultado.getCode() == EntityResult.OPERATION_SUCCESSFUL_SHOW_MESSAGE) {
				resultado = new EntityResultWrong(ErrorMessage.UPDATE_ERROR_MISSING_FIELD);
			} else {
				resultado.setMessage("Service updated");
			}

		} catch (ValidateException e) {
			resultado = e.getEntityResult();
		} catch (LiadaPardaException e) {
			resultado = new EntityResultWrong(e.getMessage());
			e.printStackTrace();
		} catch (DuplicateKeyException e) {
			resultado = new EntityResultWrong(ErrorMessage.CREATION_ERROR_DUPLICATED_FIELD);
		} catch (DataIntegrityViolationException e) {
			resultado = new EntityResultWrong(ErrorMessage.CREATION_ERROR_MISSING_FK);
		} catch (Exception e) {
			e.printStackTrace();
			resultado = new EntityResultWrong(ErrorMessage.CREATION_ERROR);
		}
		return resultado;
	}

	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult serviceDelete(Map<String, Object> attrMap) throws OntimizeJEERuntimeException {

		EntityResult resultado = new EntityResultWrong();

		try {

			cf.reset();
			cf.addBasics(ServiceDao.fields);
			List<String> requeridos = new ArrayList<String>() {
				{
					add(ServiceDao.ATTR_ID);
				}
			};

			cf.setRequired(requeridos);
			cf.setOptional(true);// El resto de los campos de fields serán aceptados
			cf.validate(attrMap);

			Map<String, Object> consultaKeyMap = new HashMap<>() {
				{
					put(ServiceDao.ATTR_ID, attrMap.get(ServiceDao.ATTR_ID));
				}
			};

			EntityResult auxEntity = serviceQuery(consultaKeyMap, EntityResultTools.attributes(ServiceDao.ATTR_ID));

			if (auxEntity.calculateRecordNumber() == 0) { // si no hay registros...
				resultado = new EntityResultWrong(ErrorMessage.DELETE_ERROR_MISSING_FIELD);
			} else {
				resultado = this.daoHelper.delete(this.serviceDao, attrMap);
				resultado.setMessage("Service deleted");
			}

		} catch (ValidateException e) {
			resultado = e.getEntityResult();
		} catch (LiadaPardaException e) {
			resultado = new EntityResultWrong(e.getMessage());
			e.printStackTrace();
		} catch (DuplicateKeyException e) {
			resultado = new EntityResultWrong(ErrorMessage.CREATION_ERROR_DUPLICATED_FIELD);
		} catch (Exception e) {
			e.printStackTrace();
			resultado = new EntityResultWrong(ErrorMessage.UNKNOWN_ERROR);
		}
		return resultado;
	}

}