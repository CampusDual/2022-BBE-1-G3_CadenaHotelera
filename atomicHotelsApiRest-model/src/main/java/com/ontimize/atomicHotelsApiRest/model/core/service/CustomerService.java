package com.ontimize.atomicHotelsApiRest.model.core.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.SQLWarningException;
import org.springframework.stereotype.Service;

import com.ontimize.atomicHotelsApiRest.api.core.service.ICustomerService;
import com.ontimize.atomicHotelsApiRest.model.core.dao.CustomerDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.HotelDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.RoomDao;
import com.ontimize.atomicHotelsApiRest.model.core.tools.EntityResultWrong;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.dto.EntityResultMapImpl;
import com.ontimize.jee.common.exceptions.OntimizeJEERuntimeException;
import com.ontimize.jee.common.tools.EntityResultTools;
import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;

@Service("CustomerService")
@Lazy
public class CustomerService implements ICustomerService{

 @Autowired private CustomerDao customerDao;
 @Autowired private DefaultOntimizeDaoHelper daoHelper;
 
 @Override
 public EntityResult customerQuery(Map<String, Object> keyMap, List<String> attrList)
   throws OntimizeJEERuntimeException {
  return this.daoHelper.query(this.customerDao, keyMap, attrList);
 }

 @Override
 public EntityResult customerInsert(Map<String, Object> attrMap) throws OntimizeJEERuntimeException {
		
	 EntityResult resultado = new EntityResultMapImpl();
		try {
			resultado = this.daoHelper.insert(this.customerDao, attrMap);
			resultado.setMessage("Customer registrado");
		} catch (DuplicateKeyException e) {
			resultado = new EntityResultWrong("Error al crear Customer - El registro ya existe");
		} catch (DataIntegrityViolationException e) {
			resultado = new EntityResultWrong("Error al crear Customer - Falta algún campo obligatorio");
		} catch (Exception e) {
			resultado = new EntityResultWrong("Error al registrar Customer");
		}

		return resultado;
 }

 @Override
 public EntityResult customerUpdate(Map<String, Object> attrMap, Map<String, Object> keyMap)
   throws OntimizeJEERuntimeException {
	 EntityResult resultado = new EntityResultMapImpl();
		try {
			resultado = this.daoHelper.update(this.customerDao, attrMap, keyMap);
			if(resultado.getCode() == EntityResult.OPERATION_SUCCESSFUL_SHOW_MESSAGE) {
				resultado.setMessage("No se han encontrado registros para actualizar");			
			}
		} catch (DuplicateKeyException e) {
			resultado = new EntityResultWrong("Error al actualizar Customer - No es posible duplicar un registro");
		} catch (DataIntegrityViolationException e) {
			resultado = new EntityResultWrong("Error al actualizar Customer - Falta algún campo obligatorio");
		} catch (SQLWarningException e) {
			resultado = new EntityResultWrong(
					"Error al actualizar Customer - Falta el cts_id (PK) de la Customer a actualizar");
		} catch (Exception e) {
			resultado = new EntityResultWrong("Error al actualizar Customer");
		}
		return resultado; 
 }

 @Override
 public EntityResult customerDelete(Map<String, Object> keyMap) throws OntimizeJEERuntimeException {
	 EntityResult resultado = new EntityResultMapImpl();		
		try {
			if (keyMap.containsKey(CustomerDao.ATTR_ID)) {
				EntityResult auxEntity = this.daoHelper.query(this.customerDao,
						EntityResultTools.keysvalues(CustomerDao.ATTR_ID, keyMap.get(RoomDao.ATTR_ID)),
						EntityResultTools.attributes(CustomerDao.ATTR_ID));
				if (auxEntity.calculateRecordNumber() == 0) { // si no hay registros...
					resultado = new EntityResultWrong("Error al eliminar Customer - El Customer a eliminar no existe");
				} else {
					resultado = this.daoHelper.delete(this.customerDao, keyMap); 
					resultado.setMessage("Customer eliminado");
				}
			}else {
				resultado = new EntityResultWrong("Error al eliminar Customer - Falta el cst_id (PK) del Customer a eliminar");
			}
		} catch (Exception e) {
			resultado = new EntityResultWrong("Error al eliminar Customer");
		}
		return resultado; 
	
 }

}
