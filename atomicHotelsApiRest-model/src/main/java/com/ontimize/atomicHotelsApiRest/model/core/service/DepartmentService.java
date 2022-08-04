package com.ontimize.atomicHotelsApiRest.model.core.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.SQLWarningException;
import org.springframework.stereotype.Service;

import com.ontimize.atomicHotelsApiRest.api.core.exceptions.InvalidFieldsException;
import com.ontimize.atomicHotelsApiRest.api.core.exceptions.InvalidFieldsValuesException;
import com.ontimize.atomicHotelsApiRest.api.core.exceptions.LiadaPardaException;
import com.ontimize.atomicHotelsApiRest.api.core.exceptions.MissingColumnsException;
import com.ontimize.atomicHotelsApiRest.api.core.exceptions.MissingFieldsException;
import com.ontimize.atomicHotelsApiRest.api.core.exceptions.RestrictedFieldException;
import com.ontimize.atomicHotelsApiRest.api.core.exceptions.ValidateException;
import com.ontimize.atomicHotelsApiRest.api.core.service.IDepartmentService;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.dto.EntityResultMapImpl;
import com.ontimize.jee.common.exceptions.OntimizeJEERuntimeException;
import com.ontimize.jee.common.tools.EntityResultTools;
import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;
import com.ontimize.atomicHotelsApiRest.model.core.dao.DepartmentDao;
import com.ontimize.atomicHotelsApiRest.model.core.tools.ControlFields;
import com.ontimize.atomicHotelsApiRest.model.core.tools.EntityResultWrong;
import com.ontimize.atomicHotelsApiRest.model.core.tools.ErrorMessage;
import com.ontimize.atomicHotelsApiRest.model.core.tools.ValidateFields;

@Service("DepartmentService")
@Lazy
public class DepartmentService implements IDepartmentService{
	
	@Autowired
	private DepartmentDao departmentDao;
	@Autowired
	private DefaultOntimizeDaoHelper daoHelper;
	
	@Autowired
	ControlFields cf;

	@Override
	public EntityResult departmentQuery(Map<String, Object> keyMap, List<String> attrList)
			throws OntimizeJEERuntimeException {

		EntityResult resultado = new EntityResultWrong();
		try {
			cf.reset();
			cf.addBasics(DepartmentDao.fields);
			cf.validate(keyMap);
			cf.validate(attrList);
			
			resultado = this.daoHelper.query(this.departmentDao, keyMap, attrList);
			
		}catch(ValidateException e) {
			e.printStackTrace();
			resultado=new EntityResultWrong(e.getMessage());
			
		}catch(Exception e) {
			e.printStackTrace();
			resultado=new EntityResultWrong(ErrorMessage.UNKNOWN_ERROR); 
		}
		return resultado;
	}

	@Override
	public EntityResult departmentInsert(Map<String, Object> attrMap) throws OntimizeJEERuntimeException {

		EntityResult resultado = new EntityResultWrong();
		try {
			
			cf.reset();
			List<String> requeridos = new ArrayList<String>() {{
				add(DepartmentDao.ATTR_NAME);
			}};
			List<String> restricted = new ArrayList<String>() {{
				add(DepartmentDao.ATTR_ID);//No quiero que meta el id porque quiero el id autogenerado de la base de datos
			}};
			
			cf.addBasics(DepartmentDao.fields);
			cf.setRequired(requeridos);
			cf.setRestricted(restricted);
			cf.setOptional(true);//El resto de los campos de fields serán aceptados
			cf.validate(attrMap);

			resultado = this.daoHelper.insert(this.departmentDao, attrMap);
			resultado.setMessage("Department registered");

		} catch (ValidateException e) {
			resultado =  new EntityResultWrong(e.getMessage());
			e.printStackTrace();		
		}catch (DuplicateKeyException e) {
			resultado = new EntityResultWrong(ErrorMessage.CREATION_ERROR_DUPLICATED_FIELD);
		}catch (Exception e) {
			e.printStackTrace();
			resultado = new EntityResultWrong(ErrorMessage.UNKNOWN_ERROR);
		}
		return resultado;
	}

