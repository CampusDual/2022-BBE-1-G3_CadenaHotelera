--BG-109,45 
--DROP TABLE hotels;
CREATE TABLE hotels ( 
	htl_id INTEGER GENERATED BY DEFAULT AS IDENTITY(START WITH 1) NOT NULL PRIMARY KEY,
	htl_name VARCHAR(255) UNIQUE NOT NULL,
	htl_street VARCHAR(255) NOT NULL,
	htl_city VARCHAR(255) NOT NULL,
	htl_postal_code VARCHAR(255) NOT NULL,
	htl_state VARCHAR(255) NOT NULL,
	htl_country VARCHAR(255) NOT NULL,
	htl_phone VARCHAR(255),
	htl_email VARCHAR(255),
	htl_description TEXT,
	htl_is_open int2 DEFAULT 0
);

--BG-119
--DROP TABLE public.features;
CREATE TABLE public.features (
	ftr_id int4 GENERATED BY DEFAULT AS IDENTITY(START WITH 1) NOT NULL PRIMARY KEY,
	ftr_name varchar(255) UNIQUE NOT NULL,
	ftr_description varchar(255) NOT NULL
);


--BG-121
-- DROP TABLE beds_combo
CREATE TABLE beds_combo(
	bdc_id int4 GENERATED BY DEFAULT AS IDENTITY(START WITH 1) NOT NULL PRIMARY KEY,
	bdc_name varchar(255) NOT NULL,
	bdc_slots int2 NOT NULL
);

-- DROP TABLE room_types;
CREATE TABLE room_types (
	rmt_id int4 GENERATED BY DEFAULT AS IDENTITY(START WITH 1) NOT NULL PRIMARY KEY,
	rmt_name varchar(255) NOT NULL,	
	rmt_description varchar(255) NOT NULL,
	rmt_price NUMERIC(7,2) NOT NULL DEFAULT 0,
	rmt_htl_id int4 NOT NULL,
	rmt_bdc_id int4 NOT NULL,
	CONSTRAINT fk_rmt_htl_id FOREIGN KEY(rmt_htl_id) REFERENCES hotels(htl_id)
	CONSTRAINT fk_rmt_bdc_id FOREIGN KEY(rmt_bdc_id) REFERENCES beds_combo(bdc_id)
);

-- DROP TABLE room_types_features;
CREATE TABLE room_types_features (
	rmt_id int4,
	ftr_id int4,
	CONSTRAINT fk_rmt_id FOREIGN KEY(rmt_id) REFERENCES room_types(rmt_id),
	CONSTRAINT fk_ftr_id FOREIGN KEY(ftr_id) REFERENCES features(ftr_id),
	PRIMARY KEY (rmt_id,ftr_id)
);


