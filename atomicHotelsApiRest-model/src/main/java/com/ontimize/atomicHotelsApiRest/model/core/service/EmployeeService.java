package com.ontimize.atomicHotelsApiRest.model.core.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
import org.springframework.stereotype.Service;

import com.ontimize.atomicHotelsApiRest.api.core.exceptions.LiadaPardaException;
import com.ontimize.atomicHotelsApiRest.api.core.exceptions.ValidateException;
import com.ontimize.atomicHotelsApiRest.api.core.service.IEmployeeService;
import com.ontimize.atomicHotelsApiRest.model.core.dao.EmployeeDao;
import com.ontimize.atomicHotelsApiRest.model.core.tools.ControlFields;
import com.ontimize.atomicHotelsApiRest.model.core.tools.EntityResultWrong;
import com.ontimize.atomicHotelsApiRest.model.core.tools.ErrorMessage;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.dto.EntityResultMapImpl;
import com.ontimize.jee.common.exceptions.OntimizeJEERuntimeException;
import com.ontimize.jee.common.tools.EntityResultTools;
import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;

import net.bytebuddy.implementation.auxiliary.AuxiliaryType;

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
	 System.out.println("****************keyMap Query******************");
	 keyMap.forEach((k,v)->System.out.println(k+"->"+v));
	 System.out.println("\n****************attrList Query******************");
	 attrList.forEach(s->System.out.println(s));
	 
		EntityResult resultado=new EntityResultMapImpl();
		resultado=this.daoHelper.query(this.employeeDao, keyMap,attrList);
		System.out.println(resultado);
	 System.out.println("\n****************Recorremos el EntityResul******************");
		for(int i=0;i<resultado.calculateRecordNumber();i++) {
		resultado.getRecordValues(i).forEach((k,v)->System.out.println(k+"->"+v));
		System.out.println("--------");
		}
		System.out.println("\n****************Pruebas EntityResult******************");
		System.out.println(resultado.calculateRecordNumber());
		System.out.println(resultado.contains(null));
		System.out.println(resultado.containsKey("emp_fired"));
		

	 try {
		 cf.reset();
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

	
	
	 attrMap.forEach((k,v)->{
			System.out.println(k+" -> "+v);
		}
		);
	 
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
				 EmployeeDao.ATTR_COUNTRY
				 );
		 cf.setRequired(required);
		 List<String> restricted=Arrays.asList(
				 EmployeeDao.ATTR_ID
				 );
		 cf.setRestricted(restricted);
		 cf.validate(attrMap);
		 
		 Map<String,Object> consultaKeyMap=new HashMap<>() {
			  {
			  put(EmployeeDao.ATTR_IDEN_DOC,attrMap.get(EmployeeDao.ATTR_IDEN_DOC));
			  
		  }
			  };
			 EntityResult auxEntity=employeeQuery(consultaKeyMap,EntityResultTools.attributes(EmployeeDao.ATTR_IDEN_DOC,EmployeeDao.ATTR_FIRED));
			//System.out.println(auxEntity.getRecordValues(0).get(EmployeeDao.ATTR_FIRED));
			 System.out.println(((List<String>)auxEntity.get(EmployeeDao.ATTR_FIRED)));
			 System.out.println(auxEntity.calculateRecordNumber());
		  if(auxEntity.calculateRecordNumber()==0 ||!((List<String>)auxEntity.get(EmployeeDao.ATTR_FIRED)).contains(null)) {
			  resultado=this.daoHelper.insert(this.employeeDao, attrMap);
			  resultado.setMessage("Empleado  Registrado");
		  }else {
		 resultado.setMessage("Empleado no Registrado despídalo primero");
		 
		  }
	}catch (DuplicateKeyException e) {
		resultado =new EntityResultWrong(ErrorMessage.CREATION_ERROR_DUPLICATED_FIELD);
	}catch (DataIntegrityViolationException e) {
		e.printStackTrace();
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
	 System.out.println("********Datos Entrada*********");
	 System.out.println("*******attrMap******");
	 EntityResult resultado=new EntityResultWrong();
		attrMap.forEach((k,v)->{
		System.out.println(k+" -> "+v);
	}
	);
		System.out.println("*******keyMap******");
		keyMap.forEach((k,v)->{
		System.out.println(k+" -> "+v);
	}
	); 
	 
	 
	 try {
		 //ControlFields del filtro
		 List<String> requiredFilter=new ArrayList<String>(){
			 { 
				 add(EmployeeDao.ATTR_IDEN_DOC);
			 }
			 };
		 
		cf.reset();
		cf.addBasics(EmployeeDao.fields);
		cf.setRequired(requiredFilter);
		cf.setOptional(false);//no será aceptado ningun campo que no esté en required
		cf.validate(keyMap);
		//ControlFileds de los nuevos datos
		List<String> restrictedData=new ArrayList<>() {
			{
				add(EmployeeDao.ATTR_ID);
				
			}
		};
		cf.reset();
		cf.addBasics(EmployeeDao.fields);
		cf.setRestricted(restrictedData);
		cf.validate(attrMap);
		System.out.println(attrMap.get(EmployeeDao.ATTR_IDEN_DOC));//comentar
		Map<String, Object> subConsultaKeyMap = new HashMap<>() {
			  {
				  put(EmployeeDao.ATTR_IDEN_DOC,keyMap.get(EmployeeDao.ATTR_IDEN_DOC));
				  
			  }
		};
		EntityResult auxEntity =employeeQuery(subConsultaKeyMap,
				EntityResultTools.attributes(EmployeeDao.ATTR_IDEN_DOC,EmployeeDao.ATTR_FIRED));
		if(auxEntity.calculateRecordNumber()==0||!((List<String>)auxEntity.get(EmployeeDao.ATTR_FIRED)).contains(null)) {
			resultado=new EntityResultWrong(ErrorMessage.UPDATE_ERROR_MISSING_FIELD);
		}else {
			resultado=this.daoHelper.update(this.employeeDao, attrMap, keyMap);
			resultado.setMessage("Empleado Actualizado");
			
		}
	 }catch (ValidateException e) {
			resultado = new EntityResultWrong(ErrorMessage.UPDATE_ERROR + " - " + e.getMessage());
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
	 
 @Override
 public EntityResult employeeFiredUpdate(Map<String, Object> data, Map<String, Object> filter) throws OntimizeJEERuntimeException {
	 EntityResult resultado=new EntityResultWrong();
	 System.out.println("****************filter FiredUpdate******************");
	 filter.forEach((k,v)->System.out.println(k+"->"+v));
	 System.out.println("\n****************data Fired Update******************");
	 data.forEach((k,v)->System.out.println(k+"->"+v));
	 
	 try {
		 //ControlFields del filtro
		 List<String> requiredFilter=new ArrayList<String>(){
			 { 
				 add(EmployeeDao.ATTR_IDEN_DOC);
			 }
		 };
		 
		 cf.reset();
		 cf.addBasics(EmployeeDao.fields);
		 cf.setRequired(requiredFilter);
		 cf.setOptional(false);
		 List<String> restrictedData=new ArrayList<>() {
			 {
				 add(EmployeeDao.ATTR_ID);
				 
			 }
		 };
		 cf.reset();
		 cf.addBasics(EmployeeDao.fields);
		 cf.setRestricted(restrictedData);
		 cf.validate(filter);
		 Map<String, Object> subConsultaKeyMap = new HashMap<>() {
			 {
				 put(EmployeeDao.ATTR_IDEN_DOC,filter.get(EmployeeDao.ATTR_IDEN_DOC));
				 
			 }
		 };
		data=new HashMap<>();
		 
		 data.put(EmployeeDao.ATTR_FIRED, new Date());
		 
		 System.out.println("\n****************data Fired Update******************");
		 data.forEach((k,v)->System.out.println(k+"->"+v));
		 
		 EntityResult auxEntity =employeeQuery(subConsultaKeyMap,
		 EntityResultTools.attributes(EmployeeDao.ATTR_IDEN_DOC,EmployeeDao.ATTR_FIRED));
		 
		
		 
		 if(auxEntity.calculateRecordNumber()==0||!((List<String>)auxEntity.get(EmployeeDao.ATTR_FIRED)).contains(null)) {
			 resultado=new EntityResultWrong(ErrorMessage.UPDATE_ERROR_MISSING_FIELD);
		 }else {
			 this.daoHelper.update(this.employeeDao, data, filter);
			 resultado.setMessage("Empleado despedido con fecha de hoy");
		 }
	 }catch (ValidateException e) {
		 resultado = new EntityResultWrong(ErrorMessage.UPDATE_ERROR + " - " + e.getMessage());
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

// @Override
// public EntityResult employeeDelete(Map<String, Object> attrMap) throws OntimizeJEERuntimeException {
//  EntityResult resultado=new EntityResultWrong();
////	attrMap.forEach((k,v)->{
////		System.out.println(k+" -> "+v);
////	}
////	); 
//  
//  
//  try {
//	  cf.reset();
//	  cf.addBasics(EmployeeDao.fields);
//	  List<String> requeridos =new ArrayList<>(){
//		  {
//		  add(EmployeeDao.ATTR_IDEN_DOC);
//	  }
//		 };
//	cf.setRequired(requeridos);
//	cf.setOptional(true);
//	cf.validate(attrMap);
//  Map<String,Object> consultaKeyMap=new HashMap<>() {
//	  {
//	  put(EmployeeDao.ATTR_IDEN_DOC,attrMap.get(EmployeeDao.ATTR_IDEN_DOC));
//	  
//  }
//	  };
//	 EntityResult auxEntity=employeeQuery(consultaKeyMap,EntityResultTools.attributes(EmployeeDao.ATTR_IDEN_DOC,EmployeeDao.ATTR_FIRED));
//	System.out.println(auxEntity.getRecordValues(0).get(EmployeeDao.ATTR_FIRED));
//  if(auxEntity.calculateRecordNumber()==0 ) {
//	  resultado=new EntityResultWrong(ErrorMessage.UPDATE_ERROR_MISSING_FIELD);
//  }else if(auxEntity.getRecordValues(0).get(EmployeeDao.ATTR_FIRED)==null) {
//	  resultado.setMessage("Empleado no puede ser borrado,despidalo primero"); 
//  }
//  else {
//	  resultado=this.daoHelper.delete(this.employeeDao, attrMap);
//	  resultado.setMessage("Empleado borrado");
//  }
// 
// 	} catch (ValidateException | LiadaPardaException e) {
//		resultado =  new EntityResultWrong(e.getMessage());
//		e.printStackTrace();		
//	}catch (DuplicateKeyException e) {
//		resultado = new EntityResultWrong(ErrorMessage.CREATION_ERROR_DUPLICATED_FIELD);
//	}catch (Exception e) {
//		e.printStackTrace();
//		resultado = new EntityResultWrong(ErrorMessage.CREATION_ERROR);
//	}
//  return resultado;
//}

 }