	@Override													//data						//filter
	public EntityResult departmentUpdate(Map<String, Object> attrMap, Map<String, Object> keyMap)	throws OntimizeJEERuntimeException {	//attrMap filtro, keymap nuevo valor a actualizar

		EntityResult resultado = new EntityResultWrong();
	
		try {
		
			//ControlFields del filtro
			cf.reset();
			cf.addBasics(DepartmentDao.fields);
			List<String> requiredFilter = new ArrayList<String>() {{
//				add(DepartmentDao.ATTR_NAME);
				add(DepartmentDao.ATTR_ID);
			}};				
			cf.setRequired(requiredFilter);
			cf.setOptional(false);//No será aceptado ningún campo que no esté en required
			cf.validate(keyMap);
			
			
			//ControlFields de los nuevos datos
			cf.reset();
			cf.addBasics(DepartmentDao.fields);
			List<String> restrictedData = new ArrayList<String>() {{
				add(DepartmentDao.ATTR_ID);//El id no se puede actualizar
			}};
			cf.setRestricted(restrictedData);
	//		cd.setOptional(true); //No es necesario ponerlo
			cf.validate(attrMap);
			
			resultado = this.daoHelper.update(this.departmentDao, attrMap, keyMap);
	
			if (resultado.getCode() == EntityResult.OPERATION_SUCCESSFUL_SHOW_MESSAGE) {
				resultado = new EntityResultWrong(ErrorMessage.UPDATE_ERROR_MISSING_FIELD);
			} else {
				resultado.setMessage("Department updated");
			}

		} catch (ValidateException e) {
			resultado =  new EntityResultWrong(e.getMessage());
			e.printStackTrace();		
		}catch (DuplicateKeyException e) {
			resultado = new EntityResultWrong(ErrorMessage.CREATION_ERROR_DUPLICATED_FIELD);
		}catch (DataIntegrityViolationException e) {
			resultado = new EntityResultWrong(ErrorMessage.CREATION_ERROR_MISSING_FK);
		}catch (Exception e) {
			e.printStackTrace();
			resultado = new EntityResultWrong(ErrorMessage.UNKNOWN_ERROR);
		}
		return resultado;
	}

	@Override
	public EntityResult departmentDelete(Map<String, Object> attrMap) throws OntimizeJEERuntimeException {			//El filtro del where del postman		
		
		EntityResult resultado = new EntityResultWrong();

		try {
			
			cf.reset();
			cf.addBasics(DepartmentDao.fields);
			List<String> requeridos = new ArrayList<String>() {{
				add(DepartmentDao.ATTR_ID);
			}};

			cf.setRequired(requeridos);
			cf.setOptional(true);//El resto de los campos de fields serán aceptados
			cf.validate(attrMap);
			
			Map<String, Object> consultaKeyMap = new HashMap<>() { {
				put(DepartmentDao.ATTR_ID, attrMap.get(DepartmentDao.ATTR_ID));
				}
			};
			
			EntityResult auxEntity = departmentQuery(consultaKeyMap, 
					EntityResultTools.attributes(DepartmentDao.ATTR_ID));
			
			if (auxEntity.calculateRecordNumber() == 0) { // si no hay registros...
				resultado = new EntityResultWrong(ErrorMessage.DELETE_ERROR_MISSING_FIELD);
			} else {
				resultado = this.daoHelper.delete(this.departmentDao, attrMap);
				resultado.setMessage("Department deleted");
			}
			
		} catch (ValidateException  e) {
			resultado =  new EntityResultWrong(e.getMessage());
			e.printStackTrace();		
		}catch (DataIntegrityViolationException e) {
			resultado = new EntityResultWrong(ErrorMessage.CREATION_ERROR_DUPLICATED_FIELD);
		}catch (Exception e) {
			e.printStackTrace();
			resultado = new EntityResultWrong(ErrorMessage.UNKNOWN_ERROR);
		}
		return resultado;		
	}

}
