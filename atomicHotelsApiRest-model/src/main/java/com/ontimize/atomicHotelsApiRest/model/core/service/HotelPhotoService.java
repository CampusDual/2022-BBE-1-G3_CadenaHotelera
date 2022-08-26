package com.ontimize.atomicHotelsApiRest.model.core.service;

import java.awt.image.BufferedImage;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.*;

import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Optional;

import javax.annotation.processing.Filer;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import org.apache.poi.ss.util.ImageUtils;
import org.postgresql.core.Encoding;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import com.ontimize.atomicHotelsApiRest.api.core.service.IHotelPhotoService;
import com.ontimize.atomicHotelsApiRest.model.core.dao.HotelDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.HotelPhotoDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.QuestionDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.UserRoleDao;
import com.ontimize.atomicHotelsApiRest.model.core.tools.ControlFields;
import com.ontimize.atomicHotelsApiRest.model.core.tools.EntityResultWrong;
import com.ontimize.atomicHotelsApiRest.model.core.tools.ErrorMessage;
import com.ontimize.atomicHotelsApiRest.model.core.tools.TypeCodes.type;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.exceptions.OntimizeJEERuntimeException;
import com.ontimize.jee.common.security.PermissionsProviderSecured;
import com.ontimize.jee.common.tools.EntityResultTools;
import com.ontimize.jee.common.util.remote.BytesBlock;
import com.lowagie.text.Image;
import com.ontimize.atomicHotelsApiRest.api.core.exceptions.EntityResultRequiredException;
import com.ontimize.atomicHotelsApiRest.api.core.exceptions.ValidateException;
import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;

@Service("HotelPhotoService")
@Lazy

public class HotelPhotoService implements IHotelPhotoService {

	@Autowired
	private HotelPhotoDao dao;

	@Autowired
	private HotelService hs;
	@Autowired
	private DefaultOntimizeDaoHelper daoHelper;
	@Autowired
	ControlFields cf;

