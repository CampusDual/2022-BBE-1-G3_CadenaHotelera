package com.ontimize.atomicHotelsApiRest.model.core.service;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.ArrayList;
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
import javax.swing.JOptionPane;

import org.apache.poi.ss.util.ImageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
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
import com.ontimize.atomicHotelsApiRest.model.core.tools.ControlFields;
import com.ontimize.atomicHotelsApiRest.model.core.tools.EntityResultWrong;
import com.ontimize.atomicHotelsApiRest.model.core.tools.ErrorMessage;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.exceptions.OntimizeJEERuntimeException;
import com.ontimize.jee.common.security.PermissionsProviderSecured;
import com.ontimize.jee.common.util.remote.BytesBlock;
import com.lowagie.text.pdf.codec.Base64.InputStream;
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
	
	
	@Override
//	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult hotelPhotoInsert(Map<String, Object> data) throws OntimizeJEERuntimeException {
		EntityResult resultado = new EntityResultWrong();

		try {
			Path p = Paths.get((String) data.get(dao.ATTR_FILE));
			
			List<String> required = new ArrayList<>() {
				{
					add(dao.ATTR_NAME);
					add(dao.ATTR_FILE);
				}
			};
			
			List<String> restricted = new ArrayList<String>() {
				{
					add(dao.ATTR_ID);
				}
			};
			
			cf.reset();
			cf.addBasics(dao.fields);
			cf.setRequired(required);
			cf.setRestricted(restricted);
			cf.validate(data);
	
			if (Files.exists(p)) {
		    	data.put(dao.ATTR_FILE, Files.readAllBytes(p));
				resultado = daoHelper.insert(this.dao, data);
				resultado.setMessage("El archivo" + p.toAbsolutePath().toString() + " cargado correctamente para "+data.get(dao.ATTR_FILE)+"." );
			} else {
				resultado.setMessage("El archivo" + p.toAbsolutePath().toString() + " no existe. ");
			}

		} catch (ValidateException e) {
			e.getMessage();
			resultado = new EntityResultWrong(e.getMessage());
			
		}catch (DuplicateKeyException e) {
			resultado.setMessage("El nombre introducido "+data.get(dao.ATTR_NAME)+" ya tiene foto asociada");
		} catch (Exception e) {
			e.printStackTrace();
			resultado = new EntityResultWrong(ErrorMessage.ERROR);
		}
		
		return resultado;
	}
	
	
	@Override
//	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult hotelPhoto2Insert(Map<String, Object> data) throws OntimizeJEERuntimeException {
		EntityResult resultado = new EntityResultWrong();

		try {

			List<String> required = new ArrayList<>() {
				{
					add(dao.ATTR_NAME);
					add(dao.ATTR_FILE);
				}
			};
			
			List<String> restricted = new ArrayList<String>() {
				{
					add(dao.ATTR_ID);
				}
			};
			
			cf.reset();
			cf.addBasics(dao.fields);
			cf.setRequired(required);
			cf.setRestricted(restricted);
			cf.validate(data);


			try{
		        File archivo = (File) data.get(dao.ATTR_FILE);
		//		 File archivo = new File("c:\\atom1.jpg");
		        byte[] imgFoto = new byte[(int)archivo.length()];
		        FileInputStream inte = new FileInputStream(archivo);
		        inte.read(imgFoto);
		     
		        BufferedImage image = null;
		        ByteArrayInputStream in = new ByteArrayInputStream(imgFoto);
		        image = ImageIO.read(in);

		        ImageIcon icono = new ImageIcon(image);
		        JOptionPane.showMessageDialog(null, "Imagenes", "Imagen", JOptionPane.INFORMATION_MESSAGE, icono);
		    }catch(Exception ex){
		        System.out.println(ex.getMessage());
		    }


		} catch (ValidateException e) {
			e.getMessage();
			resultado = new EntityResultWrong(e.getMessage());
			
		}catch (DuplicateKeyException e) {
			resultado.setMessage("El nombre introducido "+data.get(dao.ATTR_NAME)+" ya tiene foto asociada");
		} catch (Exception e) {
			e.printStackTrace();
			resultado = new EntityResultWrong(ErrorMessage.ERROR);
		}
		
		return resultado;
	}
	
	@Override
