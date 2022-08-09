--HOTELS
INSERT INTO public.hotels (htl_name, htl_street, htl_city, htl_postal_code, htl_state, htl_cnt_iso, htl_phone, htl_email, htl_description, htl_is_open) VALUES ('Atomic 01', 'Avenida Gran V�a 102', 'Vigo',36211, 'Galicia', 'Spain','+34 986 111 111','atomic01@hotlesatomic.com','Nuevo hotel de lujo.',1);
INSERT INTO public.hotels (htl_name, htl_street, htl_city, htl_postal_code, htl_state, htl_cnt_iso, htl_phone, htl_email, htl_description, htl_is_open) VALUES ('Atomic 02', 'Avenida Linares Rivas ', 'A Coru�a',15005, 'Galicia', 'Spain','+34 981 222 222','atomic02@hotlesatomic.com','Nuevo hotel de lujo.',1);
INSERT INTO public.hotels (htl_name, htl_street, htl_city, htl_postal_code, htl_state, htl_cnt_iso, htl_phone, htl_email, htl_description, htl_is_open) VALUES ('Atomic 03', 'Avenida Ram�n Canosa ', 'Viveiro',27869, 'Galicia', 'Spain','+34 982 333 333','atomic03@hotlesatomic.com','Nuevo hotel de lujo.',1);
INSERT INTO public.hotels (htl_name, htl_street, htl_city, htl_postal_code, htl_state, htl_cnt_iso, htl_phone, htl_email, htl_description, htl_is_open) VALUES ('Atomic 04', 'Paseo de la Catellana 23', 'Madrid',28046, 'Madrid', 'Spain','+34 81 444 444','atomic04@hotlesatomic.com','Nuevo hotel de lujo.',1);
INSERT INTO public.hotels (htl_name, htl_street, htl_city, htl_postal_code, htl_state, htl_cnt_iso, htl_phone, htl_email, htl_description, htl_is_open) VALUES ('Atomic 05', 'La Rambla 9', 'Barcelona',08002, 'Barcelona', 'Spain','+34 986 555 555','atomic05@hotlesatomic.com','Nuevo hotel de lujo.',1);
INSERT INTO public.hotels (htl_name, htl_street, htl_city, htl_postal_code, htl_state, htl_cnt_iso, htl_phone, htl_email, htl_description, htl_is_open) VALUES ('Atomic 06', 'St James 13', 'London',1111, 'London', 'UK','+44 666 666 666','atomic06@hotlesatomic.com','Nuevo hotel de lujo.',1);
INSERT INTO public.hotels (htl_name, htl_street, htl_city, htl_postal_code, htl_state, htl_cnt_iso, htl_phone, htl_email, htl_description, htl_is_open) VALUES ('Atomic 07', 'Temple Bar 23', 'Dublin',22222, 'Dublin', 'Ireland','+353 777 777 777','atomic07@hotlesatomic.com','Nuevo hotel de lujo.',1);
INSERT INTO public.hotels (htl_name, htl_street, htl_city, htl_postal_code, htl_state, htl_cnt_iso, htl_phone, htl_email, htl_description, htl_is_open) VALUES ('Atomic 08', 'La Fayette 25', 'Paris',33333, 'Paris', 'France','+33 888 888 888','atomic08@hotlesatomic.com','Nuevo hotel de lujo.',0);
INSERT INTO public.hotels (htl_name, htl_street, htl_city, htl_postal_code, htl_state, htl_cnt_iso, htl_phone, htl_email, htl_description, htl_is_open) VALUES ('Atomic 09', 'Avenida de Berna 26', 'Lisbon',44444, 'Lisbon', 'Portugal','+351 999 999 999','atomic09@hotlesatomic.com','Nuevo hotel de lujo.',0);
INSERT INTO public.hotels (htl_name, htl_street, htl_city, htl_postal_code, htl_state, htl_cnt_iso, htl_phone, htl_email, htl_description, htl_is_open) VALUES ('Atomic 10', 'W Houston 23', 'New York City',55555, 'New York', 'EEUU','+1 101 101 010','atomic10@hotlesatomic.com','Nuevo hotel de lujo.',0);
INSERT INTO public.hotels (htl_name, htl_street, htl_city, htl_postal_code, htl_state, htl_cnt_iso, htl_phone, htl_email, htl_description, htl_is_open) VALUES ('Atomic 11', 'Kokubunji-Kaido 133', 'Tokyo',66666, 'Kant?', 'Japan','+03 111 111 111','atomic11@hotlesatomic.com','Nuevo hotel de lujo.',0);
INSERT INTO public.hotels (htl_name, htl_street, htl_city, htl_postal_code, htl_state, htl_cnt_iso, htl_phone, htl_email, htl_description, htl_is_open) VALUES ('Atomic 12', 'Park St 24', 'Sydney',36211, 'New South Wales', 'Australia','+61 121 121 212','atomic12@hotlesatomic.com','Nuevo hotel de lujo.',0);
INSERT INTO public.hotels (htl_name, htl_street, htl_city, htl_postal_code, htl_state, htl_cnt_iso, htl_phone, htl_email, htl_description, htl_is_open) VALUES ('Atomic 13', 'Avenida Brasil 202', 'Buenos Aires',36211, 'Buenos Aires', 'Argentina','`+54 131 131 313','atomic13hotlesatomic.com','Nuevo hotel de lujo.',0);

--BEDS_COMBO
INSERT INTO public.beds_combo (bdc_name, bdc_slots) VALUES ('Cama individual',1);
INSERT INTO public.beds_combo (bdc_name, bdc_slots) VALUES ('Dos camas individuales',2);
INSERT INTO public.beds_combo (bdc_name, bdc_slots) VALUES ('Tres camas individuales',3);
INSERT INTO public.beds_combo (bdc_name, bdc_slots) VALUES ('Cama doble',2);
INSERT INTO public.beds_combo (bdc_name, bdc_slots) VALUES ('Cama individual y doble',3);
INSERT INTO public.beds_combo (bdc_name, bdc_slots) VALUES ('Dos camas individuales y una doble',4);
INSERT INTO public.beds_combo (bdc_name, bdc_slots) VALUES ('Dos camas dobles',4);
INSERT INTO public.beds_combo (bdc_name, bdc_slots) VALUES ('Cama King-size',2);
INSERT INTO public.beds_combo (bdc_name, bdc_slots) VALUES ('Cama individual y cama King-size',3);
INSERT INTO public.beds_combo (bdc_name, bdc_slots) VALUES ('Dos camas individuales y una King-size',4);

--STATUS_BOOKING
/*
INSERT INTO public.status_booking (stb_name) VALUES ('confirmada');
INSERT INTO public.status_booking (stb_name) VALUES ('pendiente');
INSERT INTO public.status_booking (stb_name) VALUES ('cancelada');
INSERT INTO public.status_booking (stb_name) VALUES ('completada');
*/

--ROOM_TYPES
INSERT INTO public.room_types (rmt_name,rmt_description,rmt_price,rmt_bdc_id) VALUES('Habitaci�n econ�mica simple','Habitaci�n b�sica individual',30.4,1);
INSERT INTO public.room_types (rmt_name,rmt_description,rmt_price,rmt_bdc_id) VALUES('Habitaci�n econ�mica doble','Habitaci�n b�sica doble con camas individules',40.5,2);
INSERT INTO public.room_types (rmt_name,rmt_description,rmt_price,rmt_bdc_id) VALUES('Habitaci�n econ�mica triple','Habitaci�n b�sica triple con camas individulaes',80.8,3);
INSERT INTO public.room_types (rmt_name,rmt_description,rmt_price,rmt_bdc_id) VALUES('Habitaci�n econ�mica doble superior','Habitaci�n b�sica doble con cama doble',75.8,4);
INSERT INTO public.room_types (rmt_name,rmt_description,rmt_price,rmt_bdc_id) VALUES('Habitaci�n econ�mica triple superior','Habitaci�n b�sica triple con cama doble y cama individual',90.7,5);
INSERT INTO public.room_types (rmt_name,rmt_description,rmt_price,rmt_bdc_id) VALUES('Habitaci�n cuadriple','Habitaci�n con dos camas dobles',120,6);
INSERT INTO public.room_types (rmt_name,rmt_description,rmt_price,rmt_bdc_id) VALUES('Habitaci�n boble','Habitaci�n doble king-size',60,7);
INSERT INTO public.room_types (rmt_name,rmt_description,rmt_price,rmt_bdc_id) VALUES('Habitaci�n triple','Habitaci�n triple king-size',110,8);
INSERT INTO public.room_types (rmt_name,rmt_description,rmt_price,rmt_bdc_id) VALUES('Habitaci�n cuadruple','Habitaci�n cuadruple king-size',150,9);

--ROOMS
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(1,101,10,1,1);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(1,102,10,1,1);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(1,103,13,2,1);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(1,104,13,2,1);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(1,105,18,3,1);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(1,106,18,3,1);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(1,107,20,4,1);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(1,108,20,4,1);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(1,201,20,4,1);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(1,202,20,4,1);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(1,203,22,5,1);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(1,204,22,5,1);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(1,205,24,6,1);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(1,206,24,6,1);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(1,207,20,7,1);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(1,208,20,7,1);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(1,301,20,7,1);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(1,302,20,7,1);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(1,303,20,7,1);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(1,304,20,7,1);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(1,305,20,7,1);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(1,306,20,7,1);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(1,307,20,7,1);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(1,308,20,7,1);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(1,401,25,8,1);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(1,402,25,8,1);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(1,403,25,8,1);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(1,404,25,8,1);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(1,405,30,9,1);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(1,406,30,9,1);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(1,407,30,9,1);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(1,408,30,9,1);

INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(2,101,10,1,1);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(2,102,10,1,1);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(2,103,13,2,1);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(2,104,13,2,1);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(2,105,18,3,1);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(2,106,18,3,1);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(2,107,20,4,1);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(2,108,20,4,1);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(2,201,20,4,1);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(2,202,20,4,1);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(2,203,22,5,1);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(2,204,22,5,1);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(2,205,24,6,1);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(2,206,24,6,1);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(2,207,20,7,1);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(2,208,20,7,1);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(2,301,20,7,1);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(2,302,20,7,1);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(2,303,20,7,1);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(2,304,20,7,1);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(2,305,20,7,1);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(2,306,20,7,1);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(2,307,20,7,1);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(2,308,20,7,1);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(2,401,25,8,1);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(2,402,25,8,1);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(2,403,25,8,1);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(2,404,25,8,1);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(2,405,30,9,1);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(2,406,30,9,1);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(2,407,30,9,1);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(2,408,30,9,1);

INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(3,101,10,1,1);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(3,102,10,1,1);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(3,103,13,2,1);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(3,104,13,2,1);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(3,105,18,3,1);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(3,106,18,3,1);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(3,107,20,4,1);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(3,108,20,4,1);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(3,201,20,4,1);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(3,202,20,4,1);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(3,203,22,5,1);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(3,204,22,5,1);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(3,205,24,6,1);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(3,206,24,6,1);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(3,207,20,7,1);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(3,208,20,7,1);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(3,301,20,7,1);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(3,302,20,7,1);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(3,303,20,7,1);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(3,304,20,7,1);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(3,305,20,7,1);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(3,306,20,7,1);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(3,307,20,7,1);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(3,308,20,7,1);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(3,401,25,8,1);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(3,402,25,8,1);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(3,403,25,8,1);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(3,404,25,8,1);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(3,405,30,9,1);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(3,406,30,9,1);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(3,407,30,9,1);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(3,408,30,9,1);

INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(4,101,10,1,1);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(4,102,10,1,1);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(4,103,13,2,1);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(4,104,13,2,1);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(4,105,18,3,1);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(4,106,18,3,1);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(4,107,20,4,1);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(4,108,20,4,1);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(4,201,20,4,1);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(4,202,20,4,1);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(4,203,22,5,1);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(4,204,22,5,1);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(4,205,24,6,1);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(4,206,24,6,1);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(4,207,20,7,1);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(4,208,20,7,1);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(4,301,20,7,1);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(4,302,20,7,1);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(4,303,20,7,1);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(4,304,20,7,1);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(4,305,20,7,1);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(4,306,20,7,1);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(4,307,20,7,1);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(4,308,20,7,1);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(4,401,25,8,1);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(4,402,25,8,1);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(4,403,25,8,1);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(4,404,25,8,1);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(4,405,30,9,1);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(4,406,30,9,1);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(4,407,30,9,1);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(4,408,30,9,1);

INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(5,101,10,1,1);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(5,102,10,1,1);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(5,103,13,2,1);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(5,104,13,2,1);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(5,105,18,3,1);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(5,106,18,3,1);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(5,107,20,4,1);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(5,108,20,4,1);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(5,201,20,4,1);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(5,202,20,4,1);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(5,203,22,5,1);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(5,204,22,5,1);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(5,205,24,6,1);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(5,206,24,6,1);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(5,207,20,7,1);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(5,208,20,7,1);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(5,301,20,7,1);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(5,302,20,7,1);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(5,303,20,7,1);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(5,304,20,7,1);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(5,305,20,7,1);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(5,306,20,7,1);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(5,307,20,7,1);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(5,308,20,7,1);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(5,401,25,8,1);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(5,402,25,8,1);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(5,403,25,8,1);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(5,404,25,8,1);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(5,405,30,9,1);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(5,406,30,9,1);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(5,407,30,9,1);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(5,408,30,9,1);

INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(6,101,10,1,1);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(6,102,10,1,1);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(6,103,13,2,1);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(6,104,13,2,1);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(6,105,18,3,1);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(6,106,18,3,1);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(6,107,20,4,1);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(6,108,20,4,1);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(6,201,20,4,1);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(6,202,20,4,1);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(6,203,22,5,1);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(6,204,22,5,1);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(6,205,24,6,1);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(6,206,24,6,1);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(6,207,20,7,1);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(6,208,20,7,1);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(6,301,20,7,1);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(6,302,20,7,1);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(6,303,20,7,1);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(6,304,20,7,1);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(6,305,20,7,1);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(6,306,20,7,1);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(6,307,20,7,1);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(6,308,20,7,1);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(6,401,25,8,1);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(6,402,25,8,1);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(6,403,25,8,1);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(6,404,25,8,1);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(6,405,30,9,1);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(6,406,30,9,1);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(6,407,30,9,1);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(6,408,30,9,1);

INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(7,101,10,1,1);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(7,102,10,1,1);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(7,103,13,2,1);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(7,104,13,2,1);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(7,105,18,3,1);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(7,106,18,3,1);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(7,107,20,4,1);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(7,108,20,4,1);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(7,201,20,4,1);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(7,202,20,4,1);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(7,203,22,5,1);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(7,204,22,5,1);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(7,205,24,6,1);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(7,206,24,6,1);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(7,207,20,7,1);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(7,208,20,7,1);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(7,301,20,7,0);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(7,302,20,7,0);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(7,303,20,7,0);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(7,304,20,7,0);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(7,305,20,7,0);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(7,306,20,7,0);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(7,307,20,7,0);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(7,308,20,7,0);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(7,401,25,8,0);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(7,402,25,8,0);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(7,403,25,8,0);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(7,404,25,8,0);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(7,405,30,9,0);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(7,406,30,9,0);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(7,407,30,9,0);
INSERT INTO public.rooms (rm_htl_id, rm_number, rm_square_meters,rm_rmt_id,rm_status) values(7,408,30,9,0);

--SERVICES:
INSERT INTO public.services (ftr_name, ftr_description) VALUES ('aceptamos niños', 'Entorno ideal para los más pequeños de la casa.');
INSERT INTO public.services (srv_name, srv_description) VALUES ('Piscina infantil de bolas', 'impermeable, segura, con vigilancia, estancia perfecta para que los ni�os se diviertan');
INSERT INTO public.services (srv_name, srv_description) VALUES ('Piscina infantil de nataci�n', 'Peque�a piscina con poca profundidad ideal para ense�ar a los m�s peque�os a nadar');
INSERT INTO public.services (srv_name, srv_description) VALUES ('Gimnasio', 'Diferentes m�quinas para disfrutar entrenando y ponerse en forma');
INSERT INTO public.services (srv_name, srv_description) VALUES ('Actuaci�n de magia', 'Ideal para parejas mayores');
INSERT INTO public.services (srv_name, srv_description) VALUES ('Monitores de tiempo libre', 'Para ni�os o maridos aburridos');
INSERT INTO public.services (srv_name, srv_description) VALUES ('Zona aquapark', 'Diferentes toboganes y recorridos en "donuts", para la diversi�n de toda la familia');
INSERT INTO public.services (srv_name, srv_description) VALUES ('M�sica en vivo', 'Conciertos de j�venes talentos locales para amenizar una estudpenda velada');
INSERT INTO public.services (srv_name, srv_description) VALUES ('Pistas de p�del', '2 pistas 2vs2, y una 1vs1. Organizaci�n de torneos.');
INSERT INTO public.services (srv_name, srv_description) VALUES ('Pistas de tenis', 'Magn�ficas pistas de hierba y tierra para desestresarte y tonificarte.');
INSERT INTO public.services (srv_name, srv_description) VALUES ('Cancha de baloncesto', 'Pista exterior de baloncesto, que cuenta con las medidas oficiales.');
INSERT INTO public.services (srv_name, srv_description) VALUES ('Pista de futsal', 'Pista cubierta interior para jugar al futbito o al balonmano. Cuenta con las medidas oficiales.');
INSERT INTO public.services (ftr_name, ftr_description) VALUES ('aceptamos mascotas', 'Cuidamos de tus amigos peludos, como si fueran de nuestra familia.');
INSERT INTO public.services (ftr_name, ftr_description) VALUES ('peluquería canina', 'Nos encargamos de ofrecerles los mejores cuidados cuando nos visiten.');
INSERT INTO public.services (ftr_name, ftr_description) VALUES ('circuito canino', 'Para mantener a nuestras mascotas en buena forma y mejorar sus habilidades físicas.');


--HOTEL SERVICES:
INSERT INTO public.hotels_services (htl_id, srv_id) VALUES (1, 1);
INSERT INTO public.hotels_services (htl_id, srv_id) VALUES (1, 2);
INSERT INTO public.hotels_services (htl_id, srv_id) VALUES (1, 3);
INSERT INTO public.hotels_services (htl_id, srv_id) VALUES (1, 4);
INSERT INTO public.hotels_services (htl_id, srv_id) VALUES (1, 5);
INSERT INTO public.hotels_services (htl_id, srv_id) VALUES (2, 1);
INSERT INTO public.hotels_services (htl_id, srv_id) VALUES (2, 2);
INSERT INTO public.hotels_services (htl_id, srv_id) VALUES (2, 3);
INSERT INTO public.hotels_services (htl_id, srv_id) VALUES (3, 1);
INSERT INTO public.hotels_services (htl_id, srv_id) VALUES (3, 2);
INSERT INTO public.hotels_services (htl_id, srv_id) VALUES (3, 4);
INSERT INTO public.hotels_services (htl_id, srv_id) VALUES (3, 7);
INSERT INTO public.hotels_services (htl_id, srv_id) VALUES (3, 9);
INSERT INTO public.hotels_services (htl_id, srv_id) VALUES (4, 8);
INSERT INTO public.hotels_services (htl_id, srv_id) VALUES (4, 9);
INSERT INTO public.hotels_services (htl_id, srv_id) VALUES (4, 11);
INSERT INTO public.hotels_services (htl_id, srv_id) VALUES (4, 10);
INSERT INTO public.hotels_services (htl_id, srv_id) VALUES (5, 6);
INSERT INTO public.hotels_services (htl_id, srv_id) VALUES (5, 7);
INSERT INTO public.hotels_services (htl_id, srv_id) VALUES (5, 8);
INSERT INTO public.hotels_services (htl_id, srv_id) VALUES (5, 9);
INSERT INTO public.hotels_services (htl_id, srv_id) VALUES (6, 3);
INSERT INTO public.hotels_services (htl_id, srv_id) VALUES (6, 6);
INSERT INTO public.hotels_services (htl_id, srv_id) VALUES (6, 7);
INSERT INTO public.hotels_services (htl_id, srv_id) VALUES (7, 8);
INSERT INTO public.hotels_services (htl_id, srv_id) VALUES (7, 9);
INSERT INTO public.hotels_services (htl_id, srv_id) VALUES (7, 10);
INSERT INTO public.hotels_services (htl_id, srv_id) VALUES (7, 11);
INSERT INTO public.hotels_services (htl_id, srv_id) VALUES (8, 1);
INSERT INTO public.hotels_services (htl_id, srv_id) VALUES (8, 3);
INSERT INTO public.hotels_services (htl_id, srv_id) VALUES (8, 4);
INSERT INTO public.hotels_services (htl_id, srv_id) VALUES (8, 8);
INSERT INTO public.hotels_services (htl_id, srv_id) VALUES (9, 1);
INSERT INTO public.hotels_services (htl_id, srv_id) VALUES (9, 4 );
INSERT INTO public.hotels_services (htl_id, srv_id) VALUES (9, 6 );
INSERT INTO public.hotels_services (htl_id, srv_id) VALUES (9, 8 );
INSERT INTO public.hotels_services (htl_id, srv_id) VALUES (9, 9 );
INSERT INTO public.hotels_services (htl_id, srv_id) VALUES (9, 10 );
INSERT INTO public.hotels_services (htl_id, srv_id) VALUES (9, 11 );
INSERT INTO public.hotels_services (htl_id, srv_id) VALUES (10,7);
INSERT INTO public.hotels_services (htl_id, srv_id) VALUES (10,8);
INSERT INTO public.hotels_services (htl_id, srv_id) VALUES (10,10);
INSERT INTO public.hotels_services (htl_id, srv_id) VALUES (10,11);
INSERT INTO public.hotels_services (htl_id, srv_id) VALUES (11,1);
INSERT INTO public.hotels_services (htl_id, srv_id) VALUES (11,8);
INSERT INTO public.hotels_services (htl_id, srv_id) VALUES (11,10);
INSERT INTO public.hotels_services (htl_id, srv_id) VALUES (11, 11 );
INSERT INTO public.hotels_services (htl_id, srv_id) VALUES (12, 1 );
INSERT INTO public.hotels_services (htl_id, srv_id) VALUES (12, 4 );
INSERT INTO public.hotels_services (htl_id, srv_id) VALUES (12, 8 );
INSERT INTO public.hotels_services (htl_id, srv_id) VALUES (12, 9 );
INSERT INTO public.hotels_services (htl_id, srv_id) VALUES (12, 11 );
INSERT INTO public.hotels_services (htl_id, srv_id) VALUES (13, 1 );
INSERT INTO public.hotels_services (htl_id, srv_id) VALUES (13, 3 );
INSERT INTO public.hotels_services (htl_id, srv_id) VALUES (13, 11 );
INSERT INTO public.hotels_services (htl_id, srv_id) VALUES (13, 2 );
INSERT INTO public.hotels_services (htl_id, srv_id) VALUES (13, 10 );


