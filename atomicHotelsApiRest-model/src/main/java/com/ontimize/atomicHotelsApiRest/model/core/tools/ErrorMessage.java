package com.ontimize.atomicHotelsApiRest.model.core.tools;

public class ErrorMessage {
	
	 public static String CREATION_ERROR = "Error de creación ";
		
	 public static String CREATION_ERROR_DUPLICATED_FIELD = CREATION_ERROR+"- El registro ya existe";
	 
	 
	 public static String UPDATE_ERROR = "Error de actualización ";
	 
	 public static String UPDATE_ERROR_MISSING_FIELD = UPDATE_ERROR+"- El regsitro que pretende actualizar no existe.";
	 
	 public static String UPDATE_ERROR_DUPLICATED_FIELD = UPDATE_ERROR+"- No es posible duplicar un registro";
	 
	 public static String UPDATE_ERROR_REQUIRED_FIELDS = UPDATE_ERROR+" - No es psoible eliminar campos obligatorios";
	 
	 
     public static String DELETE_ERROR = "Error de eliminación ";
	 
	 public static String DELETE_ERROR_MISSING_FIELD = DELETE_ERROR+"- El regsitro que pretende eliminar no existe.";
	 
	 public static String DELETE_ERROR_FOREING_KEY = DELETE_ERROR+"- Está referenciado en alguna otra tabla (FK)";

}
