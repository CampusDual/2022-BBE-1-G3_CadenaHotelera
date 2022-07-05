package com.ontimize.atomicHotelsApiRest.model.core.ontimizeExtra;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.ontimize.jee.common.db.SQLStatementBuilder;
import com.ontimize.jee.common.db.SQLStatementBuilder.BasicExpression;
import com.ontimize.jee.common.db.SQLStatementBuilder.BasicOperator;
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

	/**
	 * Añade una basic expression al hashmap, en caso de que ya exista alguna BE, la concatena con AND   
	 * @param keyMap keyMap donde insertar la basic expression
	 * @param be
	 */
	public static void putBasicExpression(Map<String, Object> keyMap, BasicExpression be){
		BasicExpression finalBE;
		if (keyMap.containsKey(SQLStatementBuilder.ExtendedSQLConditionValuesProcessor.EXPRESSION_KEY)) {
			finalBE = new BasicExpression(
					keyMap.get(SQLStatementBuilder.ExtendedSQLConditionValuesProcessor.EXPRESSION_KEY),
					BasicOperator.AND_OP, be);
		}else{
			finalBE = be;
		}
		keyMap.put(SQLStatementBuilder.ExtendedSQLConditionValuesProcessor.EXPRESSION_KEY, finalBE);
	}
}
