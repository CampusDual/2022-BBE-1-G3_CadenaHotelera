package com.ontimize.atomicHotelsApiRest.model.core.tools;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.ontimize.atomicHotelsApiRest.api.core.exceptions.InvalidFieldsException;
import com.ontimize.atomicHotelsApiRest.api.core.exceptions.InvalidFieldsValuesException;
import com.ontimize.atomicHotelsApiRest.api.core.exceptions.LiadaPardaException;
import com.ontimize.atomicHotelsApiRest.api.core.exceptions.MissingFieldsException;
import com.ontimize.atomicHotelsApiRest.api.core.exceptions.RestrictedFieldException;
import com.ontimize.atomicHotelsApiRest.api.core.service.ICountryService;
import com.ontimize.atomicHotelsApiRest.model.core.dao.BookingDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.CustomerDao;
import com.ontimize.atomicHotelsApiRest.model.core.tools.TypeCodes.type;
import com.ontimize.jee.common.db.SQLStatementBuilder;

/**
 * Validador de campos y valores de keyMaps (filtros) y attrList (columnas),
 * según el tipado que asignamos en el e Dao \n Cambia el tipo a algunos valores
 * (Date pasa Strings a Date en el propio keyMap)
 * 
 * @author Ar
 *
 */

@Component
public class ControlFields {

	private Map<String, type> fields;
	private List<String> restricted;
	private List<String> required;
	private List<String> roleUsersRestrictions;
	private boolean optional;
	private boolean noEmptyList;
	private boolean noWildcard;
	private boolean noColumns;
	private boolean allowBasicExpression;
	private boolean controlPermissionsActive;

	@Autowired
	ICountryService countryService;

	@Autowired
	ValidateFields vF;

	@Autowired
	ControlPermissions permissions;

	public ControlFields() {
//		reset();
	}

	public void reset() {
		fields = new HashMap<String, type>();
		restricted = null;
		required = null;
		optional = true;
		noEmptyList = true; // solo para las Listas no los HashMap
		noWildcard = true;
		noColumns = false;
		allowBasicExpression = true;

		resetPermissions();

	}

	public void resetPermissions() {
		// permisos		
		permissions.reset();		
		controlPermissionsActive = true;
	}
	public void setAllowBasicExpression(boolean allowBasicExpression) {
		this.allowBasicExpression = allowBasicExpression;
	}

	public void setControlPermissionsActive(boolean controlPermissionsActive) {
		this.controlPermissionsActive = controlPermissionsActive;
	}

	public void setCPHtlColum(String columna) {
		permissions.setHtlColum(columna);
	}

	public void setCPRoleUsersRestrictions(String... roleUsersRestrictions) {
		permissions.setRoleUsersRestrictions(roleUsersRestrictions);
	}

	public void addBasics(Map<String, type> fields) {
		this.fields.putAll(fields);
	}

	public void addBasics(Map<String, type>... arrayfields) {
		for (Map<String, type> fields : arrayfields) {
			this.fields.putAll(fields);
		}
	}

	public void setRequired(List<String> requiredFields) {
		this.required = requiredFields;
	}

	public void setRestricted(List<String> restrictedFields) {
		this.restricted = restrictedFields;
	}

	public void setOptional(boolean optional) {
		this.optional = optional;
	}

	public boolean isNoEmptyList() {
		return noEmptyList;
	}

	public void setNoEmptyList(boolean noEmptyList) {
		this.noEmptyList = noEmptyList;
	}

	public boolean isNoWildcard() {
		return noWildcard;
	}

	public void setNoWildcard(boolean noWildcard) {
		this.noWildcard = noWildcard;
	}

