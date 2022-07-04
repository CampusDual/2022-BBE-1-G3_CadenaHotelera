package com.ontimize.atomicHotelsApiRest.model.core.ontimizeExtra;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.ontimize.jee.common.dto.EntityResult;

public class EntityResultExtraTools {
private EntityResultExtraTools() {

}
	/**
	 * Obtiene una Lista de los registros en una columna del EntityResult. Puede tener menos resultados que el EntityResult
	 * @param er Entity Result
	 * @param fromColumn String de la columna en el HashMap
	 * @return List<Object> 
	 */
	public static List<Object> listFromColumn(EntityResult er, String fromColumn) {
		List<Object> list = new ArrayList<>();
		Map<String,Object> line;
		for(int i = 0 ;i< er.calculateRecordNumber();i++) {
			line = er.getRecordValues(i);
//			if(er.getColumnSQLType(fromColumn);
			if(line.containsKey(fromColumn)) {
				list.add(line.get(fromColumn));
			}
		}
		return list;
	}

}
