package com.ontimize.atomicHotelsApiRest.model.core.service;



import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.ontimize.atomicHotelsApiRest.api.core.service.IPictureService;
import com.ontimize.atomicHotelsApiRest.model.core.dao.PictureDao;
import com.ontimize.atomicHotelsApiRest.model.core.tools.ControlFields;
import com.ontimize.atomicHotelsApiRest.model.core.tools.EntityResultWrong;
import com.ontimize.atomicHotelsApiRest.model.core.tools.ErrorMessage;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.exceptions.OntimizeJEERuntimeException;
import com.ontimize.atomicHotelsApiRest.api.core.exceptions.ValidateException;
import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;

@Service("PictureService")
@Lazy

public class PictureService implements IPictureService {

	@Autowired
	private PictureDao pictureDao;
	@Autowired
	private DefaultOntimizeDaoHelper daoHelper;

	@Autowired
	ControlFields cf;

//	private Image imageCoverter(byte[] bytes) throws IOException {
//		
//		
//		  ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
//		    Iterator readers = ImageIO.getImageReadersByFormatName("jpeg");    
//		    ImageReader reader = (ImageReader) readers.next();
//		    Object source = bis;
//		    ImageInputStream iis = ImageIO.createImageInputStream(source);
//		    reader.setInput(iis, true);
//		    ImageReadParam param = reader.getDefaultReadParam();
//		    return reader.read(0, param);
//		
//		return null;
//		
//	}


	@Override
	public EntityResult pictureQuery(Map<String, Object> keyMap, List<String> attrList)
			throws OntimizeJEERuntimeException {
		// TODO Esbozo de método generado automáticamente
		return null;
	}

	@Override
	public EntityResult pictureInsert(Map<String, Object> data) throws OntimizeJEERuntimeException {
		EntityResult resultado = new EntityResultWrong();
		
		try {
		Path p = Paths.get((String) data.get(pictureDao.ATTR_FILE));
		cf.reset();	
		cf.addBasics(pictureDao.fields);
		List<String> required=new ArrayList<>(){
			{
				add(pictureDao.ATTR_NAME);
				add(pictureDao.ATTR_FILE);
			}
			};
		cf.setRequired(required);
		cf.validate(data);
		
		
		
			if (Files.exists(p)) {
				data.put(pictureDao.ATTR_FILE, Files.readAllBytes(p));
				resultado = daoHelper.insert(pictureDao, data);
			} else {
				resultado.setMessage("El archivo" + p.toAbsolutePath().toString() + "no existe. ");
			}
		}catch (ValidateException e) {
			e.getMessage();
			resultado=new EntityResultWrong(e.getMessage());
			
		} catch (Exception e) {
			e.printStackTrace();
			resultado=new EntityResultWrong(ErrorMessage.ERROR);
		}

		return resultado;
	}

	@Override
	public EntityResult pictureUpdate(Map<String, Object> attrMap, Map<String, Object> keyMap)
			throws OntimizeJEERuntimeException {
		// TODO Esbozo de método generado automáticamente
		return null;
	}

	@Override
	public EntityResult pictureDelete(Map<String, Object> keyMap) throws OntimizeJEERuntimeException {
		// TODO Esbozo de método generado automáticamente
		return null;
	}

}