--TABLAS CUSTOMERS:
-- Surnames en singular
--dni, cambiar y poner documento identidad, acreditativo...
--aumentar d�gitos de credit card, y pensar si se ponen los espacios a la hora de ingresar  los datos
--�ltima columna, credita card, no llevar�a d�a

--CUSTOMERS
/*
INSERT INTO public.customers (cst_name, cst_surnames, cst_email, cst_birth_date, cst_dni, cst_address, cst_nationality,cst_phone, cst_creditcard, cst_valid_date) VALUES ('John','Smith', 'johnSmth123@gmail.com', '01/22/1980', '12345678A', '10, Downing Street, London, SWIA 2AA, United Kingdom', 'Ingl�s', '+44 07987654321', '4321987650129900', '01/10/22' );
INSERT INTO public.customers (cst_name, cst_surnames, cst_email, cst_birth_date, cst_dni, cst_address, cst_nationality,cst_phone, cst_creditcard, cst_valid_date) VALUES ('Nayib','Bukele', 'naybuk@gmail.com', '06/24/1981', '023773088', '4A Calle Poniente, San Salvador, El Salvador', 'Salvadore�o', '+503 211304', '3455687650129900', '01/12/24' );
INSERT INTO public.customers (cst_name, cst_surnames, cst_email, cst_birth_date, cst_dni, cst_address, cst_nationality,cst_phone, cst_creditcard, cst_valid_date) VALUES ('Diego','Guerra Goya', 'guerrita78@gmail.com', '03/20/1978', '23898620A', 'Av.Oza, num 3, 4Izq, A Coru�a, ', 'Espa�ol', '+34 636443658', '9221678950129455', '01/08/23' );
INSERT INTO public.customers (cst_name, cst_surnames, cst_email, cst_birth_date, cst_dni, cst_address, cst_nationality,cst_phone, cst_creditcard, cst_valid_date) VALUES ('Ana','L�pez Estrada', 'anitadinamita@terra.es', '11/11/1984', '98765432A', 'Av.Porto, num78, 7, Vigo, Pontevedra, Espa�a', 'Espa�ola', '+34 07987654321', '4321987650129900', '01/10/22' );
INSERT INTO public.customers (cst_name, cst_surnames, cst_email, cst_birth_date, cst_dni, cst_address, cst_nationality,cst_phone, cst_creditcard, cst_valid_date) VALUES ('Adam','Smith', 'capital4life@outlook.com', '10/27/1953', '12876543A', 'Edimburgo, Escocia. Direcci�n, Charlotte Square,6', 'Escoc�s', '+44 7700123456', '6781987633429900', '01/1/26' );
INSERT INTO public.customers (cst_name, cst_surnames, cst_email, cst_birth_date, cst_dni, cst_address, cst_nationality,cst_phone, cst_creditcard, cst_valid_date) VALUES ('Elder','Santos', 'elder3@gmail.com', '01/22/1960', '14775678A', 'Rua nova, 56, 3Der, Oporto, Portugal', 'Portugu�s', '+351 079876543', '5621987650129222', '01/02/26' );
INSERT INTO public.customers (cst_name, cst_surnames, cst_email, cst_birth_date, cst_dni, cst_address, cst_nationality,cst_phone, cst_creditcard, cst_valid_date) VALUES ('Paloma','Solana Torres', 'Patomareado@hotmail.com', '12/20/2000', '78345978A', 'Calle �palo, num5, 6, Madrid, Espa�a', 'Espa�ola', '+34 78987994321', '9998987650129900', '01/09/27' );
INSERT INTO public.customers (cst_name, cst_surnames, cst_email, cst_birth_date, cst_dni, cst_address, cst_nationality,cst_phone, cst_creditcard, cst_valid_date) VALUES ('Anatoli','Karpov', 'jaquealrey@gmail.com', '01/23/1959', '77845678A', 'Calle Kr�glaia, 67, 8, Moscow, Russia', 'Ruso', '+812 633676543', '9898987650129900', '01/01/28' );
INSERT INTO public.customers (cst_name, cst_surnames, cst_email, cst_birth_date, cst_dni, cst_address, cst_nationality,cst_phone, cst_creditcard, cst_valid_date) VALUES ('Antonio','Castilla L�pez', 'antopez@gmail.com', '01/11/1999', '44346678A', 'Plaza real, 34, 5I, Zaragoza, Espa�a', 'Espa�ol', '+34 96387654321', '5561987666729900', '01/11/24' );
INSERT INTO public.customers (cst_name, cst_surnames, cst_email, cst_birth_date, cst_dni, cst_address, cst_nationality,cst_phone, cst_creditcard, cst_valid_date) VALUES ('Paco','Soria Lobo', 'pacobo@gmail.com', '04/12/1977', '44645678A', 'Av artabria, num56, 3, Murcia, Espa�a', 'Espa�ol', '+34 34567654321', '9871987650129900', '01/07/27' );
INSERT INTO public.customers (cst_name, cst_surnames, cst_email, cst_birth_date, cst_dni, cst_address, cst_nationality,cst_phone, cst_creditcard, cst_valid_date) VALUES ('Juana','Prudant', 'juapru@gmail.com', '09/17/1967', '67845678A', 'Calle lobo, num45, 4Der, Toledo, Espa�a', 'Espa�ol', '+34 07987654321', '4556787650129900', '01/11/25' );
INSERT INTO public.customers (cst_name, cst_surnames, cst_email, cst_birth_date, cst_dni, cst_address, cst_nationality,cst_phone, cst_creditcard, cst_valid_date) VALUES ('Julia','Ondas', 'jul345@gmail.com', '05/05/1980', '78945678A', 'Calle caramelo, num56, 6, La Puebla, M�xico', 'Mexicana', '+52 4831212891', '8865987650129900', '01/12/26' );
*/
--particulares
INSERT INTO public.customers (cst_name, cst_surname, cst_email, 			cst_birth_date, cst_identity_document, 	cst_address , 		cst_city,	cst_zip_code,	cst_state, 	cst_cnt_iso, 	cst_phone, 			cst_agree_spam) 
						VALUES ('John','Smith', 	'johnSmth123@gmail.com', '01/22/1980', '12345678A', 			'10, Downing Street','London','SWIA 2AA',		'',			'GB', 			'+44 07987654321', 	0);
					
INSERT INTO public.customers (cst_name, cst_surname, cst_email, 			cst_birth_date, cst_identity_document, 	cst_address , 		cst_city,	cst_zip_code,	cst_state, 	cst_cnt_iso, 	cst_phone, 			cst_agree_spam)
						VALUES ('Nayib','Bukele', 'naybuk@gmail.com', 		'06/24/1981', 	'023773088', 			'4A Calle Poniente',' San Salvador', '2232224',	'',			'SV', 		'+503 211304', 0 );

INSERT INTO public.customers (cst_name, cst_surname, cst_email, 			cst_birth_date, cst_identity_document, 	cst_address , 		cst_city,	cst_zip_code,	cst_state, 	cst_cnt_iso, 	cst_phone, 		cst_agree_spam)
						VALUES('Diego','Guerra Goya', 'guerrita78@gmail.com', '03/20/1978', '23898620A', 			'Av.Oza, num 3 4Izq','A Coruña', '15001', 		'A Coruña',	'ES', 			'+34 636443658', 0);

INSERT INTO public.customers (cst_name, cst_surname, cst_email, 			cst_birth_date, 	cst_identity_document, 	cst_address , 		cst_city,	cst_zip_code,	cst_state, 	cst_cnt_iso, 	cst_phone, 			cst_agree_spam)
						VALUES ('Ana','López Estrada', 'anitadinamita@terra.es', '11/11/1984', '98765432A', 			'Av.Porto, num78, 7','Vigo',	'36201',		'Pontevedra', 'ES', '+34 07987654321', 1);

INSERT INTO public.customers (cst_name, cst_surname, cst_email, 			cst_birth_date, cst_identity_document, 	cst_address , 		cst_city,	cst_zip_code,	cst_state, 	cst_cnt_iso, 	cst_phone, 			cst_agree_spam)
						VALUES ('Adam','Smith', 'capital4life@outlook.com', '10/27/1953', '12876543A', 				'Charlotte Square,6', 'Edimburgo','78504',		'Escocía', 'GB','+44 7700123456', 1);

INSERT INTO public.customers (cst_name, cst_surname, cst_email, 			cst_birth_date, cst_identity_document, 	cst_address , 			cst_city,	cst_zip_code,	cst_state, 	cst_cnt_iso, 	cst_phone, 			cst_agree_spam)
						VALUES ('Elder','Santos', 'elder3@gmail.com', '01/22/1960', '14775678A',					'Rua nova, 56, 3Der',' Oporto', 	'4000-015',		'',			'PT', 			'+351 079876543', 0);

