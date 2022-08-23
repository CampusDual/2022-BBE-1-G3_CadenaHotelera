package com.ontimize.atomicHotelsApiRest.model.core.tools;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import com.ontimize.atomicHotelsApiRest.model.core.dao.CustomerDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.HotelDao;
import com.ontimize.jee.common.db.SQLStatementBuilder.BasicExpression;
import com.ontimize.jee.common.db.SQLStatementBuilder.BasicField;
import com.ontimize.jee.common.db.SQLStatementBuilder.BasicOperator;
import static org.junit.jupiter.api.Assertions.*;

class EntityResultExtraToolsTest {

	@Nested
	@DisplayName("Test for bassics expressions")
	@TestInstance(TestInstance.Lifecycle.PER_CLASS)
	public class BasicExpressions {

		@Test
		@DisplayName("contiene key")
		void testEntityResultExtraToolsTestOk() {
			BasicField hotel_id = new BasicField(HotelDao.ATTR_ID);
			BasicField customer_name = new BasicField(CustomerDao.ATTR_NAME);
			BasicField trap = new BasicField(CustomerDao.ATTR_ID);

			BasicExpression exp01 = new BasicExpression(hotel_id, BasicOperator.LESS_EQUAL_OP, 5);
			BasicExpression exp02 = new BasicExpression(customer_name, BasicOperator.NOT_EQUAL_OP, "PACO");
			BasicExpression groupExp01 = new BasicExpression(exp01, BasicOperator.AND_OP, exp02);

			BasicExpression exp03 = new BasicExpression(trap, BasicOperator.LESS_EQUAL_OP, 4);

			BasicExpression finalExp = new BasicExpression(groupExp01, BasicOperator.OR_OP, exp03);
			assertTrue(EntityResultExtraTools.basicExpressionContains(finalExp, trap));
		}

	}

}
