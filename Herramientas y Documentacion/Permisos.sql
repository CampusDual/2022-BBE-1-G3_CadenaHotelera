
--metodos registrados
		-- hotel
		INSERT INTO tserver_permission VALUES (1,'com.ontimize.atomicHotelsApiRest.api.core.service.IHotelService/hotelQuery');
		INSERT INTO tserver_permission VALUES (2,'com.ontimize.atomicHotelsApiRest.api.core.service.IHotelService/hotelInsert');
		INSERT INTO tserver_permission VALUES (3,'com.ontimize.atomicHotelsApiRest.api.core.service.IHotelService/hotelUpdate');
		INSERT INTO tserver_permission VALUES (4,'com.ontimize.atomicHotelsApiRest.api.core.service.IHotelService/hotelDelete');
		
		-- bedCombo
		INSERT INTO tserver_permission VALUES (5,'com.ontimize.atomicHotelsApiRest.api.core.service.IBedComboService/bedComboQuery');
		INSERT INTO tserver_permission VALUES (6,'com.ontimize.atomicHotelsApiRest.api.core.service.IBedComboService/bedComboInsert');
		INSERT INTO tserver_permission VALUES (7,'com.ontimize.atomicHotelsApiRest.api.core.service.IBedComboService/bedComboUpdate');
		INSERT INTO tserver_permission VALUES (8,'com.ontimize.atomicHotelsApiRest.api.core.service.IBedComboService/bedComboDelete');
		
		-- bookingGuest
		INSERT INTO tserver_permission VALUES (9,'com.ontimize.atomicHotelsApiRest.api.core.service.IBookingGuestService/bookingGuestQuery');
		INSERT INTO tserver_permission VALUES (10,'com.ontimize.atomicHotelsApiRest.api.core.service.IBookingGuestService/bookingGuestInsert');
		INSERT INTO tserver_permission VALUES (11,'com.ontimize.atomicHotelsApiRest.api.core.service.IBookingGuestService/guestCountQuery');
		INSERT INTO tserver_permission VALUES (12,'com.ontimize.atomicHotelsApiRest.api.core.service.IBookingGuestService/bookingGuestDelete');
		INSERT INTO tserver_permission VALUES (13,'com.ontimize.atomicHotelsApiRest.api.core.service.IBookingGuestService/bookingGuestsInfoQuery');
		
		-- booking
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
		
		-- bookingServiceExtra
		INSERT INTO tserver_permission VALUES (27,'com.ontimize.atomicHotelsApiRest.api.core.service.IBookingServiceExtraService/bookingServiceExtraQuery');
		INSERT INTO tserver_permission VALUES (28,'com.ontimize.atomicHotelsApiRest.api.core.service.IBookingServiceExtraService/bookingServiceExtraInsert');
		INSERT INTO tserver_permission VALUES (29,'com.ontimize.atomicHotelsApiRest.api.core.service.IBookingServiceExtraService/bookingServiceExtraDelete');
		INSERT INTO tserver_permission VALUES (30,'com.ontimize.atomicHotelsApiRest.api.core.service.IBookingServiceExtraService/bookingExtraServicePriceUnitsTotalQuery');
		INSERT INTO tserver_permission VALUES (31,'com.ontimize.atomicHotelsApiRest.api.core.service.IBookingServiceExtraService/extraServicesNameDescriptionUnitsPriceDateQuery');
		
		-- country
		INSERT INTO tserver_permission VALUES (32,'com.ontimize.atomicHotelsApiRest.api.core.service.ICountryService/countryQuery');
		
		-- creditCard
		INSERT INTO tserver_permission VALUES (33,'com.ontimize.atomicHotelsApiRest.api.core.service.ICreditCardService/creditCardQuery');
		INSERT INTO tserver_permission VALUES (34,'com.ontimize.atomicHotelsApiRest.api.core.service.ICreditCardService/creditCardInsert');
		INSERT INTO tserver_permission VALUES (35,'com.ontimize.atomicHotelsApiRest.api.core.service.ICreditCardService/creditCardDelete');
		
		-- customerCreditCard
		INSERT INTO tserver_permission VALUES (36,'com.ontimize.atomicHotelsApiRest.api.core.service.ICustomerCreditCardService/customerCreditCardQuery');
		INSERT INTO tserver_permission VALUES (37,'com.ontimize.atomicHotelsApiRest.api.core.service.ICustomerCreditCardService/customerCreditCardInsert');
		INSERT INTO tserver_permission VALUES (38,'com.ontimize.atomicHotelsApiRest.api.core.service.ICustomerCreditCardService/customerCreditCardDelete');
		
		-- customer
		INSERT INTO tserver_permission VALUES (39,'com.ontimize.atomicHotelsApiRest.api.core.service.ICustomerService/customerQuery');
		INSERT INTO tserver_permission VALUES (40,'com.ontimize.atomicHotelsApiRest.api.core.service.ICustomerService/mailAgreementQuery');
		INSERT INTO tserver_permission VALUES (41,'com.ontimize.atomicHotelsApiRest.api.core.service.ICustomerService/isCustomerValidBookingHolder');
		INSERT INTO tserver_permission VALUES (42,'com.ontimize.atomicHotelsApiRest.api.core.service.ICustomerService/customerDelete');
		INSERT INTO tserver_permission VALUES (43,'com.ontimize.atomicHotelsApiRest.api.core.service.ICustomerService/businessCustomerInsert');
		INSERT INTO tserver_permission VALUES (44,'com.ontimize.atomicHotelsApiRest.api.core.service.ICustomerService/regularCustomerInsert');
		INSERT INTO tserver_permission VALUES (45,'com.ontimize.atomicHotelsApiRest.api.core.service.ICustomerService/customerCancelUpdate');
		INSERT INTO tserver_permission VALUES (46,'com.ontimize.atomicHotelsApiRest.api.core.service.ICustomerService/customerBusinessUpdate');
		INSERT INTO tserver_permission VALUES (47,'com.ontimize.atomicHotelsApiRest.api.core.service.ICustomerService/customerRegularUpdate');
		
		-- feature
		INSERT INTO tserver_permission VALUES (48,'com.ontimize.atomicHotelsApiRest.api.core.service.IFeatureService/featureQuery');
		INSERT INTO tserver_permission VALUES (49,'com.ontimize.atomicHotelsApiRest.api.core.service.IFeatureService/featureInsert');
		INSERT INTO tserver_permission VALUES (50,'com.ontimize.atomicHotelsApiRest.api.core.service.IFeatureService/featureUpdate');
		INSERT INTO tserver_permission VALUES (51,'com.ontimize.atomicHotelsApiRest.api.core.service.IFeatureService/featureDelete');
		
		-- hotelServiceExtra
		INSERT INTO tserver_permission VALUES (52,'com.ontimize.atomicHotelsApiRest.api.core.service.IHotelServiceExtraService/hotelServiceExtraQuery');
		INSERT INTO tserver_permission VALUES (53,'com.ontimize.atomicHotelsApiRest.api.core.service.IHotelServiceExtraService/hotelServiceExtraInsert');
		INSERT INTO tserver_permission VALUES (54,'com.ontimize.atomicHotelsApiRest.api.core.service.IHotelServiceExtraService/hotelServiceExtraUpdate');
		INSERT INTO tserver_permission VALUES (55,'com.ontimize.atomicHotelsApiRest.api.core.service.IHotelServiceExtraService/hotelServiceExtraDelete');
		
		-- hotelService
		INSERT INTO tserver_permission VALUES (56,'com.ontimize.atomicHotelsApiRest.api.core.service.IHotelServiceService/hotelServiceQuery');
		INSERT INTO tserver_permission VALUES (57,'com.ontimize.atomicHotelsApiRest.api.core.service.IHotelServiceService/hotelServiceInsert');
		INSERT INTO tserver_permission VALUES (58,'com.ontimize.atomicHotelsApiRest.api.core.service.IHotelServiceService/hotelServiceUpdate');
		INSERT INTO tserver_permission VALUES (59,'com.ontimize.atomicHotelsApiRest.api.core.service.IHotelServiceService/hotelServiceDelete');
		
		-- receipt
		INSERT INTO tserver_permission VALUES (60,'com.ontimize.atomicHotelsApiRest.api.core.service.IReceiptService/receiptQuery');
		INSERT INTO tserver_permission VALUES (61,'com.ontimize.atomicHotelsApiRest.api.core.service.IReceiptService/receiptInsert');
		INSERT INTO tserver_permission VALUES (62,'com.ontimize.atomicHotelsApiRest.api.core.service.IReceiptService/completeReceiptQuery');
		INSERT INTO tserver_permission VALUES (63,'com.ontimize.atomicHotelsApiRest.api.core.service.IReceiptService/receiptDelete');
		
		-- room
		INSERT INTO tserver_permission VALUES (64,'com.ontimize.atomicHotelsApiRest.api.core.service.IRoomService/roomQuery');
		INSERT INTO tserver_permission VALUES (65,'com.ontimize.atomicHotelsApiRest.api.core.service.IRoomService/roomInsert');
		INSERT INTO tserver_permission VALUES (66,'com.ontimize.atomicHotelsApiRest.api.core.service.IRoomService/roomUpdate');
		INSERT INTO tserver_permission VALUES (67,'com.ontimize.atomicHotelsApiRest.api.core.service.IRoomService/roomDelete');
		INSERT INTO tserver_permission VALUES (68,'com.ontimize.atomicHotelsApiRest.api.core.service.IRoomService/roomsUnbookedInRangeQuery');
		INSERT INTO tserver_permission VALUES (69,'com.ontimize.atomicHotelsApiRest.api.core.service.IRoomService/isRoomUnbookedgInRange');
		INSERT INTO tserver_permission VALUES (70,'com.ontimize.atomicHotelsApiRest.api.core.service.IRoomService/roomInfoQuery');
		INSERT INTO tserver_permission VALUES (71,'com.ontimize.atomicHotelsApiRest.api.core.service.IRoomService/infoHotelFeaturesQuery');
		
		-- roomTypeFeature
		INSERT INTO tserver_permission VALUES (72,'com.ontimize.atomicHotelsApiRest.api.core.service.IRoomTypeFeatureService/roomTypeFeatureQuery');
		INSERT INTO tserver_permission VALUES (73,'com.ontimize.atomicHotelsApiRest.api.core.service.IRoomTypeFeatureService/roomTypeFeatureInsert');
		INSERT INTO tserver_permission VALUES (74,'com.ontimize.atomicHotelsApiRest.api.core.service.IRoomTypeFeatureService/roomTypeFeatureDelete');
		
		-- roomType
		INSERT INTO tserver_permission VALUES (75,'com.ontimize.atomicHotelsApiRest.api.core.service.IRoomTypeService/roomTypeQuery');
		INSERT INTO tserver_permission VALUES (76,'com.ontimize.atomicHotelsApiRest.api.core.service.IRoomTypeService/roomTypeInsert');
		INSERT INTO tserver_permission VALUES (77,'com.ontimize.atomicHotelsApiRest.api.core.service.IRoomTypeService/roomTypeUpdate');
		INSERT INTO tserver_permission VALUES (78,'com.ontimize.atomicHotelsApiRest.api.core.service.IRoomTypeService/roomTypeDelete');
		INSERT INTO tserver_permission VALUES (79,'com.ontimize.atomicHotelsApiRest.api.core.service.IRoomTypeService/infoQuery');
		INSERT INTO tserver_permission VALUES (80,'com.ontimize.atomicHotelsApiRest.api.core.service.IRoomTypeService/infoRoomFeaturesQuery');
		
		-- service
		INSERT INTO tserver_permission VALUES (81,'com.ontimize.atomicHotelsApiRest.api.core.service.IServiceService/serviceQuery');
		INSERT INTO tserver_permission VALUES (82,'com.ontimize.atomicHotelsApiRest.api.core.service.IServiceService/serviceInsert');
		INSERT INTO tserver_permission VALUES (83,'com.ontimize.atomicHotelsApiRest.api.core.service.IServiceService/serviceUpdate');
		INSERT INTO tserver_permission VALUES (84,'com.ontimize.atomicHotelsApiRest.api.core.service.IServiceService/serviceDelete');
		
		-- servicesXtra
		INSERT INTO tserver_permission VALUES (85,'com.ontimize.atomicHotelsApiRest.api.core.service.IServicesXtraService/servicesXtraQuery');
		INSERT INTO tserver_permission VALUES (86,'com.ontimize.atomicHotelsApiRest.api.core.service.IServicesXtraService/servicesXtraInsert');
		INSERT INTO tserver_permission VALUES (87,'com.ontimize.atomicHotelsApiRest.api.core.service.IServicesXtraService/servicesXtraUpdate');
		INSERT INTO tserver_permission VALUES (88,'com.ontimize.atomicHotelsApiRest.api.core.service.IServicesXtraService/servicesXtraDelete');
		
		-- user
		INSERT INTO tserver_permission VALUES (89,'com.ontimize.atomicHotelsApiRest.api.core.service.IUserService/userQuery');
		INSERT INTO tserver_permission VALUES (90,'com.ontimize.atomicHotelsApiRest.api.core.service.IUserService/userInsert');
		INSERT INTO tserver_permission VALUES (91,'com.ontimize.atomicHotelsApiRest.api.core.service.IUserService/userUpdate');
		INSERT INTO tserver_permission VALUES (92,'com.ontimize.atomicHotelsApiRest.api.core.service.IUserService/userDelete');
		
		-- department
	-- department
		INSERT INTO tserver_permission VALUES (93,'com.ontimize.atomicHotelsApiRest.api.core.service.IDepartmentService/departmentQuery');
		INSERT INTO tserver_permission VALUES (94,'com.ontimize.atomicHotelsApiRest.api.core.service.IDepartmentService/departmentInsert');
		INSERT INTO tserver_permission VALUES (95,'com.ontimize.atomicHotelsApiRest.api.core.service.IDepartmentService/departmentUpdate');
		INSERT INTO tserver_permission VALUES (96,'com.ontimize.atomicHotelsApiRest.api.core.service.IDepartmentService/departmentDelete');
			
		-- employee
		INSERT INTO tserver_permission VALUES (97,'com.ontimize.atomicHotelsApiRest.api.core.service.IEmployeeService/employeeQuery');
		INSERT INTO tserver_permission VALUES (98,'com.ontimize.atomicHotelsApiRest.api.core.service.IEmployeeService/employeeInsert');
		INSERT INTO tserver_permission VALUES (99,'com.ontimize.atomicHotelsApiRest.api.core.service.IEmployeeService/employeeUpdate');
		INSERT INTO tserver_permission VALUES (100,'com.ontimize.atomicHotelsApiRest.api.core.service.IEmployeeService/employeeFiredUpdate');
	

		-- bill
		INSERT INTO tserver_permission VALUES (101,'com.ontimize.atomicHotelsApiRest.api.core.service.IBillService/billQuery');
		INSERT INTO tserver_permission VALUES (102,'com.ontimize.atomicHotelsApiRest.api.core.service.IBillService/billInsert');
		INSERT INTO tserver_permission VALUES (103,'com.ontimize.atomicHotelsApiRest.api.core.service.IBillService/billUpdate');
		INSERT INTO tserver_permission VALUES (104,'com.ontimize.atomicHotelsApiRest.api.core.service.IBillService/billDelete');
		INSERT INTO tserver_permission VALUES (105,'com.ontimize.atomicHotelsApiRest.api.core.service.IBillService/gastosDepartamentoQuery');
		INSERT INTO tserver_permission VALUES (106,'com.ontimize.atomicHotelsApiRest.api.core.service.IBillService/gastosDepartamentoHotelQuery');
		INSERT INTO tserver_permission VALUES (107,'com.ontimize.atomicHotelsApiRest.api.core.service.IBillService/billsByHotelDepartmentQuery');
		
		-- userRole
		INSERT INTO tserver_permission VALUES (108,'com.ontimize.atomicHotelsApiRest.api.core.service.IUserRoleService/userRoleQuery');
		INSERT INTO tserver_permission VALUES (109,'com.ontimize.atomicHotelsApiRest.api.core.service.IUserRoleService/userRoleInsert');
		INSERT INTO tserver_permission VALUES (110,'com.ontimize.atomicHotelsApiRest.api.core.service.IUserRoleService/userRoleDelete');	
	
		-- statistics
		INSERT INTO tserver_permission VALUES (113,'com.ontimize.atomicHotelsApiRest.api.core.service.IStatisticsService/hotelMaximumCapacityQuery');
		INSERT INTO tserver_permission VALUES (114,'com.ontimize.atomicHotelsApiRest.api.core.service.IStatisticsService/hotelOccupancyPercentageQuery');
		INSERT INTO tserver_permission VALUES (115,'com.ontimize.atomicHotelsApiRest.api.core.service.IStatisticsService/hotelCapacityInDateRangeQuery');
		INSERT INTO tserver_permission VALUES (116,'com.ontimize.atomicHotelsApiRest.api.core.service.IStatisticsService/hotelOccupancyByNationalityPercentageQuery');
		INSERT INTO tserver_permission VALUES (117,'com.ontimize.atomicHotelsApiRest.api.core.service.IStatisticsService/departmentExpensesByHotelQuery');
		INSERT INTO tserver_permission VALUES (118,'com.ontimize.atomicHotelsApiRest.api.core.service.IStatisticsService/roomsIncomeByHotelQuery');
		INSERT INTO tserver_permission VALUES (119,'com.ontimize.atomicHotelsApiRest.api.core.service.IStatisticsService/servicesExtraIncomeByHotelQuery');
		INSERT INTO tserver_permission VALUES (120,'com.ontimize.atomicHotelsApiRest.api.core.service.IStatisticsService/incomeVsExpensesByHotelQuery');

		-- picture
		-- employeePhoto
		INSERT INTO tserver_permission VALUES (121,'com.ontimize.atomicHotelsApiRest.api.core.service.IEmployeePhotoService/employeePhotoQuery');
		INSERT INTO tserver_permission VALUES (122,'com.ontimize.atomicHotelsApiRest.api.core.service.IEmployeePhotoService/employeePhotoInsert');
		INSERT INTO tserver_permission VALUES (123,'com.ontimize.atomicHotelsApiRest.api.core.service.IEmployeePhotoService/employeePhotoDelete');
		INSERT INTO tserver_permission VALUES (124,'com.ontimize.atomicHotelsApiRest.api.core.service.IEmployeePhotoService/getPicture');
																									
		-- question
		INSERT INTO tserver_permission VALUES (125,'com.ontimize.atomicHotelsApiRest.api.core.service.IQuestionService/questionQuery');
		INSERT INTO tserver_permission VALUES (126,'com.ontimize.atomicHotelsApiRest.api.core.service.IQuestionService/questionDelete');
		INSERT INTO tserver_permission VALUES (127,'com.ontimize.atomicHotelsApiRest.api.core.service.IQuestionService/questionUpdate');
		INSERT INTO tserver_permission VALUES (128,'com.ontimize.atomicHotelsApiRest.api.core.service.IQuestionService/questionInsert');
		INSERT INTO tserver_permission VALUES (129,'com.ontimize.atomicHotelsApiRest.api.core.service.IQuestionService/questionPublicQuery');

		-- answer
		INSERT INTO tserver_permission VALUES (130,'com.ontimize.atomicHotelsApiRest.api.core.service.IAnswerService/answerQuery');
		INSERT INTO tserver_permission VALUES (131,'com.ontimize.atomicHotelsApiRest.api.core.service.IAnswerService/answerDelete');
		INSERT INTO tserver_permission VALUES (132,'com.ontimize.atomicHotelsApiRest.api.core.service.IAnswerService/answerUpdate');
		INSERT INTO tserver_permission VALUES (133,'com.ontimize.atomicHotelsApiRest.api.core.service.IAnswerService/answerInsert');
		INSERT INTO tserver_permission VALUES (134,'com.ontimize.atomicHotelsApiRest.api.core.service.IAnswerService/answerPublicQuery');

		-- hotelPhoto
		INSERT INTO tserver_permission VALUES (135,'com.ontimize.atomicHotelsApiRest.api.core.service.IHotelPhotoService/hotelPhotoQuery');
		INSERT INTO tserver_permission VALUES (136,'com.ontimize.atomicHotelsApiRest.api.core.service.IHotelPhotoService/hotelPhotoInsert');
		INSERT INTO tserver_permission VALUES (137,'com.ontimize.atomicHotelsApiRest.api.core.service.IHotelPhotoService/hotelPhotoDelete');
		INSERT INTO tserver_permission VALUES (138,'com.ontimize.atomicHotelsApiRest.api.core.service.IHotelPhotoService/getHotelPictureQuery');

		-- report
		INSERT INTO tserver_permission VALUES (139,'com.ontimize.atomicHotelsApiRest.api.core.service.IReportService/hotels');
		INSERT INTO tserver_permission VALUES (140,'com.ontimize.atomicHotelsApiRest.api.core.service.IReportService/employeePieCostByDepartament');
		INSERT INTO tserver_permission VALUES (141,'com.ontimize.atomicHotelsApiRest.api.core.service.IReportService/incomeVsExpensesChart');
		INSERT INTO tserver_permission VALUES (142,'com.ontimize.atomicHotelsApiRest.api.core.service.IReportService/receipt');
		INSERT INTO tserver_permission VALUES (143,'com.ontimize.atomicHotelsApiRest.api.core.service.IReportService/occupancyChart');
		INSERT INTO tserver_permission VALUES (144,'com.ontimize.atomicHotelsApiRest.api.core.service.IReportService/occupancyByNationalityChart');
		INSERT INTO tserver_permission VALUES (145,'com.ontimize.atomicHotelsApiRest.api.core.service.IReportService/listAllEmployeeReport');
		INSERT INTO tserver_permission VALUES (146,'com.ontimize.atomicHotelsApiRest.api.core.service.IReportService/employeesByHotel');
		INSERT INTO tserver_permission VALUES (147,'com.ontimize.atomicHotelsApiRest.api.core.service.IReportService/departmentExpensesByHotelChart');
	


		--varios
		INSERT INTO tserver_permission VALUES (111,'com.ontimize.atomicHotelsApiRest.api.core.service.IHotelService/poiQuery');
		INSERT INTO tserver_permission VALUES (112,'com.ontimize.atomicHotelsApiRest.api.core.service.IUserService/userCancelUpdate');
	
	
