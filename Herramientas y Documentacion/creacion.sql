--BG-109,45 
--DROP TABLE hotels;
CREATE TABLE hotels ( 
	htl_id INTEGER GENERATED BY DEFAULT AS IDENTITY(START WITH 1) NOT NULL PRIMARY KEY,
	htl_name VARCHAR(255) UNIQUE NOT NULL,
	htl_street VARCHAR(255) NOT NULL,
	htl_city VARCHAR(255) NOT NULL,
	htl_postal_code VARCHAR(255) NOT NULL,
	htl_state VARCHAR(255) NOT NULL,
	htl_cnt_iso VARCHAR(255) NOT NULL,

	htl_phone VARCHAR(255),
	htl_email VARCHAR(255),
	htl_description TEXT,
	htl_is_open int2 DEFAULT 0
	
	FOREIGN KEY(htl_cnt_iso) REFERENCES countries(cnt_iso)	
);
--ALTER TABLE public.hotels ADD htl_lat VARCHAR(15) NULL;
--ALTER TABLE public.hotels ADD htl_lon VARCHAR(15) NULL;
--ALTER TABLE public.hotels ADD htl_cnt_iso char(2)  NULL;
--ALTER TABLE public.hotels ADD FOREIGN KEY(htl_cnt_iso) REFERENCES countries(cnt_iso)	;
--UPDATE public.hotels  set htl_cnt_iso = 'ES'
--ALTER TABLE public.hotels ALTER COLUMN htl_cnt_iso SET NOT NULL;

--BG-119
--DROP TABLE public.features;
CREATE TABLE public.features (
	ftr_id int4 GENERATED BY DEFAULT AS IDENTITY(START WITH 1) NOT NULL PRIMARY KEY,
	ftr_name varchar(255) UNIQUE NOT NULL,
	ftr_description varchar(255)
);


--BG-121
--DROP TABLE beds_combo;
CREATE TABLE beds_combo(
	bdc_id int4 GENERATED BY DEFAULT AS IDENTITY(START WITH 1) NOT NULL PRIMARY KEY,
	bdc_name varchar(255) NOT NULL UNIQUE,
	bdc_slots int2 NOT NULL
);

--DROP TABLE room_types;
CREATE TABLE room_types (
	rmt_id int4 GENERATED BY DEFAULT AS IDENTITY(START WITH 1) NOT NULL PRIMARY KEY,
	rmt_name varchar(255) UNIQUE NOT NULL,	
	rmt_description varchar(255),
	rmt_price NUMERIC(7,2) NOT NULL DEFAULT 0,
	rmt_bdc_id int4 NOT NULL,
	CONSTRAINT fk_rmt_bdc_id FOREIGN KEY(rmt_bdc_id) REFERENCES beds_combo(bdc_id)
);

--DROP TABLE room_types_features;
CREATE TABLE room_types_features (
	rmt_id int4,
	ftr_id int4,
	CONSTRAINT fk_rmt_id FOREIGN KEY(rmt_id) REFERENCES room_types(rmt_id),
	CONSTRAINT fk_ftr_id FOREIGN KEY(ftr_id) REFERENCES features(ftr_id),
	PRIMARY KEY (rmt_id,ftr_id)
);

-- BG- 111
--DROP TABLE public.rooms;
CREATE TABLE public.rooms (
	rm_id int4 GENERATED BY DEFAULT AS IDENTITY(START WITH 1) NOT NULL PRIMARY KEY,
	rm_htl_id int4 NOT NULL,
	rm_number int4 NOT NULL,
	rm_square_meters int4,
	rm_rmt_id int4,
	rm_status int2 DEFAULT 0,
	UNIQUE (rm_htl_id, rm_number),
	FOREIGN KEY (rm_htl_id) REFERENCES hotels(htl_id),
	FOREIGN KEY (rm_rmt_id) REFERENCES room_types(rmt_id)
);