INSERT INTO public.customers (cst_name, cst_surname, cst_email, 			cst_birth_date, cst_identity_document, 	cst_address , 			cst_city,	cst_zip_code,	cst_state, 	cst_cnt_iso, 	cst_phone, 			cst_agree_spam)
						VALUES ('Paloma','Solana Torres', 'Patomareado@hotmail.com', '12/20/2000', '78345978A', 	'Calle �palo, num5, 6, ','Madrid', '28010',			'Madrid',	'ES', '+34 78987994321', 0 );

INSERT INTO public.customers (cst_name, cst_surname, cst_email, 			cst_birth_date, cst_identity_document, 	cst_address , 		cst_city,	cst_zip_code,	cst_state, 	cst_cnt_iso, 	cst_phone, 			cst_agree_spam)
						VALUES ('Anatoli','Karpov', 'jaquealrey@gmail.com', '01/23/1959', '77845678A', 'Calle Kr�glaia, 67, 8,',  		'Moscow', 	'12321321',		'',			'RU', 			'+812 633676543', 1);
/*
INSERT INTO public.customers (cst_name, cst_surname, cst_email, 			cst_birth_date, cst_identity_document, 	cst_address , 		cst_city,	cst_zip_code,	cst_state, 	cst_cnt_iso, 	cst_phone, 			cst_agree_spam)
						VALUES ('Antonio','Castilla L�pez', 'antopez@gmail.com', '01/11/1999', '44346678A', 'Plaza real, 34, 5I, Zaragoza, Espa�a', 'Espa�ol', '+34 96387654321', 0);
INSERT INTO public.customers (cst_name, cst_surname, cst_email, 			cst_birth_date, cst_identity_document, 	cst_address , 		cst_city,	cst_zip_code,	cst_state, 	cst_cnt_iso, 	cst_phone, 			cst_agree_spam)
						VALUES ('Paco','Soria Lobo', 'pacobo@gmail.com', '04/12/1977', '44645678A', 'Av artabria, num56, 3, Murcia, Espa�a', 'Espa�ol', '+34 34567654321', 1);
INSERT INTO public.customers (cst_name, cst_surname, cst_email, 			cst_birth_date, cst_identity_document, 	cst_address , 		cst_city,	cst_zip_code,	cst_state, 	cst_cnt_iso, 	cst_phone)
						VALUES ('Juana','Prudant', 'juapru@gmail.com', '09/17/1967', '67845678A', 'Calle lobo, num45, 4Der, Toledo, Espa�a', 'Espa�ol', '+34 07987654321');
INSERT INTO public.customers (cst_name, cst_surname, cst_email, 			cst_birth_date, cst_identity_document, 	cst_address , 		cst_city,	cst_zip_code,	cst_state, 	cst_cnt_iso, 	cst_phone)
						VALUES ('Julia','Ondas', 'jul345@gmail.com', '05/05/1980', '78945678A', 'Calle caramelo, num56, 6, La Puebla, M�xico', 'Mexicana', '+52 4831212891');
*/
--empresas
INSERT INTO public.customers (cst_name, 		cst_email, 				 cst_vat_number , 	cst_address , 							cst_city,	cst_zip_code,	cst_state, 	cst_cnt_iso, 	cst_phone, 			cst_agree_spam) 
					VALUES ('Imatia S.L.',	'hola@imatia.com', 'C944557', 				'Edificio Citexvi, Fonte das Abelleiras, s/n ·',	'Vigo',	'36310',		'Pontevedra','ES', 			'0034986342774',  0 );
INSERT INTO public.customers (cst_name, 		cst_email, 				 cst_vat_number , 	cst_address , 			cst_city,	cst_zip_code,	cst_state, 	cst_cnt_iso, 	cst_phone, 			cst_agree_spam) 
						VALUES ('Xunta de Galicia S.L.',	'info@xunta.gal', 'Q34351255', 		'plz del sol 1	',	'Santiago de Compostela',	'15001',		'A Coruña',			'ES', 			'+3498122411',  0 );					
INSERT INTO public.customers (cst_name, 		cst_email, 				 cst_vat_number , 	cst_address , 			cst_city,	cst_zip_code,	cst_state, 	cst_cnt_iso, 	cst_phone, 			cst_agree_spam) 
						VALUES ('Reporteros Viajeros S.A.',	'reporterosviajeros@gmail.com', 'B1235544', 		'10, Downing Street',	'Braga',	'4000-122',		'',			'PT', 			'+44 07987654321',  0 );

					
--BOOKINGS
/*INSERT INTO public.bookings (bkg_stb_id, bkg_cst_id, bkg_rm_id,bkg_observations,bkg_checkin,bkg_checkout) VALUES (1,2,1,'dentro todo','2022-01-10','2022-01-17');
INSERT INTO public.bookings (bkg_stb_id, bkg_cst_id, bkg_rm_id,bkg_observations,bkg_checkin,bkg_checkout) VALUES (1,2,1,'todo fuera','2022-01-01','2022-02-04');
INSERT INTO public.bookings (bkg_stb_id, bkg_cst_id, bkg_rm_id,bkg_observations,bkg_checkin,bkg_checkout) VALUES (1,2,1,'dentro checkout','2022-01-01','2022-01-06');
INSERT INTO public.bookings (bkg_stb_id, bkg_cst_id, bkg_rm_id,bkg_observations,bkg_checkin,bkg_checkout) VALUES (1,2,3,'dentro todo 2','2022-01-19','2022-01-25');
INSERT INTO public.bookings (bkg_stb_id, bkg_cst_id, bkg_rm_id,bkg_observations,bkg_checkin,bkg_checkout) VALUES (4,1,1,'dentro checkin','2022-07-24','2022-02-02');
INSERT INTO public.bookings (bkg_stb_id, bkg_cst_id, bkg_rm_id,bkg_observations,bkg_checkin,bkg_checkout) VALUES (1,2,3,'dentro limite checkin','2022-01-05','2022-01-06');
INSERT INTO public.bookings (bkg_stb_id, bkg_cst_id, bkg_rm_id,bkg_observations,bkg_checkin,bkg_checkout) VALUES (1,2,3,'fuera checkout -limite checkout','2022-01-04','2022-01-05');
INSERT INTO public.bookings (bkg_stb_id, bkg_cst_id, bkg_rm_id,bkg_observations,bkg_checkin,bkg_checkout) VALUES (1,2,3,'fuera todo, limite checkin-rangeout','2023-01-20','2023-01-21');
INSERT INTO public.bookings (bkg_stb_id, bkg_cst_id, bkg_rm_id,bkg_observations,bkg_checkin,bkg_checkout) VALUES (1,2,3,'fuera todo','2023-01-21','2023-01-26');
*/
INSERT INTO public.bookings (bkg_cst_id, bkg_rm_id,bkg_observations,bkg_start,bkg_end) VALUES (2,1,'dentro todo','2022-01-10','2022-01-17');
INSERT INTO public.bookings (bkg_cst_id, bkg_rm_id,bkg_observations,bkg_start,bkg_end) VALUES (2,1,'todo fuera','2022-01-01','2022-02-04');
INSERT INTO public.bookings (bkg_cst_id, bkg_rm_id,bkg_observations,bkg_start,bkg_end) VALUES (2,1,'dentro checkout','2022-01-01','2022-01-06');
INSERT INTO public.bookings (bkg_cst_id, bkg_rm_id,bkg_observations,bkg_start,bkg_end) VALUES (2,3,'dentro todo 2','2022-01-19','2022-01-25');
INSERT INTO public.bookings (bkg_cst_id, bkg_rm_id,bkg_observations,bkg_start,bkg_end) VALUES (1,1,'dentro checkin','2022-07-24','2022-02-02');
INSERT INTO public.bookings (bkg_cst_id, bkg_rm_id,bkg_observations,bkg_start,bkg_end) VALUES (2,3,'dentro limite checkin','2022-01-05','2022-01-06');
INSERT INTO public.bookings (bkg_cst_id, bkg_rm_id,bkg_observations,bkg_start,bkg_end) VALUES (2,3,'fuera checkout -limite checkout','2022-01-04','2022-01-05');
INSERT INTO public.bookings (bkg_cst_id, bkg_rm_id,bkg_observations,bkg_start,bkg_end) VALUES (2,3,'fuera todo, limite checkin-rangeout','2023-01-20','2023-01-21');
INSERT INTO public.bookings (bkg_cst_id, bkg_rm_id,bkg_observations,bkg_start,bkg_end) VALUES (2,3,'fuera todo','2023-01-21','2023-01-26');

--FEATURES
INSERT INTO public.features (ftr_name, ftr_description) VALUES ('Zona Wifi','Wifi hotspot a trav�s de un proceso de autenticaci�n o login.');
INSERT INTO public.features (ftr_name, ftr_description) VALUES ('Insonorizaci�n','A fin de evitar ruidos molestos y permitir una mejor conciliaci�n del sue�o y descanso, dispondr� de una magn�fica insonorizaci�n de la habitaci�n y aislamiento ac�stico de las ventanas.');
INSERT INTO public.features (ftr_name, ftr_description) VALUES ('Amenities','Toallas, peines, cepillos de dientes, gel y jab�n sin aditivos, toallitas desmaquillantes y gorros de ducha.');
INSERT INTO public.features (ftr_name, ftr_description) VALUES ('Secador de pelo','Color negro. Potencia: 2500W');
INSERT INTO public.features (ftr_name, ftr_description) VALUES ('Jacuzzi','7 chorros');
INSERT INTO public.features (ftr_name, ftr_description) VALUES ('Caja fuerte','Ferrimax Serie Hotel TSW. Espesor total de las paredes: 80 mm; cada nivel de seguridad lleva un blindaje interior diferente');
INSERT INTO public.features (ftr_name, ftr_description) VALUES ('Ba�era con hidromasaje','Spatec Duo. Fabricada en fibra de vidrio reforzado con acabado acr�lico, color negro, dimensiones: 180x120x61 cm, LED de iluminaci�n subacu�tica, desinfecci�n autom�tica con ozono');
INSERT INTO public.features (ftr_name, ftr_description) VALUES ('Tv por cable','Pantalla panor�mica de 50 pulgadas, Full HD o HD Ready, conexiones USB, HDMI, MP3');



