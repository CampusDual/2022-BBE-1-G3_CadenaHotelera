package com.ontimize.atomicHotelsApiRest.model.core.tools;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ontimize.atomicHotelsApiRest.api.core.exceptions.InvalidFieldsException;
import com.ontimize.atomicHotelsApiRest.api.core.exceptions.InvalidFieldsValuesException;
import com.ontimize.atomicHotelsApiRest.api.core.exceptions.LiadaPardaException;
import com.ontimize.atomicHotelsApiRest.api.core.exceptions.MissingFieldsException;
import com.ontimize.atomicHotelsApiRest.api.core.exceptions.RestrictedFieldException;
import com.ontimize.atomicHotelsApiRest.model.core.tools.TypeCodes.type;

public class ControlFields {

	private Map<String, type> fields = new HashMap<String, type>();
	private List<String> restricted = null;
	private List<String> required = null;
	private boolean optional = true;

	public ControlFields() {

	}

	public void addBasics(Map<String, type> fields) {
		this.fields.putAll(fields);
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

	public void validate(Map<String, Object> keyMap) throws MissingFieldsException, RestrictedFieldException,
			InvalidFieldsException, InvalidFieldsValuesException, LiadaPardaException {

		if (required != null) {
			for (String key : required) {
				if (!keyMap.containsKey(key)) {
					throw new MissingFieldsException(ErrorMessage.REQUIRED_FIELD);
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

		boolean validType = false;
		for (String key : keyMap.keySet()) {
			if (fields.containsKey(key)) {

				switch (fields.get(key)) {
				case STRING:
					if ((keyMap.get(key) instanceof String)) {
						validType = true;
					}
					break;
				case INTEGER:
					if ((keyMap.get(key) instanceof Integer)) {
						validType = true;
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
				throw new InvalidFieldsException(ErrorMessage.INVALID_FIELD + key);
			}
		}
	}

	public void validate(List<String> list) {

	}

	public static void set(Map<String, Object> keyMap, String... fields) throws MissingFieldsException {
		for (String field : fields) {
			if (!keyMap.containsKey(field)) {
				throw new MissingFieldsException("Falta el campo " + field);
			}
			if (keyMap.get(field) == null) {
				throw new MissingFieldsException("El campo " + field + " es nulo");
			}
		}
	}

}