--DROP TABLE public.customers;
--INSERTAR COLUMNA mailAgreement 
/*CREATE TABLE public.customers (
	cst_id int4 GENERATED BY DEFAULT AS IDENTITY(START WITH 1) NOT NULL PRIMARY KEY,
	cst_name varchar(255) NOT NULL,
	--cst_surnames varchar(255) NOT NULL,
	cst_surname varchar(255) NOT NULL,
	cst_docId varchar(20) NOT NULL UNIQUE ,
	--cst_identity_document varchar(9) NOT NULL UNIQUE ,
	cst_email varchar(50),
	cst_birth_date date,
	cst_address varchar(255),
	cst_nationality varchar(100) NOT NULL,
	cst_phone varchar(15) NOT NULL,
	cst_creditcard varchar(16) NOT NULL,
	cst_valid_date date NOT NULL,
	--cst_registration_date DATE DEFAULT CURRENT_DATE
);*/


ALTER TABLE public.customers ADD cst_cnt_iso char(2)  NULL;
ALTER TABLE public.customers ADD FOREIGN KEY(cst_cnt_iso) REFERENCES countries(cnt_iso);
UPDATE public.customers  set cst_cnt_iso = 'ES';
ALTER TABLE public.customers ALTER COLUMN cst_cnt_iso SET NOT NULL;
ALTER TABLE public.customers RENAME COLUMN cst_surnames TO cst_surname;

ALTER TABLE public.customers ADD cst_identity_document varchar(20);
ALTER TABLE public.customers ADD cst_vat_number varchar(20);

UPDATE public.customers set cst_identity_document = cst_dni;

ALTER TABLE public.customers ADD cst_city varchar(255);
ALTER TABLE public.customers ADD cst_state varchar(255);
ALTER TABLE public.customers ADD cst_canceled timestamp ;
ALTER TABLE public.customers ADD cst_agree_spam int2 DEFAULT 0 ;
ALTER TABLE public.customers ADD cst_address varchar(255);
ALTER TABLE public.customers ADD cst_zip_code varchar(255);
UPDATE public.customers set cst_agree_spam = (case when mailagreement then 1 else 0 end );

ALTER TABLE public.customers DROP COLUMN  cst_dni;
ALTER TABLE public.customers DROP COLUMN  cst_nationality;
ALTER TABLE public.customers DROP COLUMN  cst_creditcard;
ALTER TABLE public.customers DROP COLUMN  cst_valid_date;
ALTER TABLE public.customers DROP COLUMN  mailagreement;


-- BG-141 -- Tabla pendiente de añadir, reemplazando a la anterior
CREATE TABLE public.customers (
	cst_id int4 GENERATED BY DEFAULT AS IDENTITY(START WITH 1) NOT NULL PRIMARY KEY,
	cst_identity_document varchar(20),
	cst_vat_number varchar(20),
	cst_name varchar(255) NOT NULL,
	cst_surname varchar(255),
	cst_address varchar(255),
	cst_birth_date date,	
	cst_city varchar(255),
	cst_state varchar(255),	
	cst_zip_code varchar(255),	
	cst_cnt_iso char(2) NOT NULL,
	cst_phone varchar(15) NOT NULL,
	cst_email varchar(50) NOT NULL,	
	cst_agree_spam int2 DEFAULT 0 NOT NULL,
	cst_canceled timestamp,
	cst_created timestamp default now() NOT NULL 

	FOREIGN KEY(cst_cnt_iso) REFERENCES countries(cnt_iso)	

);



-- BG- 117
--DROP TABLE public.status_booking;
--CREATE TABLE status_booking (
-- stb_id INTEGER GENERATED BY DEFAULT AS IDENTITY(START WITH 1) NOT NULL PRIMARY KEY,
-- stb_name varchar(255) NOT NULL 
--);

--DROP TABLE bookings;
CREATE TABLE bookings (
	bkg_id INTEGER GENERATED BY DEFAULT AS IDENTITY(START WITH 1) NOT NULL PRIMARY KEY,
	bkg_cst_id int4 NOT NULL, 
	bkg_rm_id int4 NOT NULL,
	bkg_observations varchar(255),
	bkg_start date NOT NULL,
	bkg_end date NOT NULL,
	bkg_checkin timestamp,
	bkg_checkout timestamp,
	bkg_canceled timestamp,
	bkg_created timestamp DEFAULT current_timestamp NOT NULL,
	FOREIGN KEY(bkg_cst_id) REFERENCES customers(cst_id),
	FOREIGN KEY(bkg_rm_id) REFERENCES rooms(rm_id)
);


