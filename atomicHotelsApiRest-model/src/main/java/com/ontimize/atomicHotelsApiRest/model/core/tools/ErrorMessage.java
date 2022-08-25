package com.ontimize.atomicHotelsApiRest.model.core.tools;

public class ErrorMessage {
	
//ERRORES DE CREATE
	public static final String CREATION_ERROR = "Error de creación ";		
	public static final String CREATION_ERROR_DUPLICATED_FIELD = "El registro ya existe";	 
	public static final String CREATION_ERROR_MISSING_FK = "No existe la referencia necesaria en otra tabla";
	public static final String INVALID_MAIL= "Campo mail no es correcto"; 
	public static final String INVALID_PHONE= "Campo telefono no es correcto";
	public static final String INVALID_DNI= "Campo DNI no es correcto";
//ERRORES DE UPDATE
	public static final String UPDATE_ERROR = "Error de actualización ";	 
	public static final String UPDATE_ERROR_MISSING_FIELD = "El registro que pretende actualizar no existe.";	
	public static final String UPDATE_ERROR_WRONG_TYPE= "El registro que pretende actualizar no es del tipo adecuado.";	
	public static final String UPDATE_ERROR_DUPLICATED_FIELD = "No es posible duplicar un registro";	 
	public static final String UPDATE_ERROR_REQUIRED_FIELDS = "No es posible eliminar campos obligatorios";	 
	public static final String UPDATE_ERROR_MISSING_FK = "No existe la referencia necesaria en otra tabla";
	public static final String UPDATE_CUSTOMER_BOOKINGS_PENDING = "El cliente no puede ser modificado, tiene reservas pendientes.";
 
	 
//ERRORES DE DELETE
    public static final String DELETE_ERROR = "Error de eliminación ";	 
	public static final String DELETE_ERROR_MISSING_FIELD = "El registro que pretende eliminar no existe.";	 
	public static final String DELETE_ERROR_FOREING_KEY = "No es posible eliminar el registro. Está referenciado en alguna otra tabla (FK)";	 
	 
//ERRORES DE QUERY
	public static final String SUBQUERY_ERROR = "Error de subconsulta  ";	 
	public static final String RESULT_REQUIRED = "Datos requeridos no encontrados";
	public static final String INVALID_FILTER_FIELD_ID = "Identificador utilizado en el filtro no existe";
	public static final String INVALID_ROOM_ID = "La habitación no existe.";
	public static final String INVALID_HOTEL_ID = "El hotel no existe.";
	public static final String INVALID_CUSTOMER_ID = "El cliente no existe.";
	public static final String INVALID_QUESTION_ID = "La pregunta no existe.";
	public static final String BOOKED_ROOM = "La habitación está reservada dentro de ese rango de fechas";
	
	 
//ERRORES PERMISOS
	public static final String REQUIRED_HTL_ID = "Columna Hotel Id requerida y no especificada.";	 
	public static final String WRONG_HTL_ID = "No tiene permisos para esa referencia de hotel";	 
	public static final String INCOMPLETE_USER_DATA= "Configuración del usuario Incompleta";	 
	public static final String UNKNOWN_ROL = "Rol desconocido";	 
	
//ERRORES VARIOS
	
	public static final String DATA_RANGE_REVERSE = "La fecha de inicio no puede ser posterior a la fecha de fin";	 
	public static final String DATA_START_BEFORE_TODAY = "La fecha de inicio no puede ser anterior a hoy";
	public static final String DATA_EXPIRY_BEFORE_TODAY = "Error de fecha o la fecha de validez no puede ser anterior a hoy ";
	public static final String ALLOWED_FIELDS = "Campos permitidos: ";
	public static final String ALLOWED_COLUMSS = "Columnas permitidas: ";
	public static final String REQUIRED_FIELD = "Falta campo requerido - ";
	public static final String REQUIRED_COLUMN = "Falta columna requerida - ";
	public static final String REQUIRED_MINIMUM_COLUMS = "Se requiere mínimo una columna";
	public static final String INVALID_FIELD = "Campo no válido - ";
	public static final String INVALID_COLUM = "Columna no válida - ";
	
	public static final String REQUIRED_COLUMNS = "Faltan columnas requeridas";
	public static final String INVALID_ACTION = "Acción no válida";
	public static final String NEGATIVE_OR_CERO_NOT_ALLOWED="Número igual o menor a 0 no permitido";
	public static final String INVALID_NUMBER_CREDITCARD="Número de tarejeta no válido";
	public static final String WRONG_TYPE = "Tipo incorrecto - ";
	public static final String REQUIRED_TYPE = " - Tipo requerido - ";
	public static final String NO_NULL_DATA= "No se admiten datos null";
	public static final String NO_NULL_VALUE = "No se admite valor null - ";
	public static final String NO_GUEST_IN_NOT_CONFIRMED_BOOKING = "No se puede añadir un huésped a una reserva si ésta no se encuentra en estado 'CONFIRMED'";
	public static final String NO_RECEIPT_FOR_UNFINISH_BOOKING = "No se puede generar un recibo de una reserva que no está completada";
	public static final String NO_BOOKING_ID = "El número de reserva no existe";
	public static final String NO_CUSTOMER_ID = "El cliente no existe";
	public static final String BOOKING_COMPLETED_NO_MORE_GUESTS_ALLOWED = "La reserva está completa. No admite más huéspedes";
	public static final String GUEST_MUST_BE_AN_INDIVIDUAL = "Un huésped tiene que ser una persona física";
	
	public static final String UNKNOWN_ERROR = "Error desconocido";
	public static final String ERROR = UNKNOWN_ERROR ;
	public static final String INTERNAL_CAGADA = "Error interno";
	
	public static final String NO_BASIC_EXPRESSION = "No se permiten Basics Expresions";
	public static final String NO_ALLOW_COLUMS = "Esta consulta no permite especificar las Columnas de retorno";
	public static final String UNCAUGHT_EXCEPTION = "Excepción no capturada.";
	
	public static final String STRING_TOO_LONG =  "Cadena demasiado larga, máximo 255 caracteres.";
	public static final String SMALL_STRING_TOO_LONG =  "Cadena demasiado larga, máximo 50 caracteres.";
	public static final String STRING_EMPTY=  "No se admiten textos vacios.";
	

}
