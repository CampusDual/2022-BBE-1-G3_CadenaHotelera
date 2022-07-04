--SERVICES:

INSERT INTO public.services (srv_name, srv_description) VALUES ('Piscina infantil de bolas', 'impermeable, segura, con vigilancia, estancia perfecta para que los niños se diviertan');
INSERT INTO public.services (srv_name, srv_description) VALUES ('Piscina infantil de natación', 'Pequeña piscina con poca profundidad ideal para enseñar a los más pequeños a nadar');
INSERT INTO public.services (srv_name, srv_description) VALUES ('Gimnasio', 'Diferentes máquinas para disfrutar entrenando y te ponerse en forma');
INSERT INTO public.services (srv_name, srv_description) VALUES ('Actuación de magia', 'Ideal para parejas mayores');
INSERT INTO public.services (srv_name, srv_description) VALUES ('Monitores de tiempo libre', 'Para niños o maridos aburridos');
INSERT INTO public.services (srv_name, srv_description) VALUES ('Zona aquapark', 'Diferentes toboganes y recorridos en "donuts", para la diversión de toda la familia');
INSERT INTO public.services (srv_name, srv_description) VALUES ('Música en vivo', 'Conciertos de jóvenes talentos locales para amenizar una estudpenda velada');
INSERT INTO public.services (srv_name, srv_description) VALUES ('Pistas de pádel', '2 pistas 2vs2, y una 1vs1. Organización de torneos.');
INSERT INTO public.services (srv_name, srv_description) VALUES ('Pistas de tenis', 'Magníficas pistas de hierba y tierra para desestresarte y tonificarte.');
INSERT INTO public.services (srv_name, srv_description) VALUES ('Cancha de baloncesto', 'Pista exterior de baloncesto, que cuenta con las medidas oficiales.');
INSERT INTO public.services (srv_name, srv_description) VALUES ('Pista de futsal', 'Pista cubierta interior para jugar al futbito o al balonmano. Cuenta con las medidas oficiales.');

--HOTEL SERVICES:

INSERT INTO public.hotels_services (htl_id, srv_id) VALUES (1, 1 );
INSERT INTO public.hotels_services (htl_id, srv_id) VALUES (1, 3 );
INSERT INTO public.hotels_services (htl_id, srv_id) VALUES (1, 4 );
INSERT INTO public.hotels_services (htl_id, srv_id) VALUES (1, 7 );
INSERT INTO public.hotels_services (htl_id, srv_id) VALUES (1, 9 );
INSERT INTO public.hotels_services (htl_id, srv_id) VALUES (2, 6 );
INSERT INTO public.hotels_services (htl_id, srv_id) VALUES (2, 8 );
INSERT INTO public.hotels_services (htl_id, srv_id) VALUES (2, 11 );
INSERT INTO public.hotels_services (htl_id, srv_id) VALUES (3, 1 );
INSERT INTO public.hotels_services (htl_id, srv_id) VALUES (3, 3 );
INSERT INTO public.hotels_services (htl_id, srv_id) VALUES (3, 4 );
INSERT INTO public.hotels_services (htl_id, srv_id) VALUES (3, 7 );
INSERT INTO public.hotels_services (htl_id, srv_id) VALUES (3, 9 );
INSERT INTO public.hotels_services (htl_id, srv_id) VALUES (4, 8 );
INSERT INTO public.hotels_services (htl_id, srv_id) VALUES (4, 9 );
INSERT INTO public.hotels_services (htl_id, srv_id) VALUES (4, 11 );
INSERT INTO public.hotels_services (htl_id, srv_id) VALUES (4, 15 );
INSERT INTO public.hotels_services (htl_id, srv_id) VALUES (5, 6 );
INSERT INTO public.hotels_services (htl_id, srv_id) VALUES (5, 7 );
INSERT INTO public.hotels_services (htl_id, srv_id) VALUES (5, 8 );
INSERT INTO public.hotels_services (htl_id, srv_id) VALUES (5, 9 );
INSERT INTO public.hotels_services (htl_id, srv_id) VALUES (6, 3 );
INSERT INTO public.hotels_services (htl_id, srv_id) VALUES (6, 6 );
INSERT INTO public.hotels_services (htl_id, srv_id) VALUES (6, 7 );
INSERT INTO public.hotels_services (htl_id, srv_id) VALUES (7, 8 );
INSERT INTO public.hotels_services (htl_id, srv_id) VALUES (7, 9 );
INSERT INTO public.hotels_services (htl_id, srv_id) VALUES (7, 10 );
INSERT INTO public.hotels_services (htl_id, srv_id) VALUES (7, 13 );
INSERT INTO public.hotels_services (htl_id, srv_id) VALUES (8, 1 );
INSERT INTO public.hotels_services (htl_id, srv_id) VALUES (8, 3 );
INSERT INTO public.hotels_services (htl_id, srv_id) VALUES (8, 4 );
INSERT INTO public.hotels_services (htl_id, srv_id) VALUES (8, 8 );
INSERT INTO public.hotels_services (htl_id, srv_id) VALUES (9, 1 );
INSERT INTO public.hotels_services (htl_id, srv_id) VALUES (9, 4 );
INSERT INTO public.hotels_services (htl_id, srv_id) VALUES (9, 6 );
INSERT INTO public.hotels_services (htl_id, srv_id) VALUES (9, 8 );
INSERT INTO public.hotels_services (htl_id, srv_id) VALUES (9, 9 );
INSERT INTO public.hotels_services (htl_id, srv_id) VALUES (9, 10 );
INSERT INTO public.hotels_services (htl_id, srv_id) VALUES (9, 12 );
INSERT INTO public.hotels_services (htl_id, srv_id) VALUES (10, 7 );
INSERT INTO public.hotels_services (htl_id, srv_id) VALUES (10, 8 );
INSERT INTO public.hotels_services (htl_id, srv_id) VALUES (10, 10 );
INSERT INTO public.hotels_services (htl_id, srv_id) VALUES (10, 11 );
INSERT INTO public.hotels_services (htl_id, srv_id) VALUES (11, 1 );
INSERT INTO public.hotels_services (htl_id, srv_id) VALUES (11, 8 );
INSERT INTO public.hotels_services (htl_id, srv_id) VALUES (11, 10 );
INSERT INTO public.hotels_services (htl_id, srv_id) VALUES (11, 11 );
INSERT INTO public.hotels_services (htl_id, srv_id) VALUES (12, 1 );
INSERT INTO public.hotels_services (htl_id, srv_id) VALUES (12, 4 );
INSERT INTO public.hotels_services (htl_id, srv_id) VALUES (12, 8 );
INSERT INTO public.hotels_services (htl_id, srv_id) VALUES (12, 9 );
INSERT INTO public.hotels_services (htl_id, srv_id) VALUES (12, 11 );
INSERT INTO public.hotels_services (htl_id, srv_id) VALUES (13, 1 );
INSERT INTO public.hotels_services (htl_id, srv_id) VALUES (13, 13 );
INSERT INTO public.hotels_services (htl_id, srv_id) VALUES (13, 14 );
INSERT INTO public.hotels_services (htl_id, srv_id) VALUES (13, 15 );
INSERT INTO public.hotels_services (htl_id, srv_id) VALUES (13, 16 );


--TABLAS CUSTOMERS:
-- Surnames en singular
--dni, cambiar y poner documento identidad, acreditativo...
--aumentar dígitos de credit card, y pensar si se ponen los espacios a la hora de ingresar  los datos
--Última columna, credita card, no llevaría día