-- BG- 56
--DROP TABLE public.services;
CREATE TABLE public.services (
srv_id int4 PRIMARY KEY NOT NULL GENERATED BY DEFAULT AS IDENTITY (START WITH 1),
srv_name varchar(255) NOT NULL ,
srv_description varchar(255)
);
ALTER TABLE public.SERVICES  add constraint UN_name unique (srv_name) ;

--DROP TABLE public.hotels_services;
CREATE TABLE public.hotels_services (
	htl_id int4 NOT NULL,
	srv_id int4 NOT NULL,
	CONSTRAINT hotels_services_pkey PRIMARY KEY (htl_id, srv_id)
);


-- public.hotels_services foreign keys
ALTER TABLE public.hotels_services ADD CONSTRAINT fk_htl_id FOREIGN KEY (htl_id) REFERENCES public.hotels(htl_id);
ALTER TABLE public.hotels_services ADD CONSTRAINT fk_srv_id FOREIGN KEY (srv_id) REFERENCES public.services(srv_id);


--BG-130 
--DROP table servicesXtra
CREATE TABLE servicesXtra ( 
	sxt_id INTEGER GENERATED BY DEFAULT AS IDENTITY(START WITH 1) NOT NULL PRIMARY KEY,
	sxt_name VARCHAR(255) UNIQUE NOT NULL,
	sxt_description varchar(255)
);

--BG - 138
-- DROP TABLE public.receipts;
CREATE TABLE public.receipts(
rcp_id INTEGER GENERATED BY DEFAULT AS IDENTITY(START WITH 1) NOT NULL PRIMARY KEY,
rcp_date timestamp DEFAULT current_timestamp NOT NULL,
rcp_bkg_id INTEGER NOT NULL,
rcp_total_services NUMERIC(7,2),
rcp_days INTEGER,
rcp_total_room NUMERIC(7,2),
rcp_total NUMERIC(7,2),
FOREIGN KEY(rcp_bkg_id) REFERENCES bookings(bkg_id)
UNIQUE(rcp_bkg_id)
);

--BG - 144
--DROP TABLE countries;
CREATE TABLE countries (
--	cnt_id INTEGER GENERATED BY DEFAULT AS IDENTITY(START WITH 1) NOT NULL PRIMARY KEY,
	cnt_iso char(2) primary key NOT NULL,
	cnt_name varchar(80) NOT null	
);

--DROP TABLE public.hotels_services_extra;
CREATE TABLE public.hotels_services_extra(
	hsx_id INTEGER GENERATED BY DEFAULT AS IDENTITY(START WITH 1) NOT NULL PRIMARY KEY,
	hsx_htl_id int4 NOT NULL,
	hsx_sxt_id int4 NOT NULL,
	hsx_precio numeric(7,2) not null, 
	FOREIGN KEY (hsx_htl_id) REFERENCES public.hotels(htl_id),
	FOREIGN KEY (hsx_sxt_id) REFERENCES public.servicesxtra(sxt_id),
	unique (hsx_htl_id, hsx_sxt_id)
);

--BG - 151

-- DROP TABLE public.creditcard;

CREATE TABLE public.creditcard (
	crd_id int4 NOT NULL GENERATED BY DEFAULT AS IDENTITY,
	crd_number numeric(16) NOT NULL,
	crd_date_expiry date NOT NULL,
	CONSTRAINT creditcard_crd_number_key UNIQUE (crd_number),
	CONSTRAINT creditcard_pkey PRIMARY KEY (crd_id)
);

-- DROP TABLE public.customers_creditcard;

CREATE TABLE public.customers_creditcard (
	cst_id int4 NOT NULL,
	crd_id int4 NOT NULL,
	CONSTRAINT customers_creditcard_crd_id_key UNIQUE (crd_id)
);


-- public.customers_creditcard foreign keys

ALTER TABLE public.customers_creditcard ADD CONSTRAINT customers_creditcard_crd_id_fkey FOREIGN KEY (crd_id) REFERENCES public.creditcard(crd_id);
ALTER TABLE public.customers_creditcard ADD CONSTRAINT customers_creditcard_cst_id_fkey FOREIGN KEY (cst_id) REFERENCES public.customers(cst_id);