-- modificaciones
	UPDATE tserver_permission SET permission_name = 'com.ontimize.atomicHotelsApiRest.api.core.service.IHotelPhotoService/hotelPhotoQuery' WHERE id_server_permission = 135;
	UPDATE tserver_permission SET permission_name = 'com.ontimize.atomicHotelsApiRest.api.core.service.IHotelPhotoService/hotelPhotoInsert' WHERE id_server_permission = 136;
	UPDATE tserver_permission SET permission_name = 'com.ontimize.atomicHotelsApiRest.api.core.service.IHotelPhotoService/hotelPhotoDelete' WHERE id_server_permission = 137;
	UPDATE tserver_permission SET permission_name = 'com.ontimize.atomicHotelsApiRest.api.core.service.IHotelPhotoService/getHotelPictureQuery' WHERE id_server_permission = 138;
	

	UPDATE tserver_permission SET permission_name = '' WHERE id_server_permission = 125;
	
--ROLES
	INSERT INTO trole (id_rolename,rolename,xmlclientpermission) VALUES (1,'ceo','<?xml version="1.0" encoding="UTF-8"?><security></security>');
	INSERT INTO trole (id_rolename,rolename,xmlclientpermission) VALUES (2,'hotelManager','<?xml version="1.0" encoding="UTF-8"?><security></security>');
	INSERT INTO trole (id_rolename,rolename,xmlclientpermission) VALUES (3,'staff','<?xml version="1.0" encoding="UTF-8"?><security></security>');
	INSERT INTO trole (id_rolename,rolename,xmlclientpermission) VALUES (4,'customer','<?xml version="1.0" encoding="UTF-8"?><security></security>');
	INSERT INTO trole (id_rolename,rolename,xmlclientpermission) VALUES (5,'user','<?xml version="1.0" encoding="UTF-8"?><security></security>');
	

