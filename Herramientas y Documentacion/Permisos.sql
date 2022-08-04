INSERT INTO trole (id_rolename,rolename,xmlclientpermission) VALUES (1,'adminHotel','<?xml version="1.0" encoding="UTF-8"?><security></security>');
INSERT INTO trole (id_rolename,rolename,xmlclientpermission) VALUES (2,'staff','<?xml version="1.0" encoding="UTF-8"?><security></security>');
INSERT INTO trole (id_rolename,rolename,xmlclientpermission) VALUES (3,'user','<?xml version="1.0" encoding="UTF-8"?><security></security>');






INSERT INTO tserver_permission VALUES (1,'com.ontimize.atomicHotelsApiRest.api.core.service.IHotelService/hotelQuery');
INSERT INTO tserver_permission VALUES (2,'com.ontimize.atomicHotelsApiRest.api.core.service.IHotelService/hotelInsert');
INSERT INTO tserver_permission VALUES (3,'com.ontimize.atomicHotelsApiRest.api.core.service.IHotelService/hotelUpdate');
INSERT INTO tserver_permission VALUES (4,'com.ontimize.atomicHotelsApiRest.api.core.service.IHotelService/hotelDelete');


INSERT INTO tserver_permission VALUES (5,'com.ontimize.atomicHotelsApiRest.api.core.service.IBedComboService/bedComboQuery');
INSERT INTO tserver_permission VALUES (6,'com.ontimize.atomicHotelsApiRest.api.core.service.IBedComboService/bedComboInsert');
INSERT INTO tserver_permission VALUES (7,'com.ontimize.atomicHotelsApiRest.api.core.service.IBedComboService/bedComboUpdate');
INSERT INTO tserver_permission VALUES (8,'com.ontimize.atomicHotelsApiRest.api.core.service.IBedComboService/bedComboDelete');


INSERT INTO tserver_permission VALUES (9,'com.ontimize.atomicHotelsApiRest.api.core.service.IBookingGuestService/bookingGuestQuery');
INSERT INTO tserver_permission VALUES (10,'com.ontimize.atomicHotelsApiRest.api.core.service.IBookingGuestService/bookingGuestInsert');
INSERT INTO tserver_permission VALUES (11,'com.ontimize.atomicHotelsApiRest.api.core.service.IBookingGuestService/guestCountQuery');
INSERT INTO tserver_permission VALUES (12,'com.ontimize.atomicHotelsApiRest.api.core.service.IBookingGuestService/bookingGuestDelete');
INSERT INTO tserver_permission VALUES (13,'com.ontimize.atomicHotelsApiRest.api.core.service.IBookingGuestService/bookingGuestsInfoQuery');


INSERT INTO tserver_permission VALUES (14,'com.ontimize.atomicHotelsApiRest.api.core.service.IBookingService/bookingQuery');
INSERT INTO tserver_permission VALUES (15,'com.ontimize.atomicHotelsApiRest.api.core.service.IBookingService/bookingInsert');
INSERT INTO tserver_permission VALUES (16,'com.ontimize.atomicHotelsApiRest.api.core.service.IBookingService/bookingActionUpdate');
INSERT INTO tserver_permission VALUES (17,'com.ontimize.atomicHotelsApiRest.api.core.service.IBookingService/bookingDelete');
INSERT INTO tserver_permission VALUES (18,'com.ontimize.atomicHotelsApiRest.api.core.service.IBookingService/bookingInfoQuery');
INSERT INTO tserver_permission VALUES (19,'com.ontimize.atomicHotelsApiRest.api.core.service.IBookingService/bookingsInRangeQuery');
INSERT INTO tserver_permission VALUES (20,'com.ontimize.atomicHotelsApiRest.api.core.service.IBookingService/bookingsInRangeInfoQuery');
INSERT INTO tserver_permission VALUES (21,'com.ontimize.atomicHotelsApiRest.api.core.service.IBookingService/bookingDaysUnitaryRoomPriceQuery');
INSERT INTO tserver_permission VALUES (22,'com.ontimize.atomicHotelsApiRest.api.core.service.IBookingService/bookingsHotelsQuery');
INSERT INTO tserver_permission VALUES (23,'com.ontimize.atomicHotelsApiRest.api.core.service.IBookingService/booking_now_by_room_numberQuery');
INSERT INTO tserver_permission VALUES (24,'com.ontimize.atomicHotelsApiRest.api.core.service.IBookingService/bookingSlotsInfoQuery');
INSERT INTO tserver_permission VALUES (25,'com.ontimize.atomicHotelsApiRest.api.core.service.IBookingService/bookingCompleteInfoQuery');
INSERT INTO tserver_permission VALUES (26,'com.ontimize.atomicHotelsApiRest.api.core.service.IBookingService/bookingHotelRoomRoomTypeQuery');


