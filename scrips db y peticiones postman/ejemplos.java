class codigosEjemplo{
	
/* @ProfeLorena
 * Cuando quereis hacer una consulta, necesitais pasarle los filtros y las columnas que quereis que os devuelva. 
 * Métodos que os ayudan a esto: 
 */
	EntityResultTools.keysvalues("nombre_columna","valor_columna",“nombre_columna2","valor_columna2"…);
	EntityResultTools.attributes("columna_devolver", "columna_devolver2","columna_devolver3"); //....
	
	//Ejemplo:
	@Autowired
	private DefaultOntimizeDaoHelper    daoHelper;
	
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult roomsByHotelQuery(Map<String, Object> keysValues, List<String> attributes) {
		//…
		/* CONSULTA LA QUERY DEL DAO hotelDao cuyo id=" queryRooms" pasándole el // filtro HTL_ID = keysValues.get("HOTEL_ID") y devolviendo las columnas HTL_ID, HTL_NAME Y HTL_COUNTRY*/
		EntityResult queryRes = this.daoHelper.query(this.hotelDao, EntityResultTools.keysvalues("HTL_ID", keysValues.get("HOTEL_ID")), EntityResultTools.attributes("HTL_ID", "HTL_NAME", "HTL_COUNTRY"),"queryRooms");
		  //   ….
		    return queryRes;    
	}
}