--USUARIOS		
ALTER TABLE public.tuser ADD htl_restriction integer NULL;
ALTER TABLE public.tuser ADD FOREIGN KEY(htl_restriction) REFERENCES hotels(htl_id)	;


	INSERT INTO tuser (user_ ,"password","name") VALUES ('admin','123456','Administrador del sistema');

	INSERT INTO tuser (user_ ,"password","name",surname) VALUES ('atom','123456','Mr Atom','Rodriguez');
	INSERT INTO tuser (user_ ,"password","name",htl_restriction) VALUES ('gerenteAtom01','123456','Gerente del Hotel Atom 1',1);
	INSERT INTO tuser (user_ ,"password","name",htl_restriction) VALUES ('gerenteAtom02','123456','Gerente del Hotel Atom 1',2);
	INSERT INTO tuser (user_ ,"password","name",htl_restriction) VALUES ('personalAtom01','123456','Personal de recepción de atom 01',1);
	INSERT INTO tuser (user_ ,"password","name",htl_restriction) VALUES ('personalAtom02','123456','Personal de recepción de atom 02',2);
	INSERT INTO tuser (user_ ,"password","name") VALUES ('turisticas','123456','Reservas turisticas europeas sl.');
	INSERT INTO tuser (user_ ,"password","name") VALUES ('usuarioLibre','123456','usuario basico ');
	

