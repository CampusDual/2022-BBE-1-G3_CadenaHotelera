-- Drop table
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