--ROOM_TYPES_FEATURES
INSERT INTO public.room_types_features (rmt_id,ftr_id) VALUES (1,1);
INSERT INTO public.room_types_features (rmt_id,ftr_id) VALUES (1,2);
INSERT INTO public.room_types_features (rmt_id,ftr_id) VALUES (1,3);
INSERT INTO public.room_types_features (rmt_id,ftr_id) VALUES (1,4);
INSERT INTO public.room_types_features (rmt_id,ftr_id) VALUES (2,1);
INSERT INTO public.room_types_features (rmt_id,ftr_id) VALUES (2,2);
INSERT INTO public.room_types_features (rmt_id,ftr_id) VALUES (2,3);
INSERT INTO public.room_types_features (rmt_id,ftr_id) VALUES (2,4);
INSERT INTO public.room_types_features (rmt_id,ftr_id) VALUES (3,1);
INSERT INTO public.room_types_features (rmt_id,ftr_id) VALUES (3,2);
INSERT INTO public.room_types_features (rmt_id,ftr_id) VALUES (3,3);
INSERT INTO public.room_types_features (rmt_id,ftr_id) VALUES (3,4);
INSERT INTO public.room_types_features (rmt_id,ftr_id) VALUES (4,1);
INSERT INTO public.room_types_features (rmt_id,ftr_id) VALUES (4,2);
INSERT INTO public.room_types_features (rmt_id,ftr_id) VALUES (4,3);
INSERT INTO public.room_types_features (rmt_id,ftr_id) VALUES (4,4);
INSERT INTO public.room_types_features (rmt_id,ftr_id) VALUES (5,1);
INSERT INTO public.room_types_features (rmt_id,ftr_id) VALUES (5,2);
INSERT INTO public.room_types_features (rmt_id,ftr_id) VALUES (5,3);
INSERT INTO public.room_types_features (rmt_id,ftr_id) VALUES (5,4);
INSERT INTO public.room_types_features (rmt_id,ftr_id) VALUES (6,1);
INSERT INTO public.room_types_features (rmt_id,ftr_id) VALUES (6,2);
INSERT INTO public.room_types_features (rmt_id,ftr_id) VALUES (6,3);
INSERT INTO public.room_types_features (rmt_id,ftr_id) VALUES (6,4);
INSERT INTO public.room_types_features (rmt_id,ftr_id) VALUES (6,5);
INSERT INTO public.room_types_features (rmt_id,ftr_id) VALUES (6,6);
INSERT INTO public.room_types_features (rmt_id,ftr_id) VALUES (7,1);
INSERT INTO public.room_types_features (rmt_id,ftr_id) VALUES (7,2);
INSERT INTO public.room_types_features (rmt_id,ftr_id) VALUES (7,3);
INSERT INTO public.room_types_features (rmt_id,ftr_id) VALUES (7,4);
INSERT INTO public.room_types_features (rmt_id,ftr_id) VALUES (7,5);
INSERT INTO public.room_types_features (rmt_id,ftr_id) VALUES (7,6);
INSERT INTO public.room_types_features (rmt_id,ftr_id) VALUES (8,1);
INSERT INTO public.room_types_features (rmt_id,ftr_id) VALUES (8,2);
INSERT INTO public.room_types_features (rmt_id,ftr_id) VALUES (8,3);
INSERT INTO public.room_types_features (rmt_id,ftr_id) VALUES (8,4);
INSERT INTO public.room_types_features (rmt_id,ftr_id) VALUES (8,5);
INSERT INTO public.room_types_features (rmt_id,ftr_id) VALUES (8,6);
INSERT INTO public.room_types_features (rmt_id,ftr_id) VALUES (8,7);
INSERT INTO public.room_types_features (rmt_id,ftr_id) VALUES (8,8);
INSERT INTO public.room_types_features (rmt_id,ftr_id) VALUES (9,1);
INSERT INTO public.room_types_features (rmt_id,ftr_id) VALUES (9,2);
INSERT INTO public.room_types_features (rmt_id,ftr_id) VALUES (9,3);
INSERT INTO public.room_types_features (rmt_id,ftr_id) VALUES (9,4);
INSERT INTO public.room_types_features (rmt_id,ftr_id) VALUES (9,5);
INSERT INTO public.room_types_features (rmt_id,ftr_id) VALUES (9,6);
INSERT INTO public.room_types_features (rmt_id,ftr_id) VALUES (9,7);
INSERT INTO public.room_types_features (rmt_id,ftr_id) VALUES (9,8);


--SERVICESEXTRA
INSERT INTO public.servicesXtra (sxt_name, sxt_description) VALUES ('Servicio de custodia de maletas.', 'Podr� dejarnos sus pertenencias con toda seguridad.');
INSERT INTO public.servicesXtra (sxt_name, sxt_description) VALUES ('Servicio de lavander�a.', 'Ropa limpia en 24 horas.');
INSERT INTO public.servicesXtra (sxt_name, sxt_description) VALUES ('Servicio de peluquer�a.', 'Directamente a la habitaci�n.');
INSERT INTO public.servicesXtra (sxt_name, sxt_description) VALUES ('Sauna.', 'Tonifica y libera toxinas.');
INSERT INTO public.servicesXtra (sxt_name, sxt_description) VALUES ('Gimnasio.', 'Mant�n la rutina del cuerpo en vacaciones.');
INSERT INTO public.servicesXtra (sxt_name, sxt_description) VALUES ('Bienvenida personalizada.', 'Qu� mejor bienvenida, que celebrarlo con champagne, flores y bombones.');
INSERT INTO public.servicesXtra (sxt_name, sxt_description) VALUES ('Alquiler de salones.', 'Para reuniones profesionales, comuniones, bodas y grandes eventos, disponemos del sal�n que necesitas.');
INSERT INTO public.servicesXtra (sxt_name, sxt_description) VALUES ('Visitas guiadas.', 'Tenemos los mejores tours para que disfutes de tu estancia.');
INSERT INTO public.servicesXtra (sxt_name, sxt_description) VALUES ('Servicio restaurante.', 'Plantilla profesional y excelente servicio.');
INSERT INTO public.servicesXtra (sxt_name, sxt_description) VALUES ('Media pensi�n.', 'Incluido desayuno y una comida (almuerzo o cena).');
INSERT INTO public.servicesXtra (sxt_name, sxt_description) VALUES ('Pensi�n completa.', 'Incluye desayuno, almuerzo y cena.');
INSERT INTO public.servicesXtra (sxt_name, sxt_description) VALUES ('Media pensi�n plus.', 'Incluye desayuno y cena en nuestro restaurante, tendr�s tambi�n libre elecci�n de restaurantes asociados para cenar durante toda la estancia, cena en un restaurante diferente cada noche.');
INSERT INTO public.servicesXtra (sxt_name, sxt_description) VALUES ('Servicio de habitaci�n.', 'Te llevamos lo que necesites para que no abandones tu lugar de descanso.');
INSERT INTO public.servicesXtra (sxt_name, sxt_description) VALUES ('Aparcani�os.', 'Para que los m�s peque�os, no impidan que disfrutes de un poco de tiempo para ti.');
INSERT INTO public.servicesXtra (sxt_name, sxt_description) VALUES ('Minibar.', 'Las mejores bebidas y snacks al alcance de su mano.');