	/**
	 * 
	 * @param keyMap
	 * @throws MissingFieldsException
	 * @throws RestrictedFieldException
	 * @throws InvalidFieldsException
	 * @throws InvalidFieldsValuesException
	 * @throws LiadaPardaException
	 */
	@SuppressWarnings("static-access")
	public void validate(Map<String, Object> keyMap) throws MissingFieldsException, RestrictedFieldException,
			InvalidFieldsException, InvalidFieldsValuesException, LiadaPardaException {

		if (keyMap == null) {
			throw new MissingFieldsException(ErrorMessage.NO_NULL_DATA);
		}

		// no podemos hacer este metodo, porque
//		if( !allowBasicExpression && keyMap.containsKey(SQLStatementBuilder.ExtendedSQLConditionValuesProcessor.EXPRESSION_KEY)) {		
//			throw new InvalidFieldsException(ErrorMessage.NO_BASIC_EXPRESSION);
//		}

		if (required != null) {
			for (String key : required) {
				if (!keyMap.containsKey(key)) {
					throw new MissingFieldsException(ErrorMessage.REQUIRED_FIELD + key);
				}
			}
		}

		if (restricted != null) {
			for (String key : restricted) {
				if (keyMap.containsKey(key)) {
					throw new RestrictedFieldException(ErrorMessage.INVALID_FIELD + key);
				}
			}
		}

		if (!optional && required == null) {
			throw new LiadaPardaException(ErrorMessage.INTERNAL_CAGADA);
		} else {
			if (!optional && (required.size() != keyMap.size())) {
				throw new InvalidFieldsException(ErrorMessage.ALLOWED_FIELDS + required.toString());
			}
		}

//validar typos y valores
		for (String key : keyMap.keySet()) {
			boolean validType = false;

			if (fields.containsKey(key)) {// valida que exista en los fields
				if (keyMap.get(key) == null) {
					throw new MissingFieldsException(ErrorMessage.NO_NULL_VALUE + key);
				}

				switch (fields.get(key)) {
				case STRING:
					if ((keyMap.get(key) instanceof String)) {
						validType = true;
					}
					break;
				case NO_EMPTY_STRING:
					if ((keyMap.get(key) instanceof String && !((String) keyMap.get(key)).isEmpty())) {
						validType = true;
					}
					break;
				case INTEGER:
					if ((keyMap.get(key) instanceof Integer)) {
						validType = true;
					}
					break;
				case LONG:
					if (keyMap.get(key) instanceof Integer || keyMap.get(key) instanceof Long) {
						validType = true;
					}
					break;

				case DOUBLE:
					if (keyMap.get(key) instanceof Integer || keyMap.get(key) instanceof Long
							|| keyMap.get(key) instanceof Double) {
						validType = true;
					}
					break;

				case PRICE:
					if (keyMap.get(key) instanceof Integer || keyMap.get(key) instanceof Double) {
						vF.formatprice(keyMap.get(key));
						validType = true;
					}
					break;

				case CREDIT_CARD:
					if (keyMap.get(key) instanceof Long) {

						vF.invalidCreditCard((Long) keyMap.get(key));
						validType = true;
					}
					break;

				case EXPIRATION_DATE:
					if ((keyMap.get(key) instanceof String)) {
						vF.validDateExpiry((String) keyMap.get(key));
						validType = true;
					}
					break;

				case PHONE:
					if ((keyMap.get(key) instanceof String)) {
						vF.isPhone((String) keyMap.get(key));
						validType = true;
					}
					break;

				case COUNTRY:
					if ((keyMap.get(key) instanceof String)) {
						vF.country((String) keyMap.get(key));
						String country = (String) keyMap.get(key);
						validType = true;
					}
					break;

				case DATETIME:// diferenciar al devolver los datos
				case DATE:
					if ((keyMap.get(key) instanceof String)) {
						keyMap.replace(key, vF.stringToDate((String) keyMap.get(key)));
						validType = true;
					} else if ((keyMap.get(key) instanceof Date)) {
						validType = true;
					}
					break;

				case EMAIL:
					if ((keyMap.get(key) instanceof String)) {
						vF.checkMail((String) keyMap.get(key));
						validType = true;
					}
					break;
				case DNI:
					if ((keyMap.get(key) instanceof String)) {
						vF.isDNI((String) keyMap.get(key));
						keyMap.replace(key, ((String) keyMap.get(key)).toUpperCase());
						validType = true;
					}
					break;

				case INTEGER_UNSIGNED:
					if ((keyMap.get(key) instanceof Integer)) {
						vF.NegativeNotAllowed((Integer) keyMap.get(key));
						validType = true;
					}
					break;

				case BOOLEAN:
					if ((keyMap.get(key) instanceof Integer)) {
						vF.isBoolean((Integer) keyMap.get(key));
						validType = true;
					}
					break;

				case BOOKING_ACTION:
					if ((keyMap.get(key) instanceof BookingDao.Action)) {
						validType = true;
					} else if ((keyMap.get(key) instanceof String)) {
						try {
							keyMap.replace(key, BookingDao.Action.valueOf((String) keyMap.get(key)));
							validType = true;
						} catch (IllegalArgumentException e) {
							validType = false;
						}
					}
					break;

				case CUSTOMER_ACTION:

					if ((keyMap.get(key) instanceof CustomerDao.Action)) {
						validType = true;
					} else if ((keyMap.get(key) instanceof String)) {
						try {
							keyMap.replace(key, CustomerDao.Action.valueOf((String) keyMap.get(key)));
							validType = true;
						} catch (IllegalArgumentException e) {
							validType = false;
						}
					}
					break;

				default:
					throw new InvalidFieldsValuesException(ErrorMessage.WRONG_TYPE + key);
				}

				if (!validType) {
					throw new InvalidFieldsValuesException(
							ErrorMessage.WRONG_TYPE + key + ErrorMessage.REQUIRED_TYPE + fields.get(key));
				}

				// STRING, INTEGER, INTEGER_UNSIGNED, LONG, LONG_UNSIGNED, DOUBLE,
				// DOUBLE_UNSIGNED, PRICE, EMAIL, CREDIT_CARD,
				// PHONE, DATE, DATETIME, ACTION, BOOLEAN, COUNTRY

			} else {
				if (allowBasicExpression
						&& key.equals(SQLStatementBuilder.ExtendedSQLConditionValuesProcessor.EXPRESSION_KEY)) {
					// TODO comprobamos contenido de basic expresion....
				} else {
					throw new InvalidFieldsException(ErrorMessage.INVALID_FIELD + key);
				}
			}
		}
		// permisos
		if (controlPermissionsActive) {
			permissions.restrict(keyMap);
		}
	}

