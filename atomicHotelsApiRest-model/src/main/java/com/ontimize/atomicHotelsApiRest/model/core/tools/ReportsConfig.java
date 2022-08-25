package com.ontimize.atomicHotelsApiRest.model.core.tools;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public abstract class ReportsConfig {
	//rutas
	public static final String PATH_LOGO_ATOM_01 = "..\\atomicHotelsApiRest-model\\src\\main\\resources\\reports\\images\\logo_atom_01.png";
	public static final String PATH_LOGO_ATOM_02 = "..\\atomicHotelsApiRest-model\\src\\main\\resources\\reports\\images\\logo_atom_02.png";
	public static final String PATH_ICON_ATOM = "..\\atomicHotelsApiRest-model\\src\\main\\resources\\reports\\images\\icono_atom.png";
    public static final String PATH_ATOM_WHITE03="..\\atomicHotelsApiRest-model\\src\\main\\resources\\reports\\images\\logo atom_white3.png";
    public static final String PATH_MAP="..\\atomicHotelsApiRest-model\\src\\main\\resources\\reports\\images\\mapaHotels.png";
	//textos
	public static final String TITLE = "Atom Hotels";
	public static final String SUBTITLE = "Hotel Group";



	public static Map<String, Object> getBasicParameters() {

		Map<String, Object> result = new HashMap<>();

		result.put("title", TITLE);
		result.put("subtitle", SUBTITLE);
		result.put("logo_01", getImage(PATH_LOGO_ATOM_01));
		result.put("logo_02", getImage(PATH_LOGO_ATOM_02));
		result.put("icon", getImage(PATH_ICON_ATOM));
        result.put("fondo", getImage(PATH_ATOM_WHITE03));
        result.put("mapa", getImage(PATH_MAP));
		return result;
	}

	public static Map<String, Object> getBasicParametersPutAll(Map<String, Object> extra){
		Map<String, Object> result = getBasicParameters();
		result.putAll(extra);
		return result;
	}
	private static ByteArrayInputStream getImage(String path) {		
		try {
			return new ByteArrayInputStream(Files.readAllBytes(Paths.get(path)));
		} catch (IOException e) {			
			e.printStackTrace();
			return null;
		}
	}
}