--COUNTRIES
INSERT INTO countries VALUES('AF', 'Afganistán');
INSERT INTO countries VALUES('AX', 'Islas Gland');
INSERT INTO countries VALUES('AL', 'Albania');
INSERT INTO countries VALUES('DE', 'Alemania');
INSERT INTO countries VALUES('AD', 'Andorra');
INSERT INTO countries VALUES('AO', 'Angola');
INSERT INTO countries VALUES('AI', 'Anguilla');
INSERT INTO countries VALUES('AQ', 'Antártida');
INSERT INTO countries VALUES('AG', 'Antigua y Barbuda');
INSERT INTO countries VALUES('AN', 'Antillas Holandesas');
INSERT INTO countries VALUES('SA', 'Arabia Saudí');
INSERT INTO countries VALUES('DZ', 'Argelia');
INSERT INTO countries VALUES('AR', 'Argentina');
INSERT INTO countries VALUES('AM', 'Armenia');
INSERT INTO countries VALUES('AW', 'Aruba');
INSERT INTO countries VALUES('AU', 'Australia');
INSERT INTO countries VALUES('AT', 'Austria');
INSERT INTO countries VALUES('AZ', 'Azerbaiyán');
INSERT INTO countries VALUES('BS', 'Bahamas');
INSERT INTO countries VALUES('BH', 'Bahréin');
INSERT INTO countries VALUES('BD', 'Bangladesh');
INSERT INTO countries VALUES('BB', 'Barbados');
INSERT INTO countries VALUES('BY', 'Bielorrusia');
INSERT INTO countries VALUES('BE', 'Bélgica');
INSERT INTO countries VALUES('BZ', 'Belice');
INSERT INTO countries VALUES('BJ', 'Benin');
INSERT INTO countries VALUES('BM', 'Bermudas');
INSERT INTO countries VALUES('BT', 'Bhután');
INSERT INTO countries VALUES('BO', 'Bolivia');
INSERT INTO countries VALUES('BA', 'Bosnia y Herzegovina');
INSERT INTO countries VALUES('BW', 'Botsuana');
INSERT INTO countries VALUES('BV', 'Isla Bouvet');
INSERT INTO countries VALUES('BR', 'Brasil');
INSERT INTO countries VALUES('BN', 'Brunéi');
INSERT INTO countries VALUES('BG', 'Bulgaria');
INSERT INTO countries VALUES('BF', 'Burkina Faso');
INSERT INTO countries VALUES('BI', 'Burundi');
INSERT INTO countries VALUES('CV', 'Cabo Verde');
INSERT INTO countries VALUES('KY', 'Islas Caimán');
INSERT INTO countries VALUES('KH', 'Camboya');
INSERT INTO countries VALUES('CM', 'Camerún');
INSERT INTO countries VALUES('CA', 'Canadá');
INSERT INTO countries VALUES('CF', 'República Centroafricana');
INSERT INTO countries VALUES('TD', 'Chad');
INSERT INTO countries VALUES('CZ', 'República Checa');
INSERT INTO countries VALUES('CL', 'Chile');
INSERT INTO countries VALUES('CN', 'China');
INSERT INTO countries VALUES('CY', 'Chipre');
INSERT INTO countries VALUES('CX', 'Isla de Navidad');
INSERT INTO countries VALUES('VA', 'Ciudad del Vaticano');
INSERT INTO countries VALUES('CC', 'Islas Cocos');
INSERT INTO countries VALUES('CO', 'Colombia');
INSERT INTO countries VALUES('KM', 'Comoras');
INSERT INTO countries VALUES('CD', 'República Democrática del Congo');
INSERT INTO countries VALUES('CG', 'Congo');
INSERT INTO countries VALUES('CK', 'Islas Cook');
INSERT INTO countries VALUES('KP', 'Corea del Norte');
INSERT INTO countries VALUES('KR', 'Corea del Sur');
INSERT INTO countries VALUES('CI', 'Costa de Marfil');
INSERT INTO countries VALUES('CR', 'Costa Rica');
INSERT INTO countries VALUES('HR', 'Croacia');
INSERT INTO countries VALUES('CU', 'Cuba');
INSERT INTO countries VALUES('DK', 'Dinamarca');
INSERT INTO countries VALUES('DM', 'Dominica');
INSERT INTO countries VALUES('DO', 'República Dominicana');
INSERT INTO countries VALUES('EC', 'Ecuador');
INSERT INTO countries VALUES('EG', 'Egipto');
INSERT INTO countries VALUES('SV', 'El Salvador');
INSERT INTO countries VALUES('AE', 'Emiratos Árabes Unidos');
INSERT INTO countries VALUES('ER', 'Eritrea');
INSERT INTO countries VALUES('SK', 'Eslovaquia');
INSERT INTO countries VALUES('SI', 'Eslovenia');
INSERT INTO countries VALUES('ES', 'España');
INSERT INTO countries VALUES('UM', 'Islas ultramarinas de Estados Unidos');
INSERT INTO countries VALUES('US', 'Estados Unidos');
INSERT INTO countries VALUES('EE', 'Estonia');
INSERT INTO countries VALUES('ET', 'Etiopía');
INSERT INTO countries VALUES('FO', 'Islas Feroe');
INSERT INTO countries VALUES('PH', 'Filipinas');
INSERT INTO countries VALUES('FI', 'Finlandia');
INSERT INTO countries VALUES('FJ', 'Fiyi');
INSERT INTO countries VALUES('FR', 'Francia');
INSERT INTO countries VALUES('GA', 'Gabón');
INSERT INTO countries VALUES('GM', 'Gambia');
INSERT INTO countries VALUES('GE', 'Georgia');
INSERT INTO countries VALUES('GS', 'Islas Georgias del Sur y Sandwich del Sur');
INSERT INTO countries VALUES('GH', 'Ghana');
INSERT INTO countries VALUES('GI', 'Gibraltar');
INSERT INTO countries VALUES('GD', 'Granada');
INSERT INTO countries VALUES('GR', 'Grecia');
INSERT INTO countries VALUES('GL', 'Groenlandia');
INSERT INTO countries VALUES('GP', 'Guadalupe');
INSERT INTO countries VALUES('GU', 'Guam');
INSERT INTO countries VALUES('GT', 'Guatemala');
INSERT INTO countries VALUES('GF', 'Guayana Francesa');
INSERT INTO countries VALUES('GN', 'Guinea');
INSERT INTO countries VALUES('GQ', 'Guinea Ecuatorial');
INSERT INTO countries VALUES('GW', 'Guinea-Bissau');
INSERT INTO countries VALUES('GY', 'Guyana');
INSERT INTO countries VALUES('HT', 'Haití');
INSERT INTO countries VALUES('HM', 'Islas Heard y McDonald');
INSERT INTO countries VALUES('HN', 'Honduras');
INSERT INTO countries VALUES('HK', 'Hong Kong');
INSERT INTO countries VALUES('HU', 'Hungría');
INSERT INTO countries VALUES('IN', 'India');
INSERT INTO countries VALUES('ID', 'Indonesia');
INSERT INTO countries VALUES('IR', 'Irán');
INSERT INTO countries VALUES('IQ', 'Iraq');
INSERT INTO countries VALUES('IE', 'Irlanda');
INSERT INTO countries VALUES('IS', 'Islandia');
INSERT INTO countries VALUES('IL', 'Israel');
INSERT INTO countries VALUES('IT', 'Italia');
INSERT INTO countries VALUES('JM', 'Jamaica');
INSERT INTO countries VALUES('JP', 'Japón');
INSERT INTO countries VALUES('JO', 'Jordania');
INSERT INTO countries VALUES('KZ', 'Kazajstán');
INSERT INTO countries VALUES('KE', 'Kenia');
INSERT INTO countries VALUES('KG', 'Kirguistán');
INSERT INTO countries VALUES('KI', 'Kiribati');
INSERT INTO countries VALUES('KW', 'Kuwait');
INSERT INTO countries VALUES('LA', 'Laos');
INSERT INTO countries VALUES('LS', 'Lesotho');
INSERT INTO countries VALUES('LV', 'Letonia');
INSERT INTO countries VALUES('LB', 'Líbano');
INSERT INTO countries VALUES('LR', 'Liberia');
INSERT INTO countries VALUES('LY', 'Libia');
INSERT INTO countries VALUES('LI', 'Liechtenstein');
INSERT INTO countries VALUES('LT', 'Lituania');
INSERT INTO countries VALUES('LU', 'Luxemburgo');
INSERT INTO countries VALUES('MO', 'Macao');
INSERT INTO countries VALUES('MK', 'ARY Macedonia');
INSERT INTO countries VALUES('MG', 'Madagascar');
INSERT INTO countries VALUES('MY', 'Malasia');
INSERT INTO countries VALUES('MW', 'Malawi');
INSERT INTO countries VALUES('MV', 'Maldivas');
INSERT INTO countries VALUES('ML', 'Malí');
INSERT INTO countries VALUES('MT', 'Malta');
INSERT INTO countries VALUES('FK', 'Islas Malvinas');
INSERT INTO countries VALUES('MP', 'Islas Marianas del Norte');
INSERT INTO countries VALUES('MA', 'Marruecos');
INSERT INTO countries VALUES('MH', 'Islas Marshall');
INSERT INTO countries VALUES('MQ', 'Martinica');
INSERT INTO countries VALUES('MU', 'Mauricio');
INSERT INTO countries VALUES('MR', 'Mauritania');
INSERT INTO countries VALUES('YT', 'Mayotte');
INSERT INTO countries VALUES('MX', 'México');
INSERT INTO countries VALUES('FM', 'Micronesia');
INSERT INTO countries VALUES('MD', 'Moldavia');
INSERT INTO countries VALUES('MC', 'Mónaco');
INSERT INTO countries VALUES('MN', 'Mongolia');
INSERT INTO countries VALUES('MS', 'Montserrat');
INSERT INTO countries VALUES('MZ', 'Mozambique');
INSERT INTO countries VALUES('MM', 'Myanmar');
INSERT INTO countries VALUES('NA', 'Namibia');
INSERT INTO countries VALUES('NR', 'Nauru');
INSERT INTO countries VALUES('NP', 'Nepal');
INSERT INTO countries VALUES('NI', 'Nicaragua');
INSERT INTO countries VALUES('NE', 'Níger');
INSERT INTO countries VALUES('NG', 'Nigeria');
INSERT INTO countries VALUES('NU', 'Niue');
INSERT INTO countries VALUES('NF', 'Isla Norfolk');
INSERT INTO countries VALUES('NO', 'Noruega');
INSERT INTO countries VALUES('NC', 'Nueva Caledonia');
INSERT INTO countries VALUES('NZ', 'Nueva Zelanda');
INSERT INTO countries VALUES('OM', 'Omán');
INSERT INTO countries VALUES('NL', 'Países Bajos');
INSERT INTO countries VALUES('PK', 'Pakistán');
INSERT INTO countries VALUES('PW', 'Palau');
INSERT INTO countries VALUES('PS', 'Palestina');
INSERT INTO countries VALUES('PA', 'Panamá');
INSERT INTO countries VALUES('PG', 'Papúa Nueva Guinea');
INSERT INTO countries VALUES('PY', 'Paraguay');
INSERT INTO countries VALUES('PE', 'Perú');
INSERT INTO countries VALUES('PN', 'Islas Pitcairn');
INSERT INTO countries VALUES('PF', 'Polinesia Francesa');
INSERT INTO countries VALUES('PL', 'Polonia');
INSERT INTO countries VALUES('PT', 'Portugal');
INSERT INTO countries VALUES('PR', 'Puerto Rico');
INSERT INTO countries VALUES('QA', 'Qatar');
INSERT INTO countries VALUES('GB', 'Reino Unido');
INSERT INTO countries VALUES('RE', 'Reunión');
INSERT INTO countries VALUES('RW', 'Ruanda');
INSERT INTO countries VALUES('RO', 'Rumania');
INSERT INTO countries VALUES('RU', 'Rusia');
INSERT INTO countries VALUES('EH', 'Sahara Occidental');
INSERT INTO countries VALUES('SB', 'Islas Salomón');
INSERT INTO countries VALUES('WS', 'Samoa');
INSERT INTO countries VALUES('AS', 'Samoa Americana');
INSERT INTO countries VALUES('KN', 'San Cristóbal y Nevis');
INSERT INTO countries VALUES('SM', 'San Marino');
INSERT INTO countries VALUES('PM', 'San Pedro y Miquelón');
INSERT INTO countries VALUES('VC', 'San Vicente y las Granadinas');
INSERT INTO countries VALUES('SH', 'Santa Helena');
INSERT INTO countries VALUES('LC', 'Santa Lucía');
INSERT INTO countries VALUES('ST', 'Santo Tomé y Príncipe');
INSERT INTO countries VALUES('SN', 'Senegal');
INSERT INTO countries VALUES('CS', 'Serbia y Montenegro');
INSERT INTO countries VALUES('SC', 'Seychelles');
INSERT INTO countries VALUES('SL', 'Sierra Leona');
INSERT INTO countries VALUES('SG', 'Singapur');
INSERT INTO countries VALUES('SY', 'Siria');
INSERT INTO countries VALUES('SO', 'Somalia');
INSERT INTO countries VALUES('LK', 'Sri Lanka');
INSERT INTO countries VALUES('SZ', 'Suazilandia');
INSERT INTO countries VALUES('ZA', 'Sudáfrica');
INSERT INTO countries VALUES('SD', 'Sudán');
INSERT INTO countries VALUES('SE', 'Suecia');
INSERT INTO countries VALUES('CH', 'Suiza');
INSERT INTO countries VALUES('SR', 'Surinam');
INSERT INTO countries VALUES('SJ', 'Svalbard y Jan Mayen');
INSERT INTO countries VALUES('TH', 'Tailandia');
INSERT INTO countries VALUES('TW', 'Taiwán');
INSERT INTO countries VALUES('TZ', 'Tanzania');
INSERT INTO countries VALUES('TJ', 'Tayikistán');
INSERT INTO countries VALUES('IO', 'Territorio Británico del Océano Índico');
INSERT INTO countries VALUES('TF', 'Territorios Australes Franceses');
INSERT INTO countries VALUES('TL', 'Timor Oriental');
INSERT INTO countries VALUES('TG', 'Togo');
INSERT INTO countries VALUES('TK', 'Tokelau');
INSERT INTO countries VALUES('TO', 'Tonga');
INSERT INTO countries VALUES('TT', 'Trinidad y Tobago');
INSERT INTO countries VALUES('TN', 'Túnez');
INSERT INTO countries VALUES('TC', 'Islas Turcas y Caicos');
INSERT INTO countries VALUES('TM', 'Turkmenistán');
INSERT INTO countries VALUES('TR', 'Turquía');
INSERT INTO countries VALUES('TV', 'Tuvalu');
INSERT INTO countries VALUES('UA', 'Ucrania');
INSERT INTO countries VALUES('UG', 'Uganda');
INSERT INTO countries VALUES('UY', 'Uruguay');
INSERT INTO countries VALUES('UZ', 'Uzbekistán');
INSERT INTO countries VALUES('VU', 'Vanuatu');
INSERT INTO countries VALUES('VE', 'Venezuela');
INSERT INTO countries VALUES('VN', 'Vietnam');
INSERT INTO countries VALUES('VG', 'Islas Vírgenes Británicas');
INSERT INTO countries VALUES('VI', 'Islas Vírgenes de los Estados Unidos');
INSERT INTO countries VALUES('WF', 'Wallis y Futuna');
INSERT INTO countries VALUES('YE', 'Yemen');
INSERT INTO countries VALUES('DJ', 'Yibuti');
INSERT INTO countries VALUES('ZM', 'Zambia');
INSERT INTO countries VALUES('ZW', 'Zimbabue');