INSERT INTO tserver_permission VALUES (27,'com.ontimize.atomicHotelsApiRest.api.core.service.IBookingServiceExtraService/bookingServiceExtraQuery');
INSERT INTO tserver_permission VALUES (28,'com.ontimize.atomicHotelsApiRest.api.core.service.IBookingServiceExtraService/bookingServiceExtraInsert');
INSERT INTO tserver_permission VALUES (29,'com.ontimize.atomicHotelsApiRest.api.core.service.IBookingServiceExtraService/bookingServiceExtraDelete');
INSERT INTO tserver_permission VALUES (30,'com.ontimize.atomicHotelsApiRest.api.core.service.IBookingServiceExtraService/bookingExtraServicePriceUnitsTotalQuery');
INSERT INTO tserver_permission VALUES (31,'com.ontimize.atomicHotelsApiRest.api.core.service.IBookingServiceExtraService/extraServicesNameDescriptionUnitsPriceDateQuery');


INSERT INTO tserver_permission VALUES (32,'com.ontimize.atomicHotelsApiRest.api.core.service.ICountryService/countryQuery');


INSERT INTO tserver_permission VALUES (33,'com.ontimize.atomicHotelsApiRest.api.core.service.ICreditCardService/creditCardQuery');
INSERT INTO tserver_permission VALUES (34,'com.ontimize.atomicHotelsApiRest.api.core.service.ICreditCardService/creditCardInsert');
INSERT INTO tserver_permission VALUES (35,'com.ontimize.atomicHotelsApiRest.api.core.service.ICreditCardService/creditCardDelete');


INSERT INTO tserver_permission VALUES (36,'com.ontimize.atomicHotelsApiRest.api.core.service.ICustomerCreditCardService/customerCreditCardQuery');
INSERT INTO tserver_permission VALUES (37,'com.ontimize.atomicHotelsApiRest.api.core.service.ICustomerCreditCardService/customerCreditCardInsert');
INSERT INTO tserver_permission VALUES (38,'com.ontimize.atomicHotelsApiRest.api.core.service.ICustomerCreditCardService/customerCreditCardDelete');


INSERT INTO tserver_permission VALUES (39,'com.ontimize.atomicHotelsApiRest.api.core.service.ICustomerService/customerQuery');
INSERT INTO tserver_permission VALUES (40,'com.ontimize.atomicHotelsApiRest.api.core.service.ICustomerService/mailAgreementQuery');
INSERT INTO tserver_permission VALUES (41,'com.ontimize.atomicHotelsApiRest.api.core.service.ICustomerService/isCustomerValidBookingHolder');
INSERT INTO tserver_permission VALUES (42,'com.ontimize.atomicHotelsApiRest.api.core.service.ICustomerService/customerDelete');
INSERT INTO tserver_permission VALUES (43,'com.ontimize.atomicHotelsApiRest.api.core.service.ICustomerService/businessCustomerInsert');
INSERT INTO tserver_permission VALUES (44,'com.ontimize.atomicHotelsApiRest.api.core.service.ICustomerService/regularCustomerInsert');
INSERT INTO tserver_permission VALUES (45,'com.ontimize.atomicHotelsApiRest.api.core.service.ICustomerService/customerCancelUpdate');
INSERT INTO tserver_permission VALUES (46,'com.ontimize.atomicHotelsApiRest.api.core.service.ICustomerService/customerBusinessUpdate');
INSERT INTO tserver_permission VALUES (47,'com.ontimize.atomicHotelsApiRest.api.core.service.ICustomerService/customerRegularUpdate');