--CUSTOMERS
INSERT INTO public.customers (cst_name, cst_surnames, cst_email, cst_birth_date, cst_dni, cst_address, cst_nationality,cst_phone, cst_creditcard, cst_valid_date) VALUES ('John','Smith', 'johnSmth123@gmail.com', '01/22/1980', '12345678A', '10, Downing Street, London, SWIA 2AA, United Kingdom', 'Inglés', '+44 07987654321', '4321987650129900', '01/10/22' );
INSERT INTO public.customers (cst_name, cst_surnames, cst_email, cst_birth_date, cst_dni, cst_address, cst_nationality,cst_phone, cst_creditcard, cst_valid_date) VALUES ('Nayib','Bukele', 'naybuk@gmail.com', '06/24/1981', '023773088', '4A Calle Poniente, San Salvador, El Salvador', 'Salvadoreño', '+503 21130481', '3455687650129900', '01/12/24' );
INSERT INTO public.customers (cst_name, cst_surnames, cst_email, cst_birth_date, cst_dni, cst_address, cst_nationality,cst_phone, cst_creditcard, cst_valid_date) VALUES ('Diego','Guerra Goya', 'guerrita78@gmail.com', '03/20/1978', '23898620A', 'Av.Oza, num 3, 4Izq, A Coruña, ', 'Español', '+34 636443658', '9221678950129455', '01/08/23' );
INSERT INTO public.customers (cst_name, cst_surnames, cst_email, cst_birth_date, cst_dni, cst_address, cst_nationality,cst_phone, cst_creditcard, cst_valid_date) VALUES ('Ana','López Estrada', 'anitadinamita@terra.es', '11/11/1984', '98765432A', 'Av.Porto, num78, 7, Vigo, Pontevedra, España', 'Española', '+34 07987654321', '4321987650129900', '01/10/22' );
INSERT INTO public.customers (cst_name, cst_surnames, cst_email, cst_birth_date, cst_dni, cst_address, cst_nationality,cst_phone, cst_creditcard, cst_valid_date) VALUES ('Adam','Smith', 'capital4life@outlook.com', '10/27/1953', '12876543A', 'Edimburgo, Escocia. Dirección, Charlotte Square,6', 'Escocés', '+44 7700123456', '6781987633429900', '01/1/26' );
INSERT INTO public.customers (cst_name, cst_surnames, cst_email, cst_birth_date, cst_dni, cst_address, cst_nationality,cst_phone, cst_creditcard, cst_valid_date) VALUES ('Elder','Santos', 'elder3@gmail.com', '01/22/1960', '14775678A', 'Rua nova, 56, 3Der, Oporto, Portugal', 'Portugués', '+351 0798765431', '5621987650129222', '01/02/26' );
INSERT INTO public.customers (cst_name, cst_surnames, cst_email, cst_birth_date, cst_dni, cst_address, cst_nationality,cst_phone, cst_creditcard, cst_valid_date) VALUES ('Paloma','Solana Torres', 'Patomareado@hotmail.com', '12/20/2000', '78345978A', 'Calle Ópalo, num5, 6, Madrid, España', 'Española', '+34 78987994321', '9998987650129900', '01/09/27' );
INSERT INTO public.customers (cst_name, cst_surnames, cst_email, cst_birth_date, cst_dni, cst_address, cst_nationality,cst_phone, cst_creditcard, cst_valid_date) VALUES ('Anatoli','Karpov', 'jaquealrey@gmail.com', '01/23/1959', '77845678A', 'Calle Krúglaia, 67, 8, Moscow, Russia', 'Ruso', '+812 6336765431', '9898987650129900', '01/01/28' );
INSERT INTO public.customers (cst_name, cst_surnames, cst_email, cst_birth_date, cst_dni, cst_address, cst_nationality,cst_phone, cst_creditcard, cst_valid_date) VALUES ('Antonio','Castilla López', 'antopez@gmail.com', '01/11/1999', '44346678A', 'Plaza real, 34, 5I, Zaragoza, España', 'Español', '+34 96387654321', '5561987666729900', '01/11/24' );
INSERT INTO public.customers (cst_name, cst_surnames, cst_email, cst_birth_date, cst_dni, cst_address, cst_nationality,cst_phone, cst_creditcard, cst_valid_date) VALUES ('Paco','Soria Lobo', 'pacobo@gmail.com', '04/12/1977', '44645678A', 'Av artabria, num56, 3, Murcia, España', 'Español', '+34 34567654321', '9871987650129900', '01/07/27' );
INSERT INTO public.customers (cst_name, cst_surnames, cst_email, cst_birth_date, cst_dni, cst_address, cst_nationality,cst_phone, cst_creditcard, cst_valid_date) VALUES ('Juana','Prudant', 'juapru@gmail.com', '09/17/1967', '67845678A', 'Calle lobo, num45, 4Der, Toledo, España', 'Español', '+34 07987654321', '4556787650129900', '01/11/25' );
INSERT INTO public.customers (cst_name, cst_surnames, cst_email, cst_birth_date, cst_dni, cst_address, cst_nationality,cst_phone, cst_creditcard, cst_valid_date) VALUES ('Julia','Ondas', 'jul345@gmail.com', '05/05/1980', '78945678A', 'Calle caramelo, num56, 6, La Puebla, México', 'Mexicana', '  +52 4831212891', '8865987650129900', '01/12/26' );