--ROLES/PERMISOS
ALTER TABLE trole_server_permission ADD UNIQUE(id_rolename,id_server_permission);
ALTER TABLE tuser_role ADD UNIQUE(id_rolename,user_);

TRUNCATE TABLE trole_server_permission;
ALTER SEQUENCE trole_server_permission_id_role_server_permission_seq RESTART WITH 1;
	--admin
		INSERT INTO trole_server_permission (id_rolename,id_server_permission) SELECT 0,id_server_permission FROM tserver_permission WHERE id_server_permission >= 1 AND id_server_permission <= 9999999;
	--ceo 1
	INSERT INTO trole_server_permission (id_rolename,id_server_permission) SELECT 1,id_server_permission FROM tserver_permission WHERE id_server_permission >= 1 AND id_server_permission <= 9999999;
	--INSERT INTO trole_server_permission (id_rolename,id_server_permission) VALUES (1,112);
	--DELETE FROM trole_server_permission WHERE id_rolename = 1


	--hotelManager 2		
		INSERT INTO trole_server_permission (id_rolename,id_server_permission) SELECT 2,id_server_permission 
			FROM tserver_permission WHERE id_server_permission >= 1 AND id_server_permission <= 138;
		DELETE FROM trole_server_permission 
			WHERE ( id_server_permission in(2,4,6,7,8,49,50,51,73,74,76,77,78,82,83,84,94,95,96,108,109,110,112) 
					OR id_server_permission >= 86 AND id_server_permission <= 92
			) AND ID_ROLENAME = 2;	
		INSERT INTO trole_server_permission (id_rolename,id_server_permission) VALUES (2,140);
		INSERT INTO trole_server_permission (id_rolename,id_server_permission) VALUES (2,142);
		INSERT INTO trole_server_permission (id_rolename,id_server_permission) VALUES (2,144);
		INSERT INTO trole_server_permission (id_rolename,id_server_permission) VALUES (2,146);
		INSERT INTO trole_server_permission (id_rolename,id_server_permission) VALUES (2,147);
		
		
		--DELETE FROM trole_server_permission WHERE id_rolename = 2

	--staff 3
		INSERT INTO trole_server_permission (id_rolename,id_server_permission) SELECT 3,id_server_permission FROM tserver_permission WHERE id_server_permission >= 1 AND id_server_permission <= 93	;
	INSERT INTO trole_server_permission (id_rolename,id_server_permission) SELECT 3,id_server_permission FROM tserver_permission WHERE id_server_permission >= 125 AND id_server_permission <= 134	;
		DELETE FROM trole_server_permission
			WHERE ( id_server_permission in(2,3,4,6,7,8,49,50,51,53,55,57,58,59,65,66,67,73,74,76,77,78,82,83,84) 
					OR id_server_permission >= 86 AND id_server_permission <= 92
			) AND ID_ROLENAME = 3;
		INSERT INTO trole_server_permission (id_rolename,id_server_permission) VALUES (3,111);
		INSERT INTO trole_server_permission (id_rolename,id_server_permission) VALUES (3,142);

	
	
	
	--customer 4	
		INSERT INTO trole_server_permission (id_rolename,id_server_permission) SELECT 4,id_server_permission FROM tserver_permission WHERE id_server_permission >= 1 AND id_server_permission <= 85	;	
		DELETE FROM trole_server_permission 
			WHERE ( id_server_permission in(2,3,4,6,7,8,9,10,11,12,13,16,17,23,25,27,28,29,30,31,42,45,49,50,51,53,54,55,57,58,59,60,61,62,63,65,66,67,73,74,76,77,78,82,83,84) 
			) AND ID_ROLENAME = 4;
		
		INSERT INTO trole_server_permission (id_rolename,id_server_permission) VALUES (4,111);
		INSERT INTO trole_server_permission (id_rolename,id_server_permission) VALUES (4,128);
		INSERT INTO trole_server_permission (id_rolename,id_server_permission) VALUES (4,129);
		INSERT INTO trole_server_permission (id_rolename,id_server_permission) VALUES (4,134);
	
	--user 5 - información pública	
		INSERT INTO trole_server_permission (id_rolename,id_server_permission) VALUES (5,1);
		INSERT INTO trole_server_permission (id_rolename,id_server_permission) VALUES (5,111);
		INSERT INTO trole_server_permission (id_rolename,id_server_permission) VALUES (5,128);
		INSERT INTO trole_server_permission (id_rolename,id_server_permission) VALUES (5,129);
		INSERT INTO trole_server_permission (id_rolename,id_server_permission) VALUES (5,134);


	
	
		--INSERT INTO trole_server_permission (id_rolename,id_server_permission) SELECT 2,id_server_permission FROM tserver_permission WHERE id_server_permission in(3,5)	
		--	OR id_server_permission >= 9 AND id_server_permission <= 16
		-- INSERT INTO trole_server_permission (id_rolename,id_server_permission) SELECT 3,id_server_permission FROM tserver_permission WHERE id_server_permission in(5)	
		--	OR id_server_permission >= 9 AND id_server_permission <= 16
		--INSERT INTO trole_server_permission (id_rolename,id_server_permission) SELECT 4,id_server_permission FROM tserver_permission WHERE id_server_permission in(5)	
		--	OR id_server_permission >= 9 AND id_server_permission <= 15
		--	OR id_server_permission >= 18 AND id_server_permission <= 20