INSERT INTO tserver_permission VALUES (48,'com.ontimize.atomicHotelsApiRest.api.core.service.IFeatureService/featureQuery');
INSERT INTO tserver_permission VALUES (49,'com.ontimize.atomicHotelsApiRest.api.core.service.IFeatureService/featureInsert');
INSERT INTO tserver_permission VALUES (50,'com.ontimize.atomicHotelsApiRest.api.core.service.IFeatureService/featureUpdate');
INSERT INTO tserver_permission VALUES (51,'com.ontimize.atomicHotelsApiRest.api.core.service.IFeatureService/featureDelete');


INSERT INTO tserver_permission VALUES (52,'com.ontimize.atomicHotelsApiRest.api.core.service.IHotelServiceExtraService/hotelServiceExtraQuery');
INSERT INTO tserver_permission VALUES (53,'com.ontimize.atomicHotelsApiRest.api.core.service.IHotelServiceExtraService/hotelServiceExtraInsert');
INSERT INTO tserver_permission VALUES (54,'com.ontimize.atomicHotelsApiRest.api.core.service.IHotelServiceExtraService/hotelServiceExtraUpdate');
INSERT INTO tserver_permission VALUES (55,'com.ontimize.atomicHotelsApiRest.api.core.service.IHotelServiceExtraService/hotelServiceExtraDelete');


INSERT INTO tserver_permission VALUES (56,'com.ontimize.atomicHotelsApiRest.api.core.service.IHotelServiceService/hotelServiceQuery');
INSERT INTO tserver_permission VALUES (57,'com.ontimize.atomicHotelsApiRest.api.core.service.IHotelServiceService/hotelServiceInsert');
INSERT INTO tserver_permission VALUES (58,'com.ontimize.atomicHotelsApiRest.api.core.service.IHotelServiceService/hotelServiceUpdate');
INSERT INTO tserver_permission VALUES (59,'com.ontimize.atomicHotelsApiRest.api.core.service.IHotelServiceService/hotelServiceDelete');


INSERT INTO tserver_permission VALUES (60,'com.ontimize.atomicHotelsApiRest.api.core.service.IReceiptService/receiptQuery');
INSERT INTO tserver_permission VALUES (61,'com.ontimize.atomicHotelsApiRest.api.core.service.IReceiptService/receiptInsert');
INSERT INTO tserver_permission VALUES (62,'com.ontimize.atomicHotelsApiRest.api.core.service.IReceiptService/completeReceiptQuery');
INSERT INTO tserver_permission VALUES (63,'com.ontimize.atomicHotelsApiRest.api.core.service.IReceiptService/receiptDelete');


INSERT INTO tserver_permission VALUES (64,'com.ontimize.atomicHotelsApiRest.api.core.service.IRoomService/roomQuery');
INSERT INTO tserver_permission VALUES (65,'com.ontimize.atomicHotelsApiRest.api.core.service.IRoomService/roomInsert');
INSERT INTO tserver_permission VALUES (66,'com.ontimize.atomicHotelsApiRest.api.core.service.IRoomService/roomUpdate');
INSERT INTO tserver_permission VALUES (67,'com.ontimize.atomicHotelsApiRest.api.core.service.IRoomService/roomDelete');
INSERT INTO tserver_permission VALUES (68,'com.ontimize.atomicHotelsApiRest.api.core.service.IRoomService/roomsUnbookedInRangeQuery');
INSERT INTO tserver_permission VALUES (69,'com.ontimize.atomicHotelsApiRest.api.core.service.IRoomService/isRoomUnbookedgInRange');
INSERT INTO tserver_permission VALUES (70,'com.ontimize.atomicHotelsApiRest.api.core.service.IRoomService/roomInfoQuery');
INSERT INTO tserver_permission VALUES (71,'com.ontimize.atomicHotelsApiRest.api.core.service.IRoomService/infoHotelFeaturesQuery');


