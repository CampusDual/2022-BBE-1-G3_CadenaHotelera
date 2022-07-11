package com.ontimize.atomicHotelsApiRest.ws.core.rest;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ontimize.atomicHotelsApiRest.api.core.service.IRoomTypeService;
import com.ontimize.atomicHotelsApiRest.model.core.tools.EntityResultWrong;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.server.rest.ORestController;

@RestController // indica que esta clase trabaja como un controlador, que responderá a las
				// peticiones cuya URL tenga el path indicado en la anotación
@RequestMapping("/roomTypes") // (en este caso, roomTypes)
public class RoomTypesController extends ORestController<IRoomTypeService> {

	@Autowired // permite que los DAO se enlacen correctamente a las variables donde las hemos
				// definido, evitando el uso de métodos getter y setter.
	private IRoomTypeService roomTypesService;

	@Override
	public IRoomTypeService getService() {
		return this.roomTypesService;
	}

	@RequestMapping(value = "info", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public EntityResult infoSearch(@RequestBody Map<String, Object> req) {
		try {
//			List<String> attrList = (List<String>) req.get("columns");
//			Map<String, Object> keyMap = (Map<String, Object>) req.get("filter");
//			return roomTypesService.infoQuery(keyMap, attrList);
			return roomTypesService.infoQuery((Map<String, Object>) req.get("filter"), (List<String>) req.get("columns"));
		} catch (Exception e) {
			e.printStackTrace();
//			EntityResult res = new EntityResultMapImpl();
//			res.setCode(EntityResult.OPERATION_WRONG);
//			return res;
			return new EntityResultWrong(); //esta me la cree yo ;P (Ar)
		}
	}

}
