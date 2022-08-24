package com.ontimize.atomicHotelsApiRest.model.core.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.util.comparator.Comparators;

import com.ontimize.atomicHotelsApiRest.api.core.exceptions.ValidateException;
import com.ontimize.atomicHotelsApiRest.api.core.service.IEmployeeService;
import com.ontimize.atomicHotelsApiRest.model.core.dao.EmployeeDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.UserRoleDao;
import com.ontimize.atomicHotelsApiRest.model.core.tools.ControlFields;
import com.ontimize.atomicHotelsApiRest.model.core.tools.EntityResultWrong;
import com.ontimize.atomicHotelsApiRest.model.core.tools.ErrorMessage;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.dto.EntityResultMapImpl;
import com.ontimize.jee.common.exceptions.OntimizeJEERuntimeException;
import com.ontimize.jee.common.security.PermissionsProviderSecured;
import com.ontimize.jee.common.tools.EntityResultTools;
import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;

@Service("EmployeeService")
@Lazy
public class EmployeeService implements IEmployeeService {

	@Autowired
	private EmployeeDao dao;
	@Autowired
	private DefaultOntimizeDaoHelper daoHelper;
	@Autowired
	ControlFields cf;

	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult employeeQuery(Map<String, Object> filter, List<String> columns)
			throws OntimizeJEERuntimeException {
		EntityResult resultado = new EntityResultMapImpl();
//	 System.out.println("****************filter Query******************");
//	 filter.forEach((k,v)->System.out.println(k+"->"+v));
//	 System.out.println("\n****************columns Query******************");
//	 columns.forEach(s->System.out.println(s));
//		resultado=this.daoHelper.query(this.employeeDao, filter,columns);
//		System.out.println("\n****************EntityResul en crudo******************");
//		
//		System.out.println(resultado);
//	 System.out.println("\n****************Recorremos el EntityResul******************");
//		for(int i=0;i<resultado.calculateRecordNumber();i++) {
//		resultado.getRecordValues(i).forEach((k,v)->System.out.println(k+"->"+v));
//		System.out.println("--------");
//		}
//		System.out.println("\n****************Pruebas EntityResult******************");
//		System.out.println(resultado.calculateRecordNumber());
//		System.out.println(resultado.contains(null));
//		System.out.println(resultado.containsKey("emp_fired"));
//		
		try {
			cf.reset();
			
			cf.setCPHtlColum(dao.ATTR_HOTEL);
			cf.setCPRoleUsersRestrictions(UserRoleDao.ROLE_MANAGER);
			
			cf.addBasics(dao.fields);
			cf.validate(filter);
			cf.validate(columns);

			resultado = this.daoHelper.query(this.dao, filter, columns);
			if(resultado.calculateRecordNumber() == 0) {
				resultado.setMessage("No hay resultados para el hotel que gerenta");
			}
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
	public EntityResult employeeInsert(Map<String, Object> data) throws OntimizeJEERuntimeException {

		EntityResult resultado = new EntityResultWrong();
//
//	
//	 System.out.println("****************Data insert ******************");
//	 data.forEach((k,v)->{
//			System.out.println(k+" -> "+v);
//		}
//		);

		try {
			cf.reset();
			
			cf.setCPHtlColum(dao.ATTR_HOTEL);
			cf.setCPRoleUsersRestrictions(UserRoleDao.ROLE_MANAGER);
			
			
			cf.addBasics(dao.fields);
			List<String> required = Arrays.asList(dao.ATTR_NAME, dao.ATTR_IDEN_DOC,
					dao.ATTR_SOCIAL_DOC, dao.ATTR_SALARY, dao.ATTR_PHONE,
					dao.ATTR_ACCOUNT, dao.ATTR_DEPARTMENT, dao.ATTR_HOTEL,
					dao.ATTR_HIRING, dao.ATTR_COUNTRY);
			cf.setRequired(required);
			List<String> restricted = Arrays.asList(dao.ATTR_ID);
			cf.setRestricted(restricted);
			cf.validate(data);

			Map<String, Object> consultaKeyMap = new HashMap<>() {
				{
					put(dao.ATTR_IDEN_DOC, data.get(dao.ATTR_IDEN_DOC));

				}
			};
			EntityResult auxEntity = this.daoHelper.query(this.dao,consultaKeyMap, EntityResultTools
					.attributes(dao.ATTR_IDEN_DOC, dao.ATTR_HIRING, dao.ATTR_FIRED));//consulta sin filtros, por si empleado está contratado en otro hotel.
//			EntityResult auxEntity = employeeQuery(consultaKeyMap, EntityResultTools
//					.attributes(dao.ATTR_IDEN_DOC, dao.ATTR_HIRING, dao.ATTR_FIRED));

			if (auxEntity.calculateRecordNumber() == 0) {
				resultado = this.daoHelper.insert(this.dao, data);
				resultado.setMessage("Empleado contratado por primera vez");
			} else if (!((List<String>) auxEntity.get(dao.ATTR_FIRED)).contains(null)) {
				if (data.get(dao.ATTR_FIRED) != null) {
					if (((Date) data.get(dao.ATTR_HIRING))
							.compareTo(((Date) data.get(dao.ATTR_FIRED))) < 0) {
						resultado = this.daoHelper.insert(this.dao, data);
						resultado.setMessage("Empleado contratado , este es su " + auxEntity.calculateRecordNumber()
								+ " contrato con la cadena");
					} else {
						resultado.setMessage("La fecha de despido no puede ser anterior o igual a la de contratacion ");
					}

				} else {
					List<Date> firedDates=(List<Date>) auxEntity.get(dao.ATTR_FIRED);
					
					// aqui esta el tomate
					Date max=firedDates.stream().max(Date::compareTo).get();
					if(max.compareTo((Date) data.get(dao.ATTR_HIRING))>0){
						resultado.setMessage("La fecha de contratacion es anterior al ultimo despido "+max.toString());
					}else {
					resultado = this.daoHelper.insert(this.dao, data);
					resultado.setMessage("Empleado contratado , este es su " + (auxEntity.calculateRecordNumber() + 1)
							+ " contrato con la cadena");
					}
				}
			} else {
				resultado.setMessage("Empleado con contrato en vigor, rescindalo primero");

			}
		} catch (DuplicateKeyException e) {
			resultado = new EntityResultWrong(ErrorMessage.CREATION_ERROR_DUPLICATED_FIELD);
		} catch (DataIntegrityViolationException e) {
			e.printStackTrace();
			resultado = new EntityResultWrong(ErrorMessage.CREATION_ERROR_MISSING_FK);
		} catch (ValidateException e) {
			resultado = e.getEntityResult();
		} catch (Exception e) {
			resultado = new EntityResultWrong(ErrorMessage.UNKNOWN_ERROR);
			e.printStackTrace();
		}
		return resultado;
	}

	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult employeeUpdate(Map<String, Object> data, Map<String, Object> filter)
			throws OntimizeJEERuntimeException {
		EntityResult resultado = new EntityResultWrong();
//	 System.out.println("********Datos Entrada*********");
//	 System.out.println("*******Filter******");
//		filter.forEach((k,v)->{
//		System.out.println(k+" -> "+v);
//	}
//	);
//		System.out.println("*******Data******");
//		data.forEach((k,v)->{
//		System.out.println(k+" -> "+v);
//	}
//	); 

		try {
			// ControlFields del filtro
			List<String> requiredFilter = new ArrayList<String>() {
				{
					add(dao.ATTR_IDEN_DOC);
				}
			};

			cf.reset();
			cf.addBasics(dao.fields);
			cf.setRequired(requiredFilter);
			cf.setOptional(false);// no será aceptado ningun campo que no esté en required
			cf.validate(filter);
			// ControlFileds de los nuevos datos
			List<String> restrictedData = new ArrayList<>() {
				{
					add(dao.ATTR_ID);

				}
			};
			cf.reset();
			cf.addBasics(dao.fields);
			cf.setRestricted(restrictedData);
			cf.validate(data);
			System.out.println(data.get(dao.ATTR_IDEN_DOC));// comentar
			Map<String, Object> subConsultaKeyMap = new HashMap<>() {
				{
					put(dao.ATTR_IDEN_DOC, filter.get(dao.ATTR_IDEN_DOC));

				}
			};
			EntityResult auxEntity = employeeQuery(subConsultaKeyMap, EntityResultTools
					.attributes(dao.ATTR_ID,dao.ATTR_IDEN_DOC, dao.ATTR_HIRING, dao.ATTR_FIRED));
			
			if (auxEntity.calculateRecordNumber() == 0) {
				resultado.setMessage("Empleado no registrado, registrelo primero");
			} else if (((List<String>) auxEntity.get(dao.ATTR_FIRED)).contains(null)) {
				if (data.get(dao.ATTR_FIRED) != null) {
					if (((Date) auxEntity.getRecordValues(auxEntity.calculateRecordNumber()-1).get(dao.ATTR_HIRING))
							.compareTo(((Date) data.get(dao.ATTR_FIRED)))< 0) {
						filter.put(dao.ATTR_ID,auxEntity.getRecordValues(auxEntity.calculateRecordNumber()-1).get(dao.ATTR_ID));
						resultado = this.daoHelper.update(this.dao, data, filter);
						resultado.setMessage("Empleado despedido de su " + auxEntity.calculateRecordNumber()
								+ " contrato con la cadena");
					} else {
						resultado.setMessage("La fecha de despido no puede ser anterior o igual a la de contratacion ");
					}

				} else {
					resultado = this.daoHelper.update(this.dao, data, filter);
					resultado.setMessage("Empleado actualizado en  su " + auxEntity.calculateRecordNumber()
							+ " contrato con la cadena");
				}
			} else {
				resultado.setMessage("Empleado sin contrato en vigor , contratelo primero");

			}
		} catch (ValidateException e) {
			resultado = e.getEntityResult();
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
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult employeeFiredUpdate(Map<String, Object> data, Map<String, Object> filter)
			throws OntimizeJEERuntimeException {
		data.put(dao.ATTR_FIRED, new Date());
		return this.employeeUpdate(data, filter);
	}

}