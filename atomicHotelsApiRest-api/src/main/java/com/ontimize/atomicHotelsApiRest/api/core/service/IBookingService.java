package com.ontimize.atomicHotelsApiRest.api.core.service;

import java.util.List;
import java.util.Map;

import com.ontimize.atomicHotelsApiRest.api.core.exceptions.EntityResultRequiredException;
import com.ontimize.atomicHotelsApiRest.api.core.exceptions.InvalidFieldsValuesException;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.exceptions.OntimizeJEERuntimeException;

public interface IBookingService {
	
	public EntityResult bookingQuery(Map<String, Object> keyMap, List<String> attrList) throws OntimizeJEERuntimeException;
	public EntityResult bookingInfoQuery(Map<String, Object> keyMap, List<String> attrList) throws OntimizeJEERuntimeException;
	public EntityResult bookingInsert(Map<String, Object> attrMap) throws OntimizeJEERuntimeException, EntityResultRequiredException;
	public EntityResult bookingActionUpdate(Map<String, Object> attrMap, Map<String, Object> keyMap) throws OntimizeJEERuntimeException;
	public EntityResult bookingDelete(Map<String, Object> keyMap) throws OntimizeJEERuntimeException;
	public EntityResult bookingsInRangeQuery(Map<String, Object> keyMap, List<String> attrList) throws OntimizeJEERuntimeException, InvalidFieldsValuesException;
	public EntityResult bookingsInRangeInfoQuery(Map<String, Object> keyMap, List<String> attrList)
			throws OntimizeJEERuntimeException;
//	public EntityResult bookingDaysUnitaryRoomPriceQuery(Map<String, Object> keyMap, List<String> attrList) throws OntimizeJEERuntimeException;
//	public EntityResult bookingsHotelsQuery (Map<String, Object> keyMap, List<String> attrList) throws OntimizeJEERuntimeException;
	public EntityResult booking_now_by_room_numberQuery(Map<String, Object> keyMap, List<String> attrList) throws OntimizeJEERuntimeException;
//	public EntityResult bookingSlotsInfoQuery(Map<String, Object> keyMap, List<String> attrList) throws OntimizeJEERuntimeException;
	public EntityResult bookingCompleteInfoQuery(Map<String, Object> keyMap, List<String> attrList)
			throws OntimizeJEERuntimeException;
//	public EntityResult bookingHotelRoomRoomTypeQuery(Map<String, Object> keyMap, List<String> attrList) throws OntimizeJEERuntimeException;
}