	/**
	 * Método para insertar imágenes en la tabla hotelphotos. Dependiendo de los
	 * parámetros de búsqueda del postman, diferenciando si a la hora de enviar la
	 * imagen, se especifica el origen de la misma, de las siguientes formas:
	 * -ATTR_FILE_PATH : ruta (Ej: "htl_pct_file_path": "c:\\atom1.jpg")
	 * -ATTR_FILE_URL : url (Ej: "htl_pct_file_url":
	 * "http://lh5.googleusercontent.com/-UzW5aTVIdo8/JGCWrabJ6jc/s512-c/photo.jpg"
	 * -ATTR_FILE_BYTE : archivo de bytes "htl_pct_file_byte":
	 * "/9j/4AAQSkZJRgABAQAAAQABAAD/2wCEAAkGBxISEBAQEhESFRUXFxYXFxgXbD/Q3q88Sp1HnTofm/c1S7hdS....
	 */
	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult hotelPhotoInsert(Map<String, Object> data) throws OntimizeJEERuntimeException {
		EntityResult resultado = new EntityResultWrong();

		try {

			List<String> required = new ArrayList<>() {
				{
					add(dao.ATTR_NAME);
					add(dao.ATTR_HTL_ID);
					// add(dao.ATTR_FILE);
				}
			};

			List<String> restricted = new ArrayList<String>() {
				{
					add(dao.ATTR_ID);
				}
			};

			cf.reset();
			cf.setCPHtlColum(dao.ATTR_HTL_ID);
			cf.setCPRoleUsersRestrictions(UserRoleDao.ROLE_MANAGER, UserRoleDao.ROLE_STAFF);
			cf.addBasics(dao.fields);
			cf.setRequired(required);
			cf.setRestricted(restricted);
			cf.validate(data);

			Map<String, Object> subConsultaKeyMap = new HashMap<>();
			subConsultaKeyMap.put(HotelDao.ATTR_ID, data.get(dao.ATTR_HTL_ID));
			EntityResult auxEntity = hs.hotelQuery(subConsultaKeyMap, EntityResultTools.attributes(HotelDao.ATTR_ID)); // aqui
																														// se
																														// restringen
																														// por
																														// permisos
			if (auxEntity.calculateRecordNumber() == 0) { // si no hay registros, la habitación es erronea.
				throw new EntityResultRequiredException(ErrorMessage.INVALID_HOTEL_ID);
			}

			if (data.get(dao.ATTR_FILE_PATH) != null && data.get(dao.ATTR_FILE_URL) == null
					&& data.get(dao.ATTR_FILE_BYTE) == null) {
				Path p = Paths.get((String) data.get(dao.ATTR_FILE_PATH));
				if (Files.exists(p)) {
					data.put(dao.ATTR_FILE, Files.readAllBytes(p));
					resultado = daoHelper.insert(this.dao, data);
					resultado.setMessage("El archivo" + p.toAbsolutePath().toString() + " cargado correctamente.");

				} else {
					resultado.setMessage("El archivo" + p.toAbsolutePath().toString() + " no existe. ");
				}
			} else if (data.get(dao.ATTR_FILE_URL) != null && data.get(dao.ATTR_FILE_PATH) == null
					&& data.get(dao.ATTR_FILE_BYTE) == null) {

				URL u = new URL((String) data.get(dao.ATTR_FILE_URL));

				InputStream openStream = u.openStream();
				int contentLength = openStream.available();
				byte[] binaryData = new byte[contentLength];
				openStream.read(binaryData);

				data.put(dao.ATTR_FILE, binaryData);
				resultado = daoHelper.insert(this.dao, data);
				resultado.setMessage("Archivo cargado correctamente .");
			} else if (data.get(dao.ATTR_FILE_BYTE) != null && data.get(dao.ATTR_FILE_URL) == null
					&& data.get(dao.ATTR_FILE_PATH) == null) {

				byte[] bis = Base64.getDecoder().decode((String) data.get(dao.ATTR_FILE_BYTE));

				data.put(dao.ATTR_FILE, bis);
				resultado = daoHelper.insert(this.dao, data);
				resultado.setMessage("Archivo cargado correctamente.");
			} else {
				resultado.setMessage("El archivo no existe. ");
			}

		} catch (ValidateException e) {
			resultado = e.getEntityResult();
		} catch (DuplicateKeyException e) {
			resultado.setMessage("El nombre introducido " + data.get(dao.ATTR_NAME) + " ya tiene foto asociada");
		} catch (EntityResultRequiredException e) {
			resultado.setMessage(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			resultado = new EntityResultWrong(ErrorMessage.ERROR);
		}

		return resultado;
	}

	/**
	 * Método para visualizar las imágenes. ResponseEntity representa la respuesta
	 * HTTP completa: código de estado, encabezados y cuerpo. Podemos usarlo para
	 * configurar completamente la respuesta HTTP. ResponseEntity proporciona dos
	 * interfaces de construcción anidadas: HeadersBuilder y su subinterfaz,
	 * BodyBuilder. Podemos acceder a sus capacidades a través de sus métodos
	 * estáticos.
	 */
	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public ResponseEntity getHotelPictureQuery(Map<String, Object> filter, List<String> columns) {
		ResponseEntity resultado = ResponseEntity.ok(new EntityResultWrong());
		EntityResult resultadoER = new EntityResultWrong();

		try {

			cf.reset();
			cf.addBasics(dao.fields);
			// cf.setRequired(List.of(dao.ATTR_ID));
			cf.setCPHtlColum(dao.ATTR_HTL_ID);
			cf.setCPRoleUsersRestrictions(UserRoleDao.ROLE_MANAGER, UserRoleDao.ROLE_STAFF);

			cf.validate(filter);
			cf.validate(columns);

			Map<String, Object> consultaKeyMap = new HashMap<>() {
				{
					put(dao.ATTR_ID, filter.get(dao.ATTR_ID));
				}
			};
			cf.reset();
			cf.setCPHtlColum(dao.ATTR_HTL_ID);
			cf.setCPRoleUsersRestrictions(UserRoleDao.ROLE_MANAGER, UserRoleDao.ROLE_STAFF);

			Map<String, Object> consultaKeyMap2 = new HashMap<>() {
				{
					put(HotelDao.ATTR_ID, filter.get(dao.ATTR_HTL_ID));
				}
			};

			if (hotelPhotoQuery(consultaKeyMap, List.of(dao.ATTR_ID)).calculateRecordNumber() > 0
					&& hs.hotelQuery(consultaKeyMap2, List.of(HotelDao.ATTR_ID)).calculateRecordNumber() > 0) {

				resultadoER = this.daoHelper.query(dao, filter, columns);
				BytesBlock bytes = (BytesBlock) resultadoER.getRecordValues(0).get(dao.ATTR_FILE);
				HttpHeaders header = new HttpHeaders();
				header.setContentType(MediaType.IMAGE_JPEG);
				String pictureName = "picture.jpg";
				header.setContentDispositionFormData(pictureName, pictureName);
				header.setCacheControl("must-revalidate, post-check=0, pre-check=0");
				resultado = new ResponseEntity(bytes.getBytes(), header, HttpStatus.OK);

			} else {
				filter.put(dao.ATTR_ID, "1"); // Poner en la tabla, el identificador 1, la foto de file not found
				resultadoER = this.daoHelper.query(dao, filter, columns);
				BytesBlock bytes = (BytesBlock) resultadoER.getRecordValues(0).get(dao.ATTR_FILE);
				HttpHeaders header = new HttpHeaders();
				header.setContentType(MediaType.IMAGE_JPEG);
				String pictureName = "picture.jpg";
				header.setContentDispositionFormData(pictureName, pictureName);
				header.setCacheControl("must-revalidate, post-check=0, pre-check=0");
				resultado = new ResponseEntity(bytes.getBytes(), header, HttpStatus.OK);
			}

		} catch (ValidateException e) {
			resultado = ResponseEntity.ok(e.getEntityResult());
		} catch (Exception e) {
			e.printStackTrace();
			resultado = ResponseEntity.ok(new EntityResultWrong(ErrorMessage.UNKNOWN_ERROR));
		}
		return resultado;
	}

	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult hotelPhotoDelete(Map<String, Object> filter) throws OntimizeJEERuntimeException {

		EntityResult resultado = new EntityResultWrong();
		try {
			cf.reset();
			cf.addBasics(dao.fields);
			cf.setRequired(List.of(dao.ATTR_ID));
			cf.setCPHtlColum(dao.ATTR_HTL_ID);
			cf.setCPRoleUsersRestrictions(UserRoleDao.ROLE_MANAGER, UserRoleDao.ROLE_STAFF);

			cf.validate(filter);
			Map<String, Object> consulta = new HashMap<>();
			consulta.put(dao.ATTR_ID, filter.get(dao.ATTR_ID));

			if (daoHelper.query(dao, consulta, List.of(dao.ATTR_ID)).calculateRecordNumber() > 0) {
				resultado = daoHelper.delete(dao, filter);
				resultado.setMessage("Foto con identificador : " + filter.get(dao.ATTR_ID) + ". borrada");
			} else {
				resultado = new EntityResultWrong(ErrorMessage.DELETE_ERROR_MISSING_FIELD);
			}
		} catch (ValidateException e) {
			resultado = e.getEntityResult();
		} catch (Exception e) {
			e.printStackTrace();
			resultado = new EntityResultWrong(ErrorMessage.ERROR);
		}

		return resultado;
	}

	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult hotelPhotoQuery(Map<String, Object> filter, List<String> columns)
			throws OntimizeJEERuntimeException {
		EntityResult resultado = new EntityResultWrong();

		try {
			cf.reset();
			cf.addBasics(dao.fields);
			// cf.setRequired(List.of(dao.ATTR_ID));
			cf.setCPHtlColum(dao.ATTR_HTL_ID);
			cf.setCPRoleUsersRestrictions(UserRoleDao.ROLE_MANAGER, UserRoleDao.ROLE_STAFF);
			cf.validate(filter);
			cf.validate(columns);

			resultado = daoHelper.query(dao, filter, columns);
		} catch (ValidateException e) {
			resultado = e.getEntityResult();
		} catch (Exception e) {
			e.printStackTrace();
			resultado = new EntityResultWrong(ErrorMessage.ERROR);
		}

		return resultado;

	}

}