//	@Secured({ PermissionsProviderSecured.SECURED })
	public ResponseEntity getHotelPictureQuery(Map<String, Object> filter, List<String> columns) {
		EntityResult resultado = new EntityResultWrong();

		Map<String, Object> consultaKeyMap = new HashMap<>() {
			{
				put(dao.ATTR_ID, filter.get(dao.ATTR_ID));
			}
		};
		Map<String, Object> consultaKeyMap2 = new HashMap<>() {
			{
				put(HotelDao.ATTR_ID, filter.get(dao.ATTR_ID));
			}
		};

		if (hotelPhotoQuery(consultaKeyMap, List.of(dao.ATTR_ID)).calculateRecordNumber() > 0
				&& hs.hotelQuery(consultaKeyMap2, List.of(HotelDao.ATTR_ID)).calculateRecordNumber() > 0) {

			resultado = this.daoHelper.query(dao, filter, columns);
			BytesBlock bytes = (BytesBlock) resultado.getRecordValues(0).get(dao.ATTR_FILE);
			HttpHeaders header = new HttpHeaders();
			header.setContentType(MediaType.IMAGE_JPEG);
			String pictureName = "picture.jpg";
			header.setContentDispositionFormData(pictureName, pictureName);
			header.setCacheControl("must-revalidate, post-check=0, pre-check=0");
			return new ResponseEntity(bytes.getBytes(), header, HttpStatus.OK);

		} else {
			filter.put(dao.ATTR_ID, "1");	//Poner en la tabla, el identificador 1, la foto de file not found
			resultado = this.daoHelper.query(dao, filter, columns);
			BytesBlock bytes = (BytesBlock) resultado.getRecordValues(0).get(dao.ATTR_FILE);
			HttpHeaders header = new HttpHeaders();
			header.setContentType(MediaType.IMAGE_JPEG);
			String pictureName = "picture.jpg";
			header.setContentDispositionFormData(pictureName, pictureName);
			header.setCacheControl("must-revalidate, post-check=0, pre-check=0");
			return new ResponseEntity(bytes.getBytes(), header, HttpStatus.OK);
		}
	}

	@Override
//	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult hotelPhotoDelete(Map<String, Object> filter) throws OntimizeJEERuntimeException {
		
	EntityResult resultado= new EntityResultWrong();
	try {
		cf.reset();
	cf.addBasics(dao.fields);	
	cf.setRequired(List.of(dao.ATTR_ID));
	cf.validate(filter);
	Map<String,Object> consulta=new HashMap<>();
	consulta.put(dao.ATTR_ID,filter.get(dao.ATTR_ID));
	
	if(daoHelper.query(dao, consulta,List.of(dao.ATTR_ID)).calculateRecordNumber()>0) {
	resultado=daoHelper.delete(dao, filter);
	resultado.setMessage("Foto con identificador : "+filter.get(dao.ATTR_ID)+". borrada");
	}else {
		resultado=new EntityResultWrong(ErrorMessage.DELETE_ERROR_MISSING_FIELD);
	}
	} catch (ValidateException e) {
		e.getMessage();
		resultado = new EntityResultWrong(e.getMessage());
	} catch (Exception e) {
		e.printStackTrace();
		resultado = new EntityResultWrong(ErrorMessage.ERROR);
	}
	
	return resultado;
	}



	@Override
	//@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult hotelPhotoQuery(Map<String, Object>filter, List<String> columns) throws OntimizeJEERuntimeException {
		 EntityResult resultado=new EntityResultWrong();
		 
		 try {
		 cf.reset();
		 cf.addBasics(dao.fields);
	//	 cf.setRequired(List.of(dao.ATTR_ID));
		 cf.validate(filter);
		 cf.validate(columns);
		 resultado=daoHelper.query(dao, filter, columns);
		 } catch (ValidateException e) {
				e.getMessage();
				resultado = new EntityResultWrong(e.getMessage());

			} catch (Exception e) {
				e.printStackTrace();
				resultado = new EntityResultWrong(ErrorMessage.ERROR);
			}
		 
		 return resultado;
		
	}

}