--ROLES/USUARIOS

	--admin 
	INSERT INTO tuser_role (id_rolename,user_) VALUES (0,'admin');

	--ceo 1
	INSERT INTO tuser_role (id_rolename,user_) VALUES (1,'atom');
	--hotelManager 2
	INSERT INTO tuser_role (id_rolename,user_) VALUES (2,'gerenteAtom01');
	INSERT INTO tuser_role (id_rolename,user_) VALUES (2,'gerenteAtom02');
	--staff 3
	INSERT INTO tuser_role (id_rolename,user_) VALUES (3,'personalAtom01');
	INSERT INTO tuser_role (id_rolename,user_) VALUES (3,'personalAtom02');
	--customer 4
	INSERT INTO tuser_role (id_rolename,user_) VALUES (4,'turisticas');
	--user 5
	INSERT INTO tuser_role (id_rolename,user_) VALUES (5,'usuarioLibre');

--MODIFICACIONES EN TABLAS 
ALTER TABLE public.bookings ADD user_ varchar(50) NULL;
ALTER TABLE public.bookings ADD FOREIGN KEY(user_) REFERENCES tuser(user_)	;

ALTER TABLE public.customers ADD cst_user varchar(50) NULL;
ALTER TABLE public.customers ADD FOREIGN KEY(cst_user) REFERENCES tuser(user_)	;

--ALTER TABLE public.customers RENAME COLUMN user_ TO cst_user;




