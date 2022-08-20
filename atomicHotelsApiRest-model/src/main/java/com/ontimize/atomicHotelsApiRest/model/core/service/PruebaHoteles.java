package com.ontimize.atomicHotelsApiRest.model.core.service;

public class PruebaHoteles {
	
	public Integer ID;
	public String NAME;
	public String CITY;
	
	public PruebaHoteles() {}
	
	public PruebaHoteles(Integer ID, String NAME, String CITY) {
		this.ID=ID;
		this.NAME=NAME;
		this.CITY=CITY;
	}

	public Integer getID() {
		return ID;
	}

	public void setID(Integer iD) {
		ID = iD;
	}

	public String getNAME() {
		return NAME;
	}

	public void setNAME(String nAME) {
		NAME = nAME;
	}

	public String getCITY() {
		return CITY;
	}

	public void setCITY(String cITY) {
		CITY = cITY;
	}
	

}