INSERT INTO tserver_permission VALUES (72,'com.ontimize.atomicHotelsApiRest.api.core.service.IRoomTypeFeatureService/roomTypeFeatureQuery');
INSERT INTO tserver_permission VALUES (73,'com.ontimize.atomicHotelsApiRest.api.core.service.IRoomTypeFeatureService/roomTypeFeatureInsert');
INSERT INTO tserver_permission VALUES (74,'com.ontimize.atomicHotelsApiRest.api.core.service.IRoomTypeFeatureService/roomTypeFeatureDelete');


INSERT INTO tserver_permission VALUES (75,'com.ontimize.atomicHotelsApiRest.api.core.service.IRoomTypeService/roomTypeQuery');
INSERT INTO tserver_permission VALUES (76,'com.ontimize.atomicHotelsApiRest.api.core.service.IRoomTypeService/roomTypeInsert');
INSERT INTO tserver_permission VALUES (77,'com.ontimize.atomicHotelsApiRest.api.core.service.IRoomTypeService/roomTypeUpdate');
INSERT INTO tserver_permission VALUES (78,'com.ontimize.atomicHotelsApiRest.api.core.service.IRoomTypeService/roomTypeDelete');
INSERT INTO tserver_permission VALUES (79,'com.ontimize.atomicHotelsApiRest.api.core.service.IRoomTypeService/infoQuery');
INSERT INTO tserver_permission VALUES (80,'com.ontimize.atomicHotelsApiRest.api.core.service.IRoomTypeService/infoRoomFeaturesQuery');


INSERT INTO tserver_permission VALUES (81,'com.ontimize.atomicHotelsApiRest.api.core.service.IServiceService/serviceQuery');
INSERT INTO tserver_permission VALUES (82,'com.ontimize.atomicHotelsApiRest.api.core.service.IServiceService/serviceInsert');
INSERT INTO tserver_permission VALUES (83,'com.ontimize.atomicHotelsApiRest.api.core.service.IServiceService/serviceUpdate');
INSERT INTO tserver_permission VALUES (84,'com.ontimize.atomicHotelsApiRest.api.core.service.IServiceService/serviceDelete');


INSERT INTO tserver_permission VALUES (85,'com.ontimize.atomicHotelsApiRest.api.core.service.IServicesXtraService/servicesXtraQuery');
INSERT INTO tserver_permission VALUES (86,'com.ontimize.atomicHotelsApiRest.api.core.service.IServicesXtraService/servicesXtraInsert');
INSERT INTO tserver_permission VALUES (87,'com.ontimize.atomicHotelsApiRest.api.core.service.IServicesXtraService/servicesXtraUpdate');
INSERT INTO tserver_permission VALUES (88,'com.ontimize.atomicHotelsApiRest.api.core.service.IServicesXtraService/servicesXtraDelete');


INSERT INTO tserver_permission VALUES (89,'com.ontimize.atomicHotelsApiRest.api.core.service.IUserService/userQuery');
INSERT INTO tserver_permission VALUES (90,'com.ontimize.atomicHotelsApiRest.api.core.service.IUserService/userInsert');
INSERT INTO tserver_permission VALUES (91,'com.ontimize.atomicHotelsApiRest.api.core.service.IUserService/userUpdate');
INSERT INTO tserver_permission VALUES (92,'com.ontimize.atomicHotelsApiRest.api.core.service.IUserService/userDelete');