--DEPARTMENTS
INSERT INTO public.departments (dpt_name, dpt_description) VALUES ('Limpieza', 'Se encarga de la limpieza, orden y estética de las diferentes áreas que conforman un hotel');
INSERT INTO public.departments (dpt_name, dpt_description) VALUES ('Hostelería', 'Se ocupa de las operaciones de adquisición, producción y preparación de alimentos y bebidas para venderlos en el hotel. Encargado de servicios de habitación, banquetes y celebraciones.');
INSERT INTO public.departments (dpt_name, dpt_description) VALUES ('Recursos humanos', 'Tiene como finalidad gestionar el personal que constituye la organización, tal como el reclutamiento, la supervisión y formación.');
INSERT INTO public.departments (dpt_name, dpt_description) VALUES ('Marketing', 'Responsable de ejecutar las estrategias de publicidad. Su función es dar visibilidad y posicionamiento en los diferentes medios de exposición para conseguir un mayor número de ventas.');
INSERT INTO public.departments (dpt_name, dpt_description) VALUES ('Seguridad', 'Se encarga de mantener el hotel y sus diferentes áreas bajo un sistema de seguridad. El objetivo es proteger a los huéspedes y los trabajadores.');
INSERT INTO public.departments (dpt_name, dpt_description) VALUES ('Recepción', 'encargado de administrar la recepción del hotel, de recibir las quejas de los huéspedes, anotar las sugerencias, controlar las llaves de las habitaciones, ofrecer información del hotel, gestionar la salida y entrada de los clientes.');
INSERT INTO public.departments (dpt_name, dpt_description) VALUES ('Mantenimiento', 'Se dedica al mantenimiento preventivo y correctivo de todos los elementos que conforma un hotel');


