package com.ontimize.atomicHotelsApiRest.model.core.service;


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;

import java.io.FileInputStream;

import java.io.IOException;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.ontimize.atomicHotelsApiRest.api.core.service.IPictureService;
import com.ontimize.atomicHotelsApiRest.model.core.dao.PictureDao;
import com.ontimize.atomicHotelsApiRest.model.core.tools.ControlFields;
import com.ontimize.atomicHotelsApiRest.model.core.tools.EntityResultWrong;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.exceptions.OntimizeJEERuntimeException;
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
//    public  byte[]  savePicture(String ruta) {
//        FileInputStream fis = null;
//        try {
//             File file = new File(ruta);
//             fis = new FileInputStream(file);
//             fis.read();
//             Str
//            
//             pstm.setBinaryStream(3, fis,(int) file.length());
//             pstm.execute();
//             pstm.close();
//             return true;
//        } catch (FileNotFoundException e) {
//             System.out.println(e.getMessage());
//        } catch (SQLException e) {
//            System.out.println(e.getMessage());
//        } finally {
//            try {
//               fis.close();
//             } catch (IOException e) {
//               System.out.println(e.getMessage());
//             }
//        }
//        return false;
//   }
	
	
	
	
	
	@Override
	public EntityResult pictureQuery(Map<String, Object> keyMap, List<String> attrList)
			throws OntimizeJEERuntimeException {
		// TODO Esbozo de método generado automáticamente
		return null;
	}

	@Override
	public EntityResult pictureInsert(Map<String, Object> data) throws OntimizeJEERuntimeException {
		EntityResult resultado = new EntityResultWrong();

		BufferedInputStream bin=null;
		BufferedOutputStream bout=null;
		Path p = Paths.get((String) data.get(pictureDao.ATTR_FILE));
		System.out.println(p.toAbsolutePath().toString());
		try {
			
			bin=new BufferedInputStream(new FileInputStream(p.toString()));
			bin.read();
			bin.transferTo(bout);
			bin.close();
		} catch (IOException e) {

			e.printStackTrace();
		}
		data.put(pictureDao.ATTR_FILE, bout);
		System.out.println(bout);
		resultado = daoHelper.insert(pictureDao, data);

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