--DROP TABLE public.bookings_services_extra;
CREATE TABLE public.bookings_services_extra(
	bsx_id INTEGER GENERATED BY DEFAULT AS IDENTITY(START WITH 1) NOT NULL PRIMARY KEY,
	bsx_bkg_id int4 NOT NULL,
	bsx_sxt_id int4 NOT NULL,
	bsx_units int4 not null,
	bsx_precio numeric(7,2) not null, 
	bsx_date timestamp DEFAULT current_timestamp NOT NULL, 
	FOREIGN KEY (bsx_sxt_id) REFERENCES public.servicesxtra(sxt_id),
	FOREIGN KEY (bsx_bkg_id) REFERENCES public.bookings(bkg_id)
);


--BG - 140
--DROP TABLE bookings_guests;
CREATE TABLE bookings_guests(
	bgs_id INTEGER GENERATED BY DEFAULT AS IDENTITY(START WITH 1) NOT NULL PRIMARY KEY,
	bgs_bkg_id INTEGER NOT NULL,
	bgs_cst_id INTEGER NOT NULL,
	bgs_registration_date TIMESTAMP DEFAULT current_timestamp NOT NULL, 
	FOREIGN KEY(bgs_bkg_id) REFERENCES bookings(bkg_id),
	FOREIGN KEY(bgs_cst_id) REFERENCES customers(cst_id),
	UNIQUE(bgs_bkg_id,bgs_cst_id)
);

--BG - 112
-- public.employees definition

-- Drop table

-- DROP TABLE public.employees;

CREATE TABLE public.employees (
	emp_id int4 NOT NULL GENERATED BY DEFAULT AS IDENTITY (START with 1) PRIMARY KEY,
	emp_name varchar(255) NOT NULL,
	emp_surname varchar(255) NULL,
	emp_identity_document varchar(20) NOT NULL,
	emp_social_security_number varchar(10) NOT NULL,
	emp_salary numeric(6) NOT NULL,
	emp_phone_number varchar(15) NOT NULL,
	emp_account_number varchar(20) NOT NULL,
	emp_email varchar(50) NULL,
	emp_city varchar(255) NULL,
	emp_state varchar(255) NULL,
	emp_address varchar(255) NULL,
	emp_zip_code varchar(255) NULL,
	emp_dpt_id int4 NOT NULL,
	emp_htl_id int4 NOT NULL,
	emp_cnt_iso bpchar(2) NOT NULL,
	emp_hiring timestamp NOT NULL,
	emp_fired timestamp NULL,
	CONSTRAINT employees_emp_dpt_id_emp_htl_id_key UNIQUE (emp_dpt_id, emp_htl_id) 
);


-- public.employees foreign keys

ALTER TABLE public.employees ADD CONSTRAINT employees_emp_cnt_iso_fkey FOREIGN KEY (emp_cnt_iso) REFERENCES public.countries(cnt_iso);
ALTER TABLE public.employees ADD CONSTRAINT employees_emp_dpt_id_fkey FOREIGN KEY (emp_dpt_id) REFERENCES public.departments(dpt_id);
ALTER TABLE public.employees ADD CONSTRAINT employees_emp_htl_id_fkey FOREIGN KEY (emp_htl_id) REFERENCES public.hotels(htl_id);


--BG 161
--DROP table departments
CREATE TABLE departments ( 
	dpt_id INTEGER GENERATED BY DEFAULT AS IDENTITY(START WITH 1) NOT NULL PRIMARY KEY,
	dpt_name VARCHAR(255) UNIQUE NOT NULL,
	dpt_description varchar(255)
);


--BG 162
--DROP table bills
CREATE TABLE bills ( 
	bll_id INTEGER GENERATED BY DEFAULT AS IDENTITY(START WITH 1) NOT NULL PRIMARY KEY,
	bll_htl_id int4 NOT null,
	bll_dpt_id int4 NOT NULL,
	bll_concept VARCHAR(255) NOT NULL,
	bll_date timestamp NOT NULL,
	bll_amount NUMERIC(7,2) NOT NULL DEFAULT 0,
	FOREIGN KEY (bll_htl_id) REFERENCES public.hotels(htl_id),
	FOREIGN KEY (bll_dpt_id) REFERENCES public.departments(dpt_id)
);