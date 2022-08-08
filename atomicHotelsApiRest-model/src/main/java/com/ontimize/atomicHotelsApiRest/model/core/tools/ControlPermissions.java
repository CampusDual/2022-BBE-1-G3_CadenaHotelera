package com.ontimize.atomicHotelsApiRest.model.core.tools;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.ontimize.atomicHotelsApiRest.api.core.exceptions.InvalidFieldsException;
import com.ontimize.atomicHotelsApiRest.api.core.exceptions.InvalidFieldsValuesException;
import com.ontimize.atomicHotelsApiRest.api.core.exceptions.LiadaPardaException;
import com.ontimize.atomicHotelsApiRest.model.core.dao.UserDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.UserRoleDao;
import com.ontimize.jee.common.services.user.UserInformation;

@Component
public class ControlPermissions {
	private List<String> roleUsersRestrictions;
	private Map<String, Object> keyMap;
	private String htl_colum;
	private String user_colum = "user_";

	public ControlPermissions() {
		reset();
	}

	public void setMap(Map<String, Object> keyMap) {

	}

	public void reset() {
		this.roleUsersRestrictions = null;
		this.keyMap = null;
		this.htl_colum = null;
	}
	public void setHtlColum(String columna) {
		this.htl_colum = columna;
	}
	public void setRoleUsersRestrictions(String... roleUsersRestrictions) {
		this.roleUsersRestrictions = Arrays.asList(roleUsersRestrictions);
	}

	public void restrict(Map<String, Object> keyMap) throws LiadaPardaException, InvalidFieldsValuesException {
		if (roleUsersRestrictions != null) {
			UserInformation ui = ((UserInformation) SecurityContextHolder.getContext().getAuthentication()
					.getPrincipal());
			String usuario = ((UserInformation) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
					.getLogin();
System.err.println(usuario);
			for (GrantedAuthority rol : ui.getAuthorities()) {
				if (roleUsersRestrictions.contains(rol.getAuthority())) {
					switch (rol.getAuthority()) {
					case UserRoleDao.ROLE_CEO:
						// sin restricciónes
						break;
					case UserRoleDao.ROLE_MANAGER:
					case UserRoleDao.ROLE_STAFF:
						if(ui.getOtherData().get(UserDao.ATTR_HTL) == null) {
							throw new InvalidFieldsValuesException("Configuración del usuario Incompleta");
						}
						if (htl_colum == null) {
							throw new LiadaPardaException("Columna Hotel Id requerida y no especificada.");
						}
						keyMap.put(htl_colum, ui.getOtherData().get(UserDao.ATTR_HTL));
						break;

					case UserRoleDao.ROLE_CUSTOMER:
						keyMap.put(user_colum, usuario);
						break;

					case UserRoleDao.ROLE_USER:
						// restricciones a nivel de permisos en metodo
						break;
					default:
						throw new LiadaPardaException("Rol desconocido");
					}
				}
			}

		}
	}
}
