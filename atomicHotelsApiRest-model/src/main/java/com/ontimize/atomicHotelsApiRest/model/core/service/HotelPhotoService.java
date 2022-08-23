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
import com.ontimize.atomicHotelsApiRest.model.core.tools.TypeCodes.type;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.exceptions.OntimizeJEERuntimeException;
import com.ontimize.jee.common.security.PermissionsProviderSecured;
import com.ontimize.jee.common.util.remote.BytesBlock;
import com.lowagie.text.Image;
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

			List<String> required = new ArrayList<>() {
				{
					add(dao.ATTR_NAME);
	//				add(dao.ATTR_FILE);
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
			
			if(data.get(dao.ATTR_FILE_PATH) !=null && data.get(dao.ATTR_FILE_URL) ==null && data.get(dao.ATTR_FILE_BYTE) ==null) {
				Path p = Paths.get((String) data.get(dao.ATTR_FILE_PATH));
				if (Files.exists(p)) {
			    	data.put(dao.ATTR_FILE, Files.readAllBytes(p));
					resultado = daoHelper.insert(this.dao, data);
					resultado.setMessage("El archivo" + p.toAbsolutePath().toString() + " cargado correctamente para "+data.get(dao.ATTR_FILE)+"." );
				} else {
					resultado.setMessage("El archivo" + p.toAbsolutePath().toString() + " no existe. ");
				}
			}
			/*
			 * URL u = new URL(content);

			 */
			if(data.get(dao.ATTR_FILE_URL) !=null && data.get(dao.ATTR_FILE_PATH) ==null && data.get(dao.ATTR_FILE_BYTE) ==null) {
				URL u = new URL((String) data.get(dao.ATTR_FILE_URL));
/*				int contentLength = u.openConnection().getContentLength();
				InputStream openStream = u.openStream();
				byte[] binaryData = new byte[contentLength];
*/

/*				openStream = u.openStream();
		int contentLength = openStream.available();
		byte[] binaryData = new byte[contentLength];
		openStream.read(binaryData);
*/
				
				
//			    data.put(dao.ATTR_FILE, binaryData);
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				InputStream is = null;
				try {
				  is = u.openStream ();
				  byte[] byteChunk = new byte[4096]; // Or whatever size you want to read in at a time.
				  int n;

				  while ( (n = is.read(byteChunk)) > 0 ) {
				    baos.write(byteChunk, 0, n);
				  }
				}
				catch (IOException e) {
				  System.err.printf ("Failed while reading bytes from %s: %s", u.toExternalForm(), e.getMessage());
				  e.printStackTrace ();
				  // Perform any other exception handling that's appropriate.
				}
				finally {
				  if (is != null) { is.close(); }
				}
				data.put(dao.ATTR_FILE, baos);
				
				
				resultado = daoHelper.insert(this.dao, data);
				resultado.setMessage("Archivo cargado correctamente ." );
			} else {
				resultado.setMessage("El archivo no existe. ");
			}
			if(data.get(dao.ATTR_FILE_BYTE) !=null && data.get(dao.ATTR_FILE_URL) ==null && data.get(dao.ATTR_FILE_PATH) ==null) {
/*				
				  ByteArrayInputStream bis = new ByteArrayInputStream((byte[]) data.get(dao.ATTR_FILE_BYTE));
			        Iterator<?> readers = ImageIO.getImageReadersByFormatName("jpg");
			        //ImageIO is a class containing static convenience methods for locating ImageReaders
			        //and ImageWriters, and performing simple encoding and decoding.
			 
			        ImageReader reader = (ImageReader) readers.next();
			        Object source = bis; // File or InputStream, it seems file is OK
			 
			        ImageInputStream iis = ImageIO.createImageInputStream(source);
			        //Returns an ImageInputStream that will take its input from the given Object
			 
			        reader.setInput(iis, true);
			        ImageReadParam param = reader.getDefaultReadParam();
			 
			        Image image = reader.read(0, param);
			        //got an image file
			 
			        BufferedImage bufferedImage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_RGB);
			        //bufferedImage is the RenderedImage to be written
			        Graphics2D g2 = bufferedImage.createGraphics();
			        g2.drawImage(image, null, null);
			        File imageFile = new File("C:\\newrose2.jpg");
			        ImageIO.write(bufferedImage, "jpg", imageFile);
			        //"jpg" is the format of the image
			        //imageFile is the file to be written to.
			 
			        System.out.println(imageFile.getPath());
			    }
				
*/				
				
				
				
			
		//		data.put(dao.ATTR_FILE, data.get(dao.ATTR_FILE_BYTE));
				ByteArrayInputStream bis = (ByteArrayInputStream) data.get(dao.ATTR_FILE_BYTE);
				BufferedImage imagen = ImageIO.read(bis);
		//		portada.setIcon(Imagen);
			    data.put(dao.ATTR_FILE, imagen);
				resultado = daoHelper.insert(this.dao, data);
				resultado.setMessage("Archivo cargado correctamente." );
			} else {
				resultado.setMessage("El archivo no existe. ");
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

	/*
	 * @Override
//	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult hotelPhotoInsert(Map<String, Object> data) throws OntimizeJEERuntimeException {
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
			
	
			Path p = Paths.get((String) data.get(dao.ATTR_FILE));
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
	 */
	
	@Override
//	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult hotelPhoto2Insert(Map<String, Object> data) throws OntimizeJEERuntimeException {
		EntityResult resultado = new EntityResultWrong();

		try {

			List<String> required = new ArrayList<>() {
				{
					add(dao.ATTR_NAME);
//					add(dao.ATTR_FILE);
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

			URL u = new URL((String) data.get(dao.ATTR_FILE_URL));
			int contentLength = u.openConnection().getContentLength();
			InputStream openStream = u.openStream();
			byte[] binaryData = new byte[contentLength];


		    data.put(dao.ATTR_FILE, binaryData);
			resultado = daoHelper.insert(this.dao, data);
			resultado.setMessage("Archivo cargado correctamente ." );
				
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