--BILLS (ordenado según departamentos)
--Hostelería
INSERT INTO public.bills (bll_htl_id, bll_dpt_id, bll_concept, bll_date, bll_amount) VALUES (4, 2, 'Reposición almacén', '2020-01-01', 3000.5);
INSERT INTO public.bills (bll_htl_id, bll_dpt_id, bll_concept, bll_date, bll_amount) VALUES (4, 2, 'Reposición almacén', '2020-04-01', 1750);
INSERT INTO public.bills (bll_htl_id, bll_dpt_id, bll_concept, bll_date, bll_amount) VALUES (4, 2, 'Reposición almacén', '2020-05-08', 3000.15);
INSERT INTO public.bills (bll_htl_id, bll_dpt_id, bll_concept, bll_date, bll_amount) VALUES (4, 2, 'Reposición almacén', '2020-07-08', 4000.5);
INSERT INTO public.bills (bll_htl_id, bll_dpt_id, bll_concept, bll_date, bll_amount) VALUES (4, 2, 'Reposición almacén', '2020-09-08', 5000.55);
INSERT INTO public.bills (bll_htl_id, bll_dpt_id, bll_concept, bll_date, bll_amount) VALUES (4, 2, 'Reposición almacén', '2020-11-08', 1960);
INSERT INTO public.bills (bll_htl_id, bll_dpt_id, bll_concept, bll_date, bll_amount) VALUES (4, 2, 'Reposición almacén', '2020-12-08', 6700);
INSERT INTO public.bills (bll_htl_id, bll_dpt_id, bll_concept, bll_date, bll_amount) VALUES (4, 2, 'Reposición almacén', '2021-01-08', 1500.5);
INSERT INTO public.bills (bll_htl_id, bll_dpt_id, bll_concept, bll_date, bll_amount) VALUES (4, 2, 'Reposición almacén', '2021-03-08', 3000.5);
INSERT INTO public.bills (bll_htl_id, bll_dpt_id, bll_concept, bll_date, bll_amount) VALUES (4, 2, 'Reposición almacén', '2021-06-08', 2500.56);
INSERT INTO public.bills (bll_htl_id, bll_dpt_id, bll_concept, bll_date, bll_amount) VALUES (4, 2, 'Reposición almacén', '2021-10-08', 4000.5);
INSERT INTO public.bills (bll_htl_id, bll_dpt_id, bll_concept, bll_date, bll_amount) VALUES (4, 2, 'Reposición almacén', '2021-12-08', 6500.78);
INSERT INTO public.bills (bll_htl_id, bll_dpt_id, bll_concept, bll_date, bll_amount) VALUES (5, 2, 'Reposición almacén', '2019-11-08', 4000.5);
INSERT INTO public.bills (bll_htl_id, bll_dpt_id, bll_concept, bll_date, bll_amount) VALUES (5, 2, 'Reposición almacén', '2019-03-08', 6500.78);
INSERT INTO public.bills (bll_htl_id, bll_dpt_id, bll_concept, bll_date, bll_amount) VALUES (5, 2, 'Reposición almacén', '2021-09-08', 1060);
INSERT INTO public.bills (bll_htl_id, bll_dpt_id, bll_concept, bll_date, bll_amount) VALUES (5, 2, 'Reposición almacén', '2021-12-08', 2700);
INSERT INTO public.bills (bll_htl_id, bll_dpt_id, bll_concept, bll_date, bll_amount) VALUES (5, 2, 'Reposición almacén', '2022-05-08', 1500.5);
INSERT INTO public.bills (bll_htl_id, bll_dpt_id, bll_concept, bll_date, bll_amount) VALUES (5, 2, 'Reposición almacén', '2022-06-08', 3000);
INSERT INTO public.bills (bll_htl_id, bll_dpt_id, bll_concept, bll_date, bll_amount) VALUES (5, 2, 'Reposición almacén', '2022-07-08', 2500.56);
INSERT INTO public.bills (bll_htl_id, bll_dpt_id, bll_concept, bll_date, bll_amount) VALUES (6, 2, 'Reposición almacén', '2022-01-08', 1670);
INSERT INTO public.bills (bll_htl_id, bll_dpt_id, bll_concept, bll_date, bll_amount) VALUES (6, 2, 'Reposición almacén', '2022-05-08', 1400.5);
INSERT INTO public.bills (bll_htl_id, bll_dpt_id, bll_concept, bll_date, bll_amount) VALUES (6, 2, 'Reposición almacén', '2022-07-08', 1200.95);
--Limpieza
INSERT INTO public.bills (bll_htl_id, bll_dpt_id, bll_concept, bll_date, bll_amount) VALUES (4, 1, 'Limpieza', '2022-06-08', 1000);
INSERT INTO public.bills (bll_htl_id, bll_dpt_id, bll_concept, bll_date, bll_amount) VALUES (4, 1, 'Limpieza', '2022-07-08', 1500.56);
INSERT INTO public.bills (bll_htl_id, bll_dpt_id, bll_concept, bll_date, bll_amount) VALUES (4, 1, 'Limpieza', '2022-01-08', 1670);
INSERT INTO public.bills (bll_htl_id, bll_dpt_id, bll_concept, bll_date, bll_amount) VALUES (5, 1, 'Limpieza', '2022-06-08', 1000);
INSERT INTO public.bills (bll_htl_id, bll_dpt_id, bll_concept, bll_date, bll_amount) VALUES (5, 1, 'Limpieza', '2022-07-08', 1500.56);
INSERT INTO public.bills (bll_htl_id, bll_dpt_id, bll_concept, bll_date, bll_amount) VALUES (6, 1, 'Limpieza', '2022-01-08', 1670);
INSERT INTO public.bills (bll_htl_id, bll_dpt_id, bll_concept, bll_date, bll_amount) VALUES (6, 1, 'Limpieza', '2022-05-08', 1800.5);
INSERT INTO public.bills (bll_htl_id, bll_dpt_id, bll_concept, bll_date, bll_amount) VALUES (6, 1, 'Limpieza', '2022-07-08', 1800.95);
--Marketing
INSERT INTO public.bills (bll_htl_id, bll_dpt_id, bll_concept, bll_date, bll_amount) VALUES (4, 4, 'Marketing', '2022-06-08', 300);
INSERT INTO public.bills (bll_htl_id, bll_dpt_id, bll_concept, bll_date, bll_amount) VALUES (4, 4, 'Marketing', '2022-07-08', 250.56);
INSERT INTO public.bills (bll_htl_id, bll_dpt_id, bll_concept, bll_date, bll_amount) VALUES (4, 4, 'Marketing', '2022-01-08', 160);
INSERT INTO public.bills (bll_htl_id, bll_dpt_id, bll_concept, bll_date, bll_amount) VALUES (5, 4, 'Marketing', '2022-06-08', 300);
INSERT INTO public.bills (bll_htl_id, bll_dpt_id, bll_concept, bll_date, bll_amount) VALUES (5, 4, 'Marketing', '2022-07-08', 200.56);
INSERT INTO public.bills (bll_htl_id, bll_dpt_id, bll_concept, bll_date, bll_amount) VALUES (6, 4, 'Marketing', '2022-01-08', 170);
INSERT INTO public.bills (bll_htl_id, bll_dpt_id, bll_concept, bll_date, bll_amount) VALUES (6, 4, 'Marketing', '2022-05-08', 140.5);
INSERT INTO public.bills (bll_htl_id, bll_dpt_id, bll_concept, bll_date, bll_amount) VALUES (6, 4, 'Marketing', '2022-07-08', 120.95);
--Seguridad
INSERT INTO public.bills (bll_htl_id, bll_dpt_id, bll_concept, bll_date, bll_amount) VALUES (4, 5, 'Seguridad', '2022-06-08', 500);
INSERT INTO public.bills (bll_htl_id, bll_dpt_id, bll_concept, bll_date, bll_amount) VALUES (4, 5, 'Seguridad', '2022-07-08', 600.56);
INSERT INTO public.bills (bll_htl_id, bll_dpt_id, bll_concept, bll_date, bll_amount) VALUES (4, 5, 'Seguridad', '2022-01-08', 167);
INSERT INTO public.bills (bll_htl_id, bll_dpt_id, bll_concept, bll_date, bll_amount) VALUES (5, 5, 'Seguridad', '2022-06-08', 300);
INSERT INTO public.bills (bll_htl_id, bll_dpt_id, bll_concept, bll_date, bll_amount) VALUES (5, 5, 'Seguridad', '2022-07-08', 250.56);
INSERT INTO public.bills (bll_htl_id, bll_dpt_id, bll_concept, bll_date, bll_amount) VALUES (6, 5, 'Seguridad', '2022-01-08', 1670);
INSERT INTO public.bills (bll_htl_id, bll_dpt_id, bll_concept, bll_date, bll_amount) VALUES (6, 5, 'Seguridad', '2022-05-08', 140.5);
INSERT INTO public.bills (bll_htl_id, bll_dpt_id, bll_concept, bll_date, bll_amount) VALUES (6, 5, 'Seguridad', '2022-07-08', 120.95);
--Recepción
INSERT INTO public.bills (bll_htl_id, bll_dpt_id, bll_concept, bll_date, bll_amount) VALUES (4, 6, 'Recepción', '2022-06-08', 100);
INSERT INTO public.bills (bll_htl_id, bll_dpt_id, bll_concept, bll_date, bll_amount) VALUES (4, 6, 'Recepción', '2022-07-08', 60.56);
INSERT INTO public.bills (bll_htl_id, bll_dpt_id, bll_concept, bll_date, bll_amount) VALUES (4, 6, 'Recepción', '2022-01-08', 167);
INSERT INTO public.bills (bll_htl_id, bll_dpt_id, bll_concept, bll_date, bll_amount) VALUES (5, 6, 'Recepción', '2022-06-08', 300);
INSERT INTO public.bills (bll_htl_id, bll_dpt_id, bll_concept, bll_date, bll_amount) VALUES (5, 6, 'Recepción', '2022-07-08', 280.6);
INSERT INTO public.bills (bll_htl_id, bll_dpt_id, bll_concept, bll_date, bll_amount) VALUES (6, 6, 'Recepción', '2022-01-08', 170);
INSERT INTO public.bills (bll_htl_id, bll_dpt_id, bll_concept, bll_date, bll_amount) VALUES (6, 6, 'Recepción', '2022-05-08', 140.5);
INSERT INTO public.bills (bll_htl_id, bll_dpt_id, bll_concept, bll_date, bll_amount) VALUES (6, 6, 'Recepción', '2022-07-08', 120);
--Mantenimiento
INSERT INTO public.bills (bll_htl_id, bll_dpt_id, bll_concept, bll_date, bll_amount) VALUES (4, 7, 'Mantenimiento', '2022-06-08', 1600);
INSERT INTO public.bills (bll_htl_id, bll_dpt_id, bll_concept, bll_date, bll_amount) VALUES (4, 7, 'Mantenimiento', '2022-07-08', 60.56);
INSERT INTO public.bills (bll_htl_id, bll_dpt_id, bll_concept, bll_date, bll_amount) VALUES (4, 7, 'Mantenimiento', '2022-01-08', 167);
INSERT INTO public.bills (bll_htl_id, bll_dpt_id, bll_concept, bll_date, bll_amount) VALUES (5, 7, 'Mantenimiento', '2022-06-08', 300);
INSERT INTO public.bills (bll_htl_id, bll_dpt_id, bll_concept, bll_date, bll_amount) VALUES (5, 7, 'Mantenimiento', '2022-07-08', 2080.6);
INSERT INTO public.bills (bll_htl_id, bll_dpt_id, bll_concept, bll_date, bll_amount) VALUES (6, 7, 'Mantenimiento', '2022-01-08', 1770);
INSERT INTO public.bills (bll_htl_id, bll_dpt_id, bll_concept, bll_date, bll_amount) VALUES (6, 7, 'Mantenimiento', '2022-05-08', 195.5);
INSERT INTO public.bills (bll_htl_id, bll_dpt_id, bll_concept, bll_date, bll_amount) VALUES (6, 7, 'Mantenimiento', '2022-07-08', 130);
----RRHH
INSERT INTO public.bills (bll_htl_id, bll_dpt_id, bll_concept, bll_date, bll_amount) VALUES (4, 3, 'RRHH', '2022-06-08', 16000);
INSERT INTO public.bills (bll_htl_id, bll_dpt_id, bll_concept, bll_date, bll_amount) VALUES (4, 3, 'RRHH', '2022-07-08', 18000.56);
INSERT INTO public.bills (bll_htl_id, bll_dpt_id, bll_concept, bll_date, bll_amount) VALUES (4, 3, 'RRHH', '2022-01-08', 16700);
INSERT INTO public.bills (bll_htl_id, bll_dpt_id, bll_concept, bll_date, bll_amount) VALUES (5, 3, 'RRHH', '2022-06-08', 15000);
INSERT INTO public.bills (bll_htl_id, bll_dpt_id, bll_concept, bll_date, bll_amount) VALUES (5, 3, 'RRHH', '2022-07-08', 20800.6);
INSERT INTO public.bills (bll_htl_id, bll_dpt_id, bll_concept, bll_date, bll_amount) VALUES (6, 3, 'RRHH', '2022-01-08', 17700);
INSERT INTO public.bills (bll_htl_id, bll_dpt_id, bll_concept, bll_date, bll_amount) VALUES (6, 3, 'RRHH', '2022-05-08', 19500.5);
INSERT INTO public.bills (bll_htl_id, bll_dpt_id, bll_concept, bll_date, bll_amount) VALUES (6, 3, 'RRHH', '2022-07-08', 21000);
----Employees
INSERT INTO employees (emp_name, emp_surname, emp_identity_document, emp_social_security_number, emp_salary, emp_phone_number, emp_account_number, emp_email, emp_city, emp_state, emp_address, emp_zip_code, emp_dpt_id, emp_htl_id, emp_cnt_iso, emp_hiring, emp_fired) VALUES('Armando', 'Castro Blanco', '12345678Z', '25646464646665', 1800.35, '667880938', '3697/5656/8856/4100', 'armadocastro@atomic.es', 'Vigo', 'Pontevedra', 'Bueu', '89614', 1, 1, 'ES', '2022-08-08 00:00:00.000', NULL);
INSERT INTO employees (emp_name, emp_surname, emp_identity_document, emp_social_security_number, emp_salary, emp_phone_number, emp_account_number, emp_email, emp_city, emp_state, emp_address, emp_zip_code, emp_dpt_id, emp_htl_id, emp_cnt_iso, emp_hiring, emp_fired) VALUES('Cesar', 'Bouzas Soto', '34896415x', '00001010010010', 9999.99, '667880938', '3491/5465/8856/4151', 'cesarbouzas@atomic.es', 'OLEIROS', 'A Coruna', 'Oleiros', '15179', 2, 1, 'ES', '2022-08-08 00:00:00.000', NULL);
INSERT INTO employees (emp_name, emp_surname, emp_identity_document, emp_social_security_number, emp_salary, emp_phone_number, emp_account_number, emp_email, emp_city, emp_state, emp_address, emp_zip_code, emp_dpt_id, emp_htl_id, emp_cnt_iso, emp_hiring, emp_fired) VALUES('Estefania', 'Penide Casanova', '12345701Z', '124567899984', 1800.35, '5689741415', '3456/5656/8856/6101', 'estefaniapenide@atomic.es', 'Vigo', 'Pontevedra', 'Av de la Coruña', '15412', 3, 1, 'ES', '2022-08-08 00:00:00.000', NULL);
INSERT INTO employees (emp_name, emp_surname, emp_identity_document, emp_social_security_number, emp_salary, emp_phone_number, emp_account_number, emp_email, emp_city, emp_state, emp_address, emp_zip_code, emp_dpt_id, emp_htl_id, emp_cnt_iso, emp_hiring, emp_fired) VALUES('Alejandro', 'Pernas Seoane', '12345724Z', '12453478999', 1800.35, '558996546', '3456/9999/9999/9999', 'alexPernsa@atomic.es', 'A Coruña', 'A Coruña', 'Los Castros', '15412', 4, 1, 'ES', '2022-08-08 00:00:00.000', NULL);
INSERT INTO employees (emp_name, emp_surname, emp_identity_document, emp_social_security_number, emp_salary, emp_phone_number, emp_account_number, emp_email, emp_city, emp_state, emp_address, emp_zip_code, emp_dpt_id, emp_htl_id, emp_cnt_iso, emp_hiring, emp_fired) VALUES('Tomas', 'Gil Lage', '12345747Z', '1245348777', 1800.35, '214578955', '3456/8888/8888/8888', 'tomasgil@atomic.es', 'Viveiro', 'A Coruña', 'Calle de la playa', '14789', 5, 1, 'ES', '2022-08-08 00:00:00.000', NULL);
INSERT INTO employees (emp_name, emp_surname, emp_identity_document, emp_social_security_number, emp_salary, emp_phone_number, emp_account_number, emp_email, emp_city, emp_state, emp_address, emp_zip_code, emp_dpt_id, emp_htl_id, emp_cnt_iso, emp_hiring, emp_fired) VALUES('Lorena', 'Sobral Lemos', '12345770Z', '1245348666', 2100.99, '988541456', '3456/6666/6666/6666', 'lorenasobral@atomic.es', 'Ponte caldelas', 'Pontevedra', 'Calle del codigo secreto', '14145', 6, 1, 'ES', '2022-08-08 00:00:00.000', NULL);
INSERT INTO employees (emp_name, emp_surname, emp_identity_document, emp_social_security_number, emp_salary, emp_phone_number, emp_account_number, emp_email, emp_city, emp_state, emp_address, emp_zip_code, emp_dpt_id, emp_htl_id, emp_cnt_iso, emp_hiring, emp_fired) VALUES('Hugo', 'Marchose del Curso', '12345793Z', '00000001', 1000.99, '55555555', '5555/5555/5555/5555', 'hugomarchose@atomic.es', 'Mañon', 'Ourense', 'Calle cámara del comercio apagada', '17896', 7, 1, 'ES', '2022-08-08 00:00:00.000', NULL);



