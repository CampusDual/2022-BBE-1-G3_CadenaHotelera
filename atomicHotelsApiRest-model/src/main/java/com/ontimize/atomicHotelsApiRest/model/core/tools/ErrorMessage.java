package com.ontimize.atomicHotelsApiRest.model.core.tools;

public class ErrorMessage {
	
	 public static String CREATION_ERROR = "Error de creación ";
		
	 public static String CREATION_ERROR_DUPLICATED_FIELD = CREATION_ERROR+"- El registro ya existe";
	 
	 public static String CREATION_ERROR_MISSING_FK = CREATION_ERROR+"- No existe la referencia necesaria en otra tabla";
	 
	 
	 public static String UPDATE_ERROR = "Error de actualización ";
	 
	 public static String UPDATE_ERROR_MISSING_FIELD = UPDATE_ERROR+"- El regsitro que pretende actualizar no existe.";
	 
	 public static String UPDATE_ERROR_DUPLICATED_FIELD = UPDATE_ERROR+"- No es posible duplicar un registro";
	 
	 public static String UPDATE_ERROR_REQUIRED_FIELDS = UPDATE_ERROR+"- No es posible eliminar campos obligatorios";
	 
	 public static String UPDATE_ERROR_MISSING_FK = UPDATE_ERROR+"- No existe la referencia necesaria en otra tabla";
	 
	 
     public static String DELETE_ERROR = "Error de eliminación ";
	 
	 public static String DELETE_ERROR_MISSING_FIELD = DELETE_ERROR+"- El regsitro que pretende eliminar no existe.";
	 
	 public static String DELETE_ERROR_FOREING_KEY = DELETE_ERROR+"- Está referenciado en alguna otra tabla (FK)";
	 
	 
	 public static String SUBQUERY_ERROR = "Error de subconsulta  ";
	 
	 public static String RESULT_REQUIRED = SUBQUERY_ERROR + "- Datos requeridos no encontrados";
	 
	 
	 public static String DATA_RANGE_REVERSE = "La fecha de inicio no puede ser posterior a la fecha de fin)";
	 
	 public static String REQUIRED_FIELDS = "Faltan campos requeridos";

}