	/**
	 * Para validar atributos de vuelta. Ignora los valores.
	 * 
	 * @param columns Lista de camos a validar
	 * @throws MissingFieldsException
	 * @throws RestrictedFieldException
	 * @throws LiadaPardaException
	 * @throws InvalidFieldsException
	 */
	public void validate(List<String> columns)
			throws MissingFieldsException, RestrictedFieldException, LiadaPardaException, InvalidFieldsException {

		if (columns == null) {
			throw new MissingFieldsException(ErrorMessage.NO_NULL_DATA);
		}

		if (noEmptyList) {
			int minimuSize = 1;

			if (noWildcard && columns.contains("*")) {
				minimuSize++;
			}

			if (columns.size() < minimuSize) {
				throw new MissingFieldsException(ErrorMessage.REQUIRED_MINIMUM_COLUMS);
			}

			if (required != null) {
				for (String key : required) {
					if (!columns.contains(key)) {
						throw new MissingFieldsException(ErrorMessage.REQUIRED_FIELD);
					}
				}
			}

			if (restricted != null) {
				for (String key : restricted) {
					if (columns.contains(key)) {
						throw new RestrictedFieldException(ErrorMessage.INVALID_FIELD + key);
					}
				}
			}

			if (!optional && required == null) {
				throw new LiadaPardaException(ErrorMessage.INTERNAL_CAGADA);
			} else {
				if (!optional && (required.size() != columns.size())) {
					throw new InvalidFieldsException(ErrorMessage.ALLOWED_FIELDS + required.toString());
				}
			}

			for (String key : columns) {
				if (!fields.containsKey(key)) {
					throw new InvalidFieldsException(ErrorMessage.INVALID_FIELD + key);
				}
			}
		} else { // si tiene que ser lista vacía;
			if (noWildcard && columns.contains("*")) {
				columns.remove("*");
			}
			if (columns.size() > 0) {
				throw new InvalidFieldsException(ErrorMessage.NO_ALLOW_COLUMS);
			} else {
				columns.add("null"); // para saltarse los filtros de ontimize
			}
		}
	}

	public void addCPUser(boolean b) {
		permissions.addUser(b);
	}

//	public static void set(Map<String, Object> keyMap, String... fields) throws MissingFieldsException {
//		for (String field : fields) {
//			if (!keyMap.containsKey(field)) {
//				throw new MissingFieldsException("Falta el campo " + field);
//			}
//			if (keyMap.get(field) == null) {
//				throw new MissingFieldsException("El campo " + field + " es nulo");
//			}
//		}
//	}

}
