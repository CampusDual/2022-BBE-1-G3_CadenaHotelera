package com.ontimize.atomicHotelsApiRest.model.core.service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import com.ontimize.atomicHotelsApiRest.api.core.exceptions.ValidateException;
import com.ontimize.atomicHotelsApiRest.api.core.service.IEmployeeService;
import com.ontimize.atomicHotelsApiRest.model.core.dao.EmployeeDao;
import com.ontimize.atomicHotelsApiRest.model.core.tools.ControlFields;
import com.ontimize.atomicHotelsApiRest.model.core.tools.EntityResultWrong;
import com.ontimize.atomicHotelsApiRest.model.core.tools.ErrorMessage;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.exceptions.OntimizeJEERuntimeException;
import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;

@Service("EmployeeService")
@Lazy
public class EmployeeService implements IEmployeeService{

 @Autowired 
 private EmployeeDao employeeDao;
@Autowired 
private DefaultOntimizeDaoHelper daoHelper;
@Autowired
ControlFields cf;
 @Override
 public EntityResult employeeQuery(Map<String, Object> keyMap, List<String> attrList)
   throws OntimizeJEERuntimeException {
	 EntityResult resultado=new EntityResultWrong();
	 try {
		 cf.addBasics(EmployeeDao.fields);
		 cf.validate(attrList);
		 cf.validate(keyMap);
		 
		resultado=this.daoHelper.query(this.employeeDao, keyMap, attrList); 
	 }catch(ValidateException e) {
		 e.printStackTrace();
		 resultado=new EntityResultWrong(e.getMessage());
	 }catch(Exception e) {
		 e.printStackTrace();
		 resultado =new EntityResultWrong(ErrorMessage.UNKNOWN_ERROR);
	 }
	 return resultado ;
 }

 @Override
 public EntityResult employeeInsert(Map<String, Object> attrMap) throws OntimizeJEERuntimeException {
  
	 EntityResult resultado= new EntityResultWrong();
	 try {
		 cf.reset();
		 cf.addBasics(EmployeeDao.fields);
		 List<String> required=Arrays.asList(
				 EmployeeDao.ATTR_NAME,
				 EmployeeDao.ATTR_IDEN_DOC,
				 EmployeeDao.ATTR_SOCIAL_DOC,
				 EmployeeDao.ATTR_SALARY,
				 EmployeeDao.ATTR_PHONE,
				 EmployeeDao.ATTR_ACCOUNT,
				 EmployeeDao.ATTR_DEPARTMENT,
				 EmployeeDao.ATTR_HOTEL,
				 EmployeeDao.ATTR_HIRING,
				 EmployeeDao.ATTR_FIRED
				 );
		 cf.setRequired(required);
		 List<String> restricted=Arrays.asList(
				 EmployeeDao.ATTR_ID
				 );
		 cf.setRestricted(restricted);
		 cf.validate(attrMap);
		 resultado=this.daoHelper.insert(this.employeeDao, attrMap);
		 resultado.setMessage("Empleado Registrado");
	 
	}catch (DuplicateKeyException e) {
		resultado =new EntityResultWrong(ErrorMessage.CREATION_ERROR_DUPLICATED_FIELD);
	}catch (DataIntegrityViolationException e) {
		resultado = new EntityResultWrong(ErrorMessage.CREATION_ERROR_MISSING_FK);
	}catch (ValidateException e) {
		resultado =new EntityResultWrong(ErrorMessage.CREATION_ERROR+e.getMessage());
	}
	catch(Exception e) {
		resultado=new EntityResultWrong(ErrorMessage.UNKNOWN_ERROR);
		e.printStackTrace();
	}
	return resultado;
}

 @Override
 public EntityResult employeeUpdate(Map<String, Object> attrMap, Map<String, Object> keyMap)
   throws OntimizeJEERuntimeException {
  return this.daoHelper.update(this.employeeDao, attrMap, keyMap);
 }

 @Override
 public EntityResult employeeDelete(Map<String, Object> keyMap) throws OntimizeJEERuntimeException {
  return this.daoHelper.delete(this.employeeDao, keyMap);
 }
 }