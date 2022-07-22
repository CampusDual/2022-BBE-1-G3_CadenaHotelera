package com.ontimize.atomicHotelsApiRest.model.core.tools;

public class ErrorMessage {
	
//ERRORES DE CREATE
	public static final String CREATION_ERROR = "Error de creación ";		
	public static final String CREATION_ERROR_DUPLICATED_FIELD = CREATION_ERROR+"- El registro ya existe";	 
	public static final String CREATION_ERROR_MISSING_FK = CREATION_ERROR+"- No existe la referencia necesaria en otra tabla";
	public static final String INVALID_MAIL= "Campo mail no es correcto"; 
	 
//ERRORES DE UPDATE
	public static final String UPDATE_ERROR = "Error de actualización ";	 
	public static final String UPDATE_ERROR_MISSING_FIELD = UPDATE_ERROR+"- El registro que pretende actualizar no existe.";	
	public static final String UPDATE_ERROR_DUPLICATED_FIELD = UPDATE_ERROR+"- No es posible duplicar un registro";	 
	public static final String UPDATE_ERROR_REQUIRED_FIELDS = UPDATE_ERROR+"- No es posible eliminar campos obligatorios";	 
	public static final String UPDATE_ERROR_MISSING_FK = UPDATE_ERROR+"- No existe la referencia necesaria en otra tabla";
 
	 
//ERRORES DE DELETE
    public static final String DELETE_ERROR = "Error de eliminación ";	 
	public static final String DELETE_ERROR_MISSING_FIELD = DELETE_ERROR+"- El registro que pretende eliminar no existe.";	 
	public static final String DELETE_ERROR_FOREING_KEY = DELETE_ERROR+"- Está referenciado en alguna otra tabla (FK)";	 
	 
//ERRORES DE QUERY
	public static final String SUBQUERY_ERROR = "Error de subconsulta  ";	 
	public static final String RESULT_REQUIRED = SUBQUERY_ERROR + "- Datos requeridos no encontrados";
	
	 
//ERRORES VARIOS	 
	public static final String DATA_RANGE_REVERSE = "La fecha de inicio no puede ser posterior a la fecha de fin";	 
	public static final String DATA_START_BEFORE_TODAY = "La fecha de inicio no puede ser anterior a hoy";
	public static final String DATA_EXPIRY_BEFORE_TODAY = "Error de fecha o la fecha de validez no puede ser anterior a hoy ";
	public static final String ALLOWED_FIELDS = "Campos permitidos: \n\t";
	public static final String REQUIRED_FIELD = "Falta campo requerido - ";
	public static final String REQUIRED_MINIMUM_FIELDs = "Se requiere mínimo un campo";
	public static final String INVALID_FIELD = "Campo no válido - ";
	public static final String REQUIRED_COLUMNS = "Faltan columnas requeridas";
	public static final String INVALID_ACTION = "Acción no válida";
	public static final String NEGATIVE_OR_CERO_NOT_ALLOWED="Número igual o menor a 0 no permitido";
	public static final String INVALID_NUMBER_CREDITCARD="Número de tarejeta no válido";
	public static final String WRONG_TYPE = "Tipo incorrecto - ";
	public static final String REQUIRED_TYPE = " - Tipo requerido - ";
	public static final String ERROR = "Error";
	
	public static final String INTERNAL_CAGADA = "Error interno";
	
}
